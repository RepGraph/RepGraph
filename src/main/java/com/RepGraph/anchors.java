
package com.RepGraph;

public class anchors {
    private int from;
    private int end;

    public anchors(int from, int end) {
        this.from = from;
        this.end = end;
    }

    public int getFrom() {
        return from;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

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
