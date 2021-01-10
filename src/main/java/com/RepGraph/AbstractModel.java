package com.RepGraph;

import java.util.*;

/**
 * The RepGraphModel class is used to store all the system's graphs and run analysis functions on graphs.
 */
abstract class AbstractModel {

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

    public abstract HashMap<String, Object> DisplaySubset(String graphId, String headNodeID, String SubsetType, int format);


    public abstract AbstractGraph CreateSubsetAdjacent(String graphID, String headNodeID);


    public abstract AbstractGraph CreateSubsetDescendent(String graphID, String headNodeID) ;


    public abstract HashMap<String, Object> searchSubgraphPattern(String graphID, String[] NodeId, int[] EdgeIndices);


    public abstract HashMap<String, Object> searchSubgraphPattern(AbstractGraph subgraph);


    public abstract HashMap<String, Object> searchSubgraphNodeSet(ArrayList<String> labels);

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


    public abstract HashMap<String, Object> compareTwoGraphs(String graphID1, String graphID2);


    public abstract HashMap<String, Object> runFormalTests(String graphID, boolean planar, boolean longestPathDirected, boolean longestPathUndirected, boolean connected);


    public abstract HashMap<String, Object> Visualise(String graphID, int format);


    public abstract HashMap<String, Object> VisualisePlanar(AbstractGraph graph);


    public abstract HashMap<String, Object> VisualiseFlat(AbstractGraph graph);


    public abstract HashMap<String, Object> VisualiseTree(AbstractGraph graph);


    public abstract HashMap<String, Object> VisualiseHierarchy(AbstractGraph graph);

}
