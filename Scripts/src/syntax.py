import re

from delphin import tdl as d_tdl
from delphin import derivation as d_derivation


def match_token_vertex(new_token, token_dict):
    # match the start vertex of any token with same start char
    start_index, end_index = -1, -1

    for t_token in token_dict.values():
        if t_token.lnk.data[0] == new_token.start_char:
            start_index = t_token.start
            break

    for t_token in token_dict.values():
        if t_token.lnk.data[1] == new_token.end_char:
            end_index = t_token.end - 1
            break

    if start_index >= 0 and end_index == -1:
        end_index = start_index

    return start_index, end_index


def covers_span(parent_node, child_node):
    return parent_node.start_token_index <= child_node.start_token_index and parent_node.end_token_index >= child_node.end_token_index


def create_span_node_map(nodes):
    span_node_map = dict()
    for node_id, node in nodes.items():
        tok_span = "%d:%d" % (node.start_token_index, node.end_token_index)
        span_node_map[tok_span] = node_id
    return span_node_map


def create_token_preterminal_node_map(nodes):
    token_preterminal_node_map = dict()
    for node in nodes.values():
        for token_id in node.token_ids:
            token_preterminal_node_map[token_id] = node.node_id

    return token_preterminal_node_map


def create_char_token_maps(tokens):
    start_char_token_map = dict()
    end_char_token_map = dict()

    # Deals with mutiple tokens with same char span but different token spans
    for token_node_id, new_token in tokens.items():
        if new_token.start_char not in start_char_token_map:
            start_char_token_map[new_token.start_char] = token_node_id
        else:
            current_start = start_char_token_map[new_token.start_char]
            if tokens[current_start].index > new_token.index:
                start_char_token_map[new_token.start_char] = token_node_id

        if new_token.end_char not in end_char_token_map:
            end_char_token_map[new_token.end_char] = token_node_id
        else:
            current_end = end_char_token_map[new_token.end_char]
            if tokens[current_end].index < new_token.index:
                end_char_token_map[new_token.end_char] = token_node_id
    
    return start_char_token_map, end_char_token_map


def normalize_parse_tree_token(token_str):
    repls = [("(", "-LRB-"),(")", "-RRB-"), ("[", "-LSB-"),("]", "-RSB-"), ("{", "-LCB-"),("}", "-RCB-")]

    for repl in repls:
        token_str = token_str.replace(*repl)
    
    return token_str


def normalize_token_str(token_str, sentence_str=""):
    token_str = token_str.replace("–","-").replace("…", "...").replace("’", "'").replace("‘", "`")

    if "\"" in sentence_str:
        token_str = token_str.replace("“","\"").replace("”", "\"")
    else:
        token_str = token_str.replace("“","``").replace("”", "''")

    return token_str


def parse_token_tfs(node_token):
    tfs = node_token.tfs
    start_char, end_char, form = -1, -1, ""

    m_form = re.search(r"\+FORM \\\"(\S+)\\\"", tfs)
    if m_form:
        form = m_form.groups()[0]
    else:
        m_carg = re.search(r"\+CARG #(\d)=\\\"(\S+)\\\"", tfs)
        if m_carg:
            ind, form = m_carg.groups()[:2]
            m_form2 = re.search(r"\+FORM #(\d)", tfs)
            if m_form2:
                if int(ind) != int(m_form2.groups()[0]):
                    print("Form not matching CARG feature")
            else:
                print("Form reference not found")
        else:
            print("No CARG or Form entry", tfs)
            
    m_from = re.search(r"\+FROM (#\d=)?\\\"(\d+)\\\"", tfs)
    if m_from:
        start_char = int(m_from.groups()[-1])

    m_to = re.search(r"\+TO (#\d=)?\\\"(\d+)\\\"", tfs)
    if m_to:
        end_char = int(m_to.groups()[-1])

    return start_char, end_char, form


def parse_node_token(node_token, sentence, token_dict=None, token_length=1, current_char=0):
    token_node_id = node_token.id

    if token_dict is not None and token_node_id in token_dict:
        tok = token_dict[token_node_id]
        tok_start_char, tok_end_char = tok.lnk.data[0], tok.lnk.data[1]
        new_tokens = []
        if tok.start == tok.end-1:
            new_tokens = [Token(token_node_id, tok)]
        else:
            # Split up multitokens
            tok_current_char = tok_start_char
            for i in range(tok.start, tok.end):
                matched = False
                for tid, ttoken in token_dict.items():
                    if ttoken.start == i and ttoken.end == i+1:
                        sub_token = Token(ttoken.id, ttoken) 
                        current_end_char = tok_current_char + len(sub_token.token_str)
                        sub_token.start_char, sub_token.end_char = tok_current_char, current_end_char
                        sub_token_str = sentence[sub_token.start_char:sub_token.end_char].strip() 
                        
                        sub_token.token_str = normalize_token_str(sub_token.token_str, sentence)

                        if sub_token_str != sub_token.token_str:
                            print("Token mismatch", sub_token_str, sub_token.token_str)
                            sub_token.token_str = sub_token_str

                        new_tokens.append(sub_token)
                        tok_current_char = current_end_char
                        matched = True
                        break
                assert matched, "Unmatched token in multitoken"

        return new_tokens
    else:
        start_char, end_char, token_str = parse_token_tfs(node_token)
        assert start_char >= 0 and end_char >= 0, "Can't parse token " + str(node_token.tfs)
        start_char = max(start_char, current_char)

        sentence_substr = sentence[start_char:end_char].strip()

        orig_token_str = normalize_token_str(token_str, sentence)
        is_matching_substr = True
        if len(orig_token_str) < len(sentence_substr): 
            is_matching_substr = False
            if orig_token_str.lower() in sentence_substr.lower():
                offset = sentence_substr.lower().index(orig_token_str.lower())
                start_char += offset
                end_char = start_char + len(orig_token_str)
                is_matching_substr = True
            #else:
            #    print("Token Mismatch", orig_token_str, sentence_substr)

        token_str = sentence[start_char:end_char]

        if token_length == 1:
            new_token = Token(token_node_id)
            new_token.start_char, new_token.end_char, new_token.token_str = start_char, end_char, token_str
            return [new_token]
        else:
            offset = 0
            new_tokens = []
            new_token_id = token_node_id * 10
            while offset < len(token_str):
                if "-" in token_str[offset:]:
                    end = token_str.index("-", offset) + 1
                else:
                    end = len(token_str)
                new_token_id = new_token_id * 10
                new_token_str = token_str[offset:end]
                new_token = Token(new_token_id)
                new_token.start_char, new_token.end_char, new_token.token_str = start_char+offset, start_char+end, new_token_str
                offset = end 
                new_tokens.append(new_token)
            if (not is_matching_substr) and len(token_str.split("-")) != token_length: # should be fine
                print("Unmatched multi-token length", token_length, token_str, orig_token_str, sentence_substr)
            return new_tokens


class Lexicon():
    def __init__(self, erg_path):
        self.lex = {} # map syntactic name (in tree) to lexical entry
        self.read_lexicon(erg_path)

    def read_lexicon(self, erg_path):
        #TODO extend to do more complex things
        try:
            for event, obj, _ in d_tdl.iterparse(erg_path + "/lexicon.tdl"):
                if event == 'TypeDefinition':
                    syntactic_name = obj.identifier
                    lexical_type = str(obj.conjunction.terms[0])
                    lemma = " ".join(map(str, obj['ORTH'].values())) 
                    if "SYNSEM" in obj: # else doesn't really matter
                        semantic_predicate, carg = None, None
                        if "LKEYS.KEYREL.PRED" in obj["SYNSEM"]:
                            semantic_predicate = str(obj["SYNSEM"]["LKEYS.KEYREL.PRED"])
                        if "LKEYS.KEYREL.CARG" in obj["SYNSEM"]:
                            carg = str(obj["SYNSEM"]["LKEYS.KEYREL.CARG"])
                        self.lex[syntactic_name] = (lemma, lexical_type, semantic_predicate, carg)
        except FileNotFoundError:
            return

    def get_lexical_type(self, name):
        if name in self.lex:
            return self.lex[name][1]
        else:
            return None


class Node():
    def __init__(self, node_id, syntax_label, start_token_index, end_token_index):
        self.node_id = node_id
        self.syntax_labels = [syntax_label]
        self.phrase_labels = []
        self.semantic_nodes = [] #TODO need to store indices here to make life easier

        self.semantic_parent_node = -1
        self.semantic_parent_edge_label = ""
        
        self.child_node_ids = []
        self.overlapping_node_ids = []

        self.start_token_index = start_token_index
        self.end_token_index = end_token_index
        self.isToken = False
        self.token_form = ""
        self.token_ids = []

    def __str__(self):
        #return str([str(snode) for snode in self.semantic_nodes])
        return "%d:%d %s %s children:%d tokens: %s" % (self.start_token_index, self.end_token_index, str(self.syntax_labels), str(self.semantic_nodes), len(self.child_node_ids), str(self.token_ids))


class Token():
    def __init__(self, token_id, tok=None):
        self.token_id = token_id
        if tok is not None:
            self.token_str = tok.form
            self.index = tok.start
            self.start_char = tok.lnk.data[0]
            self.end_char = tok.lnk.data[1]
        else:
            self.token_str = ""
            self.index = -1
            self.start_char = -1
            self.end_char = -1 # exclusive
        #self.unmatchedToken = False
        self.lemma = ""
        self.carg = ""
        self.is_unknown = False

    def __str__(self):
        return "%d %s" % (self.index, self.token_str)


class SyntacticRepresentation():
    def __init__(self, sid, sentence, derivation_rep, token_dict=None, lexicon=None):
        self.sid = sid
        self.sentence = sentence.replace("–","-")
        self.token_dict = token_dict
        self.nodes = dict()
        self.tokens = dict()
        self.token_sequence = []
        self.lexicon = lexicon

        self.root_node_id = self.build_derivation_tree(derivation_rep)

        self.span_node_map = create_span_node_map(self.nodes)
        self.token_preterminal_node_map = create_token_preterminal_node_map(self.nodes)

        self.normalize_token_span_strs()
        self.start_char_token_map, self.end_char_token_map = create_char_token_maps(self.tokens)


    def build_derivation_tree(self, drv_node):
        new_node = Node(drv_node.id, drv_node.entity, drv_node.start, drv_node.end-1)

        # Collapse unary chains
        while type(drv_node) != d_derivation.UDFTerminal and len(drv_node.daughters) == 1:
            drv_node = drv_node.daughters[0]
            if type(drv_node) != d_derivation.UDFTerminal:
                assert drv_node.start == new_node.start_token_index and drv_node.end - 1 == new_node.end_token_index, "unary syntactic child has different span than parent"
                new_node.syntax_labels.append(drv_node.entity)
       
        if type(drv_node) == d_derivation.UDFTerminal:
            # Assign tokens to syntax nodes
            new_node.isToken = True
            new_node.token_form = drv_node.form
            if len(self.token_sequence) == 0:
                current_char = 0
            else:
                current_char = self.tokens[self.token_sequence[-1]].end_char

            for node_token in drv_node.tokens:
                assert len(self.token_sequence) >= new_node.start_token_index and len(self.token_sequence) <= new_node.end_token_index, str(new_node) + " " + str(len(self.token_sequence))
                token_length = new_node.end_token_index + 1 - len(self.token_sequence)
                new_tokens = parse_node_token(node_token, self.sentence, token_length=token_length, current_char=current_char)

                for new_token in new_tokens:
                    new_node.token_ids.append(new_token.token_id)
                    new_token.index = len(self.token_sequence)
                    self.token_sequence.append(new_token.token_id)
                    self.tokens[new_token.token_id] = new_token
                    current_char = new_token.end_char
        else:
            # Recursively traverse children
            assert len(drv_node.daughters) == 2, "non-binary tree: " + sentence_id
            for child in drv_node.daughters:
                child_id = self.build_derivation_tree(child)
                new_node.child_node_ids.append(child_id)
            
        self.nodes[new_node.node_id] = new_node
        return new_node.node_id 

    def tokens_str(self):
        s = ""
        for i, tid in enumerate(self.token_sequence):
            tok = self.tokens[tid]
            s += str(tok.index) + ":" + str(tok.start_char) + "-" + str(tok.end_char) + " " + tok.token_str + "  "
        return s

    def normalize_token_span_strs(self, update_token_spans=True):
        multi_span = False
        current_char = -1

        for i, tid in enumerate(self.token_sequence):
            if i == 0:
                current_token = self.tokens[tid]
            else:
                current_token = next_token

            if i < len(self.token_sequence) - 1:
                next_tid = self.token_sequence[i+1]
                next_token = self.tokens[next_tid]

            init_token_str = current_token.token_str
            sentence_str = self.sentence[current_token.start_char:current_token.end_char].strip()
            current_token.token_str = normalize_token_str(current_token.token_str, sentence_str)

            if i < len(self.token_sequence) - 1 and not multi_span:
                if current_token.end_char > next_token.start_char:
                    multi_span = True
                    span_end_char = current_token.end_char
                    if sentence_str.lower().startswith(current_token.token_str.lower()):
                        end_char = current_token.start_char + len(current_token.token_str)
                        if update_token_spans:
                            current_token.end_char = end_char
                        current_char = end_char
                    #else:
                    #    print("Multispan not matching", sentence_str, current_token.token_str)
                else:
                    current_char = current_token.end_char
                    current_token.token_str = sentence_str 

            elif multi_span:
                sentence_str = self.sentence[current_char:current_token.end_char].strip()
                if sentence_str.lower().startswith(current_token.token_str.lower()):
                    end_char = current_char + len(current_token.token_str)
                    if update_token_spans:
                        current_token.start_char = current_char
                        current_token.end_char = end_char
                    current_char = end_char
                #else:
                #    print("Multispan not matching", sentence_str, current_token.token_str)

                if not current_token.end_char > next_token.start_char:
                    multi_span = False
            else:
                current_char = current_token.end_char
                current_token.token_str = sentence_str 


    def derivation_tree_str(self, node_id, level=0, newline=False):
        node = self.nodes[node_id]
        out_str = "\n" + " "*level if newline else ""

        unary_count = 0 
        for label in node.syntax_labels:
            if label.endswith("_c") and not (len(node.token_ids) > 0 and label == node.syntax_labels[-1]):
                out_str += "(" + label + " " + str(node.start_token_index) + " " + str(node.end_token_index) + " " #TODO indexes temp
                unary_count += 1
            else:
                break

        out_str += " ".join([self.derivation_tree_str(child_id, level+4, newline) for child_id in node.child_node_ids])

        out_str += " ".join(['(X ' + normalize_parse_tree_token(self.tokens[token_id].token_str) + ')' for token_id in node.token_ids])

        out_str += ")"*unary_count
        return out_str


    def supertag_str(self, node_id):
        tags, words = self.supertag_tuple(node_id)
        tag_str = ""
        for tag, word in zip(tags, words):
            #n_word = [normalize_parse_tree_token(w) for w in word] # don't use for now
            tag_str += "[" + ";".join(tag) + "] " + " ".join(word) + " "
        return tag_str.strip()


    def supertag_tuple(self, node_id):
        node = self.nodes[node_id]

        tag = []
        tag_state = False
        for label in node.syntax_labels:
            if tag_state or (not label.endswith("_c")) or (len(node.token_ids) > 0 and label == node.syntax_labels[-1]):
                tag_state = True
                tag.append(label)

        if len(tag) > 0:
            lex_type = self.lexicon.get_lexical_type(tag[-1])
            if lex_type is not None:
                tag[-1] = lex_type
            elif tag[-1].endswith("_disc_adv"):
                tag[-1] = "disc_adv"
            elif tag[-1].startswith("punct_ellipsis_"):
                tag[-1] = "punct_ellipsis"
            elif not tag[-1].startswith("generic"):
                print("Unknown word tag", tag[-1])

        assert not (len(tag) > 0 and len(node.child_node_ids) > 0), tag

        tags = []
        words = []

        for child_id in node.child_node_ids:
            child_tags, child_words = self.supertag_tuple(child_id)
            tags.extend(child_tags)
            words.extend(child_words)

        word = [self.tokens[token_id].token_str for token_id in node.token_ids]
        if len(word) > 0:
            assert len(tag) > 0, word
            tags = [tag]
            words = [word]

        return tags, words



