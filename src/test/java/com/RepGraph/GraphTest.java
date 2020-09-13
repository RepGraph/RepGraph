package com.RepGraph;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static junit.framework.TestCase.assertEquals;


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
        edges.add(new edge(1, 3, "testlabel", "testpostlabel"));
        edges.add(new edge(0, 2, "testlabel", "testpostlabel"));

        tokens.add(new token(0, "node 1", "node1", "node1"));
        tokens.add(new token(1, "node 2", "node2", "node2"));
        tokens.add(new token(2, "node 3", "node3", "node3"));
        tokens.add(new token(3, "node 4", "node4", "node4"));

        graph g = new graph("11111", "testsource", "node1 node2 node3 node4", nodes, tokens, edges);

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

}
