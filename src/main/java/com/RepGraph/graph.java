package com.RepGraph;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;

import java.io.IOException;
import java.util.*;

import static java.util.Collections.max;

/**
 * The graph class represents a single sentence which comprises of nodes, edges and tokens.
 */
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
     * The graphs linear list of nodes - used for returning graph data correctly
     */
    @JsonProperty("nodes")
    private ArrayList<node> nodelist;

    /**
     * A HashMap of the graph nodes with the node's ID as the key - used for quicker,easier and safer algorithmic use of node data
     */
    @JsonIgnore
    private HashMap<Integer, node> nodes;

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
        this.nodelist = new ArrayList<>();
        this.nodes = new HashMap<>();
        this.tokens = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.tops = new ArrayList<>();
    }

    /**
     * parameterised constructor for the graph class that takes in the node's Hashmap instead of the linear nodeslist
     * and populates the nodeslist from the hashmap information.
     *
     * @param id     The graph's ID number.
     * @param source The graph's source.
     * @param input  The graph's input/sentence.
     * @param nodes  An HashMap of the graph's nodes with the node's ID as the key
     * @param tokens An array list of the graph's tokens.
     * @param edges  An array list of the graph's edges.
     * @param tops   An array list of the graph's top node ids.
     */
    public graph(String id, String source, String input, HashMap<Integer, node> nodes, ArrayList<token> tokens, ArrayList<edge> edges, ArrayList<Integer> tops) {
        this.id = id;
        this.source = source;
        this.input = input;
        this.nodelist = new ArrayList<>();
        this.nodes = nodes;
        for (node n : nodes.values()) {
            this.nodelist.add(n);
        }
        this.tokens = tokens;
        this.edges = edges;
        this.tops = tops;

    }

    /**
     * parameterised constructor for the graph class that takes in the linear nodeslist instead of the hashmap nodes
     * and populates the nodes hashmap from the list information.
     *
     * @param id       The graph's ID number.
     * @param source   The graph's source.
     * @param input    The graph's input/sentence.
     * @param nodelist An ArrayList of the graphs linear list of nodes
     * @param tokens   An array list of the graph's tokens.
     * @param edges    An array list of the graph's edges.
     * @param tops     An array list of the graph's top node ids.
     */
    public graph(String id, String source, String input, ArrayList<node> nodelist, ArrayList<token> tokens, ArrayList<edge> edges, ArrayList<Integer> tops) {
        this.id = id;
        this.source = source;
        this.input = input;
        this.nodelist = nodelist;
        this.nodes = new HashMap<Integer, node>();
        for (node n : nodelist) {
            this.nodes.put(n.getId(), n);
        }
        this.tokens = tokens;
        this.edges = edges;
        this.tops = tops;

    }

    /**
     * This is the getter for the linear list of nodes "nodeslist"
     *
     * @return ArrayList<node> This is the linear list of nodes of the graph object
     */
    @JsonGetter("nodes")
    public ArrayList<node> getNodelist() {
        return this.nodelist;
    }

    /**
     * The Setter method for the linear nodeslist attribute. The setter of the linear list of nodes also resets and populates the hashmap nodes.
     *
     * @param nodelist This is the linear list of nodes is used to set the objects list of nodes.
     */
    @JsonSetter("nodes")
    public void setNodelist(ArrayList<node> nodelist) {
        this.nodelist = nodelist;
        nodes = new HashMap<Integer, node>();
        for (node n : nodelist) {
            nodes.put(n.getId(), n);
        }
    }

    /**
     * Getter method for the graph's ID number.
     *
     * @return String The graph's ID number.
     */
    public String getId() {
        return id;
    }

    /**
     * Setter method for the graph's ID number.
     *
     * @param id The graph's ID number.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter method for the graph's source.
     *
     * @return String The graph's source.
     */
    public String getSource() {
        return source;
    }

    /**
     * Setter method for the graph's source.
     *
     * @param source The graph's source.
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * Getter method for the graph's input.
     *
     * @return String The graph's input.
     */
    public String getInput() {
        return input;
    }

    /**
     * Setter method for the graph's input.
     *
     * @param input The graph's input.
     */
    public void setInput(String input) {
        this.input = input;
    }

    /**
     * Getter method for the graph's nodes HashMap.
     *
     * @return HashMap<Integer, node> The graph's nodes HashMap.
     */
    public HashMap<Integer, node> getNodes() {
        return nodes;
    }

    /**
     * Setter method for the graph's nodes HashMap. This setter also resets and populates the linear list of nodes "nodeslist"
     *
     * @param nodes The graph's nodes HashMap.
     */
    public void setNodes(HashMap<Integer, node> nodes) {
        this.nodelist.clear();
        this.nodes = nodes;
        for (node n : nodes.values()) {
            this.nodelist.add(n);
        }
    }

    /**
     * Getter method for the graph's tokens.
     *
     * @return ArrayList The graph's tokens.
     */
    public ArrayList<token> getTokens() {
        return tokens;
    }

    /**
     * Setter method for the graph's tokens.
     *
     * @param tokens The graph's tokens.
     */
    public void setTokens(ArrayList<token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Getter method for the graph's edges.
     *
     * @return ArrayList The graph's edges.
     */
    public ArrayList<edge> getEdges() {
        return edges;
    }

    /**
     * Setter method for the graph's edges.
     *
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
     * Analysis Tool for finding the longest paths in the graph.
     *
     * @param directed This is the boolean to decide if the longest path found is directed or undirected.
     * @return ArrayList<ArrayList < Integer>> The a list of longest paths in the graph.
     */
    public ArrayList<ArrayList<Integer>> findLongest(boolean directed) {

        //Ensure that all graph node's have their neighbours assigned
        setNodeNeighbours();

        ArrayList<ArrayList<Integer>> paths = new ArrayList<>();

        if ((nodes.size() == 0) || (edges.size() == 0)) { //No edges or nodes in graph i.e. path is empty.

            return paths;

        } else if (edges.size() == 1) {//Only 1 edge in the graph i.e. path is the source node to the target node of the edge

            paths.add(new ArrayList<>());
            paths.get(0).add(edges.get(0).getSource());
            paths.get(0).add(edges.get(0).getTarget());
            return paths;

        } else {

            if (!directed) {

                if (connectedBFS(nodes.values().iterator().next().getId())) {
                    //Finding the longest path in a undirected connected graph.

                    //Find the longest paths from the first node in the graph.
                    ArrayList<ArrayList<Integer>> endpoints = BFS(nodes.values().iterator().next().getId());
                    ArrayList<ArrayList<Integer>> temp;
                    ArrayList<Integer> pathEnds = new ArrayList<>();

                    //Iterate through each path's end points (which is in position 0 as the path is reversed)
                    for (ArrayList<Integer> end : endpoints) {
                        if (!pathEnds.contains(end.get(0))) { //Avoids identical paths that are just reversed.
                            temp = BFS(end.get(0)); //Finds the true longest path from this particular end point.
                            for (ArrayList<Integer> al : temp) {//Add path to longest paths.
                                paths.add(new ArrayList<>(al));
                                pathEnds.add(al.get(0));
                            }
                        }
                    }
                } else { //Finding the longest path in a undirected disconnected graph.

                    //Default longest path set from the first node in the graph.
                    ArrayList<ArrayList<Integer>> longest;
                    longest = BFS(nodes.values().iterator().next().getId());

                    //Add longest paths to the overall longest paths
                    for (ArrayList<Integer> al : longest) {
                        paths.add(al);
                    }

                    ArrayList<ArrayList<Integer>> temp;

                    //Makes each node the start node and compared the longest paths found from that node to the current best longest path.
                    for (int i : nodes.keySet()) {
                        temp = BFS(i);

                        if (temp.size() != 0) {
                            if ((longest.size() == 0) || (temp.get(0).size() > longest.get(0).size())) {//New path is longest than the current longest path, so overwrite the overall longest paths
                                longest = temp;
                                paths.clear();
                                for (ArrayList<Integer> al : temp) {
                                    paths.add(new ArrayList<>(al));
                                }
                            } else if (temp.get(0).size() == longest.get(0).size()) {//New path is the same length as the current longest path, so add it to the overall longest paths
                                for (ArrayList<Integer> al : temp) {
                                    paths.add(new ArrayList<>(al));
                                }
                            }
                        }
                    }

                    //Reverse the each of the longest paths so that they start with the smaller node ID. Removes duplicate but reversed paths as well.
                    ArrayList<ArrayList<Integer>> newPaths = new ArrayList<>();
                    for (ArrayList<Integer> al : paths) {
                        if (!newPaths.contains(al)) {
                            Collections.reverse(al);
                            if (!newPaths.contains(al)) {
                                newPaths.add(al);
                            }
                        }
                    }
                    return newPaths;
                }
            } else {//Finding the longest path in a directed graph.

                //Default longest path set from first node in the graph.
                ArrayList<ArrayList<Integer>> longest;
                Set<Integer> nodesInPath = new HashSet<>(); //Keeps track of all nodes in the overall longest path.
                longest = directedLongestPaths(nodes.values().iterator().next().getId());

                //Add longest paths to the overall longest paths
                for (ArrayList<Integer> integers : longest) {
                    paths.add(integers);
                    for (int n : integers) {
                        nodesInPath.add(n);
                    }
                }

                ArrayList<ArrayList<Integer>> temp;

                //Makes each node the start node and finds the longest overall path/s.
                for (int i : nodes.keySet()) {
                    if (!nodesInPath.contains(i)) {//Cuts processing time by not letting a node already in the overall longest path be a start node, as the longest path from that node will never yield a longer path than the current overall longest path.
                        temp = directedLongestPaths(i);

                        if (temp.size() != 0) {
                            if ((longest.size() == 0) || (temp.get(0).size() > longest.get(0).size())) {//New path is longest than the current longest path, so overwrite the overall longest paths
                                longest = temp;
                                paths.clear();
                                for (ArrayList<Integer> al : temp) {
                                    paths.add(new ArrayList<>(al));
                                    for (int n : al) {
                                        nodesInPath.add(n);
                                    }
                                }
                            } else if (temp.get(0).size() == longest.get(0).size()) {//New path is the same length as the current longest path, so add it to the overall longest paths
                                for (ArrayList<Integer> al : temp) {
                                    paths.add(new ArrayList<>(al));
                                    for (int n : al) {
                                        nodesInPath.add(n);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //Reverses the path so that start node is first.
            for (ArrayList<Integer> item : paths) {
                Collections.reverse(item);
            }

            return paths;
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

            if (!nodes.containsKey(source) || nodes.get(source).getDirectedNeighbours().size() != 0) {
                //Node neighbours have already been set
                return;
            }
        }

        //Iterate through each edge and set the corresponding node's thier neighbours.
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
     * This method checks to see if a graph has a dangling edge i.e an edge that doesnt connect to any node
     *
     * @return boolean returns true if the graph has a dangling edge otherwise returns false.
     */
    public boolean hasDanglingEdge() {
        for (edge e : edges) {
            if (!nodes.containsKey(e.getTarget()) || !nodes.containsKey(e.getSource())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the longest paths from a given start node to all the other nodes in the system and returns the paths of the longest paths.
     *
     * @param startNodeID Number of the start node.
     * @return ArrayList<ArrayList < Integer>> The longest paths available from the start node.
     */
    public ArrayList<ArrayList<Integer>> directedLongestPaths(int startNodeID) {

        ArrayList<ArrayList<Integer>> paths = new ArrayList<>();

        //Check if the start node has any neighbours, if not then return an empty path.
        if (nodes.get(startNodeID).getDirectedNeighbours().size() == 0) {
            return paths;
        }

        HashMap<Integer, Integer> dist = new HashMap<>(); //HashMap to keep track of each node's distance from start node
        HashMap<Integer, Integer> prevNode = new HashMap<>(); //HashMap to keep track of each node's previous node in the path

        //Set all nodes in the graph to unvisited
        HashMap<Integer, Boolean> visited = new HashMap<>();
        for (int i : nodes.keySet()) {
            visited.put(i, false);
        }

        //Topologically sort every unvisited node, which gets added to the stack.
        Stack<Integer> stack = new Stack<Integer>();
        for (int i : nodes.keySet()) {
            if (!visited.get(i)) {
                topologicalSort(i, visited, stack);
            }
        }

        //Set all distances to NINF except the start node.
        for (int i : nodes.keySet()) {
            dist.put(i, Integer.MIN_VALUE);
        }
        dist.put(startNodeID, 0);

        //Iterate through the stack to find longest path.
        while (!stack.empty()) {
            int u = stack.pop();

            if (dist.get(u) != Integer.MIN_VALUE) {
                for (node n : nodes.get(u).getDirectedNeighbours()) {
                    if (dist.get(n.getId()) < dist.get(u) + 1) { //Check if the current distance is shorter than the new distance, if so update the distance and previous node arrays.
                        dist.put(n.getId(), dist.get(u) + 1);
                        prevNode.put(n.getId(), u);
                    }
                }
            }
        }
        return traverseLongestPath(dist, prevNode, startNodeID);

    }

    /**
     * Recursive function that Topologically Sorts a graph.
     *
     * @param nodeID  The current node.
     * @param visited A Hashmap of integer keys and boolean values,
     *                representing whether a node (represented by its ID in the key of the hashmap)
     *                has already been visited or not
     *                i.e if it has been visited the boolean value of the hashmap will be true.
     * @param stack   ?
     * @return Stack<Integer>
     */
    public Stack<Integer> topologicalSort(int nodeID, HashMap<Integer, Boolean> visited, Stack<Integer> stack) {
        visited.put(nodeID, true);

        //Iterate through every neighbouring node of the given node.
        for (node neighbourNode : nodes.get(nodeID).getDirectedNeighbours()) {
            if (!visited.get(neighbourNode.getId())) { //If node is unvisited then topologically sort it.
                topologicalSort(neighbourNode.getId(), visited, stack);
            }
        }

        stack.push(nodeID);
        return stack;
    }

    /**
     * Breadth First Search algorithm for finding the longest path from a given start node in a graph.
     *
     * @param startNodeID The starting node ID.
     * @return ArrayList<ArrayList < Integer>>
     */
    public ArrayList<ArrayList<Integer>> BFS(int startNodeID) {

        ArrayList<ArrayList<Integer>> paths = new ArrayList<>();

        //Creates a list of all directed and undirected neighbours of the start node.
        ArrayList<node> allNeighbours = combineNeighbours(startNodeID);

        //Checks to see if the node has any neighbours. If not, then return empty path.
        if (allNeighbours.size() == 0) {
            return paths;
        }

        HashMap<Integer, Integer> dist = new HashMap<>();//HashMap to keep track of each node's distance from start node

        // All distances from start node start at -1, except the start node.
        for (int i : nodes.keySet()) {
            dist.put(i, -1);
        }
        dist.put(startNodeID, 0);

        HashMap<Integer, Integer> prevNode = new HashMap<>();//HashMap to keep track of each node's previous node in the path

        Queue<Integer> q = new LinkedList<>();

        q.add(startNodeID);

        //Iterate through the queue of nodes until it is empty.
        while (!q.isEmpty()) {
            int currentNodeID = q.poll();

            //Combine the lists of all directed and undirected neighbours of the current node.
            allNeighbours = combineNeighbours(currentNodeID);

            //Iterate through all neighbouring nodes.
            for (int i = 0; i < allNeighbours.size(); i++) {
                int neighbourNodeID = allNeighbours.get(i).getId();

                if (dist.get(neighbourNodeID) == -1) {//Check if the node is unvisited. If so, add it to the queue, update its distance and its previous node.
                    q.add(neighbourNodeID);
                    dist.put(neighbourNodeID, dist.get(currentNodeID) + 1);
                    prevNode.put(neighbourNodeID, currentNodeID);
                }
            }
        }

        return traverseLongestPath(dist, prevNode, startNodeID);
    }

    /**
     * Combines the directed and undirected node neighbours of a given node.
     *
     * @param nodeID The ID of the node.
     * @return ArrayList<node> List of the node's directed and undirected neighbours.
     */
    public ArrayList<node> combineNeighbours(int nodeID) {
        ArrayList<node> allNeighbours = new ArrayList<>(nodes.get(nodeID).getDirectedNeighbours());
        ArrayList<node> undirectedNeighbours = new ArrayList<>(nodes.get(nodeID).getUndirectedNeighbours());
        //Adds all a node's undirected neighbours and its directed neighbours together.
        allNeighbours.addAll(undirectedNeighbours);
        return allNeighbours;
    }

    /**
     * Returns the longest paths given a list of distances and an array of each node's previous node in the path.
     *
     * @param dist        A HashMap<Integer, Integer> of each nodes maximum distance
     *                    with the node's ID being the key and the maximum distance being the value.
     * @param prevNode    HashMap<Integer, Integer> of each node's previous node in a path
     *                    with the Node's ID being the key and the previous node's ID being te value..
     * @param startNodeID The node ID of the start node.
     * @return ArrayList<ArrayList < Integer>> The longest paths.
     */
    public ArrayList<ArrayList<Integer>> traverseLongestPath(HashMap<Integer, Integer> dist, HashMap<Integer, Integer> prevNode,
                                                             int startNodeID) {

        ArrayList<ArrayList<Integer>> paths = new ArrayList<>();
        int max = Collections.max(dist.values()); //Find the longest distance in the distance array

        //Uses the prevNode ArrayList to find the path of the longest distance starting at the end node.
        ArrayList<Integer> path = new ArrayList<>();
        for (int i : dist.keySet()) {
            if (dist.get(i) == max) { //i.e. a longest path
                path.clear();
                path.add(i);
                int prev = prevNode.get(i);
                while (prev != startNodeID) {//Iterate through the previous node array until you reach the start node.
                    path.add(prev);
                    prev = prevNode.get(prev);
                }
                path.add(startNodeID);
                paths.add(new ArrayList<>(path)); //Add the path to the list of overall longest paths
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

        ArrayList<node> ordered = new ArrayList<>();
        //add the node objects to a list
        for (node n : nodes.values()) {
            ordered.add(new node(n));
        }

        //order the nodes according to the beginning of their span
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

        //Map node ids to their token span beginning
        for (int i = 0; i < ordered.size(); i++) {
            nodeToToken.put(ordered.get(i).getId(), ordered.get(i).getAnchors().get(0).getFrom());
        }

        ArrayList<edge> updated = new ArrayList<>();

        int source, target;

        //for all edges change the source and target to refer to their span beginning of the respective nodes
        for (edge e : edges) {

            source = e.getSource();
            target = e.getTarget();

            edge newEdge = new edge();

            for (int i = 0; i < ordered.size(); i++) {
                node n = ordered.get(i);
                if (n.getId() == source) {
                    newEdge.setSource(nodeToToken.get(n.getId()));
                }
                if (n.getId() == target) {
                    newEdge.setTarget(nodeToToken.get(n.getId()));
                }
            }
            updated.add(newEdge);

        }

        //check every edge with every other edge to see if any are crossing
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
     * Returns a graph object in it's Planar format i.e
     * Orders the nodes linearly based on their anchor's "from" positions
     * and updates all edges to point to their corresponding new "IDs" which are represented by their token index
     *
     * @return graph This is the graph in a format to indicate its planarity.
     */
    public graph PlanarGraph() {

        ArrayList<node> ordered = new ArrayList<>();
        for (node n : nodes.values()) {
            ordered.add(new node(n));
        }

        Collections.sort(ordered, new Comparator<node>() {
            @Override
            public int compare(node o1, node o2) {
                o1.getAnchors().get(0).setEnd(o1.getAnchors().get(0).getFrom());
                o2.getAnchors().get(0).setEnd(o2.getAnchors().get(0).getFrom());
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
            newEdge.setLabel("");
            newEdge.setPostLabel("");
            for (int i = 0; i < ordered.size(); i++) {

                node n = ordered.get(i);

                if (n.getId() == source) {

                    newEdge.setSource(nodeToToken.get(n.getId()));

                }
                if (n.getId() == target) {

                    newEdge.setTarget(nodeToToken.get(n.getId()));

                }
            }
            updated.add(newEdge);

        }

        HashMap<Integer, node> newNodes = new HashMap<>();
        for (node n : ordered) {
            newNodes.put(n.getId(), n);
        }

        graph planarVisualisation = new graph(this.getId(), this.getSource(), this.input, newNodes, this.tokens, updated, this.tops);

        return planarVisualisation;

    }

    /**
     * Creates a list of tokens in a range starting at "from" and ending at "end"
     *
     * @param from The start of the range of tokens
     * @param end  The End of the range of tokens
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
     * gets the "form" of tokens and turns them into a string
     *
     * @param tokenIn List of tokens to become a string
     * @return String This is the string of all token's form.
     */
    public String getTokenInput(ArrayList<token> tokenIn) {
        String output = "";

        for (token t : tokenIn) {
            output += " " + t.getForm();

        }

        return output.trim();
    }

    /**
     * Equals method for the graph class.
     *
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
     *
     * @return boolean Whether the graph is connected or not.
     */
    public boolean connectedBFS(int startNodeID) {

        setNodeNeighbours();


        if (nodes.size() <= 1) { //If there is only 1 node or less, then the graph is connected
            return true;
        }

        //Creates a list of all directed and undirected neighbours of the start node.
        ArrayList<node> allNeighbours = combineNeighbours(startNodeID);

        //Checks to see if the node has any neighbours, if not, return false
        if (allNeighbours.size() == 0) {
            return false;
        }


        HashMap<Integer, Integer> dist = new HashMap<>();//HashMap to keep track of each node's distance from start node

        //All distances from start node start at -1, except the start node.
        for (int i : nodes.keySet()) {
            dist.put(i, -1);
        }
        dist.put(startNodeID, 0);

        int nodesVisited = 0;

        Queue<Integer> q = new LinkedList<>();
        q.add(startNodeID);

        //Iterate through the queue of nodes until it is empty.
        while (!q.isEmpty()) {
            int currentNodeID = q.poll();
            nodesVisited++;

            //Combine the lists of all directed and undirected neighbours of the current node.
            allNeighbours = combineNeighbours(currentNodeID);

            //Iterate through all neighbouring nodes
            for (int i = 0; i < allNeighbours.size(); i++) {
                int neighbourNodeID = allNeighbours.get(i).getId();

                if (dist.get(neighbourNodeID) == -1) {//Check if the node is unvisited. If so, add it to the queue and update its distance.
                    q.add(neighbourNodeID);
                    dist.put(neighbourNodeID, dist.get(currentNodeID) + 1);
                }
            }
        }

        if (nodesVisited < nodes.size()) { //If there were any unvisited nodes then the graph is not connected
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks if a directed or an undirected graph is cyclic or not by calling on the appropriate recursive function required.
     *
     * @param directed Boolean to see if the graph is directed or not.
     * @return Boolean If the graph is cyclic or not.
     */
    public boolean isCyclic(boolean directed) {

        setNodeNeighbours();

        //Mark nodes as unvisited and not in the stack (stack is for directed graphs only)
        HashMap<node, Boolean> visited = new HashMap<>();
        HashMap<node, Boolean> stack = new HashMap<>();
        for (node n : nodes.values()) {
            visited.put(n, false);
            stack.put(n, false);
        }

        if (directed) {
            //Call recursive function to detect cycles in different DFS directed trees.
            for (node n : nodes.values()) {
                if (isCyclicCheckerDirected(n, visited, stack)) {
                    return true;
                }
            }
        } else {
            //Call recursive function to detect cycles in different DFS undirected trees.
            for (node n : nodes.values()) {
                if (!visited.get(n)) { //Check if the node hasn't already been visited
                    if (isCyclicCheckerUndirected(n, visited, -1)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Recursive function that checks for cycles in an undirected graph.
     *
     * @param v       The current node.
     * @param visited A HashMap of node to boolean to keep track of which nodes have been visited already.
     * @param parent  The ID of the parent node of the current node.
     * @return Boolean If the graph is cyclic or not.
     */
    public boolean isCyclicCheckerUndirected(node v, HashMap<node, Boolean> visited, int parent) {

        //Set the current node as visited.
        visited.put(v, true);

        //Iterate through all the current node's neighbouring nodes
        for (node neighbour : combineNeighbours(v.getId())) {
            if (!visited.get(neighbour)) { //If the neighbour is unvisited
                if (isCyclicCheckerUndirected(neighbour, visited, v.getId())) {
                    return true;
                }
                ;
            } else if (neighbour.getId() != parent) { //If the neighbouring is visited and not a parent of the current node, then there is a cycle.
                return true;
            }
        }

        return false;
    }

    /**
     * Recursive function that checks for cycles in an directed graph.
     *
     * @param v       The current node.
     * @param visited A HashMap of node to boolean to keep track of which nodes have been visited already.
     * @param stack   A HashMap of node to boolean to keep track of which nodes have been added to the stack.
     * @return Boolean If the graph is cyclic or not.
     */
    public boolean isCyclicCheckerDirected(node v, HashMap<node, Boolean> visited, HashMap<node, Boolean> stack) {

        //Check if the node is in the stack already.
        if (stack.get(v)) {
            return true;
        }

        // Check if the node has already been visited.
        if (visited.get(v)) {
            return false;
        }

        //Set stack and visited to true.
        stack.put(v, true);
        visited.put(v, true);

        //Iterate through the node's directed neighbours and call recursive function.
        for (node neighbour : v.getDirectedNeighbours()) {
            if (isCyclicCheckerDirected(neighbour, visited, stack)) {
                return true;
            }
        }

        stack.put(v, false);

        return false;
    }
}
