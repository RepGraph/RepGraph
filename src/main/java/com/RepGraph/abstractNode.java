/**
 * An abstract node class extends the node class and represents grammatical or semantic properties.
 * @since 29/08/2020
 */

package com.RepGraph;

import java.util.ArrayList;

public class abstractNode extends node{

    /**
     * Default constructor for the abstract node class.
     */
    public abstractNode(){
        super();
    }

    /**
     * Fully parameterised constructor for the abstract node class.
     * @param id The node's ID number.
     * @param label The node's label.
     * @param anchors An array list of the node's anchors.
     */
    public abstractNode(int id, String label, ArrayList<anchors> anchors) {
        super(id, label, anchors);
    }
}
