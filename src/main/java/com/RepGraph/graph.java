package com.RepGraph;

import java.util.ArrayList;

public class graph {
    private String id;
    private String source;
    private String input;
    private ArrayList<node> nodes;
    private ArrayList<token> tokens;
    private ArrayList<edge> edges;

    public graph(){}

    public graph(String id, String source, String input, ArrayList<node> nodes, ArrayList<token> tokens, ArrayList<edge> edges){
        this.id = id;
        this.source= source;
        this.input = input;
        this.nodes = nodes;
        this.tokens = tokens;
        this.edges= edges;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public ArrayList<node> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<node> nodes) {
        this.nodes = nodes;
    }

    public ArrayList<token> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<token> tokens) {
        this.tokens = tokens;
    }

    public ArrayList<edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<edge> edges) {
        this.edges = edges;
    }
}
