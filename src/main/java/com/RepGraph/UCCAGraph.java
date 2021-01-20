package com.RepGraph;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    @JsonCreator
    public UCCAGraph(@JsonProperty("id") String id, @JsonProperty("source") String source, @JsonProperty("input") String input, @JsonProperty("nodes") ArrayList<Node> nodes, @JsonProperty("edges") ArrayList<Edge> edges, @JsonProperty("tops") ArrayList<Integer> top) {
        this.id = id;
        this.source = source;
        this.input = input;
        this.edges = edges;
        for (Node n : nodes) {
            this.nodes.put(n.getId(), n);
        }
        this.setNodeNeighbours();
        this.top = top.get(0) + "";
        this.setNodeSpans(this.nodes.get(this.top));
        for (Node n : this.nodes.values()) {
            if (n.isSurface()) {
                String label = "";
                for (Anchors anch : n.getAnchors()) {
                    label += " "+this.input.substring(anch.getFrom(), anch.getEnd());
                }
                n.setLabel(label);
            } else {
                n.setLabel("#" + n.getId());
            }
        }
        populateTokens();

    }

    public void setNodeSpans(Node root) {

        if (root.getAnchors() == null) {
            root.setSurface(false);
            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;
            for (Node n : root.getDirectedNeighbours()) {
                if (n.getAnchors() == null) {
                    setNodeSpans(n);
                } else {
                    n.setSurface(true);
                }
                for (Anchors anch : n.getAnchors()) {
                    min = Math.min(min, anch.getFrom());
                    max = Math.max(max, anch.getEnd());
                }
            }

            Anchors anch = new Anchors(min, max);
            ArrayList<Anchors> arrAnch = new ArrayList<>();
            arrAnch.add(anch);
            root.setAnchors(arrAnch);

        }

    }


}
