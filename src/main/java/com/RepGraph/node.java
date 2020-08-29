package com.RepGraph;

import java.util.ArrayList;

public class node {
    private String id;
    private String label;
    private ArrayList<String> anchors;

    public node(){}

    public node(String id, String label, ArrayList<String> anchors){
        this.id = id;
        this.label = label;
        this.anchors = anchors;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ArrayList<String> getAnchors() {
        return anchors;
    }

    public void setAnchors(ArrayList<String> anchors) {
        this.anchors = anchors;
    }
}
