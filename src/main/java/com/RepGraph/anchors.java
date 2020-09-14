
package com.RepGraph;

/**
 * This class stores the token span information of a node
 */
public class anchors {

    private int from;
    private int end;

    /**
     * Default Constructor
     */
    public anchors() {

    }

    /**
     * This is the parameterised constructor
     *
     * @param from This refers to the first token in the node span
     * @param end  This refers to the last token in the node span
     */
    public anchors(int from, int end) {
        this.from = from;
        this.end = end;
    }

    /**
     * This is the getter method for the "from" private variable
     * @return int This is the value of "from"
     */
    public int getFrom() {
        return from;
    }

    /**
     * This is the getter method for the "end" private variable
     * @return int This is the value of "end"
     */
    public int getEnd() {
        return end;
    }

    /**
     * This is the setter method for the "end" private variable
     * @param end This is the value that the "end" variable will be set to.
     */
    public void setEnd(int end) {
        this.end = end;
    }

    /**
     * This is the setter method for the "from" private variable
     * @param from This is the value that the "from" variable will be set to.
     */
    public void setFrom(int from) {
        this.from = from;
    }

    /**
     * Equals method for the node class.
     * @param o Object
     * @return boolean Whether to two classes being compared are equal.
     */
    @Override
    public boolean equals(Object o){

        if (o == this){
            return true;
        }

        if (!(o instanceof anchors)){
            return false;
        }

        anchors a = (anchors) o;

        return (from == a.getFrom()) && (end == a.getEnd());
    }


}
