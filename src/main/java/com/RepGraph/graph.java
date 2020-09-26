/**
 * The graph class represents a single sentence which comprises of nodes, edges and tokens.
 * @since 29/08/2020
 */
package com.RepGraph;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Array;
import java.util.*;

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
     * An array list of the graph's top node ids.
     */
    @JsonProperty("tops")
    private ArrayList<Integer> tops;

    //Stack used for topological sorting of a graph.
    private Stack<Integer> stack;

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
     * @param tops An array list of the graph's top node ids.
     */
    public graph(String id, String source, String input, ArrayList<node> nodes, ArrayList<token> tokens, ArrayList<edge> edges, ArrayList<Integer> tops) {
        this.id = id;
        this.source= source;
        this.input = input;
        this.nodes = nodes;
        this.tokens = tokens;
        this.edges= edges;
        this.tops = tops;
        this.stack = new Stack<Integer>();
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
     * Getter method for a graphs top node array
     *
     * @return ArrayList<Integer> This is the ArrayList of top node ids.
     */
    public ArrayList<Integer> getTops() {
        return tops;
    }

    /**
     * Setter method for a graphs top node array
     *
     * @param tops This is an ArrayList of the ids of the top nodes in a graph
     */
    public void setTops(ArrayList<Integer> tops) {
        this.tops = tops;
    }

    /**
     * Analysis Tool for finding the longest path in the graph.
     * @return ArrayList<ArrayList<Integer>> The a list of longest paths in the graph.
     */
    public ArrayList<ArrayList<Integer>> findLongest(boolean directed){

        setNodeNeighbours();

        ArrayList<ArrayList<Integer>> paths = new ArrayList<>();

        //Default longest path set from node 0.
        ArrayList<ArrayList<Integer>> longest = directedLongestPaths(0);
        for (int i =0 ; i<longest.size();i++) {
            paths.add(longest.get(i));
        }
        ArrayList<ArrayList<Integer>> temp;

        //Makes each node the start node and finds the longest overall path.
        for (int i =1; i<nodes.size();i++){
            temp = directedLongestPaths(i);
                if (temp.size() != 0) {
                    if ((longest.size() == 0) || (temp.get(0).size() > longest.get(0).size())) {
                        longest = temp;
                        paths.clear();
                        for (int j = 0; j < temp.size(); j++) {
                            paths.add(new ArrayList<Integer>(temp.get(j)));
                        }
                    } else if (temp.get(0).size() == longest.get(0).size()) {
                        for (int j = 0; j < temp.size(); j++) {
                            paths.add(new ArrayList<Integer>(temp.get(j)));
                        }
                    } else {
                    }
                }
        }


        //Reverses the path so that start node is first.
        ArrayList<ArrayList<Integer>> reversed = new ArrayList<>();
        ArrayList<Integer> item = new ArrayList<>();
        for (int i = 0; i < paths.size(); i++){
            item.clear();
            for (int j = paths.get(i).size() -1; j >= 0; j-- ){
                item.add(paths.get(i).get(j));
            }
            reversed.add(new ArrayList<>(item));
        }

        return reversed;
    }

    /**
     * Assigns all the nodes in the graph their directed and undirected neighbouring nodes, which will be used for analysis.
     */
    public void setNodeNeighbours(){
        int source;
        int target;

        if (edges.size() == 0) {
            //Graph has no edges
            return;
        } else {
            source = edges.get(0).getSource();
            if (nodes.get(source).getDirectedNeighbours().size() != 0) {
                //Node neighbours have already been set
                return;
            }
        }

        for (int i=0; i<edges.size();i++){
            edge currentEdge = edges.get(i);

            source = currentEdge.getSource();
            target = currentEdge.getTarget();

            node sourceNode = nodes.get(source);
            sourceNode.addDirectedNeighbour(nodes.get(target));
            nodes.get(target).addUndirectedNeighbour(sourceNode);

            sourceNode.addEdgeNeighbour(currentEdge);

        }
    }

    /**
     * Finds the longest distance from a given start node to all the other nodes in the system and returns the path of the longest path.
     * @param startNode Number of the start node.
     * @return ArrayList<ArrayList<Integer>> The longest paths available from the start node.
     */
    public ArrayList<ArrayList<Integer>> directedLongestPaths(int startNode){

        ArrayList<ArrayList<Integer>> paths = new ArrayList<>();

        //Check if the start node has any nerighbours
        if (nodes.get(startNode).getDirectedNeighbours().size()==0){
            return paths;
        }

        ArrayList<Integer> dist = new ArrayList<>();
        int[] prevNode = new int[nodes.size()];

        //Set all nodes in the graph to unvisited (i.e. visited[nodeID] = false)
        boolean [] visisted = new boolean[nodes.size()];
        for (int i =0; i<nodes.size();i++){
            visisted[i] = false;
        }

        //Topologically sort every unvisited node.
        for (int i =0; i<nodes.size();i++){
            if (!visisted[i]){
                topologicalSort(i, visisted);
            }
        }

        //Set all distances to NINF except the start node.
        for (int i =0; i<nodes.size();i++){
            dist.add(Integer.MIN_VALUE);
        }
        dist.set(startNode,0);

        //Iterate through the stack to find longest path.
        while (!stack.empty()){
            int u = stack.pop();

            if (dist.get(u) != Integer.MIN_VALUE){
                for (node n : nodes.get(u).getDirectedNeighbours()){
                    if (dist.get(n.getId()) < dist.get(u) + 1){
                        dist.set(n.getId(), dist.get(u) + 1);
                        prevNode[n.getId()] = u;
                    }
                }
            }
        }

        //Finds node index with the longest viable path. (i.e. most negative distance)
        int max = dist.get(0);
        int maxIndex =0;
        for (int i = 0; i < dist.size(); i++) {
            if (dist.get(maxIndex) < dist.get(i)) {
                max = dist.get(i);
                maxIndex = i;
            }
        }

        //Uses the prevNode ArrayList to find the path of the longest distance starting at the end node.
        ArrayList<Integer> path = new ArrayList<>();
        for (int i = 0; i<dist.size();i++){
            if (dist.get(i)==max){
                path.clear();
                path.add(i);
                int prev = prevNode[i];
                while (prev!=startNode){
                    path.add(prev);
                    prev = prevNode[prev];
                }
                path.add(startNode);
                paths.add(new ArrayList<Integer>(path));
            }
        }


        return paths;

    }

    /**
     * Recursive function that Topologically Sorts a graph.
     * @param nodeID The current node.
     * @param visited A list of booleans, representing whether a node has already been visited or not.
     */
    public void topologicalSort(int nodeID, boolean visited[]){
        visited[nodeID] = true;

        //Iterate through every neighbouring node of the given node.
        for (node neighbourNode : nodes.get(nodeID).getDirectedNeighbours()){
            if (!visited[neighbourNode.getId()]){
                topologicalSort(neighbourNode.getId(),visited);
            }
        }

        stack.push(nodeID);
    }

    /**
     * Breadth First Search algorithm for finding the longest path from a given start node in a graph.
     * @param startNodeID The starting node ID.
     * @return ArrayList<Integer> The path of the longest route from the given start node.
     */
    public ArrayList< ArrayList<Integer>> BFS(int startNodeID){

        ArrayList< ArrayList<Integer>> paths = new ArrayList<>();

        //Creates a list of all directed and undirected neighbours of the start node.
        ArrayList<node> allNeighbours = new ArrayList<node>(nodes.get(startNodeID).getDirectedNeighbours().size());
        ArrayList<node> undirectedNeighbours = nodes.get(startNodeID).getUndirectedNeighbours();
        for (int i = 0; i < undirectedNeighbours.size(); i++){
            allNeighbours.add(undirectedNeighbours.get(i));
        }

        //Checks to see if the node has any neighbours.
        if (allNeighbours.size()==0){
            return paths;
        }

        //All distances from start node start at -1, except the start node.
        ArrayList<Integer> dist = new ArrayList<>();
        for (int i =0; i<nodes.size();i++){
            dist.add(-1);
        }
        dist.set(startNodeID,0);

        int[] prevNode = new int[nodes.size()];

        Queue<Integer> q = new LinkedList<>();

        q.add(startNodeID);

        while(q.isEmpty()){
            int currentNodeID = q.poll();

            //Combine the lists of all directed and undirected neighbours of the current node.
            allNeighbours = nodes.get(currentNodeID).getDirectedNeighbours();
            undirectedNeighbours = nodes.get(currentNodeID).getUndirectedNeighbours();
            for (int i = 0; i < undirectedNeighbours.size(); i++){
                allNeighbours.add(undirectedNeighbours.get(i));
            }

            //Iterate through all neighbouring nodes
            for (int i=0;i<allNeighbours.size();i++){
                int neighbourNodeID = allNeighbours.get(i).getId();

                if (dist.get(neighbourNodeID)==-1){
                    q.add(neighbourNodeID);
                    dist.set(neighbourNodeID, dist.get(currentNodeID)+1);
                    prevNode[neighbourNodeID] = currentNodeID;
                }
            }
        }


        int max = dist.get(0);
        int maxIndex =0;

        //Finds node index with the longest viable path. (i.e. biggest distance)
        for (int i = 0; i < dist.size(); i++) {
            if (dist.get(maxIndex) < dist.get(i)) {
                max = dist.get(i);
                maxIndex = i;
            }
        }

        //Uses the prevNode ArrayList to find the path of the longest distance starting at the end node.
        ArrayList<Integer> path = new ArrayList<>();
        for (int i = 0; i<dist.size();i++){
            if (dist.get(i)==max){
                path.clear();
                path.add(i);
                int prev = prevNode[i];
                while (prev!=startNodeID){
                    path.add(prev);
                    prev = prevNode[prev];
                }
                path.add(startNodeID);
                paths.add(new ArrayList<Integer>(path));
            }
        }

        return paths;
    }

    /**
     * Equals method for the graph class.
     * @param o Object
     * @return boolean Whether to two classes being compared are equal.
     */
    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof graph)) {
            return false;
        }

        graph g = (graph) o;

        return ((id.equals(g.getId())) && (source.equals(g.getSource())) && (input.equals(g.getInput())) && (nodes.equals(g.getNodes())) && (tokens.equals(g.getTokens())) && (edges.equals(g.getEdges())) && tops.equals(g.getTops()));
    }


}
