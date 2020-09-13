package com.RepGraph;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;

public class NodeTest {

    @Test
    public void test_Constructor_CreatesAndAssignsMemberVariables() throws NoSuchFieldException, IllegalAccessException {

        ArrayList<anchors> anchs = new ArrayList<anchors>();
        anchs.add(new anchors(0, 1));
        final node n = new node(3, "proper_q", anchs);


        //Get fields without using getter
        final Field fieldID = n.getClass().getDeclaredField("id");
        fieldID.setAccessible(true);
        final Field fieldLabel = n.getClass().getDeclaredField("label");
        fieldLabel.setAccessible(true);
        final Field fieldAnchors = n.getClass().getDeclaredField("anchors");
        fieldAnchors.setAccessible(true);


        assertEquals("id value was not constructed properly.", fieldID.get(n), 3);
        assertEquals("label value was not constructed properly.", fieldLabel.get(n), "proper_q");
        assertEquals("anchors value was not constructed properly.", fieldAnchors.get(n), anchs);
    }



}
