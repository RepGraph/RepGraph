/**
 * The graph class represents a single sentence which comprises of nodes, edges and tokens.
 *
 * @since 29/08/2020
 */
package com.RepGraph;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    /**
     * Default constructor for the graph class.
     */
    public graph() {
    }

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
        this.source = source;
        this.input = input;
        this.nodes = nodes;
        this.tokens = tokens;
        this.edges = edges;
        this.tops = tops;
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
     * @return ArrayList<ArrayList < Integer>> The a list of longest paths in the graph.
     */
    public ArrayList<ArrayList<Integer>> findLongest(boolean directed) {

        setNodeNeighbours();

        ArrayList<ArrayList<Integer>> paths = new ArrayList<>();

        if ((nodes.size() == 0) || (edges.size() == 0)) {
            return paths;
        } else if (edges.size() == 1) {
            paths.add(new ArrayList<Integer>());
            paths.get(0).add(edges.get(0).getSource());
            paths.get(0).add(edges.get(0).getTarget());
            return paths;
        } else {

            if (directed) {
                //Default longest path set from node 0.
                ArrayList<ArrayList<Integer>> longest = directedLongestPaths(0);
                for (int i = 0; i < longest.size(); i++) {
                    paths.add(longest.get(i));
                }
                ArrayList<ArrayList<Integer>> temp;

                //Makes each node the start node and finds the longest overall path/s.
                for (int i = 1; i < nodes.size(); i++) {
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
            } else {

                if (connectedBFS()){
                    //First BFS to find end point of longest path
                    ArrayList<ArrayList<Integer>> endpoints = BFS(0);
                   // System.out.println("endpoints: " + endpoints.toString());
                    ArrayList<ArrayList<Integer>> temp;
                    ArrayList<Integer> pathEnds = new ArrayList<>();
                    int endpoint;
                    for (int i = 0; i < endpoints.size(); i++) {
                        if (!pathEnds.contains(endpoints.get(i).get(0))){
                            endpoint = endpoints.get(i).get(0);
                            //System.out.println("endpoint: " + endpoint);
                            //System.out.println(pathEnds.toString());
                            temp = BFS(endpoint);
                            for (int j = 0; j < temp.size(); j++) {
                                //System.out.println("Adding path: " + temp.get(j).toString());
                                paths.add(new ArrayList<Integer>(temp.get(j)));
                                pathEnds.add(temp.get(j).get(0));
                            }
                        }

                    }
                }
                else{

                }

            }


            //Reverses the path so that start node is first.
            ArrayList<ArrayList<Integer>> reversed = new ArrayList<>();
            ArrayList<Integer> item = new ArrayList<>();
            for (int i = 0; i < paths.size(); i++) {
                item.clear();
                for (int j = paths.get(i).size() - 1; j >= 0; j--) {
                    item.add(paths.get(i).get(j));
                }
                reversed.add(new ArrayList<>(item));
            }

            return reversed;
        }
    }

    /**
     * Assigns all the nodes in the graph their directed and undirected neighbouring nodes, which will be used for analysis.
     */
    public void setNodeNeighbours() {
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

        for (int i = 0; i < edges.size(); i++) {
            edge currentEdge = edges.get(i);

            source = currentEdge.getSource();
            target = currentEdge.getTarget();

            node sourceNode = nodes.get(source);
            sourceNode.addDirectedNeighbour(nodes.get(target));
            nodes.get(target).addUndirectedNeighbour(sourceNode);

            sourceNode.addDirectedEdgeNeighbour(currentEdge);
            nodes.get(target).addUndirectedEdgeNeighbour(currentEdge);

        }
    }

    /**
     * Finds the longest distance from a given start node to all the other nodes in the system and returns the path of the longest path.
     * @param startNodeID Number of the start node.
     * @return ArrayList<ArrayList < Integer>> The longest paths available from the start node.
     */
    public ArrayList<ArrayList<Integer>> directedLongestPaths(int startNodeID) {

        ArrayList<ArrayList<Integer>> paths = new ArrayList<>();

        //Check if the start node has any neighbours
        if (nodes.get(startNodeID).getDirectedNeighbours().size() == 0) {
            return paths;
        }

        ArrayList<Integer> dist = new ArrayList<>();
        int[] prevNode = new int[nodes.size()];

        //Set all nodes in the graph to unvisited (i.e. visited[nodeID] = false)
        boolean[] visisted = new boolean[nodes.size()];
        for (int i = 0; i < nodes.size(); i++) {
            visisted[i] = false;
        }

        //Topologically sort every unvisited node.
        Stack<Integer> stack = new Stack<Integer>();
        for (int i = 0; i < nodes.size(); i++) {
            if (!visisted[i]) {
                topologicalSort(i, visisted, stack);
            }
        }

        //Set all distances to NINF except the start node.
        for (int i = 0; i < nodes.size(); i++) {
            dist.add(Integer.MIN_VALUE);
        }
        dist.set(startNodeID, 0);

        //Iterate through the stack to find longest path.
        while (!stack.empty()) {
            int u = stack.pop();

            if (dist.get(u) != Integer.MIN_VALUE) {
                for (node n : nodes.get(u).getDirectedNeighbours()) {
                    if (dist.get(n.getId()) < dist.get(u) + 1) {
                        dist.set(n.getId(), dist.get(u) + 1);
                        prevNode[n.getId()] = u;
                    }
                }
            }
        }
        return traverseLongestPath(dist,prevNode,startNodeID);

    }

    /**
     * Recursive function that Topologically Sorts a graph.
     * @param nodeID The current node.
     * @param visited A list of booleans, representing whether a node has already been visited or not.
     */
    public Stack<Integer> topologicalSort(int nodeID, boolean visited[], Stack<Integer> stack) {
        visited[nodeID] = true;

        //Iterate through every neighbouring node of the given node.
        for (node neighbourNode : nodes.get(nodeID).getDirectedNeighbours()) {
            if (!visited[neighbourNode.getId()]) {
                topologicalSort(neighbourNode.getId(), visited, stack);
            }
        }

        stack.push(nodeID);
        return stack;
    }

    /**
     * Breadth First Search algorithm for finding the longest path from a given start node in a graph.
     * @param startNodeID The starting node ID.
     * @return ArrayList<Integer> The path of the longest route from the given start node.
     */
    public ArrayList<ArrayList<Integer>> BFS(int startNodeID) {

        ArrayList<ArrayList<Integer>> paths = new ArrayList<>();

        //Creates a list of all directed and undirected neighbours of the start node.
        ArrayList<node> allNeighbours = combineNeighbours(startNodeID);

        //Checks to see if the node has any neighbours.
        if (allNeighbours.size() == 0) {
            return paths;
        }

        //All distances from start node start at -1, except the start node.
        ArrayList<Integer> dist = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            dist.add(-1);
        }
        dist.set(startNodeID, 0);

        int[] prevNode = new int[nodes.size()];

        Queue<Integer> q = new LinkedList<>();

        q.add(startNodeID);

        while (!q.isEmpty()) {
            int currentNodeID = q.poll();

            //Combine the lists of all directed and undirected neighbours of the current node.
            allNeighbours = combineNeighbours(currentNodeID);

            //Iterate through all neighbouring nodes.
            for (int i = 0; i < allNeighbours.size(); i++) {
                int neighbourNodeID = allNeighbours.get(i).getId();

                if (dist.get(neighbourNodeID) == -1) {
                    q.add(neighbourNodeID);
                    dist.set(neighbourNodeID, dist.get(currentNodeID) + 1);
                    prevNode[neighbourNodeID] = currentNodeID;
                }
            }
        }

        return traverseLongestPath(dist,prevNode,startNodeID);
    }

    /**
     * Combines the directed and undirected node neighbours of a given node.
     * @param nodeID The ID of the node.
     * @return ArrayList<node> List of the node's directed and undirected neighbours.
     */
    public ArrayList<node> combineNeighbours(int nodeID){
        ArrayList<node> allNeighbours = new ArrayList<>(nodes.get(nodeID).getDirectedNeighbours());
        ArrayList<node> undirectedNeighbours = new ArrayList<>(nodes.get(nodeID).getUndirectedNeighbours());
        for (int i = 0; i < undirectedNeighbours.size(); i++) {
            allNeighbours.add(undirectedNeighbours.get(i));
        }
        return allNeighbours;
    }

    /**
     * Returns the longest paths given a list of distances and an array of each node's previous node in the path.
     * @param dist ArrayList of each nodes maximum distance.
     * @param prevNode Array of each node's previous node in a path.
     * @param startNodeID The node ID of the start node.
     * @return
     */
    public ArrayList<ArrayList<Integer>> traverseLongestPath(ArrayList<Integer> dist, int[] prevNode, int startNodeID){

        ArrayList<ArrayList<Integer>> paths = new ArrayList<>();
        int max = dist.get(0);
        int maxIndex = 0;

        //Finds node index with the longest viable path. (i.e. biggest distance)
        for (int i = 0; i < dist.size(); i++) {
            if (dist.get(maxIndex) < dist.get(i)) {
                max = dist.get(i);
                maxIndex = i;
            }
        }

        //Uses the prevNode ArrayList to find the path of the longest distance starting at the end node.
        ArrayList<Integer> path = new ArrayList<>();
        for (int i = 0; i < dist.size(); i++) {
            if (dist.get(i) == max) {
                path.clear();
                path.add(i);
                int prev = prevNode[i];
                while (prev != startNodeID) {
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
     * Method to check if a graph is planar
     *
     * @return boolean returns true if the graph is planar
     */
    public boolean GraphIsPlanar() {
        ArrayList<node> ordered = new ArrayList<>(nodes);

        Collections.sort(ordered, new Comparator<node>() {
            @Override
            public int compare(node o1, node o2) {
                if (o1.getAnchors().get(0).getFrom() < o2.getAnchors().get(0).getFrom()) {
                    return -1;
                } else if (o1.getAnchors().get(0).getFrom() == o2.getAnchors().get(0).getFrom()) {
                    return 0;
                }
                return 1;
            }
        });

        HashMap<Integer, Integer> nodeToToken = new HashMap<>();

        for (int i = 0; i < ordered.size(); i++) {
            nodeToToken.put(ordered.get(i).getId(), ordered.get(i).getAnchors().get(0).getFrom());
        }

        ArrayList<edge> updated = new ArrayList<>();

        int source, target;

        for (edge e : edges) {

            source = e.getSource();
            target = e.getTarget();

            edge newEdge = new edge();
            for (int i = 0; i < ordered.size(); i++) {
                node n = ordered.get(i);
                if (n.getId() == source) {
                    newEdge.setSource(nodeToToken.get(i));
                }
                if (n.getId() == target) {
                    newEdge.setTarget(nodeToToken.get(i));
                }
            }
            updated.add(newEdge);

        }


        for (edge e : updated) {

            for (edge other : updated) {

                if (Math.min(e.getSource(), e.getTarget()) < Math.min(other.getSource(), other.getTarget()) && Math.min(other.getSource(), other.getTarget()) < Math.max(e.getSource(), e.getTarget()) && Math.max(e.getSource(), e.getTarget()) < Math.max(other.getSource(), other.getTarget())) {
                    return false;
                }

            }
        }
        return true;
    }

    /**
     * Creates a list of tokens in a range starting at "from" and ending at "end"
     * @param from The start of the range of tokens
     * @param end The End of the range of tokens
     * @return ArrayList<token> Returns a list of token objects
     */
    public ArrayList<token> getTokenSpan(int from, int end) {
        ArrayList<token> returnTokens = new ArrayList<>();
        for (int i = from; i < end + 1; i++) {
            returnTokens.add(tokens.get(i));
        }
        return returnTokens;
    }

    /**
     * gets the form of tokens and turns them into a string
     * @param tokenIn List of tokens to become a string
     * @return String This is the string of all token's form.
     */
    public String getTokenInput(ArrayList<token> tokenIn) {
        String output = "";

        for (token t : tokenIn) {
            output += t.getForm() + " ";

        }
        return output;
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

    /**
     * Breadth First Search algorithm for determining whether a graph is connected or not.
     * @return boolean Whether the graph is connected or not.
     */
    public boolean connectedBFS() {

        setNodeNeighbours();

        int startNodeID = 0;

        if (nodes.size() <= 1) {
            return true;
        }
        //Creates a list of all directed and undirected neighbours of the start node.
        ArrayList<node> allNeighbours = new ArrayList<node>(nodes.get(startNodeID).getDirectedNeighbours());
        ArrayList<node> undirectedNeighbours = new ArrayList<node>(nodes.get(startNodeID).getUndirectedNeighbours());
        for (int i = 0; i < undirectedNeighbours.size(); i++) {
            allNeighbours.add(undirectedNeighbours.get(i));
        }

        //Checks to see if the node has any neighbours.
        if (allNeighbours.size() == 0) {
            return false;
        }

        //All distances from start node start at -1, except the start node.
        ArrayList<Integer> dist = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            dist.add(-1);
        }
        dist.set(startNodeID, 0);

        int nodesVisited = 0;

        Queue<Integer> q = new LinkedList<>();
        q.add(startNodeID);


        while (!q.isEmpty()) {
            int currentNodeID = q.poll();
            nodesVisited++;

            //Combine the lists of all directed and undirected neighbours of the current node.
            allNeighbours = new ArrayList<node>(nodes.get(currentNodeID).getDirectedNeighbours());
            undirectedNeighbours = new ArrayList<node>(nodes.get(currentNodeID).getUndirectedNeighbours());
            for (int i = 0; i < undirectedNeighbours.size(); i++) {
                allNeighbours.add(undirectedNeighbours.get(i));
            }

            //Iterate through all neighbouring nodes
            for (int i = 0; i < allNeighbours.size(); i++) {
                int neighbourNodeID = allNeighbours.get(i).getId();

                if (dist.get(neighbourNodeID) == -1) {
                    q.add(neighbourNodeID);
                    dist.set(neighbourNodeID, dist.get(currentNodeID) + 1);
                }
            }
        }

        if (nodesVisited < nodes.size()) {
            return false;
        } else {
            return true;
        }
    }

}
