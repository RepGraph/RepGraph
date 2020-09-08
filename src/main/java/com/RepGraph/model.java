/**
 * The model class is used to store all the system's graphs and run analysis functions on graphs.
 * @since 29/08/2020
 */

package com.RepGraph;

import java.util.HashMap;

public class model {

    /**
     * A hashmap of the model's graphs that maps graph IDs to their graph class.
     */
    private HashMap<String, graph> graphs;

    /**
     * Default constructor for the model class.
     */
    public model(){}

    /**
     * Getter method for a graph given the graph's ID.
     * @param graphID A graph's ID.
     * @return graph The requested graph.
     */
    public graph getGraph(String graphID){
        return null;
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
    public String searchSubgraph(String graphID, String headNodeID){
        return null;
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
