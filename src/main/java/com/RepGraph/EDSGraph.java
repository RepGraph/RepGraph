package com.RepGraph;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.*;

/**
 * The Graph class represents a single sentence which comprises of nodes, edges and tokens.
 */
public class EDSGraph extends AbstractGraph {


    /**
     * Default constructor for the Graph class.
     */
    public EDSGraph() {
        super();

        this.tokens = new ArrayList<>();
    }


    public EDSGraph(String id, String source, String input, HashMap<String, Node> nodes, ArrayList<Token> tokens, ArrayList<Edge> edges, String top) {
        super(id, source, input, nodes, edges, top);
        this.tokens = tokens;
    }

    public void populateEdges() {
        if (this.edges.size() == 0) {
            for (Node n : this.nodes.values()) {
                for (Edge e : n.getDirectedEdgeNeighbours()) {
                    this.edges.add(e);
                }
            }
        }
    }

    /**
     * Analysis Tool for finding the longest paths in the Graph.
     *
     * @param directed This is the boolean to decide if the longest path found is directed or undirected.
     * @return ArrayList<ArrayList < Integer>> The a list of longest paths in the Graph.
     */
    public ArrayList<ArrayList<String>> findLongest(boolean directed) {

        //Ensure that all Graph Node's have their neighbours assigned
        setNodeNeighbours();

        ArrayList<ArrayList<String>> paths = new ArrayList<>();

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
                    ArrayList<ArrayList<String>> endpoints = BFS(nodes.values().iterator().next().getId());
                    ArrayList<ArrayList<String>> temp;
                    ArrayList<String> pathEnds = new ArrayList<>();

                    //Iterate through each path's end points (which is in position 0 as the path is reversed)
                    for (ArrayList<String> end : endpoints) {
                        if (!pathEnds.contains(end.get(0))) { //Avoids identical paths that are just reversed.
                            temp = BFS(end.get(0)); //Finds the true longest path from this particular end point.
                            for (ArrayList<String> al : temp) {//Add path to longest paths.
                                paths.add(new ArrayList<>(al));
                                pathEnds.add(al.get(0));
                            }
                        }
                    }
                } else { //Finding the longest path in a undirected disconnected Graph.

                    //Default longest path set from the first Node in the Graph.
                    ArrayList<ArrayList<String>> longest;
                    longest = BFS(nodes.values().iterator().next().getId());

                    //Add longest paths to the overall longest paths
                    for (ArrayList<String> al : longest) {
                        paths.add(al);
                    }

                    ArrayList<ArrayList<String>> temp;

                    //Makes each Node the start Node and compared the longest paths found from that Node to the current best longest path.
                    for (String i : nodes.keySet()) {
                        temp = BFS(i);

                        if (temp.size() != 0) {
                            if ((longest.size() == 0) || (temp.get(0).size() > longest.get(0).size())) {//New path is longest than the current longest path, so overwrite the overall longest paths
                                longest = temp;
                                paths.clear();
                                for (ArrayList<String> al : temp) {
                                    paths.add(new ArrayList<>(al));
                                }
                            } else if (temp.get(0).size() == longest.get(0).size()) {//New path is the same length as the current longest path, so add it to the overall longest paths
                                for (ArrayList<String> al : temp) {
                                    paths.add(new ArrayList<>(al));
                                }
                            }
                        }
                    }

                    //Reverse the each of the longest paths so that they start with the smaller Node ID. Removes duplicate but reversed paths as well.
                    ArrayList<ArrayList<String>> newPaths = new ArrayList<>();
                    for (ArrayList<String> al : paths) {
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
                ArrayList<ArrayList<String>> longest;
                Set<String> nodesInPath = new HashSet<>(); //Keeps track of all nodes in the overall longest path.
                longest = directedLongestPaths(nodes.values().iterator().next().getId());

                //Add longest paths to the overall longest paths
                for (ArrayList<String> integers : longest) {
                    paths.add(integers);
                    for (String n : integers) {
                        nodesInPath.add(n);
                    }
                }

                ArrayList<ArrayList<String>> temp;

                //Makes each Node the start Node and finds the longest overall path/s.
                for (String i : nodes.keySet()) {
                    if (!nodesInPath.contains(i)) {//Cuts processing time by not letting a Node already in the overall longest path be a start Node, as the longest path from that Node will never yield a longer path than the current overall longest path.
                        temp = directedLongestPaths(i);

                        if (temp.size() != 0) {
                            if ((longest.size() == 0) || (temp.get(0).size() > longest.get(0).size())) {//New path is longest than the current longest path, so overwrite the overall longest paths
                                longest = temp;
                                paths.clear();
                                for (ArrayList<String> al : temp) {
                                    paths.add(new ArrayList<>(al));
                                    for (String n : al) {
                                        nodesInPath.add(n);
                                    }
                                }
                            } else if (temp.get(0).size() == longest.get(0).size()) {//New path is the same length as the current longest path, so add it to the overall longest paths
                                for (ArrayList<String> al : temp) {
                                    paths.add(new ArrayList<>(al));
                                    for (String n : al) {
                                        nodesInPath.add(n);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            //Reverses the path so that start Node is first.
            for (ArrayList<String> item : paths) {
                Collections.reverse(item);
            }

            return paths;
        }

    }


    /**
     * Finds the longest paths from a given start Node to all the other nodes in the system and returns the paths of the longest paths.
     *
     * @param startNodeID Number of the start Node.
     * @return ArrayList<ArrayList < Integer>> The longest paths available from the start Node.
     */
    public ArrayList<ArrayList<String>> directedLongestPaths(String startNodeID) {

        ArrayList<ArrayList<String>> paths = new ArrayList<>();

        //Check if the start Node has any neighbours, if not then return an empty path.
        if (nodes.get(startNodeID).getDirectedNeighbours().size() == 0) {
            return paths;
        }

        HashMap<String, Integer> dist = new HashMap<>(); //HashMap to keep track of each Node's distance from start Node
        HashMap<String, String> prevNode = new HashMap<>(); //HashMap to keep track of each Node's previous Node in the path

        //Set all nodes in the Graph to unvisited
        HashMap<String, Boolean> visited = new HashMap<>();
        for (String i : nodes.keySet()) {
            visited.put(i, false);
        }

        //Topologically sort every unvisited Node, which gets added to the stack.
        Stack<String> stack = new Stack<>();
        for (String i : nodes.keySet()) {
            if (!visited.get(i)) {
                topologicalSort(i, visited, stack);
            }
        }

        //Set all distances to NINF except the start Node.
        for (String i : nodes.keySet()) {
            dist.put(i, Integer.MIN_VALUE);
        }
        dist.put(startNodeID, 0);

        //Iterate through the stack to find longest path.
        while (!stack.empty()) {
            String u = stack.pop();

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
    public Stack<String> topologicalSort(String nodeID, HashMap<String, Boolean> visited, Stack<String> stack) {
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
    public ArrayList<ArrayList<String>> BFS(String startNodeID) {

        ArrayList<ArrayList<String>> paths = new ArrayList<>();

        //Creates a list of all directed and undirected neighbours of the start Node.
        ArrayList<Node> allNeighbours = combineNeighbours(startNodeID);

        //Checks to see if the Node has any neighbours. If not, then return empty path.
        if (allNeighbours.size() == 0) {
            return paths;
        }

        HashMap<String, Integer> dist = new HashMap<>();//HashMap to keep track of each Node's distance from start Node

        // All distances from start Node start at -1, except the start Node.
        for (String i : nodes.keySet()) {
            dist.put(i, -1);
        }
        dist.put(startNodeID, 0);

        HashMap<String, String> prevNode = new HashMap<>();//HashMap to keep track of each Node's previous Node in the path

        Queue<String> q = new LinkedList<>();

        q.add(startNodeID);

        //Iterate through the queue of nodes until it is empty.
        while (!q.isEmpty()) {
            String currentNodeID = q.poll();

            //Combine the lists of all directed and undirected neighbours of the current Node.
            allNeighbours = combineNeighbours(currentNodeID);

            //Iterate through all neighbouring nodes.
            for (int i = 0; i < allNeighbours.size(); i++) {
                String neighbourNodeID = allNeighbours.get(i).getId();

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
     * Returns the longest paths given a list of distances and an array of each Node's previous Node in the path.
     *
     * @param dist        A HashMap<Integer, Integer> of each nodes maximum distance
     *                    with the Node's ID being the key and the maximum distance being the value.
     * @param prevNode    HashMap<Integer, Integer> of each Node's previous Node in a path
     *                    with the Node's ID being the key and the previous Node's ID being te value..
     * @param startNodeID The Node ID of the start Node.
     * @return ArrayList<ArrayList < Integer>> The longest paths.
     */
    public ArrayList<ArrayList<String>> traverseLongestPath(HashMap<String, Integer> dist, HashMap<String, String> prevNode,
                                                            String startNodeID) {

        ArrayList<ArrayList<String>> paths = new ArrayList<>();
        int max = Collections.max(dist.values()); //Find the longest distance in the distance array

        //Uses the prevNode ArrayList to find the path of the longest distance starting at the end Node.
        ArrayList<String> path = new ArrayList<>();
        for (String i : dist.keySet()) {
            if (dist.get(i) == max) { //i.e. a longest path
                path.clear();
                path.add(i);
                String prev = prevNode.get(i);
                while (!prev.equals(startNodeID)) {//Iterate through the previous Node array until you reach the start Node.
                    path.add(prev);
                    prev = prevNode.get(prev);
                }
                path.add(startNodeID);
                paths.add(new ArrayList<>(path)); //Add the path to the list of overall longest paths
            }
        }

        return paths;
    }

    @JsonIgnore
    /**
     * Method to check if a Graph is planar
     *
     * @return boolean returns true if the Graph is planar
     */
    public boolean isPlanar() {

        ArrayList<Node> ordered = new ArrayList<>();
        //add the Node objects to a list
        for (Node n : nodes.values()) {
            ordered.add(new Node(n));
        }

        //order the nodes according to the beginning of their span
        Collections.sort(ordered, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if (o1.getAnchors().getFrom() < o2.getAnchors().getFrom()) {
                    return -1;
                } else if (o1.getAnchors().getFrom() == o2.getAnchors().getFrom()) {
                    return 0;
                }
                return 1;
            }
        });

        HashMap<String, String> nodeToToken = new HashMap<>();

        //Map Node ids to their Token span beginning
        for (int i = 0; i < ordered.size(); i++) {
            nodeToToken.put(ordered.get(i).getId(), ordered.get(i).getAnchors().getFrom() + "");
        }

        ArrayList<Edge> updated = new ArrayList<>();

        String source, target;

        //for all edges change the source and target to refer to their span beginning of the respective nodes
        for (Edge e : edges) {

            source = e.getSource();
            target = e.getTarget();

            Edge newEdge = new Edge();

            for (int i = 0; i < ordered.size(); i++) {
                Node n = ordered.get(i);
                if (n.getId().equals(source)) {
                    newEdge.setSource(nodeToToken.get(n.getId()));
                }
                if (n.getId().equals(target)) {
                    newEdge.setTarget(nodeToToken.get(n.getId()));
                }
            }
            updated.add(newEdge);

        }

        //check every Edge with every other Edge to see if any are crossing
        for (Edge e : updated) {

            for (Edge other : updated) {

                if (Math.min(Integer.parseInt(e.getSource()), Integer.parseInt(e.getTarget())) < Math.min(Integer.parseInt(other.getSource()), Integer.parseInt(other.getTarget())) && Math.min(Integer.parseInt(other.getSource()), Integer.parseInt(other.getTarget())) < Math.max(Integer.parseInt(e.getSource()), Integer.parseInt(e.getTarget())) && Math.max(Integer.parseInt(e.getSource()), Integer.parseInt(e.getTarget())) < Math.max(Integer.parseInt(other.getSource()), Integer.parseInt(other.getTarget()))) {
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
    public EDSGraph PlanarGraph() {

        ArrayList<Node> ordered = new ArrayList<>();
        for (Node n : nodes.values()) {
            ordered.add(new Node(n));
        }

        Collections.sort(ordered, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                o1.getAnchors().setEnd(o1.getAnchors().getFrom());
                o2.getAnchors().setEnd(o2.getAnchors().getFrom());
                if (o1.getAnchors().getFrom() < o2.getAnchors().getFrom()) {

                    return -1;
                } else if (o1.getAnchors().getFrom() == o2.getAnchors().getFrom()) {

                    return 0;
                }
                return 1;
            }
        });


        HashMap<String, String> nodeToToken = new HashMap<>();

        for (int i = 0; i < ordered.size(); i++) {
            nodeToToken.put(ordered.get(i).getId(), ordered.get(i).getAnchors().getFrom() + "");
        }

        ArrayList<Edge> updated = new ArrayList<>();

        String source, target;

        for (Edge e : edges) {

            source = e.getSource();
            target = e.getTarget();

            Edge newEdge = new Edge();
            newEdge.setLabel("");
            newEdge.setPostLabel("");
            for (int i = 0; i < ordered.size(); i++) {

                Node n = ordered.get(i);

                if (n.getId().equals(source)) {

                    newEdge.setSource(nodeToToken.get(n.getId()));

                }
                if (n.getId().equals(target)) {

                    newEdge.setTarget(nodeToToken.get(n.getId()));

                }
            }

            updated.add(newEdge);

        }

        HashMap<String, Node> newNodes = new HashMap<>();
        for (Node n : ordered) {
            //n.setId(nodeToToken.get(n.getId()));
            newNodes.put(n.getId(), n);
        }

        EDSGraph planarVisualisation = new EDSGraph(this.getId(), this.getSource(), this.input, newNodes, this.tokens, updated, this.top);


        return planarVisualisation;

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

        if (!(o instanceof EDSGraph)) {
            return false;
        }

        EDSGraph g = (EDSGraph) o;


        return ((id.equals(g.getId())) && (source.equals(g.getSource())) && (input.equals(g.getInput())) && (nodes.equals(g.getNodes())) && (tokens.equals(g.getTokens())) && (edges.equals(g.getEdges())) && top.equals(g.getTop()));
    }

    /**
     * Breadth First Search algorithm for determining whether a Graph is connected or not.
     *
     * @return boolean Whether the Graph is connected or not.
     */
    public boolean connectedBFS(String startNodeID) {

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


        HashMap<String, Integer> dist = new HashMap<>();//HashMap to keep track of each Node's distance from start Node

        //All distances from start Node start at -1, except the start Node.
        for (String i : nodes.keySet()) {
            dist.put(i, -1);
        }
        dist.put(startNodeID, 0);

        int nodesVisited = 0;

        Queue<String> q = new LinkedList<>();
        q.add(startNodeID);

        //Iterate through the queue of nodes until it is empty.
        while (!q.isEmpty()) {
            String currentNodeID = q.poll();
            nodesVisited++;

            //Combine the lists of all directed and undirected neighbours of the current Node.
            allNeighbours = combineNeighbours(currentNodeID);

            //Iterate through all neighbouring nodes
            for (int i = 0; i < allNeighbours.size(); i++) {
                String neighbourNodeID = allNeighbours.get(i).getId();

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
                    if (isCyclicCheckerUndirected(n, visited, -1 + "")) {
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
    public boolean isCyclicCheckerUndirected(Node v, HashMap<Node, Boolean> visited, String parent) {

        //Set the current Node as visited.
        visited.put(v, true);

        //Iterate through all the current Node's neighbouring nodes
        for (Node neighbour : combineNeighbours(v.getId())) {
            if (!visited.get(neighbour)) { //If the neighbour is unvisited
                if (isCyclicCheckerUndirected(neighbour, visited, v.getId())) {
                    return true;
                }
                ;
            } else if (!neighbour.getId().equals(parent)) { //If the neighbouring is visited and not a parent of the current Node, then there is a cycle.
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
