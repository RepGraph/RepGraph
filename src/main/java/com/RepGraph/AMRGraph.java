package com.RepGraph;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Graph class represents a single sentence which comprises of nodes, edges and tokens.
 */
public class AMRGraph extends AbstractGraph {


    /**
     * Default constructor for the Graph class.
     */
    public AMRGraph() {
        super();
    }

    public AMRGraph(String id, String source, String input, HashMap<String, Node> nodes, ArrayList<Token> tokens, ArrayList<Edge> edges, String top) {
        super(id, source, input, nodes, edges,tokens, top);

    }

    @JsonSetter("nodes")
    public void setNodes(ArrayList<Node> nodelist) {

        for (Node n : nodelist) {
            this.nodes.put(n.getId(), n);
        }

    }


}
