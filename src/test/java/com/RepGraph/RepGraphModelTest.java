package com.RepGraph;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import static junit.framework.TestCase.assertEquals;


public class RepGraphModelTest {

    private graph g;

    @Before
    public void construct_test_graph(){
        ArrayList<node> nodes = new ArrayList<>();
        ArrayList<edge> edges = new ArrayList<>();
        ArrayList<token> tokens = new ArrayList<>();

        nodes.add(new node(0, "node1", new ArrayList<anchors>()));
        nodes.add(new node(1, "node2", new ArrayList<anchors>()));
        nodes.add(new node(2, "node3", new ArrayList<anchors>()));
        nodes.add(new node(3, "node4", new ArrayList<anchors>()));

        edges.add(new edge(0, 1, "testlabel", "testpostlabel"));
        edges.add(new edge(1, 3, "testlabel", "testpostlabel"));
        edges.add(new edge(0, 2, "testlabel", "testpostlabel"));

        tokens.add(new token(0, "node1 form", "node1 lemma", "node1 carg"));
        tokens.add(new token(1, "node2 form", "node2 lemma", "node2 carg"));
        tokens.add(new token(2, "node3 form", "node3 lemma", "node3 carg"));
        tokens.add(new token(3, "node4 form", "node4 lemma", "node4 carg"));

        g = new graph("11111", "testsource", "node1 node2 node3 node4", nodes, tokens, edges);
    }

    @Test
    public void test_getGraph_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{

        construct_test_graph();

        HashMap<String, graph> graphs = new HashMap<>();
        graphs.put("11111", g);

        RepGraphModel r = new RepGraphModel();

        //Set field without using setter
        final Field field = r.getClass().getDeclaredField("graphs");
        field.setAccessible(true);
        field.set(r, graphs);


        assertEquals("graph value was not retrieved properly.", r.getGraph("11111"),g);
    }

    @Test
    public void test_addGraph_AddGraphCorrectly() throws NoSuchFieldException, IllegalAccessException{

        construct_test_graph();

        HashMap<String, graph> graphs = new HashMap<>();
        graphs.put("11111", g);


        RepGraphModel r = new RepGraphModel();
        r.addGraph(g);

        //Get fields without using getter
        final Field field = r.getClass().getDeclaredField("graphs");
        field.setAccessible(true);

        assertEquals("graph value was not added correctly.", field.get(r) , graphs);
    }

}
