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
     * Uses a graph ID and the number of a node in the graph and creates a subgraph which will be used to search for in other graphs.
     * @return String A string of graph IDs who have matching subgraphs.
     */
    public ArrayList<String> searchSubgraphPattern(String graphID, graph subgraph) {

        ArrayList<String> FoundGraphs = new ArrayList<String>();


        ArrayList<Boolean> checks = new ArrayList<>();
        for (graph g : graphs.values()) {

            for (node n : g.getNodes()) {
                for (node sn : subgraph.getNodes()) {
                    if (n.getLabel().equals(sn.getLabel())) {
                        for (edge e : g.getEdges()) {
                            for (edge se : subgraph.getEdges()) {
                                if (e.getSource() == n.getId() && se.getSource() == sn.getId() && g.getNodes().get(e.getTarget()).getLabel().equals(subgraph.getNodes().get(se.getTarget()).getLabel())) {
                                    checks.add(true);
                                }
                            }
                        }
                    }
                }

            }
            if (checks.size() == subgraph.getEdges().size() && !checks.contains(false)) {
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
        ArrayList<String> labels = new ArrayList<String>();
        for (int i = 0; i < NodeID.length; i++) {
            String currLabel = graphs.get(graphID).getNodes().get(NodeID[i]).getLabel();
            if (!labels.contains(currLabel)) {
                labels.add(currLabel);
            }
        }
        //***********************************************************

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
