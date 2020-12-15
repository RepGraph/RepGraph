package com.RepGraph;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.*;

import static junit.framework.TestCase.*;


public class GraphTest {

    @Test
    public void test_Constructor_CreatesAndAssignsMemberVariables() throws NoSuchFieldException, IllegalAccessException {

        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();
        ArrayList<Token> tokens = new ArrayList<>();

        nodes.put(0, new Node(0, "node1", new ArrayList<Anchors>()));
        nodes.put(1, new Node(1, "node2", new ArrayList<Anchors>()));
        nodes.put(2, new Node(2, "node3", new ArrayList<Anchors>()));
        nodes.put(3, new Node(3, "node4", new ArrayList<Anchors>()));

        edges.add(new Edge(0, 1, "testlabel", "testpostlabel"));
        edges.add(new Edge(1, 3, "testlabel1", "testpostlabel1"));
        edges.add(new Edge(0, 2, "testlabel2", "testpostlabel2"));

        tokens.add(new Token(0, "node1 form", "node1 lemma", "node1 carg"));
        tokens.add(new Token(1, "node2 form", "node2 lemma", "node2 carg"));
        tokens.add(new Token(2, "node3 form", "node3 lemma", "node3 carg"));
        tokens.add(new Token(3, "node4 form", "node4 lemma", "node4 carg"));

        Graph g = new Graph("11111", "testsource", "node1 node2 node3 node4", nodes, tokens, edges, new ArrayList<Integer>());

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
        final Graph g = new Graph();

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(g, "11111");

        assertEquals("id value was not retrieved properly.", g.getId(), "11111");
    }

    @Test
    public void test_getSource_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {
        final Graph g = new Graph();

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("source");
        field.setAccessible(true);
        field.set(g, "testsource");

        assertEquals("source value was not retrieved properly.", g.getSource(), "testsource");
    }

    @Test
    public void test_getInput_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {
        final Graph g = new Graph();

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("input");
        field.setAccessible(true);
        field.set(g, "node1 node2 node3 node4");

        assertEquals("input value was not retrieved properly.", g.getInput(), "node1 node2 node3 node4");
    }

    @Test
    public void test_getNodelist_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {
        final Graph g = new Graph();

        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(new Node(0, "node1", new ArrayList<Anchors>()));

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("nodelist");
        field.setAccessible(true);
        field.set(g, nodes);


        assertEquals("nodelist value was not retrieved properly.", g.getNodelist(), nodes);

    }

    @Test
    public void test_getNodes_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {
        final Graph g = new Graph();

        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        nodes.put(0,new Node(0, "node1", new ArrayList<Anchors>()));

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("nodes");
        field.setAccessible(true);
        field.set(g, nodes);

        assertEquals("nodes value was not retrieved properly.", g.getNodes(), nodes);
    }

    @Test
    public void test_getTokens_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {
        final Graph g = new Graph();

        ArrayList<Token> tokens = new ArrayList<Token>();
        tokens.add(new Token(0, "form1", "lemma1", "carg1"));

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("tokens");
        field.setAccessible(true);
        field.set(g, tokens);

        assertEquals("tokens value was not retrieved properly.", g.getTokens(), tokens);
    }

    @Test
    public void test_getEdges_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {
        final Graph g = new Graph();

        ArrayList<Edge> edges = new ArrayList<Edge>();
        edges.add(new Edge(0, 1, "testlabel", "testpostlabel"));

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("edges");
        field.setAccessible(true);
        field.set(g, edges);

        assertEquals("edges value was not retrieved properly.", g.getEdges(), edges);
    }

    @Test
    public void test_getTops_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {
        final Graph g = new Graph();

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

        final Graph g = new Graph();

        g.setId("11111");

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("id");
        field.setAccessible(true);

        assertEquals("id value was not retrieved properly.", field.get(g), "11111");
    }

    @Test
    public void test_setSource_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final Graph g = new Graph();

        g.setSource("testSource");

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("source");
        field.setAccessible(true);

        assertEquals("source value was not retrieved properly.", field.get(g), "testSource");
    }

    @Test
    public void test_setInput_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final Graph g = new Graph();

        g.setInput("node1 node2 node3 node4");

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("input");
        field.setAccessible(true);

        assertEquals("input value was not retrieved properly.", field.get(g), "node1 node2 node3 node4");
    }

    @Test
    public void test_setNodelist_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {


        final Graph g = new Graph();

        ArrayList<Node> nodes = new ArrayList<>();
        nodes.add(new Node(0, "node1", new ArrayList<Anchors>()));

        g.setNodelist(nodes);

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("nodelist");
        field.setAccessible(true);

        assertEquals("nodes value was not retrieved properly.", field.get(g), nodes);


        final Field fieldHash = g.getClass().getDeclaredField("nodes");
        fieldHash.setAccessible(true);

        HashMap<Integer, Node> nodeHashMap = new HashMap<Integer, Node>();
        nodeHashMap.put(0, new Node(0, "node1", new ArrayList<Anchors>()));

        assertEquals("nodes value was not set properly when nodeslist was set.", nodeHashMap, fieldHash.get(g));

    }

    @Test
    public void test_setNodes_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final Graph g = new Graph();

        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        nodes.put(0,new Node(0, "node1", new ArrayList<Anchors>()));

        g.setNodes(nodes);

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("nodes");
        field.setAccessible(true);

        assertEquals("nodes value was not retrieved properly.", field.get(g), nodes);
    }

    @Test
    public void test_setTokens_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final Graph g = new Graph();

        ArrayList<Token> tokens = new ArrayList<Token>();
        tokens.add(new Token(0, "form1", "lemma1", "carg1"));

        g.setTokens(tokens);

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("tokens");
        field.setAccessible(true);

        assertEquals("tokens value was not retrieved properly.", field.get(g), tokens);
    }

    @Test
    public void test_setEdges_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final Graph g = new Graph();

        ArrayList<Edge> edges = new ArrayList<Edge>();
        edges.add(new Edge(0, 1, "testlabel", "testpostlabel"));

        g.setEdges(edges);

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("edges");
        field.setAccessible(true);

        assertEquals("edges value was not retrieved properly.", field.get(g), edges);
    }

    @Test
    public void test_setTops_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final Graph g = new Graph();

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

        HashMap<Integer, Node> nodes1 = new HashMap<Integer, Node>();
        ArrayList<Edge> edges1 = new ArrayList<>();
        ArrayList<Token> tokens1 = new ArrayList<>();

        HashMap<Integer, Node> nodes2 = new HashMap<Integer, Node>();
        ArrayList<Edge> edges2 = new ArrayList<>();
        ArrayList<Token> tokens2 = new ArrayList<>();


        ArrayList<Anchors> anchs = new ArrayList<>();
        anchs.add(new Anchors(1, 2));

        nodes1.put(0,new Node(0, "node1", anchs));
        nodes1.put(1,new Node(1, "node2", anchs));
        nodes2.put(2,new Node(2, "node3", anchs));
        nodes2.put(3,new Node(3, "node4", anchs));

        edges1.add(new Edge(0, 1, "testlabel", "testpostlabel"));
        edges2.add(new Edge(1, 3, "testlabel1", "testpostlabel1"));
        edges2.add(new Edge(0, 2, "testlabel2", "testpostlabel2"));

        tokens1.add(new Token(0, "node1 form", "node1 lemma", "node1 carg"));
        tokens1.add(new Token(1, "node2 form", "node2 lemma", "node2 carg"));
        tokens2.add(new Token(2, "node3 form", "node3 lemma", "node3 carg"));
        tokens2.add(new Token(3, "node4 form", "node4 lemma", "node4 carg"));

        Graph g1 = new Graph("11111", "testsource1", "node1 node2 node3 node4", nodes1, tokens1, edges1, new ArrayList<Integer>());
        Graph g2 = new Graph("11111", "testsource2", "node1 node2 node3 node4", nodes2, tokens2, edges2, new ArrayList<Integer>());


        assertFalse("Equals does not work with two graphs with different values.", g1.equals(g2));
    }

    @Test
    public void test_equals_IdenticalGraphsWithSameValues() {

        HashMap<Integer, Node> nodes1 = new HashMap<Integer, Node>();
        ArrayList<Edge> edges1 = new ArrayList<>();
        ArrayList<Token> tokens1 = new ArrayList<>();

        HashMap<Integer, Node> nodes2 = new HashMap<Integer, Node>();
        ArrayList<Edge> edges2 = new ArrayList<>();
        ArrayList<Token> tokens2 = new ArrayList<>();

        ArrayList<Anchors> anchs = new ArrayList<>();
        anchs.add(new Anchors(1, 2));

        nodes1.put(0,new Node(0, "node1", anchs));
        nodes2.put(0,new Node(0, "node1", anchs));

        edges1.add(new Edge(0, 1, "testlabel", "testpostlabel"));
        edges2.add(new Edge(0, 1, "testlabel", "testpostlabel"));

        tokens1.add(new Token(0, "node1 form", "node1 lemma", "node1 carg"));
        tokens2.add(new Token(0, "node1 form", "node1 lemma", "node1 carg"));

        Graph g1 = new Graph("11111", "testsource", "node1 node2 node3 node4", nodes1, tokens1, edges1, new ArrayList<Integer>());
        Graph g2 = new Graph("11111", "testsource", "node1 node2 node3 node4", nodes2, tokens2, edges2, new ArrayList<Integer>());

        assertTrue("Equals does not work with two identical graphs with the same values.", g1.equals(g2));
    }

    @Test
    public void test_equals_TwoObjectsOfDifferentClasses() {

        HashMap<Integer, Node> nodes1 = new HashMap<Integer, Node>();
        ArrayList<Edge> edges1 = new ArrayList<>();
        ArrayList<Token> tokens1 = new ArrayList<>();

        nodes1.put(0,new Node(0, "node1", new ArrayList<Anchors>()));

        edges1.add(new Edge(0, 1, "testlabel", "testpostlabel"));

        tokens1.add(new Token(0, "node1 form", "node1 lemma", "node1 carg"));

        Graph g1 = new Graph("11111", "testsource", "node1 node2 node3 node4", nodes1, tokens1, edges1, new ArrayList<Integer>());

        assertFalse("Equals does not work with two objects of different classes", g1.equals(new Anchors(0, 1)));
    }

    @Test
    public void test_equals_EqualToItself() {

        HashMap<Integer, Node> nodes1 = new HashMap<Integer, Node>();
        ArrayList<Edge> edges1 = new ArrayList<>();
        ArrayList<Token> tokens1 = new ArrayList<>();

        nodes1.put(0,new Node(0, "node1", new ArrayList<Anchors>()));

        edges1.add(new Edge(0, 1, "testlabel", "testpostlabel"));

        tokens1.add(new Token(0, "node1 form", "node1 lemma", "node1 carg"));

        Graph g1 = new Graph("11111", "testsource", "node1 node2 node3 node4", nodes1, tokens1, edges1, new ArrayList<Integer>());

        assertTrue("Equals does not work with a Token equalling itself.", g1.equals(g1));
    }

    @Test
    public void test_setNodeNeighbours_setDirectedNeighbouringNodesCorrectly() throws NoSuchFieldException, IllegalAccessException {
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node1", new ArrayList<>());
        Node node1 = new Node(1, "node2", new ArrayList<>());
        Node node2 = new Node(2, "node3", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);

        edges.add(new Edge(0, 1, "testlabel", "testpostlabel"));
        edges.add(new Edge(1, 2, "testlabel1", "testpostlabel1"));
        edges.add(new Edge(0, 2, "testlabel2", "testpostlabel2"));

        Graph g = new Graph("11111", "testsource", "node1 node2 node3 node4", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());

        ArrayList<Node> correctResult0 = new ArrayList<>();
        ArrayList<Node> correctResult1 = new ArrayList<>();
        ArrayList<Node> correctResult2 = new ArrayList<>();

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
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node1", new ArrayList<>());
        Node node1 = new Node(1, "node2", new ArrayList<>());
        Node node2 = new Node(2, "node3", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);

        edges.add(new Edge(0, 1, "testlabel", "testpostlabel"));
        edges.add(new Edge(1, 2, "testlabel1", "testpostlabel1"));
        edges.add(new Edge(0, 2, "testlabel2", "testpostlabel2"));

        Graph g = new Graph("11111", "testsource", "node1 node2 node3 node4", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());

        ArrayList<Node> correctResult0 = new ArrayList<>();
        ArrayList<Node> correctResult1 = new ArrayList<>();
        ArrayList<Node> correctResult2 = new ArrayList<>();

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
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node1", new ArrayList<>());
        Node node1 = new Node(1, "node2", new ArrayList<>());
        Node node2 = new Node(2, "node3", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);


        Edge edge0 = new Edge(0, 1, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(1, 2, "testlabel1", "testpostlabel1");
        Edge edge2 = new Edge(0, 2, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);

        Graph g = new Graph("11111", "testsource", "node1 node2 node3 node4", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());

        ArrayList<Edge> correctResult0 = new ArrayList<>();
        ArrayList<Edge> correctResult1 = new ArrayList<>();
        ArrayList<Edge> correctResult2 = new ArrayList<>();

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
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node1", new ArrayList<>());
        Node node1 = new Node(1, "node2", new ArrayList<>());
        Node node2 = new Node(2, "node3", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);


        Edge edge0 = new Edge(0, 1, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(1, 2, "testlabel1", "testpostlabel1");
        Edge edge2 = new Edge(0, 2, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);

        Graph g = new Graph("11111", "testsource", "node1 node2 node3 node4", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());

        ArrayList<Edge> correctResult0 = new ArrayList<>();
        ArrayList<Edge> correctResult1 = new ArrayList<>();
        ArrayList<Edge> correctResult2 = new ArrayList<>();


        correctResult1.add(edge0);
        correctResult2.add(edge1);
        correctResult2.add(edge2);

        g.setNodeNeighbours();


        Comparator<Edge> comp = new Comparator<Edge>() {
            @Override
            public int compare(Edge o1, Edge o2) {
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
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node1", new ArrayList<>());
        Node node1 = new Node(1, "node2", new ArrayList<>());
        Node node2 = new Node(2, "node3", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);

        Graph g = new Graph("11111", "testsource", "node1 node2 node3 node4", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());

        ArrayList<Edge> emptyArray = new ArrayList<>();

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
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node1", new ArrayList<>());
        Node node1 = new Node(1, "node2", new ArrayList<>());
        Node node2 = new Node(2, "node3", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);


        Edge edge0 = new Edge(0, 1, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(1, 2, "testlabel1", "testpostlabel1");
        Edge edge2 = new Edge(2, 0, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);

        Graph g = new Graph("11111", "testsource", "node1 node2 node3 node4", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());

        ArrayList<Node> directedNeighbours = new ArrayList<>();
        ArrayList<Node> undirectedNeighbours = new ArrayList<>();
        ArrayList<Edge> edgeNeighbours = new ArrayList<>();

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

        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);

        Edge edge0 = new Edge(0, 1, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(0, 2, "testlabel1", "testpostlabel1");
        Edge edge2 = new Edge(2, 3, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());

        //Creating the array of Node neighbours for the nodes in the Graph.
        ArrayList<Node> nodeNeighbours0 = new ArrayList<>();
        ArrayList<Node> nodeNeighbours2 = new ArrayList<>();

        nodeNeighbours0.add(node1);
        nodeNeighbours0.add(node2);
        nodeNeighbours2.add(node3);

        //Setting Node neighbours without using setNodeNeighbours method.
        final Field nodeField0 = node0.getClass().getDeclaredField("directedNeighbours");
        nodeField0.setAccessible(true);
        nodeField0.set(node0, nodeNeighbours0);
        final Field nodeField2 = node2.getClass().getDeclaredField("directedNeighbours");
        nodeField2.setAccessible(true);
        nodeField2.set(node2, nodeNeighbours2);

        //Expected results for longest path for each Node as the start Node.
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


        assertTrue("directedLongestPaths method does not correctly finds the longest directed path for Node 0.", g.directedLongestPaths(0).equals(correctResult0));
        assertTrue("directedLongestPaths method does not correctly finds the longest directed path for Node 1.", g.directedLongestPaths(1).equals(correctResult1));
        assertTrue("directedLongestPaths method does not correctly finds the longest directed path for Node 2.", g.directedLongestPaths(2).equals(correctResult2));
        assertTrue("directedLongestPaths method does not correctly finds the longest directed path for Node 3.", g.directedLongestPaths(3).equals(correctResult3));

    }

    @Test
    public void test_directedLongestPaths_DirectedMultipleLongestPathFromStartNodeInAcyclicGraph() throws NoSuchFieldException, IllegalAccessException {

        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        Node node4 = new Node(4, "node4", new ArrayList<>());
        Node node5 = new Node(5, "node5", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);

        Edge edge0 = new Edge(0, 1, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(1, 2, "testlabel", "testpostlabel");
        Edge edge2 = new Edge(0, 3, "testlabel", "testpostlabel");
        Edge edge3 = new Edge(3, 4, "testlabel", "testpostlabel");
        Edge edge4 = new Edge(0, 5, "testlabel", "testpostlabel");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(edge4);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());

        //Creating the array of Node neighbours for the nodes in the Graph.
        ArrayList<Node> nodeNeighbours0 = new ArrayList<>();
        ArrayList<Node> nodeNeighbours1 = new ArrayList<>();
        ArrayList<Node> nodeNeighbours2 = new ArrayList<>();
        ArrayList<Node> nodeNeighbours3 = new ArrayList<>();

        nodeNeighbours0.add(node1);
        nodeNeighbours0.add(node3);
        nodeNeighbours0.add(node5);
        nodeNeighbours1.add(node2);
        nodeNeighbours3.add(node4);

        //Setting Node neighbours without using setNodeNeighbours method.
        final Field nodeField0 = node0.getClass().getDeclaredField("directedNeighbours");
        nodeField0.setAccessible(true);
        nodeField0.set(node0, nodeNeighbours0);
        final Field nodeField1 = node1.getClass().getDeclaredField("directedNeighbours");
        nodeField1.setAccessible(true);
        nodeField1.set(node1, nodeNeighbours1);
        final Field nodeField3 = node3.getClass().getDeclaredField("directedNeighbours");
        nodeField3.setAccessible(true);
        nodeField3.set(node3, nodeNeighbours3);

        //Expected results for longest path for each Node as the start Node.
        ArrayList<ArrayList<Integer>> correctResult0 = new ArrayList<>();
        correctResult0.add(new ArrayList<Integer>());
        correctResult0.get(0).add(2);
        correctResult0.get(0).add(1);
        correctResult0.get(0).add(0);
        correctResult0.add(new ArrayList<Integer>());
        correctResult0.get(1).add(4);
        correctResult0.get(1).add(3);
        correctResult0.get(1).add(0);


        assertTrue("directedLongestPaths method does not correctly find multiple longest directed paths from a start Node.", g.directedLongestPaths(0).equals(correctResult0));

    }

    @Test
    public void test_topologicalSort_SortsCorrectly() throws NoSuchFieldException, IllegalAccessException{
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        Node node4 = new Node(4, "node4", new ArrayList<>());
        Node node5 = new Node(5, "node5", new ArrayList<>());
        Node node6 = new Node(6, "node6", new ArrayList<>());
        Node node7 = new Node(7, "node7", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);
        nodes.put(6,node6);
        nodes.put(7,node7);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());

        //Creating the array of Node neighbours for the nodes in the Graph.
        ArrayList<Node> nodeNeighbours0 = new ArrayList<>();
        ArrayList<Node> nodeNeighbours1 = new ArrayList<>();
        ArrayList<Node> nodeNeighbours3 = new ArrayList<>();
        ArrayList<Node> nodeNeighbours4 = new ArrayList<>();

        nodeNeighbours0.add(node2);
        nodeNeighbours0.add(node3);
        nodeNeighbours0.add(node4);
        nodeNeighbours1.add(node2);
        nodeNeighbours1.add(node5);
        nodeNeighbours3.add(node5);
        nodeNeighbours4.add(node7);

        //Setting Node neighbours without using setNodeNeighbours method.
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

        //Expected results for longest path for each Node as the start Node.
        Stack<Integer> expectedResult = new Stack<Integer>();
        expectedResult.push(2);
        expectedResult.push(5);
        expectedResult.push(3);
        expectedResult.push(7);
        expectedResult.push(4);
        expectedResult.push(0);


        assertTrue("topologicalSort does not sort a Graph correctly for Node 0.", g.topologicalSort(0,visited,stack).equals(expectedResult));

        stack.clear();
        visited.put(0,false);
        visited.put(1,false);
        visited.put(2,false);
        visited.put(3,false);
        visited.put(4,false);
        visited.put(5,false);
        visited.put(6,false);
        visited.put(7,false);

        //Expected results for longest path for each Node as the start Node.
        expectedResult.clear();
        expectedResult.push(2);
        expectedResult.push(5);
        expectedResult.push(1);

        assertTrue("topologicalSort does not sort a Graph correctly for Node 1.", g.topologicalSort(1,visited,stack).equals(expectedResult));

        stack.clear();
        visited.put(0,false);
        visited.put(1,false);
        visited.put(2,false);
        visited.put(3,false);
        visited.put(4,false);
        visited.put(5,false);
        visited.put(6,false);
        visited.put(7,false);

        //Expected results for longest path for each Node as the start Node.
        expectedResult.clear();
        expectedResult.push(2);

        assertTrue("topologicalSort does not sort a Graph correctly for Node 2.", g.topologicalSort(2,visited,stack).equals(expectedResult));

        stack.clear();
        visited.put(0,false);
        visited.put(1,false);
        visited.put(2,false);
        visited.put(3,false);
        visited.put(4,false);
        visited.put(5,false);
        visited.put(6,false);
        visited.put(7,false);

        //Expected results for longest path for each Node as the start Node.
        expectedResult.clear();
        expectedResult.push(6);

        assertTrue("topologicalSort does not sort a Graph correctly for Node 6.", g.topologicalSort(6,visited,stack).equals(expectedResult));

        stack.clear();
        visited.put(0,false);
        visited.put(1,false);
        visited.put(2,false);
        visited.put(3,false);
        visited.put(4,false);
        visited.put(5,false);
        visited.put(6,false);
        visited.put(7,false);

        //Expected results for longest path for each Node as the start Node.
        expectedResult.clear();
        expectedResult.push(7);

        assertTrue("topologicalSort does not sort a Graph correctly for Node 7.", g.topologicalSort(7,visited,stack).equals(expectedResult));

    }

    @Test
    public void test_BFS_UndirectedSingleLongestPathFromStartNodeInAcyclicGraph() throws NoSuchFieldException, IllegalAccessException {

        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);

        Edge edge0 = new Edge(0, 1, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(2, 0, "testlabel1", "testpostlabel1");
        Edge edge2 = new Edge(2, 3, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());

        //Creating the array of directed and undirected Node neighbours for the nodes in the Graph.
        ArrayList<Node> directedNeighbours0 = new ArrayList<>();
        ArrayList<Node> directedNeighbours2 = new ArrayList<>();

        directedNeighbours0.add(node1);
        directedNeighbours0.add(node2);
        directedNeighbours2.add(node3);

        ArrayList<Node> undirectedNeighbours1 = new ArrayList<>();
        ArrayList<Node> undirectedNeighbours2 = new ArrayList<>();
        ArrayList<Node> undirectedNeighbours3 = new ArrayList<>();

        undirectedNeighbours1.add(node0);
        undirectedNeighbours2.add(node0);
        undirectedNeighbours3.add(node2);

        //Setting Node neighbours without using setNodeNeighbours method.
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

        //Expected results for longest path for each Node as the start Node.
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

        assertTrue("BFS longest path algorithm does not correctly finds the longest undirected path for Node 0.", g.BFS(0).equals(correctResult0));
        assertTrue("BFS longest path algorithm does not correctly finds the longest undirected path for Node 1.", g.BFS(1).equals(correctResult1));
        assertTrue("BFS longest path algorithm does not correctly finds the longest undirected path for Node 2.", g.BFS(2).equals(correctResult2));
        assertTrue("BFS longest path algorithm does not correctly finds the longest undirected path for Node 3.", g.BFS(3).equals(correctResult3));

    }

    @Test
    public void test_BFS_UndirectedMultipleLongestPathFromStartNodeInAcyclicGraph() throws NoSuchFieldException, IllegalAccessException {

        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        Node node4 = new Node(4, "node4", new ArrayList<>());
        Node node5 = new Node(5, "node5", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);

        Edge edge0 = new Edge(0, 1, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(1, 2, "testlabel", "testpostlabel");
        Edge edge2 = new Edge(3, 0, "testlabel", "testpostlabel");
        Edge edge3 = new Edge(3, 4, "testlabel", "testpostlabel");
        Edge edge4 = new Edge(0, 5, "testlabel", "testpostlabel");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(edge4);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());

        //Creating the array of Node neighbours for the nodes in the Graph.
        ArrayList<Node> directNeighbours0 = new ArrayList<>();
        ArrayList<Node> directNeighbours1 = new ArrayList<>();
        ArrayList<Node> directNeighbours3 = new ArrayList<>();

        directNeighbours0.add(node1);
        directNeighbours0.add(node3);
        directNeighbours0.add(node5);
        directNeighbours1.add(node2);
        directNeighbours3.add(node4);

        ArrayList<Node> undirectNeighbours1 = new ArrayList<>();
        ArrayList<Node> undirectNeighbours2 = new ArrayList<>();
        ArrayList<Node> undirectNeighbours5 = new ArrayList<>();
        ArrayList<Node> undirectNeighbours3 = new ArrayList<>();
        ArrayList<Node> undirectNeighbours4 = new ArrayList<>();

        undirectNeighbours1.add(node0);
        undirectNeighbours2.add(node1);
        undirectNeighbours5.add(node0);
        undirectNeighbours3.add(node0);
        undirectNeighbours4.add(node3);

        //Setting Node neighbours without using setNodeNeighbours method.
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

        //Expected results for longest path for each Node as the start Node.
        ArrayList<ArrayList<Integer>> correctResult0 = new ArrayList<>();
        correctResult0.add(new ArrayList<Integer>());
        correctResult0.get(0).add(2);
        correctResult0.get(0).add(1);
        correctResult0.get(0).add(0);
        correctResult0.add(new ArrayList<Integer>());
        correctResult0.get(1).add(4);
        correctResult0.get(1).add(3);
        correctResult0.get(1).add(0);


        assertTrue("BFS method does not correctly find multiple longest undirected paths from a start Node.", g.BFS(0).equals(correctResult0));

    }

    @Test
    public void test_findLongest_DirectedSingleLongestPath(){

        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);

        Edge edge0 = new Edge(1, 0, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(1, 2, "testlabel1", "testpostlabel1");
        Edge edge2 = new Edge(2, 3, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());


        //Expected result for longest path.
        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(0).add(1);
        correctResult.get(0).add(2);
        correctResult.get(0).add(3);

        assertTrue("findLongest path algorithm does not correctly find a single longest directed path in a Graph.", g.findLongest(true).equals(correctResult));

    }

    @Test
    public void test_findLongest_DirectedMultipleLongestPathsFromSingleStartNode()  {
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        Node node4 = new Node(4, "node4", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);

        Edge edge0 = new Edge(1, 0, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(1, 2, "testlabel1", "testpostlabel1");
        Edge edge2 = new Edge(2, 3, "testlabel2", "testpostlabel2");
        Edge edge3 = new Edge(0, 4, "testlabel3", "testpostlabel3");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());


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

        assertTrue("findLongest path algorithm does not correctly find multiple longest directed paths from a single Node in a Graph.", g.findLongest(true).equals(correctResult));
    }

    @Test
    public void test_findLongest_DirectedMultipleLongestPathsFromDifferentStartNodes() {
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);

        Edge edge0 = new Edge(0, 2, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(1, 2, "testlabel1", "testpostlabel1");
        Edge edge2 = new Edge(2, 3, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());


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

        assertTrue("findLongest path algorithm does not correctly find multiple longest directed paths from different start nodes in a Graph.", g.findLongest(true).equals(correctResult));
    }

    @Test
    public void test_findLongest_DirectedMultipleLongestPathsFromSameAndDifferentStartNodes()  {
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        Node node4 = new Node(4, "node4", new ArrayList<>());
        Node node5 = new Node(5, "node5", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);

        Edge edge0 = new Edge(0, 1, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(0, 2, "testlabel1", "testpostlabel1");
        Edge edge2 = new Edge(2, 3, "testlabel2", "testpostlabel2");
        Edge edge3 = new Edge(5, 2, "testlabel2", "testpostlabel2");
        Edge edge4 = new Edge(1, 4, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(edge4);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());

        //Expected results for longest path for each Node as the start Node.
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

        assertTrue("findLongest path algorithm does not correctly find multiple longest directed paths from the same and different start nodes in a Graph.", g.findLongest(true).equals(correctResult));
    }

    @Test
    public void test_findLongest_UndirectedSingleLongestPath()  {

        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);

        Edge edge0 = new Edge(1, 0, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(1, 2, "testlabel1", "testpostlabel1");
        Edge edge2 = new Edge(3, 2, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());


        //Expected result for longest path.
        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(0).add(3);
        correctResult.get(0).add(2);
        correctResult.get(0).add(1);
        correctResult.get(0).add(0);


        assertTrue("findLongest path algorithm does not correctly find a single longest undirected path in a Graph.", g.findLongest(false).equals(correctResult));

    }

    @Test
    public void test_findLongest_UndirectedMultipleLongestPathsFromSingleStartNode()  {
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        Node node4 = new Node(4, "node4", new ArrayList<>());
        Node node5 = new Node(5, "node5", new ArrayList<>());
        Node node6 = new Node(6, "node6", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);
        nodes.put(6,node6);

        Edge edge0 = new Edge(0, 1, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(1, 2, "testlabel1", "testpostlabel1");
        Edge edge2 = new Edge(2, 3, "testlabel2", "testpostlabel2");
        Edge edge3 = new Edge(0, 4, "testlabel3", "testpostlabel3");
        Edge edge4 = new Edge(4, 5, "testlabel3", "testpostlabel3");
        Edge edge5 = new Edge(4, 6, "testlabel3", "testpostlabel3");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(edge4);
        edges.add(edge5);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());


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

        assertTrue("findLongest path algorithm does not correctly find multiple longest undirected paths from a single Node in a Graph.", g.findLongest(false).equals(correctResult));
    }

    @Test
    public void test_findLongest_UndirectedMultipleLongestPathsFromDifferentStartNodes()  {
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node4", new ArrayList<>());
        Node node3 = new Node(3, "node5", new ArrayList<>());
        Node node4 = new Node(4, "node6", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);

        Edge edge0 = new Edge(0, 1, "testlabel", "testpostlabel");
        Edge edge3 = new Edge(0, 2, "testlabel3", "testpostlabel3");
        Edge edge4 = new Edge(2, 3, "testlabel3", "testpostlabel3");
        Edge edge5 = new Edge(2, 4, "testlabel3", "testpostlabel3");
        edges.add(edge0);
        edges.add(edge3);
        edges.add(edge4);
        edges.add(edge5);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());


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

        assertTrue("findLongest path algorithm does not correctly find multiple longest undirected paths from a single Node in a Graph.", g.findLongest(false).equals(correctResult));
    }

    @Test
    public void test_findLongest_UndirectedMultipleLongestPathsFromSameAndDifferentStartNodes() {
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        Node node4 = new Node(4, "node4", new ArrayList<>());
        Node node5 = new Node(5, "node5", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);

        Edge edge0 = new Edge(0, 2, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(1, 2, "testlabel1", "testpostlabel1");
        Edge edge2 = new Edge(2, 3, "testlabel2", "testpostlabel2");
        Edge edge3 = new Edge(4, 3, "testlabel3", "testpostlabel3");
        Edge edge4 = new Edge(3, 5, "testlabel3", "testpostlabel3");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(edge4);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());

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

        assertTrue("findLongest path algorithm does not correctly find multiple longest undirected paths from a single Node in a Graph.", g.findLongest(false).equals(correctResult));
    }

    @Test
    public void test_findLongest_UndirectedMultipleLongestPathsInSymmetricalGraphEdgeCase(){
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        Node node4 = new Node(4, "node4", new ArrayList<>());
        Node node5 = new Node(5, "node5", new ArrayList<>());
        Node node6 = new Node(6, "node6", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);
        nodes.put(6,node6);

        Edge edge0 = new Edge(0, 1, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(2, 0, "testlabel1", "testpostlabel1");
        Edge edge2 = new Edge(1, 4, "testlabel2", "testpostlabel2");
        Edge edge3 = new Edge(5, 1, "testlabel3", "testpostlabel3");
        Edge edge4 = new Edge(2, 3, "testlabel3", "testpostlabel3");
        Edge edge5 = new Edge(2, 6, "testlabel3", "testpostlabel3");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(edge4);
        edges.add(edge5);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());


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

       assertTrue("findLongest path algorithm does not correctly find multiple longest undirected paths in a Graph that is perfectly symmetrical around Node 0.", g.findLongest(false).equals(correctResult));
    }

    @Test
    public void test_findLongest_UndirectedSingleLongestPathInDisconnectedGraphCase1(){
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        Node node4 = new Node(4, "node4", new ArrayList<>());
        Node node5 = new Node(5, "node5", new ArrayList<>());
        Node node6 = new Node(6, "node6", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);
        nodes.put(6,node6);

        Edge edge0 = new Edge(1, 0, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(1, 2, "testlabel1", "testpostlabel1");
        Edge edge2 = new Edge(4, 5, "testlabel2", "testpostlabel2");

        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());


        //Expected result for longest path.
        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();
        correctResult.add(new ArrayList<>());
        correctResult.get(0).add(0);
        correctResult.get(0).add(1);
        correctResult.get(0).add(2);


        assertTrue("findLongest path algorithm does not correctly find a single longest undirected path in a disconnected Graph #1.", g.findLongest(false).equals(correctResult));

    }

    @Test
    public void test_findLongest_UndirectedSingleLongestPathInDisconnectedGraphCase2(){
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        Node node4 = new Node(4, "node4", new ArrayList<>());
        Node node5 = new Node(5, "node5", new ArrayList<>());
        Node node6 = new Node(6, "node6", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);
        nodes.put(6,node6);

        Edge edge0 = new Edge(1, 0, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(1, 2, "testlabel1", "testpostlabel1");
        Edge edge2 = new Edge(4, 5, "testlabel2", "testpostlabel2");
        Edge edge3 = new Edge(6, 4, "testlabel2", "testpostlabel2");
        Edge edge4 = new Edge(5, 3, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(edge4);

        Graph g2 = new Graph("11111", "testsource", "testInput", new HashMap<Integer, Node>(nodes), new ArrayList<Token>(),  new ArrayList<>(edges), new ArrayList<Integer>());

        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();
        correctResult.add(new ArrayList<>());
        correctResult.get(0).add(3);
        correctResult.get(0).add(5);
        correctResult.get(0).add(4);
        correctResult.get(0).add(6);

        assertTrue("findLongest path algorithm does not correctly find a single longest undirected path in a disconnected Graph #2.", g2.findLongest(false).equals(correctResult));
    }

    @Test
    public void test_findLongest_UndirectedMultipleLongestPathInDisconnectedGraph(){
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        Node node4 = new Node(4, "node4", new ArrayList<>());
        Node node5 = new Node(5, "node5", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);

        Edge edge0 = new Edge(0, 1, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(0, 2, "testlabel1", "testpostlabel1");
        Edge edge2 = new Edge(4, 5, "testlabel2", "testpostlabel2");
        Edge edge4 = new Edge(5, 3, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge4);

        Graph g2 = new Graph("11111", "testsource", "testInput", new HashMap<Integer, Node>(nodes), new ArrayList<Token>(),  new ArrayList<>(edges), new ArrayList<Integer>());

        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();
        correctResult.add(new ArrayList<>());
        correctResult.get(0).add(1);
        correctResult.get(0).add(0);
        correctResult.get(0).add(2);
        correctResult.add(new ArrayList<>());
        correctResult.get(1).add(3);
        correctResult.get(1).add(5);
        correctResult.get(1).add(4);

        assertTrue("findLongest path algorithm does not correctly find multiple longest undirected paths in a disconnected Graph.", g2.findLongest(false).equals(correctResult));
    }

    @Test
    public void test_findLongest_UndirectedMultipleLongestPathInComplexDisconnectedGraph(){
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        Node node4 = new Node(4, "node4", new ArrayList<>());
        Node node5 = new Node(5, "node5", new ArrayList<>());
        Node node6 = new Node(6, "node6", new ArrayList<>());
        Node node7 = new Node(7, "node7", new ArrayList<>());
        Node node8 = new Node(8, "node8", new ArrayList<>());
        Node node9 = new Node(9, "node0", new ArrayList<>());
        Node node10 = new Node(10, "node10", new ArrayList<>());
        Node node11 = new Node(11, "node11", new ArrayList<>());
        Node node12 = new Node(12, "node11", new ArrayList<>());
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

        Edge edge0 = new Edge(1, 0, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(1, 2, "testlabel1", "testpostlabel1");
        Edge edge2 = new Edge(12, 5, "testlabel2", "testpostlabel2");
        Edge edge3 = new Edge(6, 1, "testlabel2", "testpostlabel2");
        Edge edge4 = new Edge(5, 3, "testlabel2", "testpostlabel2");
        Edge edge5 = new Edge(7, 3, "testlabel2", "testpostlabel2");
        Edge edge6 = new Edge(8, 3, "testlabel2", "testpostlabel2");
        Edge edge7 = new Edge(2, 10, "testlabel2", "testpostlabel2");
        Edge edge8 = new Edge(9, 6, "testlabel2", "testpostlabel2");
        Edge edge9 = new Edge(0, 11, "testlabel2", "testpostlabel2");
        Edge edge10 = new Edge(4, 12, "testlabel2", "testpostlabel2");
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

        Graph g2 = new Graph("11111", "testsource", "testInput", new HashMap<Integer, Node>(nodes), new ArrayList<Token>(),  new ArrayList<>(edges), new ArrayList<Integer>());

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


        assertTrue("findLongest path algorithm does not correctly find multiple longest undirected paths in a complex disconnected Graph.", g2.findLongest(false).equals(correctResult));
    }

    @Test
    public void test_findLongest_OneEdgeInGraph() {
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);

        Edge edge0 = new Edge(0, 1, "testlabel", "testpostlabel");
        edges.add(edge0);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());

        //Expected result for longest path.
        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();
        correctResult.add(new ArrayList<Integer>());
        correctResult.get(0).add(0);
        correctResult.get(0).add(1);

        assertTrue("findLongest path algorithm does not correctly return the path of a single edged directed Graph.", g.findLongest(true).equals(correctResult));
        assertTrue("findLongest path algorithm does not correctly return the path of a single edged undirected Graph.", g.findLongest(false).equals(correctResult));
    }

    @Test
    public void test_findLongest_NoNodes() {
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), new ArrayList<>(), new ArrayList<Integer>());

        //Expected result for longest path.
        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();

        assertTrue("findLongest path algorithm does not correctly return the path in a directed Graph with no nodes.", g.findLongest(true).equals(correctResult));
        assertTrue("findLongest path algorithm does not correctly return the path in an undirected Graph with no nodes.", g.findLongest(false).equals(correctResult));
    }

    @Test
    public void test_findLongest_NoEdges()  {
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();


        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), new ArrayList<>(), new ArrayList<Integer>());

        //Expected result for longest path.
        ArrayList<ArrayList<Integer>> correctResult = new ArrayList<>();

        assertTrue("findLongest path algorithm does not correctly return the path in a directed Graph with no edges", g.findLongest(true).equals(correctResult));
        assertTrue("findLongest path algorithm does not correctly return the path in an undirected Graph with no edges.", g.findLongest(false).equals(correctResult));
    }

    @Test
    public void test_combineNeighbours_CombinesNeighboursCorrectly()throws NoSuchFieldException, IllegalAccessException {
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        Node node4 = new Node(4, "node4", new ArrayList<>());
        Node node5 = new Node(5, "node5", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());

        //Creating the array of Node neighbours for the nodes in the Graph.
        ArrayList<Node> directNeighbours0 = new ArrayList<>();

        directNeighbours0.add(node1);
        directNeighbours0.add(node3);
        directNeighbours0.add(node5);
        ArrayList<Node> undirectNeighbours0 = new ArrayList<>();

        undirectNeighbours0.add(node2);
        undirectNeighbours0.add(node4);

        //Setting Node neighbours without using setNodeNeighbours method.
        final Field directField0 = node0.getClass().getDeclaredField("directedNeighbours");
        directField0.setAccessible(true);
        directField0.set(node0, directNeighbours0);
        final Field undirectField0= node0.getClass().getDeclaredField("undirectedNeighbours");
        undirectField0.setAccessible(true);
        undirectField0.set(node0, undirectNeighbours0);

        //Expected results for longest path for each Node as the start Node.
        ArrayList<Node> correctResult0 = new ArrayList<>();
        correctResult0.add(node1);
        correctResult0.add(node3);
        correctResult0.add(node5);
        correctResult0.add(node2);
        correctResult0.add(node4);

        assertTrue("combineNodeNeighbours does not correctly combine undirected and directed Node neighbours", g.combineNeighbours(0).equals(correctResult0));

    }

    @Test
    public void test_combineNeighbours_NoUndirectedNeighbours()throws NoSuchFieldException, IllegalAccessException {
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        Node node4 = new Node(4, "node4", new ArrayList<>());
        Node node5 = new Node(5, "node5", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());

        //Creating the array of Node neighbours for the nodes in the Graph.
        ArrayList<Node> directNeighbours0 = new ArrayList<>();

        directNeighbours0.add(node1);
        directNeighbours0.add(node3);
        directNeighbours0.add(node5);
        ArrayList<Node> undirectNeighbours0 = new ArrayList<>();


        //Setting Node neighbours without using setNodeNeighbours method.
        final Field directField0 = node0.getClass().getDeclaredField("directedNeighbours");
        directField0.setAccessible(true);
        directField0.set(node0, directNeighbours0);
        final Field undirectField0= node0.getClass().getDeclaredField("undirectedNeighbours");
        undirectField0.setAccessible(true);
        undirectField0.set(node0, undirectNeighbours0);

        //Expected results for longest path for each Node as the start Node.
        ArrayList<Node> correctResult0 = new ArrayList<>();
        correctResult0.add(node1);
        correctResult0.add(node3);
        correctResult0.add(node5);

        assertTrue("combineNodeNeighbours does not correctly combine undirected and directed Node neighbours when a Node has no undirected neighbours", g.combineNeighbours(0).equals(correctResult0));

    }

    @Test
    public void test_combineNeighbours_NoDirectedNeighbours()throws NoSuchFieldException, IllegalAccessException {
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        Node node4 = new Node(4, "node4", new ArrayList<>());
        Node node5 = new Node(5, "node5", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());

        //Creating the array of Node neighbours for the nodes in the Graph.
        ArrayList<Node> directNeighbours0 = new ArrayList<>();

        ArrayList<Node> undirectNeighbours0 = new ArrayList<>();
        undirectNeighbours0.add(node2);
        undirectNeighbours0.add(node4);


        //Setting Node neighbours without using setNodeNeighbours method.
        final Field directField0 = node0.getClass().getDeclaredField("directedNeighbours");
        directField0.setAccessible(true);
        directField0.set(node0, directNeighbours0);
        final Field undirectField0= node0.getClass().getDeclaredField("undirectedNeighbours");
        undirectField0.setAccessible(true);
        undirectField0.set(node0, undirectNeighbours0);

        //Expected results for longest path for each Node as the start Node.
        ArrayList<Node> correctResult0 = new ArrayList<>();
        correctResult0.add(node2);
        correctResult0.add(node4);

        assertTrue("combineNodeNeighbours does not correctly combine undirected and directed Node neighbours when a Node has no directed neighbours", g.combineNeighbours(0).equals(correctResult0));

    }

    @Test
    public void test_combineNeighbours_NoNeighbours()throws NoSuchFieldException, IllegalAccessException {
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        Node node4 = new Node(4, "node4", new ArrayList<>());
        Node node5 = new Node(5, "node5", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());

        //Creating the array of Node neighbours for the nodes in the Graph.
        ArrayList<Node> directNeighbours0 = new ArrayList<>();

        ArrayList<Node> undirectNeighbours0 = new ArrayList<>();


        //Setting Node neighbours without using setNodeNeighbours method.
        final Field directField0 = node0.getClass().getDeclaredField("directedNeighbours");
        directField0.setAccessible(true);
        directField0.set(node0, directNeighbours0);
        final Field undirectField0= node0.getClass().getDeclaredField("undirectedNeighbours");
        undirectField0.setAccessible(true);
        undirectField0.set(node0, undirectNeighbours0);

        //Expected results for longest path for each Node as the start Node.
        ArrayList<Node> correctResult0 = new ArrayList<>();

        assertTrue("combineNodeNeighbours does not correctly combine undirected and directed Node neighbours when a Node has no neighbours", g.combineNeighbours(0).equals(correctResult0));

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

        Graph g = new Graph();

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

        Graph g = new Graph();

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
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();
        ArrayList<Token> tokens = new ArrayList<>();


        ArrayList<Anchors> anch1 = new ArrayList<Anchors>();
        anch1.add(new Anchors(0, 0));
        nodes.put(0,new Node(0, "Node" + (0 + 1), anch1));

        ArrayList<Anchors> anch2 = new ArrayList<Anchors>();
        anch2.add(new Anchors(1, 1));
        nodes.put(1,new Node(1, "Node" + (1 + 1), anch2));

        ArrayList<Anchors> anch3 = new ArrayList<Anchors>();
        anch3.add(new Anchors(2, 2));
        nodes.put(2,new Node(2, "Node" + (2 + 1), anch3));

        ArrayList<Anchors> anch4 = new ArrayList<Anchors>();
        anch4.add(new Anchors(3, 3));
        nodes.put(3,new Node(3, "Node" + (3 + 1), anch4));

        ArrayList<Anchors> anch5 = new ArrayList<Anchors>();
        anch5.add(new Anchors(4, 4));
        nodes.put(4,new Node(4, "Node" + (4 + 1), anch5));

        edges.add(new Edge(0, 1, "testlabel", "testpostlabel"));
        edges.add(new Edge(1, 3, "testlabel1", "testpostlabel1"));
        edges.add(new Edge(2, 4, "testlabel2", "testpostlabel2"));

        Graph g = new Graph("11111", "testsource", "node1 node2 node3 node4", nodes, tokens, edges, new ArrayList<Integer>());

        assertFalse("isPlanar Correctly identifies planar and non-planar graphs", g.GraphIsPlanar());

        edges.clear();
        edges.add(new Edge(0, 1, "testlabel", "testpostlabel"));
        edges.add(new Edge(1, 2, "testlabel1", "testpostlabel1"));
        edges.add(new Edge(2, 3, "testlabel2", "testpostlabel2"));
        edges.add(new Edge(3, 4, "testlabel2", "testpostlabel2"));

        assertTrue("isPlanar Correctly identifies planar and non-planar graphs", g.GraphIsPlanar());

        edges.clear();
        edges.add(new Edge(0, 2, "testlabel", "testpostlabel"));
        edges.add(new Edge(1, 3, "testlabel1", "testpostlabel1"));

        assertFalse("isPlanar Correctly identifies planar and non-planar graphs", g.GraphIsPlanar());

        edges.clear();
        edges.add(new Edge(0, 4, "testlabel", "testpostlabel"));
        edges.add(new Edge(1, 4, "testlabel1", "testpostlabel1"));
        edges.add(new Edge(2, 4, "testlabel2", "testpostlabel2"));
        edges.add(new Edge(3, 4, "testlabel2", "testpostlabel2"));

        assertTrue("isPlanar Correctly identifies planar and non-planar graphs", g.GraphIsPlanar());

        edges.clear();
        edges.add(new Edge(4, 0, "testlabel", "testpostlabel"));
        edges.add(new Edge(1, 0, "testlabel1", "testpostlabel1"));
        edges.add(new Edge(2, 0, "testlabel2", "testpostlabel2"));
        edges.add(new Edge(3, 0, "testlabel2", "testpostlabel2"));

        assertTrue("isPlanar Correctly identifies planar and non-planar graphs", g.GraphIsPlanar());


    }

    @Test
    public void test_isPlanar_HandlesNodesWithSameToken() {
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();
        ArrayList<Token> tokens = new ArrayList<>();


        ArrayList<Anchors> anch1 = new ArrayList<Anchors>();
        anch1.add(new Anchors(0, 0));
        nodes.put(0,new Node(0, "Node" + (0 + 1), anch1));

        ArrayList<Anchors> anch2 = new ArrayList<Anchors>();
        anch2.add(new Anchors(0, 0));
        nodes.put(1,new Node(1, "Node" + (1 + 1), anch2));

        ArrayList<Anchors> anch3 = new ArrayList<Anchors>();
        anch3.add(new Anchors(1, 1));
        nodes.put(2,new Node(2, "Node" + (2 + 1), anch3));

        ArrayList<Anchors> anch4 = new ArrayList<Anchors>();
        anch4.add(new Anchors(2, 2));
        nodes.put(3,new Node(3, "Node" + (3 + 1), anch4));

        ArrayList<Anchors> anch5 = new ArrayList<Anchors>();
        anch5.add(new Anchors(3, 3));
        nodes.put(4,new Node(4, "Node" + (4 + 1), anch5));

        edges.add(new Edge(0, 2, "testlabel", "testpostlabel"));
        edges.add(new Edge(1, 3, "testlabel1", "testpostlabel1"));


        Graph g = new Graph("11111", "testsource", "node1 node2 node3 node4", nodes, tokens, edges, new ArrayList<Integer>());

        assertTrue("isPlanar Correctly identifies planar and non-planar graphs with duplicate Token references", g.GraphIsPlanar());

        edges.clear();
        edges.add(new Edge(0, 3, "testlabel", "testpostlabel"));
        edges.add(new Edge(2, 3, "testlabel1", "testpostlabel1"));
        edges.add(new Edge(1, 3, "testlabel2", "testpostlabel2"));
        edges.add(new Edge(3, 4, "testlabel2", "testpostlabel2"));

        assertTrue("isPlanar Correctly identifies planar and non-planar graphs with duplicate Token references", g.GraphIsPlanar());

        anch1 = new ArrayList<Anchors>();
        anch1.add(new Anchors(0, 0));
        nodes.clear();
        nodes.put(0,new Node(0, "Node" + (0 + 1), anch1));

        anch2 = new ArrayList<Anchors>();
        anch2.add(new Anchors(0, 0));
        nodes.put(1,new Node(1, "Node" + (1 + 1), anch2));

        anch3 = new ArrayList<Anchors>();
        anch3.add(new Anchors(1, 1));
        nodes.put(2,new Node(2, "Node" + (2 + 1), anch3));

        anch4 = new ArrayList<Anchors>();
        anch4.add(new Anchors(1, 1));
        nodes.put(3,new Node(3, "Node" + (3 + 1), anch4));

        anch5 = new ArrayList<Anchors>();
        anch5.add(new Anchors(2, 2));
        nodes.put(4,new Node(4, "Node" + (4 + 1), anch5));

        ArrayList<Anchors> anch6 = new ArrayList<Anchors>();
        anch6.add(new Anchors(3, 3));
        nodes.put(5,new Node(5, "Node" + (5 + 1), anch6));

        edges.clear();
        edges.add(new Edge(0, 3, "testlabel", "testpostlabel"));
        edges.add(new Edge(1, 4, "testlabel1", "testpostlabel1"));
        edges.add(new Edge(2, 4, "testlabel2", "testpostlabel2"));

        assertTrue("isPlanar Correctly identifies planar and non-planar graphs with duplicate Token references", g.GraphIsPlanar());

        edges.clear();
        edges.add(new Edge(0, 3, "testlabel", "testpostlabel"));
        edges.add(new Edge(1, 4, "testlabel1", "testpostlabel1"));
        edges.add(new Edge(2, 4, "testlabel2", "testpostlabel2"));
        edges.add(new Edge(3, 5, "testlabel2", "testpostlabel2"));

        assertFalse("isPlanar Correctly identifies planar and non-planar graphs with duplicate Token references", g.GraphIsPlanar());


    }

    @Test
    public void test_isPlanar_HandlesNoEdgesInGraph() {
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();
        ArrayList<Token> tokens = new ArrayList<>();


        ArrayList<Anchors> anch1 = new ArrayList<Anchors>();
        anch1.add(new Anchors(0, 0));
        nodes.put(0,new Node(0, "Node" + (0 + 1), anch1));

        ArrayList<Anchors> anch2 = new ArrayList<Anchors>();
        anch2.add(new Anchors(1, 1));
        nodes.put(1,new Node(1, "Node" + (1 + 1), anch2));

        ArrayList<Anchors> anch3 = new ArrayList<Anchors>();
        anch3.add(new Anchors(2, 2));
        nodes.put(2,new Node(2, "Node" + (2 + 1), anch3));

        ArrayList<Anchors> anch4 = new ArrayList<Anchors>();
        anch4.add(new Anchors(3, 3));
        nodes.put(3,new Node(3, "Node" + (3 + 1), anch4));

        ArrayList<Anchors> anch5 = new ArrayList<Anchors>();
        anch5.add(new Anchors(4, 4));
        nodes.put(4,new Node(4, "Node" + (4 + 1), anch5));


        Graph g = new Graph("11111", "testsource", "node1 node2 node3 node4", nodes, tokens, edges, new ArrayList<Integer>());

        assertTrue("isPlanar Correctly identifies planar and non-planar graphs", g.GraphIsPlanar());

    }

    @Test
    public void test_isPlanar_HandlesNoNodesInGraph() {
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();
        ArrayList<Token> tokens = new ArrayList<>();

        Graph g = new Graph("11111", "testsource", "node1 node2 node3 node4", nodes, tokens, edges, new ArrayList<Integer>());

        assertTrue("isPlanar Correctly identifies planar and non-planar graphs", g.GraphIsPlanar());

    }

    @Test
    public void test_getTokenSpan_getTokensCorrectly(){
        ArrayList<Token> tokens = new ArrayList<>();

        Token token0 = new Token(0,"form0", "lemma0", "carg0");
        Token token1 = new Token(1,"form1", "lemma1", "carg1");
        Token token2 = new Token(2,"form2", "lemma2", "carg2");
        Token token3 = new Token(3,"form3", "lemma3", "carg3");
        Token token4 = new Token(4,"form4", "lemma4", "carg4");
        Token token5 = new Token(5,"form5", "lemma5", "carg5");
        tokens.add(token0);
        tokens.add(token1);
        tokens.add(token2);
        tokens.add(token3);
        tokens.add(token4);
        tokens.add(token5);

        Graph g = new Graph("1","source","input",new HashMap<Integer, Node>(),tokens,new ArrayList<Edge>(),new ArrayList<Integer>());

        ArrayList<Token> expected = new ArrayList<>();
        expected.add(token1);
        expected.add(token2);
        expected.add(token3);

        assertTrue("getTokenSpan does not get a range of tokens correctly.",g.getTokenSpan(1,3).equals(expected));

    }

    @Test
    public void test_getTokenSpan_noTokensInGraph(){

        Graph g = new Graph("1","source","input",new HashMap<Integer, Node>(),new ArrayList<Token>(),new ArrayList<Edge>(),new ArrayList<Integer>());

        ArrayList<Token> expected = new ArrayList<>();

        assertTrue("getTokenSpan does not correctly retrieve tokens from an empty Graph.",g.getTokenSpan(0,-1).equals(expected));
    }

    @Test
    public void test_getTokenSpan_GetOneTokenOnly(){
        ArrayList<Token> tokens = new ArrayList<>();

        Token token0 = new Token(0,"form0", "lemma0", "carg0");
        Token token1 = new Token(1,"form1", "lemma1", "carg1");
        Token token2 = new Token(2,"form2", "lemma2", "carg2");
        tokens.add(token0);
        tokens.add(token1);
        tokens.add(token2);

        Graph g = new Graph("1","source","input",new HashMap<Integer, Node>(),tokens,new ArrayList<Edge>(),new ArrayList<Integer>());

        ArrayList<Token> expected = new ArrayList<>();
        expected.add(token1);

        assertTrue("getTokenSpan does not get a single Token correctly.",g.getTokenSpan(1,1).equals(expected));
    }

    @Test
    public void test_getTokenInput_GetsTokenInputCorrectly(){
        ArrayList<Token> tokens = new ArrayList<>();

        Token token0 = new Token(0,"form0", "lemma0", "carg0");
        Token token1 = new Token(1,"form1", "lemma1", "carg1");
        Token token2 = new Token(2,"form2", "lemma2", "carg2");
        Token token3 = new Token(3,"form3", "lemma3", "carg3");
        Token token4 = new Token(4,"form4", "lemma4", "carg4");
        Token token5 = new Token(5,"form5", "lemma5", "carg5");
        tokens.add(token0);
        tokens.add(token1);
        tokens.add(token2);
        tokens.add(token3);
        tokens.add(token4);
        tokens.add(token5);

        Graph g = new Graph("1","source","input",new HashMap<Integer, Node>(),tokens,new ArrayList<Edge>(),new ArrayList<Integer>());

        String expected = "form0 form1 form2 form3 form4 form5";

        assertTrue("getTokenInput does not get tokens' forms correctly.",g.getTokenInput(tokens).equals(expected));
    }

    @Test
    public void test_getTokenInput_EmptyTokenArray(){
        ArrayList<Token> tokens = new ArrayList<>();


        Graph g = new Graph("1","source","input",new HashMap<Integer, Node>(),tokens,new ArrayList<Edge>(),new ArrayList<Integer>());

        String expected = "" ;

        assertTrue("getTokenInput does not get tokens' forms correctly from an empty array of tokens.",g.getTokenInput(tokens).equals(expected));
    }

    @Test
    public void test_connectedBFS_IdentifiesConnectedGraph(){
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        Node node4 = new Node(4, "node4", new ArrayList<>());
        Node node5 = new Node(5, "node5", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);

        Edge edge0 = new Edge(0, 1, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(1, 2, "testlabel", "testpostlabel");
        Edge edge2 = new Edge(3, 0, "testlabel", "testpostlabel");
        Edge edge3 = new Edge(3, 4, "testlabel", "testpostlabel");
        Edge edge4 = new Edge(0, 5, "testlabel", "testpostlabel");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(edge4);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());

        assertTrue("connectedBFS method does not correctly identify a connected Graph.", g.connectedBFS(0));

    }

    @Test
    public void test_connectedBFS_IdentifiesDisconnectedGraph(){
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        Node node4 = new Node(4, "node4", new ArrayList<>());
        Node node5 = new Node(5, "node5", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);

        Edge edge0 = new Edge(0, 1, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(1, 2, "testlabel", "testpostlabel");
        Edge edge2 = new Edge(3, 0, "testlabel", "testpostlabel");
        Edge edge3 = new Edge(3, 4, "testlabel", "testpostlabel");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());

        assertFalse("connectedBFS method does not correctly identify a disconnected Graph.", g.connectedBFS(0));

    }

    @Test
    public void test_connectedBFS_Node0IsDisconnected(){
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);

        Edge edge1 = new Edge(1, 2, "testlabel", "testpostlabel");
        edges.add(edge1);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());

        assertFalse("connectedBFS method does not correctly identify a disconnected Graph where Node 0 is disconnected.", g.connectedBFS(0));

    }

    @Test
    public void test_connectedBFS_SingleNodeGraph(){
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        nodes.put(0,node0);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), new ArrayList<Edge>(), new ArrayList<Integer>());

        assertTrue("connectedBFS method does not correctly identify a connected Graph with a single Node.", g.connectedBFS(0));

    }

    @Test
    public void test_hasDanglingEdge_ReturnsTrueIfGraphHasDanglingEdge() {
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        nodes.put(0, node0);
        edges.add(new Edge(0, 1, "", ""));

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());

        assertTrue("Has Dangling Edge Has Not Correctly Identified dangling Edge", g.hasDanglingEdge());
    }

    @Test
    public void test_PlanarGraph_CorrectlyConstructsGraph() {

        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();
        ArrayList anch1 = new ArrayList();
        ArrayList anch2 = new ArrayList();
        ArrayList anch3 = new ArrayList();
        ArrayList anch4 = new ArrayList();
        Anchors anchor1 = new Anchors(0, 0);
        Anchors anchor2 = new Anchors(1, 1);
        Anchors anchor3 = new Anchors(2, 2);
        Anchors anchor4 = new Anchors(3, 3);
        anch1.add(anchor1);
        anch2.add(anchor2);
        anch3.add(anchor3);
        anch4.add(anchor4);

        Node node0 = new Node(0, "node0", anch2);
        Node node1 = new Node(1, "node1", anch1);
        Node node2 = new Node(2, "node2", anch4);
        Node node3 = new Node(3, "node3", anch3);
        /*
        node1
        node0
        node3
        node2
         */
        nodes.put(0, node0);
        nodes.put(1, node1);
        nodes.put(2, node2);
        nodes.put(3, node3);

        Edge edge0 = new Edge(0, 1, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(1, 2, "testlabel1", "testpostlabel1");
        Edge edge2 = new Edge(2, 3, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);

        Graph g = new Graph("11111", "testsource", "testInput", nodes, new ArrayList<Token>(), edges, new ArrayList<Integer>());


        ArrayList<Edge> expectedEdges = new ArrayList<>();
        Edge UpdatedEdge0 = new Edge(1, 0, "", "");
        Edge UpdatedEdge1 = new Edge(0, 3, "", "");
        Edge UpdatedEdge2 = new Edge(3, 2, "", "");
        expectedEdges.add(UpdatedEdge0);
        expectedEdges.add(UpdatedEdge1);
        expectedEdges.add(UpdatedEdge2);

        assertEquals("Planar Graph was not constructed correctly", g.PlanarGraph().getEdges().size(), expectedEdges.size());

        assertEquals("Planar Graph was not constructed correctly", g.PlanarGraph().getEdges(), expectedEdges);

    }

    @Test
    public void test_isCyclic_CorrectlyIdentifiesCycleInDirectedGraph(){
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        Node node4 = new Node(4, "node4", new ArrayList<>());
        Node node5 = new Node(5, "node5", new ArrayList<>());
        Node node6 = new Node(6, "node6", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);
        nodes.put(6,node6);

        Edge edge0 = new Edge(1, 0, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(0, 2, "testlabel1", "testpostlabel1");
        Edge edge2 = new Edge(0, 3, "testlabel2", "testpostlabel2");
        Edge edge3 = new Edge(2, 1, "testlabel2", "testpostlabel2");
        Edge edge4 = new Edge(1, 6, "testlabel2", "testpostlabel2");
        Edge edge5 = new Edge(2, 5, "testlabel2", "testpostlabel2");
        Edge edge6 = new Edge(2, 4, "testlabel2", "testpostlabel2");
        Edge edge7 = new Edge(4, 5, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(edge4);
        edges.add(edge5);
        edges.add(edge6);
        edges.add(edge7);

        Graph g2 = new Graph("11111", "testsource", "testInput", new HashMap<Integer, Node>(nodes), new ArrayList<Token>(),  new ArrayList<>(edges), new ArrayList<Integer>());

        assertTrue("isCylic failed to find a cycle in a directed Graph.", g2.isCyclic(true));
    }

    @Test
    public void test_isCyclic_CorrectlyIdentifiesNoCycleInDirectedGraph(){
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        Node node4 = new Node(4, "node4", new ArrayList<>());
        Node node5 = new Node(5, "node5", new ArrayList<>());
        Node node6 = new Node(6, "node6", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);
        nodes.put(6,node6);

        Edge edge0 = new Edge(1, 0, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(0, 2, "testlabel1", "testpostlabel1");
        Edge edge2 = new Edge(0, 3, "testlabel2", "testpostlabel2");
        Edge edge3 = new Edge(1, 2, "testlabel2", "testpostlabel2");
        Edge edge4 = new Edge(1, 6, "testlabel2", "testpostlabel2");
        Edge edge5 = new Edge(2, 5, "testlabel2", "testpostlabel2");
        Edge edge6 = new Edge(2, 4, "testlabel2", "testpostlabel2");
        Edge edge7 = new Edge(4, 5, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge3);
        edges.add(edge4);
        edges.add(edge5);
        edges.add(edge6);
        edges.add(edge7);

        Graph g2 = new Graph("11111", "testsource", "testInput", new HashMap<Integer, Node>(nodes), new ArrayList<Token>(),  new ArrayList<>(edges), new ArrayList<Integer>());

        assertFalse("isCylic failed to find a no cycles in a directed Graph with no cycles.", g2.isCyclic(true));
    }

    @Test
    public void test_isCyclic_CorrectlyIdentifiesCycleInUndirectedGraph(){
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        Node node4 = new Node(4, "node4", new ArrayList<>());
        Node node5 = new Node(5, "node5", new ArrayList<>());
        Node node6 = new Node(6, "node6", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);
        nodes.put(6,node6);

        Edge edge0 = new Edge(1, 0, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(0, 2, "testlabel1", "testpostlabel1");
        Edge edge2 = new Edge(0, 3, "testlabel2", "testpostlabel2");
        Edge edge4 = new Edge(1, 6, "testlabel2", "testpostlabel2");
        Edge edge5 = new Edge(2, 5, "testlabel2", "testpostlabel2");
        Edge edge6 = new Edge(2, 4, "testlabel2", "testpostlabel2");
        Edge edge7 = new Edge(4, 5, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge4);
        edges.add(edge5);
        edges.add(edge6);
        edges.add(edge7);

        Graph g2 = new Graph("11111", "testsource", "testInput", new HashMap<Integer, Node>(nodes), new ArrayList<Token>(),  new ArrayList<>(edges), new ArrayList<Integer>());

        assertTrue("isCylic failed to find a cycle in a undirected Graph.", g2.isCyclic(false));
    }

    @Test
    public void test_isCyclic_CorrectlyIdentifiesNoCyclesInUndirectedGraph(){
        //Creating the nodes and edges for the Graph
        HashMap<Integer, Node> nodes = new HashMap<Integer, Node>();
        ArrayList<Edge> edges = new ArrayList<>();

        Node node0 = new Node(0, "node0", new ArrayList<>());
        Node node1 = new Node(1, "node1", new ArrayList<>());
        Node node2 = new Node(2, "node2", new ArrayList<>());
        Node node3 = new Node(3, "node3", new ArrayList<>());
        Node node4 = new Node(4, "node4", new ArrayList<>());
        Node node5 = new Node(5, "node5", new ArrayList<>());
        Node node6 = new Node(6, "node6", new ArrayList<>());
        nodes.put(0,node0);
        nodes.put(1,node1);
        nodes.put(2,node2);
        nodes.put(3,node3);
        nodes.put(4,node4);
        nodes.put(5,node5);
        nodes.put(6,node6);

        Edge edge0 = new Edge(1, 0, "testlabel", "testpostlabel");
        Edge edge1 = new Edge(0, 2, "testlabel1", "testpostlabel1");
        Edge edge2 = new Edge(0, 3, "testlabel2", "testpostlabel2");
        Edge edge4 = new Edge(1, 6, "testlabel2", "testpostlabel2");
        Edge edge5 = new Edge(2, 5, "testlabel2", "testpostlabel2");
        Edge edge6 = new Edge(2, 4, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);
        edges.add(edge4);
        edges.add(edge5);
        edges.add(edge6);

        Graph g2 = new Graph("11111", "testsource", "testInput", new HashMap<Integer, Node>(nodes), new ArrayList<Token>(),  new ArrayList<>(edges), new ArrayList<Integer>());

        assertFalse("isCylic failed to find no cycle in a undirected Graph with no cycles.", g2.isCyclic(false));
    }

    @Test
    public void test_Constructor_AcceptArrayAndConvertToHashMapCorrectly()throws NoSuchFieldException, IllegalAccessException{

        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Edge> edges = new ArrayList<>();
        ArrayList<Token> tokens = new ArrayList<>();

        nodes.add(new Node(0, "node1", new ArrayList<Anchors>()));
        nodes.add(new Node(1, "node2", new ArrayList<Anchors>()));
        nodes.add(new Node(2, "node3", new ArrayList<Anchors>()));
        nodes.add(new Node(3, "node4", new ArrayList<Anchors>()));

        edges.add(new Edge(0, 1, "testlabel", "testpostlabel"));
        edges.add(new Edge(1, 3, "testlabel1", "testpostlabel1"));
        edges.add(new Edge(0, 2, "testlabel2", "testpostlabel2"));

        tokens.add(new Token(0, "node1 form", "node1 lemma", "node1 carg"));
        tokens.add(new Token(1, "node2 form", "node2 lemma", "node2 carg"));
        tokens.add(new Token(2, "node3 form", "node3 lemma", "node3 carg"));
        tokens.add(new Token(3, "node4 form", "node4 lemma", "node4 carg"));

        Graph g = new Graph("11111", "testsource", "node1 node2 node3 node4", nodes, tokens, edges, new ArrayList<Integer>());

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

        HashMap<Integer, Node> ExpectedNodes = new HashMap<Integer, Node>();

        ExpectedNodes.put(0, new Node(0, "node1", new ArrayList<Anchors>()));
        ExpectedNodes.put(1, new Node(1, "node2", new ArrayList<Anchors>()));
        ExpectedNodes.put(2, new Node(2, "node3", new ArrayList<Anchors>()));
        ExpectedNodes.put(3, new Node(3, "node4", new ArrayList<Anchors>()));

        assertEquals("id value was not constructed properly.", fieldID.get(g), "11111");
        assertEquals("source value was not constructed properly.", fieldSource.get(g), "testsource");
        assertEquals("input value was not constructed properly.", fieldInput.get(g), "node1 node2 node3 node4");
        assertEquals("nodes value was not constructed properly.", fieldNodes.get(g), ExpectedNodes);
        assertEquals("tokens value was not constructed properly.", fieldTokens.get(g), tokens);
        assertEquals("edges value was not constructed properly.", fieldEdges.get(g), edges);
    }

}
