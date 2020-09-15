/**
 * The graph class represents a single sentence which comprises of nodes, edges and tokens.
 * @since 29/08/2020
 */
package com.RepGraph;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class graph {

    /**
     * The graph's ID number.
     */
    private String id;

    /**
     * The graph's source.
     */
    private String source;

    /**
     * The graph's input/sentence.
     */
    private String input;

    /**
     * An array list of the graph's nodes.
     */
    private ArrayList<node> nodes;

    /**
     * An array list of the graph's tokens.
     */
    private ArrayList<token> tokens;

    /**
     * An array list of the graph's edges.
     */
    private ArrayList<edge> edges;

    /**
     * Default constructor for the graph class.
     */
    public graph(){}

    /**
     * Fully parameterised constructor for the graph class.
     * @param id The graph's ID number.
     * @param source The graph's source.
     * @param input The graph's input/sentence.
     * @param nodes An array list of the graph's nodes.
     * @param tokens An array list of the graph's tokens.
     * @param edges An array list of the graph's edges.
     */
    public graph(String id, String source, String input, ArrayList<node> nodes, ArrayList<token> tokens, ArrayList<edge> edges){
        this.id = id;
        this.source= source;
        this.input = input;
        this.nodes = nodes;
        this.tokens = tokens;
        this.edges= edges;
    }

    /**
     * Getter method for the graph's ID number.
     * @return String The graph's ID number.
     */
    public String getId() {
        return id;
    }

    /**
     * Setter method for the graph's ID number.
     * @param id The graph's ID number.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter method for the graph's source.
     * @return String The graph's source.
     */
    public String getSource() {
        return source;
    }

    /**
     * Setter method for the graph's source.
     * @param source The graph's source.
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Getter method for the graph's input.
     * @return String The graph's input.
     */
    public String getInput() {
        return input;
    }

    /**
     * Setter method for the graph's input.
     * @param input The graph's input.
     */
    public void setInput(String input) {
        this.input = input;
    }

    /**
     * Getter method for the graph's nodes.
     * @return ArrayList The graph's nodes.
     */
    public ArrayList<node> getNodes() {
        return nodes;
    }

    /**
     * Setter method for the graph's nodes.
     * @param nodes The graph's nodes.
     */
    public void setNodes(ArrayList<node> nodes) {
        this.nodes = nodes;
    }

    /**
     * Getter method for the graph's tokens.
     * @return ArrayList The graph's tokens.
     */
    public ArrayList<token> getTokens() {
        return tokens;
    }

    /**
     * Setter method for the graph's tokens.
     * @param tokens The graph's tokens.
     */
    public void setTokens(ArrayList<token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Getter method for the graph's edges.
     * @return ArrayList The graph's edges.
     */
    public ArrayList<edge> getEdges() {
        return edges;
    }

    /**
     * Setter method for the graph's edges.
     * @param edges The graph's edges.
     */
    public void setEdges(ArrayList<edge> edges) {
        this.edges = edges;
    }

    /**
     * Analysis Tool for finding the longest path in the graph.
     * @return ArrayList The route of the longest path.
     */
    public ArrayList<Integer> findLongest(){
        setNodeNeighbours();
        ArrayList<Integer> longest = Dijkstra(0);
        ArrayList<Integer> temp;
        for (int i =1; i<nodes.size();i++){
            temp = Dijkstra(i);
            if (temp.size() > longest.size()){
                longest = temp;
            }
        }

        ArrayList<Integer> reversed = new ArrayList<Integer>();
        for (int i = longest.size() - 1; i >= 0; i--) {
            reversed.add(longest.get(i));
        }

        return reversed;
    }

    /**
     * Assigns all the nodes in the graph their neighbouring nodes, which will be used for analysis.
     */
    public void setNodeNeighbours(){
        int source;
        int target;
        for (int i=0; i<edges.size();i++){
            edge currentEdge = edges.get(i);

            source = currentEdge.getSource();
            target = currentEdge.getTarget();

            node currentNode = nodes.get(source);
            currentNode.addNeighbour(nodes.get(target));

            currentNode.addEdgeNeighbour(currentEdge);

        }
    }

    /**
     * Finds the longest distance from a given start node to all the other nodes in the system and returns the path of the longest path.
     * @param startNode Number of the start node.
     * @return ArrayList The longest path available from the start node.
     */
    public ArrayList<Integer> Dijkstra(int startNode){
        int nodesVisited = 0;
        PriorityQueue<node> Q = new PriorityQueue<node>();
        int[] dist = new int[nodes.size()];
        int[] prevNode = new int[nodes.size()];

        for (int i = 0; i<nodes.size();i++){
            Q.add(nodes.get(i));
            dist[i] = Integer.MIN_VALUE;
        }

        dist[startNode] = 0;

        while ((Q.size() != 0) && (nodesVisited < nodes.size())){
            node currentNode = Q.poll();
            nodesVisited++;

            for (node neighbourNodeIDnode : currentNode.getNodeNeighbours()) {
                int neighbourNodeID = neighbourNodeIDnode.getId();
                int currentNodeID = currentNode.getId();
                if (dist[neighbourNodeID] > dist[currentNodeID] - 1){
                    dist[neighbourNodeID] = dist[currentNodeID] - 1;
                    prevNode[neighbourNodeID] = currentNodeID;
                }
            }
        }

        ArrayList<Integer> path = new ArrayList<Integer>();
        int max = 0;
        for (int i = 0; i<dist.length;i++){
            if (dist[max] < dist[i]){
                max = i;
            }
        }

        path.add(max);
        int prev = prevNode[max];
        while (prev!=startNode){
            path.add(prev);
            prev = prevNode[prev];
        }
        path.add(startNode);

        return path;

    }
}
