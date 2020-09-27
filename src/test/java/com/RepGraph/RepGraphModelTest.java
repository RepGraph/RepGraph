package com.RepGraph;

import org.junit.Before;
import org.junit.Test;

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

}
