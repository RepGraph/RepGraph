package com.RepGraph;

import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Graph class represents a single sentence which comprises of nodes, edges and tokens.
 */
public class PTGGraph extends AbstractGraph {


    /**
     * Default constructor for the Graph class.
     */
    public PTGGraph() {
        super();
    }

    public PTGGraph(String id, String source, String input, HashMap<String, Node> nodes, ArrayList<Token> tokens, ArrayList<Edge> edges, String top) {
        super(id, source, input, nodes, edges,tokens, top);

    }

    @JsonSetter("nodes")
    public void setNodes(ArrayList<Node> nodelist) {

        for (Node n : nodelist) {
            if (n.getLabel()!=null){
            if (n.getLabel().startsWith("#")){
                n.setSurface(false);
            }else{n.setSurface(true);}

        }
            this.nodes.put(n.getId(), n);
        }
        ArrayList<Anchors> anchArr = new ArrayList<>();
        Anchors anchors = new Anchors(0,this.input.length());
        anchArr.add(anchors);

        this.nodes.get("0").setAnchors(anchArr);
        this.nodes.get("0").setLabel("top");

        populateTokens();
    }
}
