/**
 * The RepGraphModel class is used to store all the system's graphs and run analysis functions on graphs.
 *
 * @since 29/08/2020
 */

package com.RepGraph;


import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

public class RepGraphModel {

    /**
     * A hashmap of the model's graphs that maps graph IDs to their graph class.
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
     * @param value Graph object
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

    public graph DisplaySubset(String graphId, int headNodeID, String SubsetType) {
        if (SubsetType.equals("adjacent")) {
            return DisplaySubsetAdjacent(graphId, headNodeID);
        } else if (SubsetType.equals("descendent")) {
            return DisplaySubsetDescendent(graphId, headNodeID);
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
    public graph DisplaySubsetAdjacent(String graphID, int headNodeID) {

        graph subset = new graph();
        ArrayList<node> adjacentNodes = new ArrayList<>();
        ArrayList<edge> adjacentEdges = new ArrayList<>();

        graph parent = getGraph(graphID);
        parent.setNodeNeighbours();

        ArrayList<token> SubsetTokens = new ArrayList<>();

        //DONT NEED FOR LOOP IF NODE ID IS ALWAYS THE SAME AS INDEX
        for (node n : parent.getNodes()) {

            if (n.getId() == headNodeID) {
                adjacentNodes.add(n);
                int minFrom = n.getAnchors().get(0).getFrom();
                int maxEnd = n.getAnchors().get(0).getEnd();
                ArrayList<node> nodeNeighbours = new ArrayList<>(n.getDirectedNeighbours());
                ArrayList<edge> edgeNeighbours = new ArrayList<>(n.getDirectedEdgeNeighbours());

                nodeNeighbours.addAll(n.getUndirectedNeighbours());
                edgeNeighbours.addAll(n.getUndirectedEdgeNeighbours());
                for (node nn : nodeNeighbours) {
                    adjacentNodes.add(nn);
                    if (nn.getAnchors().get(0).getFrom() < minFrom) {
                        minFrom = nn.getAnchors().get(0).getFrom();
                    }
                    if (nn.getAnchors().get(0).getEnd() > maxEnd) {
                        maxEnd = nn.getAnchors().get(0).getEnd();
                    }


                }
                for (edge ne : edgeNeighbours) {
                    adjacentEdges.add(ne);
                }
                SubsetTokens.addAll(parent.getTokenSpan(minFrom, maxEnd));
            }
        }
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
    public graph DisplaySubsetDescendent(String graphID, int headNodeID) {

        graph subset = new graph();

        ArrayList<node> DescendentNodes = new ArrayList<>();
        ArrayList<edge> DescendentEdges = new ArrayList<>();
        ArrayList<token> SubsetTokens = new ArrayList<>();

        graph parent = getGraph(graphID);
        parent.setNodeNeighbours();


        //DONT NEED FOR LOOP IF NODE ID IS ALWAYS THE SAME AS INDEX
        for (node n : parent.getNodes()) {

            if (n.getId() == headNodeID) {
                DescendentNodes.add(n);
                DescendentEdges.addAll(n.getDirectedEdgeNeighbours());

                int minFrom = n.getAnchors().get(0).getFrom();
                int maxEnd = n.getAnchors().get(0).getEnd();

                HashMap<Integer, node> descNode = new HashMap<>();
                HashMap<String, edge> descEdge = new HashMap<>();

                DiscoverNodesAndEdges(n, descNode, descEdge);

                DescendentNodes.addAll(descNode.values());

                DescendentEdges.addAll(descEdge.values());

                for (node nn : DescendentNodes) {
                    if (nn.getAnchors().get(0).getFrom() < minFrom) {
                        minFrom = nn.getAnchors().get(0).getFrom();
                    }
                    if (nn.getAnchors().get(0).getEnd() > maxEnd) {
                        maxEnd = nn.getAnchors().get(0).getEnd();
                    }
                }

                SubsetTokens.addAll(parent.getTokenSpan(minFrom, maxEnd));
            }
        }

        subset.setId(parent.getId());
        subset.setSource(parent.getSource());
        subset.setNodes(DescendentNodes);
        subset.setEdges(DescendentEdges);
        subset.setTokens(SubsetTokens);
        subset.setInput(parent.getTokenInput(SubsetTokens));
        subset.setTops(parent.getTops());

        return subset;
    }

    public void DiscoverNodesAndEdges(node root, HashMap<Integer, node> descendants, HashMap<String, edge> descendantsEdges) {

        if (root == null)
            return;

        Stack<node> s = new Stack<>();
        node curr = root;

        // traverse the tree
        while (curr != null || s.size() > 0) {

            for (node n : curr.getDirectedNeighbours()) {
                s.push(n);
                descendants.put(n.getId(), n);

                for (edge e : n.getDirectedEdgeNeighbours()) {

                    descendantsEdges.put(e.getSource() + " " + e.getTarget(), e);
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
     * @return ArrayList Returns an arraylist of strings containing the graph IDs where the pattern was found
     */
    public ArrayList<String> searchSubgraphPattern(String graphID, int[] NodeId, int[] EdgeIndices) {
        graph parent = graphs.get(graphID);
        ArrayList<node> subnodes = new ArrayList<>();
        ArrayList<edge> subedges = new ArrayList<>();
        for (int n : NodeId) {
            subnodes.add(new node(parent.getNodes().get(n)));
        }
        for (int n : EdgeIndices) {
            subedges.add(new edge(parent.getEdges().get(n)));
        }
        graph subgraph = new graph();


        for (int i = 0; i < subnodes.size(); i++) {
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

        subgraph.setNodes(subnodes);
        subgraph.setEdges(subedges);

        return searchSubgraphPattern(subgraph);

    }

    /**
     * This method searches through the graph objects in the hashmap to find graphs that contain a
     * subgraph pattern.
     *
     * @return String A string of graph IDs who have matching subgraphs.
     */
    public ArrayList<String> searchSubgraphPattern(graph subgraph) {

        //subgraph pattern must be connected

        //Empty Arraylist for the graph ids that have the subgraph pattern specified.
        ArrayList<String> FoundGraphs = new ArrayList<String>();
        //If no nodes there is no pattern, if there are no edges it is not connected, and if there are less edges than number of nodes -1 then it is not connected
        if (subgraph.getNodes().size() == 0 || subgraph.getEdges().size() == 0 || subgraph.getEdges().size() < subgraph.getNodes().size() - 1) {
            return FoundGraphs;
        }

        //checks more advanced situations of graph not being connected
        if (!subgraph.connectedBFS()) {
            return FoundGraphs;
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
            return FoundGraphs;
        }

        //Iterate over each graph in the dataset
        for (graph g : graphs.values()) {
            try {
                g.setNodeNeighbours();
            } catch (IndexOutOfBoundsException f) {
                continue;
            }
            //Iterate over each node in the subgraph patttern
            for (node sn : subgraph.getNodes()) {
                //for each node in the subgraph pattern, it iterates over every node in the current graph that is being checked.
                //This is to find a node label equal to the current sub graph node being checked.
                for (node n : g.getNodes()) {
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
                FoundGraphs.add(g.getId());
            }
            checks.clear();
            for (int i = 0; i < checksarr.length; i++) {
                checksarr[i] = false;
            }
        }


        return FoundGraphs;
    }


    /**
     * Uses a graph ID and a set of Node IDS which will be used to search for which graphs have the requested node labels present .
     *
     * @return ArrayList<String> An arraylist of strings that contain the graph IDs who have matching node labels.
     */
    public ArrayList<String> searchSubgraphNodeSet(ArrayList<String> labels) {


        ArrayList<String> FoundGraphs = new ArrayList<String>();
        if (labels.size() == 0) {
            return FoundGraphs;
        }


        //***********************************************************
        //boolean array that checks if certain nodes are found
        //could use a hashmap with the label as a key and boolean as value.
        //this would allow to avoid using a for loop inside graph iteration
        boolean[] checks = new boolean[labels.size()];

        for (graph g : graphs.values()) {
            ArrayList<node> tempNodes = new ArrayList<>(g.getNodes());
            for (int i = 0; i < labels.size(); i++) {
                for (node n : tempNodes) {
                    if (n.getLabel().equals(labels.get(i))) {
                        checks[i] = true;
                        //removes node so that it wont be checked again in case two or more of the same labels are required in the set
                        tempNodes.remove(n);
                        break;
                    }
                }
            }

            //checks if all node labels have been found
            if (areAllTrue(checks)) {

                FoundGraphs.add(g.getId());

            }
            for (int i = 0; i < checks.length; i++) {
                checks[i] = false;
            }
        }

        return FoundGraphs;
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
     * @return String The differences and similarities of the two graphs.
     */
    public HashMap<String, Object> compareTwoGraphs(String graphID1, String graphID2) {

        ArrayList<node> nodes1 = graphs.get(graphID1).getNodes();
        ArrayList<node> nodes2 = new ArrayList<>(graphs.get(graphID2).getNodes());
        ArrayList<node> similarNodes = new ArrayList<node>();

        for (node n1 : nodes1) {
            for (node n2 : nodes2) {
                if (n1.getLabel().equals(n2.getLabel())) {
                    similarNodes.add(n1);
                    nodes2.remove(n2);
                    break;
                }
            }
        }

        ArrayList<edge> edges1 = graphs.get(graphID1).getEdges();
        ArrayList<edge> edges2 = new ArrayList<>(graphs.get(graphID2).getEdges());
        ArrayList<edge> similarEdges = new ArrayList<>();

        for (edge e1 : edges1) {
            for (edge e2 : edges2) {
                if ((e1.getLabel().equals(e2.getLabel())) && (e1.getPostLabel().equals(e2.getPostLabel()))) {
                    similarEdges.add(e1);
                    edges2.remove(e2);
                    break;
                }
            }
        }

        HashMap<String, Object> returnObj = new HashMap<>();
        returnObj.put("Nodes", similarNodes);
        returnObj.put("Edges", similarEdges);

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
     * @return String Results of the tests.
     */
    public HashMap<String, Object> runFormalTests(String graphID, boolean planar, boolean longestPathDirected, boolean longestPathUndirected, boolean connected) {
        HashMap<String, Object> returnObj = new HashMap<>();
        if (planar) {
            returnObj.put("Planar", graphs.get(graphID).GraphIsPlanar());
        }
        if (longestPathDirected) {
            returnObj.put("LongestPathDirected", graphs.get(graphID).findLongest(true));
        }
        if (longestPathUndirected) {
            returnObj.put("LongestPathUndirected", graphs.get(graphID).findLongest(false));
        }
        if (connected) {
            returnObj.put("Connected", graphs.get(graphID).connectedBFS());
        }
        return returnObj;
    }

    public HashMap<String, Object> VisualiseFlat(String graphID) {

        graph graph = graphs.get(graphID);

        int height = 100;
        int totalGraphHeight = height * 50 + (height - 1) * 70; //number of levels times the height of each node and the spaces between them

        ArrayList<HashMap<String, Object>> finalNodes = new ArrayList<>();


        for (int i = 0; i < graph.getNodes().size(); i++) {
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

        HashMap<String, Object> singleNode = new HashMap<>();
        singleNode.put("id", graph.getNodes().size());
        singleNode.put("x", graph.getTops().get(0) * 130);
        singleNode.put("y", totalGraphHeight - 150);
        singleNode.put("label", "TOP");
        singleNode.put("type", "node");
        singleNode.put("group", "token");
        singleNode.put("fixed", true);

        finalNodes.add(singleNode);


        ArrayList<HashMap<String, Object>> finalGraphEdges = new ArrayList<>();
        int fromID = 0, toID = 0, fromLevel = 0, toLevel = 0;

        for (edge e : graph.getEdges()) {
            HashMap<String, Object> singleEdge = new HashMap<>();
            for (HashMap<String, Object> node : finalNodes) {

                if ((Integer) node.get("id") == e.getSource()) {
                    fromID = e.getSource();
                    fromLevel = (Integer) node.get("nodeLevel");

                }
                if ((Integer) node.get("id") == e.getTarget()) {
                    toID = e.getTarget();
                    toLevel = (Integer) node.get("nodeLevel");
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

        HashMap<String, Object> Visualised = new HashMap<>();
        Visualised.put("id", graphID);
        Visualised.put("nodes", finalNodes);
        Visualised.put("edges", finalGraphEdges);

        return Visualised;

    }


    public HashMap<String, Object> VisualiseHierarchy(String graphID) {
        graph graph = graphs.get(graphID);

        //Determine span lengths of each node
        int[] graphNodeSpanLengths = new int[graph.getNodes().size()];

        for (int i = 0; i < graphNodeSpanLengths.length; i++) {
            int end = graph.getNodes().get(i).getAnchors().get(0).getEnd();
            int from = graph.getNodes().get(i).getAnchors().get(0).getFrom();
            int span = end - from;
            graphNodeSpanLengths[i] = span;
        }


        //Determine unique span lengths of all the node spans
        ArrayList<Integer> uniqueSpanLengths = new ArrayList<>();

        HashMap<Integer, Boolean> map = new HashMap<>();
        for (Integer item : graphNodeSpanLengths) {
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

            for (
                    int spanIndex = 0;
                    spanIndex < graphNodeSpanLengths.length;
                    spanIndex++
            ) {
                if (graphNodeSpanLengths[spanIndex] == level) {
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
        //console.log({height});
        //console.log({nodesInLevels});
        //console.log({previousLevelHeights});

        //Sort the nodes into the final levels
        ArrayList<ArrayList<node>> nodesInFinalLevels = new ArrayList<>();
        for (int index = 0; index < height; index++) {
            nodesInFinalLevels.add(new ArrayList<node>());
        }
        for (int level = 0; level < newNodeLevels.size(); level++) {
            //console.log(nodesInLevels[level]);
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
        //console.log({ nodesInFinalLevels });

        //Map the nodes in each level to the correct format

        int totalGraphHeight = height * 50 + (height - 1) * 70; //number of levels times the height of each node and the spaces between them

        ArrayList<HashMap<String, Object>> finalNodes = new ArrayList<>();

        for (int level = 0; level < nodesInFinalLevels.size(); level++) {

            for (node n : nodesInFinalLevels.get(level)) {
                HashMap<String, Object> singleNode = new HashMap<>();
                singleNode.put("id", n.getId());
                singleNode.put("x", n.getAnchors().get(0).getFrom() * 110);
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
        ArrayList<HashMap<String, Object>> finalTokens = new ArrayList<>();

        for (token t : graph.getTokens()) {
            HashMap<String, Object> singleToken = new HashMap<>();
            singleToken.put("index", t.getIndex());
            singleToken.put("x", t.getIndex() * 110);
            singleToken.put("y", totalGraphHeight + 100);
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
        int fromID = 0, toID = 0, fromLevel = 0, toLevel = 0;

        for (edge e : graph.getEdges()) {
            HashMap<String, Object> singleEdge = new HashMap<>();
            for (HashMap<String, Object> node : finalNodes) {

                if ((Integer) node.get("id") == e.getSource()) {
                    fromID = e.getSource();
                    fromLevel = (Integer) node.get("nodeLevel");

                }
                if ((Integer) node.get("id") == e.getTarget()) {
                    toID = e.getTarget();
                    toLevel = (Integer) node.get("nodeLevel");
                }
            }
            String edgeType = "";
            if (fromLevel == toLevel && fromLevel == 0) {
                edgeType = "curvedCW";
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
            smooth.put("roundness", 0.4);

            HashMap<String, Object> end = new HashMap<>();
            end.put("from", 20);
            end.put("to", 0);
            singleEdge.put("smooth", smooth);
            singleEdge.put("endPointOffset", end);
            finalGraphEdges.add(singleEdge);

        }


        HashMap<String, Object> Visualised = new HashMap<>();
        Visualised.put("id", graphID);
        Visualised.put("nodes", finalGraphNodes);
        Visualised.put("edges", finalGraphEdges);


        return Visualised;
    }

}
