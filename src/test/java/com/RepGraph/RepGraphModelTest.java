package com.RepGraph;

import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;


public class RepGraphModelTest {

    private graph g1, g2, g3, g4;

    @Before
    public void construct_test_graph(){
        ArrayList<node> nodes1 = new ArrayList<>();
        ArrayList<node> nodes2 = new ArrayList<>();
        ArrayList<node> nodes3 = new ArrayList<>();
        ArrayList<node> nodes4 = new ArrayList<>();
        ArrayList<edge> edges1 = new ArrayList<>();
        ArrayList<token> tokens1 = new ArrayList<>();
        ArrayList<token> tokens2 = new ArrayList<>();
        ArrayList<token> tokens3 = new ArrayList<>();
        ArrayList<token> tokens4 = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            nodes1.add(new node(i, "node" + (i + 1), new ArrayList<anchors>()));
            nodes2.add(new node(i, "node" + (i + 2), new ArrayList<anchors>()));
            nodes3.add(new node(i, "node" + (i + 3), new ArrayList<anchors>()));
            nodes4.add(new node(i, "node" + (7), new ArrayList<anchors>()));
        }

        edges1.add(new edge(0, 1, "testlabel", "testpostlabel"));
        edges1.add(new edge(1, 3, "testlabel", "testpostlabel"));
        edges1.add(new edge(0, 2, "testlabel", "testpostlabel"));


        for (int i = 0; i < 4; i++) {
            tokens1.add(new token(i, "node" + (i + 1) + " form", "node" + (i + 1) + " lemma", "node" + (i + 1) + " carg"));
            tokens2.add(new token(i + 1, "node" + (i + 2) + " form", "node" + (i + 2) + " lemma", "node" + (i + 2) + " carg"));
            tokens3.add(new token(i + 2, "node" + (i + 3) + " form", "node" + (i + 3) + " lemma", "node" + (i + 3) + " carg"));
            tokens3.add(new token(6, "node" + (7) + " form", "node" + (7) + " lemma", "node" + (7) + " carg"));

        }

        g1 = new graph("11111", "testsource", "node1 node2 node3 node4", nodes1, tokens1, edges1, new ArrayList<Integer>());
        g2 = new graph("22222", "testsource", "node2 node3 node4 node5", nodes2, tokens2, edges1, new ArrayList<Integer>());
        g3 = new graph("33333", "testsource", "node3 node4 node5 node6", nodes3, tokens3, edges1, new ArrayList<Integer>());
        g4 = new graph("44444", "testsource", "node7 node7 node7 node7", nodes4, tokens4, edges1, new ArrayList<Integer>());


    }

    @Test
    public void test_getGraph_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{

        construct_test_graph();

        HashMap<String, graph> graphs = new HashMap<>();
        graphs.put("11111", g1);

        RepGraphModel r = new RepGraphModel();

        //Set field without using setter
        final Field field = r.getClass().getDeclaredField("graphs");
        field.setAccessible(true);
        field.set(r, graphs);


        assertEquals("graph value was not retrieved properly.", r.getGraph("11111"), g1);
    }

    @Test
    public void test_addGraph_AddGraphCorrectly() throws NoSuchFieldException, IllegalAccessException{

        construct_test_graph();

        HashMap<String, graph> graphs = new HashMap<>();
        graphs.put("11111", g1);


        RepGraphModel r = new RepGraphModel();
        r.addGraph(g1);

        //Get fields without using getter
        final Field field = r.getClass().getDeclaredField("graphs");
        field.setAccessible(true);

        assertEquals("graph value was not added correctly.", field.get(r) , graphs);
    }

    @Test(timeout = 10)
    public void test_SearchSubgraphNodeSet_FindsGraphsCorrectly() throws NoSuchFieldException, IllegalAccessException {
        RepGraphModel model = new RepGraphModel();

        HashMap<String, graph> graphs = new HashMap<>();
        graphs.put("11111", g1);
        graphs.put("22222", g2);
        graphs.put("33333", g3);

        ArrayList<String> labels = new ArrayList<>();


        //Set field without using setter
        final Field field = model.getClass().getDeclaredField("graphs");
        field.setAccessible(true);
        field.set(model, graphs);

        ArrayList<String> correctResults = new ArrayList<>();
        correctResults.add("11111");
        labels.add("node1");
        ArrayList<String> results = model.searchSubgraphNodeSet(labels);

        Collections.sort(results);
        Collections.sort(correctResults);
        assertEquals("Graphs were searched for a set of node labels and were not found correctly.", correctResults, results);

        correctResults.clear();
        correctResults.add("11111");
        correctResults.add("22222");
        labels.clear();
        labels.add("node2");
        results = model.searchSubgraphNodeSet(labels);

        Collections.sort(results);
        Collections.sort(correctResults);
        assertEquals("Graphs were searched for a set of node labels and were not found correctly.", correctResults, results);

        correctResults.clear();
        correctResults.add("22222");
        labels.clear();
        labels.add("node2");
        labels.add("node3");
        labels.add("node4");
        labels.add("node5");
        results = model.searchSubgraphNodeSet(labels);

        Collections.sort(results);
        Collections.sort(correctResults);
        assertEquals("Graphs were searched for a set of node labels and were not found correctly.", correctResults, results);

        correctResults.clear();
        correctResults.add("11111");
        correctResults.add("22222");
        correctResults.add("33333");
        labels.clear();
        labels.add("node3");
        labels.add("node4");
        results = model.searchSubgraphNodeSet(labels);

        Collections.sort(results);
        Collections.sort(correctResults);
        assertEquals("Graphs were searched for a set of node labels and were not found correctly.", correctResults, results);

        correctResults.clear();

        correctResults.add("33333");
        labels.clear();
        labels.add("node6");
        results = model.searchSubgraphNodeSet(labels);

        Collections.sort(results);
        Collections.sort(correctResults);
        assertEquals("Graphs were searched for a set of node labels and were not found correctly.", correctResults, results);


    }

    @Test
    public void test_SearchSubgraphNodeSet_HandlesEmptyNodeSetArray() throws NoSuchFieldException, IllegalAccessException {

        RepGraphModel model = new RepGraphModel();
        ArrayList<String> labels = new ArrayList<>();
        HashMap<String, graph> graphs = new HashMap<>();
        graphs.put("11111", g1);
        graphs.put("22222", g2);
        graphs.put("33333", g3);

        //Set field without using setter
        final Field field = model.getClass().getDeclaredField("graphs");
        field.setAccessible(true);
        field.set(model, graphs);

        ArrayList<String> correctResults = new ArrayList<>();
        ArrayList<String> results = model.searchSubgraphNodeSet(labels);

        Collections.sort(results);
        Collections.sort(correctResults);
        assertEquals("Graphs were searched for an empty set of labels and the method did not function correctly.", correctResults, results);

    }

    @Test
    public void test_SearchSubgraphNodeSet_HandlesIncorrectNodeData() throws NoSuchFieldException, IllegalAccessException {

        RepGraphModel model = new RepGraphModel();
        ArrayList<String> labels = new ArrayList<>();
        labels.add("node7");
        HashMap<String, graph> graphs = new HashMap<>();
        graphs.put("11111", g1);
        graphs.put("22222", g2);
        graphs.put("33333", g3);

        //Set field without using setter
        final Field field = model.getClass().getDeclaredField("graphs");
        field.setAccessible(true);
        field.set(model, graphs);

        ArrayList<String> correctResults = new ArrayList<>();
        ArrayList<String> results = model.searchSubgraphNodeSet(labels);

        Collections.sort(results);
        Collections.sort(correctResults);
        assertEquals("Graphs were searched for an empty set of labels and the method did not function correctly.", correctResults, results);

    }

    @Test
    public void test_SearchSubgraphNodeSet_HandlesDuplicateLabels() throws NoSuchFieldException, IllegalAccessException {

        RepGraphModel model = new RepGraphModel();
        ArrayList<String> labels = new ArrayList<>();

        HashMap<String, graph> graphs = new HashMap<>();
        graphs.put("11111", g1);
        graphs.put("22222", g2);
        graphs.put("33333", g3);
        graphs.put("44444", g4);

        //Set field without using setter
        final Field field = model.getClass().getDeclaredField("graphs");
        field.setAccessible(true);
        field.set(model, graphs);

        ArrayList<String> correctResults = new ArrayList<>();
        labels.add("node1");
        labels.add("node1");
        ArrayList<String> results = model.searchSubgraphNodeSet(labels);

        Collections.sort(results);
        Collections.sort(correctResults);
        assertEquals("Graphs were searched for an empty set of labels and the method did not function correctly.", correctResults, results);

        correctResults.clear();

        correctResults.add("44444");
        labels.clear();
        labels.add("node7");
        labels.add("node7");
        labels.add("node7");
        labels.add("node7");
        results = model.searchSubgraphNodeSet(labels);

        Collections.sort(results);
        Collections.sort(correctResults);
        assertEquals("Graphs were searched for a set of node labels and were not found correctly.", correctResults, results);

        correctResults.clear();
        labels.clear();
        labels.add("node7");
        labels.add("node7");
        labels.add("node7");
        labels.add("node7");
        labels.add("node7");
        results = model.searchSubgraphNodeSet(labels);

        Collections.sort(results);
        Collections.sort(correctResults);
        assertEquals("Graphs were searched for a set of node labels and were not found correctly.", correctResults, results);


    }


    @Test
    public void test_SearchSubgraphPattern_FindsGraphsCorrectly() throws NoSuchFieldException, IllegalAccessException {

        RepGraphModel model = new RepGraphModel();

        HashMap<String, graph> graphs = new HashMap<>();
        graphs.put("11111", g1);
        graphs.put("22222", g2);
        graphs.put("33333", g3);

        //Set field without using setter
        final Field field = model.getClass().getDeclaredField("graphs");
        field.setAccessible(true);
        field.set(model, graphs);

        ArrayList<node> subnodes = new ArrayList<>();
        ArrayList<edge> subedges = new ArrayList<>();

        subnodes.add(new node(0, "node" + (2), new ArrayList<anchors>()));
        subnodes.add(new node(1, "node" + (4), new ArrayList<anchors>()));
        subedges.add(new edge(0, 1, "testlabel", "testpostlabel"));
        graph subgraph1 = new graph("44444", "testsource", "node2 node4", subnodes, new ArrayList<token>(), subedges, new ArrayList<Integer>());

        subnodes = new ArrayList<>();
        subnodes.add(new node(0, "node" + (1), new ArrayList<anchors>()));
        subnodes.add(new node(1, "node" + (2), new ArrayList<anchors>()));

        graph subgraph2 = new graph("55555", "testsource", "node1 node2", subnodes, new ArrayList<token>(), subedges, new ArrayList<Integer>());

        subnodes = new ArrayList<>();
        subnodes.add(new node(0, "node" + (3), new ArrayList<anchors>()));
        subnodes.add(new node(1, "node" + (4), new ArrayList<anchors>()));

        graph subgraph3 = new graph("66666", "testsource", "node3 node4", subnodes, new ArrayList<token>(), subedges, new ArrayList<Integer>());

        subnodes = new ArrayList<>();
        subnodes.add(new node(0, "node" + (3), new ArrayList<anchors>()));
        subnodes.add(new node(1, "node" + (5), new ArrayList<anchors>()));

        graph subgraph4 = new graph("77777", "testsource", "node3 node5", subnodes, new ArrayList<token>(), subedges, new ArrayList<Integer>());


        ArrayList<String> correctResults = new ArrayList<>();
        correctResults.add("11111");
        correctResults.add("22222");
        ArrayList<String> results = model.searchSubgraphPattern(subgraph1);

        Collections.sort(results);
        Collections.sort(correctResults);
        assertEquals("Graphs were searched for a subgraph pattern and were not found correctly.", correctResults, results);

        correctResults.clear();
        correctResults.add("11111");
        results = model.searchSubgraphPattern(subgraph2);

        Collections.sort(results);
        Collections.sort(correctResults);
        assertEquals("Graphs were searched for a subgraph pattern and were not found correctly.", correctResults, results);

        correctResults.clear();
        correctResults.add("33333");
        results = model.searchSubgraphPattern(subgraph3);

        Collections.sort(results);
        Collections.sort(correctResults);
        assertEquals("Graphs were searched for a subgraph pattern and were not found correctly.", correctResults, results);

        correctResults.clear();
        correctResults.add("22222");
        correctResults.add("33333");
        results = model.searchSubgraphPattern(subgraph4);

        Collections.sort(results);
        Collections.sort(correctResults);
        assertEquals("Graphs were searched for a subgraph pattern and were not found correctly.", correctResults, results);

    }

    @Test
    public void test_SearchSubgraphPattern_HandlesEmptySubgraph() throws NoSuchFieldException, IllegalAccessException {
        RepGraphModel model = new RepGraphModel();

        HashMap<String, graph> graphs = new HashMap<>();
        graphs.put("11111", g1);
        graphs.put("22222", g2);
        graphs.put("33333", g3);

        //Set field without using setter
        final Field field = model.getClass().getDeclaredField("graphs");
        field.setAccessible(true);
        field.set(model, graphs);

        ArrayList<node> subnodes = new ArrayList<>();
        ArrayList<edge> subedges = new ArrayList<>();

        graph subgraph = new graph("44444", "testsource", "", subnodes, new ArrayList<token>(), subedges, new ArrayList<Integer>());

        ArrayList<String> correctResults = new ArrayList<>();
        ArrayList<String> results = model.searchSubgraphPattern(subgraph);

        Collections.sort(results);
        Collections.sort(correctResults);
        assertEquals("Graphs were searched using an empty subgraph pattern and the method did not handle it correctly.", correctResults, results);

    }

    @Test
    public void test_SearchSubgraphPattern_HandlesNonConnectedSubgraph() throws NoSuchFieldException, IllegalAccessException {
        RepGraphModel model = new RepGraphModel();

        HashMap<String, graph> graphs = new HashMap<>();
        graphs.put("11111", g1);
        graphs.put("22222", g2);
        graphs.put("33333", g3);

        //Set field without using setter
        final Field field = model.getClass().getDeclaredField("graphs");
        field.setAccessible(true);
        field.set(model, graphs);

        ArrayList<node> subnodes = new ArrayList<>();
        ArrayList<edge> subedges = new ArrayList<>();

        subnodes.add(new node(0, "node" + (3), new ArrayList<anchors>()));
        subnodes.add(new node(1, "node" + (5), new ArrayList<anchors>()));

        graph subgraph = new graph("44444", "testsource", "node3 node5", subnodes, new ArrayList<token>(), subedges, new ArrayList<Integer>());

        ArrayList<String> correctResults = new ArrayList<>();
        ArrayList<String> results = model.searchSubgraphPattern(subgraph);

        Collections.sort(results);
        Collections.sort(correctResults);
        assertEquals("Graphs were searched for a non connected subgraph and it was not handled correctly.", correctResults, results);


    }

    @Test
    public void test_SearchSubgraphPattern_HandlesIncorrectEdgeData() throws NoSuchFieldException, IllegalAccessException {
        RepGraphModel model = new RepGraphModel();

        HashMap<String, graph> graphs = new HashMap<>();
        graphs.put("11111", g1);
        graphs.put("22222", g2);
        graphs.put("33333", g3);

        //Set field without using setter
        final Field field = model.getClass().getDeclaredField("graphs");
        field.setAccessible(true);
        field.set(model, graphs);

        ArrayList<node> subnodes = new ArrayList<>();
        ArrayList<edge> subedges = new ArrayList<>();

        subnodes.add(new node(0, "node" + (3), new ArrayList<anchors>()));
        subnodes.add(new node(1, "node" + (5), new ArrayList<anchors>()));
        subedges.add(new edge(2, 3, "testlabel", "testpostlabel"));

        graph subgraph = new graph("44444", "testsource", "node3 node5", subnodes, new ArrayList<token>(), subedges, new ArrayList<Integer>());

        ArrayList<String> correctResults = new ArrayList<>();
        ArrayList<String> results = model.searchSubgraphPattern(subgraph);

        Collections.sort(results);
        Collections.sort(correctResults);
        assertEquals("Graphs were searched for a non connected subgraph and it was not handled correctly.", correctResults, results);

    }

    @Test
    public void test_compareTwoGraphs_FindsSimilarNodesAndEdgesCorrectly(){
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
        edge edge1 = new edge(0, 2, "testlabel1", "testpostlabel1");
        edge edge2 = new edge(2, 3, "testlabel2", "testpostlabel2");
        edges.add(edge0);
        edges.add(edge1);
        edges.add(edge2);

        graph g1 = new graph("1", "testsource1", "testInput1", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        ArrayList<node> nodes2 = new ArrayList<>();
        ArrayList<edge> edges2 = new ArrayList<>();

        node node3 = new node(0, "node0", new ArrayList<>());
        node node4 = new node(1, "node3", new ArrayList<>());
        node node5 = new node(2, "node2", new ArrayList<>());
        nodes2.add(node3);
        nodes2.add(node4);
        nodes2.add(node5);

        edge edge3 = new edge(2, 3, "testlabel3", "testpostlabel3");

        edges2.add(edge3);
        edges2.add(edge1);
        edges2.add(edge0);

        graph g2 = new graph("2", "testsource2", "testInput2", nodes2, new ArrayList<token>(), edges2, new ArrayList<Integer>());
        RepGraphModel model = new RepGraphModel();
        model.addGraph(g1);
        model.addGraph(g2);


        ArrayList<node> similarNodes = new ArrayList<>();
        ArrayList<edge> similarEdges = new ArrayList<>();
        similarNodes.add(node0);
        similarNodes.add(node2);
        similarEdges.add(edge0);
        similarEdges.add(edge1);

        HashMap<String,Object> expected = new HashMap<>();
        expected.put("Nodes", similarNodes);
        expected.put("Edges", similarEdges);

        assertEquals("compareTwoGraphs does not correctly identify similar nodes and edges.",expected,model.compareTwoGraphs("1","2"));
    }

}
