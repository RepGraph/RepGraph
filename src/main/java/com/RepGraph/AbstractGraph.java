package com.RepGraph;

import com.fasterxml.jackson.annotation.*;

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

    protected ArrayList<Token> tokens;

    @JsonAlias({"relations","nodes"})
    @JsonProperty("nodes")
    protected HashMap<String, Node> nodes;

    /**
     * An array list of the Graph's edges.
     */
    @JsonAlias({"links","edges"})
    protected ArrayList<Edge> edges;


    /**
     * ID of the Top node
     */
    @JsonAlias({"top","tops"})
    protected String top;

    /**
     * Default constructor for the Graph class.
     */
    public AbstractGraph() {
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
    public AbstractGraph(String id, String source, String input, HashMap<String, Node> nodes, ArrayList<Edge> edges, String top) {
        this.id = id;
        this.source = source;
        this.input = input;

        this.nodes = nodes;

        this.edges = edges;
        this.top = top;

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


    public ArrayList<Token> getTokens() {
        return tokens;
    }


    public void setTokens(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public ArrayList<Token> extractTokensFromNodes() {


        ArrayList<Token> tokenlist = new ArrayList<>();
        String[] list = this.input.split(" ");

        for (int i =0;i<list.length;i++) {
            list[i]+=" ";
            tokenlist.add(new Token(i,list[i],list[i],list[i]));

        }

        int lengthBoundary[] = new int[list.length];

        lengthBoundary[0] = list[0].length();


        for (int i = 1; i < lengthBoundary.length; ++i) {
            lengthBoundary[i] = lengthBoundary[i - 1] + list[i].length();

        }

        for (Node n:this.nodes.values()) {
            for (int i = 0; i <lengthBoundary.length ; i++) {
                if (n.getAnchors().getFrom()<lengthBoundary[i] && i<=n.getAnchors().getFrom()){
                    n.getAnchors().setFrom(i);
                }
                if (n.getAnchors().getEnd()<=lengthBoundary[i] && i<=n.getAnchors().getEnd()){
                    n.getAnchors().setEnd(i);
                }

            }
        }

        return tokenlist;
    }

    /**
     * Getter method for a graphs top Node array
     *
     * @return ArrayList<Integer> This is the ArrayList of top Node ids.
     */
    public String getTop() {
        return top;
    }


    @JsonSetter("tops")
    public void setTopArray(ArrayList<Integer> top) {
        this.top = top.get(0)+"";
    }

    @JsonSetter("top")
    public void setTopString(String top) {
        this.top = top;
    }


    @JsonGetter("nodes")
    public ArrayList<Node> returnNodeInArrayList() {
        ArrayList<Node> returnNodes = new ArrayList<Node>();
        for (Node n : this.nodes.values()) {
            returnNodes.add(n);
        }
        return returnNodes;
    }

    @JsonIgnore
    /**
     * Getter method for the Graph's nodes HashMap.
     *
     * @return HashMap<Integer, Node> The Graph's nodes HashMap.
     */
    public HashMap<String, Node> getNodes() {
        return this.nodes;
    }

    @JsonSetter("nodes")
    public void setNodes(ArrayList<Node> nodelist) {

        for (Node n : nodelist) {
            this.nodes.put(n.getId(), n);
        }
    }

    @JsonIgnore
    public void setNodes(HashMap<String, Node> nodes) {

        this.nodes = nodes;
    }

    public void populateTokens(){
        if (tokens.size()==0){
        setTokens(extractTokensFromNodes());}
    }
    /**
     * Creates a list of tokens in a range starting at "from" and ending at "end"
     *
     * @param from The start of the range of tokens
     * @param end  The End of the range of tokens
     * @return ArrayList<Token> Returns a list of Token objects
     */
    public ArrayList<Token> getTokenSpan(int from, int end) {
        ArrayList<Token> returnTokens = new ArrayList<>();
        for (int i = from; i < end + 1; i++) {
            returnTokens.add(tokens.get(i));
        }
        return returnTokens;
    }

    /**
     * gets the "form" of tokens and turns them into a string
     *
     * @param tokenIn List of tokens to become a string
     * @return String This is the string of all Token's form.
     */
    public String getTokenInput(ArrayList<Token> tokenIn) {
        String output = "";

        for (Token t : tokenIn) {
            output += " " + t.getForm();

        }

        return output.trim();
    }

    /**
     * This method checks to see if a Graph has a dangling Edge i.e an Edge that doesnt connect to any Node
     *
     * @return boolean returns true if the Graph has a dangling Edge otherwise returns false.
     */
    public boolean hasDanglingEdge() {
        for (Edge e : edges) {
            if (!nodes.containsKey(e.getTarget()) || !nodes.containsKey(e.getSource())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Assigns all the nodes in the Graph their directed and undirected neighbouring nodes, which will be used for analysis.
     */
    public void setNodeNeighbours() {
        String source;
        String target;

        if (edges.size() == 0) {
            //Graph has no edges
            return;
        } else {

            source = edges.get(0).getSource();

            if (!nodes.containsKey(source) || nodes.get(source).getDirectedNeighbours().size() != 0) {
                //Node neighbours have already been set
                return;
            }
        }

        //Iterate through each Edge and set the corresponding Node's thier neighbours.
        for (int i = 0; i < edges.size(); i++) {

            Edge currentEdge = edges.get(i);

            source = currentEdge.getSource();
            target = currentEdge.getTarget();

            Node sourceNode = nodes.get(source);
            sourceNode.addDirectedNeighbour(nodes.get(target));
            nodes.get(target).addUndirectedNeighbour(sourceNode);
            if (!sourceNode.getDirectedEdgeNeighbours().contains(currentEdge)){sourceNode.addDirectedEdgeNeighbour(currentEdge);}

            nodes.get(target).addUndirectedEdgeNeighbour(currentEdge);

        }

    }

    /**
     * Combines the directed and undirected Node neighbours of a given Node.
     *
     * @param nodeID The ID of the Node.
     * @return ArrayList<Node> List of the Node's directed and undirected neighbours.
     */
    public ArrayList<Node> combineNeighbours(String nodeID) {
        ArrayList<Node> allNeighbours = new ArrayList<>(nodes.get(nodeID).getDirectedNeighbours());
        ArrayList<Node> undirectedNeighbours = new ArrayList<>(nodes.get(nodeID).getUndirectedNeighbours());
        //Adds all a Node's undirected neighbours and its directed neighbours together.
        allNeighbours.addAll(undirectedNeighbours);
        return allNeighbours;
    }

    /**
     * Analysis Tool for finding the longest paths in the Graph.
     *
     * @param directed This is the boolean to decide if the longest path found is directed or undirected.
     * @return ArrayList<ArrayList < Integer>> The a list of longest paths in the Graph.
     */
    public abstract ArrayList<ArrayList<String>> findLongest(boolean directed);


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
