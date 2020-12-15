package com.RepGraph;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static junit.framework.TestCase.*;

public class NodeTest {

    @Test
    public void test_Constructor_CreatesAndAssignsMemberVariables() throws NoSuchFieldException, IllegalAccessException {

        ArrayList<Anchors> anchs = new ArrayList<Anchors>();
        anchs.add(new Anchors(0, 1));
        final Node n = new Node(3, "proper_q", anchs);


        //Get fields without using getter
        final Field fieldID = n.getClass().getDeclaredField("id");
        fieldID.setAccessible(true);
        final Field fieldLabel = n.getClass().getDeclaredField("label");
        fieldLabel.setAccessible(true);
        final Field fieldAnchors = n.getClass().getDeclaredField("Anchors");
        fieldAnchors.setAccessible(true);


        assertEquals("id value was not constructed properly.", fieldID.get(n), 3);
        assertEquals("label value was not constructed properly.", fieldLabel.get(n), "proper_q");
        assertEquals("Anchors value was not constructed properly.", fieldAnchors.get(n), anchs);
    }

    @Test
    public void test_getID_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{
        final Node n = new Node();

        //Set field without using setter
        final Field field = n.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(n, 3);

        assertEquals("id value was not retrieved properly.", n.getId(), 3);
    }

    @Test
    public void test_getLabel_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{
        final Node n = new Node();

        //Set field without using setter
        final Field field = n.getClass().getDeclaredField("label");
        field.setAccessible(true);
        field.set(n, "proper_q");

        assertEquals("label value was not retrieved properly.", n.getLabel(), "proper_q");
    }

    @Test
    public void test_getAnchors_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{
        final Node n = new Node();

        ArrayList<Anchors> anchs = new ArrayList<Anchors>();
        anchs.add(new Anchors(0, 1));

        //Set field without using setter
        final Field field = n.getClass().getDeclaredField("Anchors");
        field.setAccessible(true);
        field.set(n, anchs);

        assertEquals("Anchors value was not retrieved properly.", n.getAnchors(), anchs);
    }

    @Test
    public void test_setID_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{

        final Node n = new Node();
        n.setId(3);

        //Get field without using getter
        final Field field = n.getClass().getDeclaredField("id");
        field.setAccessible(true);

        assertEquals("id value was not set properly.", field.get(n), 3);
    }

    @Test
    public void test_setLabel_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{

        final Node n = new Node();
        n.setLabel("proper_q");

        //Get field without using getter
        final Field field = n.getClass().getDeclaredField("label");
        field.setAccessible(true);

        assertEquals("label value was not set properly.", field.get(n), "proper_q");
    }

    @Test
    public void test_setAnchors_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{

        final Node n = new Node();
        ArrayList<Anchors> anchs = new ArrayList<Anchors>();
        anchs.add(new Anchors(0, 1));
        n.setAnchors(anchs);

        //Get field without using getter
        final Field field = n.getClass().getDeclaredField("Anchors");
        field.setAccessible(true);

        assertEquals("Anchors value was not set properly.", field.get(n), anchs);
    }

    @Test
    public void test_addDirectedNeighbour_AddsNeighbourCorrectly() throws NoSuchFieldException, IllegalAccessException{

        final Node n = new Node();

        ArrayList<Anchors> anchs = new ArrayList<>();
        anchs.add(new Anchors(0, 1));

        final Node n1 = new Node(3,"proper_q",anchs);
        final Node n2 = new Node(2,"test_q",anchs);

        ArrayList<Node> correctResult = new ArrayList<Node>();
        correctResult.add(n1);
        correctResult.add(n2);
        n.addDirectedNeighbour(n1);
        n.addDirectedNeighbour(n2);

        //Get field without using getter
        final Field field = n.getClass().getDeclaredField("directedNeighbours");
        field.setAccessible(true);

        assertEquals("Directed Node neighbour was not added to correctly.", field.get(n), correctResult);
    }

    @Test
    public void test_getDirectedNeighbours_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{

        final Node n = new Node();

        ArrayList<Anchors> anchs = new ArrayList<>();
        anchs.add(new Anchors(0, 1));

        final Node n1 = new Node(3,"proper_q",anchs);
        final Node n2 = new Node(2,"test_q",anchs);

        ArrayList<Node> correctResult = new ArrayList<Node>();
        correctResult.add(n1);
        correctResult.add(n2);

        //Get field without using getter
        final Field field = n.getClass().getDeclaredField("directedNeighbours");
        field.setAccessible(true);
        field.set(n, correctResult);

        assertEquals("directedNeighbours value was not retrieved correctly.", n.getDirectedNeighbours(), correctResult);
    }

    @Test
    public void test_addUndirectedNeighbour_AddsNeighbourCorrectly() throws NoSuchFieldException, IllegalAccessException{

        final Node n = new Node();

        ArrayList<Anchors> anchs = new ArrayList<>();
        anchs.add(new Anchors(0, 1));

        final Node n1 = new Node(3,"proper_q",anchs);
        final Node n2 = new Node(2,"test_q",anchs);

        ArrayList<Node> correctResult = new ArrayList<Node>();
        correctResult.add(n1);
        correctResult.add(n2);
        n.addUndirectedNeighbour(n1);
        n.addUndirectedNeighbour(n2);

        //Get field without using getter
        final Field field = n.getClass().getDeclaredField("undirectedNeighbours");
        field.setAccessible(true);

        assertEquals("Undirected Node neighbour was not added to correctly.", field.get(n), correctResult);
    }

    @Test
    public void test_getUndirectedNeighbours_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{

        final Node n = new Node();

        ArrayList<Anchors> anchs = new ArrayList<>();
        anchs.add(new Anchors(0, 1));

        final Node n1 = new Node(3,"proper_q",anchs);
        final Node n2 = new Node(2,"test_q",anchs);

        ArrayList<Node> correctResult = new ArrayList<Node>();
        correctResult.add(n1);
        correctResult.add(n2);

        //Get field without using getter
        final Field field = n.getClass().getDeclaredField("undirectedNeighbours");
        field.setAccessible(true);
        field.set(n, correctResult);

        assertEquals("undirectedNeighbours value was not retrieved correctly.", n.getUndirectedNeighbours(), correctResult);
    }

    @Test
    public void test_addDirectedEdgeNeighbour_AddEdgeCorectly() throws NoSuchFieldException, IllegalAccessException {
        final Node n = new Node();

        ArrayList<Anchors> anchs = new ArrayList<>();
        anchs.add(new Anchors(0, 1));

        final Edge e1 = new Edge(0,1,"testLabel1","testPostLabel1");
        final Edge e2 = new Edge(2,3,"testLabel2","testPostLabel2");

        ArrayList<Edge> correctResult = new ArrayList<Edge>();
        correctResult.add(e1);
        correctResult.add(e2);

        n.addDirectedEdgeNeighbour(e1);
        n.addDirectedEdgeNeighbour(e2);

        //Get field without using getter
        final Field field = n.getClass().getDeclaredField("directedEdgeNeighbours");
        field.setAccessible(true);

        assertEquals("edgeNeighbours was not added to correctly.", field.get(n), correctResult);
    }

    @Test
    public void test_getDirectedEdgeNeighbours_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final Node n = new Node();

        ArrayList<Anchors> anchs = new ArrayList<>();
        anchs.add(new Anchors(0, 1));

        final Edge e1 = new Edge(0,1,"testLabel1","testPostLabel1");
        final Edge e2 = new Edge(2,3,"testLabel2","testPostLabel2");

        ArrayList<Edge> correctResult = new ArrayList<Edge>();
        correctResult.add(e1);
        correctResult.add(e2);

        //Get field without using getter
        final Field field = n.getClass().getDeclaredField("directedEdgeNeighbours");
        field.setAccessible(true);
        field.set(n, correctResult);

        assertEquals("edgeNeighbours value was not retrieved correctly.", n.getDirectedEdgeNeighbours(), correctResult);
    }

    @Test
    public void test_equal_TwoNodesWithDifferentValues() {

        ArrayList<Anchors> anchs = new ArrayList<>();
        anchs.add(new Anchors(0, 1));

        final Node n1 = new Node(2, "named", anchs);
        final Node n2 = new Node(3, "proper_q", anchs);

        assertFalse("Equals does not work with two nodes with different values.", n1.equals(n2));
    }

    @Test
    public void test_equal_IdenticalNodeWithSameValues() {

        ArrayList<Anchors> anchs1 = new ArrayList<>();
        anchs1.add(new Anchors(0, 1));

        ArrayList<Anchors> anchs2 = new ArrayList<>();
        anchs2.add(new Anchors(0, 1));

        final Node n1 = new Node(3, "proper_q", anchs1);
        final Node n2 = new Node(3, "proper_q", anchs2);

        assertTrue("Equals does not work with two identical nodes with the same values.", n1.equals(n2));
    }

    @Test
    public void test_equal_TwoObjectsOfDifferentClasses() {

        ArrayList<Anchors> anchs = new ArrayList<>();
        anchs.add(new Anchors(0, 1));

        final Node n1 = new Node(3, "proper_q", anchs);

        assertFalse("Equals does not work with two objects of different classes", n1.equals(anchs));
    }

    @Test
    public void test_equal_EqualToItself() {

        ArrayList<Anchors> anchs = new ArrayList<>();
        anchs.add(new Anchors(0, 1));

        final Node n1 = new Node(3, "proper_q", anchs);

        assertTrue("Equals does not work with a Node equalling itself.", n1.equals(n1));
    }

}
