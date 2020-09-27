package com.RepGraph;

import org.junit.Before;
import org.junit.Test;

import javax.validation.constraints.AssertTrue;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static junit.framework.TestCase.*;


public class GraphTest {

    @Test
    public void test_Constructor_CreatesAndAssignsMemberVariables() throws NoSuchFieldException, IllegalAccessException {

        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();
        ArrayList<token> tokens = new ArrayList<>();

        nodes.add(new node(0, "node1", new ArrayList<anchors>()));
        nodes.add(new node(1, "node2", new ArrayList<anchors>()));
        nodes.add(new node(2, "node3", new ArrayList<anchors>()));
        nodes.add(new node(3, "node4", new ArrayList<anchors>()));

        edges.add(new edge(0, 1, "testlabel", "testpostlabel"));
        edges.add(new edge(1, 3, "testlabel1", "testpostlabel1"));
        edges.add(new edge(0, 2, "testlabel2", "testpostlabel2"));

        tokens.add(new token(0, "node1 form", "node1 lemma", "node1 carg"));
        tokens.add(new token(1, "node2 form", "node2 lemma", "node2 carg"));
        tokens.add(new token(2, "node3 form", "node3 lemma", "node3 carg"));
        tokens.add(new token(3, "node4 form", "node4 lemma", "node4 carg"));

        graph g = new graph("11111", "testsource", "node1 node2 node3 node4", nodes, tokens, edges, new ArrayList<Integer>());

        //Get fields without using getter
        final Field fieldID = g.getClass().getDeclaredField("id");
        fieldID.setAccessible(true);
        final Field fieldSource = g.getClass().getDeclaredField("source");
        fieldSource.setAccessible(true);
        final Field fieldInput = g.getClass().getDeclaredField("input");
        fieldInput.setAccessible(true);
        final Field fieldNodes = g.getClass().getDeclaredField("nodes");
        fieldNodes.setAccessible(true);
        final Field fieldTokens = g.getClass().getDeclaredField("tokens");
        fieldTokens.setAccessible(true);
        final Field fieldEdges = g.getClass().getDeclaredField("edges");
        fieldEdges.setAccessible(true);


        assertEquals("id value was not constructed properly.", fieldID.get(g), "11111");
        assertEquals("source value was not constructed properly.", fieldSource.get(g), "testsource");
        assertEquals("input value was not constructed properly.", fieldInput.get(g), "node1 node2 node3 node4");
        assertEquals("nodes value was not constructed properly.", fieldNodes.get(g), nodes);
        assertEquals("tokens value was not constructed properly.", fieldTokens.get(g), tokens);
        assertEquals("edges value was not constructed properly.", fieldEdges.get(g), edges);


    }

    @Test
    public void test_getId_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {
        final graph g = new graph();

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(g, "11111");

        assertEquals("id value was not retrieved properly.", g.getId(), "11111");
    }

    @Test
    public void test_getSource_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {
        final graph g = new graph();

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("source");
        field.setAccessible(true);
        field.set(g, "testsource");

        assertEquals("source value was not retrieved properly.", g.getSource(), "testsource");
    }

    @Test
    public void test_getInput_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {
        final graph g = new graph();

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("input");
        field.setAccessible(true);
        field.set(g, "node1 node2 node3 node4");

        assertEquals("input value was not retrieved properly.", g.getInput(), "node1 node2 node3 node4");
    }

    @Test
    public void test_getNodes_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {
        final graph g = new graph();

        ArrayList<node> nodes = new ArrayList<>();
        nodes.add(new node(0, "node1", new ArrayList<anchors>()));

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("nodes");
        field.setAccessible(true);
        field.set(g, nodes);

        assertEquals("nodes value was not retrieved properly.", g.getNodes(), nodes);
    }

    @Test
    public void test_getTokens_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {
        final graph g = new graph();

        ArrayList<token> tokens = new ArrayList<token>();
        tokens.add(new token(0, "form1", "lemma1", "carg1"));

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("tokens");
        field.setAccessible(true);
        field.set(g, tokens);

        assertEquals("tokens value was not retrieved properly.", g.getTokens(), tokens);
    }

    @Test
    public void test_getEdges_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {
        final graph g = new graph();

        ArrayList<edge> edges = new ArrayList<edge>();
        edges.add(new edge(0, 1, "testlabel", "testpostlabel"));

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("edges");
        field.setAccessible(true);
        field.set(g, edges);

        assertEquals("edges value was not retrieved properly.", g.getEdges(), edges);
    }

    @Test
    public void test_setId_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final graph g = new graph();

        g.setId("11111");

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("id");
        field.setAccessible(true);

        assertEquals("id value was not retrieved properly.", field.get(g), "11111");
    }

    @Test
    public void test_setSource_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final graph g = new graph();

        g.setSource("testSource");

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("source");
        field.setAccessible(true);

        assertEquals("source value was not retrieved properly.", field.get(g), "testSource");
    }

    @Test
    public void test_setInput_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final graph g = new graph();

        g.setInput("node1 node2 node3 node4");

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("input");
        field.setAccessible(true);

        assertEquals("input value was not retrieved properly.", field.get(g), "node1 node2 node3 node4");
    }

    @Test
    public void test_setNodes_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final graph g = new graph();

        ArrayList<node> nodes = new ArrayList<>();
        nodes.add(new node(0, "node1", new ArrayList<anchors>()));

        g.setNodes(nodes);

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("nodes");
        field.setAccessible(true);

        assertEquals("nodes value was not retrieved properly.", field.get(g), nodes);
    }

    @Test
    public void test_setTokens_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final graph g = new graph();

        ArrayList<token> tokens = new ArrayList<token>();
        tokens.add(new token(0, "form1", "lemma1", "carg1"));

        g.setTokens(tokens);

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("tokens");
        field.setAccessible(true);

        assertEquals("tokens value was not retrieved properly.", field.get(g), tokens);
    }

    @Test
    public void test_setEdges_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final graph g = new graph();

        ArrayList<edge> edges = new ArrayList<edge>();
        edges.add(new edge(0, 1, "testlabel", "testpostlabel"));

        g.setEdges(edges);

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("edges");
        field.setAccessible(true);

        assertEquals("edges value was not retrieved properly.", field.get(g), edges);
    }

    @Test
    public void test_equals_TwoGraphsWithDifferentValues() {

        ArrayList<node> nodes1 = new ArrayList<>();
        ArrayList<edge> edges1 = new ArrayList<>();
        ArrayList<token> tokens1 = new ArrayList<>();

        ArrayList<node> nodes2 = new ArrayList<>();
        ArrayList<edge> edges2 = new ArrayList<>();
        ArrayList<token> tokens2 = new ArrayList<>();


        ArrayList<anchors> anchs = new ArrayList<>();
        anchs.add(new anchors(1, 2));

        nodes1.add(new node(0, "node1", anchs));
        nodes1.add(new node(1, "node2", anchs));
        nodes2.add(new node(2, "node3", anchs));
        nodes2.add(new node(3, "node4", anchs));

        edges1.add(new edge(0, 1, "testlabel", "testpostlabel"));
        edges2.add(new edge(1, 3, "testlabel1", "testpostlabel1"));
        edges2.add(new edge(0, 2, "testlabel2", "testpostlabel2"));

        tokens1.add(new token(0, "node1 form", "node1 lemma", "node1 carg"));
        tokens1.add(new token(1, "node2 form", "node2 lemma", "node2 carg"));
        tokens2.add(new token(2, "node3 form", "node3 lemma", "node3 carg"));
        tokens2.add(new token(3, "node4 form", "node4 lemma", "node4 carg"));

        graph g1 = new graph("11111", "testsource1", "node1 node2 node3 node4", nodes1, tokens1, edges1, new ArrayList<Integer>());
        graph g2 = new graph("11111", "testsource2", "node1 node2 node3 node4", nodes2, tokens2, edges2, new ArrayList<Integer>());


        assertFalse("Equals does not work with two graphs with different values.", g1.equals(g2));
    }

    @Test
    public void test_equals_IdenticalGraphsWithSameValues() {

        ArrayList<node> nodes1 = new ArrayList<>();
        ArrayList<edge> edges1 = new ArrayList<>();
        ArrayList<token> tokens1 = new ArrayList<>();

        ArrayList<node> nodes2 = new ArrayList<>();
        ArrayList<edge> edges2 = new ArrayList<>();
        ArrayList<token> tokens2 = new ArrayList<>();

        ArrayList<anchors> anchs = new ArrayList<>();
        anchs.add(new anchors(1, 2));

        nodes1.add(new node(0, "node1", anchs));
        nodes2.add(new node(0, "node1", anchs));

        edges1.add(new edge(0, 1, "testlabel", "testpostlabel"));
        edges2.add(new edge(0, 1, "testlabel", "testpostlabel"));

        tokens1.add(new token(0, "node1 form", "node1 lemma", "node1 carg"));
        tokens2.add(new token(0, "node1 form", "node1 lemma", "node1 carg"));

        graph g1 = new graph("11111", "testsource", "node1 node2 node3 node4", nodes1, tokens1, edges1, new ArrayList<Integer>());
        graph g2 = new graph("11111", "testsource", "node1 node2 node3 node4", nodes2, tokens2, edges2, new ArrayList<Integer>());

        assertTrue("Equals does not work with two identical graphs with the same values.", g1.equals(g2));
    }

    @Test
    public void test_equals_TwoObjectsOfDifferentClasses() {

        ArrayList<node> nodes1 = new ArrayList<>();
        ArrayList<edge> edges1 = new ArrayList<>();
        ArrayList<token> tokens1 = new ArrayList<>();

        nodes1.add(new node(0, "node1", new ArrayList<anchors>()));

        edges1.add(new edge(0, 1, "testlabel", "testpostlabel"));

        tokens1.add(new token(0, "node1 form", "node1 lemma", "node1 carg"));

        graph g1 = new graph("11111", "testsource", "node1 node2 node3 node4", nodes1, tokens1, edges1, new ArrayList<Integer>());

        assertFalse("Equals does not work with two objects of different classes", g1.equals(new anchors(0, 1)));
    }

    @Test
    public void test_equals_EqualToItself() {

        ArrayList<node> nodes1 = new ArrayList<>();
        ArrayList<edge> edges1 = new ArrayList<>();
        ArrayList<token> tokens1 = new ArrayList<>();

        nodes1.add(new node(0, "node1", new ArrayList<anchors>()));

        edges1.add(new edge(0, 1, "testlabel", "testpostlabel"));

        tokens1.add(new token(0, "node1 form", "node1 lemma", "node1 carg"));

        graph g1 = new graph("11111", "testsource", "node1 node2 node3 node4", nodes1, tokens1, edges1, new ArrayList<Integer>());

        assertTrue("Equals does not work with a token equalling itself.", g1.equals(g1));
    }

    @Test
    public void test_setNodeNeighbours_setDirectedNeighbouringNodesCorrectly() throws NoSuchFieldException, IllegalAccessException {
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node1", new ArrayList<>());
        node node1 = new node(1, "node2", new ArrayList<>());
        node node2 = new node(2, "node3", new ArrayList<>());
        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);

        edges.add(new edge(0, 1, "testlabel", "testpostlabel"));
        edges.add(new edge(1, 2, "testlabel1", "testpostlabel1"));
        edges.add(new edge(0, 2, "testlabel2", "testpostlabel2"));

        graph g = new graph("11111", "testsource", "node1 node2 node3 node4", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        ArrayList<node> correctResult0 = new ArrayList<>();
        ArrayList<node> correctResult1 = new ArrayList<>();
        ArrayList<node> correctResult2 = new ArrayList<>();

        correctResult0.add(node1);
        correctResult0.add(node2);
        correctResult1.add(node2);

        g.setNodeNeighbours();

        //Get fields without using getter
        final Field directField0 = nodes.get(0).getClass().getDeclaredField("directedNeighbours");
        directField0.setAccessible(true);
        final Field directField1 = nodes.get(1).getClass().getDeclaredField("directedNeighbours");
        directField1.setAccessible(true);
        final Field directField2 = nodes.get(2).getClass().getDeclaredField("directedNeighbours");
        directField2.setAccessible(true);


        assertTrue("setNodeNeighbours does not set directed neighbouring nodes correctly #1", directField0.get(node0).equals(correctResult0));
        assertTrue("setNodeNeighbours does not set directed neighbouring nodes correctly #2", directField1.get(node1).equals(correctResult1));
        assertTrue("setNodeNeighbours does not set directed neighbouring nodes correctly #3", directField2.get(node2).equals(correctResult2));
    }

    @Test
    public void test_setNodeNeighbours_setUndirectedNeighbouringNodesCorrectly() throws NoSuchFieldException, IllegalAccessException {
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node1", new ArrayList<>());
        node node1 = new node(1, "node2", new ArrayList<>());
        node node2 = new node(2, "node3", new ArrayList<>());
        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);

        edges.add(new edge(0, 1, "testlabel", "testpostlabel"));
        edges.add(new edge(1, 2, "testlabel1", "testpostlabel1"));
        edges.add(new edge(0, 2, "testlabel2", "testpostlabel2"));

        graph g = new graph("11111", "testsource", "node1 node2 node3 node4", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        ArrayList<node> correctResult0 = new ArrayList<>();
        ArrayList<node> correctResult1 = new ArrayList<>();
        ArrayList<node> correctResult2 = new ArrayList<>();

        correctResult1.add(node0);
        correctResult2.add(node1);
        correctResult2.add(node0);

        g.setNodeNeighbours();

        //Get fields without using getter
        final Field nodeField0 = nodes.get(0).getClass().getDeclaredField("undirectedNeighbours");
        nodeField0.setAccessible(true);
        final Field nodeField1 = nodes.get(1).getClass().getDeclaredField("undirectedNeighbours");
        nodeField1.setAccessible(true);
        final Field nodeField2 = nodes.get(2).getClass().getDeclaredField("undirectedNeighbours");
        nodeField2.setAccessible(true);


        assertTrue("setNodeNeighbours does not set undirected neighbouring nodes correctly #1", nodeField0.get(node0).equals(correctResult0));
        assertTrue("setNodeNeighbours does not set undirected neighbouring nodes correctly #2", nodeField1.get(node1).equals(correctResult1));
        assertTrue("setNodeNeighbours does not set undirected neighbouring nodes correctly #3", nodeField2.get(node2).equals(correctResult2));
    }

    @Test
    public void test_setNodeNeighbours_setEdgesCorrectly() throws NoSuchFieldException, IllegalAccessException {
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node1", new ArrayList<>());
        node node1 = new node(1, "node2", new ArrayList<>());
        node node2 = new node(2, "node3", new ArrayList<>());
        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);


        edge edge0 = new edge(0, 1, "testlabel", "testpostlabel");
        edge edge1 = new edge(1, 2, "testlabel1", "testpostlabel1");
        edge edge2 = new edge(0, 2, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);

        graph g = new graph("11111", "testsource", "node1 node2 node3 node4", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        ArrayList<edge> correctResult0 = new ArrayList<>();
        ArrayList<edge> correctResult1 = new ArrayList<>();
        ArrayList<edge> correctResult2 = new ArrayList<>();

        correctResult0.add(edge0);
        correctResult0.add(edge2);
        correctResult1.add(edge1);

        g.setNodeNeighbours();

        //Get fields without using getter
        final Field edgeField0 = nodes.get(0).getClass().getDeclaredField("edgeNeighbours");
        edgeField0.setAccessible(true);
        final Field edgeField1 = nodes.get(1).getClass().getDeclaredField("edgeNeighbours");
        edgeField1.setAccessible(true);
        final Field edgeField2 = nodes.get(2).getClass().getDeclaredField("edgeNeighbours");
        edgeField2.setAccessible(true);


        assertTrue("setNodeNeighbours does not set edges correctly #1", edgeField0.get(node0).equals(correctResult0));
        assertTrue("setNodeNeighbours does not set edges correctly #2", edgeField1.get(node1).equals(correctResult1));
        assertTrue("setNodeNeighbours does not set edges correctly #3", edgeField2.get(node2).equals(correctResult2));
    }

    @Test
    public void test_setNodeNeighbours_NoEdgesInGraph() throws NoSuchFieldException, IllegalAccessException {
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node1", new ArrayList<>());
        node node1 = new node(1, "node2", new ArrayList<>());
        node node2 = new node(2, "node3", new ArrayList<>());
        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);

        graph g = new graph("11111", "testsource", "node1 node2 node3 node4", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        ArrayList<edge> emptyArray = new ArrayList<>();

        g.setNodeNeighbours();

        //Get fields without using getter
        final Field nodeField0 = nodes.get(0).getClass().getDeclaredField("edgeNeighbours");
        nodeField0.setAccessible(true);
        final Field nodeField1 = nodes.get(1).getClass().getDeclaredField("edgeNeighbours");
        nodeField1.setAccessible(true);
        final Field nodeField2 = nodes.get(2).getClass().getDeclaredField("edgeNeighbours");
        nodeField2.setAccessible(true);

        final Field edgeField0 = nodes.get(0).getClass().getDeclaredField("edgeNeighbours");
        edgeField0.setAccessible(true);
        final Field edgeField1 = nodes.get(1).getClass().getDeclaredField("edgeNeighbours");
        edgeField1.setAccessible(true);
        final Field edgeField2 = nodes.get(2).getClass().getDeclaredField("edgeNeighbours");
        edgeField2.setAccessible(true);


        assertTrue("setNodeNeighbours does not handle edgeless graphs correctly.", edgeField0.get(node0).equals(emptyArray));
        assertTrue("setNodeNeighbours does not handle edgeless graphs correctly.", edgeField1.get(node1).equals(emptyArray));
        assertTrue("setNodeNeighbours does not handle edgeless graphs correctly.", edgeField2.get(node2).equals(emptyArray));
        assertTrue("setNodeNeighbours does not handle edgeless graphs correctly.", nodeField0.get(node0).equals(emptyArray));
        assertTrue("setNodeNeighbours does not handle edgeless graphs correctly.", nodeField1.get(node1).equals(emptyArray));
        assertTrue("setNodeNeighbours does not handle edgeless graphs correctly.", nodeField2.get(node2).equals(emptyArray));
    }

    @Test
    public void test_setNodeNeighbours_NodesAlreadyHaveNodeNeighbours() throws NoSuchFieldException, IllegalAccessException {
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node1", new ArrayList<>());
        node node1 = new node(1, "node2", new ArrayList<>());
        node node2 = new node(2, "node3", new ArrayList<>());
        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);


        edge edge0 = new edge(0, 1, "testlabel", "testpostlabel");
        edge edge1 = new edge(1, 2, "testlabel1", "testpostlabel1");
        edge edge2 = new edge(2, 0, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);

        graph g = new graph("11111", "testsource", "node1 node2 node3 node4", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        ArrayList<node> directedNeighbours = new ArrayList<>();
        ArrayList<node> undirectedNeighbours = new ArrayList<>();
        ArrayList<edge> edgeNeighbours = new ArrayList<>();

        directedNeighbours.add(node1);
        undirectedNeighbours.add(node2);
        edgeNeighbours.add(edge0);

        //Setting without using setter methods
        final Field directField = node0.getClass().getDeclaredField("directedNeighbours");
        directField.setAccessible(true);
        directField.set(node0, directedNeighbours);
        final Field undirectField = node0.getClass().getDeclaredField("undirectedNeighbours");
        undirectField.setAccessible(true);
        undirectField.set(node0, undirectedNeighbours);
        final Field edgeField = node0.getClass().getDeclaredField("edgeNeighbours");
        edgeField.setAccessible(true);
        edgeField.set(node0, edgeNeighbours);

        g.setNodeNeighbours();

        assertTrue("setNodeNeighbours re-assigns directed neighbouring nodes that are already assigned #1.", directField.get(node0).equals(directedNeighbours));
        assertTrue("setNodeNeighbours re-assigns undirected neighbouring nodes that are already assigned #1.", undirectField.get(node0).equals(undirectedNeighbours));
        assertTrue("setNodeNeighbours re-assigns neighbouring nodes that are already assigned #2.", edgeField.get(node0).equals(edgeNeighbours));
    }

    @Test
    public void test_directedLongestPaths_DirectedSingleLongestPathFromStartNodeInAcyclicGraph() throws NoSuchFieldException, IllegalAccessException {

        //Creating the nodes and edges for the graph
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);

        edge edge0 = new edge(0, 1, "testlabel", "testpostlabel");
        edge edge1 = new edge(0, 2, "testlabel1", "testpostlabel1");
        edge edge2 = new edge(2, 3, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        //Creating the array of node neighbours for the nodes in the graph.
        ArrayList<node> nodeNeighbours0 = new ArrayList<>();
        ArrayList<node> nodeNeighbours2 = new ArrayList<>();

        nodeNeighbours0.add(node1);
        nodeNeighbours0.add(node2);
        nodeNeighbours2.add(node3);

        //Setting node neighbours without using setNodeNeighbours method.
        final Field nodeField0 = node0.getClass().getDeclaredField("directedNeighbours");
        nodeField0.setAccessible(true);
        nodeField0.set(node0, nodeNeighbours0);
        final Field nodeField2 = node2.getClass().getDeclaredField("directedNeighbours");
        nodeField2.setAccessible(true);
        nodeField2.set(node2, nodeNeighbours2);

        //Expected results for longest path for each node as the start node.
        ArrayList<ArrayList<Integer>> correctResult0 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> correctResult1 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> correctResult2 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> correctResult3 = new ArrayList<>();

        correctResult0.add(new ArrayList<Integer>());
        correctResult0.get(0).add(3);
        correctResult0.get(0).add(2);
        correctResult0.get(0).add(0);

        correctResult2.add(new ArrayList<Integer>());
        correctResult2.get(0).add(3);
        correctResult2.get(0).add(2);


        assertTrue("directedLongestPaths method does not correctly finds the longest directed path for node 0.", g.directedLongestPaths(0).equals(correctResult0));
        assertTrue("directedLongestPaths method does not correctly finds the longest directed path for node 1.", g.directedLongestPaths(1).equals(correctResult1));
        assertTrue("directedLongestPaths method does not correctly finds the longest directed path for node 2.", g.directedLongestPaths(2).equals(correctResult2));
        assertTrue("directedLongestPaths method does not correctly finds the longest directed path for node 3.", g.directedLongestPaths(3).equals(correctResult3));

    }


    @Test
    public void test_directedLongestPaths_DirectedMultipleLongestPathFromStartNodeInAcyclicGraph() throws NoSuchFieldException, IllegalAccessException {

        //Creating the nodes and edges for the graph
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);
        nodes.add(node5);

        edge edge0 = new edge(0, 1, "testlabel", "testpostlabel");
        edge edge1 = new edge(1, 2, "testlabel", "testpostlabel");
        edge edge2 = new edge(0, 3, "testlabel", "testpostlabel");
        edge edge3 = new edge(3, 4, "testlabel", "testpostlabel");
        edge edge4 = new edge(0, 5, "testlabel", "testpostlabel");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(edge4);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        //Creating the array of node neighbours for the nodes in the graph.
        ArrayList<node> nodeNeighbours0 = new ArrayList<>();
        ArrayList<node> nodeNeighbours1 = new ArrayList<>();
        ArrayList<node> nodeNeighbours2 = new ArrayList<>();
        ArrayList<node> nodeNeighbours3 = new ArrayList<>();

        nodeNeighbours0.add(node1);
        nodeNeighbours0.add(node3);
        nodeNeighbours0.add(node5);
        nodeNeighbours1.add(node2);
        nodeNeighbours3.add(node4);

        //Setting node neighbours without using setNodeNeighbours method.
        final Field nodeField0 = node0.getClass().getDeclaredField("directedNeighbours");
        nodeField0.setAccessible(true);
        nodeField0.set(node0, nodeNeighbours0);
        final Field nodeField1 = node1.getClass().getDeclaredField("directedNeighbours");
        nodeField1.setAccessible(true);
        nodeField1.set(node1, nodeNeighbours1);
        final Field nodeField3 = node3.getClass().getDeclaredField("directedNeighbours");
        nodeField3.setAccessible(true);
        nodeField3.set(node3, nodeNeighbours3);

        //Expected results for longest path for each node as the start node.
        ArrayList<ArrayList<Integer>> correctResult0 = new ArrayList<>();
        correctResult0.add(new ArrayList<Integer>());
        correctResult0.get(0).add(2);
        correctResult0.get(0).add(1);
        correctResult0.get(0).add(0);
        correctResult0.add(new ArrayList<Integer>());
        correctResult0.get(1).add(4);
        correctResult0.get(1).add(3);
        correctResult0.get(1).add(0);


        assertTrue("directedLongestPaths method does not correctly find multiple longest directed paths from a start node.", g.directedLongestPaths(0).equals(correctResult0));

    }

    /*
    @Test
    public void test_directedLongestPaths_DirectedSingleLongestPathFromStartNodeInCyclicGraph() throws NoSuchFieldException, IllegalAccessException{

        //Creating the nodes and edges for the graph
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);

        edge edge0 =new edge(0, 1, "testlabel", "testpostlabel");
        edge edge1 =new edge(0, 2, "testlabel1", "testpostlabel1");
        edge edge2 =new edge(2, 3, "testlabel2", "testpostlabel2");
        edge edge3 =new edge(3, 0, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        //Creating the array of node neighbours for the nodes in the graph.
        ArrayList<node> nodeNeighbours0 = new ArrayList<>();
        ArrayList<node> nodeNeighbours1 = new ArrayList<>();
        ArrayList<node> nodeNeighbours2 = new ArrayList<>();
        ArrayList<node> nodeNeighbours3 = new ArrayList<>();

        nodeNeighbours0.add(node1);
        nodeNeighbours0.add(node2);
        nodeNeighbours2.add(node3);
        nodeNeighbours3.add(node0);

        //Setting node neighbours without using setNodeNeighbours method.
        final Field nodeField0 = node0.getClass().getDeclaredField("directedNeighbours");
        nodeField0.setAccessible(true);
        nodeField0.set(node0, nodeNeighbours0);
        final Field nodeField1 = node1.getClass().getDeclaredField("directedNeighbours");
        nodeField1.setAccessible(true);
        nodeField1.set(node1, nodeNeighbours1);
        final Field nodeField2 = node2.getClass().getDeclaredField("directedNeighbours");
        nodeField2.setAccessible(true);
        nodeField2.set(node2, nodeNeighbours2);
        final Field nodeField3 = node3.getClass().getDeclaredField("directedNeighbours");
        nodeField3.setAccessible(true);
        nodeField3.set(node3, nodeNeighbours3);

        //Expected results for longest path for each node as the start node.
        ArrayList<ArrayList<Integer>> correctResult0 = new ArrayList<>();

        //Cyclic path
        correctResult0.add(new ArrayList<Integer>());
        correctResult0.get(0).add(0);
        correctResult0.get(0).add(3);
        correctResult0.get(0).add(2);
        correctResult0.get(0).add(0);

        assertTrue("directedLongestPaths method does not correctly find the longest directed path from a start node in a cyclic graph.", g.directedLongestPaths(0).equals(correctResult0));

    }
    */

    /*
    @Test
    public void test_directedLongestPaths_DirectedMultipleLongestPathFromStartNodeInCyclicGraph() throws NoSuchFieldException, IllegalAccessException{

        //Creating the nodes and edges for the graph
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();
        ArrayList<token> tokens = new ArrayList<>();


        ArrayList<anchors> anch1 = new ArrayList<anchors>();
        anch1.add(new anchors(0, 0));
        nodes.add(new node(0, "node" + (0 + 1), anch1));

        ArrayList<anchors> anch2 = new ArrayList<anchors>();
        anch2.add(new anchors(1, 1));
        nodes.add(new node(1, "node" + (1 + 1), anch2));

        ArrayList<anchors> anch3 = new ArrayList<anchors>();
        anch3.add(new anchors(2, 2));
        nodes.add(new node(2, "node" + (2 + 1), anch3));

        ArrayList<anchors> anch4 = new ArrayList<anchors>();
        anch4.add(new anchors(3, 3));
        nodes.add(new node(3, "node" + (3 + 1), anch4));

        //Setting node neighbours without using setNodeNeighbours method.
        final Field nodeField0 = node0.getClass().getDeclaredField("directedNeighbours");
        nodeField0.setAccessible(true);
        nodeField0.set(node0, nodeNeighbours0);
        final Field nodeField1 = node1.getClass().getDeclaredField("directedNeighbours");
        nodeField1.setAccessible(true);
        nodeField1.set(node1, nodeNeighbours1);
        final Field nodeField2 = node2.getClass().getDeclaredField("directedNeighbours");
        nodeField2.setAccessible(true);
        nodeField2.set(node2, nodeNeighbours2);
        final Field nodeField3 = node3.getClass().getDeclaredField("directedNeighbours");
        nodeField3.setAccessible(true);
        nodeField3.set(node3, nodeNeighbours3);

        edges.add(new edge(0, 1, "testlabel", "testpostlabel"));
        edges.add(new edge(1, 3, "testlabel1", "testpostlabel1"));
        edges.add(new edge(2, 4, "testlabel2", "testpostlabel2"));

        graph g = new graph("11111", "testsource", "node1 node2 node3 node4", nodes, tokens, edges, new ArrayList<Integer>());

        assertTrue("directedLongestPaths method does not correctly find multiple longest directed paths from a start node in a cyclic graph.", g.directedLongestPaths(2).equals(correctResult2));

    }
     */

    @Test
    public void test_BFS_UndirectedSingleLongestPathFromStartNodeInAcyclicGraph() throws NoSuchFieldException, IllegalAccessException {

        edges.clear();
        edges.add(new edge(0, 2, "testlabel", "testpostlabel"));
        edges.add(new edge(1, 3, "testlabel1", "testpostlabel1"));

        assertFalse("isPlanar Correctly identifies planar and non-planar graphs", g.isPlanar());

        edge edge0 = new edge(0, 1, "testlabel", "testpostlabel");
        edge edge1 = new edge(2, 0, "testlabel1", "testpostlabel1");
        edge edge2 = new edge(2, 3, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        //Creating the array of directed and undirected node neighbours for the nodes in the graph.
        ArrayList<node> directedNeighbours0 = new ArrayList<>();
        ArrayList<node> directedNeighbours2 = new ArrayList<>();

        directedNeighbours0.add(node1);
        directedNeighbours0.add(node2);
        directedNeighbours2.add(node3);

        ArrayList<node> undirectedNeighbours1 = new ArrayList<>();
        ArrayList<node> undirectedNeighbours2 = new ArrayList<>();
        ArrayList<node> undirectedNeighbours3 = new ArrayList<>();

        undirectedNeighbours1.add(node0);
        undirectedNeighbours2.add(node0);
        undirectedNeighbours3.add(node2);

        //Setting node neighbours without using setNodeNeighbours method.
        final Field directedField0 = node0.getClass().getDeclaredField("directedNeighbours");
        directedField0.setAccessible(true);
        directedField0.set(node0, directedNeighbours0);

        final Field directedField2 = node2.getClass().getDeclaredField("directedNeighbours");
        directedField2.setAccessible(true);
        directedField2.set(node2, directedNeighbours2);

        final Field undirectedField1 = node1.getClass().getDeclaredField("undirectedNeighbours");
        undirectedField1.setAccessible(true);
        undirectedField1.set(node1, undirectedNeighbours1);

        final Field undirectedField2 = node2.getClass().getDeclaredField("undirectedNeighbours");
        undirectedField2.setAccessible(true);
        undirectedField2.set(node2, undirectedNeighbours2);

        final Field undirectedField3 = node3.getClass().getDeclaredField("undirectedNeighbours");
        undirectedField3.setAccessible(true);
        undirectedField3.set(node3, undirectedNeighbours3);

        //Expected results for longest path for each node as the start node.
        ArrayList<ArrayList<Integer>> correctResult0 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> correctResult1 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> correctResult2 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> correctResult3 = new ArrayList<>();

        correctResult0.add(new ArrayList<Integer>());
        correctResult0.get(0).add(3);
        correctResult0.get(0).add(2);
        correctResult0.get(0).add(0);

        correctResult1.add(new ArrayList<Integer>());
        correctResult1.get(0).add(3);
        correctResult1.get(0).add(2);
        correctResult1.get(0).add(0);
        correctResult1.get(0).add(1);

        correctResult2.add(new ArrayList<Integer>());
        correctResult2.get(0).add(1);
        correctResult2.get(0).add(0);
        correctResult2.get(0).add(2);

        correctResult3.add(new ArrayList<Integer>());
        correctResult3.get(0).add(1);
        correctResult3.get(0).add(0);
        correctResult3.get(0).add(2);
        correctResult3.get(0).add(3);

        assertTrue("BFS longest path algorithm does not correctly finds the longest undirected path for node 0.", g.BFS(0).equals(correctResult0));
        assertTrue("BFS longest path algorithm does not correctly finds the longest undirected path for node 1.", g.BFS(1).equals(correctResult1));
        assertTrue("BFS longest path algorithm does not correctly finds the longest undirected path for node 2.", g.BFS(2).equals(correctResult2));
        assertTrue("BFS longest path algorithm does not correctly finds the longest undirected path for node 3.", g.BFS(3).equals(correctResult3));

    }

    @Test
    public void test_BFS_UndirectedMultipleLongestPathFromStartNodeInAcyclicGraph() throws NoSuchFieldException, IllegalAccessException {

        //Creating the nodes and edges for the graph
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();
        ArrayList<token> tokens = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);
        nodes.add(node5);

        edge edge0 = new edge(0, 1, "testlabel", "testpostlabel");
        edge edge1 = new edge(1, 2, "testlabel", "testpostlabel");
        edge edge2 = new edge(3, 0, "testlabel", "testpostlabel");
        edge edge3 = new edge(3, 4, "testlabel", "testpostlabel");
        edge edge4 = new edge(0, 5, "testlabel", "testpostlabel");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(edge4);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        //Creating the array of node neighbours for the nodes in the graph.
        ArrayList<node> directNeighbours0 = new ArrayList<>();
        ArrayList<node> directNeighbours1 = new ArrayList<>();
        ArrayList<node> directNeighbours3 = new ArrayList<>();

        directNeighbours0.add(node1);
        directNeighbours0.add(node3);
        directNeighbours0.add(node5);
        directNeighbours1.add(node2);
        directNeighbours3.add(node4);

        ArrayList<node> undirectNeighbours1 = new ArrayList<>();
        ArrayList<node> undirectNeighbours2 = new ArrayList<>();
        ArrayList<node> undirectNeighbours5 = new ArrayList<>();
        ArrayList<node> undirectNeighbours3 = new ArrayList<>();
        ArrayList<node> undirectNeighbours4 = new ArrayList<>();

        undirectNeighbours1.add(node0);
        undirectNeighbours2.add(node1);
        undirectNeighbours5.add(node0);
        undirectNeighbours3.add(node0);
        undirectNeighbours4.add(node3);

        //Setting node neighbours without using setNodeNeighbours method.
        final Field directField0 = node0.getClass().getDeclaredField("directedNeighbours");
        directField0.setAccessible(true);
        directField0.set(node0, directNeighbours0);
        final Field directField1 = node1.getClass().getDeclaredField("directedNeighbours");
        directField1.setAccessible(true);
        directField1.set(node1, directNeighbours1);
        final Field directField3 = node3.getClass().getDeclaredField("directedNeighbours");
        directField3.setAccessible(true);
        directField3.set(node3, directNeighbours3);
        final Field undirectField1 = node1.getClass().getDeclaredField("undirectedNeighbours");
        undirectField1.setAccessible(true);
        undirectField1.set(node1, undirectNeighbours1);
        final Field undirectField2 = node2.getClass().getDeclaredField("undirectedNeighbours");
        undirectField2.setAccessible(true);
        undirectField2.set(node2, undirectNeighbours2);
        final Field undirectField3 = node3.getClass().getDeclaredField("undirectedNeighbours");
        undirectField3.setAccessible(true);
        undirectField3.set(node3, undirectNeighbours3);
        final Field undirectField4 = node4.getClass().getDeclaredField("undirectedNeighbours");
        undirectField4.setAccessible(true);
        undirectField4.set(node4, undirectNeighbours4);
        final Field undirectField5 = node5.getClass().getDeclaredField("undirectedNeighbours");
        undirectField5.setAccessible(true);
        undirectField5.set(node5, undirectNeighbours5);

        //Expected results for longest path for each node as the start node.
        ArrayList<ArrayList<Integer>> correctResult0 = new ArrayList<>();
        correctResult0.add(new ArrayList<Integer>());
        correctResult0.get(0).add(2);
        correctResult0.get(0).add(1);
        correctResult0.get(0).add(0);
        correctResult0.add(new ArrayList<Integer>());
        correctResult0.get(1).add(4);
        correctResult0.get(1).add(3);
        correctResult0.get(1).add(0);


        assertTrue("BFS method does not correctly find multiple longest undirected paths from a start node.", g.BFS(0).equals(correctResult0));

    }

    /*
    @Test
    public void test_BFS_UndirectedMultipleLongestPathFromStartNodeInCyclicGraph() throws NoSuchFieldException, IllegalAccessException {

        //Creating the nodes and edges for the graph
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);
        edge edge0 = new edge(0, 1, "testlabel", "testpostlabel");
        edge edge1 = new edge(1, 2, "testlabel", "testpostlabel");
        edge edge2 = new edge(0, 2, "testlabel", "testpostlabel");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        //Creating the array of node neighbours for the nodes in the graph.
        ArrayList<node> directNeighbours0 = new ArrayList<>();
        ArrayList<node> directNeighbours1 = new ArrayList<>();

        directNeighbours0.add(node1);
        directNeighbours0.add(node2);
        directNeighbours1.add(node2);

        ArrayList<node> undirectNeighbours1 = new ArrayList<>();
        ArrayList<node> undirectNeighbours2 = new ArrayList<>();

        undirectNeighbours1.add(node0);
        undirectNeighbours2.add(node1);
        undirectNeighbours2.add(node0);

        //Setting node neighbours without using setNodeNeighbours method.
        final Field directField0 = node0.getClass().getDeclaredField("directedNeighbours");
        directField0.setAccessible(true);
        directField0.set(node0, directNeighbours0);
        final Field directField1 = node1.getClass().getDeclaredField("directedNeighbours");
        directField1.setAccessible(true);
        directField1.set(node1, directNeighbours1);
        final Field undirectField1 = node1.getClass().getDeclaredField("undirectedNeighbours");
        undirectField1.setAccessible(true);
        undirectField1.set(node1, undirectNeighbours1);
        final Field undirectField2 = node2.getClass().getDeclaredField("undirectedNeighbours");
        undirectField2.setAccessible(true);
        undirectField2.set(node2, undirectNeighbours2);

        //Expected results for longest path for each node as the start node.
        ArrayList<ArrayList<Integer>> correctResult0 = new ArrayList<>();
        correctResult0.add(new ArrayList<Integer>());
        correctResult0.get(0).add(2);
        correctResult0.get(0).add(1);
        correctResult0.get(0).add(0);

        assertTrue("BFS method does not correctly find a single longest undirected path from a start node in an acyclic graph.", g.BFS(0).equals(correctResult0));
    }
     */

    /*
    @Test
    public void test_BFS_UndirectedMultipleLongestPathFromStartNodeInCyclicGraph() throws NoSuchFieldException, IllegalAccessException {

        //Creating the nodes and edges for the graph
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);
        nodes.add(node5);

        edge edge0 = new edge(0, 1, "testlabel", "testpostlabel");
        edge edge1 = new edge(1, 2, "testlabel", "testpostlabel");
        edge edge2 = new edge(3, 0, "testlabel", "testpostlabel");
        edge edge3 = new edge(2, 4, "testlabel", "testpostlabel");
        edge edge4 = new edge(0, 5, "testlabel", "testpostlabel");
        edge edge5 = new edge(3, 5, "testlabel", "testpostlabel");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(edge4);
        edges.add(edge5);

        ArrayList<anchors> anch5 = new ArrayList<anchors>();
        anch5.add(new anchors(4, 4));
        nodes.add(new node(4, "node" + (4 + 1), anch5));

        //Creating the array of node neighbours for the nodes in the graph.
        ArrayList<node> directNeighbours0 = new ArrayList<>();
        ArrayList<node> directNeighbours1 = new ArrayList<>();
        ArrayList<node> directNeighbours2 = new ArrayList<>();
        ArrayList<node> directNeighbours3 = new ArrayList<>();

        directNeighbours0.add(node1);
        directNeighbours0.add(node3);
        directNeighbours0.add(node5);
        directNeighbours1.add(node2);
        directNeighbours2.add(node4);
        directNeighbours3.add(node5);

        ArrayList<node> undirectNeighbours1 = new ArrayList<>();
        ArrayList<node> undirectNeighbours2 = new ArrayList<>();
        ArrayList<node> undirectNeighbours5 = new ArrayList<>();
        ArrayList<node> undirectNeighbours3 = new ArrayList<>();
        ArrayList<node> undirectNeighbours4 = new ArrayList<>();

        undirectNeighbours1.add(node0);
        undirectNeighbours2.add(node1);
        undirectNeighbours5.add(node0);
        undirectNeighbours5.add(node3);
        undirectNeighbours3.add(node0);
        undirectNeighbours4.add(node2);

        //Setting node neighbours without using setNodeNeighbours method.
        final Field directField0 = node0.getClass().getDeclaredField("directedNeighbours");
        directField0.setAccessible(true);
        directField0.set(node0, directNeighbours0);
        final Field directField1 = node1.getClass().getDeclaredField("directedNeighbours");
        directField1.setAccessible(true);
        directField1.set(node1, directNeighbours1);
        final Field directField2 = node2.getClass().getDeclaredField("directedNeighbours");
        directField2.setAccessible(true);
        directField2.set(node2, directNeighbours2);
        final Field directField3 = node3.getClass().getDeclaredField("directedNeighbours");
        directField3.setAccessible(true);
        directField3.set(node3, directNeighbours3);
        final Field undirectField1 = node1.getClass().getDeclaredField("undirectedNeighbours");
        undirectField1.setAccessible(true);
        undirectField1.set(node1, undirectNeighbours1);
        final Field undirectField2 = node2.getClass().getDeclaredField("undirectedNeighbours");
        undirectField2.setAccessible(true);
        undirectField2.set(node2, undirectNeighbours2);
        final Field undirectField3 = node3.getClass().getDeclaredField("undirectedNeighbours");
        undirectField3.setAccessible(true);
        undirectField3.set(node3, undirectNeighbours3);
        final Field undirectField4 = node4.getClass().getDeclaredField("undirectedNeighbours");
        undirectField4.setAccessible(true);
        undirectField4.set(node4, undirectNeighbours4);
        final Field undirectField5 = node5.getClass().getDeclaredField("undirectedNeighbours");
        undirectField5.setAccessible(true);
        undirectField5.set(node5, undirectNeighbours5);

        //Expected results for longest path for each node as the start node.
        ArrayList<ArrayList<Integer>> correctResult0 = new ArrayList<>();
        correctResult0.add(new ArrayList<Integer>());
        correctResult0.get(0).add(4);
        correctResult0.get(0).add(2);
        correctResult0.get(0).add(1);
        correctResult0.get(0).add(0);
        correctResult0.add(new ArrayList<Integer>());
        correctResult0.get(1).add(0);
        correctResult0.get(1).add(5);
        correctResult0.get(1).add(3);
        correctResult0.get(1).add(0);

        assertTrue("BFS method does not correctly find multiple longest undirected paths from a start node in an acyclic graph.", g.BFS(0).equals(correctResult0));
    }
    */

    @Test
    public void test_findLongest_DirectedSingleLongestPath() throws NoSuchFieldException, IllegalAccessException {

        //Creating the nodes and edges for the graph
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);

        edge edge0 = new edge(1, 0, "testlabel", "testpostlabel");
        edge edge1 = new edge(1, 2, "testlabel1", "testpostlabel1");
        edge edge2 = new edge(2, 3, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        graph g = new graph("11111", "testsource", "node1 node2 node3 node4", nodes, tokens, edges, new ArrayList<Integer>());

        //Expected result for longest path.
        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(0).add(1);
        correctResult.get(0).add(2);
        correctResult.get(0).add(3);

        assertTrue("findLongest path algorithm does not correctly find a single longest directed path in a graph.", g.findLongest(true).equals(correctResult));

    }

    @Test
    public void test_findLongest_DirectedMultipleLongestPathsFromSingleStartNode() throws NoSuchFieldException, IllegalAccessException {
        //Creating the nodes and edges for the graph
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();
        ArrayList<token> tokens = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);

        edge edge0 = new edge(1, 0, "testlabel", "testpostlabel");
        edge edge1 = new edge(1, 2, "testlabel1", "testpostlabel1");
        edge edge2 = new edge(2, 3, "testlabel2", "testpostlabel2");
        edge edge3 = new edge(0, 4, "testlabel3", "testpostlabel3");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());


        //Expected result for longest path.
        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(0).add(1);
        correctResult.get(0).add(2);
        correctResult.get(0).add(3);
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(1).add(1);
        correctResult.get(1).add(0);
        correctResult.get(1).add(4);

        assertTrue("findLongest path algorithm does not correctly find multiple longest directed paths from a single node in a graph.", g.findLongest(true).equals(correctResult));
    }

    @Test
    public void test_findLongest_DirectedMultipleLongestPathsFromDifferentStartNodes() throws NoSuchFieldException, IllegalAccessException {
        //Creating the nodes and edges for the graph
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);

        edge edge0 = new edge(0, 2, "testlabel", "testpostlabel");
        edge edge1 = new edge(1, 2, "testlabel1", "testpostlabel1");
        edge edge2 = new edge(2, 3, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());


        //Expected result for longest path.
        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(0).add(0);
        correctResult.get(0).add(2);
        correctResult.get(0).add(3);
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(1).add(1);
        correctResult.get(1).add(2);
        correctResult.get(1).add(3);

        assertTrue("findLongest path algorithm does not correctly find multiple longest directed paths from different start nodes in a graph.", g.findLongest(true).equals(correctResult));
    }

    @Test
    public void test_findLongest_DirectedMultipleLongestPathsFromSameAndDifferentStartNodes() throws NoSuchFieldException, IllegalAccessException {
        //Creating the nodes and edges for the graph
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);
        nodes.add(node5);

        edge edge0 = new edge(0, 1, "testlabel", "testpostlabel");
        edge edge1 = new edge(0, 2, "testlabel1", "testpostlabel1");
        edge edge2 = new edge(2, 3, "testlabel2", "testpostlabel2");
        edge edge3 = new edge(5, 2, "testlabel2", "testpostlabel2");
        edge edge4 = new edge(1, 4, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(edge4);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        //Expected results for longest path for each node as the start node.
        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();

        //Cyclic path
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(0).add(0);
        correctResult.get(0).add(2);
        correctResult.get(0).add(3);
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(1).add(0);
        correctResult.get(1).add(1);
        correctResult.get(1).add(4);
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(2).add(5);
        correctResult.get(2).add(2);
        correctResult.get(2).add(3);

        assertTrue("findLongest path algorithm does not correctly find multiple longest directed paths from the same and different start nodes in a graph.", g.findLongest(true).equals(correctResult));
    }

    @Test
    public void test_findLongest_UndirectedSingleLongestPath() throws NoSuchFieldException, IllegalAccessException {

        //Creating the nodes and edges for the graph
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);

        edge edge0 = new edge(1, 0, "testlabel", "testpostlabel");
        edge edge1 = new edge(1, 2, "testlabel1", "testpostlabel1");
        edge edge2 = new edge(3, 2, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());


        //Expected result for longest path.
        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(0).add(3);
        correctResult.get(0).add(2);
        correctResult.get(0).add(1);
        correctResult.get(0).add(0);

        assertTrue("findLongest path algorithm does not correctly find a single longest undirected path in a graph.", g.findLongest(false).equals(correctResult));

    }

    @Test
    public void test_findLongest_UndirectedMultipleLongestPathsFromSingleStartNode() throws NoSuchFieldException, IllegalAccessException {
        //Creating the nodes and edges for the graph
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        node node6 = new node(6, "node6", new ArrayList<>());
        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);
        nodes.add(node5);
        nodes.add(node6);

        edge edge0 = new edge(0, 1, "testlabel", "testpostlabel");
        edge edge1 = new edge(1, 2, "testlabel1", "testpostlabel1");
        edge edge2 = new edge(2, 3, "testlabel2", "testpostlabel2");
        edge edge3 = new edge(0, 4, "testlabel3", "testpostlabel3");
        edge edge4 = new edge(4, 5, "testlabel3", "testpostlabel3");
        edge edge5 = new edge(4, 6, "testlabel3", "testpostlabel3");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(edge4);
        edges.add(edge5);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());


        //Expected result for longest path.
        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(0).add(3);
        correctResult.get(0).add(2);
        correctResult.get(0).add(1);
        correctResult.get(0).add(0);
        correctResult.get(0).add(4);
        correctResult.get(0).add(5);
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(1).add(3);
        correctResult.get(1).add(2);
        correctResult.get(1).add(1);
        correctResult.get(1).add(0);
        correctResult.get(1).add(4);
        correctResult.get(1).add(6);

        assertTrue("findLongest path algorithm does not correctly find multiple longest undirected paths from a single node in a graph.", g.findLongest(false).equals(correctResult));
    }

    @Test
    public void test_findLongest_UndirectedMultipleLongestPathsFromDifferentStartNodes() throws NoSuchFieldException, IllegalAccessException {
        //Creating the nodes and edges for the graph
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node4", new ArrayList<>());
        node node3 = new node(3, "node5", new ArrayList<>());
        node node4 = new node(4, "node6", new ArrayList<>());
        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);

        edge edge0 = new edge(0, 1, "testlabel", "testpostlabel");
        edge edge3 = new edge(0, 2, "testlabel3", "testpostlabel3");
        edge edge4 = new edge(2, 3, "testlabel3", "testpostlabel3");
        edge edge5 = new edge(2, 4, "testlabel3", "testpostlabel3");
        edges.add(edge0);
        edges.add(edge3);
        edges.add(edge4);
        edges.add(edge5);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());


        //Expected result for longest path.
        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(0).add(3);
        correctResult.get(0).add(2);
        correctResult.get(0).add(0);
        correctResult.get(0).add(1);
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(1).add(4);
        correctResult.get(1).add(2);
        correctResult.get(1).add(0);
        correctResult.get(1).add(1);

        assertTrue("findLongest path algorithm does not correctly find multiple longest undirected paths from a single node in a graph.", g.findLongest(false).equals(correctResult));
    }

    @Test
    public void test_findLongest_UndirectedMultipleLongestPathsFromSameAndDifferentStartNodes() throws NoSuchFieldException, IllegalAccessException {
        //Creating the nodes and edges for the graph
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);
        nodes.add(node5);

        edge edge0 = new edge(0, 2, "testlabel", "testpostlabel");
        edge edge1 = new edge(1, 2, "testlabel1", "testpostlabel1");
        edge edge2 = new edge(2, 3, "testlabel2", "testpostlabel2");
        edge edge3 = new edge(4, 3, "testlabel3", "testpostlabel3");
        edge edge4 = new edge(3, 5, "testlabel3", "testpostlabel3");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(edge4);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        //Expected result for longest path.
        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(0).add(4);
        correctResult.get(0).add(3);
        correctResult.get(0).add(2);
        correctResult.get(0).add(0);
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(1).add(4);
        correctResult.get(1).add(3);
        correctResult.get(1).add(2);
        correctResult.get(1).add(1);
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(2).add(5);
        correctResult.get(2).add(3);
        correctResult.get(2).add(2);
        correctResult.get(2).add(0);
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(3).add(5);
        correctResult.get(3).add(3);
        correctResult.get(3).add(2);
        correctResult.get(3).add(1);

        assertTrue("findLongest path algorithm does not correctly find multiple longest undirected paths from a single node in a graph.", g.findLongest(false).equals(correctResult));
    }

    /*
    @Test
    public void test_findLongest_UndirectedMultipleLongestPathsInSymmetricalGraphEdgeCase() throws NoSuchFieldException, IllegalAccessException {
        //Creating the nodes and edges for the graph
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        node node6 = new node(6, "node6", new ArrayList<>());
        nodes.add(node0);
        nodes.add(node1);
        nodes.add(node2);
        nodes.add(node3);
        nodes.add(node4);
        nodes.add(node5);
        nodes.add(node6);

        edge edge0 = new edge(0, 1, "testlabel", "testpostlabel");
        edge edge1 = new edge(2, 0, "testlabel1", "testpostlabel1");
        edge edge2 = new edge(1, 4, "testlabel2", "testpostlabel2");
        edge edge3 = new edge(5, 1, "testlabel3", "testpostlabel3");
        edge edge4 = new edge(2, 3, "testlabel3", "testpostlabel3");
        edge edge5 = new edge(2, 6, "testlabel3", "testpostlabel3");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(edge4);
        edges.add(edge5);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());


        //Expected result for longest path.
        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(0).add(3);
        correctResult.get(0).add(2);
        correctResult.get(0).add(0);
        correctResult.get(0).add(1);
        correctResult.get(0).add(4);
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(1).add(3);
        correctResult.get(1).add(2);
        correctResult.get(1).add(0);
        correctResult.get(1).add(1);
        correctResult.get(1).add(5);
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(2).add(4);
        correctResult.get(2).add(1);
        correctResult.get(2).add(0);
        correctResult.get(2).add(2);
        correctResult.get(2).add(3);
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(3).add(4);
        correctResult.get(3).add(1);
        correctResult.get(3).add(0);
        correctResult.get(3).add(2);
        correctResult.get(3).add(6);

        assertTrue("findLongest path algorithm does not correctly find multiple longest undirected paths in a graph that is perfectly symmetrical around node 0.", g.findLongest(false).equals(correctResult));
    }
    */

    @Test
    public void test_findLongest_OneEdgeInGraph() throws NoSuchFieldException, IllegalAccessException {
        //Creating the nodes and edges for the graph
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        nodes.add(node0);
        nodes.add(node1);

        edge edge0 = new edge(0, 1, "testlabel", "testpostlabel");
        edges.add(edge0);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        //Expected result for longest path.
        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(0).add(0);
        correctResult.get(0).add(1);

        assertTrue("findLongest path algorithm does not correctly return the path of a single edged directed graph.", g.findLongest(true).equals(correctResult));
        assertTrue("findLongest path algorithm does not correctly return the path of a single edged undirected graph.", g.findLongest(false).equals(correctResult));
    }

    @Test
    public void test_findLongest_NoNodes() throws NoSuchFieldException, IllegalAccessException {
        //Creating the nodes and edges for the graph
        ArrayList<node> nodes = new ArrayList<>();

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), new ArrayList<>(), new ArrayList<Integer>());

        //Expected result for longest path.
        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();

        assertTrue("findLongest path algorithm does not correctly return the path in a directed graph with no nodes.", g.findLongest(true).equals(correctResult));
        assertTrue("findLongest path algorithm does not correctly return the path in an undirected graph with no nodes.", g.findLongest(false).equals(correctResult));
    }

    @Test
    public void test_findLongest_NoEdges() throws NoSuchFieldException, IllegalAccessException {
        //Creating the nodes and edges for the graph
        ArrayList<node> nodes = new ArrayList<>();


        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        nodes.add(node0);
        nodes.add(node1);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), new ArrayList<>(), new ArrayList<Integer>());

        //Expected result for longest path.
        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();

        assertTrue("findLongest path algorithm does not correctly return the path in a directed graph with no edges", g.findLongest(true).equals(correctResult));
        assertTrue("findLongest path algorithm does not correctly return the path in an undirected graph with no edges.", g.findLongest(false).equals(correctResult));
    }


    @Test
    public void test_isPlanar_IdentifiesPlanarGraph() {
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();
        ArrayList<token> tokens = new ArrayList<>();


        ArrayList<anchors> anch1 = new ArrayList<anchors>();
        anch1.add(new anchors(0, 0));
        nodes.add(new node(0, "node" + (0 + 1), anch1));

        ArrayList<anchors> anch2 = new ArrayList<anchors>();
        anch2.add(new anchors(1, 1));
        nodes.add(new node(1, "node" + (1 + 1), anch2));

        ArrayList<anchors> anch3 = new ArrayList<anchors>();
        anch3.add(new anchors(2, 2));
        nodes.add(new node(2, "node" + (2 + 1), anch3));

        ArrayList<anchors> anch4 = new ArrayList<anchors>();
        anch4.add(new anchors(3, 3));
        nodes.add(new node(3, "node" + (3 + 1), anch4));

        ArrayList<anchors> anch5 = new ArrayList<anchors>();
        anch5.add(new anchors(4, 4));
        nodes.add(new node(4, "node" + (4 + 1), anch5));

        edges.add(new edge(0, 1, "testlabel", "testpostlabel"));
        edges.add(new edge(1, 3, "testlabel1", "testpostlabel1"));
        edges.add(new edge(2, 4, "testlabel2", "testpostlabel2"));

        graph g = new graph("11111", "testsource", "node1 node2 node3 node4", nodes, tokens, edges, new ArrayList<Integer>());

        assertFalse("isPlanar Correctly identifies planar and non-planar graphs", g.isPlanar());

        edges.clear();
        edges.add(new edge(0, 1, "testlabel", "testpostlabel"));
        edges.add(new edge(1, 2, "testlabel1", "testpostlabel1"));
        edges.add(new edge(2, 3, "testlabel2", "testpostlabel2"));
        edges.add(new edge(3, 4, "testlabel2", "testpostlabel2"));

        assertTrue("isPlanar Correctly identifies planar and non-planar graphs", g.isPlanar());

        edges.clear();
        edges.add(new edge(0, 2, "testlabel", "testpostlabel"));
        edges.add(new edge(1, 3, "testlabel1", "testpostlabel1"));

        assertFalse("isPlanar Correctly identifies planar and non-planar graphs", g.isPlanar());

        edges.clear();
        edges.add(new edge(0, 4, "testlabel", "testpostlabel"));
        edges.add(new edge(1, 4, "testlabel1", "testpostlabel1"));
        edges.add(new edge(2, 4, "testlabel2", "testpostlabel2"));
        edges.add(new edge(3, 4, "testlabel2", "testpostlabel2"));

        assertTrue("isPlanar Correctly identifies planar and non-planar graphs", g.isPlanar());

        edges.clear();
        edges.add(new edge(4, 0, "testlabel", "testpostlabel"));
        edges.add(new edge(1, 0, "testlabel1", "testpostlabel1"));
        edges.add(new edge(2, 0, "testlabel2", "testpostlabel2"));
        edges.add(new edge(3, 0, "testlabel2", "testpostlabel2"));

        assertTrue("isPlanar Correctly identifies planar and non-planar graphs", g.isPlanar());


    }

    @Test
    public void test_isPlanar_HandlesNoEdgesInGraph() {
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();
        ArrayList<token> tokens = new ArrayList<>();


        ArrayList<anchors> anch1 = new ArrayList<anchors>();
        anch1.add(new anchors(0, 0));
        nodes.add(new node(0, "node" + (0 + 1), anch1));

        ArrayList<anchors> anch2 = new ArrayList<anchors>();
        anch2.add(new anchors(1, 1));
        nodes.add(new node(1, "node" + (1 + 1), anch2));

        ArrayList<anchors> anch3 = new ArrayList<anchors>();
        anch3.add(new anchors(2, 2));
        nodes.add(new node(2, "node" + (2 + 1), anch3));

        ArrayList<anchors> anch4 = new ArrayList<anchors>();
        anch4.add(new anchors(3, 3));
        nodes.add(new node(3, "node" + (3 + 1), anch4));

        ArrayList<anchors> anch5 = new ArrayList<anchors>();
        anch5.add(new anchors(4, 4));
        nodes.add(new node(4, "node" + (4 + 1), anch5));


        graph g = new graph("11111", "testsource", "node1 node2 node3 node4", nodes, tokens, edges, new ArrayList<Integer>());

        assertTrue("isPlanar Correctly identifies planar and non-planar graphs", g.isPlanar());

    }

    @Test
    public void test_isPlanar_HandlesNoNodesInGraph() {
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();
        ArrayList<token> tokens = new ArrayList<>();

        graph g = new graph("11111", "testsource", "node1 node2 node3 node4", nodes, tokens, edges, new ArrayList<Integer>());

        assertTrue("isPlanar Correctly identifies planar and non-planar graphs", g.isPlanar());

    }


}
