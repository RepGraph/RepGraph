package com.RepGraph;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static junit.framework.TestCase.*;

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

    @Test
    public void test_getID_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{
        final node n = new node();

        //Set field without using setter
        final Field field = n.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(n, 3);

        assertEquals("id value was not retrieved properly.", n.getId(), 3);
    }

    @Test
    public void test_getLabel_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{
        final node n = new node();

        //Set field without using setter
        final Field field = n.getClass().getDeclaredField("label");
        field.setAccessible(true);
        field.set(n, "proper_q");

        assertEquals("label value was not retrieved properly.", n.getLabel(), "proper_q");
    }

    @Test
    public void test_getAnchors_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{
        final node n = new node();

        ArrayList<anchors> anchs = new ArrayList<anchors>();
        anchs.add(new anchors(0, 1));

        //Set field without using setter
        final Field field = n.getClass().getDeclaredField("anchors");
        field.setAccessible(true);
        field.set(n, anchs);

        assertEquals("anchors value was not retrieved properly.", n.getAnchors(), anchs);
    }

    @Test
    public void test_setID_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{

        final node n = new node();
        n.setId(3);

        //Get field without using getter
        final Field field = n.getClass().getDeclaredField("id");
        field.setAccessible(true);

        assertEquals("id value was not set properly.", field.get(n), 3);
    }

    @Test
    public void test_setLabel_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{

        final node n = new node();
        n.setLabel("proper_q");

        //Get field without using getter
        final Field field = n.getClass().getDeclaredField("label");
        field.setAccessible(true);

        assertEquals("label value was not set properly.", field.get(n), "proper_q");
    }

    @Test
    public void test_setAnchors_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{

        final node n = new node();
        ArrayList<anchors> anchs = new ArrayList<anchors>();
        anchs.add(new anchors(0, 1));
        n.setAnchors(anchs);

        //Get field without using getter
        final Field field = n.getClass().getDeclaredField("anchors");
        field.setAccessible(true);

        assertEquals("anchors value was not set properly.", field.get(n), anchs);
    }

    /*@Test
    public void test_addNeighbour_AddNeighbourCorrectly() throws NoSuchFieldException, IllegalAccessException{

        final node n = new node();

        ArrayList<anchors> anchs = new ArrayList<anchors>();
        anchs.add(new anchors(0, 1));

        final node n1 = new node(3,"proper_q",anchs);

        ArrayList<node> g = new ArrayList<node>();
        g.add(n1);
        n.addNeighbour(n1);

        //Get field without using getter
        final Field field = n.getClass().getDeclaredField("nodeNeighbours");
        field.setAccessible(true);

        assertEquals("nodeNeighbours was not added to correctly.", field.get(n), g);
    }
*/
    @Test
    public void test_getNodeNeighbours_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{

        final node n = new node();

        ArrayList<anchors> anchs = new ArrayList<anchors>();
        anchs.add(new anchors(0, 1));

        final node n1 = new node(3,"proper_q",anchs);

        ArrayList<node> g = new ArrayList<node>();
        g.add(n1);

        //Get field without using getter
        final Field field = n.getClass().getDeclaredField("nodeNeighbours");
        field.setAccessible(true);
        field.set(n, g);

        assertEquals("nodeNeighbours value was not retrieved correctly.", n.getNodeNeighbours(), g);
    }

    @Test
    public void test_equal_TwoNodesWithDifferentValues() {

        ArrayList<anchors> anchs = new ArrayList<anchors>();
        anchs.add(new anchors(0, 1));

        final node n1 = new node(2, "named", anchs);
        final node n2 = new node(3, "proper_q", anchs);

        assertFalse("Equals does not work with two nodes with different values.", n1.equals(n2));
    }

    @Test
    public void test_equal_IdenticalNodeWithSameValues() {

        ArrayList<anchors> anchs = new ArrayList<anchors>();
        anchs.add(new anchors(0, 1));

        final node n1 = new node(3, "proper_q", anchs);
        final node n2 = new node(3, "proper_q", anchs);

        assertTrue("Equals does not work with two identical nodes.", n1.equals(n2));
    }

    @Test
    public void test_equal_TwoObjectsOfDifferentClasses() {

        ArrayList<anchors> anchs = new ArrayList<anchors>();
        anchs.add(new anchors(0, 1));

        final node n1 = new node(3, "proper_q", anchs);

        assertFalse("Equals does not work with two objects of different classes", n1.equals(anchs));
    }

    @Test
    public void test_equal_EqualToItself() {

        ArrayList<anchors> anchs = new ArrayList<anchors>();
        anchs.add(new anchors(0, 1));

        final node n1 = new node(3, "proper_q", anchs);

        assertTrue("Equals does not work with a node equalling itself.", n1.equals(n1));
    }

}
