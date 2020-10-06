/**
 * A semantic node class that represents a node in a graph.
 * @since 29/08/2020
 */

package com.RepGraph;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
     * An array list of directed neighbouring nodes.
     */
    @JsonIgnore
    private ArrayList<node> directedNeighbours;

    /**
     * An array list of undirected neighbouring nodes.
     */
    @JsonIgnore
    private ArrayList<node> undirectedNeighbours;

    /**
     * An array list of edges to the directed neighbouring nodes.
     */
    @JsonIgnore
    private ArrayList<edge> directedEdgeNeighbours;

    /**
     * An array list of edges to the undirected neighbouring nodes.
     */
    @JsonIgnore
    private ArrayList<edge> undirectedEdgeNeighbours;

    /**
     * Default constructor for the node class.
     */
    public node() {
        this.directedNeighbours = new ArrayList<>();
        this.undirectedNeighbours = new ArrayList<>();
        this.directedEdgeNeighbours = new ArrayList<>();
        this.undirectedEdgeNeighbours = new ArrayList<>();
    }

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
        this.undirectedNeighbours = new ArrayList<>();
        this.directedNeighbours = new ArrayList<>();
        this.directedEdgeNeighbours = new ArrayList<>();
        this.undirectedEdgeNeighbours = new ArrayList<>();
    }

    public node(node n) {
        this.label = n.label;
        this.id = n.id;
        anchors anch = new anchors(n.anchors.get(0).getFrom(), n.anchors.get(0).getEnd());
        ArrayList<anchors> anchorsarr = new ArrayList<>();
        anchorsarr.add(anch);
        this.anchors = anchorsarr;
        this.undirectedEdgeNeighbours = new ArrayList<>();
        this.directedEdgeNeighbours = new ArrayList<>();
        this.directedNeighbours = new ArrayList<>();
        this.undirectedNeighbours = new ArrayList<>();

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
     * Adds a directed neighbouring node.
     * @param neighbour A direct neighbouring node.
     */
    public void addDirectedNeighbour(node neighbour) {
        directedNeighbours.add(neighbour);
    }

    /**
     * Adds an undirected neighbouring node.
     * @param neighbour An undirected neighbouring node.
     */
    public void addUndirectedNeighbour(node neighbour) {
        undirectedNeighbours.add(neighbour);
    }

    /**
     * Getter method for a node's directed neighbours.
     * @return ArrayList The node's direct neighbours.
     */
    public ArrayList<node> getDirectedNeighbours() {
        return directedNeighbours;
    }

    /**
     * Getter method for a node's undirected neighbours.
     * @return ArrayList The node's undirected neighbours.
     */
    public ArrayList<node> getUndirectedNeighbours() {
        return undirectedNeighbours;
    }


    /**
     * Getter method for EdgeNeighbours.
     *
     * @return ArrayList The edges connected from this node.
     */
    public ArrayList<edge> getDirectedEdgeNeighbours() {
        return directedEdgeNeighbours;
    }


    /**
     * Adds an edge to the EdgeNeighbours ArrayList
     *
     * @param e This is the edge that will be added to the EdgeNeighbours ArrayList
     */
    public void addDirectedEdgeNeighbour(edge e) {
        directedEdgeNeighbours.add(e);
    }

    /**
     * Getter method for EdgeNeighbours.
     *
     * @return ArrayList The edges connected from this node.
     */
    public ArrayList<edge> getUndirectedEdgeNeighbours() {
        return undirectedEdgeNeighbours;
    }


    /**
     * Adds an edge to the EdgeNeighbours ArrayList
     *
     * @param e This is the edge that will be added to the EdgeNeighbours ArrayList
     */
    public void addUndirectedEdgeNeighbour(edge e) {
        undirectedEdgeNeighbours.add(e);
    }

    /**
     * Equals method for the node class.
     *
     * @param o Object
     * @return boolean Whether to two classes being compared are equal.
     */
    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof node)) {
            return false;
        }

        node n = (node) o;
        return ((id == n.getId()) && (label.equals(n.getLabel())) && (anchors.equals(n.getAnchors())));
    }
}
