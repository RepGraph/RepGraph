package com.RepGraph;

import java.util.*;

/**
 * The RepGraphModel class is used to store all the system's graphs and run analysis functions on graphs.
 */
public class MRSModel extends AbstractModel {


    public MRSModel() {
        super();
    }

    /**
     * Display Subset overloaded method to decide which type of subset to construct
     * and what type of visualisation the subset should be displayed in
     *
     * @param graphId    The ID of the MRSGraph where the subset is being constructed
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
     * Uses a MRSGraph ID and the number of a Node in the MRSGraph and returns a subset of the MRSGraph. The subset is all the adjacent nodes around the head Node id given
     *
     * @param graphID    The MRSGraph ID.
     * @param headNodeID The MRSGraph's Node which will be the head Node of the subset.
     * @return MRSGraph The subset of the MRSGraph.
     */
    public MRSGraph CreateSubsetAdjacent(String graphID, String headNodeID) {


        MRSGraph subset = new MRSGraph();
        MRSGraph parent = (MRSGraph) getGraph(graphID);
        parent.setNodeNeighbours();

        HashMap<String, Node> adjacentNodes = new HashMap<String, Node>();
        ArrayList<Edge> adjacentEdges = new ArrayList<>();
        ArrayList<Token> SubsetTokens = new ArrayList<>();

        //Get the head Node where the subset creation starts from
        MRSGraph t = (MRSGraph) graphs.get(graphID);
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

        //Use "getTokenSpan" to get all the tokens from the MRSGraph that are in the subsets span and add it to the subset MRSGraph object
        SubsetTokens.addAll(parent.getTokenSpan(minFrom, maxEnd));
        //Set all the details of the subset MRSGraph object.
        subset.setId(parent.getId());
        subset.setSource(parent.getSource());
        subset.setNodes(adjacentNodes);
        subset.setEdges(adjacentEdges);
        subset.setTokens(SubsetTokens);
        subset.setInput(parent.getTokenInput(SubsetTokens));
        subset.setTopString(parent.getTop());

        return subset;
    }

    /**
     * Uses a MRSGraph ID and the number of a Node in the MRSGraph and returns a subset of the MRSGraph. The subset is all the descendent nodes from the head Node id given
     *
     * @param graphID    The MRSGraph ID.
     * @param headNodeID The MRSGraph's Node which will be the head Node of the subset.
     * @return MRSGraph The subset of the MRSGraph.
     */
    public MRSGraph CreateSubsetDescendent(String graphID, String headNodeID) {

        MRSGraph subset = new MRSGraph();
        MRSGraph parent = (MRSGraph) getGraph(graphID);
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
        subset.setTopString(parent.getTop());

        return subset;
    }

    /**
     * Overloaded method to search for subgraph pattern using different parameters
     *
     * @param graphID     ID of MRSGraph that contains the selected pattern
     * @param NodeId      Int array of Node IDs of the pattern selected
     * @param EdgeIndices int array of the Edge indices of the pattern selected
     * @return HashMap<String, Object> Returns a hashmap of information
     * i.e the "data" key contains a list of hashmaps that contain the MRSGraph ID's and Inputs of graphs that contain the subgraph pattern.
     * the "Response" key contains an error response if necessary.
     */
    public HashMap<String, Object> searchSubgraphPattern(String graphID, String[] NodeId, int[] EdgeIndices) {
        MRSGraph parent = (MRSGraph) graphs.get(graphID);
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

        MRSGraph subgraph = new MRSGraph();


        subgraph.setNodes(subnodes);
        subgraph.setEdges(subedges);

        return searchSubgraphPattern(subgraph);

    }

    public HashMap<String, Object> searchSubgraphPattern(AbstractGraph Asubgraph) {

        MRSGraph subgraph = (MRSGraph) Asubgraph;

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


        // Hashmap with a string key and boolean value - this is used to confirm that each unique Edge in the subgraph has been found in the MRSGraph being checked.
        //This is necessary because there can be multiple of the same two Node links so a counter or an array cant be used.
        HashMap<String, Boolean> checks = new HashMap<>();
        //This is just used to easily check that all the required edges in the subgraph are true because all values in this array need to be true before a MRSGraph is added
        //to foundgraphs arraylist.
        boolean[] checksarr = new boolean[subgraph.getEdges().size()];
        try {
            subgraph.setNodeNeighbours();
        } catch (IndexOutOfBoundsException e) {
            returninfo.put("response", "Failure");
            return returninfo;

        }


        //Iterate over each MRSGraph in the dataset

        for (AbstractGraph t : graphs.values()) {
            MRSGraph g = (MRSGraph) t;
            //If it cant set the Node neighbours then just the specific MRSGraph iteration.
            try {
                g.setNodeNeighbours();
            } catch (IndexOutOfBoundsException f) {
                continue;
            }

            //Iterate over each Node in the subgraph patttern
            for (Node sn : subgraph.getNodes().values()) {
                //for each Node in the subgraph pattern, it iterates over every Node in the current MRSGraph that is being checked.
                //This is to find a Node label equal to the current sub MRSGraph Node being checked.
                for (Node n : g.getNodes().values()) {
                    //This checks if the labels are equal
                    if (n.getLabel().equals(sn.getLabel())) {
                        //Once it finds a Node in the MRSGraph that is the same as the subgraph Node label it has to iterate over the subgraph edges list
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
            //This checks to see if all values in the boolean array are true and if so adds the current MRSGraph to the list of found graphs.
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
     * * i.e the "data" key contains a list of hashmaps that contain the MRSGraph ID's and Inputs of graphs that have the set of Node labels.
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
        //this would allow to avoid using a for loop inside MRSGraph iteration
        boolean[] checks = new boolean[labels.size()];

        for (AbstractGraph t : graphs.values()) {
            MRSGraph g = (MRSGraph) t;
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
            //resets the checks array when its done checking a MRSGraph
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
     * @param graphID1 MRSGraph ID of the first MRSGraph.
     * @param graphID2 MRSGraph ID of the second MRSGraph.
     * @return HashMap<String, Object> The differences and similarities of the two graphs i.e
     * the "SimilarNodes1" key gives the Node ids of the similar nodes in graph1.
     * the "SimilarNodes2" key gives the Node ids of the similar nodes in graph2.
     * the "SimilarEdges1" key gives the Node ids of the similar edges in graph1.
     * the "SimilarEdge2" key gives the Node ids of the similar edges in graph2.
     */
    public HashMap<String, Object> compareTwoGraphs(String graphID1, String graphID2) {
        MRSGraph g1 = (MRSGraph) graphs.get(graphID1);
        MRSGraph g2 = (MRSGraph) graphs.get(graphID2);
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

        //iterates over every Node of each MRSGraph
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
     * Runs formal tests on a MRSGraph.
     *
     * @param graphID               The MRSGraph ID which the tests will be run.
     * @param planar                Boolean to decide if to test for if the MRSGraph is planar.
     * @param longestPathDirected   Boolean to decide if to find the longest directed path.
     * @param longestPathUndirected Boolean to decide if to find the longest undirected path.
     * @param connected             Boolean to decide if to test for if the MRSGraph is connected.
     * @return HashMap<String, Object> Results of the tests i.e
     * the "Planar" key returns a boolean of whether or not the MRSGraph is planar
     * the "PlanarVis" key returns the visualisation data
     * the "LongestPathDirected" key returns an ArrayList of an Arraylist of integers defining the multiple longest directed paths in the graphs
     * the "LongestPathUndirected" key returns an ArrayList of an Arraylist of integers defining the multiple longest undirected paths in the graphs
     * the "Connected" returns a boolean of whether or not the MRSGraph is connected.
     */
    public HashMap<String, Object> runFormalTests(String graphID, boolean planar, boolean longestPathDirected, boolean longestPathUndirected, boolean connected) {
        HashMap<String, Object> returnObj = new HashMap<>();
        MRSGraph g = (MRSGraph) graphs.get(graphID);
        if (planar) {

            returnObj.put("Planar", g.isPlanar());
            returnObj.put("PlanarVis", VisualisePlanar(graphs.get(graphID)));
        }
        if (longestPathDirected) {
            //checks if graphs are cyclic, if so returns a message indicating the MRSGraph is cyclic otherwise sends back the longest path directed information
            if (graphs.get(graphID).isCyclic(true)) {
                returnObj.put("LongestPathDirected", "Cycle Detected");
            } else {
                returnObj.put("LongestPathDirected", graphs.get(graphID).findLongest(true));
            }
        }
        //checks if graphs are cyclic, if so returns a message indicating the MRSGraph is cyclic otherwise sends back the longest path directed information
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
     * @param graphID This is the graphID of the MRSGraph to be visualised
     * @param format  this is the format of the visualisation i.e
     *                format 1 - hierarchical,
     *                format 2 - tree like,
     *                format 3 - flat,
     *                format 4 - planar.
     * @return HashMap<String, Object> This is the visualisation information that is used to display the visualisation on the front-end
     */
    public HashMap<String, Object> Visualise(String graphID, int format) {
        MRSGraph graph = (MRSGraph) graphs.get(graphID);
        graph.populateTokens();
        graph.populateEdges();

        if (format == 1) {
            //return VisualiseHierarchy(graph);
        } else if (format == 2) {
            //return VisualiseTree(graph);
        } else if (format == 3) {
            return VisualiseFlat(graph);
        } else if (format == 4) {
            //return VisualisePlanar(graph);
        }
        return null;
    }

    public HashMap<String, Object> VisualisePlanar(AbstractGraph Agraph) {

        //gets the planar optimised MRSGraph
        MRSGraph Dgraph = (MRSGraph) Agraph;
        MRSGraph graph = Dgraph.PlanarGraph();

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

            if (Integer.parseInt(fromID) > Integer.parseInt(toID)) {
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
     * The visualisation method for visualising a Flat MRSGraph.
     *
     * @param graph the MRSGraph object to be visualised in a Flat format
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


            fromID = e.getSource();


            toID = e.getTarget();


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
        return null;
    }

    ;

    public HashMap<String, Object> VisualiseHierarchy(AbstractGraph Agraph) {
        return null;
    }

    ;

}
