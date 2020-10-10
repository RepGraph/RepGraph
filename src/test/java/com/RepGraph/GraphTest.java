package com.RepGraph;

import org.junit.Before;
import org.junit.Test;

import javax.validation.constraints.AssertTrue;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

import static junit.framework.TestCase.*;


public class GraphTest {

    @Test
    public void test_Constructor_CreatesAndAssignsMemberVariables() throws NoSuchFieldException, IllegalAccessException {

        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();
        ArrayList<token> tokens = new ArrayList<>();

        nodes.put(0, new node(0, "node1", new ArrayList<anchors>()));
        nodes.put(1, new node(1, "node2", new ArrayList<anchors>()));
        nodes.put(2, new node(2, "node3", new ArrayList<anchors>()));
        nodes.put(3, new node(3, "node4", new ArrayList<anchors>()));

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

        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        nodes.put(0,new node(0, "node1", new ArrayList<anchors>()));

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
    public void test_getTops_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {
        final graph g = new graph();

        ArrayList<Integer> tops = new ArrayList<>();
        tops.add(2);
        tops.add(0);

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("tops");
        field.setAccessible(true);
        field.set(g, tops);

        assertEquals("tops value was not retrieved properly.", g.getTops(), tops);
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

        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        nodes.put(0,new node(0, "node1", new ArrayList<anchors>()));

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
    public void test_setTops_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final graph g = new graph();

        ArrayList<Integer> tops = new ArrayList<>();
        tops.add(0);
        tops.add(2);

        g.setTops(tops);

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("tops");
        field.setAccessible(true);

        assertEquals("tops value was not retrieved properly.", field.get(g), tops);
    }

    @Test
    public void test_equals_TwoGraphsWithDifferentValues() {

        HashMap<Integer, node> nodes1 = new HashMap<Integer, node>();
        ArrayList<edge> edges1 = new ArrayList<>();
        ArrayList<token> tokens1 = new ArrayList<>();

        HashMap<Integer, node> nodes2 = new HashMap<Integer, node>();
        ArrayList<edge> edges2 = new ArrayList<>();
        ArrayList<token> tokens2 = new ArrayList<>();


        ArrayList<anchors> anchs = new ArrayList<>();
        anchs.add(new anchors(1, 2));

        nodes1.put(0,new node(0, "node1", anchs));
        nodes1.put(1,new node(1, "node2", anchs));
        nodes2.put(2,new node(2, "node3", anchs));
        nodes2.put(3,new node(3, "node4", anchs));

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

        HashMap<Integer, node> nodes1 = new HashMap<Integer, node>();
        ArrayList<edge> edges1 = new ArrayList<>();
        ArrayList<token> tokens1 = new ArrayList<>();

        HashMap<Integer, node> nodes2 = new HashMap<Integer, node>();
        ArrayList<edge> edges2 = new ArrayList<>();
        ArrayList<token> tokens2 = new ArrayList<>();

        ArrayList<anchors> anchs = new ArrayList<>();
        anchs.add(new anchors(1, 2));

        nodes1.put(0,new node(0, "node1", anchs));
        nodes2.put(0,new node(0, "node1", anchs));

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

        HashMap<Integer, node> nodes1 = new HashMap<Integer, node>();
        ArrayList<edge> edges1 = new ArrayList<>();
        ArrayList<token> tokens1 = new ArrayList<>();

        nodes1.put(0,new node(0, "node1", new ArrayList<anchors>()));

        edges1.add(new edge(0, 1, "testlabel", "testpostlabel"));

        tokens1.add(new token(0, "node1 form", "node1 lemma", "node1 carg"));

        graph g1 = new graph("11111", "testsource", "node1 node2 node3 node4", nodes1, tokens1, edges1, new ArrayList<Integer>());

        assertFalse("Equals does not work with two objects of different classes", g1.equals(new anchors(0, 1)));
    }

    @Test
    public void test_equals_EqualToItself() {

        HashMap<Integer, node> nodes1 = new HashMap<Integer, node>();
        ArrayList<edge> edges1 = new ArrayList<>();
        ArrayList<token> tokens1 = new ArrayList<>();

        nodes1.put(0,new node(0, "node1", new ArrayList<anchors>()));

        edges1.add(new edge(0, 1, "testlabel", "testpostlabel"));

        tokens1.add(new token(0, "node1 form", "node1 lemma", "node1 carg"));

        graph g1 = new graph("11111", "testsource", "node1 node2 node3 node4", nodes1, tokens1, edges1, new ArrayList<Integer>());

        assertTrue("Equals does not work with a token equalling itself.", g1.equals(g1));
    }

    @Test
    public void test_setNodeNeighbours_setDirectedNeighbouringNodesCorrectly() throws NoSuchFieldException, IllegalAccessException {
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node1", new ArrayList<>());
        node node1 = new node(1, "node2", new ArrayList<>());
        node node2 = new node(2, "node3", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);

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
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node1", new ArrayList<>());
        node node1 = new node(1, "node2", new ArrayList<>());
        node node2 = new node(2, "node3", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);

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
    public void test_setNodeNeighbours_setDirectedEdgesCorrectly() throws NoSuchFieldException, IllegalAccessException {
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node1", new ArrayList<>());
        node node1 = new node(1, "node2", new ArrayList<>());
        node node2 = new node(2, "node3", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);


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
        final Field edgeField0 = nodes.get(0).getClass().getDeclaredField("directedEdgeNeighbours");
        edgeField0.setAccessible(true);
        final Field edgeField1 = nodes.get(1).getClass().getDeclaredField("directedEdgeNeighbours");
        edgeField1.setAccessible(true);
        final Field edgeField2 = nodes.get(2).getClass().getDeclaredField("directedEdgeNeighbours");
        edgeField2.setAccessible(true);


        assertTrue("setNodeNeighbours does not set edges correctly #1", edgeField0.get(node0).equals(correctResult0));
        assertTrue("setNodeNeighbours does not set edges correctly #2", edgeField1.get(node1).equals(correctResult1));
        assertTrue("setNodeNeighbours does not set edges correctly #3", edgeField2.get(node2).equals(correctResult2));
    }

    @Test
    public void test_setNodeNeighbours_setUndirectedEdgesCorrectly() throws NoSuchFieldException, IllegalAccessException {
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node1", new ArrayList<>());
        node node1 = new node(1, "node2", new ArrayList<>());
        node node2 = new node(2, "node3", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);


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


        correctResult1.add(edge0);
        correctResult2.add(edge1);
        correctResult2.add(edge2);

        g.setNodeNeighbours();


        Comparator<edge> comp = new Comparator<edge>() {
            @Override
            public int compare(edge o1, edge o2) {
                if (o1.getSource() < o2.getSource()) {
                    return -1;
                } else if (o1.getSource() > o2.getSource()) {
                    return 1;
                }
                return 0;
            }
        };

        Collections.sort(correctResult0, comp);
        Collections.sort(correctResult1, comp);
        Collections.sort(correctResult2, comp);
        Collections.sort(g.getNodes().get(0).getUndirectedEdgeNeighbours(), comp);
        Collections.sort(g.getNodes().get(1).getUndirectedEdgeNeighbours(), comp);
        Collections.sort(g.getNodes().get(2).getUndirectedEdgeNeighbours(), comp);

//Get fields without using getter
        final Field edgeField0 = nodes.get(0).getClass().getDeclaredField("undirectedEdgeNeighbours");
        edgeField0.setAccessible(true);
        final Field edgeField1 = nodes.get(1).getClass().getDeclaredField("undirectedEdgeNeighbours");
        edgeField1.setAccessible(true);
        final Field edgeField2 = nodes.get(2).getClass().getDeclaredField("undirectedEdgeNeighbours");
        edgeField2.setAccessible(true);


        assertTrue("setNodeNeighbours does not set edges correctly #1", edgeField0.get(node0).equals(correctResult0));
        assertTrue("setNodeNeighbours does not set edges correctly #2", edgeField1.get(node1).equals(correctResult1));
        assertTrue("setNodeNeighbours does not set edges correctly #3", edgeField2.get(node2).equals(correctResult2));
    }

    @Test
    public void test_setNodeNeighbours_NoEdgesInGraph() throws NoSuchFieldException, IllegalAccessException {
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node1", new ArrayList<>());
        node node1 = new node(1, "node2", new ArrayList<>());
        node node2 = new node(2, "node3", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);

        graph g = new graph("11111", "testsource", "node1 node2 node3 node4", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        ArrayList<edge> emptyArray = new ArrayList<>();

        g.setNodeNeighbours();

        //Get fields without using getter
        final Field nodeField0 = nodes.get(0).getClass().getDeclaredField("directedNeighbours");
        nodeField0.setAccessible(true);
        final Field nodeField1 = nodes.get(1).getClass().getDeclaredField("directedNeighbours");
        nodeField1.setAccessible(true);
        final Field nodeField2 = nodes.get(2).getClass().getDeclaredField("directedNeighbours");
        nodeField2.setAccessible(true);

        final Field edgeField0 = nodes.get(0).getClass().getDeclaredField("directedEdgeNeighbours");
        edgeField0.setAccessible(true);
        final Field edgeField1 = nodes.get(1).getClass().getDeclaredField("directedEdgeNeighbours");
        edgeField1.setAccessible(true);
        final Field edgeField2 = nodes.get(2).getClass().getDeclaredField("directedEdgeNeighbours");
        edgeField2.setAccessible(true);

        final Field UnedgeField0 = nodes.get(0).getClass().getDeclaredField("undirectedEdgeNeighbours");
        UnedgeField0.setAccessible(true);
        final Field UnedgeField1 = nodes.get(1).getClass().getDeclaredField("undirectedEdgeNeighbours");
        UnedgeField1.setAccessible(true);
        final Field UnedgeField2 = nodes.get(2).getClass().getDeclaredField("undirectedEdgeNeighbours");
        UnedgeField2.setAccessible(true);


        assertTrue("setNodeNeighbours does not handle edgeless graphs correctly.", edgeField0.get(node0).equals(emptyArray));
        assertTrue("setNodeNeighbours does not handle edgeless graphs correctly.", edgeField1.get(node1).equals(emptyArray));
        assertTrue("setNodeNeighbours does not handle edgeless graphs correctly.", edgeField2.get(node2).equals(emptyArray));
        assertTrue("setNodeNeighbours does not handle edgeless graphs correctly.", nodeField0.get(node0).equals(emptyArray));
        assertTrue("setNodeNeighbours does not handle edgeless graphs correctly.", nodeField1.get(node1).equals(emptyArray));
        assertTrue("setNodeNeighbours does not handle edgeless graphs correctly.", nodeField2.get(node2).equals(emptyArray));
        assertTrue("setNodeNeighbours does not handle edgeless graphs correctly.", UnedgeField0.get(node0).equals(emptyArray));
        assertTrue("setNodeNeighbours does not handle edgeless graphs correctly.", UnedgeField1.get(node1).equals(emptyArray));
        assertTrue("setNodeNeighbours does not handle edgeless graphs correctly.", UnedgeField2.get(node2).equals(emptyArray));

    }

    @Test
    public void test_setNodeNeighbours_NodesAlreadyHaveNodeNeighbours() throws NoSuchFieldException, IllegalAccessException {
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node1", new ArrayList<>());
        node node1 = new node(1, "node2", new ArrayList<>());
        node node2 = new node(2, "node3", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);


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
        final Field edgeField = node0.getClass().getDeclaredField("directedEdgeNeighbours");
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
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);

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
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);

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

    @Test
    public void test_topologicalSort_SortsCorrectly() throws NoSuchFieldException, IllegalAccessException{
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        node node6 = new node(6, "node6", new ArrayList<>());
        node node7 = new node(7, "node7", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);
        nodes.put(6,node6);
        nodes.put(7,node7);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        //Creating the array of node neighbours for the nodes in the graph.
        ArrayList<node> nodeNeighbours0 = new ArrayList<>();
        ArrayList<node> nodeNeighbours1 = new ArrayList<>();
        ArrayList<node> nodeNeighbours3 = new ArrayList<>();
        ArrayList<node> nodeNeighbours4 = new ArrayList<>();

        nodeNeighbours0.add(node2);
        nodeNeighbours0.add(node3);
        nodeNeighbours0.add(node4);
        nodeNeighbours1.add(node2);
        nodeNeighbours1.add(node5);
        nodeNeighbours3.add(node5);
        nodeNeighbours4.add(node7);

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
        final Field nodeField4 = node4.getClass().getDeclaredField("directedNeighbours");
        nodeField4.setAccessible(true);
        nodeField4.set(node4, nodeNeighbours4);

        Stack<Integer> stack = new Stack<Integer>();
        HashMap<Integer,Boolean> visited = new HashMap<Integer, Boolean>();
        visited.put(0,false);
        visited.put(1,false);
        visited.put(2,false);
        visited.put(3,false);
        visited.put(4,false);
        visited.put(5,false);
        visited.put(6,false);
        visited.put(7,false);

        //Expected results for longest path for each node as the start node.
        Stack<Integer> expectedResult = new Stack<Integer>();
        expectedResult.push(2);
        expectedResult.push(5);
        expectedResult.push(3);
        expectedResult.push(7);
        expectedResult.push(4);
        expectedResult.push(0);


        assertTrue("topologicalSort does not sort a graph correctly for node 0.", g.topologicalSort(0,visited,stack).equals(expectedResult));

        stack.clear();
        visited.put(0,false);
        visited.put(1,false);
        visited.put(2,false);
        visited.put(3,false);
        visited.put(4,false);
        visited.put(5,false);
        visited.put(6,false);
        visited.put(7,false);

        //Expected results for longest path for each node as the start node.
        expectedResult.clear();
        expectedResult.push(2);
        expectedResult.push(5);
        expectedResult.push(1);

        assertTrue("topologicalSort does not sort a graph correctly for node 1.", g.topologicalSort(1,visited,stack).equals(expectedResult));

        stack.clear();
        visited.put(0,false);
        visited.put(1,false);
        visited.put(2,false);
        visited.put(3,false);
        visited.put(4,false);
        visited.put(5,false);
        visited.put(6,false);
        visited.put(7,false);

        //Expected results for longest path for each node as the start node.
        expectedResult.clear();
        expectedResult.push(2);

        assertTrue("topologicalSort does not sort a graph correctly for node 2.", g.topologicalSort(2,visited,stack).equals(expectedResult));

        stack.clear();
        visited.put(0,false);
        visited.put(1,false);
        visited.put(2,false);
        visited.put(3,false);
        visited.put(4,false);
        visited.put(5,false);
        visited.put(6,false);
        visited.put(7,false);

        //Expected results for longest path for each node as the start node.
        expectedResult.clear();
        expectedResult.push(6);

        assertTrue("topologicalSort does not sort a graph correctly for node 6.", g.topologicalSort(6,visited,stack).equals(expectedResult));

        stack.clear();
        visited.put(0,false);
        visited.put(1,false);
        visited.put(2,false);
        visited.put(3,false);
        visited.put(4,false);
        visited.put(5,false);
        visited.put(6,false);
        visited.put(7,false);

        //Expected results for longest path for each node as the start node.
        expectedResult.clear();
        expectedResult.push(7);

        assertTrue("topologicalSort does not sort a graph correctly for node 7.", g.topologicalSort(7,visited,stack).equals(expectedResult));

    }

    @Test
    public void test_BFS_UndirectedSingleLongestPathFromStartNodeInAcyclicGraph() throws NoSuchFieldException, IllegalAccessException {

        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);

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
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);

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

    @Test
    public void test_findLongest_DirectedSingleLongestPath(){

        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);

        edge edge0 = new edge(1, 0, "testlabel", "testpostlabel");
        edge edge1 = new edge(1, 2, "testlabel1", "testpostlabel1");
        edge edge2 = new edge(2, 3, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());


        //Expected result for longest path.
        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(0).add(1);
        correctResult.get(0).add(2);
        correctResult.get(0).add(3);

        assertTrue("findLongest path algorithm does not correctly find a single longest directed path in a graph.", g.findLongest(true).equals(correctResult));

    }

    @Test
    public void test_findLongest_DirectedMultipleLongestPathsFromSingleStartNode()  {
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);

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
    public void test_findLongest_DirectedMultipleLongestPathsFromDifferentStartNodes() {
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);

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
    public void test_findLongest_DirectedMultipleLongestPathsFromSameAndDifferentStartNodes()  {
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);

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
    public void test_findLongest_UndirectedSingleLongestPath()  {

        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);

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
    public void test_findLongest_UndirectedMultipleLongestPathsFromSingleStartNode()  {
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        node node6 = new node(6, "node6", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);
        nodes.put(6,node6);

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
    public void test_findLongest_UndirectedMultipleLongestPathsFromDifferentStartNodes()  {
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node4", new ArrayList<>());
        node node3 = new node(3, "node5", new ArrayList<>());
        node node4 = new node(4, "node6", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);

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
    public void test_findLongest_UndirectedMultipleLongestPathsFromSameAndDifferentStartNodes() {
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);

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

    @Test
    public void test_findLongest_UndirectedMultipleLongestPathsInSymmetricalGraphEdgeCase(){
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        node node6 = new node(6, "node6", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);
        nodes.put(6,node6);

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
        correctResult.get(2).add(6);
        correctResult.get(2).add(2);
        correctResult.get(2).add(0);
        correctResult.get(2).add(1);
        correctResult.get(2).add(4);
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(3).add(6);
        correctResult.get(3).add(2);
        correctResult.get(3).add(0);
        correctResult.get(3).add(1);
        correctResult.get(3).add(5);

       assertTrue("findLongest path algorithm does not correctly find multiple longest undirected paths in a graph that is perfectly symmetrical around node 0.", g.findLongest(false).equals(correctResult));
    }

    @Test
    public void test_findLongest_UndirectedSingleLongestPathInDisconnectedGraphCase1(){
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        node node6 = new node(6, "node6", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);
        nodes.put(6,node6);

        edge edge0 = new edge(1, 0, "testlabel", "testpostlabel");
        edge edge1 = new edge(1, 2, "testlabel1", "testpostlabel1");
        edge edge2 = new edge(4, 5, "testlabel2", "testpostlabel2");

        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());


        //Expected result for longest path.
        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();
        correctResult.add(new ArrayList<>());
        correctResult.get(0).add(0);
        correctResult.get(0).add(1);
        correctResult.get(0).add(2);


        assertTrue("findLongest path algorithm does not correctly find a single longest undirected path in a disconnected graph #1.", g.findLongest(false).equals(correctResult));

    }

    @Test
    public void test_findLongest_UndirectedSingleLongestPathInDisconnectedGraphCase2(){
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        node node6 = new node(6, "node6", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);
        nodes.put(6,node6);

        edge edge0 = new edge(1, 0, "testlabel", "testpostlabel");
        edge edge1 = new edge(1, 2, "testlabel1", "testpostlabel1");
        edge edge2 = new edge(4, 5, "testlabel2", "testpostlabel2");
        edge edge3 = new edge(6, 4, "testlabel2", "testpostlabel2");
        edge edge4 = new edge(5, 3, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(edge4);

        graph g2 = new graph("11111", "testsource", "testInput", new HashMap<Integer,node>(nodes), new ArrayList<token>(),  new ArrayList<>(edges), new ArrayList<Integer>());

        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();
        correctResult.add(new ArrayList<>());
        correctResult.get(0).add(3);
        correctResult.get(0).add(5);
        correctResult.get(0).add(4);
        correctResult.get(0).add(6);

        assertTrue("findLongest path algorithm does not correctly find a single longest undirected path in a disconnected graph #2.", g2.findLongest(false).equals(correctResult));
    }

    @Test
    public void test_findLongest_UndirectedMultipleLongestPathInDisconnectedGraph(){
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);

        edge edge0 = new edge(0, 1, "testlabel", "testpostlabel");
        edge edge1 = new edge(0, 2, "testlabel1", "testpostlabel1");
        edge edge2 = new edge(4, 5, "testlabel2", "testpostlabel2");
        edge edge4 = new edge(5, 3, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge4);

        graph g2 = new graph("11111", "testsource", "testInput", new HashMap<Integer,node>(nodes), new ArrayList<token>(),  new ArrayList<>(edges), new ArrayList<Integer>());

        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();
        correctResult.add(new ArrayList<>());
        correctResult.get(0).add(1);
        correctResult.get(0).add(0);
        correctResult.get(0).add(2);
        correctResult.add(new ArrayList<>());
        correctResult.get(1).add(3);
        correctResult.get(1).add(5);
        correctResult.get(1).add(4);

        assertTrue("findLongest path algorithm does not correctly find multiple longest undirected paths in a disconnected graph.", g2.findLongest(false).equals(correctResult));
    }

    @Test
    public void test_findLongest_UndirectedMultipleLongestPathInComplexDisconnectedGraph(){
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        node node6 = new node(6, "node6", new ArrayList<>());
        node node7 = new node(7, "node7", new ArrayList<>());
        node node8 = new node(8, "node8", new ArrayList<>());
        node node9 = new node(9, "node0", new ArrayList<>());
        node node10 = new node(10, "node10", new ArrayList<>());
        node node11 = new node(11, "node11", new ArrayList<>());
        node node12 = new node(12, "node11", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);
        nodes.put(6,node6);
        nodes.put(7,node7);
        nodes.put(8,node8);
        nodes.put(9,node9);
        nodes.put(10,node10);
        nodes.put(11,node11);
        nodes.put(12,node12);

        edge edge0 = new edge(1, 0, "testlabel", "testpostlabel");
        edge edge1 = new edge(1, 2, "testlabel1", "testpostlabel1");
        edge edge2 = new edge(12, 5, "testlabel2", "testpostlabel2");
        edge edge3 = new edge(6, 1, "testlabel2", "testpostlabel2");
        edge edge4 = new edge(5, 3, "testlabel2", "testpostlabel2");
        edge edge5 = new edge(7, 3, "testlabel2", "testpostlabel2");
        edge edge6 = new edge(8, 3, "testlabel2", "testpostlabel2");
        edge edge7 = new edge(2, 10, "testlabel2", "testpostlabel2");
        edge edge8 = new edge(9, 6, "testlabel2", "testpostlabel2");
        edge edge9 = new edge(0, 11, "testlabel2", "testpostlabel2");
        edge edge10 = new edge(4, 12, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(edge4);
        edges.add(edge5);
        edges.add(edge6);
        edges.add(edge7);
        edges.add(edge8);
        edges.add(edge9);
        edges.add(edge10);

        graph g2 = new graph("11111", "testsource", "testInput", new HashMap<Integer,node>(nodes), new ArrayList<token>(),  new ArrayList<>(edges), new ArrayList<Integer>());

        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();
        correctResult.add(new ArrayList<>());
        correctResult.get(0).add(4);
        correctResult.get(0).add(12);
        correctResult.get(0).add(5);
        correctResult.get(0).add(3);
        correctResult.get(0).add(7);
        correctResult.add(new ArrayList<>());
        correctResult.get(1).add(4);
        correctResult.get(1).add(12);
        correctResult.get(1).add(5);
        correctResult.get(1).add(3);
        correctResult.get(1).add(8);
        correctResult.add(new ArrayList<>());
        correctResult.get(2).add(9);
        correctResult.get(2).add(6);
        correctResult.get(2).add(1);
        correctResult.get(2).add(2);
        correctResult.get(2).add(10);
        correctResult.add(new ArrayList<>());
        correctResult.get(3).add(9);
        correctResult.get(3).add(6);
        correctResult.get(3).add(1);
        correctResult.get(3).add(0);
        correctResult.get(3).add(11);
        correctResult.add(new ArrayList<>());
        correctResult.get(4).add(10);
        correctResult.get(4).add(2);
        correctResult.get(4).add(1);
        correctResult.get(4).add(0);
        correctResult.get(4).add(11);


        assertTrue("findLongest path algorithm does not correctly find multiple longest undirected paths in a complex disconnected graph.", g2.findLongest(false).equals(correctResult));
    }

    @Test
    public void test_findLongest_OneEdgeInGraph() {
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);

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
    public void test_findLongest_NoNodes() {
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), new ArrayList<>(), new ArrayList<Integer>());

        //Expected result for longest path.
        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();

        assertTrue("findLongest path algorithm does not correctly return the path in a directed graph with no nodes.", g.findLongest(true).equals(correctResult));
        assertTrue("findLongest path algorithm does not correctly return the path in an undirected graph with no nodes.", g.findLongest(false).equals(correctResult));
    }

    @Test
    public void test_findLongest_NoEdges()  {
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();


        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), new ArrayList<>(), new ArrayList<Integer>());

        //Expected result for longest path.
        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();

        assertTrue("findLongest path algorithm does not correctly return the path in a directed graph with no edges", g.findLongest(true).equals(correctResult));
        assertTrue("findLongest path algorithm does not correctly return the path in an undirected graph with no edges.", g.findLongest(false).equals(correctResult));
    }

    @Test
    public void test_combineNeighbours_CombinesNeighboursCorrectly()throws NoSuchFieldException, IllegalAccessException {
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        //Creating the array of node neighbours for the nodes in the graph.
        ArrayList<node> directNeighbours0 = new ArrayList<>();

        directNeighbours0.add(node1);
        directNeighbours0.add(node3);
        directNeighbours0.add(node5);
        ArrayList<node> undirectNeighbours0 = new ArrayList<>();

        undirectNeighbours0.add(node2);
        undirectNeighbours0.add(node4);

        //Setting node neighbours without using setNodeNeighbours method.
        final Field directField0 = node0.getClass().getDeclaredField("directedNeighbours");
        directField0.setAccessible(true);
        directField0.set(node0, directNeighbours0);
        final Field undirectField0= node0.getClass().getDeclaredField("undirectedNeighbours");
        undirectField0.setAccessible(true);
        undirectField0.set(node0, undirectNeighbours0);

        //Expected results for longest path for each node as the start node.
        ArrayList<node> correctResult0 = new ArrayList<>();
        correctResult0.add(node1);
        correctResult0.add(node3);
        correctResult0.add(node5);
        correctResult0.add(node2);
        correctResult0.add(node4);

        assertTrue("combineNodeNeighbours does not correctly combine undirected and directed node neighbours", g.combineNeighbours(0).equals(correctResult0));

    }

    @Test
    public void test_combineNeighbours_NoUndirectedNeighbours()throws NoSuchFieldException, IllegalAccessException {
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        //Creating the array of node neighbours for the nodes in the graph.
        ArrayList<node> directNeighbours0 = new ArrayList<>();

        directNeighbours0.add(node1);
        directNeighbours0.add(node3);
        directNeighbours0.add(node5);
        ArrayList<node> undirectNeighbours0 = new ArrayList<>();


        //Setting node neighbours without using setNodeNeighbours method.
        final Field directField0 = node0.getClass().getDeclaredField("directedNeighbours");
        directField0.setAccessible(true);
        directField0.set(node0, directNeighbours0);
        final Field undirectField0= node0.getClass().getDeclaredField("undirectedNeighbours");
        undirectField0.setAccessible(true);
        undirectField0.set(node0, undirectNeighbours0);

        //Expected results for longest path for each node as the start node.
        ArrayList<node> correctResult0 = new ArrayList<>();
        correctResult0.add(node1);
        correctResult0.add(node3);
        correctResult0.add(node5);

        assertTrue("combineNodeNeighbours does not correctly combine undirected and directed node neighbours when a node has no undirected neighbours", g.combineNeighbours(0).equals(correctResult0));

    }

    @Test
    public void test_combineNeighbours_NoDirectedNeighbours()throws NoSuchFieldException, IllegalAccessException {
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        //Creating the array of node neighbours for the nodes in the graph.
        ArrayList<node> directNeighbours0 = new ArrayList<>();

        ArrayList<node> undirectNeighbours0 = new ArrayList<>();
        undirectNeighbours0.add(node2);
        undirectNeighbours0.add(node4);


        //Setting node neighbours without using setNodeNeighbours method.
        final Field directField0 = node0.getClass().getDeclaredField("directedNeighbours");
        directField0.setAccessible(true);
        directField0.set(node0, directNeighbours0);
        final Field undirectField0= node0.getClass().getDeclaredField("undirectedNeighbours");
        undirectField0.setAccessible(true);
        undirectField0.set(node0, undirectNeighbours0);

        //Expected results for longest path for each node as the start node.
        ArrayList<node> correctResult0 = new ArrayList<>();
        correctResult0.add(node2);
        correctResult0.add(node4);

        assertTrue("combineNodeNeighbours does not correctly combine undirected and directed node neighbours when a node has no directed neighbours", g.combineNeighbours(0).equals(correctResult0));

    }

    @Test
    public void test_combineNeighbours_NoNeighbours()throws NoSuchFieldException, IllegalAccessException {
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        //Creating the array of node neighbours for the nodes in the graph.
        ArrayList<node> directNeighbours0 = new ArrayList<>();

        ArrayList<node> undirectNeighbours0 = new ArrayList<>();


        //Setting node neighbours without using setNodeNeighbours method.
        final Field directField0 = node0.getClass().getDeclaredField("directedNeighbours");
        directField0.setAccessible(true);
        directField0.set(node0, directNeighbours0);
        final Field undirectField0= node0.getClass().getDeclaredField("undirectedNeighbours");
        undirectField0.setAccessible(true);
        undirectField0.set(node0, undirectNeighbours0);

        //Expected results for longest path for each node as the start node.
        ArrayList<node> correctResult0 = new ArrayList<>();

        assertTrue("combineNodeNeighbours does not correctly combine undirected and directed node neighbours when a node has no neighbours", g.combineNeighbours(0).equals(correctResult0));

    }

    @Test
    public void test_traverseLongestPath_FindsSingleLongestPathCorrectly(){
        HashMap<Integer, Integer> distances = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> prevNode = new HashMap<Integer, Integer>();

        distances.put(0,0);
        distances.put(1,1);
        distances.put(2,1);
        distances.put(3,2);
        distances.put(4,Integer.MIN_VALUE);

        prevNode.put(0, 0);
        prevNode.put(1, 0);
        prevNode.put(2, 0);
        prevNode.put(3, 2);

        graph g = new graph();

        ArrayList<ArrayList<Integer>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.get(0).add(3);
        expected.get(0).add(2);
        expected.get(0).add(0);


        assertTrue("traverseLongeestPath does not correctly find a single longest path",g.traverseLongestPath(distances,prevNode,0).equals(expected));

    }

    @Test
    public void test_traverseLongestPath_FindsMultipleLongestPathsCorrectly(){
        HashMap<Integer, Integer> distances = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> prevNode = new HashMap<Integer, Integer>();

        distances.put(0,0);
        distances.put(1,1);
        distances.put(2,1);
        distances.put(3,2);
        distances.put(4,Integer.MIN_VALUE);
        distances.put(5,2);

        prevNode.put(0, 0);
        prevNode.put(1 ,0);
        prevNode.put(2 ,0);
        prevNode.put(3 ,2);
        prevNode.put(5 ,1);

        graph g = new graph();

        ArrayList<ArrayList<Integer>> expected = new ArrayList<>();
        expected.add(new ArrayList<>());
        expected.get(0).add(3);
        expected.get(0).add(2);
        expected.get(0).add(0);
        expected.add(new ArrayList<>());
        expected.get(1).add(5);
        expected.get(1).add(1);
        expected.get(1).add(0);


        assertTrue("traverseLongeestPath does not correctly find a single longest path",g.traverseLongestPath(distances,prevNode,0).equals(expected));

    }

    @Test
    public void test_isPlanar_IdentifiesPlanarGraph() {
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();
        ArrayList<token> tokens = new ArrayList<>();


        ArrayList<anchors> anch1 = new ArrayList<anchors>();
        anch1.add(new anchors(0, 0));
        nodes.put(0,new node(0, "node" + (0 + 1), anch1));

        ArrayList<anchors> anch2 = new ArrayList<anchors>();
        anch2.add(new anchors(1, 1));
        nodes.put(1,new node(1, "node" + (1 + 1), anch2));

        ArrayList<anchors> anch3 = new ArrayList<anchors>();
        anch3.add(new anchors(2, 2));
        nodes.put(2,new node(2, "node" + (2 + 1), anch3));

        ArrayList<anchors> anch4 = new ArrayList<anchors>();
        anch4.add(new anchors(3, 3));
        nodes.put(3,new node(3, "node" + (3 + 1), anch4));

        ArrayList<anchors> anch5 = new ArrayList<anchors>();
        anch5.add(new anchors(4, 4));
        nodes.put(4,new node(4, "node" + (4 + 1), anch5));

        edges.add(new edge(0, 1, "testlabel", "testpostlabel"));
        edges.add(new edge(1, 3, "testlabel1", "testpostlabel1"));
        edges.add(new edge(2, 4, "testlabel2", "testpostlabel2"));

        graph g = new graph("11111", "testsource", "node1 node2 node3 node4", nodes, tokens, edges, new ArrayList<Integer>());

        assertFalse("isPlanar Correctly identifies planar and non-planar graphs", g.GraphIsPlanar());

        edges.clear();
        edges.add(new edge(0, 1, "testlabel", "testpostlabel"));
        edges.add(new edge(1, 2, "testlabel1", "testpostlabel1"));
        edges.add(new edge(2, 3, "testlabel2", "testpostlabel2"));
        edges.add(new edge(3, 4, "testlabel2", "testpostlabel2"));

        assertTrue("isPlanar Correctly identifies planar and non-planar graphs", g.GraphIsPlanar());

        edges.clear();
        edges.add(new edge(0, 2, "testlabel", "testpostlabel"));
        edges.add(new edge(1, 3, "testlabel1", "testpostlabel1"));

        assertFalse("isPlanar Correctly identifies planar and non-planar graphs", g.GraphIsPlanar());

        edges.clear();
        edges.add(new edge(0, 4, "testlabel", "testpostlabel"));
        edges.add(new edge(1, 4, "testlabel1", "testpostlabel1"));
        edges.add(new edge(2, 4, "testlabel2", "testpostlabel2"));
        edges.add(new edge(3, 4, "testlabel2", "testpostlabel2"));

        assertTrue("isPlanar Correctly identifies planar and non-planar graphs", g.GraphIsPlanar());

        edges.clear();
        edges.add(new edge(4, 0, "testlabel", "testpostlabel"));
        edges.add(new edge(1, 0, "testlabel1", "testpostlabel1"));
        edges.add(new edge(2, 0, "testlabel2", "testpostlabel2"));
        edges.add(new edge(3, 0, "testlabel2", "testpostlabel2"));

        assertTrue("isPlanar Correctly identifies planar and non-planar graphs", g.GraphIsPlanar());


    }

    @Test
    public void test_isPlanar_HandlesNodesWithSameToken() {
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();
        ArrayList<token> tokens = new ArrayList<>();


        ArrayList<anchors> anch1 = new ArrayList<anchors>();
        anch1.add(new anchors(0, 0));
        nodes.put(0,new node(0, "node" + (0 + 1), anch1));

        ArrayList<anchors> anch2 = new ArrayList<anchors>();
        anch2.add(new anchors(0, 0));
        nodes.put(1,new node(1, "node" + (1 + 1), anch2));

        ArrayList<anchors> anch3 = new ArrayList<anchors>();
        anch3.add(new anchors(1, 1));
        nodes.put(2,new node(2, "node" + (2 + 1), anch3));

        ArrayList<anchors> anch4 = new ArrayList<anchors>();
        anch4.add(new anchors(2, 2));
        nodes.put(3,new node(3, "node" + (3 + 1), anch4));

        ArrayList<anchors> anch5 = new ArrayList<anchors>();
        anch5.add(new anchors(3, 3));
        nodes.put(4,new node(4, "node" + (4 + 1), anch5));

        edges.add(new edge(0, 2, "testlabel", "testpostlabel"));
        edges.add(new edge(1, 3, "testlabel1", "testpostlabel1"));


        graph g = new graph("11111", "testsource", "node1 node2 node3 node4", nodes, tokens, edges, new ArrayList<Integer>());

        assertTrue("isPlanar Correctly identifies planar and non-planar graphs with duplicate token references", g.GraphIsPlanar());

        edges.clear();
        edges.add(new edge(0, 3, "testlabel", "testpostlabel"));
        edges.add(new edge(2, 3, "testlabel1", "testpostlabel1"));
        edges.add(new edge(1, 3, "testlabel2", "testpostlabel2"));
        edges.add(new edge(3, 4, "testlabel2", "testpostlabel2"));

        assertTrue("isPlanar Correctly identifies planar and non-planar graphs with duplicate token references", g.GraphIsPlanar());

        anch1 = new ArrayList<anchors>();
        anch1.add(new anchors(0, 0));
        nodes.clear();
        nodes.put(0,new node(0, "node" + (0 + 1), anch1));

        anch2 = new ArrayList<anchors>();
        anch2.add(new anchors(0, 0));
        nodes.put(1,new node(1, "node" + (1 + 1), anch2));

        anch3 = new ArrayList<anchors>();
        anch3.add(new anchors(1, 1));
        nodes.put(2,new node(2, "node" + (2 + 1), anch3));

        anch4 = new ArrayList<anchors>();
        anch4.add(new anchors(1, 1));
        nodes.put(3,new node(3, "node" + (3 + 1), anch4));

        anch5 = new ArrayList<anchors>();
        anch5.add(new anchors(2, 2));
        nodes.put(4,new node(4, "node" + (4 + 1), anch5));

        ArrayList<anchors> anch6 = new ArrayList<anchors>();
        anch6.add(new anchors(3, 3));
        nodes.put(5,new node(5, "node" + (5 + 1), anch6));

        edges.clear();
        edges.add(new edge(0, 3, "testlabel", "testpostlabel"));
        edges.add(new edge(1, 4, "testlabel1", "testpostlabel1"));
        edges.add(new edge(2, 4, "testlabel2", "testpostlabel2"));

        assertTrue("isPlanar Correctly identifies planar and non-planar graphs with duplicate token references", g.GraphIsPlanar());

        edges.clear();
        edges.add(new edge(0, 3, "testlabel", "testpostlabel"));
        edges.add(new edge(1, 4, "testlabel1", "testpostlabel1"));
        edges.add(new edge(2, 4, "testlabel2", "testpostlabel2"));
        edges.add(new edge(3, 5, "testlabel2", "testpostlabel2"));

        assertFalse("isPlanar Correctly identifies planar and non-planar graphs with duplicate token references", g.GraphIsPlanar());


    }

    @Test
    public void test_isPlanar_HandlesNoEdgesInGraph() {
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();
        ArrayList<token> tokens = new ArrayList<>();


        ArrayList<anchors> anch1 = new ArrayList<anchors>();
        anch1.add(new anchors(0, 0));
        nodes.put(0,new node(0, "node" + (0 + 1), anch1));

        ArrayList<anchors> anch2 = new ArrayList<anchors>();
        anch2.add(new anchors(1, 1));
        nodes.put(1,new node(1, "node" + (1 + 1), anch2));

        ArrayList<anchors> anch3 = new ArrayList<anchors>();
        anch3.add(new anchors(2, 2));
        nodes.put(2,new node(2, "node" + (2 + 1), anch3));

        ArrayList<anchors> anch4 = new ArrayList<anchors>();
        anch4.add(new anchors(3, 3));
        nodes.put(3,new node(3, "node" + (3 + 1), anch4));

        ArrayList<anchors> anch5 = new ArrayList<anchors>();
        anch5.add(new anchors(4, 4));
        nodes.put(4,new node(4, "node" + (4 + 1), anch5));


        graph g = new graph("11111", "testsource", "node1 node2 node3 node4", nodes, tokens, edges, new ArrayList<Integer>());

        assertTrue("isPlanar Correctly identifies planar and non-planar graphs", g.GraphIsPlanar());

    }

    @Test
    public void test_isPlanar_HandlesNoNodesInGraph() {
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();
        ArrayList<token> tokens = new ArrayList<>();

        graph g = new graph("11111", "testsource", "node1 node2 node3 node4", nodes, tokens, edges, new ArrayList<Integer>());

        assertTrue("isPlanar Correctly identifies planar and non-planar graphs", g.GraphIsPlanar());

    }

    @Test
    public void test_getTokenSpan_getTokensCorrectly(){
        ArrayList<token> tokens = new ArrayList<>();

        token token0 = new token(0,"form0", "lemma0", "carg0");
        token token1 = new token(1,"form1", "lemma1", "carg1");
        token token2 = new token(2,"form2", "lemma2", "carg2");
        token token3 = new token(3,"form3", "lemma3", "carg3");
        token token4 = new token(4,"form4", "lemma4", "carg4");
        token token5 = new token(5,"form5", "lemma5", "carg5");
        tokens.add(token0);
        tokens.add(token1);
        tokens.add(token2);
        tokens.add(token3);
        tokens.add(token4);
        tokens.add(token5);

        graph g = new graph("1","source","input",new HashMap<Integer, node>(),tokens,new ArrayList<edge>(),new ArrayList<Integer>());

        ArrayList<token> expected = new ArrayList<>();
        expected.add(token1);
        expected.add(token2);
        expected.add(token3);

        assertTrue("getTokenSpan does not get a range of tokens correctly.",g.getTokenSpan(1,3).equals(expected));

    }

    @Test
    public void test_getTokenSpan_noTokensInGraph(){

        graph g = new graph("1","source","input",new HashMap<Integer, node>(),new ArrayList<token>(),new ArrayList<edge>(),new ArrayList<Integer>());

        ArrayList<token> expected = new ArrayList<>();

        assertTrue("getTokenSpan does not correctly retrieve tokens from an empty graph.",g.getTokenSpan(0,-1).equals(expected));
    }

    @Test
    public void test_getTokenSpan_GetOneTokenOnly(){
        ArrayList<token> tokens = new ArrayList<>();

        token token0 = new token(0,"form0", "lemma0", "carg0");
        token token1 = new token(1,"form1", "lemma1", "carg1");
        token token2 = new token(2,"form2", "lemma2", "carg2");
        tokens.add(token0);
        tokens.add(token1);
        tokens.add(token2);

        graph g = new graph("1","source","input",new HashMap<Integer, node>(),tokens,new ArrayList<edge>(),new ArrayList<Integer>());

        ArrayList<token> expected = new ArrayList<>();
        expected.add(token1);

        assertTrue("getTokenSpan does not get a single token correctly.",g.getTokenSpan(1,1).equals(expected));
    }

    @Test
    public void test_getTokenInput_GetsTokenInputCorrectly(){
        ArrayList<token> tokens = new ArrayList<>();

        token token0 = new token(0,"form0", "lemma0", "carg0");
        token token1 = new token(1,"form1", "lemma1", "carg1");
        token token2 = new token(2,"form2", "lemma2", "carg2");
        token token3 = new token(3,"form3", "lemma3", "carg3");
        token token4 = new token(4,"form4", "lemma4", "carg4");
        token token5 = new token(5,"form5", "lemma5", "carg5");
        tokens.add(token0);
        tokens.add(token1);
        tokens.add(token2);
        tokens.add(token3);
        tokens.add(token4);
        tokens.add(token5);

        graph g = new graph("1","source","input",new HashMap<Integer, node>(),tokens,new ArrayList<edge>(),new ArrayList<Integer>());

        String expected = "form0 form1 form2 form3 form4 form5";

        assertTrue("getTokenInput does not get tokens' forms correctly.",g.getTokenInput(tokens).equals(expected));
    }

    @Test
    public void test_getTokenInput_EmptyTokenArray(){
        ArrayList<token> tokens = new ArrayList<>();


        graph g = new graph("1","source","input",new HashMap<Integer, node>(),tokens,new ArrayList<edge>(),new ArrayList<Integer>());

        String expected = "" ;

        assertTrue("getTokenInput does not get tokens' forms correctly from an empty array of tokens.",g.getTokenInput(tokens).equals(expected));
    }

    @Test
    public void test_connectedBFS_IdentifiesConnectedGraph(){
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);

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

        assertTrue("connectedBFS method does not correctly identify a connected graph.", g.connectedBFS(0));

    }

    @Test
    public void test_connectedBFS_IdentifiesDisconnectedGraph(){
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        node node3 = new node(3, "node3", new ArrayList<>());
        node node4 = new node(4, "node4", new ArrayList<>());
        node node5 = new node(5, "node5", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);

        edge edge0 = new edge(0, 1, "testlabel", "testpostlabel");
        edge edge1 = new edge(1, 2, "testlabel", "testpostlabel");
        edge edge2 = new edge(3, 0, "testlabel", "testpostlabel");
        edge edge3 = new edge(3, 4, "testlabel", "testpostlabel");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        assertFalse("connectedBFS method does not correctly identify a disconnected graph.", g.connectedBFS(0));

    }

    @Test
    public void test_connectedBFS_Node0IsDisconnected(){
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        node node1 = new node(1, "node1", new ArrayList<>());
        node node2 = new node(2, "node2", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);

        edge edge1 = new edge(1, 2, "testlabel", "testpostlabel");
        edges.add(edge1);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        assertFalse("connectedBFS method does not correctly identify a disconnected graph where node 0 is disconnected.", g.connectedBFS(0));

    }

    @Test
    public void test_connectedBFS_SingleNodeGraph(){
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        nodes.put(0,node0);

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), new ArrayList<edge>(), new ArrayList<Integer>());

        assertTrue("connectedBFS method does not correctly identify a connected graph with a single node.", g.connectedBFS(0));

    }

    @Test
    public void test_hasDanglingEdge_ReturnsTrueIfGraphHasDanglingEdge() {
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();

        node node0 = new node(0, "node0", new ArrayList<>());
        nodes.put(0, node0);
        edges.add(new edge(0, 1, "", ""));

        graph g = new graph("11111", "testsource", "testInput", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        assertTrue("Has Dangling Edge Has Not Correctly Identified dangling edge", g.hasDanglingEdge());
    }

    @Test
    public void test_Constructor_AcceptArrayAndConvertToHashMapCorrectly()throws NoSuchFieldException, IllegalAccessException{

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

        HashMap<Integer, node> ExpectedNodes = new HashMap<Integer, node>();

        ExpectedNodes.put(0, new node(0, "node1", new ArrayList<anchors>()));
        ExpectedNodes.put(1, new node(1, "node2", new ArrayList<anchors>()));
        ExpectedNodes.put(2, new node(2, "node3", new ArrayList<anchors>()));
        ExpectedNodes.put(3, new node(3, "node4", new ArrayList<anchors>()));

        assertEquals("id value was not constructed properly.", fieldID.get(g), "11111");
        assertEquals("source value was not constructed properly.", fieldSource.get(g), "testsource");
        assertEquals("input value was not constructed properly.", fieldInput.get(g), "node1 node2 node3 node4");
        assertEquals("nodes value was not constructed properly.", fieldNodes.get(g), ExpectedNodes);
        assertEquals("tokens value was not constructed properly.", fieldTokens.get(g), tokens);
        assertEquals("edges value was not constructed properly.", fieldEdges.get(g), edges);
    }
}
