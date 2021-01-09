/**
 * A semantic Node class that represents a Node in a Graph.
 *
 * @since 29/08/2020
 */

package com.RepGraph;

import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;

@JsonIgnoreProperties(value = {"is_surface","sortinfo"})
public class Node {

    /**
     * The Node's ID number.
     */
    @JsonAlias({"id","nodeid"})
    private String id;

    /**
     * The Node's label.
     */
    @JsonAlias({"predicate","label"})
    private String label;

    /**
     * An array list of Anchors which give the Node to Token index alignment - done in ArrayList format for easier processing and compatibility with API
     */
    @JsonAlias("lnk")
    private Anchors anchors;

    /**
     * An array list of directed neighbouring nodes i.e the nodes that this Node points to.
     */
    @JsonIgnore
    private ArrayList<Node> directedNeighbours;

    /**
     * An array list of undirected neighbouring nodes i.e the nodes that point to this Node.
     */
    @JsonIgnore
    private ArrayList<Node> undirectedNeighbours;

    /**
     * An array list of edges to the directed neighbouring edges i.e the edges leaving this Node.
     */
    @JsonIgnore
    private ArrayList<Edge> directedEdgeNeighbours;

    /**
     * An array list of edges to the undirected neighbouring edges i.e the edges coming into this Node.
     */
    @JsonIgnore
    private ArrayList<Edge> undirectedEdgeNeighbours;

    /**
     * Default constructor for the Node class.
     */
    public Node() {
        this.directedNeighbours = new ArrayList<>();
        this.undirectedNeighbours = new ArrayList<>();
        this.directedEdgeNeighbours = new ArrayList<>();
        this.undirectedEdgeNeighbours = new ArrayList<>();
    }

    /**
     * Fully parameterised constructor for the Node class.
     * @param id The Node's ID number.
     * @param label The Node's label.
     * @param anchors An array list of the Node's Anchors.
     */
    public Node(String id, String label, Anchors anchors) {
        this.id = id;
        this.label = label;
        this.anchors = anchors;
        this.undirectedNeighbours = new ArrayList<>();
        this.directedNeighbours = new ArrayList<>();
        this.directedEdgeNeighbours = new ArrayList<>();
        this.undirectedEdgeNeighbours = new ArrayList<>();
    }

    /**
     * Copy constructor for the Node class.
     * The copy constructor does not copy across neighbouring nodes or neighbouring edges
     * as it is unnecessary and should only be populated if necessary for analysis
     *
     * @param n This is the Node that is being copied.
     */
    public Node(Node n) {
        this.label = n.label;
        this.id = n.id;
        Anchors anch = new Anchors(n.anchors.getFrom(), n.anchors.getEnd());
        this.anchors = anch;
        this.undirectedEdgeNeighbours = new ArrayList<>();
        this.directedEdgeNeighbours = new ArrayList<>();
        this.directedNeighbours = new ArrayList<>();
        this.undirectedNeighbours = new ArrayList<>();

    }

    /**
     * Getter method for the Node's ID number.
     * @return Integer The Node's ID number.
     */
    public String getId() {
        return id;
    }

    /**
     * Setter method for the Node's ID number.
     * @param id The Node's ID number.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter method for the Node's label.
     * @return String The Node's label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Setter method for the Node's label.
     * @param label The Node's label.
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Getter method for the the Node's Anchors.
     * @return ArrayList An ArrayList of the Node's Anchors.
     */
    @JsonGetter("anchors")
    public ArrayList<Anchors> getArrayAnchors() {
        ArrayList<Anchors> arr = new ArrayList<>();
        arr.add(this.anchors);
        return arr;
    }


    public Anchors getAnchors() {
        return anchors;
    }

    /**
     * Setter method for the the Node's Anchors.
     * @param anchors An ArrayList of the Node's Anchors.
     */
    public void setAnchors(Anchors anchors) {
        this.anchors = anchors;
    }

    @JsonSetter("anchors")
    public void setAnchors(ArrayList<Anchors> anchors) {
        this.anchors = anchors.get(0);
    }

    /**
     * Adds a directed neighbouring Node. i.e a Node that this Node points to.
     * @param neighbour A direct neighbouring Node.
     */
    public void addDirectedNeighbour(Node neighbour) {
        directedNeighbours.add(neighbour);
    }

    /**
     * Adds an undirected neighbouring Node. i.e a Node that points to this Node.
     * @param neighbour An undirected neighbouring Node.
     */
    public void addUndirectedNeighbour(Node neighbour) {
        undirectedNeighbours.add(neighbour);
    }

    /**
     * Getter method for a Node's directed neighbours.
     * @return ArrayList The Node's direct neighbours.
     */
    public ArrayList<Node> getDirectedNeighbours() {
        return directedNeighbours;
    }

    /**
     * Getter method for a Node's undirected neighbours.
     * @return ArrayList The Node's undirected neighbours.
     */
    public ArrayList<Node> getUndirectedNeighbours() {
        return undirectedNeighbours;
    }


    /**
     * Getter method for DirectedEdgeNeighbours.
     *
     * @return ArrayList A list of the edges connected from this Node to other nodes.
     */
    public ArrayList<Edge> getDirectedEdgeNeighbours() {
        return directedEdgeNeighbours;
    }


    /**
     * Adds an Edge to the DirectedEdgeNeighbours ArrayList
     *
     * @param e This is the Edge that will be added to the DirectedEdgeNeighbours ArrayList
     */
    public void addDirectedEdgeNeighbour(Edge e) {
        directedEdgeNeighbours.add(e);
    }

    /**
     * Getter method for UndirectedEdgeNeighbours.
     *
     * @return ArrayList A List of the edges that connect from other nodes to this Node
     */
    public ArrayList<Edge> getUndirectedEdgeNeighbours() {
        return undirectedEdgeNeighbours;
    }


    /**
     * Adds an Edge to the UndirectedEdgeNeighbours ArrayList
     *
     * @param e This is the Edge that will be added to the UndirectedEdgeNeighbours ArrayList
     */
    public void addUndirectedEdgeNeighbour(Edge e) {
        undirectedEdgeNeighbours.add(e);
    }

    /**
     * Equals method for the Node class.
     *
     * @param o Object
     * @return boolean Whether to two classes being compared are equal.
     */
    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof Node)) {
            return false;
        }

        Node n = (Node) o;
        return ((id == n.getId()) && (label.equals(n.getLabel())) && (anchors.equals(n.getAnchors())));
    }
}
