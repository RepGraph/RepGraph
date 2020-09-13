/**
 * The RepGraphModel class is used to store all the system's graphs and run analysis functions on graphs.
 * @since 29/08/2020
 *
 */

package com.RepGraph;

import java.util.ArrayList;
import java.util.HashMap;

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
     * @param graphID A graph's ID.
     * @return graph The requested graph.
     */
    public graph getGraph(String graphID){
        return graphs.get(graphID);
    }

    /**
     * Adds graph to hashmap
     *
     * @param value Graph object
     */
    public void addGraph(graph value) {
        graphs.put(value.getId(), value);
    }

    /**
     * Uses a graph ID and the number of a node in the graph and returns a subset of the graph.
     * @param graphID The graph ID.
     * @param headNodeID The graph's node which will be the head node of the subset.
     * @return graph The subset of the graph.
     */
    public graph displaySubset(String graphID, String headNodeID){
        return null;
    }

    /**
     * This method searches through the graph objects in the hashmap to find graphs that contain a
     * subgraph pattern.
     * @return String A string of graph IDs who have matching subgraphs.
     */
    public ArrayList<String> searchSubgraphPattern(graph subgraph) {

        //Empty Arraylist for the graph ids that have the subgraph pattern specified.
        ArrayList<String> FoundGraphs = new ArrayList<String>();

        // Hashmap with a string key and boolean value - this is used to confirm that each unique edge in the subgraph has been found in the graph being checked.
        //This is necessary because there can be multiple of the same two node links so a counter or an array cant be used.
        HashMap<String, Boolean> checks = new HashMap<>();
        //This is just used to easily check that all the required edges in the subgraph are true because all values in this array need to be true before a graph is added
        //to foundgraphs arraylist.
        boolean[] checksarr = new boolean[subgraph.getEdges().size()];

        //Iterate over each graph in the dataset
        for (graph g : graphs.values()) {
            //Iterate over each node in the subgraph patttern
            for (node sn : subgraph.getNodes()) {
                //for each node in the subgraph pattern, it iterates over every node in the current graph that is being checked.
                //This is to find a node label equal to the current sub graph node being checked.
                for (node n : g.getNodes()) {
                    //This checks if the labels are equal
                    if (n.getLabel().equals(sn.getLabel())) {
                        //Once it finds a node in the graph that is the same as the subgraph node label it has to iterate over the subgraph edges list
                        //to find the corresponding edge of the subgraph node that found a match.
                        for (edge se : subgraph.getEdges()) {
                            //For every edge it has to iterate over all the current graph edges to find the edge of the matched graph node as well
                            for (edge e : g.getEdges()) {
                                //This statement checks that each edge is the correct edge of the matching nodes
                                //and also checks if the target nodes of the subgraph node and graph node have the same label.
                                //if this is true it creates a hashmap entry with the subgraph node id and the subgraph target node id and sets the value to true
                                //this indicates that the unique edge in the subgraph pattern has been found.
                                if (e.getSource() == n.getId() && se.getSource() == sn.getId() && g.getNodes().get(e.getTarget()).getLabel().equals(subgraph.getNodes().get(se.getTarget()).getLabel())) {
                                    checks.put(sn.getId() + subgraph.getNodes().get(se.getTarget()).getId() + "", true);
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
        }


        return FoundGraphs;
    }




    /**
     * Uses a graph ID and a set of Node IDS which will be used to search for which graphs have the requested node labels present .
     *
     * @return ArrayList<String> An arraylist of strings that contain the graph IDs who have matching node labels.
     */
    public ArrayList<String> searchSubgraphNodeSet(String graphID, int[] NodeID) {

        ArrayList<String> FoundGraphs = new ArrayList<String>();

        //Following code is only if nodeIDs are given - we could remove if method is given node labels directly
        //***********************************************************
        //This following code creates a list and populates it with the labels of the nodes specified to be searched for
        ArrayList<String> labels = new ArrayList<String>();
        for (int i = 0; i < NodeID.length; i++) {
            String currLabel = graphs.get(graphID).getNodes().get(NodeID[i]).getLabel();
            if (!labels.contains(currLabel)) {
                labels.add(currLabel);
            }
        }
        //***********************************************************
        //boolean array that checks if certain nodes are found
        //could use a hashmap with the label as a key and boolean as value.
        //this would allow to avoid using a for loop inside graph iteration
        boolean[] checks = new boolean[labels.size()];

        for (graph g : graphs.values()) {
            for (node n : g.getNodes()) {
                //could use indexOf to get rid of forloop
                for (int i = 0; i < labels.size(); i++) {
                    if (n.getLabel().equals(labels.get(i))) {
                        checks[i] = true;
                    }
                }
            }
            //checks if all node labels have been found
            if (areAllTrue(checks)) {
                FoundGraphs.add(g.getId());
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
        for (boolean b : array) if (!b) return false;
        return true;
    }
    /**
     * Compares two graphs and searches for similarities and differences.
     * @param graphID1 Graph ID of the first graph.
     * @param graphID2 Graph ID of the second graph.
     * @return String The differences and similarities of the two graphs.
     */
    public String compareTwoGraphs(String graphID1, String graphID2){
        return null;
    }

    /**
     * Runs formal tests on a graph.
     * @param graphID The graph ID which the tests will be run.
     * @param planar Boolean to decide if to test for if the graph is planar.
     * @param directed Boolean to decide if to find the longest directed or undirected path.
     * @param connected Boolean to decide if to test for if the graph is connected.
     * @return String Results of the tests.
     */
    public String runFormalTests(String graphID, boolean planar, boolean directed, boolean connected){
        return null;
    }


}
