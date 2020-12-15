package com.RepGraph;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.*;

/**
 * The Graph class represents a single sentence which comprises of nodes, edges and tokens.
 */
abstract class AbstractGraph {


    /**
     * The Graph's ID number.
     */
    protected String id;

    /**
     * The Graph's source.
     */
    protected String source;

    /**
     * The Graph's input/sentence.
     */
    protected String input;

    /**
     * A HashMap of the Graph nodes with the Node's ID as the key - used for quicker,easier and safer algorithmic use of Node data
     */
    @JsonIgnore
    protected HashMap<Integer, Node> nodes;


    /**
     * The graphs linear list of nodes - used for returning Graph data correctly
     */
    @JsonProperty("nodes")
    protected ArrayList<Node> nodelist;

    /**
     * An array list of the Graph's edges.
     */
    protected ArrayList<Edge> edges;

    /**
     * ID of the Top node
     */
    protected String top;

    /**
     * Default constructor for the Graph class.
     */
    public AbstractGraph() {
        this.nodelist = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.nodes = new HashMap<>();
    }

    /**
     * parameterised constructor for the Graph class that takes in the Node's Hashmap instead of the linear nodeslist
     * and populates the nodeslist from the hashmap information.
     *
     * @param id     The Graph's ID number.
     * @param source The Graph's source.
     * @param input  The Graph's input/sentence.
     * @param edges  An array list of the Graph's edges.
     */
    public AbstractGraph(String id, String source, String input, HashMap<Integer, Node> nodes, ArrayList<Edge> edges, String top) {
        this.id = id;
        this.source = source;
        this.input = input;
        this.nodelist = new ArrayList<>();
        this.nodes = nodes;
        for (Node n : nodes.values()) {
            this.nodelist.add(n);
        }
        this.edges = edges;
        this.top = top;

    }
    /**
     * parameterised constructor for the Graph class that takes in the Node's Hashmap instead of the linear nodeslist
     * and populates the nodeslist from the hashmap information.
     *
     * @param id     The Graph's ID number.
     * @param source The Graph's source.
     * @param input  The Graph's input/sentence.
     * @param edges  An array list of the Graph's edges.
     */
    public AbstractGraph(String id, String source, String input, ArrayList<Node> nodelist, ArrayList<Edge> edges, String top) {
        this.id = id;
        this.source = source;
        this.input = input;
        this.nodelist = nodelist;
        this.nodes = new HashMap<Integer, Node>();
        for (Node n : nodelist) {
            this.nodes.put(n.getId(), n);
        }

        this.edges = edges;
        this.top = top;

    }


    /**
     * This is the getter for the linear list of nodes "nodeslist"
     *
     * @return ArrayList<Node> This is the linear list of nodes of the Graph object
     */
    @JsonGetter("nodes")
    public ArrayList<Node> getNodelist() {
        return this.nodelist;
    }

    /**
     * The Setter method for the linear nodeslist attribute. The setter of the linear list of nodes also resets and populates the hashmap nodes.
     *
     * @param nodelist This is the linear list of nodes is used to set the objects list of nodes.
     */
    @JsonSetter("nodes")
    public void setNodelist(ArrayList<Node> nodelist) {
        this.nodelist = nodelist;
    }

    /**
     * Getter method for the Graph's ID number.
     *
     * @return String The Graph's ID number.
     */
    public String getId() {
        return id;
    }

    /**
     * Setter method for the Graph's ID number.
     *
     * @param id The Graph's ID number.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter method for the Graph's source.
     *
     * @return String The Graph's source.
     */
    public String getSource() {
        return source;
    }

    /**
     * Setter method for the Graph's source.
     *
     * @param source The Graph's source.
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Getter method for the Graph's input.
     *
     * @return String The Graph's input.
     */
    public String getInput() {
        return input;
    }

    /**
     * Setter method for the Graph's input.
     *
     * @param input The Graph's input.
     */
    public void setInput(String input) {
        this.input = input;
    }

    /**
     * Getter method for the Graph's edges.
     *
     * @return ArrayList The Graph's edges.
     */
    public ArrayList<Edge> getEdges() {
        return edges;
    }

    /**
     * Setter method for the Graph's edges.
     *
     * @param edges The Graph's edges.
     */
    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    /**
     * Getter method for a graphs top Node array
     *
     * @return ArrayList<Integer> This is the ArrayList of top Node ids.
     */
    public String getTop() {
        return top;
    }

    /**
     * Setter method for a graphs top Node array
     *
     * @param top This is an ArrayList of the ids of the top nodes in a Graph
     */
    public void setTops(String top) {
        this.top = top;
    }

    /**
     * Analysis Tool for finding the longest paths in the Graph.
     *
     * @param directed This is the boolean to decide if the longest path found is directed or undirected.
     * @return ArrayList<ArrayList < Integer>> The a list of longest paths in the Graph.
     */
    public abstract ArrayList<ArrayList<Integer>> findLongest(boolean directed);


    /**
     * Equals method for the Graph class.
     *
     * @param o Object
     * @return boolean Whether to two classes being compared are equal.
     */
    @Override
    public abstract boolean equals(Object o);

    /**
     * Checks if a directed or an undirected Graph is cyclic or not by calling on the appropriate recursive function required.
     *
     * @param directed Boolean to see if the Graph is directed or not.
     * @return Boolean If the Graph is cyclic or not.
     */
    public abstract boolean isCyclic(boolean directed);

    public abstract boolean isPlanar();
}
