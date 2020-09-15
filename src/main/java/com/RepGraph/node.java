/**
 * A semantic node class that represents a node in a graph.
 * @since 29/08/2020
 */

package com.RepGraph;

import java.util.ArrayList;

public class node {

    /**
     * The node's ID number.
     */
    private int id;

    /**
     * The node's label.
     */
    private String label;

    /**
     * An array list of anchors which give the node to token index alignment.
     */
    private ArrayList<anchors> anchors;

    /**
     * An array list of neighbouring nodes.
     */
    private ArrayList<node> nodeNeighbours;

    /**
     * An array list of edges to the neighbouring nodes.
     */
    private ArrayList<edge> edgeNeighbours;

    /**
     * When calculating longest path, prevNode will be the previous node in the path.
     */
    private int prevNode;

    /**
     * Default constructor for the node class.
     */
    public node(){}

    /**
     * Fully parameterised constructor for the node class.
     * @param id The node's ID number.
     * @param label The node's label.
     * @param anchors An array list of the node's anchors.
     */
    public node(int id, String label, ArrayList<anchors> anchors) {
        this.id = id;
        this.label = label;
        this.anchors = anchors;
        this.nodeNeighbours = new ArrayList<>();
        this.edgeNeighbours = new ArrayList<>();
    }

    /**
     * Getter method for the node's ID number.
     * @return Integer The node's ID number.
     */
    public int getId() {
        return id;
    }

    /**
     * Setter method for the node's ID number.
     * @param id The node's ID number.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Getter method for the node's label.
     * @return String The node's label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Setter method for the node's label.
     * @param label The node's label.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Getter method for the the node's anchors.
     * @return ArrayList The node's anchors.
     */
    public ArrayList<anchors> getAnchors() {
        return anchors;
    }

    /**
     * Setter method for the the node's anchors.
     * @param anchors The node's anchors.
     */
    public void setAnchors(ArrayList<anchors> anchors) {
        this.anchors = anchors;
    }

    /**
     * Adds a neighbouring node.
     * @param neighbour A neighbouring node.
     */
    public void addNeighbour(node neighbour) {
        nodeNeighbours.add(neighbour);
    }

    /**
     * Getter method for a node's neighbours.
     * @return ArrayList The node's neighbours.
     */
    public ArrayList<node> getNodeNeighbours() {
        return nodeNeighbours;
    }


    /**
     * Getter method for EdgeNeighbours.
     *
     * @return ArrayList The edges connected from this node.
     */
    public ArrayList<edge> getEdgeNeighbours() {
        return edgeNeighbours;
    }

    /**
     * Adds an edge to the EdgeNeighbours ArrayList
     *
     * @param e This is the edge that will be added to the EdgeNeighbours ArrayList
     */
    public void addEdgeNeighbour(edge e) {
        edgeNeighbours.add(e);
    }
}
