package com.RepGraph;

import org.junit.Before;
import org.junit.Test;

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

    @Test
    public void test_getId_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{
        final graph g = new graph();

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("id");
        field.setAccessible(true);
        field.set(g, "11111");

        assertEquals("id value was not retrieved properly.", g.getId(), "11111");
    }

    @Test
    public void test_getSource_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{
        final graph g = new graph();

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("source");
        field.setAccessible(true);
        field.set(g, "testsource");

        assertEquals("source value was not retrieved properly.", g.getSource(), "testsource");
    }

    @Test
    public void test_getInput_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{
        final graph g = new graph();

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("input");
        field.setAccessible(true);
        field.set(g, "node1 node2 node3 node4");

        assertEquals("input value was not retrieved properly.", g.getInput(), "node1 node2 node3 node4");
    }

    @Test
    public void test_getNodes_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{
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
    public void test_getTokens_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{
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
    public void test_getEdges_GetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{
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
    public void test_setId_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{

        final graph g = new graph();

        g.setId("11111");

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("id");
        field.setAccessible(true);

        assertEquals("id value was not retrieved properly.", field.get(g), "11111");
    }

    @Test
    public void test_setSource_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{

        final graph g = new graph();

        g.setSource("testSource");

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("source");
        field.setAccessible(true);

        assertEquals("source value was not retrieved properly.", field.get(g), "testSource");
    }

    @Test
    public void test_setInput_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{

        final graph g = new graph();

        g.setInput("node1 node2 node3 node4");

        //Set field without using setter
        final Field field = g.getClass().getDeclaredField("input");
        field.setAccessible(true);

        assertEquals("input value was not retrieved properly.", field.get(g), "node1 node2 node3 node4");
    }

    @Test
    public void test_setNodes_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{

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
    public void test_setTokens_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{

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
    public void test_setEdges_SetValueCorrectly() throws NoSuchFieldException, IllegalAccessException{

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


        ArrayList<anchors> anchs = new  ArrayList<>();
        anchs.add(new anchors(1,2));

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

        graph g1 = new graph("11111", "testsource1", "node1 node2 node3 node4", nodes1, tokens1, edges1);
        graph g2 = new graph("11111", "testsource2", "node1 node2 node3 node4", nodes2, tokens2, edges2);


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

        ArrayList<anchors> anchs = new  ArrayList<>();
        anchs.add(new anchors(1,2));

        nodes1.add(new node(0, "node1",anchs));
        nodes2.add(new node(0, "node1", anchs));

        edges1.add(new edge(0, 1, "testlabel", "testpostlabel"));
        edges2.add(new edge(0, 1, "testlabel", "testpostlabel"));

        tokens1.add(new token(0, "node1 form", "node1 lemma", "node1 carg"));
        tokens2.add(new token(0, "node1 form", "node1 lemma", "node1 carg"));

        graph g1 = new graph("11111", "testsource", "node1 node2 node3 node4", nodes1, tokens1, edges1);
        graph g2 = new graph("11111", "testsource", "node1 node2 node3 node4", nodes2, tokens2, edges2);

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

        graph g1 = new graph("11111", "testsource", "node1 node2 node3 node4", nodes1, tokens1, edges1);

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

        graph g1 = new graph("11111", "testsource", "node1 node2 node3 node4", nodes1, tokens1, edges1);

        assertTrue("Equals does not work with a token equalling itself.", g1.equals(g1));
    }


}
