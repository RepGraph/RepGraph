package com.RepGraph;

import java.io.IOException;
import java.util.*;

/**
 * The RepGraphModel class is used to store all the system's graphs and run analysis functions on graphs.
 */
public class AMRModel extends AbstractModel {


    public AMRModel() {
        super();
    }

    public AMRGraph getGraph(String graphID) throws IOException, InterruptedException {
        AMRGraph g =  (AMRGraph)graphs.get(graphID);
        g.alignNodes();
        return g;
    }

    public void alignAllGraphs() throws IOException, InterruptedException {
        for (AbstractGraph t:graphs.values()) {
            AMRGraph g =  (AMRGraph)t;
            g.alignNodes();
        }
    }


}
