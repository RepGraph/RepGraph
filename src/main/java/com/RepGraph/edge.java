/**
 * Edge class used to represent relations between nodes in a graph.
 * @since 29/08/2020
 */


package com.RepGraph;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = {"id"})
public class edge {

    /**
     * ID of the node that is the source of the edge.
     */
    private int source;

    /**
     * ID of the node that is the target of the edge.
     */
    private int target;

    /**
     * The edge's label.
     */
    private String label;

    /**
     * The edge's post label.
     */
    @JsonProperty("post-label")
    private String postLabel;

    /**
     * Default constructor of the edge class.
     */
    public edge(){}

    /**
     * The Parameterised copy constructor of the edge class
     *
     * @param e This is the edge object that is copied.
     */
    public edge(edge e) {
        this.target = e.target;
        this.source = e.source;
        this.label = e.label;
        this.postLabel = e.postLabel;
    }

    /**
     * Fully parameterised constructor of the edge class.
     * @param source Number of the node that is the source of the edge.
     * @param target Number of the node that is the target of the edge.
     * @param label The edge's label.
     * @param postLabel Default constructor of the edge class.
     */
    public edge(int source, int target, String label, String postLabel){
        this.source = source;
        this.target = target;
        this.label = label;
        this.postLabel = postLabel;
    }

    /**
     * Getter method for the source node.
     * @return Integer Number of the source node.
     */
    public int getSource() {
        return source;
    }

    /**
     * Setter method for the source node.
     * @param source Number of the source node.
     */
    public void setSource(int source) {
        this.source = source;
    }

    /**
     * Getter method for the target node.
     * @return Integer Number of the target node.
     */
    public int getTarget() {
        return target;
    }

    /**
     * Setter method for the target node.
     * @param target Number of the target node.
     */
    public void setTarget(int target) {
        this.target = target;
    }

    /**
     * Getter method for the edge label.
     * @return String The edge's label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Setter method for the edge label
     * @param label The edge's label
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Getter for the edge's post label.
     * @return String The edge's post label.
     */
    public String getPostLabel() {
        return postLabel;
    }

    /**
     * Setter method for the edge's post label.
     * @param postLabel The edge's post label.
     */
    public void setPostLabel(String postLabel) {
        this.postLabel = postLabel;
    }

    /**
     * Equals method for the edge class.
     * @param o Object
     * @return boolean Whether to two classes being compared are equal.
     */
    @Override
    public boolean equals(Object o){

        if (o == this){
            return true;
        }

        if (!(o instanceof edge)){
            return false;
        }

        edge e = (edge) o;


        return ((source == e.getSource()) && (target == e.getTarget()) && (label.equals(e.getLabel())) && (postLabel.equals(e.getPostLabel())));
    }

}
