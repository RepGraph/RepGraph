package com.RepGraph;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Graph class represents a single sentence which comprises of nodes, edges and tokens.
 */
public class UCCAGraph extends AbstractGraph {


    /**
     * Default constructor for the Graph class.
     */
    public UCCAGraph() {
        super();
    }

    public UCCAGraph(String id, String source, String input, HashMap<String, Node> nodes, ArrayList<Token> tokens, ArrayList<Edge> edges, String top) {
        super(id, source, input, nodes, edges,tokens, top);

    }

    @JsonSetter("nodes")
    public void setNodes(ArrayList<Node> nodelist) {

        for (Node n : nodelist) {
            if (n.getAnchors()==null){
                ArrayList<Anchors> anchArr = new ArrayList<>();
                Anchors anchors = new Anchors(0,this.input.length());
                anchArr.add(anchors);
                n.setAnchors(anchArr);
            }
            if (n.getLabel()==null){
                n.setLabel(n.getId());
            }
            this.nodes.put(n.getId(), n);
        }




        populateTokens();
    }

}
