import amrlib
from amrlib.alignments.rbw_aligner import RBWAligner
from amrlib.graph_processing.annotator import add_lemmas
import penman
from penman.surface import Alignment
import os

if __name__ == '__main__':
    dirname = os.path.dirname(__file__)
    filename = os.path.join(dirname, os.path.join('temp',"AMR_TEMP.txt"))
    f = open(filename, "r")

    penman_graph = add_lemmas(f.read(),snt_key="snt")
    aligner = RBWAligner.from_penman_w_json(penman_graph)
    aligned_graph = aligner.get_penman_graph()
    alignments = penman.surface._get_alignments(aligned_graph, Alignment)
    for key in alignments:
        print(key[0],alignments[key].indices)

