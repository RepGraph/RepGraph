package com.RepGraph;

import java.util.*;

/**
 * The RepGraphModel class is used to store all the system's graphs and run analysis functions on graphs.
 */
class AbstractModel {

    /**
     * A hashmap of the model's graphs that maps Graph IDs to their Graph objects.
     */
    protected HashMap<String, AbstractGraph> graphs;

    /**
     * Default constructor for the model class.
     */
    public AbstractModel() {
        graphs = new HashMap<String, AbstractGraph>();
    }

    /**
     * Getter method for a Graph given the Graph's ID.
     *
     * @param graphID A Graph's ID.
     * @return Graph The requested Graph.
     */
    public AbstractGraph getGraph(String graphID) {
        return graphs.get(graphID);
    }

    public HashMap<String, AbstractGraph> getAllGraphs() {
        return graphs;
    }

    /**
     * Adds Graph to hashmap.
     *
     * @param value This is the Graph object that is added to the graphs hashmap
     */
    public void addGraph(AbstractGraph value) {
        graphs.put(value.getId(), value);
    }

    /**
     * Checks to see if hashmap of graphs contains a certain ID
     *
     * @param graphID This is the key value to be checked
     * @return boolean This is true if the hashmap of graphs contains the graphID
     */
    public boolean containsKey(String graphID) {
        return graphs.containsKey(graphID);
    }

    /**
     * Clears all graphs in the hashmap of graphs
     */
    public void clearGraphs() {
        graphs.clear();
    }

    public void populateAllTokens() {
        AbstractGraph t = this.graphs.values().iterator().next();
        if (t.tokens.size() == 0) {

            for (AbstractGraph g : this.graphs.values()) {

                g.setTokens(g.extractTokensFromNodes());
            }
        }

    }

    public AbstractGraph CreateSubsetAdjacent(AbstractGraph parent, String headNodeID) {


        AbstractGraph subset = new AbstractGraph();
        String graphID = parent.getId();
        parent.setNodeNeighbours();

        HashMap<String, Node> adjacentNodes = new HashMap<String, Node>();
        ArrayList<Edge> adjacentEdges = new ArrayList<>();
        ArrayList<Token> SubsetTokens = new ArrayList<>();

        //Get the head Node where the subset creation starts from
        AbstractGraph t = (AbstractGraph) graphs.get(graphID);
        Node n = t.getNodes().get(headNodeID);

        //Put the head Node in the hashmap of nodes
        adjacentNodes.put(n.getId(), new Node(n));

        //set the minimum span to the head nodes Anchors "from" and maximum span to the Anchors "end"
        int minFrom = n.getAnchors().get(0).getFrom();
        int maxEnd = n.getAnchors().get(0).getEnd();

        //Add all directed and undirected neighbours into their appropriate lists/hashmaps
        ArrayList<Node> nodeNeighbours = new ArrayList<>(n.getDirectedNeighbours());
        ArrayList<Edge> edgeNeighbours = new ArrayList<>(n.getDirectedEdgeNeighbours());
        nodeNeighbours.addAll(n.getUndirectedNeighbours());
        edgeNeighbours.addAll(n.getUndirectedEdgeNeighbours());

        //Iterate through all the adjacent nodes and work out the minimum "from" and maximum "end" to account for the entire span of the subset
        for (Node nn : nodeNeighbours) {
            //add a newly constructed Node with the Node neighbours data to the adjacent nodes hashmap
            adjacentNodes.put(nn.getId(), new Node(nn));
            if (nn.getAnchors().get(0).getFrom() < minFrom) {
                minFrom = nn.getAnchors().get(0).getFrom();
            }
            if (nn.getAnchors().get(0).getEnd() > maxEnd) {
                maxEnd = nn.getAnchors().get(0).getEnd();
            }


        }
        //add a newly constructed Edge with the Edge neighbours data to the adjacent edges list
        for (Edge ne : edgeNeighbours) {
            adjacentEdges.add(new Edge(ne));
        }

        //Use "getTokenSpan" to get all the tokens from the AbstractGraph that are in the subsets span and add it to the subset AbstractGraph object
        SubsetTokens.addAll(parent.getTokenSpan(minFrom, maxEnd));
        //Set all the details of the subset AbstractGraph object.
        subset.setId(parent.getId());
        subset.setSource(parent.getSource());
        subset.setNodes(adjacentNodes);
        subset.setEdges(adjacentEdges);
        subset.setTokens(SubsetTokens);
        subset.setInput(parent.getTokenInput(SubsetTokens));
        subset.setTopString(parent.getTop());

        return subset;
    }

    public AbstractGraph CreateSubsetDescendent(AbstractGraph parent, String headNodeID) {

        AbstractGraph subset = new AbstractGraph();
        parent.setNodeNeighbours();

        HashMap<String, Node> DescendentNodes = new HashMap<String, Node>();
        ArrayList<Edge> DescendentEdges = new ArrayList<>();
        ArrayList<Token> SubsetTokens = new ArrayList<>();


        Node n = parent.getNodes().get(headNodeID);

        //Add the head Node to the descendent nodes hashmap
        DescendentNodes.put(n.getId(), new Node(n));
        for (Edge e : n.getDirectedEdgeNeighbours()) {
            DescendentEdges.add(new Edge(e));
        }

        //set the min span to the head nodes Anchors "from" and max span to the Anchors "end"
        int minFrom = n.getAnchors().get(0).getFrom();
        int maxEnd = n.getAnchors().get(0).getEnd();

        //Iterate through all the directed nodes and edges adding them to a hashmap to eliminate duplicates.
        HashMap<String, Edge> descEdge = new HashMap<>();
        Stack<Node> stack = new Stack<>();
        while (n != null) {
            for (Node nn : n.getDirectedNeighbours()) {
                DescendentNodes.put(nn.getId(), new Node(nn));
                stack.push(nn);
                //Set the min and max span appropriately as found
                if (nn.getAnchors().get(0).getFrom() < minFrom) {
                    minFrom = nn.getAnchors().get(0).getFrom();
                }
                if (nn.getAnchors().get(0).getEnd() > maxEnd) {
                    maxEnd = nn.getAnchors().get(0).getEnd();
                }
                for (Edge ne : nn.getDirectedEdgeNeighbours()) {
                    descEdge.put(ne.getSource() + " " + ne.getTarget(), new Edge(ne));
                }
            }
            if (!stack.empty()) {
                n = stack.pop();
            } else {
                n = null;
            }
        }

        //Add all edges in the hashmap to the descendent edges list
        DescendentEdges.addAll(descEdge.values());

        //Add all the Token objects that correspond to the subsets span
        SubsetTokens.addAll(parent.getTokenSpan(minFrom, maxEnd));
        subset.setId(parent.getId());
        subset.setSource(parent.getSource());
        subset.setNodes(DescendentNodes);
        subset.setEdges(DescendentEdges);
        subset.setTokens(SubsetTokens);
        subset.setInput(parent.getTokenInput(SubsetTokens));
        subset.setTopString(parent.getTop());

        return subset;
    }



    /**
     * Overloaded method to search for subgraph pattern using different parameters
     *
     * @param graphID     ID of AbstractGraph that contains the selected pattern
     * @param NodeId      Int array of Node IDs of the pattern selected
     * @param EdgeIndices int array of the Edge indices of the pattern selected
     * @return HashMap<String, Object> Returns a hashmap of information
     * i.e the "data" key contains a list of hashmaps that contain the AbstractGraph ID's and Inputs of graphs that contain the subgraph pattern.
     * the "Response" key contains an error response if necessary.
     */
    public HashMap<String, Object> searchSubgraphPattern(String graphID, String[] NodeId, int[] EdgeIndices) {
        AbstractGraph parent = (AbstractGraph) graphs.get(graphID);
        HashMap<String, Node> subnodes = new HashMap<String, Node>();
        ArrayList<Edge> subedges = new ArrayList<>();

        //Create new nodes with the selected Node data to use as the subgraphs nodes
        for (String n : NodeId) {
            subnodes.put(n, new Node(parent.getNodes().get(n)));
        }
        //Create new edges with the selected Edge data to use as the subraphs edges
        for (int n : EdgeIndices) {
            subedges.add(new Edge(parent.getEdges().get(n)));
        }

        AbstractGraph subgraph = new AbstractGraph();


        subgraph.setNodes(subnodes);
        subgraph.setEdges(subedges);

        return searchSubgraphPattern(subgraph);

    }

    public HashMap<String, Object> searchSubgraphPattern(AbstractGraph Asubgraph) {

        AbstractGraph subgraph = (AbstractGraph) Asubgraph;

        HashMap<String, Object> returninfo = new HashMap<>();

        //subgraph pattern must be connected

        //Empty Arraylist for the found graphs that have the subgraph pattern specified.
        ArrayList<HashMap<String, String>> FoundGraphs = new ArrayList<>();

        //If no nodes there is no pattern, if there are no edges it is not connected,
        // and if there are less edges than number of nodes -1 then it is not connected,
        // if it has a dangling Edge it isnt connected
        if (subgraph.getNodes().size() == 0 || subgraph.getEdges().size() == 0 || subgraph.getEdges().size() < subgraph.getNodes().size() - 1 || subgraph.hasDanglingEdge() || !subgraph.connectedBFS(subgraph.getNodes().values().iterator().next().getId())) {
            returninfo.put("response", "Failure");
            return returninfo;
        }


        // Hashmap with a string key and boolean value - this is used to confirm that each unique Edge in the subgraph has been found in the AbstractGraph being checked.
        //This is necessary because there can be multiple of the same two Node links so a counter or an array cant be used.
        HashMap<String, Boolean> checks = new HashMap<>();
        //This is just used to easily check that all the required edges in the subgraph are true because all values in this array need to be true before a AbstractGraph is added
        //to foundgraphs arraylist.
        boolean[] checksarr = new boolean[subgraph.getEdges().size()];
        try {
            subgraph.setNodeNeighbours();
        } catch (IndexOutOfBoundsException e) {
            returninfo.put("response", "Failure");
            return returninfo;

        }


        //Iterate over each AbstractGraph in the dataset

        for (AbstractGraph t : graphs.values()) {
            AbstractGraph g = (AbstractGraph) t;
            //If it cant set the Node neighbours then just the specific AbstractGraph iteration.
            try {
                g.setNodeNeighbours();
            } catch (IndexOutOfBoundsException f) {
                continue;
            }

            //Iterate over each Node in the subgraph patttern
            for (Node sn : subgraph.getNodes().values()) {
                //for each Node in the subgraph pattern, it iterates over every Node in the current AbstractGraph that is being checked.
                //This is to find a Node label equal to the current sub AbstractGraph Node being checked.
                for (Node n : g.getNodes().values()) {
                    //This checks if the labels are equal
                    if (n.getLabel().equals(sn.getLabel())) {
                        //Once it finds a Node in the AbstractGraph that is the same as the subgraph Node label it has to iterate over the subgraph edges list
                        //to find the corresponding Edge of the subgraph Node that found a match.

                        for (int i = 0; i < sn.getDirectedNeighbours().size(); i++) {
                            for (int j = 0; j < n.getDirectedNeighbours().size(); j++) {

                                if (sn.getDirectedNeighbours().get(i).getLabel().equals(n.getDirectedNeighbours().get(j).getLabel()) && sn.getDirectedEdgeNeighbours().get(i).getLabel().equals(n.getDirectedEdgeNeighbours().get(j).getLabel())) {
                                    checks.put(sn.getId() + sn.getDirectedNeighbours().get(i).getId() + "", true);
                                }
                            }
                        }
                    }
                }

            }
            //This for loop checks if the hashmap has a key for the unique Edge entry and checks if its true. if it is true then it sets
            //the corresponding index in the boolean array to true;
            for (int i = 0; i < subgraph.getEdges().size(); i++) {
                if (checks.containsKey(subgraph.getEdges().get(i).getSource() + subgraph.getEdges().get(i).getTarget() + "") && checks.get(subgraph.getEdges().get(i).getSource() + subgraph.getEdges().get(i).getTarget() + "") == true) {
                    checksarr[i] = true;

                }
            }
            //This checks to see if all values in the boolean array are true and if so adds the current AbstractGraph to the list of found graphs.
            if (areAllTrue(checksarr)) {
                HashMap<String, String> found = new HashMap<String, String>();
                found.put("id", g.getId());
                found.put("input", g.getInput());
                FoundGraphs.add(found);
            }
            checks.clear();
            for (int i = 0; i < checksarr.length; i++) {
                checksarr[i] = false;
            }
        }

        returninfo.put("response", "Success");
        returninfo.put("data", FoundGraphs);
        return returninfo;
    }

    /**
     * Searches through all the graphs to find graphs that contain all the Node labels provided.
     *
     * @param labels This is the list of Node labels to search for.
     * @return HashMap<String, Object> Returns a hashmap of information
     * * i.e the "data" key contains a list of hashmaps that contain the AbstractGraph ID's and Inputs of graphs that have the set of Node labels.
     * * the "Response" key contains an error response if necessary.
     */
    public HashMap<String, Object> searchSubgraphNodeSet(ArrayList<String> labels) {
        HashMap<String, Object> returninfo = new HashMap<>();


        ArrayList<HashMap<String, String>> FoundGraphs = new ArrayList<>();
        if (labels.size() == 0) {
            returninfo.put("response", "No Labels were entered");
            return returninfo;
        }


        //boolean array that checks if certain nodes are found
        //could use a hashmap with the label as a key and boolean as value.
        //this would allow to avoid using a for loop inside AbstractGraph iteration
        boolean[] checks = new boolean[labels.size()];

        for (AbstractGraph t : graphs.values()) {
            AbstractGraph g = (AbstractGraph) t;
            HashMap<String, Node> tempNodes = new HashMap<String, Node>(g.getNodes());
            for (int i = 0; i < labels.size(); i++) {
                for (Node n : tempNodes.values()) {
                    //checks if nodes match any of the labels specified
                    if (n.getLabel().equals(labels.get(i))) {
                        checks[i] = true;
                        //removes Node so that it wont be checked again in case two or more of the same labels are required in the set
                        tempNodes.remove(n.getId());
                        break;
                    }
                }
            }

            //checks if all Node labels have been found
            if (areAllTrue(checks)) {
                HashMap<String, String> found = new HashMap<String, String>();
                found.put("id", g.getId());
                found.put("input", g.getInput());
                FoundGraphs.add(found);

            }
            //resets the checks array when its done checking a AbstractGraph
            for (int i = 0; i < checks.length; i++) {
                checks[i] = false;
            }
        }

        returninfo.put("response", "Success");
        returninfo.put("data", FoundGraphs);
        return returninfo;
    }

    /**
     * Compares two graphs and searches for similarities and differences.
     *
     * @param graphID1 AbstractGraph ID of the first AbstractGraph.
     * @param graphID2 AbstractGraph ID of the second AbstractGraph.
     * @return HashMap<String, Object> The differences and similarities of the two graphs i.e
     * the "SimilarNodes1" key gives the Node ids of the similar nodes in graph1.
     * the "SimilarNodes2" key gives the Node ids of the similar nodes in graph2.
     * the "SimilarEdges1" key gives the Node ids of the similar edges in graph1.
     * the "SimilarEdge2" key gives the Node ids of the similar edges in graph2.
     */
    public HashMap<String, Object> compareTwoGraphs(String graphID1, String graphID2) {
        AbstractGraph g1 = (AbstractGraph) graphs.get(graphID1);
        AbstractGraph g2 = (AbstractGraph) graphs.get(graphID2);
        HashMap<String, Node> nodes1 = g1.getNodes();
        HashMap<String, Node> nodes2 = g2.getNodes();
        ArrayList<Edge> edges1 = graphs.get(graphID1).getEdges();
        ArrayList<Edge> edges2 = (graphs.get(graphID2).getEdges());


        ArrayList<String> similarNodes1 = new ArrayList<>();
        ArrayList<String> similarNodes2 = new ArrayList<>();
        ArrayList<Integer> similarEdges1 = new ArrayList<>();
        ArrayList<Integer> similarEdges2 = new ArrayList<>();


        g1.setNodeNeighbours();
        g2.setNodeNeighbours();

        //iterates over every Node of each AbstractGraph
        for (Node n1 : nodes1.values()) {
            for (Node n2 : nodes2.values()) {
                int span1 = n1.getAnchors().get(0).getEnd() - n1.getAnchors().get(0).getFrom();
                int span2 = n2.getAnchors().get(0).getEnd() - n2.getAnchors().get(0).getFrom();
                //checks if their labels and spans are equal
                if (n1.getLabel().equals(n2.getLabel()) && span1 == span2) {
                    //if they are equal then it adds the respective IDs to the individual similar nodes lists for the graphs
                    if (!similarNodes1.contains(n1.getId())) {
                        similarNodes1.add(n1.getId());
                    }
                    if (!similarNodes2.contains(n2.getId())) {
                        similarNodes2.add(n2.getId());
                    }

                    //It then iterates over all the edges and checks if the edges are connected to similar nodes on both sides as well as have the same label. if so
                    //they are regarded as similar edges and their respective indexes are added to the respective lists
                    for (Edge e1 : n1.getDirectedEdgeNeighbours()) {
                        for (Edge e2 : n2.getDirectedEdgeNeighbours()) {

                            Node nn1 = nodes1.get(e1.getTarget());
                            Node nn2 = nodes2.get(e2.getTarget());
                            span1 = nn1.getAnchors().get(0).getEnd() - nn1.getAnchors().get(0).getFrom();
                            span2 = nn2.getAnchors().get(0).getEnd() - nn2.getAnchors().get(0).getFrom();

                            if (nn1.getLabel().equals(nn2.getLabel()) && e1.getLabel().equals(e2.getLabel()) && span1 == span2) {

                                if (!similarEdges1.contains(edges1.indexOf(e1))) {

                                    similarEdges1.add(edges1.indexOf(e1));
                                }
                                if (!similarEdges2.contains(edges2.indexOf(e2))) {

                                    similarEdges2.add(edges2.indexOf(e2));
                                }

                            }
                        }

                    }
                }

            }
        }


        HashMap<String, Object> returnObj = new HashMap<>();
        returnObj.put("SimilarNodes1", similarNodes1);
        returnObj.put("SimilarNodes2", similarNodes2);
        returnObj.put("SimilarEdges1", similarEdges1);
        returnObj.put("SimilarEdges2", similarEdges2);

        return returnObj;
    }

    /**
     * Runs formal tests on a AbstractGraph.
     *
     * @param graphID               The AbstractGraph ID which the tests will be run.
     * @param planar                Boolean to decide if to test for if the AbstractGraph is planar.
     * @param longestPathDirected   Boolean to decide if to find the longest directed path.
     * @param longestPathUndirected Boolean to decide if to find the longest undirected path.
     * @param connected             Boolean to decide if to test for if the AbstractGraph is connected.
     * @return HashMap<String, Object> Results of the tests i.e
     * the "Planar" key returns a boolean of whether or not the AbstractGraph is planar
     * the "PlanarVis" key returns the visualisation data
     * the "LongestPathDirected" key returns an ArrayList of an Arraylist of integers defining the multiple longest directed paths in the graphs
     * the "LongestPathUndirected" key returns an ArrayList of an Arraylist of integers defining the multiple longest undirected paths in the graphs
     * the "Connected" returns a boolean of whether or not the AbstractGraph is connected.
     */
    public HashMap<String, Object> runFormalTests(String graphID, boolean planar, boolean longestPathDirected, boolean longestPathUndirected, boolean connected) {
        HashMap<String, Object> returnObj = new HashMap<>();
        AbstractGraph g = (AbstractGraph) graphs.get(graphID);
        if (planar) {

            returnObj.put("Planar", g.isPlanar());
            returnObj.put("PlanarVis", (graphs.get(graphID).PlanarGraph()));
        }
        if (longestPathDirected) {
            //checks if graphs are cyclic, if so returns a message indicating the AbstractGraph is cyclic otherwise sends back the longest path directed information
            if (graphs.get(graphID).isCyclic(true)) {
                returnObj.put("LongestPathDirected", "Cycle Detected");
            } else {
                returnObj.put("LongestPathDirected", graphs.get(graphID).findLongest(true));
            }
        }
        //checks if graphs are cyclic, if so returns a message indicating the AbstractGraph is cyclic otherwise sends back the longest path directed information
        if (longestPathUndirected) {
            if (graphs.get(graphID).isCyclic(false)) {
                returnObj.put("LongestPathUndirected", "Cycle Detected");
            } else {
                returnObj.put("LongestPathUndirected", graphs.get(graphID).findLongest(false));
            }
        }
        if (connected) {
            returnObj.put("Connected", g.connectedBFS(g.getNodes().values().iterator().next().getId()));
        }
        return returnObj;
    }


    /**
     * Checks if all values in boolean array are true
     *
     * @param array an array of boolean values
     * @return boolean True if all values are true and false if there is at least one false value
     */
    public boolean areAllTrue(boolean[] array) {
        for (boolean b : array) {
            if (!b) return false;
        }

        return true;
    }

    public HashMap<String, Object> DisplaySubset(String graphId, String headNodeID, String SubsetType, int format) {
        return null;
    }


    public HashMap<String, Object> Visualise(String graphID, int format) {
        return null;
    }

}
