package com.RepGraph;

public class edge {
    private int source;
    private int target;
    private String label;
    private String postLabel;

    public edge(){}

    public edge(int source, int target, String label, String postLabel){
        this.source = source;
        this.target = target;
        this.label = label;
        this.postLabel = postLabel;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPostLabel() {
        return postLabel;
    }

    public void setPostLabel(String postLabel) {
        this.postLabel = postLabel;
    }
}
