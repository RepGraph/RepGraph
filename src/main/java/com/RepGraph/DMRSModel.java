package com.RepGraph;

import java.util.*;

/**
 * The RepGraphModel class is used to store all the system's graphs and run analysis functions on graphs.
 */
public class DMRSModel extends AbstractModel{


    public DMRSModel() {
        super();
    }

    /**
     * Display Subset overloaded method to decide which type of subset to construct
     * and what type of visualisation the subset should be displayed in
     *
     * @param graphId    The ID of the DMRSGraph where the subset is being constructed
     * @param headNodeID The Node ID of the starting Node of subset creation
     * @param SubsetType The type of subset to be created
     * @param format     The format of the visualisation i.e 1 - hierarchical format, 2- tree like format, 3 - flat visualisation, 4 - planar visualisation
     * @return HashMap<String, Object> The Visualisation Data of the subset
     */
    public HashMap<String, Object> DisplaySubset(String graphId, String headNodeID, String SubsetType, int format) {
        //checks which type of subset and format the user wants - creates the subset and returns the visualisation data
        if (SubsetType.equals("adjacent")) {
            if (format == 1) {
                return VisualiseHierarchy(CreateSubsetAdjacent(graphId, headNodeID));
            } else if (format == 2) {
                return VisualiseTree(CreateSubsetAdjacent(graphId, headNodeID));
            } else if (format == 3) {
                return VisualiseFlat(CreateSubsetAdjacent(graphId, headNodeID));
            } else {
                return VisualisePlanar(CreateSubsetAdjacent(graphId, headNodeID));
            }
        } else if (SubsetType.equals("descendent")) {
            if (format == 1) {
                return VisualiseHierarchy(CreateSubsetDescendent(graphId, headNodeID));
            } else if (format == 2) {
                return VisualiseTree(CreateSubsetDescendent(graphId, headNodeID));
            } else if (format == 3) {
                return VisualiseFlat(CreateSubsetDescendent(graphId, headNodeID));
            } else {
                return VisualisePlanar(CreateSubsetDescendent(graphId, headNodeID));
            }
        }
        return null;
    }

    /**
     * Uses a DMRSGraph ID and the number of a Node in the DMRSGraph and returns a subset of the DMRSGraph. The subset is all the adjacent nodes around the head Node id given
     *
     * @param graphID    The DMRSGraph ID.
     * @param headNodeID The DMRSGraph's Node which will be the head Node of the subset.
     * @return DMRSGraph The subset of the DMRSGraph.
     */
    public DMRSGraph CreateSubsetAdjacent(String graphID, String headNodeID) {


        DMRSGraph subset = new DMRSGraph();
        DMRSGraph parent = (DMRSGraph) getGraph(graphID);
        parent.setNodeNeighbours();

        HashMap<String, Node> adjacentNodes = new HashMap<String, Node>();
        ArrayList<Edge> adjacentEdges = new ArrayList<>();
        ArrayList<Token> SubsetTokens = new ArrayList<>();

        //Get the head Node where the subset creation starts from
        DMRSGraph t = (DMRSGraph) graphs.get(graphID);
        Node n = t.getNodes().get(headNodeID);

        //Put the head Node in the hashmap of nodes
        adjacentNodes.put(n.getId(), new Node(n));

        //set the minimum span to the head nodes Anchors "from" and maximum span to the Anchors "end"
        int minFrom = n.getAnchors().getFrom();
        int maxEnd = n.getAnchors().getEnd();

        //Add all directed and undirected neighbours into their appropriate lists/hashmaps
        ArrayList<Node> nodeNeighbours = new ArrayList<>(n.getDirectedNeighbours());
        ArrayList<Edge> edgeNeighbours = new ArrayList<>(n.getDirectedEdgeNeighbours());
        nodeNeighbours.addAll(n.getUndirectedNeighbours());
        edgeNeighbours.addAll(n.getUndirectedEdgeNeighbours());

        //Iterate through all the adjacent nodes and work out the minimum "from" and maximum "end" to account for the entire span of the subset
        for (Node nn : nodeNeighbours) {
            //add a newly constructed Node with the Node neighbours data to the adjacent nodes hashmap
            adjacentNodes.put(nn.getId(), new Node(nn));
            if (nn.getAnchors().getFrom() < minFrom) {
                minFrom = nn.getAnchors().getFrom();
            }
            if (nn.getAnchors().getEnd() > maxEnd) {
                maxEnd = nn.getAnchors().getEnd();
            }


        }
        //add a newly constructed Edge with the Edge neighbours data to the adjacent edges list
        for (Edge ne : edgeNeighbours) {
            adjacentEdges.add(new Edge(ne));
        }

        //Use "getTokenSpan" to get all the tokens from the DMRSGraph that are in the subsets span and add it to the subset DMRSGraph object
        SubsetTokens.addAll(parent.getTokenSpan(minFrom, maxEnd));
        //Set all the details of the subset DMRSGraph object.
        subset.setId(parent.getId());
        subset.setSource(parent.getSource());
        subset.setNodes(adjacentNodes);
        subset.setEdges(adjacentEdges);
        subset.setTokens(SubsetTokens);
        subset.setInput(parent.getTokenInput(SubsetTokens));
        subset.setTop(parent.getTop());

        return subset;
    }

    /**
     * Uses a DMRSGraph ID and the number of a Node in the DMRSGraph and returns a subset of the DMRSGraph. The subset is all the descendent nodes from the head Node id given
     *
     * @param graphID    The DMRSGraph ID.
     * @param headNodeID The DMRSGraph's Node which will be the head Node of the subset.
     * @return DMRSGraph The subset of the DMRSGraph.
     */
    public DMRSGraph CreateSubsetDescendent(String graphID, String headNodeID) {

        DMRSGraph subset = new DMRSGraph();
        DMRSGraph parent = (DMRSGraph) getGraph(graphID);
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
        int minFrom = n.getAnchors().getFrom();
        int maxEnd = n.getAnchors().getEnd();

        //Iterate through all the directed nodes and edges adding them to a hashmap to eliminate duplicates.
        HashMap<String, Edge> descEdge = new HashMap<>();
        Stack<Node> stack = new Stack<>();
        while (n != null) {
            for (Node nn : n.getDirectedNeighbours()) {
                DescendentNodes.put(nn.getId(), new Node(nn));
                stack.push(nn);
                //Set the min and max span appropriately as found
                if (nn.getAnchors().getFrom() < minFrom) {
                    minFrom = nn.getAnchors().getFrom();
                }
                if (nn.getAnchors().getEnd() > maxEnd) {
                    maxEnd = nn.getAnchors().getEnd();
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
        subset.setTop(parent.getTop());

        return subset;
    }

    /**
     * Overloaded method to search for subgraph pattern using different parameters
     *
     * @param graphID     ID of DMRSGraph that contains the selected pattern
     * @param NodeId      Int array of Node IDs of the pattern selected
     * @param EdgeIndices int array of the Edge indices of the pattern selected
     * @return HashMap<String, Object> Returns a hashmap of information
     * i.e the "data" key contains a list of hashmaps that contain the DMRSGraph ID's and Inputs of graphs that contain the subgraph pattern.
     * the "Response" key contains an error response if necessary.
     */
    public HashMap<String, Object> searchSubgraphPattern(String graphID, String[] NodeId, int[] EdgeIndices) {
        DMRSGraph parent = (DMRSGraph) graphs.get(graphID);
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

        DMRSGraph subgraph = new DMRSGraph();


        subgraph.setNodes(subnodes);
        subgraph.setEdges(subedges);

        return searchSubgraphPattern(subgraph);

    }

    public HashMap<String, Object> searchSubgraphPattern(AbstractGraph Asubgraph) {

        DMRSGraph subgraph = (DMRSGraph)Asubgraph;

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


        // Hashmap with a string key and boolean value - this is used to confirm that each unique Edge in the subgraph has been found in the DMRSGraph being checked.
        //This is necessary because there can be multiple of the same two Node links so a counter or an array cant be used.
        HashMap<String, Boolean> checks = new HashMap<>();
        //This is just used to easily check that all the required edges in the subgraph are true because all values in this array need to be true before a DMRSGraph is added
        //to foundgraphs arraylist.
        boolean[] checksarr = new boolean[subgraph.getEdges().size()];
        try {
            subgraph.setNodeNeighbours();
        } catch (IndexOutOfBoundsException e) {
            returninfo.put("response", "Failure");
            return returninfo;

        }


        //Iterate over each DMRSGraph in the dataset

        for (AbstractGraph t : graphs.values()) {
            DMRSGraph g = (DMRSGraph) t;
            //If it cant set the Node neighbours then just the specific DMRSGraph iteration.
            try {
                g.setNodeNeighbours();
            } catch (IndexOutOfBoundsException f) {
                continue;
            }

            //Iterate over each Node in the subgraph patttern
            for (Node sn : subgraph.getNodes().values()) {
                //for each Node in the subgraph pattern, it iterates over every Node in the current DMRSGraph that is being checked.
                //This is to find a Node label equal to the current sub DMRSGraph Node being checked.
                for (Node n : g.getNodes().values()) {
                    //This checks if the labels are equal
                    if (n.getLabel().equals(sn.getLabel())) {
                        //Once it finds a Node in the DMRSGraph that is the same as the subgraph Node label it has to iterate over the subgraph edges list
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
            //This checks to see if all values in the boolean array are true and if so adds the current DMRSGraph to the list of found graphs.
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
     * * i.e the "data" key contains a list of hashmaps that contain the DMRSGraph ID's and Inputs of graphs that have the set of Node labels.
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
        //this would allow to avoid using a for loop inside DMRSGraph iteration
        boolean[] checks = new boolean[labels.size()];

        for (AbstractGraph t : graphs.values()) {
            DMRSGraph g = (DMRSGraph)t;
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
            //resets the checks array when its done checking a DMRSGraph
            for (int i = 0; i < checks.length; i++) {
                checks[i] = false;
            }
        }

        returninfo.put("response", "Success");
        returninfo.put("data", FoundGraphs);
        return returninfo;
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

    /**
     * Compares two graphs and searches for similarities and differences.
     *
     * @param graphID1 DMRSGraph ID of the first DMRSGraph.
     * @param graphID2 DMRSGraph ID of the second DMRSGraph.
     * @return HashMap<String, Object> The differences and similarities of the two graphs i.e
     * the "SimilarNodes1" key gives the Node ids of the similar nodes in graph1.
     * the "SimilarNodes2" key gives the Node ids of the similar nodes in graph2.
     * the "SimilarEdges1" key gives the Node ids of the similar edges in graph1.
     * the "SimilarEdge2" key gives the Node ids of the similar edges in graph2.
     */
    public HashMap<String, Object> compareTwoGraphs(String graphID1, String graphID2) {
        DMRSGraph g1 = (DMRSGraph)graphs.get(graphID1);
        DMRSGraph g2 = (DMRSGraph)graphs.get(graphID2);
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

        //iterates over every Node of each DMRSGraph
        for (Node n1 : nodes1.values()) {
            for (Node n2 : nodes2.values()) {
                int span1 = n1.getAnchors().getEnd() - n1.getAnchors().getFrom();
                int span2 = n2.getAnchors().getEnd() - n2.getAnchors().getFrom();
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
                            span1 = nn1.getAnchors().getEnd() - nn1.getAnchors().getFrom();
                            span2 = nn2.getAnchors().getEnd() - nn2.getAnchors().getFrom();

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
     * Runs formal tests on a DMRSGraph.
     *
     * @param graphID               The DMRSGraph ID which the tests will be run.
     * @param planar                Boolean to decide if to test for if the DMRSGraph is planar.
     * @param longestPathDirected   Boolean to decide if to find the longest directed path.
     * @param longestPathUndirected Boolean to decide if to find the longest undirected path.
     * @param connected             Boolean to decide if to test for if the DMRSGraph is connected.
     * @return HashMap<String, Object> Results of the tests i.e
     * the "Planar" key returns a boolean of whether or not the DMRSGraph is planar
     * the "PlanarVis" key returns the visualisation data
     * the "LongestPathDirected" key returns an ArrayList of an Arraylist of integers defining the multiple longest directed paths in the graphs
     * the "LongestPathUndirected" key returns an ArrayList of an Arraylist of integers defining the multiple longest undirected paths in the graphs
     * the "Connected" returns a boolean of whether or not the DMRSGraph is connected.
     */
    public HashMap<String, Object> runFormalTests(String graphID, boolean planar, boolean longestPathDirected, boolean longestPathUndirected, boolean connected) {
        HashMap<String, Object> returnObj = new HashMap<>();
        DMRSGraph g = (DMRSGraph) graphs.get(graphID);
        if (planar) {

            returnObj.put("Planar", g.isPlanar());
            returnObj.put("PlanarVis", VisualisePlanar(graphs.get(graphID)));
        }
        if (longestPathDirected) {
            //checks if graphs are cyclic, if so returns a message indicating the DMRSGraph is cyclic otherwise sends back the longest path directed information
            if (graphs.get(graphID).isCyclic(true)) {
                returnObj.put("LongestPathDirected", "Cycle Detected");
            } else {
                returnObj.put("LongestPathDirected", graphs.get(graphID).findLongest(true));
            }
        }
        //checks if graphs are cyclic, if so returns a message indicating the DMRSGraph is cyclic otherwise sends back the longest path directed information
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
     * This method returns visualisation information so that it can be visualised on the front-end
     *
     * @param graphID This is the graphID of the DMRSGraph to be visualised
     * @param format  this is the format of the visualisation i.e
     *                format 1 - hierarchical,
     *                format 2 - tree like,
     *                format 3 - flat,
     *                format 4 - planar.
     * @return HashMap<String, Object> This is the visualisation information that is used to display the visualisation on the front-end
     */
    public HashMap<String, Object> Visualise(String graphID, int format) {
        DMRSGraph graph = (DMRSGraph) graphs.get(graphID);
        if (format == 1) {
            return VisualiseHierarchy(graph);
        } else if (format == 2) {
            return VisualiseTree(graph);
        } else if (format == 3) {
            return VisualiseFlat(graph);
        } else if (format == 4) {
            return VisualisePlanar(graph);
        }
        return null;
    }

    public HashMap<String, Object> VisualisePlanar(AbstractGraph Agraph) {

        //gets the planar optimised DMRSGraph
        DMRSGraph Dgraph = (DMRSGraph)Agraph;
        DMRSGraph graph = Dgraph.PlanarGraph();

        ArrayList<Edge> crossingEdges = new ArrayList<>();

        //checks which edges are crossing and adds them to the list
        for (Edge e : graph.getEdges()) {

            for (Edge other : graph.getEdges()) {

                if (Math.min(Integer.parseInt(e.getSource()), Integer.parseInt(e.getTarget())) < Math.min(Integer.parseInt(other.getSource()), Integer.parseInt(other.getTarget())) && Math.min(Integer.parseInt(other.getSource()), Integer.parseInt(other.getTarget())) < Math.max(Integer.parseInt(e.getSource()), Integer.parseInt(e.getTarget())) && Math.max(Integer.parseInt(e.getSource()), Integer.parseInt(e.getTarget())) < Math.max(Integer.parseInt(other.getSource()), Integer.parseInt(other.getTarget()))) {

                    crossingEdges.add(e);
                    crossingEdges.add(other);
                }

            }
        }

        int height = 1;
        int totalGraphHeight = height * 50 + (height - 1) * 70; //number of levels times the height of each Node and the spaces between them

        ArrayList<HashMap<String, Object>> finalNodes = new ArrayList<>();


        HashMap<Integer, Integer> currentLevels = new HashMap<>();
        //Add all the Node data in the correct format readable by the front end
        for (Node n : graph.getNodes().values()) {
            if (!currentLevels.containsKey(n.getAnchors().getFrom())) {
                currentLevels.put(n.getAnchors().getFrom(), 0);
            } else {
                currentLevels.put(n.getAnchors().getFrom(), currentLevels.get(n.getAnchors().getFrom()) + 1);
            }

            //puts all the Node data in the hashmap
            HashMap<String, Object> singleNode = new HashMap<>();
            singleNode.put("id", n.getAnchors().getFrom() + currentLevels.get(n.getAnchors().getFrom()) * graph.getNodes().size());
            singleNode.put("x", n.getAnchors().getFrom() * 110);
            singleNode.put("y", totalGraphHeight + currentLevels.get(n.getAnchors().getFrom()) * 100);
            singleNode.put("label", n.getLabel());
            if (n.getLabel().startsWith("_")) {
                singleNode.put("group", "surfaceNode");
            } else {
                singleNode.put("group", "node");
            }
            singleNode.put("type", "node");
            singleNode.put("anchors", n.getAnchors());
            singleNode.put("group", "node");
            singleNode.put("fixed", true);
            finalNodes.add(singleNode);

        }


        ArrayList<HashMap<String, Object>> finalGraphEdges = new ArrayList<>();
        String fromID = "0", toID = "0";
        //Add all the Edge data in the correct format readable by the frontend
        for (int i = 0; i < graph.getEdges().size(); i++) {
            HashMap<String, Object> singleEdge = new HashMap<>();
            Edge e = graph.getEdges().get(i);
            String fromNode = null;
            String toNode = null;


                    fromID = e.getSource();

                    toID = e.getTarget();


            String edgeType = "";

            if (Integer.parseInt(fromID)>Integer.parseInt(toID)) {
                edgeType = "curvedCCW";
            } else {
                edgeType = "curvedCW";
            }
            if (fromID.equals(toID)) {
                continue;
            }
            singleEdge.put("id", i);
            singleEdge.put("from", fromID);
            singleEdge.put("to", toID);
            singleEdge.put("label", e.getLabel());

            singleEdge.put("color", "rgba(0,0,0,1)");
            singleEdge.put("group", "normal");
            singleEdge.put("shadow", false);
            HashMap<String, Object> back = new HashMap<>();
            back.put("enabled", true);
            if (crossingEdges.contains(e)) {
                back.put("color", "#ff0000");
            }
            singleEdge.put("background", back);

            HashMap<String, Object> smooth = new HashMap<>();
            smooth.put("type", edgeType);
            smooth.put("roundness", 0.6);

            HashMap<String, Object> end = new HashMap<>();
            end.put("from", 20);
            end.put("to", 0);
            singleEdge.put("smooth", smooth);
            singleEdge.put("endPointOffset", end);
            finalGraphEdges.add(singleEdge);

        }

        HashMap<String, Object> Visualised = new HashMap<>();
        Visualised.put("id", graph.getId());
        Visualised.put("nodes", finalNodes);
        Visualised.put("edges", finalGraphEdges);

        return Visualised;


    }

    /**
     * The visualisation method for visualising a Flat DMRSGraph.
     *
     * @param graph the DMRSGraph object to be visualised in a Flat format
     * @return HashMap<String, Object> This is the visualisation information that is used to display the visualisation on the front-end
     */
    public HashMap<String, Object> VisualiseFlat(AbstractGraph graph) {

        ArrayList<Node> ordered = new ArrayList<>();

        for (Node n : graph.getNodes().values()) {
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

        int height = 1;
        int totalGraphHeight = height * 50 + (height - 1) * 70; //number of levels times the height of each Node and the spaces between them

        ArrayList<HashMap<String, Object>> finalNodes = new ArrayList<>();
        int tops = 0;
        //simply iterate over the nodes and add their data to the visualisation object
        for (int i = 0; i < ordered.size(); i++) {
            Node n = ordered.get(i);
            if (n.getId().equals(graph.getTop())) {
                tops = i;
            }
            HashMap<String, Object> singleNode = new HashMap<>();
            singleNode.put("id", n.getId());
            singleNode.put("x", i * 130);
            singleNode.put("y", totalGraphHeight);
            singleNode.put("label", n.getLabel());
            singleNode.put("type", "node");
            singleNode.put("anchors", n.getAnchors());
            if (n.getLabel().startsWith("_")) {
                singleNode.put("group", "surfaceNode");
            } else {
                singleNode.put("group", "node");
            }
            singleNode.put("fixed", true);
            finalNodes.add(singleNode);
        }
        //create top Node
        if (graph.getNodes().containsKey(graph.getTop())) {
            HashMap<String, Object> singleNode = new HashMap<>();
            singleNode.put("id", "TOP");
            singleNode.put("x", tops * 130);
            singleNode.put("y", totalGraphHeight - 150);
            singleNode.put("label", "TOP");
            singleNode.put("type", "node");
            singleNode.put("group", "top");
            singleNode.put("fixed", true);

            finalNodes.add(singleNode);
        }


        ArrayList<HashMap<String, Object>> finalGraphEdges = new ArrayList<>();
        String fromID = "", toID = "";

        for (Edge e : graph.getEdges()) {
            HashMap<String, Object> singleEdge = new HashMap<>();
            for (HashMap<String, Object> node : finalNodes) {
                if (!node.get("id").equals((String) "TOP")) {
                    if (node.get("id").equals(e.getSource())) {
                        fromID = e.getSource();


                    }
                    if (node.get("id").equals( e.getTarget())) {
                        toID = e.getTarget();

                    }
                }
            }
            String edgeType = "curvedCW";

            singleEdge.put("id", graph.getEdges().indexOf(e));
            singleEdge.put("from", fromID);
            singleEdge.put("to", toID);
            singleEdge.put("label", e.getLabel());
            singleEdge.put("group", "normal");
            singleEdge.put("shadow", false);
            HashMap<String, Object> back = new HashMap<>();
            back.put("enabled", false);
            singleEdge.put("background", back);

            HashMap<String, Object> smooth = new HashMap<>();
            smooth.put("type", edgeType);
            smooth.put("roundness", 0.4);

            HashMap<String, Object> end = new HashMap<>();
            end.put("from", 20);
            end.put("to", 0);
            singleEdge.put("smooth", smooth);
            singleEdge.put("endPointOffset", end);
            finalGraphEdges.add(singleEdge);

        }
        HashMap<String, Object> singleEdge = new HashMap<>();
        String edgeType = "dynamic";

        if (graph.getNodes().containsKey(graph.getTop())) {
            singleEdge.put("id", graph.getEdges().size());
            singleEdge.put("from", "TOP");
            singleEdge.put("to", graph.getTop());
            singleEdge.put("group", "tokenEdge");
            singleEdge.put("shadow", false);
            HashMap<String, Object> back = new HashMap<>();
            back.put("enabled", false);
            singleEdge.put("background", back);

            HashMap<String, Object> smooth = new HashMap<>();
            smooth.put("type", edgeType);
            smooth.put("roundness", 0.4);

            HashMap<String, Object> end = new HashMap<>();
            end.put("from", 20);
            end.put("to", 0);
            singleEdge.put("smooth", smooth);
            singleEdge.put("endPointOffset", end);

            singleEdge.put("dashes", true);
            HashMap<String, Object> arrows = new HashMap<>();
            HashMap<String, Object> to = new HashMap<>();
            to.put("enabled", false);
            arrows.put("to", to);
            singleEdge.put("arrows", arrows);

            finalGraphEdges.add(singleEdge);
        }

        HashMap<String, Object> Visualised = new HashMap<>();
        Visualised.put("id", graph.getId());
        Visualised.put("nodes", finalNodes);
        Visualised.put("edges", finalGraphEdges);

        return Visualised;

    }

     public HashMap<String, Object> VisualiseTree(AbstractGraph Agraph) {

        DMRSGraph graph = (DMRSGraph)Agraph;


        graph.setNodeNeighbours();


        HashMap<String, Stack<String>> topologicalStacks = new HashMap<>(); //HashMap of each Node's topological sorting of the DMRSGraph.

        //Iterate through each Node in the DMRSGraph and add its topological sorting/stack of the DMRSGraph to the HashMap of topological stacks.
        for (String i : graph.getNodes().keySet()) {
            Stack<String> stack = new Stack<>();
            HashMap<String, Boolean> visited = new HashMap<>();
            for (String j : graph.getNodes().keySet()) {//Set each Node in the DMRSGraph to unvisited
                visited.put(j, false);
            }
            topologicalStacks.put(i, graph.topologicalSort(i, visited, stack));
        }


        int numLevels = 0;
        //Iterate through all the topological sortings/stacks and set the number of levels in the visualisation to the maximum number of descendants a Node can have (i.e.longest topological stack's length)
        for (String i : topologicalStacks.keySet()) {
            numLevels = Math.max(numLevels, topologicalStacks.get(i).size());
        }

        ArrayList<ArrayList<Node>> nodesInLevels = new ArrayList<>();

        //Set each Node in the DMRSGraph to the level based on the the number of descendent nodes they have (the length of their topological stacks). i.e. stack length 2 means 1 descendant which means level 1.
        for (int level = 1; level < numLevels + 1; level++) {
            ArrayList<Node> currentLevel = new ArrayList<>();
            for (String n : graph.getNodes().keySet()) {
                if (topologicalStacks.get(n).size() == level) {
                    currentLevel.add(graph.getNodes().get(n));
                }
            }
            nodesInLevels.add(currentLevel);
        }

        HashMap<String, Integer> xPositions = new HashMap<>(); //HashMap to keep track of the x position of each Node in the DMRSGraph
        HashMap<Integer, Node> lowestNode = new HashMap<>(); // HashMap to keep track of the lowest Node in ech x position

        //Iterate through each level and set its xPosition to the start of its Anchors and if its the first Node in that x position then add it to the lowestNode HashMap.
        for (ArrayList<Node> level : nodesInLevels) {
            for (Node n : level) {
                int column = n.getAnchors().getFrom();
                xPositions.put(n.getId(), column);
                if (!lowestNode.containsKey(column)) {
                    lowestNode.put(column, n);
                }
            }
        }

        //Iterate through each level's nodes if it is not the lowest Node in that x position then set its x position to its child Node's or its left most child that's in span if there are multiple children nodes.
        for (ArrayList<Node> level : nodesInLevels) {
            for (Node n : level) {
                if (lowestNode.get(xPositions.get(n.getId())) != n) {
                    if (n.getDirectedNeighbours().size() == 1) { //Only 1 child Node, so change the parent Node's x position to its child Node's
                        xPositions.put(n.getId(), xPositions.get(n.getDirectedNeighbours().get(0).getId()));
                    } else {

                        ArrayList<String> childrenInSpan = new ArrayList<>(); //Array to keep track of all children Node's in span.
                        for (Node neighbour : n.getDirectedNeighbours()) {
                            //If the child Node is in span, then add it to the childrenInSpan array
                            if (
                                    xPositions.get(neighbour.getId()) >= n.getAnchors().getFrom() &&
                                            xPositions.get(neighbour.getId()) <= n.getAnchors().getEnd()
                            ) {
                                childrenInSpan.add(neighbour.getId());
                            }
                        }
                        if (childrenInSpan.size() != 0) {
                            //Find the parent Node's left most child Node.
                            int leftMostChildPos = xPositions.get(childrenInSpan.get(0));
                            for (String child : childrenInSpan) {
                                leftMostChildPos = Math.min(
                                        leftMostChildPos,
                                        xPositions.get(child)
                                );
                            }
                            xPositions.put(n.getId(), leftMostChildPos); //Set the parent Node's x position to its left most child Node.
                        }
                    }
                }
            }
        }

        HashMap<Integer, HashMap<String, Node>> nodesInFinalLevels = new HashMap<>(); //HashMap mapping Levels to a HashMap of the nodes in that level.

        nodesInFinalLevels.put(0, new HashMap<>());


        int numNodesProcessed = 0;
        int currentLevel = 0;
        //Resolve all overlapping Node's i.e. same level and same x position, until all nodes have been processed.
        while (numNodesProcessed != graph.getNodes().size()) {
            HashMap<Integer, String> nodeXPos = new HashMap<>(); //Temporary HashMap of nodeId to its x position.
            nodesInFinalLevels.put(currentLevel + 1, new HashMap<>()); //Create new level above to push any overlapping nodes to that next level.
            for (Node n : nodesInLevels.get(currentLevel)) { //Iterate through every Node in the current level
                if (nodeXPos.containsKey(xPositions.get(n.getId()))) {//Check if this x position is already occupied
                    if (topologicalStacks.get(n.getId()).size() <topologicalStacks.get(nodeXPos.get(xPositions.get(n.getId()))).size()) { //Compare each of the overlapping Node's number of descendents

                        //Adds new level to push Node to, if the maximum level has already been reached.
                        if (currentLevel + 1 > numLevels - 1) {
                            nodesInLevels.add(new ArrayList<>());
                        }

                        //Push the already occupying Node up a level
                        String currentOccupyingNodeID = nodeXPos.get(xPositions.get(n.getId()));
                        nodeXPos.put(xPositions.get(n.getId()), n.getId()); //Update the temporary x position map to the new Node that will take this position.
                        nodesInLevels.get(currentLevel + 1).add(graph.getNodes().get(currentOccupyingNodeID)); //Push the currently occupying Node up a level
                        nodesInFinalLevels.get(currentLevel).remove(currentOccupyingNodeID); //Remove the currently occupying Node from this level
                        nodesInFinalLevels.get(currentLevel).put(n.getId(), n); //Add the new Node to the current level.
                    } else {

                        //Adds new level to push Node to, if the maximum level has already been reached.
                        if (currentLevel + 1 > numLevels - 1) {
                            nodesInLevels.add(new ArrayList<>());
                        }

                        nodesInLevels.get(currentLevel + 1).add(n); //Push the Node to the next level
                    }
                } else {
                    //No currently occupying Node in this position
                    nodeXPos.put(xPositions.get(n.getId()), n.getId());
                    nodesInFinalLevels.get(currentLevel).put(n.getId(), n);
                    numNodesProcessed++;
                }
            }
            currentLevel++;
        }

        int height = numLevels;

        int totalGraphHeight = height * 50 + (height - 1) * 70; //number of levels times the height of each Node and the spaces between them


        ArrayList<HashMap<String, Object>> finalNodes = new ArrayList<>();

        int levelNum = -1;
        int maxID = Integer.parseInt(graph.getNodes().keySet().iterator().next()); //Keep track of the highest Node ID

        //Iterate through each level and create the front-end Node object and set its attributes
        for (HashMap<String, Node> level : nodesInFinalLevels.values()) {
            levelNum++;
            for (Node n : level.values()) {
                HashMap<String, Object> singleNode = new HashMap<>();
                singleNode.put("id", n.getId());
                maxID = Math.max(Integer.parseInt(n.getId()),maxID);
                singleNode.put("x", xPositions.get(n.getId()) * 130);
                singleNode.put("y", totalGraphHeight - levelNum * (totalGraphHeight / height));
                singleNode.put("label", n.getLabel());
                singleNode.put("type", "node");
                singleNode.put("nodeLevel", levelNum);
                singleNode.put("anchors", n.getAnchors());
                if (n.getLabel().startsWith("_")) {
                    singleNode.put("group", "surfaceNode");
                } else {
                    singleNode.put("group", "node");
                }

                singleNode.put("fixed", true);
                finalNodes.add(singleNode);
            }
        }


        ArrayList<HashMap<String, Object>> finalTokens = new ArrayList<>();

        //Iterate through all the tokens and create the front-end Token object and set its attributes
        for (Token t : graph.getTokens()) {
            HashMap<String, Object> singleToken = new HashMap<>();
            singleToken.put("id", t.getIndex() + maxID + 1);
            singleToken.put("x", t.getIndex() * 130);
            singleToken.put("y", totalGraphHeight + 200);
            singleToken.put("label", t.getForm());
            singleToken.put("shape", "text");
            singleToken.put("type", "token");
            singleToken.put("group", "text");
            singleToken.put("fixed", true);
            finalTokens.add(singleToken);
        }


        ArrayList<HashMap<String, Object>> finalGraphEdges = new ArrayList<>();
        int  fromLevel = 0, toLevel = 0, fromX = 0, toX = 0;
        String fromID = "", toID = "";
        //Iterate through each Node and create the front-end Edge object and set its attributed.
        for (Edge e : graph.getEdges()) {
            HashMap<String, Object> singleEdge = new HashMap<>();

            //Find the Edge's source and target Node's IDs, levels and x positions.
            for (HashMap<String, Object> node : finalNodes) {

                if (node.get("id").equals(e.getSource())) {
                    fromID = e.getSource();
                    fromLevel = (Integer) node.get("nodeLevel");
                    fromX = (Integer) node.get("x");
                }
                if (node.get("id").equals(e.getTarget())) {
                    toID = e.getTarget();
                    toLevel = (Integer) node.get("nodeLevel");
                    toX = (Integer) node.get("x");
                }
            }

            String edgeType = ""; //Dictate the Edge type
            double round = 0.5; //The roundness the Edge must take on

            //Check if there exists another Edge with the same source and target Node as this Edge, decide on its direction based on its relative index to the other.
            int count = 0;
            int thisEdge = 0;
            for (Edge edge : graph.getNodes().get(fromID).getDirectedEdgeNeighbours()) {
                if (edge.getTarget().equals(toID)){
                    count++;
                    if (edge.equals(e)){
                        thisEdge = count;
                    }
                }
            }

            if (count > 1){
                if (fromLevel - toLevel > 1) {
                    round = 0.2; //Decreased roundness to not overlap to the next column if the levels are more than 3 levels apart.
                }
                if (fromLevel - toLevel > 5) {
                    round = 0.1; //Decreased roundness to not overlap to the next column if the levels are more than 3 levels apart.
                }
                if (thisEdge%2==0){
                    edgeType = "curvedCW";
                }
                else{
                    edgeType = "curvedCCW";
                }
            }
            else if (fromX == toX) {
                if (fromLevel - toLevel == 1) {
                    edgeType = "continuous"; //Same column and only level apart
                } else {
                    boolean notFound = true;
                    //Iterate through all the nodes and check if there exists a Node between the source and target nodes.
                    for (HashMap<String, Object> node : finalNodes) {
                        if ((Integer) node.get("x") == fromX && (Integer) node.get("nodeLevel") > toLevel && (Integer) node.get("nodeLevel") < fromLevel) {
                            notFound = false;
                        }
                    }
                    if (
                            notFound
                    ) {
                        edgeType = "continuous"; //No Node in between the two nodes.
                    } else {

                        if (fromLevel - toLevel > 3) {
                            round = 0.32; //Decreased roundness to not overlap to the next column if the levels are more than 3 levels apart.
                        }
                        edgeType = "curvedCCW"; //There exists a Node in between the two nodes and thus the Edge must curve to the left around the interrupting Node.

                        //Check if there already exists an Edge in that direction from the same start Node.
                        for (int i = 0; i < graph.getNodes().get(fromID).getDirectedNeighbours().size(); i++) {
                            if (
                                    xPositions.get(fromID) -
                                            xPositions.get(graph.getNodes().get(fromID).getDirectedNeighbours().get(i).getId()) == 1
                            ) {
                                edgeType = "curvedCW"; //There is already an Edge going left and thus make the Node go right instead.
                                break;
                            }
                        }
                    }
                }
            } else {
                edgeType = "dynamic"; //standard Edge
            }

            //Set the Edge's attributes
            singleEdge.put("id", graph.getEdges().indexOf(e));
            singleEdge.put("from", fromID);
            singleEdge.put("to", toID);
            singleEdge.put("label", e.getLabel());
            singleEdge.put("group", "normal");
            singleEdge.put("shadow", false);
            HashMap<String, Object> back = new HashMap<>();
            back.put("enabled", false);
            singleEdge.put("background", back);

            HashMap<String, Object> smooth = new HashMap<>();
            smooth.put("type", edgeType);
            smooth.put("roundness", round);

            HashMap<String, Object> end = new HashMap<>();
            end.put("from", 20);
            end.put("to", 0);
            singleEdge.put("smooth", smooth);
            singleEdge.put("endPointOffset", end);
            finalGraphEdges.add(singleEdge);

        }


        ArrayList<HashMap<String, Object>> connectedTokens = new ArrayList<>();
        int index = finalGraphEdges.size();

        //Add dotted edges so for the lowest Node in each column to the corresponding tokens
        for (HashMap<String, Object> n : finalNodes) {
            String group = (String) n.get("group");
            if (group.equals("node") || group.equals("surfaceNode")) {

                HashMap<String, Object> token = null;
                boolean Found = false;
                //Find the corresponding Token
                for (HashMap<String, Object> t : finalTokens) {

                    if (!connectedTokens.contains(t) && t.get("x").equals(n.get("x"))) {
                        Found = true;
                        token = t;
                    }
                }

                if (Found) {

                    //Create new Edge and set new attributes.
                    HashMap<String, Object> singleEdge = new HashMap<>();
                    singleEdge.put("id", index);
                    singleEdge.put("from", n.get("id"));
                    singleEdge.put("to", token.get("id"));
                    singleEdge.put("label", "");
                    singleEdge.put("group", "tokenEdge");
                    singleEdge.put("shadow", false);
                    HashMap<String, Object> back = new HashMap<>();
                    back.put("enabled", false);
                    singleEdge.put("background", back);

                    HashMap<String, Object> smooth = new HashMap<>();
                    smooth.put("type", "continuous");
                    smooth.put("roundness", 0.5);

                    HashMap<String, Object> end = new HashMap<>();
                    end.put("from", 20);
                    end.put("to", 0);
                    singleEdge.put("smooth", smooth);
                    singleEdge.put("endPointOffset", end);

                    singleEdge.put("dashes", true);
                    HashMap<String, Object> arrows = new HashMap<>();
                    HashMap<String, Object> to = new HashMap<>();
                    to.put("enabled", false);
                    arrows.put("to",to);
                    singleEdge.put("arrows" ,arrows);


                    finalGraphEdges.add(singleEdge);

                    index++;
                    connectedTokens.add(token);
                }

            }
        }

        //Combine nodes and tokens
        for (HashMap<String, Object> token : finalTokens) {
            finalNodes.add(token);
        }
        HashMap<String, Object> Visualised = new HashMap<>();
        Visualised.put("id", graph.getId());
        Visualised.put("nodes", finalNodes);
        Visualised.put("edges", finalGraphEdges);

        return Visualised;
    }

    public HashMap<String, Object> VisualiseHierarchy(AbstractGraph Agraph) {

        DMRSGraph graph = (DMRSGraph)Agraph;



        //Determine span lengths of each Node
        HashMap<String, Integer> graphNodeSpanLengths = new HashMap<>();
        for (Node n : graph.getNodes().values()) {
            int span = n.getAnchors().getEnd() - n.getAnchors().getFrom();
            graphNodeSpanLengths.put(n.getId(), span);
        }

        //Determine unique span lengths of all the Node spans
        ArrayList<Integer> uniqueSpanLengths = new ArrayList<>();

        HashMap<Integer, Boolean> map = new HashMap<>();
        for (Integer item : graphNodeSpanLengths.values()) {
            if (!map.containsKey(item)) {
                map.put(item, true); // set any value to Map
                uniqueSpanLengths.add(item);
            }
        }
        Collections.sort(uniqueSpanLengths);


        //Sort the nodes into each level based on their spans
        ArrayList<ArrayList<Node>> nodesInLevels = new ArrayList<>();
        for (int level : uniqueSpanLengths) {
            ArrayList<Node> currentLevel = new ArrayList<>();
            for (String spanIndex : graphNodeSpanLengths.keySet()) {
                if (graphNodeSpanLengths.get(spanIndex) == level) {
                    currentLevel.add(graph.getNodes().get(spanIndex));
                }
            }

            nodesInLevels.add(currentLevel);
        }
        //Find the nodes in each level with the same span and group them together
        //Find the unique spans in each level
        ArrayList<ArrayList<String>> uniqueSpansInLevels = new ArrayList<>();

        for (ArrayList<Node> level : nodesInLevels) {

            ArrayList<String> uniqueSpans = new ArrayList<>(); //Stores the "stringified" objects

            HashMap<String, Boolean> spanMap = new HashMap<>();
            for (Node node : level) {
                String span = node.getAnchors().getFrom() + " " + node.getAnchors().getEnd();
                if (!spanMap.containsKey(span)) {
                    spanMap.put(span, true); // set any value to Map
                    uniqueSpans.add(span);
                }
            }
            uniqueSpansInLevels.add(uniqueSpans);

        }

        ArrayList<ArrayList<ArrayList<Node>>> newNodeLevels = new ArrayList<>();
        //Iterate through the unique spans in each level and group the same ones together
        for (int level = 0; level < nodesInLevels.size(); level++) {
            ArrayList<ArrayList<Node>> newLevelOfGroups = new ArrayList<>();
            for (String uniqueSpan : uniqueSpansInLevels.get(level)) {
                //find the nodes in the level that have the same span and group them together
                ArrayList<Node> nodesWithCurrentSpan = new ArrayList<>();
                for (Node n : nodesInLevels.get(level)) {
                    String span = n.getAnchors().getFrom() + " " + n.getAnchors().getEnd();
                    if (span.equals(uniqueSpan)) {
                        nodesWithCurrentSpan.add(n);
                    }
                }

                newLevelOfGroups.add(nodesWithCurrentSpan);
            }
            newNodeLevels.add(newLevelOfGroups);
        }

        //Determine the actual number of levels needed
        int height = 0;
        ArrayList<Integer> previousLevelHeights = new ArrayList<>();
        previousLevelHeights.add(0);
        for (ArrayList<ArrayList<Node>> level : newNodeLevels) {
            int maxLevelHeight = 0;
            for (ArrayList<Node> item : level) {
                maxLevelHeight = Math.max(maxLevelHeight, item.size());
            }
            previousLevelHeights.add(maxLevelHeight);
            height += maxLevelHeight;
        }


        //Sort the nodes into the final levels
        ArrayList<ArrayList<Node>> nodesInFinalLevels = new ArrayList<>();
        for (int index = 0; index < height; index++) {
            nodesInFinalLevels.add(new ArrayList<Node>());
        }
        for (int level = 0; level < newNodeLevels.size(); level++) {
            for (ArrayList<Node> group : newNodeLevels.get(level)) {
                for (
                        int nodeGroupIndex = 0;
                        nodeGroupIndex < group.size();
                        nodeGroupIndex++
                ) {
                    ArrayList<Integer> finalLevel = new ArrayList<>();

                    finalLevel.addAll(previousLevelHeights.subList(0, level + 1));
                    int finalIndex = 0;
                    for (Integer i : finalLevel) {
                        finalIndex += i;
                    }
                    finalIndex = finalIndex + nodeGroupIndex;


                    nodesInFinalLevels.get(finalIndex).add(group.get(nodeGroupIndex));
                }
            }
        }

        //Map the nodes in each level to the correct format

        int totalGraphHeight = height * 50 + (height - 1) * 70; //number of levels times the height of each Node and the spaces between them
        int space = 130;

        ArrayList<HashMap<String, Object>> finalNodes = new ArrayList<>();

        int maxID = Integer.parseInt(graph.getNodes().keySet().iterator().next());

        for (int level = 0; level < nodesInFinalLevels.size(); level++) {
            for (Node n : nodesInFinalLevels.get(level)) {
                HashMap<String, Object> singleNode = new HashMap<>();
                singleNode.put("id", n.getId());
                maxID = Math.max(Integer.parseInt(n.getId()), maxID);
                singleNode.put("x", (n.getAnchors().getEnd() * space + n.getAnchors().getFrom() * space) / 2);
                singleNode.put("y", totalGraphHeight - level * (totalGraphHeight / height));
                singleNode.put("label", n.getLabel());
                singleNode.put("type", "node");
                singleNode.put("nodeLevel", level);
                singleNode.put("anchors", n.getAnchors());
                if (n.getLabel().startsWith("_")) {
                    singleNode.put("group", "surfaceNode");
                } else {
                    singleNode.put("group", "node");
                }
                singleNode.put("fixed", true);
                HashMap<String,Object> widthCon = new HashMap<>();
                widthCon.put("minimum", n.getAnchors().getEnd() * space - n.getAnchors().getFrom() * space + 70);
                widthCon.put("maximum", n.getAnchors().getEnd() * space - n.getAnchors().getFrom() * space + 70);
                singleNode.put("widthConstraint", widthCon);
                finalNodes.add(singleNode);
            }
        }

        if (graph.getNodes().containsKey(graph.getTop())) {
            HashMap<String, Object> singleNode = new HashMap<>();
            singleNode.put("id", "TOP");
            singleNode.put("x", (graph.getNodes().get(graph.getTop()).getAnchors().getEnd() * space + graph.getNodes().get(graph.getTop()).getAnchors().getFrom() * space) / 2);
            singleNode.put("y", totalGraphHeight - nodesInFinalLevels.size() * (totalGraphHeight / height));
            singleNode.put("label", "TOP");
            singleNode.put("type", "node");
            singleNode.put("nodeLevel", nodesInFinalLevels.size());
            singleNode.put("anchors", graph.getNodes().get(graph.getTop()).getAnchors());
            singleNode.put("group", "top");
            singleNode.put("fixed", true);
            finalNodes.add(singleNode);
        }


        ArrayList<HashMap<String, Object>> finalGraphEdges = new ArrayList<>();
        int  fromLevel = 0, toLevel = 0, fromX = 0, toX = 0;
        String fromID = "0", toID = "0";
        graph.setNodeNeighbours();
        int maxEdgeSpan = 0;
        for (Edge e : graph.getEdges()) {
            HashMap<String, Object> singleEdge = new HashMap<>();
            for (HashMap<String, Object> node : finalNodes) {
                if (!node.get("id").equals("TOP")) {
                    if (node.get("id").equals(e.getSource())) {
                        fromID = e.getSource();
                        fromLevel = (Integer) node.get("nodeLevel");
                        fromX = (Integer) node.get("x");
                    }
                    if (node.get("id").equals(e.getTarget())) {
                        toID = e.getTarget();
                        toLevel = (Integer) node.get("nodeLevel");
                        toX = (Integer) node.get("x");
                    }
                }
            }
            maxEdgeSpan = Math.max(maxEdgeSpan, Math.abs(toX - fromX));

            String edgeType = "";
            double round = 0.5; //The roundness of the Edge

            int spanLower = graph.getNodes().get(fromID).getAnchors().getFrom() * space;
            int spanUpper = graph.getNodes().get(fromID).getAnchors().getEnd() * space;

            //Edge layout algorithm to best avoid edges from overlapping
            if (toX <= spanUpper && toX >= spanLower) {
                if (Math.abs(fromLevel - toLevel) == 1) {
                    edgeType = "continuous"; //From Node and to Node are within span and only 1 level apart, so make the Edge continuous

                    //Check if there exists another Edge with the same source and target Node as this Edge, decide on its direction based on its relative index to the other.
                    int count = 0;
                    int thisEdge = 0;
                    for (Edge edge : graph.getNodes().get(fromID).getDirectedEdgeNeighbours()) {
                        if (edge.getTarget().equals(toID)){
                            count++;
                            if (edge.equals(e)){
                                thisEdge = count;
                            }
                        }
                    }

                    if (count > 1){
                        if (thisEdge%2==0){
                            edgeType = "curvedCW";
                        }
                        else{
                            edgeType = "curvedCCW";
                        }
                    }

                } else {
                    boolean notFound = true;
                    double protrusionHeight = 0.0;
                    double protrusionWidth = 0.0;
                    if (fromX == toX) {//Same column
                        //Check if there exists a Node in between the From Node and To Node
                        for (HashMap<String, Object> node : finalNodes) {
                            if ((Integer) node.get("x") == fromX) {
                                if ((((Integer) node.get("nodeLevel") > toLevel) && ((Integer) node.get("nodeLevel") < fromLevel)) || (((Integer) node.get("nodeLevel") < toLevel) && ((Integer) node.get("nodeLevel") > fromLevel))) {
                                    notFound = false;
                                }
                            }
                        }
                    } else {//Different column

                        //Check if there exists a Node in between the From Node and To Node
                        for (HashMap<String, Object> node : finalNodes) {
                            if ((Integer) node.get("x") <= spanUpper && (Integer) node.get("x") >= spanLower) {
                                if ((((Integer) node.get("nodeLevel") > toLevel) && ((Integer) node.get("nodeLevel") < fromLevel)) || (((Integer) node.get("nodeLevel") < toLevel) && ((Integer) node.get("nodeLevel") > fromLevel))) {
                                    if ((((Integer) node.get("x") >= toX) && ((Integer) node.get("x") <= fromX))) {
                                        notFound = false;

                                        //Set the protrusion level of the found Node, to quantify how much it is in the way.
                                        if (protrusionHeight < ((Integer) node.get("nodeLevel")) / (double) fromLevel && protrusionWidth < Math.abs(fromX - (Integer) node.get("x")) / (double) Math.abs(fromX - spanLower)) {
                                            protrusionHeight = (Integer) node.get("nodeLevel") / (double) fromLevel;
                                            protrusionWidth = Math.abs(fromX - (Integer) node.get("x")) / (double) Math.abs(fromX - spanLower);
                                        }
                                    } else if (((Integer) node.get("x") <= toX) && ((Integer) node.get("x") >= fromX)) {
                                        notFound = false;

                                        //Set the protrusion level of the found Node, to quantify how much it is in the way.
                                        if (protrusionHeight < ((Integer) node.get("nodeLevel")) / (double) fromLevel && protrusionWidth < Math.abs(fromX - (Integer) node.get("x")) / (double) Math.abs(fromX - spanUpper)) {
                                            protrusionHeight = (Integer) node.get("nodeLevel") / (double) fromLevel;
                                            protrusionWidth = Math.abs(fromX - (Integer) node.get("x")) / (double) Math.abs(fromX - spanUpper);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (notFound) {
                        edgeType = "dynamic"; //There is no Node between the From Node and To Node, so make the Edge type dynamic.

                    } else {
                        if (Math.abs(fromLevel - toLevel) > 3) {
                            round = 0.36; //Prevents long edges from protruding into an adjacent column
                        }
                        if (Math.abs(fromLevel - toLevel) > 4) {
                            round = 0.32; //Prevents long edges from protruding into an adjacent column
                        }
                        if ((protrusionHeight > 0.55) && (protrusionWidth > 0.55)) {
                            round = 0.47; //If the in between Node found has a high level of protrusion then make the Edge rounder to avoid clash
                        }
                        if (protrusionWidth > 0.8) {
                            round = 0.4; //If the in between Node found has a high level of vertical protrusion then make the Edge rounder to avoid clash
                        }

                        edgeType = "curvedCCW"; //Default Edge to left to avoid clash
                        if (fromLevel < toLevel) { //If Edge is upwards
                            edgeType = "curvedCW"; //Default Edge to left to avoid clash
                        }
                        if (fromX == toX) {//Same column

                            //Check neighbours to see if there already exists an Edge to the left, if so, change it to right. Same for edges facing upwards
                            for (Node neighbour : graph.getNodes().get(fromID).getDirectedNeighbours()) {
                                if (fromX / space - neighbour.getAnchors().getFrom() == 1) {
                                    if (fromLevel < toLevel) {
                                        edgeType = "curvedCCW";
                                    } else {
                                        edgeType = "curvedCW";
                                    }
                                }
                            }

                            //Check neighbours to see if there already exists an Edge to the left, if so, change it to right. Same for edges facing upwards
                            for (Node neighbour : graph.getNodes().get(toID).getDirectedNeighbours()) {
                                if (fromX / space - neighbour.getAnchors().getFrom() == 1) {
                                    if (fromLevel < toLevel) {
                                        edgeType = "curvedCCW";
                                    } else {
                                        edgeType = "curvedCW";
                                    }
                                }
                            }
                        } else if (fromX < toX) {
                            if (fromLevel < toLevel) {
                                edgeType = "curvedCCW";
                            } else {
                                edgeType = "curvedCW";
                            }
                        } else {
                            if (fromLevel < toLevel) {
                                edgeType = "curvedCW";
                            } else {
                                edgeType = "curvedCCW";
                            }
                        }
                    }
                }
            } else {
                if (fromLevel == toLevel) { //Same level
                    edgeType = "curvedCCW"; //Make Edge go under the DMRSGraph
                    round = 0.5;
                    int difference = fromX / space - toX / space;
                    if (Math.abs(difference) > 4) {
                        round = 0.3; //Change Edge roundness to avoid going off the page for long edges
                    }
                    if (Math.abs(difference) > 10) {
                        round = 0.2; //Change Edge roundness to avoid going off the page for long edges
                    }
                    if (difference > 0 && fromLevel == 0) {
                        edgeType = "curvedCW"; //If the Edge is going backwards, still make it go under the DMRSGraph
                    }

                    int count = 0;
                    int thisEdge = 0;
                    for (Edge edge : graph.getNodes().get(fromID).getDirectedEdgeNeighbours()) {
                        if (edge.getTarget() == toID){
                            count++;
                            if (edge.equals(e)){
                                thisEdge = count;
                            }
                        }
                    }

                    if (count > 1){
                        if (thisEdge%2==0){
                            round = round + 0.15;
                        }
                    }


                } else {
                    edgeType = "dynamic"; //Not in the same column, level or spans
                }
            }


            singleEdge.put("id", graph.getEdges().indexOf(e));
            singleEdge.put("from", fromID);
            singleEdge.put("to", toID);
            singleEdge.put("label", e.getLabel());

            singleEdge.put("shadow", false);
            HashMap<String, Object> back = new HashMap<>();
            back.put("enabled", false);
            singleEdge.put("background", back);

            HashMap<String, Object> smooth = new HashMap<>();
            smooth.put("type", edgeType);
            smooth.put("roundness", round);

            HashMap<String, Object> end = new HashMap<>();
            end.put("from", 20);
            end.put("to", 0);
            singleEdge.put("smooth", smooth);
            singleEdge.put("endPointOffset", end);
            finalGraphEdges.add(singleEdge);

        }

        if (graph.getNodes().containsKey(graph.getTop())) {
            HashMap<String, Object> singleEdge = new HashMap<>();
            singleEdge.put("id", graph.getEdges().size());
            singleEdge.put("from", "TOP");
            singleEdge.put("to", graph.getTop());
            singleEdge.put("group", "tokenEdge");
            singleEdge.put("shadow", false);
            HashMap<String, Object> back = new HashMap<>();
            back.put("enabled", false);
            singleEdge.put("background", back);

            HashMap<String, Object> smooth = new HashMap<>();
            smooth.put("type", "continuous");
            smooth.put("roundness", 0.4);

            HashMap<String, Object> end = new HashMap<>();
            end.put("from", 20);
            end.put("to", 0);
            singleEdge.put("smooth", smooth);
            singleEdge.put("endPointOffset", end);

            singleEdge.put("dashes", true);
            HashMap<String, Object> arrows = new HashMap<>();
            HashMap<String, Object> to = new HashMap<>();
            to.put("enabled", false);
            arrows.put("to", to);
            singleEdge.put("arrows", arrows);

            finalGraphEdges.add(singleEdge);
        }

        ArrayList<HashMap<String, Object>> finalTokens = new ArrayList<>();

        for (Token t : graph.getTokens()) {
            HashMap<String, Object> singleToken = new HashMap<>();
            singleToken.put("index", t.getIndex());
            singleToken.put("x", t.getIndex() * space);
            singleToken.put("y", totalGraphHeight + 200 + maxEdgeSpan / 28);
            singleToken.put("label", t.getForm());
            singleToken.put("type", "token");
            singleToken.put("group", "token");
            singleToken.put("fixed", true);
            finalTokens.add(singleToken);
        }


        ArrayList<HashMap<String, Object>> finalGraphNodes = new ArrayList<>();
        finalGraphNodes.addAll(finalNodes);
        finalGraphNodes.addAll(finalTokens);

        HashMap<String, Object> Visualised = new HashMap<>();
        Visualised.put("id", graph.getId());
        Visualised.put("nodes", finalGraphNodes);
        Visualised.put("edges", finalGraphEdges);


        return Visualised;
    }

}
