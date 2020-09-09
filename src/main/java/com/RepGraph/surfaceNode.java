/**
 * A surface node class extends the node class and represents the meaning of one or more tokens directly.
 * @since 29/08/2020
 */

package com.RepGraph;

import java.util.ArrayList;

public class surfaceNode extends node{

    /**
     * Default constructor for the surface node class.
     */
    public surfaceNode(){
        super();
    }

    /**
     * Fully parameterised constructor for the surface node class.
     * @param id The node's ID number.
     * @param label The node's label.
     * @param anchors An array list of the node's anchors.
     */
    public surfaceNode(int id, String label, ArrayList<anchors> anchors) {
        super(id, label, anchors);
    }
}
