package com.RepGraph;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;


public class RepGraphModelTest {

    private graph g1, g2, g3, g4;

    @Before
    public void construct_test_graph(){
        HashMap<Integer, node> nodes1 = new HashMap<Integer, node>();
        HashMap<Integer, node> nodes2 = new HashMap<Integer, node>();
        HashMap<Integer, node> nodes3 = new HashMap<Integer, node>();
        HashMap<Integer, node> nodes4 = new HashMap<Integer, node>();
        ArrayList<edge> edges1 = new ArrayList<>();
        ArrayList<token> tokens1 = new ArrayList<>();
        ArrayList<token> tokens2 = new ArrayList<>();
        ArrayList<token> tokens3 = new ArrayList<>();
        ArrayList<token> tokens4 = new ArrayList<>();


        for (int i = 0; i < 4; i++) {
            ArrayList<anchors> anch1 = new ArrayList<anchors>();
            anch1.add(new anchors(i, i));
            nodes1.put(i, new node(i, "node" + (i + 1), anch1));
            nodes2.put(i, new node(i, "node" + (i + 2), anch1));
            nodes3.put(i, new node(i, "node" + (i + 3), anch1));
            nodes4.put(i, new node(i, "node" + (7), anch1));
        }

        edges1.add(new edge(0, 1, "testlabel", "testpostlabel"));
        edges1.add(new edge(1, 3, "testlabel", "testpostlabel"));
        edges1.add(new edge(0, 2, "testlabel", "testpostlabel"));


        for (int i = 0; i < 4; i++) {
            tokens1.add(new token(i, "node" + (i + 1) + " form", "node" + (i + 1) + " lemma", "node" + (i + 1) + " carg"));
            tokens2.add(new token(i, "node" + (i + 2) + " form", "node" + (i + 2) + " lemma", "node" + (i + 2) + " carg"));
            tokens3.add(new token(i, "node" + (i + 3) + " form", "node" + (i + 3) + " lemma", "node" + (i + 3) + " carg"));
            tokens4.add(new token(6, "node" + (7) + " form", "node" + (7) + " lemma", "node" + (7) + " carg"));

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


    @Test
    public void test_CreateSubsetAdjacent_CorrectlyConstructsSubsetGraph() throws NoSuchFieldException, IllegalAccessException {

        RepGraphModel model = new RepGraphModel();

        HashMap<String, graph> graphs = new HashMap<>();
        graphs.put("11111", g1);
        graphs.put("22222", g2);
        graphs.put("33333", g3);


        //Set field without using setter
        final Field field = model.getClass().getDeclaredField("graphs");
        field.setAccessible(true);
        field.set(model, graphs);

        HashMap<Integer, node> correctNodes = new HashMap<Integer, node>();
        ArrayList<edge> correctEdges = new ArrayList<>();
        ArrayList<token> correctTokens = new ArrayList<>();


        ArrayList<anchors> anch1 = new ArrayList<anchors>();
        anch1.add(new anchors(0, 0));

        ArrayList<anchors> anch2 = new ArrayList<anchors>();
        anch2.add(new anchors(1, 1));

        ArrayList<anchors> anch3 = new ArrayList<anchors>();
        anch3.add(new anchors(2, 2));


        correctNodes.put(0, new node(0, "node" + (0 + 1), anch1));
        correctNodes.put(1, new node(1, "node" + (1 + 1), anch2));
        correctNodes.put(2, new node(2, "node" + (2 + 1), anch3));

        correctEdges.add(new edge(0, 1, "testlabel", "testpostlabel"));
        correctEdges.add(new edge(0, 2, "testlabel", "testpostlabel"));

        for (int i = 0; i < 3; i++) {
            correctTokens.add(new token(i, "node" + (i + 1) + " form", "node" + (i + 1) + " lemma", "node" + (i + 1) + " carg"));

        }

        graph expected = new graph("11111", g1.getSource(), "node1 form node2 form node3 form", correctNodes, correctTokens, correctEdges, new ArrayList<Integer>());

        graph subset = model.CreateSubsetAdjacent("11111", 0);

        assertEquals("Subset Not Constructed Properly", subset, expected);

    }

    @Test
    public void test_CreateSubsetDescendent_CorrectlyConstructsSubsetGraph() throws NoSuchFieldException, IllegalAccessException {

        RepGraphModel model = new RepGraphModel();

        HashMap<String, graph> graphs = new HashMap<>();
        graphs.put("11111", g1);
        graphs.put("22222", g2);
        graphs.put("33333", g3);


        //Set field without using setter
        final Field field = model.getClass().getDeclaredField("graphs");
        field.setAccessible(true);
        field.set(model, graphs);

        HashMap<Integer, node> correctNodes = new HashMap<Integer, node>();
        ArrayList<edge> correctEdges = new ArrayList<>();
        ArrayList<token> correctTokens = new ArrayList<>();


        ArrayList<anchors> anch1 = new ArrayList<anchors>();
        anch1.add(new anchors(0, 0));

        ArrayList<anchors> anch2 = new ArrayList<anchors>();
        anch2.add(new anchors(1, 1));

        ArrayList<anchors> anch3 = new ArrayList<anchors>();
        anch3.add(new anchors(2, 2));

        ArrayList<anchors> anch4 = new ArrayList<anchors>();
        anch4.add(new anchors(3, 3));


        correctNodes.put(0, new node(0, "node" + (0 + 1), anch1));
        correctNodes.put(1, new node(1, "node" + (1 + 1), anch2));
        correctNodes.put(2, new node(2, "node" + (2 + 1), anch3));
        correctNodes.put(3, new node(3, "node" + (3 + 1), anch4));


        correctEdges.add(new edge(0, 1, "testlabel", "testpostlabel"));
        correctEdges.add(new edge(1, 3, "testlabel", "testpostlabel"));
        correctEdges.add(new edge(0, 2, "testlabel", "testpostlabel"));


        for (int i = 0; i < 4; i++) {
            correctTokens.add(new token(i, "node" + (i + 1) + " form", "node" + (i + 1) + " lemma", "node" + (i + 1) + " carg"));

        }

        graph expected = new graph("11111", g1.getSource(), "node1 form node2 form node3 form node4 form", correctNodes, correctTokens, correctEdges, new ArrayList<Integer>());

        graph subset = model.CreateSubsetDescendent("11111", 0);

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

        Comparator<node> compNodes = new Comparator<node>() {
            @Override
            public int compare(node o1, node o2) {
                if (o1.getId() < o2.getId()) {
                    return -1;
                } else if (o1.getId() > o2.getId()) {
                    return 1;
                }
                return 0;
            }
        };

        Collections.sort(correctEdges, comp);
        Collections.sort(subset.getEdges(), comp);

        ArrayList<node> correctNodesList = new ArrayList<>(correctNodes.values());
        ArrayList<node> SubsetNodesList = new ArrayList<>(subset.getNodes().values());
        Collections.sort(correctNodesList, compNodes);
        Collections.sort(SubsetNodesList, compNodes);

        assertEquals("Subset Not Constructed Properly", subset, expected);

        subset = model.CreateSubsetDescendent("11111", 1);

        correctNodes.clear();
        correctNodes.put(1, new node(1, "node" + (1 + 1), anch2));
        correctNodes.put(3, new node(3, "node" + (3 + 1), anch4));

        correctEdges.clear();
        correctEdges.add(new edge(1, 3, "testlabel", "testpostlabel"));

        correctTokens.clear();
        correctTokens.add(new token(1, "node" + (1 + 1) + " form", "node" + (1 + 1) + " lemma", "node" + (1 + 1) + " carg"));
        correctTokens.add(new token(2, "node" + (2 + 1) + " form", "node" + (2 + 1) + " lemma", "node" + (2 + 1) + " carg"));
        correctTokens.add(new token(3, "node" + (3 + 1) + " form", "node" + (3 + 1) + " lemma", "node" + (3 + 1) + " carg"));

        expected = new graph("11111", g1.getSource(), "node2 form node3 form node4 form", correctNodes, correctTokens, correctEdges, new ArrayList<Integer>());

        Collections.sort(correctEdges, comp);
        Collections.sort(subset.getEdges(), comp);

        correctNodesList = new ArrayList<>(correctNodes.values());
        SubsetNodesList = new ArrayList<>(subset.getNodes().values());
        Collections.sort(correctNodesList, compNodes);
        Collections.sort(SubsetNodesList, compNodes);

        assertEquals("Subset Not Constructed Properly", subset, expected);


    }

    @Test
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

        HashMap<String, Object> correctResults = new HashMap<String, Object>();
        ArrayList<HashMap<String, String>> found = new ArrayList<>();
        HashMap<String, String> graph1 = new HashMap<String, String>();
        graph1.put("id", "11111");
        graph1.put("input", "node1 node2 node3 node4");
        found.add(graph1);
        correctResults.put("data", found);
        labels.add("node1");
        HashMap<String, Object> results = model.searchSubgraphNodeSet(labels);

        //Collections.sort(results);
        //Collections.sort(correctResults);
        correctResults.put("response", "Success");
        assertEquals("Graphs were searched for a set of node labels and were not found correctly.", correctResults, results);


        correctResults = new HashMap<String, Object>();
        found = new ArrayList<>();
        HashMap<String, String> graph2 = new HashMap<String, String>();
        graph2.put("id", "22222");
        graph2.put("input", "node2 node3 node4 node5");
        found.add(graph1);
        found.add(graph2);
        correctResults.put("data", found);
        labels.clear();
        labels.add("node2");
        results = model.searchSubgraphNodeSet(labels);


        correctResults.put("response", "Success");
        assertEquals("Graphs were searched for a set of node labels and were not found correctly.", correctResults, results);


        correctResults = new HashMap<String, Object>();
        found = new ArrayList<>();


        graph2 = new HashMap<String, String>();
        graph2.put("id", "22222");
        graph2.put("input", "node2 node3 node4 node5");
        found.add(graph2);
        correctResults.put("data", found);
        labels.clear();
        labels.add("node2");
        labels.add("node3");
        labels.add("node4");
        labels.add("node5");
        results = model.searchSubgraphNodeSet(labels);

        correctResults.put("response", "Success");
        assertEquals("Graphs were searched for a set of node labels and were not found correctly.", correctResults, results);

        correctResults = new HashMap<String, Object>();
        found = new ArrayList<>();
        graph2 = new HashMap<String, String>();
        graph2.put("id", "22222");
        graph2.put("input", "node2 node3 node4 node5");
        HashMap<String, String> graph3 = new HashMap<String, String>();
        graph3.put("id", "33333");
        graph3.put("input", "node3 node4 node5 node6");
        found.add(graph1);
        found.add(graph2);
        found.add(graph3);
        correctResults.put("data", found);

        labels.clear();
        labels.add("node3");
        labels.add("node4");
        results = model.searchSubgraphNodeSet(labels);

        correctResults.put("response", "Success");
        assertEquals("Graphs were searched for a set of node labels and were not found correctly.", correctResults.size(), results.size());

        ArrayList<HashMap<String, String>> obj = (ArrayList<HashMap<String, String>>) correctResults.get("data");
        ArrayList<HashMap<String, String>> Resultobj = (ArrayList<HashMap<String, String>>) results.get("data");
        for (int i = 0; i < obj.size(); i++) {
            HashMap<String, String> obj1 = obj.get(i);
            assertTrue("Graphs were searched for a subgraph pattern and were not found correctly.", Resultobj.contains(obj1));

        }
        correctResults = new HashMap<String, Object>();
        found = new ArrayList<>();

        found.add(graph3);
        correctResults.put("data", found);
        labels.clear();
        labels.add("node6");
        results = model.searchSubgraphNodeSet(labels);

        correctResults.put("response", "Success");
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

        HashMap<String, Object> correctResults = new HashMap<String, Object>();

        correctResults.put("response", "Subgraph Pattern was not entered correctly");
        HashMap<String, Object> results = model.searchSubgraphNodeSet(labels);


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

        HashMap<String, Object> correctResults = new HashMap<String, Object>();
        ArrayList<HashMap<String, String>> found = new ArrayList<>();
        correctResults.put("data", found);
        HashMap<String, Object> results = model.searchSubgraphNodeSet(labels);

        correctResults.put("response", "Success");
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

        HashMap<String, Object> correctResults = new HashMap<String, Object>();
        ArrayList<HashMap<String, String>> found = new ArrayList<>();
        correctResults.put("data", found);
        labels.add("node1");
        labels.add("node1");
        HashMap<String, Object> results = model.searchSubgraphNodeSet(labels);

        correctResults.put("response", "Success");
        assertEquals("Graphs were searched for an empty set of labels and the method did not function correctly.", correctResults, results);

        correctResults = new HashMap<String, Object>();
        found = new ArrayList<>();
        HashMap<String, String> graph4 = new HashMap<String, String>();
        graph4.put("id", "44444");
        graph4.put("input", "node7 node7 node7 node7");

        found.add(graph4);
        correctResults.put("data", found);


        labels.clear();
        labels.add("node7");
        labels.add("node7");
        labels.add("node7");
        labels.add("node7");
        results = model.searchSubgraphNodeSet(labels);

        correctResults.put("response", "Success");
        assertEquals("Graphs were searched for a set of node labels and were not found correctly.", correctResults, results);


        correctResults = new HashMap<String, Object>();
        found.clear();
        correctResults.put("data", found);
        labels.clear();
        labels.add("node7");
        labels.add("node7");
        labels.add("node7");
        labels.add("node7");
        labels.add("node7");
        results = model.searchSubgraphNodeSet(labels);

        correctResults.put("response", "Success");
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

        HashMap<Integer, node> subnodes = new HashMap<Integer, node>();
        ArrayList<edge> subedges = new ArrayList<>();

        subedges.add(new edge(0, 1, "testlabel", "testpostlabel"));

        subnodes.put(0, new node(0, "node" + (2), new ArrayList<anchors>()));
        subnodes.put(1, new node(1, "node" + (4), new ArrayList<anchors>()));
        graph subgraph1 = new graph("44444", "testsource", "node2 node4", subnodes, new ArrayList<token>(), subedges, new ArrayList<Integer>());

        subnodes = new HashMap<Integer, node>();
        subnodes.put(0, new node(0, "node" + (1), new ArrayList<anchors>()));
        subnodes.put(1, new node(1, "node" + (2), new ArrayList<anchors>()));

        graph subgraph2 = new graph("55555", "testsource", "node1 node2", subnodes, new ArrayList<token>(), subedges, new ArrayList<Integer>());

        subnodes = new HashMap<Integer, node>();
        subnodes.put(0, new node(0, "node" + (3), new ArrayList<anchors>()));
        subnodes.put(1, new node(1, "node" + (4), new ArrayList<anchors>()));

        graph subgraph3 = new graph("66666", "testsource", "node3 node4", subnodes, new ArrayList<token>(), subedges, new ArrayList<Integer>());

        subnodes = new HashMap<Integer, node>();
        subnodes.put(0, new node(0, "node" + (3), new ArrayList<anchors>()));
        subnodes.put(1, new node(1, "node" + (5), new ArrayList<anchors>()));

        graph subgraph4 = new graph("77777", "testsource", "node3 node5", subnodes, new ArrayList<token>(), subedges, new ArrayList<Integer>());

        subedges = new ArrayList<>();
        subedges.add(new edge(0, 1, "testlabel", "testpostlabel"));
        subedges.add(new edge(1, 3, "testlabel", "testpostlabel"));
        subedges.add(new edge(0, 2, "testlabel", "testpostlabel"));

        subnodes = new HashMap<Integer, node>();
        subnodes.put(0, new node(0, "node" + (1), new ArrayList<anchors>()));
        subnodes.put(1, new node(1, "node" + (2), new ArrayList<anchors>()));
        subnodes.put(2, new node(2, "node" + (3), new ArrayList<anchors>()));
        subnodes.put(3, new node(3, "node" + (4), new ArrayList<anchors>()));
        graph subgraph5 = new graph("88888", "testsource", "node1 node2 node3 node4", subnodes, new ArrayList<token>(), subedges, new ArrayList<Integer>());

        subnodes = new HashMap<Integer, node>();
        subnodes.put(0, new node(0, "node" + (1), new ArrayList<anchors>()));
        subnodes.put(1, new node(1, "node" + (2), new ArrayList<anchors>()));
        subnodes.put(2, new node(2, "node" + (3), new ArrayList<anchors>()));
        subnodes.put(3, new node(3, "node" + (4), new ArrayList<anchors>()));

        subedges = new ArrayList<>();
        subedges.add(new edge(0, 1, "testlabelWillFail", "testpostlabel"));
        subedges.add(new edge(1, 3, "testlabel", "testpostlabel"));
        subedges.add(new edge(0, 2, "testlabel", "testpostlabel"));
        graph subgraph6 = new graph("88888", "testsource", "node1 node2 node3 node4", subnodes, new ArrayList<token>(), subedges, new ArrayList<Integer>());


        HashMap<String, Object> correctResults = new HashMap<String, Object>();
        ArrayList<HashMap<String, String>> found = new ArrayList<>();
        HashMap<String, String> graph1 = new HashMap<String, String>();
        HashMap<String, String> graph2 = new HashMap<String, String>();
        HashMap<String, String> graph3 = new HashMap<String, String>();
        graph1.put("id", "11111");
        graph1.put("input", "node1 node2 node3 node4");
        found.add(graph1);
        graph2.put("id", "22222");
        graph2.put("input", "node2 node3 node4 node5");
        found.add(graph2);
        graph3.put("id", "33333");
        graph3.put("input", "node3 node4 node5 node6");
        correctResults.put("data", found);


        HashMap<String, Object> results = model.searchSubgraphPattern(subgraph1);

        correctResults.put("response", "Success");
        assertEquals("Graphs were searched for a subgraph pattern and were not found correctly.", correctResults, results);

        correctResults.clear();
        found.clear();
        found.add(graph1);
        correctResults.put("data", found);
        results = model.searchSubgraphPattern(subgraph2);

        correctResults.put("response", "Success");
        assertEquals("Graphs were searched for a subgraph pattern and were not found correctly.", correctResults, results);

        correctResults.clear();
        found.clear();
        found.add(graph3);
        correctResults.put("data", found);
        results = model.searchSubgraphPattern(subgraph3);

        correctResults.put("response", "Success");
        assertEquals("Graphs were searched for a subgraph pattern and were not found correctly.", correctResults, results);

        correctResults.clear();
        found.clear();
        found.add(graph2);
        found.add(graph3);
        correctResults.put("data", found);
        results = model.searchSubgraphPattern(subgraph4);

        correctResults.put("response", "Success");
        assertEquals("Graphs were searched for a subgraph pattern and were not found correctly.", correctResults.size(), results.size());

        ArrayList<HashMap<String, String>> obj = (ArrayList<HashMap<String, String>>) correctResults.get("data");
        ArrayList<HashMap<String, String>> Resultobj = (ArrayList<HashMap<String, String>>) results.get("data");
        for (int i = 0; i < obj.size(); i++) {
            HashMap<String, String> obj1 = obj.get(i);
            assertTrue("Graphs were searched for a subgraph pattern and were not found correctly.", Resultobj.contains(obj1));

        }


        correctResults.clear();
        found.clear();
        found.add(graph1);
        correctResults.put("data", found);
        results = model.searchSubgraphPattern(subgraph5);

        correctResults.put("response", "Success");
        assertEquals("Graphs were searched for a subgraph pattern and were not found correctly.", correctResults, results);

        correctResults.clear();
        found.clear();
        correctResults.put("data", found);
        results = model.searchSubgraphPattern(subgraph6);

        correctResults.put("response", "Success");
        assertEquals("Graphs were searched for a subgraph pattern and were not found correctly.", correctResults, results);


    }

    @Test
    public void test_SearchSubgraphPattern_HandlesDisconnectedSubGraphPattern() throws NoSuchFieldException, IllegalAccessException {
        RepGraphModel model = new RepGraphModel();

        HashMap<String, graph> graphs = new HashMap<>();
        graphs.put("11111", g1);
        graphs.put("22222", g2);
        graphs.put("33333", g3);

        //Set field without using setter
        final Field field = model.getClass().getDeclaredField("graphs");
        field.setAccessible(true);
        field.set(model, graphs);

        HashMap<Integer, node> subnodes = new HashMap<Integer, node>();
        subnodes.put(0, new node(0, "node" + (1), new ArrayList<anchors>()));
        subnodes.put(1, new node(1, "node" + (2), new ArrayList<anchors>()));
        subnodes.put(2, new node(2, "node" + (3), new ArrayList<anchors>()));

        ArrayList<edge> subedges = new ArrayList<>();
        subedges.add(new edge(0, 1, "testlabel", "testpostlabel"));
        subedges.add(new edge(1, 0, "testlabel", "testpostlabel"));

        graph subgraph = new graph("44444", "testsource", "", subnodes, new ArrayList<token>(), subedges, new ArrayList<Integer>());

        HashMap<String, Object> correctResults = new HashMap<String, Object>();

        correctResults.put("response", "Subgraph Pattern was not entered correctly");
        HashMap<String, Object> results = model.searchSubgraphPattern(subgraph);


        assertEquals("Graphs were searched using an empty subgraph pattern and the method did not handle it correctly.", correctResults, results);

    }

    @Test
    public void test_SearchSubgraphPattern_HandlesDanglingEdgeInSubGraphPattern() throws NoSuchFieldException, IllegalAccessException {
        RepGraphModel model = new RepGraphModel();

        HashMap<String, graph> graphs = new HashMap<>();
        graphs.put("11111", g1);
        graphs.put("22222", g2);
        graphs.put("33333", g3);

        //Set field without using setter
        final Field field = model.getClass().getDeclaredField("graphs");
        field.setAccessible(true);
        field.set(model, graphs);

        HashMap<Integer, node> subnodes = new HashMap<Integer, node>();
        subnodes.put(0, new node(0, "node" + (1), new ArrayList<anchors>()));
        subnodes.put(1, new node(1, "node" + (2), new ArrayList<anchors>()));
        subnodes.put(2, new node(2, "node" + (3), new ArrayList<anchors>()));

        ArrayList<edge> subedges = new ArrayList<>();
        subedges.add(new edge(0, 1, "testlabel", "testpostlabel"));
        subedges.add(new edge(1, 2, "testlabel", "testpostlabel"));
        subedges.add(new edge(2, 3, "testlabel", "testpostlabel"));


        graph subgraph = new graph("44444", "testsource", "", subnodes, new ArrayList<token>(), subedges, new ArrayList<Integer>());

        HashMap<String, Object> correctResults = new HashMap<String, Object>();

        correctResults.put("response", "Subgraph Pattern was not entered correctly");
        HashMap<String, Object> results = model.searchSubgraphPattern(subgraph);


        assertEquals("Graphs were searched using an empty subgraph pattern and the method did not handle it correctly.", correctResults, results);

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

        HashMap<Integer, node> subnodes = new HashMap<Integer, node>();
        ArrayList<edge> subedges = new ArrayList<>();

        graph subgraph = new graph("44444", "testsource", "", subnodes, new ArrayList<token>(), subedges, new ArrayList<Integer>());

        HashMap<String, Object> correctResults = new HashMap<String, Object>();
        correctResults.put("response", "Subgraph Pattern was not entered correctly");
        HashMap<String, Object> results = model.searchSubgraphPattern(subgraph);


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

        HashMap<Integer, node> subnodes = new HashMap<Integer, node>();
        ArrayList<edge> subedges = new ArrayList<>();

        subnodes.put(0, new node(0, "node" + (3), new ArrayList<anchors>()));
        subnodes.put(1, new node(1, "node" + (5), new ArrayList<anchors>()));

        graph subgraph = new graph("44444", "testsource", "node3 node5", subnodes, new ArrayList<token>(), subedges, new ArrayList<Integer>());

        HashMap<String, Object> correctResults = new HashMap<String, Object>();
        correctResults.put("response", "Subgraph Pattern was not entered correctly");
        HashMap<String, Object> results = model.searchSubgraphPattern(subgraph);


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

        HashMap<Integer, node> subnodes = new HashMap<Integer, node>();
        ArrayList<edge> subedges = new ArrayList<>();

        subnodes.put(0, new node(0, "node" + (3), new ArrayList<anchors>()));
        subnodes.put(1, new node(1, "node" + (5), new ArrayList<anchors>()));
        subedges.add(new edge(2, 3, "testlabel", "testpostlabel"));

        graph subgraph = new graph("44444", "testsource", "node3 node5", subnodes, new ArrayList<token>(), subedges, new ArrayList<Integer>());

        HashMap<String, Object> correctResults = new HashMap<String, Object>();
        correctResults.put("response", "Subgraph Pattern was not entered correctly");
        HashMap<String, Object> results = model.searchSubgraphPattern(subgraph);


        assertEquals("Graphs were searched for a non connected subgraph and it was not handled correctly.", correctResults, results);

    }

    @Test
    public void test_compareTwoGraphs_FindsSimilarNodesAndEdgesCorrectly(){
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();
        anchors anch1 = new anchors(0, 0);
        ArrayList<anchors> anch1arr = new ArrayList<>();
        anch1arr.add(anch1);
        node node0 = new node(0, "node0", anch1arr);
        node node1 = new node(1, "node1", anch1arr);
        node node2 = new node(2, "node2", anch1arr);
        nodes.put(0, node0);
        nodes.put(1, node1);
        nodes.put(2, node2);

        edge edge0 = new edge(0, 1, "testlabel", "testpostlabel");
        edge edge1 = new edge(0, 2, "testlabel", "testpostlabel");

        edges.add(edge0);
        edges.add(edge1);


        graph g1 = new graph("1", "testsource1", "testInput1", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        HashMap<Integer, node> nodes2 = new HashMap<Integer, node>();
        ArrayList<edge> edges2 = new ArrayList<>();

        node node3 = new node(0, "node0", anch1arr);
        node node4 = new node(1, "node3", anch1arr);
        node node5 = new node(2, "node2", anch1arr);
        nodes2.put(0, node3);
        nodes2.put(1, node4);
        nodes2.put(2, node5);

        edge edge3 = new edge(0, 2, "testlabel", "testpostlabel");

        edges2.add(edge3);
        edges2.add(edge1);
        edges2.add(edge0);

        graph g2 = new graph("2", "testsource2", "testInput2", nodes2, new ArrayList<token>(), edges2, new ArrayList<Integer>());
        RepGraphModel model = new RepGraphModel();
        model.addGraph(g1);
        model.addGraph(g2);


        ArrayList<Integer> similarNodes1 = new ArrayList<>();
        ArrayList<Integer> similarNodes2 = new ArrayList<>();
        ArrayList<Integer> similarEdges1 = new ArrayList<>();
        ArrayList<Integer> similarEdges2 = new ArrayList<>();
        similarNodes1.add(node0.getId());
        similarNodes1.add(node2.getId());
        similarNodes2.add(node5.getId());
        similarNodes2.add(node3.getId());
        similarEdges1.add(1);
        similarEdges2.add(0);

        Collections.sort(similarNodes1);
        Collections.sort(similarNodes2);
        Collections.sort(similarEdges1);
        Collections.sort(similarEdges2);
        HashMap<String,Object> expected = new HashMap<>();
        expected.put("SimilarNodes1", similarNodes1);
        expected.put("SimilarNodes2", similarNodes2);
        expected.put("SimilarEdges1", similarEdges1);
        expected.put("SimilarEdges2", similarEdges2);

        assertEquals("compareTwoGraphs does not correctly identify similar nodes and edges.",expected,model.compareTwoGraphs("1","2"));
    }

    @Test
    public void test_compareTwoGraphs_EmptyGraphs(){


        RepGraphModel model = new RepGraphModel();
        graph g1 = new graph("1", "", "", new ArrayList<node>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        graph g2 = new graph("2", "", "", new ArrayList<node>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        model.addGraph(g1);
        model.addGraph(g2);


        ArrayList<Integer> similarNodes1 = new ArrayList<>();
        ArrayList<Integer> similarNodes2 = new ArrayList<>();
        ArrayList<Integer> similarEdges1 = new ArrayList<>();
        ArrayList<Integer> similarEdges2 = new ArrayList<>();


        Collections.sort(similarNodes1);
        Collections.sort(similarNodes2);
        Collections.sort(similarEdges1);
        Collections.sort(similarEdges2);
        HashMap<String,Object> expected = new HashMap<>();
        expected.put("SimilarNodes1", similarNodes1);
        expected.put("SimilarNodes2", similarNodes2);
        expected.put("SimilarEdges1", similarEdges1);
        expected.put("SimilarEdges2", similarEdges2);

        assertEquals("compareTwoGraphs does not correctly identify similar nodes and edges.", expected, model.compareTwoGraphs("1", "2"));
    }

    @Test
    public void test_compareTwoGraphs_NoSimilarEdgesOrNodes() {
        //Creating the nodes and edges for the graph
        HashMap<Integer, node> nodes = new HashMap<Integer, node>();
        ArrayList<edge> edges = new ArrayList<>();
        anchors anch1 = new anchors(0, 0);
        ArrayList<anchors> anch1arr = new ArrayList<>();
        anch1arr.add(anch1);
        node node0 = new node(0, "node9", anch1arr);
        node node1 = new node(1, "node5", anch1arr);
        node node2 = new node(2, "node4", anch1arr);
        nodes.put(0, node0);
        nodes.put(1, node1);
        nodes.put(2, node2);

        edge edge0 = new edge(0, 1, "testlabel", "testpostlabel");
        edge edge1 = new edge(0, 2, "testlabel", "testpostlabel");

        edges.add(edge0);
        edges.add(edge1);


        graph g1 = new graph("1", "testsource1", "testInput1", nodes, new ArrayList<token>(), edges, new ArrayList<Integer>());

        HashMap<Integer, node> nodes2 = new HashMap<Integer, node>();
        ArrayList<edge> edges2 = new ArrayList<>();

        node node3 = new node(0, "node0", anch1arr);
        node node4 = new node(1, "node3", anch1arr);
        node node5 = new node(2, "node2", anch1arr);
        nodes2.put(0, node3);
        nodes2.put(1, node4);
        nodes2.put(2, node5);

        edge edge3 = new edge(0, 2, "testlabel11", "testpostlabel22");

        edges2.add(edge3);


        graph g2 = new graph("2", "testsource2", "testInput2", nodes2, new ArrayList<token>(), edges2, new ArrayList<Integer>());
        RepGraphModel model = new RepGraphModel();
        model.addGraph(g1);
        model.addGraph(g2);


        ArrayList<Integer> similarNodes1 = new ArrayList<>();
        ArrayList<Integer> similarNodes2 = new ArrayList<>();
        ArrayList<Integer> similarEdges1 = new ArrayList<>();
        ArrayList<Integer> similarEdges2 = new ArrayList<>();


        Collections.sort(similarNodes1);
        Collections.sort(similarNodes2);
        Collections.sort(similarEdges1);
        Collections.sort(similarEdges2);
        HashMap<String,Object> expected = new HashMap<>();
        expected.put("SimilarNodes1", similarNodes1);
        expected.put("SimilarNodes2", similarNodes2);
        expected.put("SimilarEdges1", similarEdges1);
        expected.put("SimilarEdges2", similarEdges2);

        assertEquals("compareTwoGraphs does not correctly identify similar nodes and edges.", expected, model.compareTwoGraphs("1", "2"));

    }
}
