package com.RepGraph;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.*;

import static java.util.Collections.max;

/**
 * The Graph class represents a single sentence which comprises of nodes, edges and tokens.
 */
public class Graph {


    /**
     * The Graph's ID number.
     */
    private String id;

    /**
     * The Graph's source.
     */
    private String source;

    /**
     * The Graph's input/sentence.
     */
    private String input;


    /**
     * The graphs linear list of nodes - used for returning Graph data correctly
     */
    @JsonProperty("nodes")
    private ArrayList<Node> nodelist;

    /**
     * A HashMap of the Graph nodes with the Node's ID as the key - used for quicker,easier and safer algorithmic use of Node data
     */
    @JsonIgnore
    private HashMap<Integer, Node> nodes;

    /**
     * An array list of the Graph's tokens.
     */
    private ArrayList<Token> tokens;

    /**
     * An array list of the Graph's edges.
     */
    private ArrayList<Edge> edges;

    /**
     * An array list of the Graph's top Node ids.
     */
    @JsonProperty("tops")
    private ArrayList<Integer> tops;

    /**
     * Default constructor for the Graph class.
     */
    public Graph() {
        this.nodelist = new ArrayList<>();
        this.nodes = new HashMap<>();
        this.tokens = new ArrayList<>();
        this.edges = new ArrayList<>();
        this.tops = new ArrayList<>();
    }

    /**
     * parameterised constructor for the Graph class that takes in the Node's Hashmap instead of the linear nodeslist
     * and populates the nodeslist from the hashmap information.
     *
     * @param id     The Graph's ID number.
     * @param source The Graph's source.
     * @param input  The Graph's input/sentence.
     * @param nodes  An HashMap of the Graph's nodes with the Node's ID as the key
     * @param tokens An array list of the Graph's tokens.
     * @param edges  An array list of the Graph's edges.
     * @param tops   An array list of the Graph's top Node ids.
     */
    public Graph(String id, String source, String input, HashMap<Integer, Node> nodes, ArrayList<Token> tokens, ArrayList<Edge> edges, ArrayList<Integer> tops) {
        this.id = id;
        this.source = source;
        this.input = input;
        this.nodelist = new ArrayList<>();
        this.nodes = nodes;
        for (Node n : nodes.values()) {
            this.nodelist.add(n);
        }
        this.tokens = tokens;
        this.edges = edges;
        this.tops = tops;

    }

    /**
     * parameterised constructor for the Graph class that takes in the linear nodeslist instead of the hashmap nodes
     * and populates the nodes hashmap from the list information.
     *
     * @param id       The Graph's ID number.
     * @param source   The Graph's source.
     * @param input    The Graph's input/sentence.
     * @param nodelist An ArrayList of the graphs linear list of nodes
     * @param tokens   An array list of the Graph's tokens.
     * @param edges    An array list of the Graph's edges.
     * @param tops     An array list of the Graph's top Node ids.
     */
    public Graph(String id, String source, String input, ArrayList<Node> nodelist, ArrayList<Token> tokens, ArrayList<Edge> edges, ArrayList<Integer> tops) {
        this.id = id;
        this.source = source;
        this.input = input;
        this.nodelist = nodelist;
        this.nodes = new HashMap<Integer, Node>();
        for (Node n : nodelist) {
            this.nodes.put(n.getId(), n);
        }
        this.tokens = tokens;
        this.edges = edges;
        this.tops = tops;

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
        nodes = new HashMap<Integer, Node>();
        for (Node n : nodelist) {
            nodes.put(n.getId(), n);
        }
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
     * Getter method for the Graph's nodes HashMap.
     *
     * @return HashMap<Integer, Node> The Graph's nodes HashMap.
     */
    public HashMap<Integer, Node> getNodes() {
        return nodes;
    }

    /**
     * Setter method for the Graph's nodes HashMap. This setter also resets and populates the linear list of nodes "nodeslist"
     *
     * @param nodes The Graph's nodes HashMap.
     */
    public void setNodes(HashMap<Integer, Node> nodes) {
        this.nodelist.clear();
        this.nodes = nodes;
        for (Node n : nodes.values()) {
            this.nodelist.add(n);
        }
    }

    /**
     * Getter method for the Graph's tokens.
     *
     * @return ArrayList The Graph's tokens.
     */
    public ArrayList<Token> getTokens() {
        return tokens;
    }

    /**
     * Setter method for the Graph's tokens.
     *
     * @param tokens The Graph's tokens.
     */
    public void setTokens(ArrayList<Token> tokens) {
        this.tokens = tokens;
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
    public ArrayList<Integer> getTops() {
        return tops;
    }

    /**
     * Setter method for a graphs top Node array
     *
     * @param tops This is an ArrayList of the ids of the top nodes in a Graph
     */
    public void setTops(ArrayList<Integer> tops) {
        this.tops = tops;
    }

    /**
     * Analysis Tool for finding the longest paths in the Graph.
     *
     * @param directed This is the boolean to decide if the longest path found is directed or undirected.
     * @return ArrayList<ArrayList < Integer>> The a list of longest paths in the Graph.
     */
    public ArrayList<ArrayList<Integer>> findLongest(boolean directed) {

        //Ensure that all Graph Node's have their neighbours assigned
        setNodeNeighbours();

        ArrayList<ArrayList<Integer>> paths = new ArrayList<>();

        if ((nodes.size() == 0) || (edges.size() == 0)) { //No edges or nodes in Graph i.e. path is empty.

            return paths;

        } else if (edges.size() == 1) {//Only 1 Edge in the Graph i.e. path is the source Node to the target Node of the Edge

            paths.add(new ArrayList<>());
            paths.get(0).add(edges.get(0).getSource());
            paths.get(0).add(edges.get(0).getTarget());
            return paths;

        } else {

            if (!directed) {

                if (connectedBFS(nodes.values().iterator().next().getId())) {
                    //Finding the longest path in a undirected connected Graph.

                    //Find the longest paths from the first Node in the Graph.
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
                } else { //Finding the longest path in a undirected disconnected Graph.

                    //Default longest path set from the first Node in the Graph.
                    ArrayList<ArrayList<Integer>> longest;
                    longest = BFS(nodes.values().iterator().next().getId());

                    //Add longest paths to the overall longest paths
                    for (ArrayList<Integer> al : longest) {
                        paths.add(al);
                    }

                    ArrayList<ArrayList<Integer>> temp;

                    //Makes each Node the start Node and compared the longest paths found from that Node to the current best longest path.
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

                    //Reverse the each of the longest paths so that they start with the smaller Node ID. Removes duplicate but reversed paths as well.
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
            } else {//Finding the longest path in a directed Graph.

                //Default longest path set from first Node in the Graph.
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

                //Makes each Node the start Node and finds the longest overall path/s.
                for (int i : nodes.keySet()) {
                    if (!nodesInPath.contains(i)) {//Cuts processing time by not letting a Node already in the overall longest path be a start Node, as the longest path from that Node will never yield a longer path than the current overall longest path.
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

            //Reverses the path so that start Node is first.
            for (ArrayList<Integer> item : paths) {
                Collections.reverse(item);
            }

            return paths;
        }

    }

    /**
     * Assigns all the nodes in the Graph their directed and undirected neighbouring nodes, which will be used for analysis.
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

        //Iterate through each Edge and set the corresponding Node's thier neighbours.
        for (int i = 0; i < edges.size(); i++) {
            Edge currentEdge = edges.get(i);

            source = currentEdge.getSource();
            target = currentEdge.getTarget();

            Node sourceNode = nodes.get(source);
            sourceNode.addDirectedNeighbour(nodes.get(target));
            nodes.get(target).addUndirectedNeighbour(sourceNode);

            sourceNode.addDirectedEdgeNeighbour(currentEdge);
            nodes.get(target).addUndirectedEdgeNeighbour(currentEdge);

        }

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
     * Finds the longest paths from a given start Node to all the other nodes in the system and returns the paths of the longest paths.
     *
     * @param startNodeID Number of the start Node.
     * @return ArrayList<ArrayList < Integer>> The longest paths available from the start Node.
     */
    public ArrayList<ArrayList<Integer>> directedLongestPaths(int startNodeID) {

        ArrayList<ArrayList<Integer>> paths = new ArrayList<>();

        //Check if the start Node has any neighbours, if not then return an empty path.
        if (nodes.get(startNodeID).getDirectedNeighbours().size() == 0) {
            return paths;
        }

        HashMap<Integer, Integer> dist = new HashMap<>(); //HashMap to keep track of each Node's distance from start Node
        HashMap<Integer, Integer> prevNode = new HashMap<>(); //HashMap to keep track of each Node's previous Node in the path

        //Set all nodes in the Graph to unvisited
        HashMap<Integer, Boolean> visited = new HashMap<>();
        for (int i : nodes.keySet()) {
            visited.put(i, false);
        }

        //Topologically sort every unvisited Node, which gets added to the stack.
        Stack<Integer> stack = new Stack<Integer>();
        for (int i : nodes.keySet()) {
            if (!visited.get(i)) {
                topologicalSort(i, visited, stack);
            }
        }

        //Set all distances to NINF except the start Node.
        for (int i : nodes.keySet()) {
            dist.put(i, Integer.MIN_VALUE);
        }
        dist.put(startNodeID, 0);

        //Iterate through the stack to find longest path.
        while (!stack.empty()) {
            int u = stack.pop();

            if (dist.get(u) != Integer.MIN_VALUE) {
                for (Node n : nodes.get(u).getDirectedNeighbours()) {
                    if (dist.get(n.getId()) < dist.get(u) + 1) { //Check if the current distance is shorter than the new distance, if so update the distance and previous Node arrays.
                        dist.put(n.getId(), dist.get(u) + 1);
                        prevNode.put(n.getId(), u);
                    }
                }
            }
        }
        return traverseLongestPath(dist, prevNode, startNodeID);

    }

    /**
     * Recursive function that Topologically Sorts a Graph.
     *
     * @param nodeID  The current Node.
     * @param visited A Hashmap of integer keys and boolean values,
     *                representing whether a Node (represented by its ID in the key of the hashmap)
     *                has already been visited or not
     *                i.e if it has been visited the boolean value of the hashmap will be true.
     * @param stack   ?
     * @return Stack<Integer>
     */
    public Stack<Integer> topologicalSort(int nodeID, HashMap<Integer, Boolean> visited, Stack<Integer> stack) {
        visited.put(nodeID, true);

        //Iterate through every neighbouring Node of the given Node.
        for (Node neighbourNode : nodes.get(nodeID).getDirectedNeighbours()) {
            if (!visited.get(neighbourNode.getId())) { //If Node is unvisited then topologically sort it.
                topologicalSort(neighbourNode.getId(), visited, stack);
            }
        }

        stack.push(nodeID);
        return stack;
    }

    /**
     * Breadth First Search algorithm for finding the longest path from a given start Node in a Graph.
     *
     * @param startNodeID The starting Node ID.
     * @return ArrayList<ArrayList < Integer>>
     */
    public ArrayList<ArrayList<Integer>> BFS(int startNodeID) {

        ArrayList<ArrayList<Integer>> paths = new ArrayList<>();

        //Creates a list of all directed and undirected neighbours of the start Node.
        ArrayList<Node> allNeighbours = combineNeighbours(startNodeID);

        //Checks to see if the Node has any neighbours. If not, then return empty path.
        if (allNeighbours.size() == 0) {
            return paths;
        }

        HashMap<Integer, Integer> dist = new HashMap<>();//HashMap to keep track of each Node's distance from start Node

        // All distances from start Node start at -1, except the start Node.
        for (int i : nodes.keySet()) {
            dist.put(i, -1);
        }
        dist.put(startNodeID, 0);

        HashMap<Integer, Integer> prevNode = new HashMap<>();//HashMap to keep track of each Node's previous Node in the path

        Queue<Integer> q = new LinkedList<>();

        q.add(startNodeID);

        //Iterate through the queue of nodes until it is empty.
        while (!q.isEmpty()) {
            int currentNodeID = q.poll();

            //Combine the lists of all directed and undirected neighbours of the current Node.
            allNeighbours = combineNeighbours(currentNodeID);

            //Iterate through all neighbouring nodes.
            for (int i = 0; i < allNeighbours.size(); i++) {
                int neighbourNodeID = allNeighbours.get(i).getId();

                if (dist.get(neighbourNodeID) == -1) {//Check if the Node is unvisited. If so, add it to the queue, update its distance and its previous Node.
                    q.add(neighbourNodeID);
                    dist.put(neighbourNodeID, dist.get(currentNodeID) + 1);
                    prevNode.put(neighbourNodeID, currentNodeID);
                }
            }
        }

        return traverseLongestPath(dist, prevNode, startNodeID);
    }

    /**
     * Combines the directed and undirected Node neighbours of a given Node.
     *
     * @param nodeID The ID of the Node.
     * @return ArrayList<Node> List of the Node's directed and undirected neighbours.
     */
    public ArrayList<Node> combineNeighbours(int nodeID) {
        ArrayList<Node> allNeighbours = new ArrayList<>(nodes.get(nodeID).getDirectedNeighbours());
        ArrayList<Node> undirectedNeighbours = new ArrayList<>(nodes.get(nodeID).getUndirectedNeighbours());
        //Adds all a Node's undirected neighbours and its directed neighbours together.
        allNeighbours.addAll(undirectedNeighbours);
        return allNeighbours;
    }

    /**
     * Returns the longest paths given a list of distances and an array of each Node's previous Node in the path.
     *
     * @param dist        A HashMap<Integer, Integer> of each nodes maximum distance
     *                    with the Node's ID being the key and the maximum distance being the value.
     * @param prevNode    HashMap<Integer, Integer> of each Node's previous Node in a path
     *                    with the Node's ID being the key and the previous Node's ID being te value..
     * @param startNodeID The Node ID of the start Node.
     * @return ArrayList<ArrayList < Integer>> The longest paths.
     */
    public ArrayList<ArrayList<Integer>> traverseLongestPath(HashMap<Integer, Integer> dist, HashMap<Integer, Integer> prevNode,
                                                             int startNodeID) {

        ArrayList<ArrayList<Integer>> paths = new ArrayList<>();
        int max = Collections.max(dist.values()); //Find the longest distance in the distance array

        //Uses the prevNode ArrayList to find the path of the longest distance starting at the end Node.
        ArrayList<Integer> path = new ArrayList<>();
        for (int i : dist.keySet()) {
            if (dist.get(i) == max) { //i.e. a longest path
                path.clear();
                path.add(i);
                int prev = prevNode.get(i);
                while (prev != startNodeID) {//Iterate through the previous Node array until you reach the start Node.
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
     * Method to check if a Graph is planar
     *
     * @return boolean returns true if the Graph is planar
     */
    public boolean GraphIsPlanar() {

        ArrayList<Node> ordered = new ArrayList<>();
        //add the Node objects to a list
        for (Node n : nodes.values()) {
            ordered.add(new Node(n));
        }

        //order the nodes according to the beginning of their span
        Collections.sort(ordered, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1.getAnchors().get(0).getFrom() < o2.getAnchors().get(0).getFrom()) {
                    return -1;
                } else if (o1.getAnchors().get(0).getFrom() == o2.getAnchors().get(0).getFrom()) {
                    return 0;
                }
                return 1;
            }
        });

        HashMap<Integer, Integer> nodeToToken = new HashMap<>();

        //Map Node ids to their Token span beginning
        for (int i = 0; i < ordered.size(); i++) {
            nodeToToken.put(ordered.get(i).getId(), ordered.get(i).getAnchors().get(0).getFrom());
        }

        ArrayList<Edge> updated = new ArrayList<>();

        int source, target;

        //for all edges change the source and target to refer to their span beginning of the respective nodes
        for (Edge e : edges) {

            source = e.getSource();
            target = e.getTarget();

            Edge newEdge = new Edge();

            for (int i = 0; i < ordered.size(); i++) {
                Node n = ordered.get(i);
                if (n.getId() == source) {
                    newEdge.setSource(nodeToToken.get(n.getId()));
                }
                if (n.getId() == target) {
                    newEdge.setTarget(nodeToToken.get(n.getId()));
                }
            }
            updated.add(newEdge);

        }

        //check every Edge with every other Edge to see if any are crossing
        for (Edge e : updated) {

            for (Edge other : updated) {

                if (Math.min(e.getSource(), e.getTarget()) < Math.min(other.getSource(), other.getTarget()) && Math.min(other.getSource(), other.getTarget()) < Math.max(e.getSource(), e.getTarget()) && Math.max(e.getSource(), e.getTarget()) < Math.max(other.getSource(), other.getTarget())) {
                    return false;
                }

            }
        }
        return true;
    }


    /**
     * Returns a Graph object in it's Planar format i.e
     * Orders the nodes linearly based on their anchor's "from" positions
     * and updates all edges to point to their corresponding new "IDs" which are represented by their Token index
     *
     * @return Graph This is the Graph in a format to indicate its planarity.
     */
    public Graph PlanarGraph() {

        ArrayList<Node> ordered = new ArrayList<>();
        for (Node n : nodes.values()) {
            ordered.add(new Node(n));
        }

        Collections.sort(ordered, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
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

        ArrayList<Edge> updated = new ArrayList<>();

        int source, target;

        for (Edge e : edges) {

            source = e.getSource();
            target = e.getTarget();

            Edge newEdge = new Edge();
            newEdge.setLabel("");
            newEdge.setPostLabel("");
            for (int i = 0; i < ordered.size(); i++) {

                Node n = ordered.get(i);

                if (n.getId() == source) {

                    newEdge.setSource(nodeToToken.get(n.getId()));

                }
                if (n.getId() == target) {

                    newEdge.setTarget(nodeToToken.get(n.getId()));

                }
            }
            updated.add(newEdge);

        }

        HashMap<Integer, Node> newNodes = new HashMap<>();
        for (Node n : ordered) {
            newNodes.put(n.getId(), n);
        }

        Graph planarVisualisation = new Graph(this.getId(), this.getSource(), this.input, newNodes, this.tokens, updated, this.tops);

        return planarVisualisation;

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
     * Equals method for the Graph class.
     *
     * @param o Object
     * @return boolean Whether to two classes being compared are equal.
     */
    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof Graph)) {
            return false;
        }

        Graph g = (Graph) o;


        return ((id.equals(g.getId())) && (source.equals(g.getSource())) && (input.equals(g.getInput())) && (nodes.equals(g.getNodes())) && (tokens.equals(g.getTokens())) && (edges.equals(g.getEdges())) && tops.equals(g.getTops()));
    }

    /**
     * Breadth First Search algorithm for determining whether a Graph is connected or not.
     *
     * @return boolean Whether the Graph is connected or not.
     */
    public boolean connectedBFS(int startNodeID) {

        setNodeNeighbours();


        if (nodes.size() <= 1) { //If there is only 1 Node or less, then the Graph is connected
            return true;
        }

        //Creates a list of all directed and undirected neighbours of the start Node.
        ArrayList<Node> allNeighbours = combineNeighbours(startNodeID);

        //Checks to see if the Node has any neighbours, if not, return false
        if (allNeighbours.size() == 0) {
            return false;
        }


        HashMap<Integer, Integer> dist = new HashMap<>();//HashMap to keep track of each Node's distance from start Node

        //All distances from start Node start at -1, except the start Node.
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

            //Combine the lists of all directed and undirected neighbours of the current Node.
            allNeighbours = combineNeighbours(currentNodeID);

            //Iterate through all neighbouring nodes
            for (int i = 0; i < allNeighbours.size(); i++) {
                int neighbourNodeID = allNeighbours.get(i).getId();

                if (dist.get(neighbourNodeID) == -1) {//Check if the Node is unvisited. If so, add it to the queue and update its distance.
                    q.add(neighbourNodeID);
                    dist.put(neighbourNodeID, dist.get(currentNodeID) + 1);
                }
            }
        }

        if (nodesVisited < nodes.size()) { //If there were any unvisited nodes then the Graph is not connected
            return false;
        } else {
            return true;
        }
    }

    /**
     * Checks if a directed or an undirected Graph is cyclic or not by calling on the appropriate recursive function required.
     *
     * @param directed Boolean to see if the Graph is directed or not.
     * @return Boolean If the Graph is cyclic or not.
     */
    public boolean isCyclic(boolean directed) {

        setNodeNeighbours();

        //Mark nodes as unvisited and not in the stack (stack is for directed graphs only)
        HashMap<Node, Boolean> visited = new HashMap<>();
        HashMap<Node, Boolean> stack = new HashMap<>();
        for (Node n : nodes.values()) {
            visited.put(n, false);
            stack.put(n, false);
        }

        if (directed) {
            //Call recursive function to detect cycles in different DFS directed trees.
            for (Node n : nodes.values()) {
                if (isCyclicCheckerDirected(n, visited, stack)) {
                    return true;
                }
            }
        } else {
            //Call recursive function to detect cycles in different DFS undirected trees.
            for (Node n : nodes.values()) {
                if (!visited.get(n)) { //Check if the Node hasn't already been visited
                    if (isCyclicCheckerUndirected(n, visited, -1)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Recursive function that checks for cycles in an undirected Graph.
     *
     * @param v       The current Node.
     * @param visited A HashMap of Node to boolean to keep track of which nodes have been visited already.
     * @param parent  The ID of the parent Node of the current Node.
     * @return Boolean If the Graph is cyclic or not.
     */
    public boolean isCyclicCheckerUndirected(Node v, HashMap<Node, Boolean> visited, int parent) {

        //Set the current Node as visited.
        visited.put(v, true);

        //Iterate through all the current Node's neighbouring nodes
        for (Node neighbour : combineNeighbours(v.getId())) {
            if (!visited.get(neighbour)) { //If the neighbour is unvisited
                if (isCyclicCheckerUndirected(neighbour, visited, v.getId())) {
                    return true;
                }
                ;
            } else if (neighbour.getId() != parent) { //If the neighbouring is visited and not a parent of the current Node, then there is a cycle.
                return true;
            }
        }

        return false;
    }

    /**
     * Recursive function that checks for cycles in an directed Graph.
     *
     * @param v       The current Node.
     * @param visited A HashMap of Node to boolean to keep track of which nodes have been visited already.
     * @param stack   A HashMap of Node to boolean to keep track of which nodes have been added to the stack.
     * @return Boolean If the Graph is cyclic or not.
     */
    public boolean isCyclicCheckerDirected(Node v, HashMap<Node, Boolean> visited, HashMap<Node, Boolean> stack) {

        //Check if the Node is in the stack already.
        if (stack.get(v)) {
            return true;
        }

        // Check if the Node has already been visited.
        if (visited.get(v)) {
            return false;
        }

        //Set stack and visited to true.
        stack.put(v, true);
        visited.put(v, true);

        //Iterate through the Node's directed neighbours and call recursive function.
        for (Node neighbour : v.getDirectedNeighbours()) {
            if (isCyclicCheckerDirected(neighbour, visited, stack)) {
                return true;
            }
        }

        stack.put(v, false);

        return false;
    }
}