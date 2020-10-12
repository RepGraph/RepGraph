/**
 * The RepGraphModel class is used to store all the system's graphs and run analysis functions on graphs.
 *
 * @since 29/08/2020
 */

package com.RepGraph;

import java.util.*;

public class RepGraphModel {

    /**
     * A hashmap of the model's graphs that maps graph IDs to their graph objects.
     */
    private HashMap<String, graph> graphs;

    /**
     * Default constructor for the model class.
     */
    public RepGraphModel() {
        graphs = new HashMap<String, graph>();
    }

    /**
     * Getter method for a graph given the graph's ID.
     *
     * @param graphID A graph's ID.
     * @return graph The requested graph.
     */
    public graph getGraph(String graphID) {
        return graphs.get(graphID);
    }

    /**
     * Adds graph to hashmap.
     *
     * @param value This is the graph object that is added to the graphs hashmap
     */
    public void addGraph(graph value) {
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

    /**
     * Display Subset overloaded method to decide which type of subset to construct
     * and what type of visualisation the subset should be displayed in
     *
     * @param graphId    The ID of the graph where the subset is being constructed
     * @param headNodeID The node ID of the starting node of subset creation
     * @param SubsetType The type of subset to be created
     * @param format The format of the visualisation i.e 1 - hierarchical format, 2- tree like format, 3 - flat visualisation, 4 - planar visualisation
     * @return HashMap<String, Object> The Visualisation Data of the subset
     */
    public HashMap<String, Object> DisplaySubset(String graphId, int headNodeID, String SubsetType, int format) {
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
     * Uses a graph ID and the number of a node in the graph and returns a subset of the graph. The subset is all the adjacent nodes around the head node id given
     *
     * @param graphID    The graph ID.
     * @param headNodeID The graph's node which will be the head node of the subset.
     * @return graph The subset of the graph.
     */
    public graph CreateSubsetAdjacent(String graphID, int headNodeID) {

        graph subset = new graph();
        HashMap<Integer, node> adjacentNodes = new HashMap<Integer, node>();
        ArrayList<edge> adjacentEdges = new ArrayList<>();

        graph parent = getGraph(graphID);
        parent.setNodeNeighbours();

        ArrayList<token> SubsetTokens = new ArrayList<>();


        node n = graphs.get(graphID).getNodes().get(headNodeID);

        adjacentNodes.put(n.getId(), new node(n));
        int minFrom = n.getAnchors().get(0).getFrom();
        int maxEnd = n.getAnchors().get(0).getEnd();
        ArrayList<node> nodeNeighbours = new ArrayList<>(n.getDirectedNeighbours());
        ArrayList<edge> edgeNeighbours = new ArrayList<>(n.getDirectedEdgeNeighbours());

        nodeNeighbours.addAll(n.getUndirectedNeighbours());
        edgeNeighbours.addAll(n.getUndirectedEdgeNeighbours());
        for (node nn : nodeNeighbours) {
            adjacentNodes.put(nn.getId(), new node(nn));
            if (nn.getAnchors().get(0).getFrom() < minFrom) {
                minFrom = nn.getAnchors().get(0).getFrom();
            }
            if (nn.getAnchors().get(0).getEnd() > maxEnd) {
                maxEnd = nn.getAnchors().get(0).getEnd();
            }


        }
        for (edge ne : edgeNeighbours) {
            adjacentEdges.add(new edge(ne));
        }
        SubsetTokens.addAll(parent.getTokenSpan(minFrom, maxEnd));


        subset.setId(parent.getId());
        subset.setSource(parent.getSource());
        subset.setNodes(adjacentNodes);
        subset.setEdges(adjacentEdges);
        subset.setTokens(SubsetTokens);
        subset.setInput(parent.getTokenInput(SubsetTokens));
        subset.setTops(parent.getTops());


        return subset;
    }

    /**
     * Uses a graph ID and the number of a node in the graph and returns a subset of the graph. The subset is all the descendent nodes from the head node id given
     *
     * @param graphID    The graph ID.
     * @param headNodeID The graph's node which will be the head node of the subset.
     * @return graph The subset of the graph.
     */
    public graph CreateSubsetDescendent(String graphID, int headNodeID) {

        graph subset = new graph();

        HashMap<Integer, node> DescendentNodes = new HashMap<Integer, node>();
        ArrayList<edge> DescendentEdges = new ArrayList<>();
        ArrayList<token> SubsetTokens = new ArrayList<>();

        graph parent = getGraph(graphID);
        parent.setNodeNeighbours();


// THE WHOLE DISCOVER NODES METHOD MIGHT BE REDUNDANT.
        node n = parent.getNodes().get(headNodeID);

        DescendentNodes.put(n.getId(), new node(n));
        for (edge e : n.getDirectedEdgeNeighbours()) {
            DescendentEdges.add(new edge(e));
        }


        int minFrom = n.getAnchors().get(0).getFrom();
        int maxEnd = n.getAnchors().get(0).getEnd();

        HashMap<Integer, node> descNode = new HashMap<>();
        HashMap<String, edge> descEdge = new HashMap<>();

        DiscoverNodesAndEdges(n, descNode, descEdge);

        DescendentNodes.putAll(descNode);

        DescendentEdges.addAll(descEdge.values());

        for (node nn : DescendentNodes.values()) {
            if (nn.getAnchors().get(0).getFrom() < minFrom) {
                minFrom = nn.getAnchors().get(0).getFrom();
            }
            if (nn.getAnchors().get(0).getEnd() > maxEnd) {
                maxEnd = nn.getAnchors().get(0).getEnd();
            }
        }

        SubsetTokens.addAll(parent.getTokenSpan(minFrom, maxEnd));


        subset.setId(parent.getId());
        subset.setSource(parent.getSource());
        subset.setNodes(DescendentNodes);
        subset.setEdges(DescendentEdges);
        subset.setTokens(SubsetTokens);
        subset.setInput(parent.getTokenInput(SubsetTokens));
        subset.setTops(parent.getTops());

        return subset;
    }

    /**
     * Iteratively goes down from a root node and finds all descendents and edges from that nodes and puts them in a hashmap
     *
     * @param root             This is the root node where the algorithm starts looking for all descendants from.
     * @param descendants      This is the HashMap input that is populated with descendant nodes
     *                         i.e the data is returned in the object inputted as the parameter
     * @param descendantsEdges This is the HashMap input that is populated with descendant edges
     *                         i.e the data is returned in the object inputted as the parameter
     */
    public void DiscoverNodesAndEdges(node root, HashMap<Integer, node> descendants, HashMap<String, edge> descendantsEdges) {

        if (root == null)
            return;

        Stack<node> s = new Stack<>();
        node curr = root;

        // traverse the tree
        while (curr != null || s.size() > 0) {

            for (node n : curr.getDirectedNeighbours()) {
                s.push(n);
                descendants.put(n.getId(), new node(n));

                for (edge e : n.getDirectedEdgeNeighbours()) {

                    descendantsEdges.put(e.getSource() + " " + e.getTarget(), new edge(e));
                }
            }

            try {
                curr = s.pop();
            } catch (Exception e) {
                curr = null;
            }

        }


    }

    /**
     * Overloaded method to search for subgraph pattern using different parameters
     *
     * @param graphID     ID of graph that contains the selected pattern
     * @param NodeId      Int array of node IDs of the pattern selected
     * @param EdgeIndices int array of the edge indices of the pattern selected
     * @return HashMap<String, Object> Returns a hashmap of information
     * i.e the "data" key contains a list of hashmaps that contain the graph ID's and Inputs of graphs that contain the subgraph pattern.
     * the "Response" key contains an error response if necessary.
     */
    public HashMap<String, Object> searchSubgraphPattern(String graphID, int[] NodeId, int[] EdgeIndices) {
        graph parent = graphs.get(graphID);
        HashMap<Integer, node> subnodes = new HashMap<Integer, node>();
        ArrayList<edge> subedges = new ArrayList<>();

        for (int n : NodeId) {
            subnodes.put(n, new node(parent.getNodes().get(n)));
        }
        for (int n : EdgeIndices) {
            subedges.add(new edge(parent.getEdges().get(n)));
        }
        graph subgraph = new graph();

/*
        for (int i: subnodes.keySet()) {
            for (edge e : subedges) {
                if (e.getSource() == subnodes.get(i).getId()) {
                    e.setSource(i);

                }
                if (e.getTarget() == subnodes.get(i).getId()) {
                    e.setTarget(i);

                }
            }
            subnodes.get(i).setId(i);
        }
*/

        subgraph.setNodes(subnodes);
        subgraph.setEdges(subedges);

        return searchSubgraphPattern(subgraph);

    }

    /**
     * This method searches through the graph objects in the hashmap to find graphs that contain a
     * subgraph pattern.
     *
     * @param subgraph This is the subgraph pattern graph object that is searched for. It needs to be a connected graph.
     *
     * @return HashMap<String, Object> Returns a hashmap of information
     *      * i.e the "data" key contains a list of hashmaps that contain the graph ID's and Inputs of graphs that contain the subgraph pattern.
     *      * the "Response" key contains an error response if necessary.
     */
    public HashMap<String, Object> searchSubgraphPattern(graph subgraph) {
        HashMap<String, Object> returninfo = new HashMap<>();

        //subgraph pattern must be connected

        //Empty Arraylist for the graph ids that have the subgraph pattern specified.
        ArrayList<HashMap<String, String>> FoundGraphs = new ArrayList<>();
        //If no nodes there is no pattern, if there are no edges it is not connected,
        // and if there are less edges than number of nodes -1 then it is not connected,
        // if it has a dangling edge it isnt connected
        if (subgraph.getNodes().size() == 0 || subgraph.getEdges().size() == 0 || subgraph.getEdges().size() < subgraph.getNodes().size() - 1 || subgraph.hasDanglingEdge() || !subgraph.connectedBFS(subgraph.getNodes().values().iterator().next().getId())) {
            returninfo.put("response", "Subgraph Pattern was not entered correctly");
            return returninfo;
        }


        // Hashmap with a string key and boolean value - this is used to confirm that each unique edge in the subgraph has been found in the graph being checked.
        //This is necessary because there can be multiple of the same two node links so a counter or an array cant be used.
        HashMap<String, Boolean> checks = new HashMap<>();
        //This is just used to easily check that all the required edges in the subgraph are true because all values in this array need to be true before a graph is added
        //to foundgraphs arraylist.
        boolean[] checksarr = new boolean[subgraph.getEdges().size()];
        try {
            subgraph.setNodeNeighbours();
        } catch (IndexOutOfBoundsException e) {
            returninfo.put("response", "An Error has occurred");
            return returninfo;

        }


        //Iterate over each graph in the dataset
        for (graph g : graphs.values()) {
            try {
                g.setNodeNeighbours();
            } catch (IndexOutOfBoundsException f) {
                continue;
            }

            //Iterate over each node in the subgraph patttern
            for (node sn : subgraph.getNodes().values()) {
                //for each node in the subgraph pattern, it iterates over every node in the current graph that is being checked.
                //This is to find a node label equal to the current sub graph node being checked.
                for (node n : g.getNodes().values()) {
                    //This checks if the labels are equal
                    if (n.getLabel().equals(sn.getLabel())) {
                        //Once it finds a node in the graph that is the same as the subgraph node label it has to iterate over the subgraph edges list
                        //to find the corresponding edge of the subgraph node that found a match.

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
            //This for loop checks if the hashmap has a key for the unique edge entry and checks if its true. if it is true then it sets
            //the corresponding index in the boolean array to true;
            for (int i = 0; i < subgraph.getEdges().size(); i++) {
                if (checks.containsKey(subgraph.getEdges().get(i).getSource() + subgraph.getEdges().get(i).getTarget() + "") && checks.get(subgraph.getEdges().get(i).getSource() + subgraph.getEdges().get(i).getTarget() + "") == true) {
                    checksarr[i] = true;

                }
            }
            //This checks to see if all values in the boolean array are true and if so adds the current graph to the list of found graphs.
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
     * Searches through all the graphs to find graphs that contain all the node labels provided.
     * @param labels This is the list of node labels to search for.
     * @return HashMap<String, Object> Returns a hashmap of information
     *      * i.e the "data" key contains a list of hashmaps that contain the graph ID's and Inputs of graphs that have the set of node labels.
     *      * the "Response" key contains an error response if necessary.
     */
    public HashMap<String, Object> searchSubgraphNodeSet(ArrayList<String> labels) {
        HashMap<String, Object> returninfo = new HashMap<>();


        ArrayList<HashMap<String, String>> FoundGraphs = new ArrayList<>();
        if (labels.size() == 0) {
            returninfo.put("response", "Subgraph Pattern was not entered correctly");
            return returninfo;
        }


        //***********************************************************
        //boolean array that checks if certain nodes are found
        //could use a hashmap with the label as a key and boolean as value.
        //this would allow to avoid using a for loop inside graph iteration
        boolean[] checks = new boolean[labels.size()];

        for (graph g : graphs.values()) {
            HashMap<Integer, node> tempNodes = new HashMap<Integer, node>(g.getNodes());
            for (int i = 0; i < labels.size(); i++) {
                for (node n : tempNodes.values()) {
                    if (n.getLabel().equals(labels.get(i))) {
                        checks[i] = true;
                        //removes node so that it wont be checked again in case two or more of the same labels are required in the set
                        tempNodes.remove(n.getId());
                        break;
                    }
                }
            }

            //checks if all node labels have been found
            if (areAllTrue(checks)) {
                HashMap<String, String> found = new HashMap<String, String>();
                found.put("id", g.getId());
                found.put("input", g.getInput());
                FoundGraphs.add(found);

            }
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
     * @param graphID1 Graph ID of the first graph.
     * @param graphID2 Graph ID of the second graph.
     * @return HashMap<String, Object> The differences and similarities of the two graphs i.e
     * the "SimilarNodes1" key gives the node ids of the similar nodes in graph1.
     * the "SimilarNodes2" key gives the node ids of the similar nodes in graph2.
     * the "SimilarEdges1" key gives the node ids of the similar edges in graph1.
     * the "SimilarEdge2" key gives the node ids of the similar edges in graph2.
     */
    public HashMap<String, Object> compareTwoGraphs(String graphID1, String graphID2) {

        HashMap<Integer, node> nodes1 = graphs.get(graphID1).getNodes();
        HashMap<Integer, node> nodes2 = (graphs.get(graphID2).getNodes());
        ArrayList<edge> edges1 = graphs.get(graphID1).getEdges();
        ArrayList<edge> edges2 = (graphs.get(graphID2).getEdges());


        ArrayList<Integer> similarNodes1 = new ArrayList<>();
        ArrayList<Integer> similarNodes2 = new ArrayList<>();
        ArrayList<Integer> similarEdges1 = new ArrayList<>();
        ArrayList<Integer> similarEdges2 = new ArrayList<>();


        graphs.get(graphID1).setNodeNeighbours();
        graphs.get(graphID2).setNodeNeighbours();


        for (node n1 : nodes1.values()) {
            for (node n2 : nodes2.values()) {
                int span1 = n1.getAnchors().get(0).getEnd() - n1.getAnchors().get(0).getFrom();
                int span2 = n2.getAnchors().get(0).getEnd() - n2.getAnchors().get(0).getFrom();

                if (n1.getLabel().equals(n2.getLabel()) && span1 == span2) {
                    if (!similarNodes1.contains(n1.getId())) {
                        similarNodes1.add(n1.getId());
                    }
                    if (!similarNodes2.contains(n2.getId())) {
                        similarNodes2.add(n2.getId());
                    }

                    for (edge e1 : n1.getDirectedEdgeNeighbours()) {
                        for (edge e2 : n2.getDirectedEdgeNeighbours()) {

                            node nn1 = nodes1.get(e1.getTarget());
                            node nn2 = nodes2.get(e2.getTarget());
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
     * Runs formal tests on a graph.
     *
     * @param graphID               The graph ID which the tests will be run.
     * @param planar                Boolean to decide if to test for if the graph is planar.
     * @param longestPathDirected   Boolean to decide if to find the longest directed path.
     * @param longestPathUndirected Boolean to decide if to find the longest undirected path.
     * @param connected             Boolean to decide if to test for if the graph is connected.
     * @return HashMap<String, Object> Results of the tests i.e
     * the "Planar" key returns a boolean of whether or not the graph is planar
     * the "LongestPathDirected" key returns an ArrayList of an Arraylist of integers defining the multiple longest directed paths in the graphs
     * the "LongestPathUndirected" key returns an ArrayList of an Arraylist of integers defining the multiple longest undirected paths in the graphs
     * the "Connected" returns a boolean of whether or not the graph is connected.
     */
    public HashMap<String, Object> runFormalTests(String graphID, boolean planar, boolean longestPathDirected, boolean longestPathUndirected, boolean connected) {
        HashMap<String, Object> returnObj = new HashMap<>();
        if (planar) {
            returnObj.put("Planar", graphs.get(graphID).GraphIsPlanar());
        }
        if (longestPathDirected) {
            if (graphs.get(graphID).isCyclic(true)) {
                returnObj.put("LongestPathDirected", "Cycle Detected");
            } else {
                returnObj.put("LongestPathDirected", graphs.get(graphID).findLongest(true));
            }
        }
        if (longestPathUndirected) {
            if (graphs.get(graphID).isCyclic(false)) {
                returnObj.put("LongestPathUndirected", "Cycle Detected");
            } else {
                returnObj.put("LongestPathUndirected", graphs.get(graphID).findLongest(false));
            }
        }
        if (connected) {
            returnObj.put("Connected", graphs.get(graphID).connectedBFS(graphs.get(graphID).getNodes().values().iterator().next().getId()));
        }
        return returnObj;
    }

    /**
     * This method returns visualisation information so that it can be visualised on the front-end
     * @param graphID This is the graphID of the graph to be visualised
     * @param format this is the format of the visualisation i.e
     *               format 1 - hierarchical,
     *               format 2 - tree like,
     *               format 3 - flat,
     *               format 4 - planar.
     * @return HashMap<String, Object> This is the visualisation information that is used to display the visualisation on the front-end
     *
     */
    public HashMap<String, Object> Visualise(String graphID, int format) {
        graph graph = graphs.get(graphID);
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

    /**
     * The visualisation method for visualising a planar graph.
     * @param graph the graph object to be visualised in a planar format
     * @return HashMap<String, Object> This is the visualisation information that is used to display the visualisation on the front-end
     */
    public HashMap<String, Object> VisualisePlanar(graph graph) {


        graph = graph.PlanarGraph();

        ArrayList<edge> crossingEdges = new ArrayList<>();

        for (edge e : graph.getEdges()) {

            for (edge other : graph.getEdges()) {

                if (Math.min(e.getSource(), e.getTarget()) < Math.min(other.getSource(), other.getTarget()) && Math.min(other.getSource(), other.getTarget()) < Math.max(e.getSource(), e.getTarget()) && Math.max(e.getSource(), e.getTarget()) < Math.max(other.getSource(), other.getTarget())) {

                    crossingEdges.add(e);
                    crossingEdges.add(other);
                }

            }
        }

        int height = 1;
        int totalGraphHeight = height * 50 + (height - 1) * 70; //number of levels times the height of each node and the spaces between them

        ArrayList<HashMap<String, Object>> finalNodes = new ArrayList<>();


        HashMap<Integer, Integer> currentLevels = new HashMap<>();
        for (node n : graph.getNodes().values()) {
            if (!currentLevels.containsKey(n.getAnchors().get(0).getFrom())) {
                currentLevels.put(n.getAnchors().get(0).getFrom(), 0);
            } else {
                currentLevels.put(n.getAnchors().get(0).getFrom(), currentLevels.get(n.getAnchors().get(0).getFrom()) + 1);
            }

            HashMap<String, Object> singleNode = new HashMap<>();
            singleNode.put("id", n.getAnchors().get(0).getFrom() + currentLevels.get(n.getAnchors().get(0).getFrom()) * graph.getNodes().size());
            singleNode.put("x", n.getAnchors().get(0).getFrom() * 110);
            singleNode.put("y", totalGraphHeight + currentLevels.get(n.getAnchors().get(0).getFrom()) * 100);
            singleNode.put("label", n.getLabel());
            singleNode.put("type", "node");
            singleNode.put("anchors", n.getAnchors().get(0));
            singleNode.put("group", "node");
            singleNode.put("fixed", true);
            finalNodes.add(singleNode);


        }


        ArrayList<HashMap<String, Object>> finalGraphEdges = new ArrayList<>();
        int fromID = 0, toID = 0;

        for (int i = 0; i < graph.getEdges().size(); i++) {
            HashMap<String, Object> singleEdge = new HashMap<>();
            edge e = graph.getEdges().get(i);
            anchors fromNode = null;
            anchors toNode = null;
            for (HashMap<String, Object> node : finalNodes) {

                if ((Integer) node.get("id") == e.getSource()) {
                    fromID = e.getSource();
                    fromNode = (anchors) node.get("anchors");

                }
                if ((Integer) node.get("id") == e.getTarget()) {
                    toID = e.getTarget();
                    toNode = (anchors) node.get("anchors");
                }
            }
            String edgeType = "";

            if (fromID > toID) {
                edgeType = "curvedCCW";
            } else {
                edgeType = "curvedCW";
            }
            if (fromID == toID || fromNode.getFrom() == toNode.getFrom()) {
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
     * The visualisation method for visualising a Flat graph.
     * @param graph the graph object to be visualised in a Flat format
     * @return HashMap<String, Object> This is the visualisation information that is used to display the visualisation on the front-end
     */
    public HashMap<String, Object> VisualiseFlat(graph graph) {


        int height = 1;
        int totalGraphHeight = height * 50 + (height - 1) * 70; //number of levels times the height of each node and the spaces between them

        ArrayList<HashMap<String, Object>> finalNodes = new ArrayList<>();


        for (int i : graph.getNodes().keySet()) {
            node n = graph.getNodes().get(i);
            HashMap<String, Object> singleNode = new HashMap<>();
            singleNode.put("id", n.getId());
            singleNode.put("x", i * 130);
            singleNode.put("y", totalGraphHeight);
            singleNode.put("label", n.getLabel());
            singleNode.put("type", "node");
            singleNode.put("anchors", n.getAnchors().get(0));
            singleNode.put("group", "node");
            singleNode.put("fixed", true);
            finalNodes.add(singleNode);
        }
        if (graph.getNodes().containsKey(graph.getTops().get(0))) {
        HashMap<String, Object> singleNode = new HashMap<>();
            singleNode.put("id", "TOP");
        singleNode.put("x", graph.getTops().get(0) * 130);
        singleNode.put("y", totalGraphHeight - 150);
        singleNode.put("label", "TOP");
        singleNode.put("type", "node");
        singleNode.put("group", "token");
        singleNode.put("fixed", true);

            finalNodes.add(singleNode);
        }


        ArrayList<HashMap<String, Object>> finalGraphEdges = new ArrayList<>();
        int fromID = 0, toID = 0;

        for (edge e : graph.getEdges()) {
            HashMap<String, Object> singleEdge = new HashMap<>();
            for (HashMap<String, Object> node : finalNodes) {
                if (!node.get("id").equals((String) "TOP")) {
                if ((Integer) node.get("id") == e.getSource()) {
                    fromID = e.getSource();


                }
                if ((Integer) node.get("id") == e.getTarget()) {
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

        if (graph.getNodes().containsKey(graph.getTops().get(0))) {
            singleEdge.put("id", graph.getEdges().size());
            singleEdge.put("from", "TOP");
            singleEdge.put("to", graph.getTops().get(0));
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
            finalGraphEdges.add(singleEdge);
        }

        HashMap<String, Object> Visualised = new HashMap<>();
        Visualised.put("id", graph.getId());
        Visualised.put("nodes", finalNodes);
        Visualised.put("edges", finalGraphEdges);

        return Visualised;

    }

    /**
     * The visualisation method for visualising a Tree like graph.
     * @param graph the graph object to be visualised in a Tree like format
     * @return HashMap<String, Object> This is the visualisation information that is used to display the visualisation on the front-end
     */
    public HashMap<String, Object> VisualiseTree(graph graph) {


        graph.setNodeNeighbours();
        HashMap<Integer, Stack<Integer>> topologicalStacks = new HashMap<>();

        for (int i : graph.getNodes().keySet()) {
            Stack<Integer> stack = new Stack<>();
            HashMap<Integer, Boolean> visited = new HashMap<>();
            for (int j : graph.getNodes().keySet()){
                visited.put(j, false);
            }
            topologicalStacks.put(i, graph.topologicalSort(i, visited, stack));
        }


        int numLevels = 0;
        for (int i : topologicalStacks.keySet()){
            numLevels = Math.max(numLevels, topologicalStacks.get(i).size());
        }


        ArrayList<ArrayList<node>> nodesInLevels = new ArrayList<>();

        for (int level = 1; level < numLevels + 1; level++) {
            ArrayList<node> currentLevel = new ArrayList<>();
            for (int n : graph.getNodes().keySet()) {
                if (topologicalStacks.get(n).size() == level) {
                    currentLevel.add(graph.getNodes().get(n));
                }
            }
            nodesInLevels.add(currentLevel);

        }

        HashMap<Integer, Integer> xPositions = new HashMap<>();
        HashMap<Integer, node> lowestNode = new HashMap<>();

        for (ArrayList<node> level : nodesInLevels) {
            for (node n : level) {
                int column = n.getAnchors().get(0).getFrom();
                xPositions.put(n.getId(), column);
                if (!lowestNode.containsKey(column)) {
                    lowestNode.put(column, n);
                }
            }
        }


        for (ArrayList<node> level : nodesInLevels) {
            for (node n : level) {
                if (lowestNode.get(xPositions.get(n.getId())) != n) {
                    if (n.getDirectedNeighbours().size() == 1) {
                        xPositions.put(n.getId(), xPositions.get(n.getDirectedNeighbours().get(0).getId()));
                    } else {
                        ArrayList<Integer> childrenInSpan = new ArrayList<>();
                        for (node neighbour : n.getDirectedNeighbours()) {
                            if (
                                    xPositions.get(neighbour.getId()) >= n.getAnchors().get(0).getFrom() &&
                                            xPositions.get(neighbour.getId()) <= n.getAnchors().get(0).getEnd()
                            ) {
                                childrenInSpan.add(neighbour.getId());
                            }
                        }
                        if (childrenInSpan.size() != 0) {
                            int leftMostChildPos = xPositions.get(childrenInSpan.get(0));
                            for (int child : childrenInSpan) {
                                leftMostChildPos = Math.min(
                                        leftMostChildPos,
                                        xPositions.get(child)
                                );
                            }
                            xPositions.put(n.getId(), leftMostChildPos);
                        }
                    }
                }
            }
        }

        HashMap<Integer, HashMap<Integer, node>> nodesInFinalLevels = new HashMap<>();

        nodesInFinalLevels.put(0, new HashMap<>());

        //Resolve overlapping nodes.
        int numNodesProcessed = 0;
        int currentLevel = 0;
        while (numNodesProcessed != graph.getNodes().size()) {
            HashMap<Integer, Integer> nodeXPos = new HashMap<>();
            nodesInFinalLevels.put(currentLevel + 1, new HashMap<>());
            for (node n : nodesInLevels.get(currentLevel)) {
                if (nodeXPos.containsKey(xPositions.get(n.getId()))) {
                    if (
                            topologicalStacks.get(n.getId()).size() <
                                    topologicalStacks.get(nodeXPos.get(xPositions.get(n.getId()))).size()
                    ) {
                        int currentOccupyingNodeID = nodeXPos.get(xPositions.get(n.getId()));
                        nodeXPos.put(xPositions.get(n.getId()), n.getId());
                        nodesInLevels.get(currentLevel + 1).add(
                                graph.getNodes().get(currentOccupyingNodeID)
                        );
                        nodesInFinalLevels.get(currentLevel).remove(currentOccupyingNodeID);
                        nodesInFinalLevels.get(currentLevel).put(n.getId(), n);
                    } else {
                        //nodesInLevels.add();
                        nodesInLevels.get(currentLevel + 1).add(n);
                    }
                } else {
                    nodeXPos.put(xPositions.get(n.getId()), n.getId());
                    nodesInFinalLevels.get(currentLevel).put(n.getId(), n);
                    numNodesProcessed++;
                }
            }
            currentLevel++;
        }

        int height = numLevels;

        int totalGraphHeight = height * 50 + (height - 1) * 70; //number of levels times the height of each node and the spaces between them


        ArrayList<HashMap<String, Object>> finalNodes = new ArrayList<>();

        int levelNum = -1;
        int maxID = graph.getNodes().keySet().iterator().next();
        for (HashMap<Integer, node> level : nodesInFinalLevels.values()) {
            levelNum++;
            for (node n : level.values()) {
                HashMap<String, Object> singleNode = new HashMap<>();
                singleNode.put("id", n.getId());
                maxID = Math.max(n.getId(), maxID);
                singleNode.put("x", xPositions.get(n.getId()) * 130);
                singleNode.put("y", totalGraphHeight - levelNum * (totalGraphHeight / height));
                singleNode.put("label", n.getLabel());
                singleNode.put("type", "node");
                singleNode.put("nodeLevel", levelNum);
                singleNode.put("anchors", n.getAnchors().get(0));
                singleNode.put("group", "node");
                singleNode.put("fixed", true);
                finalNodes.add(singleNode);
            }
        }


        ArrayList<HashMap<String, Object>> finalTokens = new ArrayList<>();

        for (token t : graph.getTokens()) {
            HashMap<String, Object> singleToken = new HashMap<>();
            singleToken.put("id", t.getIndex() + maxID+1);
            singleToken.put("x", t.getIndex() * 130);
            singleToken.put("y", totalGraphHeight + 200);
            singleToken.put("label", t.getForm());
            singleToken.put("type", "token");
            singleToken.put("group", "token");
            singleToken.put("fixed", true);
            finalTokens.add(singleToken);
        }


        ArrayList<HashMap<String, Object>> finalGraphEdges = new ArrayList<>();
        int fromID = 0, toID = 0, fromLevel = 0, toLevel = 0, fromX = 0, toX = 0;

        for (edge e : graph.getEdges()) {
            HashMap<String, Object> singleEdge = new HashMap<>();

            for (HashMap<String, Object> node : finalNodes) {

                if ((Integer) node.get("id") == e.getSource()) {
                    fromID = e.getSource();
                    fromLevel = (Integer) node.get("nodeLevel");
                    fromX = (Integer) node.get("x");
                }
                if ((Integer) node.get("id") == e.getTarget()) {
                    toID = e.getTarget();
                    toLevel = (Integer) node.get("nodeLevel");
                    toX = (Integer) node.get("x");
                }
            }
            String edgeType = "";

            double round = 0.5;

            if (fromX == toX) {
                if (fromLevel - toLevel == 1) {
                    edgeType = "continuous";
                } else {
                    boolean notFound = true;
                    for (HashMap<String, Object> node : finalNodes) {
                        if ((Integer) node.get("x") == fromX && (Integer) node.get("nodeLevel") > toLevel && (Integer) node.get("nodeLevel") < fromLevel) {
                            notFound = false;
                        }
                    }
                    if (
                            notFound
                    ) {
                        edgeType = "continuous";
                    } else {
                        if (fromLevel - toLevel > 3) {
                            round = 0.32;
                        }
                        edgeType = "curvedCCW";
                        for (int i = 0; i < graph.getNodes().get(fromID).getDirectedNeighbours().size(); i++) {
                            if (
                                    xPositions.get(fromID) -
                                            xPositions.get(graph.getNodes().get(fromID).getDirectedNeighbours().get(i).getId()) == 1
                            ) {
                                edgeType = "curvedCW";
                            }
                        }
                    }
                }
            } else {
                edgeType = "dynamic";
            }

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

        for (HashMap<String, Object> n : finalNodes) {
            String group = (String) n.get("group");
            if (group.equals("node")) {

                HashMap<String, Object> token = null;
                boolean Found = false;
                for (HashMap<String, Object> t : finalTokens) {

                    if (!connectedTokens.contains(t) && t.get("x").equals(n.get("x"))) {
                        Found = true;
                        token = t;
                    }
                }

                if (Found) {
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

    /**
     * The visualisation method for visualising a Hierarchical graph.
     * @param graph the graph object to be visualised in a Hierarchical format
     * @return HashMap<String, Object> This is the visualisation information that is used to display the visualisation on the front-end
     */
    public HashMap<String, Object> VisualiseHierarchy(graph graph) {


        //Determine span lengths of each node
        HashMap<Integer, Integer> graphNodeSpanLengths = new HashMap<>();
        for (node n : graph.getNodes().values()){
            int span = n.getAnchors().get(0).getEnd() - n.getAnchors().get(0).getFrom();
            graphNodeSpanLengths.put(n.getId(), span);
        }

        //Determine unique span lengths of all the node spans
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
        ArrayList<ArrayList<node>> nodesInLevels = new ArrayList<>();
        for (int level : uniqueSpanLengths) {
            ArrayList<node> currentLevel = new ArrayList<>();
            for (int spanIndex :graphNodeSpanLengths.keySet()) {
                if (graphNodeSpanLengths.get(spanIndex) == level) {
                    currentLevel.add(graph.getNodes().get(spanIndex));
                }
            }

            nodesInLevels.add(currentLevel);
        }
        //Find the nodes in each level with the same span and group them together
        //Find the unique spans in each level
        ArrayList<ArrayList<String>> uniqueSpansInLevels = new ArrayList<>();

        for (ArrayList<node> level : nodesInLevels) {

            ArrayList<String> uniqueSpans = new ArrayList<>(); //Stores the "stringified" objects

            HashMap<String, Boolean> spanMap = new HashMap<>();
            for (node node : level) {
                String span = node.getAnchors().get(0).getFrom() + " " + node.getAnchors().get(0).getEnd();
                if (!spanMap.containsKey(span)) {
                    spanMap.put(span, true); // set any value to Map
                    uniqueSpans.add(span);
                }
            }
            uniqueSpansInLevels.add(uniqueSpans);

        }

        ArrayList<ArrayList<ArrayList<node>>> newNodeLevels = new ArrayList<>();
        //Iterate through the unique spans in each level and group the same ones together
        for (int level = 0; level < nodesInLevels.size(); level++) {
            ArrayList<ArrayList<node>> newLevelOfGroups = new ArrayList<>();
            for (String uniqueSpan : uniqueSpansInLevels.get(level)) {
                //find the nodes in the level that have the same span and group them together
                ArrayList<node> nodesWithCurrentSpan = new ArrayList<>();
                for (node n : nodesInLevels.get(level)) {
                    String span = n.getAnchors().get(0).getFrom() + " " + n.getAnchors().get(0).getEnd();
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
        for (ArrayList<ArrayList<node>> level : newNodeLevels) {
            int maxLevelHeight = 0;
            for (ArrayList<node> item : level) {
                maxLevelHeight = Math.max(maxLevelHeight, item.size());
            }
            previousLevelHeights.add(maxLevelHeight);
            height += maxLevelHeight;
        }


        //Sort the nodes into the final levels
        ArrayList<ArrayList<node>> nodesInFinalLevels = new ArrayList<>();
        for (int index = 0; index < height; index++) {
            nodesInFinalLevels.add(new ArrayList<node>());
        }
        for (int level = 0; level < newNodeLevels.size(); level++) {
            for (ArrayList<node> group : newNodeLevels.get(level)) {
                //console.log({group});
                for (
                        int nodeGroupIndex = 0;
                        nodeGroupIndex < group.size();
                        nodeGroupIndex++
                ) {
                    //console.log(group[nodeGroupIndex]);
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

        int totalGraphHeight = height * 50 + (height - 1) * 70; //number of levels times the height of each node and the spaces between them
        int space = 130;

        ArrayList<HashMap<String, Object>> finalNodes = new ArrayList<>();

        for (int level = 0; level < nodesInFinalLevels.size(); level++) {

            for (node n : nodesInFinalLevels.get(level)) {
                HashMap<String, Object> singleNode = new HashMap<>();
                singleNode.put("id", n.getId());
                singleNode.put("x", n.getAnchors().get(0).getFrom() * space + (n.getAnchors().get(0).getEnd() * space - n.getAnchors().get(0).getFrom()*space)/2);
                singleNode.put("y", totalGraphHeight - level * (totalGraphHeight / height));
                singleNode.put("label", n.getLabel());
                singleNode.put("type", "node");
                singleNode.put("nodeLevel", level);
                singleNode.put("anchors", n.getAnchors().get(0));
                singleNode.put("group", "node");
                singleNode.put("fixed", true);
                finalNodes.add(singleNode);
            }
        }

        if (graph.getNodes().containsKey(graph.getTops().get(0))) {
            HashMap<String, Object> singleNode = new HashMap<>();
            singleNode.put("id", "TOP");
            singleNode.put("x", graph.getNodes().get(graph.getTops().get(0)).getAnchors().get(0).getFrom() * space);
            singleNode.put("y", totalGraphHeight - nodesInFinalLevels.size() * (totalGraphHeight / height));
            singleNode.put("label", "TOP");
            singleNode.put("type", "node");
            singleNode.put("nodeLevel", nodesInFinalLevels.size());
            singleNode.put("anchors", graph.getNodes().get(graph.getTops().get(0)).getAnchors().get(0));
            singleNode.put("group", "token");
            singleNode.put("fixed", true);
            finalNodes.add(singleNode);
        }

        ArrayList<HashMap<String, Object>> finalTokens = new ArrayList<>();

        for (token t : graph.getTokens()) {
            HashMap<String, Object> singleToken = new HashMap<>();
            singleToken.put("index", t.getIndex());
            singleToken.put("x", t.getIndex() * space);
            singleToken.put("y", totalGraphHeight + 200);
            singleToken.put("label", t.getForm());
            singleToken.put("type", "token");
            singleToken.put("group", "token");
            singleToken.put("fixed", true);
            finalTokens.add(singleToken);
        }


        ArrayList<HashMap<String, Object>> finalGraphNodes = new ArrayList<>();
        finalGraphNodes.addAll(finalNodes);
        finalGraphNodes.addAll(finalTokens);


        ArrayList<HashMap<String, Object>> finalGraphEdges = new ArrayList<>();
        int fromID = 0, toID = 0, fromLevel = 0, toLevel = 0, fromX = 0, toX = 0;

        graph.setNodeNeighbours();

        for (edge e : graph.getEdges()) {
            HashMap<String, Object> singleEdge = new HashMap<>();
            for (HashMap<String, Object> node : finalNodes) {
                if (!node.get("id").equals((String) "TOP")) {
                if ((Integer) node.get("id") == e.getSource()) {
                    fromID = e.getSource();
                    fromLevel = (Integer) node.get("nodeLevel");
                    fromX = (Integer) node.get("x");
                }
                if ((Integer) node.get("id") == e.getTarget()) {
                    toID = e.getTarget();
                    toLevel = (Integer) node.get("nodeLevel");
                    toX = (Integer) node.get("x");
                }
                }
            }

            String edgeType = "";
            double round = 0.45;

            if (fromX == toX) {
                if (Math.abs(fromLevel - toLevel) == 1) {
                    edgeType = "continuous";
                } else {
                    boolean notFound = true;
                    for (HashMap<String, Object> node : finalNodes) {
                        if ((Integer) node.get("x") == fromX && ((Integer) node.get("nodeLevel") > toLevel && (Integer) node.get("nodeLevel") < fromLevel) || ((Integer) node.get("nodeLevel") < toLevel && (Integer) node.get("nodeLevel") > fromLevel)) {
                            notFound = false;
                        }
                    }
                    if (notFound) {
                        edgeType = "continuous";
                    } else {
                        if (Math.abs(fromLevel - toLevel) > 3) {
                            round = 0.32;
                        }
                        edgeType = "curvedCCW";
                        if (fromLevel < toLevel){
                            edgeType = "curvedCW";
                        }
                        for (node neighbour : graph.getNodes().get(fromID).getDirectedNeighbours()) {
                            if (fromX / space - neighbour.getAnchors().get(0).getFrom() == 1) {
                                if (fromLevel < toLevel){
                                    edgeType = "curvedCCW";
                                }
                                else{
                                    edgeType = "curvedCW";
                                }
                            }
                        }
                        for (node neighbour : graph.getNodes().get(toID).getDirectedNeighbours()) {
                            if (fromX / space - neighbour.getAnchors().get(0).getFrom() == 1) {
                                if (fromLevel < toLevel){
                                    edgeType = "curvedCCW";
                                }
                                else{
                                    edgeType = "curvedCW";
                                }
                            }
                        }
                    }
                }
            }
            else{
                    if (fromLevel == toLevel) {
                        edgeType = "curvedCCW";
                        int difference = fromX / space - toX / space;
                        if (Math.abs(difference) > 4) {
                            round = 0.2;
                        }
                        if (Math.abs(difference) > 10) {
                            round = 0.1;
                        }
                        if (difference > 0 && fromLevel == 0) {
                            edgeType = "curvedCW";
                        }
                    } else {
                        edgeType = "dynamic";
                    }
                }


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

        if (graph.getNodes().containsKey(graph.getTops().get(0))) {
            HashMap<String, Object> singleEdge = new HashMap<>();
            singleEdge.put("id", graph.getEdges().size());
            singleEdge.put("from", "TOP");
            singleEdge.put("to", graph.getTops().get(0));
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
            finalGraphEdges.add(singleEdge);
        }

            HashMap<String, Object> Visualised = new HashMap<>();
        Visualised.put("id", graph.getId());
            Visualised.put("nodes", finalGraphNodes);
            Visualised.put("edges", finalGraphEdges);


            return Visualised;
        }

    }
