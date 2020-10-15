import difflib
import json
import re

from delphin import predicate as d_predicate

import syntax

def text_to_digits(t):
    digit_map = {"zero":0, "one":1, "two":2, "three":3, "four":4, "five":5, "six":6, "seven":7, "eight":8, "nine":9, "ten":10, "eleven":11, "twelve":12, "thirteen":13, "fourteen":14, "fifteen":15, "sixteen":16, "seventeen":17, "eighteen":18, "nineteen":19, "twenty":20, "thirty":30, "forty":40, "fifty":50, "sixty":60, "seventy":70, "eighty":80, "ninety":90, "hundred":100, "thousand":1000, "million":1000000, "billion":1000000000, "trillion":1000000000000, "second":2, "third":3, "fifth":5, "eighth":8, "ninth":9, "half":"1/2"}
    if t in digit_map:
        return str(digit_map[t])
    elif t + "th" in digit_map:
        return str(digit_map[t+"th"])
    else:
        return t


def clean_token_lemma(tok_str, pred_is_digit=False):
    t_str = tok_str.lower().strip("-.,\"\';")
    # Manual lemmatization rules
    surface_map = {"best":"good", "better":"good", "/":"and", "is":"be", "was":"be", "km":"kilometer", "worst":"bad", "worse":"bad", "&":"and", "’s":"be", "'s":"be", "isn’t":"be", "wasn’t":"be", "%":"percent", "$":"dollar", "went":"go", ":":"colon", "are":"be", "were":"be"}
    if t_str in surface_map:
        t_str = surface_map[t_str]
    if pred_is_digit:
        t_str = text_to_digits(t_str)
    return t_str


def match_surface_predicate_token(predicate, start_token_index, end_token_index, tokens, token_sequence):
    match_prob = []
    tkns = []
    lemma, pos, sense = d_predicate.split(predicate)
    if pos == 'u':
        lemma = lemma[:lemma.rindex('/')]
    for tok_index in range(start_token_index, end_token_index+1):
        # id lookup bypasses the derivation node
        token = tokens[token_sequence[tok_index]] 
        t_str = clean_token_lemma(token.token_str, predicate.isdigit())
        tkns.append(t_str)
        seq = difflib.SequenceMatcher(a=t_str, b=lemma.lower())
        match_prob.append(seq.ratio())
    token_match = start_token_index + match_prob.index(max(match_prob))
    if max(match_prob) == 0:
        print("Predicate not matching any aligned token:", predicate, tkns)

    return token_match


class SemanticNode():
    def __init__(self, node_id, predicate, carg):
        self.node_id = node_id
        self.original_predicate = predicate
        self.predicate = predicate
        self.carg = None if carg == "" else carg
        self.lemma = None

        self.internal_parent = -1
        self.internal_child = -1
        self.internal_edge_label = ""
        self.has_ancestor = False
        self.is_semantic_terminal = False # no children, used for semantic roles
        self.is_semantic_head = False # has direct semantic children in tree
        self.is_surface = False

    def __str__(self):
        return "%s" % (self.original_predicate)
        #return "%s %s %s" % (self.original_predicate, self.carg, self.predicate)


class SemanticRepresentation(syntax.SyntacticRepresentation):
    def __init__(self, sid, sentence, derivation_rep, token_dict=None, lexicon=None):
        super().__init__(sid, sentence, derivation_rep, token_dict, lexicon)

        self.dmrs_node_map = dict()
        self.overlapping_node_map = dict()
 

    def semantic_tree_str(self, node_id, level=0, newline=False, overlapping=False):
        node = self.nodes[node_id]
        out_str = "\n" + " "*level if newline else ""
        if overlapping:
            out_str += "*" + str(node.start_token_index) + ":" + str(node.end_token_index) + " "
        out_str += "(" + str([str(s_node.node_id) + ":" + s_node.predicate for s_node in node.semantic_nodes]) + " "
        for child_id in node.overlapping_node_ids:
            out_str += self.semantic_tree_str(child_id, level+4, True, True) + " "
        for child_id in node.child_node_ids:
            join_line = len(node.overlapping_node_ids) == 0 and child_id == node.child_node_ids[0] and len(node.semantic_nodes) == 0
            out_str += self.semantic_tree_str(child_id, level+4, not join_line) + " "
        for token_id in node.token_ids:
            token = self.tokens[token_id]
            if token.is_unknown:
                out_str += "{<unk>} "
            elif token.lemma == "":
                out_str += "{<none>} "
            else:
                out_str += "{%s} " % (token.lemma)
            out_str += token.token_str + " "

        return out_str + ")"
    
    
    def dmrs_json_str(self, dmrs_rep):
        dmrs_dict = {}
        dmrs_dict["id"] = self.sid.split(":")[1]
        dmrs_dict["source"] = self.sid.split(":")[0]
        dmrs_dict["input"] = self.sentence
        dmrs_dict["tokens"] = []
        dmrs_dict["nodes"] = []
        dmrs_dict["edges"] = []
        new_rep = False

        for i, token_id in enumerate(self.token_sequence):
            token = self.tokens[token_id]
            token_entry = {"form": token.token_str, "lemma": token.token_str if token.lemma == "" else token.lemma}
            if new_rep:
                token_entry["id"] = i
            else:
                token_entry["index"] = i
            token_entry["anchors"] = [{"from": token.start_char, "end": token.end_char}]
            dmrs_dict["tokens"].append(token_entry)
            if (not new_rep) and token.carg != "":
                dmrs_dict["tokens"][-1]["carg"] = token.carg 
        
        new_node_ids = {}
        for deriv_node_id, deriv_node in self.nodes.items():
            for sem_node in deriv_node.semantic_nodes:
                new_id = len(new_node_ids)
                new_node_ids[sem_node.node_id] = new_id
                node_entry = {"id": new_id, "anchors": [{"from": deriv_node.start_token_index, "end": deriv_node.end_token_index}]}
                node_entry["label"] = sem_node.predicate if new_rep else sem_node.original_predicate
                if sem_node.is_surface:
                    if new_rep:
                        properties, values = [], []
                        if sem_node.carg is not None:
                            properties.append("CARG")
                            values.append(sem_node.carg)
                        if sem_node.lemma is not None:
                            properties.append("lemma")
                            values.append(sem_node.lemma)
                        if properties:
                            node_entry["properties"] = properties
                        if values:
                            node_entry["values"] = values
                        #if not properties and not values: #TODO edge cases
                        #    print("surface unmatched:", str(sem_node))
                    else: #TODO add now
                        node_entry["is_surface"] = True

                dmrs_dict["nodes"].append(node_entry)

        if dmrs_rep.top is None or dmrs_rep.top not in new_node_ids:
            dmrs_dict["tops"] = []
        else:
            dmrs_dict["tops"] = [new_node_ids[dmrs_rep.top]]

        for i, edge in enumerate(dmrs_rep.links):
            if not (edge.start in new_node_ids and edge.end in new_node_ids):
                print("unmatched edge", edge.role)
                continue
            edge_entry = {"id": i,  "source": new_node_ids[edge.start], "target": new_node_ids[edge.end], "label": edge.role}
            if edge.post:
                edge_entry["post-label"] = edge.post
            #if is_dmrs and edge.role == "BV":
            #        edge_entry["label"] = "RSTR"
            dmrs_dict["edges"].append(edge_entry)

        return json.dumps(dmrs_dict)


    def print_mrs(self, print_full=False, print_overlapping=False, print_multitokens=False, print_non_surface=False):
        if print_full:
            print(self.sentence)
            print(self.semantic_tree_str(meaning_representation.root_node_id))

        for node_id, node in self.nodes.items():
            if print_overlapping and len(node.overlapping_node_ids) > 0:
                 print(self.semantic_tree_str(node_id))
            if print_multitokens and len(node.token_ids) > 1:
                print(self.semantic_tree_str(node_id))
            if print_non_surface:
                has_surface = False
                for snode in node.semantic_nodes:
                    if (not node.isToken) and d_predicate.is_surface(snode.original_predicate):
                        has_surface = True
                if has_surface:
                    print(self.semantic_tree_str(node_id))


    def map_dmrs(self, dmrs_rep):
        # Map dmrs nodes to the meaning representation
        for node in dmrs_rep.nodes:
            self.map_dmrs_node(node)

        # Map overlapping nodes
        for node in dmrs_rep.nodes:
            if type(self.dmrs_node_map[node.id]) == tuple:
                self.match_overlapping_dmrs_node(node)
 

    def map_dmrs_node(self, node):
        if node.lnk.data[0] in self.start_char_token_map:
            start_node = self.start_char_token_map[node.lnk.data[0]]
        else:
            start_node = None
            for start_index in range(node.lnk.data[0] + 1, node.lnk.data[0] - 3, -1):
                if start_index in self.start_char_token_map:
                    start_node = self.start_char_token_map[start_index]
                    break
            if start_node is None:
                assert node.lnk.data[0] in self.start_char_token_map, "MRS predicate not matching any token start: " + str(node.predicate) + " " + str(node.lnk) + "\n" + self.tokens_str()

        if node.lnk.data[1] in self.end_char_token_map:
            end_node = self.end_char_token_map[node.lnk.data[1]]
        else:
            end_node = None
            for end_index in range(node.lnk.data[1] - 1, node.lnk.data[1] + 3):
                if end_index in self.end_char_token_map:
                    end_node = self.end_char_token_map[end_index]
                    break
            if end_node is None:
                assert node.lnk.data[1] in self.end_char_token_map, "MRS predicate not matching any token end: " + str(node.predicate) + " " + str(node.lnk) + "\n" + self.tokens_str()

        start_token, end_token = self.tokens[start_node].index, self.tokens[end_node].index
        span_str = "%d:%d" % (start_token, end_token)
        matched_node = False

        # match nodes (not token nodes)
        if span_str in self.span_node_map:
            node_id = self.span_node_map[span_str]
            self.nodes[node_id].semantic_nodes.append(SemanticNode(node.id, node.predicate, node.carg))
            self.dmrs_node_map[node.id] = node_id
            matched_node = True
        else:
            self.dmrs_node_map[node.id] = (-1, start_token, end_token)
        
        return matched_node


    def match_overlapping_dmrs_node(self, node):
        # Find matching parent for unmatched node
        start_token, end_token = self.dmrs_node_map[node.id][1], self.dmrs_node_map[node.id][2]
        matched = False

        for node_id, s_node in self.nodes.items():
            if s_node.start_token_index == start_token and s_node.end_token_index == end_token:
                assert s_node.syntax_labels[0] == "NULL" # overlapping node
                self.nodes[node_id].semantic_nodes.append(SemanticNode(node.id, node.predicate, node.carg))
                self.dmrs_node_map[node.id] = node_id
                matched = True
                break

        for node_id, s_node in self.nodes.items():
            if (not matched) and s_node.start_token_index <= start_token and s_node.end_token_index >= end_token and len(s_node.child_node_ids) == 2:
                #TODO are we guaranteeing to match the lowest possible node?
                left_node, right_node = self.nodes[s_node.child_node_ids[0]], self.nodes[s_node.child_node_ids[1]]   
                if left_node.end_token_index >= start_token and right_node.start_token_index <= end_token:
                    new_node_id = node.id
                    while new_node_id in self.nodes:
                        new_node_id = new_node_id*10

                    new_node = syntax.Node(new_node_id, "NULL", start_token, end_token)
                    new_node.semantic_nodes.append(SemanticNode(node.id, node.predicate, node.carg))
                    self.nodes[node_id].overlapping_node_ids.append(new_node_id)
                    self.overlapping_node_map[new_node_id] = node_id
                    self.nodes[new_node_id] = new_node
                    self.dmrs_node_map[node.id] = new_node_id
                    matched = True
                    break

        assert matched, "Not Matched %s %s %s %d %d" % (self.derivation_tree_str(self.root_node_id), str(node), node.surface, start_token, end_token) 

    def process_semantic_tree(self, node_id, dmrs_rep, semantic_parent=-1):
        node = self.nodes[node_id]
        sem_node_ids = [snode.node_id for snode in node.semantic_nodes]
        remove_sem_nodes = []
        internal_edge_from = [] # semantic node ids
        internal_edge_to = []
        internal_edge_label = []

        if node.semantic_nodes:
            semantic_anchor = node_id
            node.semantic_parent_node = semantic_parent

            for edge in dmrs_rep.links:
                start_node_id = self.dmrs_node_map[edge.start]
                end_node_id = self.dmrs_node_map[edge.end]
                if end_node_id == node_id:
                    #start_id = sem_node_ids.index(edge.start)
                    end_id = sem_node_ids.index(edge.end)
                    sem_node = node.semantic_nodes[end_id]
                    if start_node_id == node_id:
                        # record internal edge
                        internal_edge_from.append(edge.start) 
                        internal_edge_to.append(edge.end) 
                        internal_edge_label.append(edge.role) 
                        # previously recorded in the node, and test for non-chains
                    elif start_node_id == semantic_parent:
                        # record ancestor edge
                        self.nodes[node_id].semantic_nodes[end_id].has_ancestor = True
                        #assert self.nodes[node_id].semantic_parent_edge_label == ""
                        self.nodes[node_id].semantic_parent_edge_label = edge.role
                        parent_sem_node_ids = [snode.node_id for snode in self.nodes[semantic_parent].semantic_nodes]
                        parent_start_id = parent_sem_node_ids.index(edge.start)
                        self.nodes[semantic_parent].semantic_nodes[parent_start_id].is_semantic_head = True

            # identify non-token-level surface predicates to move
            #   if the node has internal children, don't move
            for sid, sem_node in enumerate(node.semantic_nodes):
                if (not node.isToken) and sem_node.node_id not in internal_edge_from:
                    token_index = -1
                    if d_predicate.is_surface(sem_node.original_predicate):
                        token_index = match_surface_predicate_token(sem_node.original_predicate, node.start_token_index, node.end_token_index, self.tokens, self.token_sequence)
                    elif sem_node.carg is not None:
                        token_index = match_surface_predicate_token(sem_node.carg, node.start_token_index, node.end_token_index, self.tokens, self.token_sequence)

                    if token_index >= 0:
                        token_id = self.token_sequence[token_index]
                        new_preterminal = self.token_preterminal_node_map[token_id]
                        self.nodes[new_preterminal].semantic_nodes.append(sem_node)
                        self.dmrs_node_map[sem_node.node_id] = new_preterminal
                        remove_sem_nodes.append(sid)
                        # follow the chain
                        # for some quantifiers, might be indended to span everything, but this seems good enough for now
                        snode_id = sem_node.node_id 
                        while snode_id in internal_edge_to:
                            new_snode_id = -1
                            for edge_i, parent_node_id in enumerate(internal_edge_from):
                                if internal_edge_to[edge_i] == snode_id and internal_edge_from.count(parent_node_id) == 1:
                                    sid = sem_node_ids.index(parent_node_id)
                                    sem_node = node.semantic_nodes[sid]
                                    self.nodes[new_preterminal].semantic_nodes.append(sem_node)

                                    self.dmrs_node_map[sem_node.node_id] = new_preterminal
                                    remove_sem_nodes.append(sid)
                                    if parent_node_id in internal_edge_to:
                                        #if new_snode_id >= 0: # almost never have 2 internal parents
                                        new_snode_id = parent_node_id
                            snode_id = new_snode_id

        else:
            semantic_anchor = semantic_parent

        for i in sorted(remove_sem_nodes, reverse=True):
            del node.semantic_nodes[i]
            
        # if current node is an overlapping node and it has nodes left, send to the spanning parent 
        # (if all the arguments of the node is covered by one of the children, should ideally send down, but not now)
        if node.node_id in self.overlapping_node_map and len(node.semantic_nodes) > 0:
            parent_node_id = self.overlapping_node_map[node.node_id]
            for i in range(len(node.semantic_nodes)-1, -1, -1):
                self.nodes[parent_node_id].semantic_nodes.append(node.semantic_nodes[i])
                del node.semantic_nodes[i]

        for child_id in node.overlapping_node_ids:
            self.process_semantic_tree(child_id, dmrs_rep, semantic_anchor)

        # For token (preterminal) nodes, extract lemmas from predicates
        if node.isToken:
            if len(node.token_ids) == 1:
                tok = self.tokens[node.token_ids[0]]
                best_lemma_match_prob = 0.0
                best_sid = -1
                best_pred = ""
                t_str = clean_token_lemma(tok.token_str)
                for sid, sem_node in enumerate(node.semantic_nodes):
                    if d_predicate.is_surface(sem_node.original_predicate):
                        sem_node.is_surface = True
                        lemma, pos, sense = d_predicate.split(sem_node.original_predicate)
                        pred = "_" + ("_".join([pos, sense]) if sense is not None else pos)
                        seq = difflib.SequenceMatcher(a=lemma, b=t_str)
                        lemma_match_prob = seq.ratio()
                        if tok.lemma == "" or lemma_match_prob > best_lemma_match_prob:
                            tok.lemma = lemma
                            best_sid = sid
                            best_pred = pred
                            best_lemma_match_prob = lemma_match_prob
                        if pred == "_u_unknown":
                            if "/" in lemma:
                                tok.lemma = lemma[:lemma.rindex("/")]
                                sem_node.original_predicate = "_" + tok.lemma + pred
                            tok.is_unknown = True
                    if sem_node.carg is not None:
                        sem_node.is_surface = True
                        if tok.carg == "":
                            tok.carg = sem_node.carg
                        # For multiple CARGs, just take first one as heuristic
                if tok.carg != "":
                    if tok.lemma == "":
                        tok.lemma = tok.carg
                    else:
                        t_str = clean_token_lemma(tok.token_str, True)
                        seq = difflib.SequenceMatcher(a=tok.carg, b=t_str)
                        carg_match_prob = seq.ratio()
                        if carg_match_prob > best_lemma_match_prob:
                            tok.lemma = tok.carg
                            best_lemma_match_prob = carg_match_prob
                #if best_lemma_match_prob < 0.5 and tok.lemma != "" and tok.lemma != tok.carg:
                #    print(tok.lemma, tok.token_str)
                if best_sid >=0 and tok.lemma != tok.carg:
                    node.semantic_nodes[best_sid].predicate = best_pred
                    node.semantic_nodes[best_sid].lemma = tok.lemma
            elif len(node.token_ids) > 1:
                matched_multi = False
                for sem_node in node.semantic_nodes:
                    if d_predicate.is_surface(sem_node.original_predicate):
                        sem_node.is_surface = True
                        lemma, pos, sense = d_predicate.split(sem_node.original_predicate)
                        if "-" in lemma:
                            lemma_split = lemma.split("-")
                            lemma_split[0] += "-"
                        else:
                            #TODO "awhile"
                            lemma_split = lemma.split("+")
                        if len(lemma_split) == len(node.token_ids):
                            pred = "_" + ("_".join([pos, sense]) if sense is not None else pos)
                            sem_node.predicate = pred
                            sem_node.lemma = lemma
                            for i, tok_id in enumerate(node.token_ids):
                                tok = self.tokens[tok_id]
                                tok.lemma = lemma_split[i]

                            matched_multi = True
                            break
                    #TODO match the carg if there is one

                if matched_multi:
                    tokstr = [self.tokens[tok_id].token_str for tok_id in node.token_ids]
                    semstr = [sem_node.original_predicate for sem_node in node.semantic_nodes]
                    #print("matched", node.token_form, tokstr, semstr)

        for child_id in node.child_node_ids:
            self.process_semantic_tree(child_id, dmrs_rep, semantic_anchor)
 

    def classify_edges(self, dmrs_rep):
        # argument is non-terminal
        for edge in dmrs_rep.links:
            if not self.nodes[self.dmrs_node_map[edge.end]].isToken:
                start_node = self.dmrs_node_map[edge.start]
                start_snode_ids = [snode.node_id for snode in self.nodes[start_node].semantic_nodes]
                start_snode = self.nodes[start_node].semantic_nodes[start_snode_ids.index(edge.start)]
                print(start_snode.predicate, edge.role)
                print(self.nodes[self.dmrs_node_map[edge.start]], self.nodes[self.dmrs_node_map[edge.end]])




