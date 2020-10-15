import sys
import os
import argparse

from delphin import tsql as d_tsql
from delphin import itsdb as d_itsdb
from delphin import dmrs as d_dmrs
from delphin import eds as d_eds
from delphin import derivation as d_derivation
from delphin import predicate as d_predicate
from delphin import tokens as d_tokens
from delphin.codecs import simplemrs as dc_simplemrs
from delphin.codecs import eds as dc_eds
from delphin import mrs as d_mrs
from delphin import predicate as d_predicate

import syntax
import semantics


def get_profile_name(dirname):
    if dirname[-1] == '/':
        dirname = dirname[:-1]
    return dirname[dirname.rindex('/')+1:]


def eds_to_dmrs(eds_rep):
    node_counter = 10000
    new_node_ids = {}
    dmrs_nodes = []
    for node in eds_rep.nodes:
        new_node_ids[node.id] = node_counter
        dmrs_nodes.append(d_dmrs.Node(node_counter, node.predicate, node.type, node.properties, node.carg, node.lnk, node.surface, node.base))
        node_counter += 1

    dmrs_links = [d_dmrs.Link(new_node_ids[edge[0]], new_node_ids[edge[2]], edge[1], "") for edge in eds_rep.edges]
    return d_dmrs.DMRS(new_node_ids[eds_rep.top], new_node_ids[eds_rep.top], dmrs_nodes, dmrs_links, eds_rep.lnk, eds_rep.surface, eds_rep.identifier)


def read_mrp(mrp_input_path):
    mrp_eds = {}
    eds_state = False
    iid = ""
    eds_str = ""

    with open(mrp_input_path, 'r') as mrp_file:
        for line in mrp_file:
            if eds_state:
                if line.strip() == "":
                    mrp_eds[iid] = eds_str
                    eds_state = False
                else:
                    eds_str += line
            else:
                if line.startswith('#'):
                    iid = line[1:].strip()
                    eds_str = ""
                    eds_state = True

    return mrp_eds


def read_profile(input_dir, output_dir, profile_name, mrp_eds, lexicon, args):
    ts = d_itsdb.TestSuite(input_dir)
 
    derivation_strs = []
    supertag_strs = []
    dmrs_json_strs = []

    for iid, sentence, parse_tokens, result_derivation, result_mrs in d_tsql.select('i-id i-input p-tokens derivation mrs', ts):
        #tokens_rep = d_tokens.YYTokenLattice.from_string(parse_tokens)
        #token_dict = {tok.id : tok for tok in tokens_rep.tokens}
        
        #print(result_derivation) # temp
        derivation_rep = d_derivation.from_string(result_derivation)
        assert len(derivation_rep.daughters) == 1 
        derivation_rep = derivation_rep.daughters[0]

        if mrp_eds:
            if iid in mrp_eds:
                try:
                    eds_rep = dc_eds.decode(mrp_eds[iid])
                    dmrs_rep = eds_to_dmrs(eds_rep)
                except d_eds._exceptions.EDSSyntaxError:
                    #print("Skipping: EDS syntax error", mrp_eds[iid])
                    continue
            else:
                    #print("Unmatched:", iid)
                    continue
        else:
            try:
                mrs_rep = dc_simplemrs.decode(result_mrs)
            except d_mrs._exceptions.MRSSyntaxError:
                #print("Skipping: MRS syntax error", result_mrs)
                continue

            dmrs_rep = d_dmrs.from_mrs(mrs_rep)

        #mr = semantics.SemanticRepresentation(profile_name + ":" + iid, sentence, derivation_rep, token_dict, lexicon) # read derivation tree
        mr = semantics.SemanticRepresentation(profile_name + ":" + iid, sentence, derivation_rep, lexicon=lexicon) # read derivation tree

        if args.convert_semantics or args.extract_semantics:
            mr.map_dmrs(dmrs_rep)
            mr.process_semantic_tree(mr.root_node_id, dmrs_rep)
            mr.print_mrs()

        if args.extract_syntax:
            derivation_strs.append(mr.derivation_tree_str(mr.root_node_id, newline=False).lstrip())
            supertag_strs.append(mr.supertag_str(mr.root_node_id).strip())

        if args.extract_semantics:
            dmrs_json_strs.append(mr.dmrs_json_str(dmrs_rep))

    if args.extract_syntax:
        with open(output_dir + ".tree", 'w') as dt_out:
            for s in derivation_strs:
                dt_out.write(s + "\n")
        with open(output_dir + ".tags", 'w') as st_out:
            for s in supertag_strs:
                st_out.write(s + "\n")

    if args.extract_semantics:
        with open(output_dir + ".dmrs", 'w') as d_out:
            for s in dmrs_json_strs:
                if s != "":
                    d_out.write(s + "\n")
 

def read_redwoods(args):
    # standard redwoods split, including only profiles recommended by Stephan
    profile_dict = {}
    #profile_dict["ecommerce"] = [["ecoc", "ecos"], ["ecpa"], ["ecpr"]] # exclude for now
    profile_dict["logon"] = [["hike", "jh0", "jh1", "jh2", "jh3", "jh4", "tg1", "ps"],
             ["jh5", "tg2"],
             ["jhk", "jhu", "tgk", "tgu", "psk", "psu", "rondane"]]
    profile_dict["tanaka"] = [["rtc000", "rtc001"], [], []] # maybe exclude
    profile_dict["semcore"] = [["sc01", "sc02", "sc03"],[], []]
    profile_dict["brown"] = [[], [], ["cf04", "cf06", "cf10", "cf21", "cg07", "cg11", "cg21", "cg25", "cg32", "cg35", "ck11", "ck17", "cl05", "cl14", "cm04", "cn03", "cn10", "cp15", "cp26", "cr09"]] 
    profile_dict["verbmobil"] = [["vm6", "vm13", "vm31"], [], ["vm32"]]
    profile_dict["cb"] = [[], [], ["cb"]]   
    profile_dict["wsj"] = [["wsj00a", "wsj00b", "wsj00c", "wsj00d", "wsj01a", "wsj01b", "wsj01c", "wsj01d", "wsj02a", "wsj02b", "wsj02c", "wsj02d", "wsj03a", "wsj03b", "wsj03c", "wsj04a", "wsj04b", "wsj04c", "wsj04d", "wsj04e", "wsj05a", "wsj05b", "wsj05c", "wsj05d", "wsj05e", "wsj06a", "wsj06b", "wsj06c", "wsj06d", "wsj07a", "wsj07b", "wsj07c", "wsj07d", "wsj07e", "wsj08a", "wsj09a", "wsj09b", "wsj09c", "wsj09d", "wsj09e", "wsj10a", "wsj10b", "wsj10c", "wsj10d", "wsj11a", "wsj11b", "wsj11c", "wsj11d", "wsj11e", "wsj12a", "wsj12b", "wsj12c", "wsj12d", "wsj12e", "wsj13a", "wsj13b", "wsj13c", "wsj13d", "wsj13e", "wsj14a", "wsj14b", "wsj14c", "wsj14d", "wsj14e", "wsj15a", "wsj15b", "wsj15c", "wsj15d", "wsj15e", "wsj16a", "wsj16b", "wsj16c", "wsj16d", "wsj16e", "wsj16f", "wsj17a", "wsj17b", "wsj17c", "wsj17d", "wsj18a", "wsj18b", "wsj18c", "wsj18d", "wsj18e", "wsj19a", "wsj19b", "wsj19c", "wsj19d"],
        ["wsj20a", "wsj20b", "wsj20c", "wsj20d", "wsj20e"], 
        ["wsj21a", "wsj21b", "wsj21c", "wsj21d"]]

    profiles = []
    for pf_name, corpus in profile_dict.items():
        for pf in corpus[0]:
            profiles.append((pf, pf_name, "train"))
        for pf in corpus[1]:
            profiles.append((pf, pf_name, "dev"))
        for pf in corpus[2]:
            profiles.append((pf, pf_name, "test"))

    lexicon = syntax.Lexicon(args.grammar)

    for pf, pf_name, sset in profiles:
        in_dir = args.input + "/" + pf
        out_dir = args.output + "/" + sset + "/" + pf
        read_profile(in_dir, out_dir, pf_name, {}, lexicon, args)


def main():
    argparser = argparse.ArgumentParser()
    argparser.add_argument('-i', '--input', help='directory path to a profile, or to the entire Redwoods (with --redwoods)')
    argparser.add_argument('-o', '--output', help='directory path to output')
    argparser.add_argument('-g', '--grammar', help='directory path to the ERG', default="data/original/erg1214")
    argparser.add_argument('--redwoods', action="store_true", help='process the entire Redwoods, not just a single profile')
    argparser.add_argument('--mrp', action="store_true", help='use the MRP EDS semantic representation')
    argparser.add_argument('--mrp_input', help='directory path to mrp input')

    argparser.add_argument('--extract_syntax', action="store_true", help='extract derivation tree and supertags')
    argparser.add_argument('-c', '--convert_semantics', action="store_true", help='convert span-based DMRS')
    argparser.add_argument('--extract_semantics', action="store_true", help='convert span-based DMRS')

    args = argparser.parse_args()
    assert args.input and os.path.isdir(args.input), "Invalid input path"

    if args.redwoods:
        #TODO with MRP
        read_redwoods(args)
    else:
        lexicon = syntax.Lexicon(args.grammar)
        profile_name = get_profile_name(args.input)

        if args.mrp: # Read mrp file matching the profile
            mrp_eds = read_mrp(args.mrp_input)
        else:
            mrp_eds = {}

        read_profile(args.input, args.output, profile_name, mrp_eds, lexicon, args)


if __name__ == '__main__':
    main()

