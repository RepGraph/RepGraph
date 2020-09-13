package com.RepGraph;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class EdgeTest {


    @Test
    public void tests_getSource_GetsValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final edge e = new edge();

        //Set Field not using setter
        final Field field = e.getClass().getDeclaredField("source");
        field.setAccessible(true);
        field.set(e, 3);


        final int result = e.getSource();

        //then
        assertEquals("Source was not retrieved properly", result, 3);

    }


    @Test
    public void test_setSource_SetsValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final edge e = new edge();

        e.setSource(3);

        //get value not using getter
        final Field field = e.getClass().getDeclaredField("source");
        field.setAccessible(true);
        assertEquals("Source was not set properly", field.get(e), 3);
    }


    @Test
    public void tests_getTarget_GetsValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final edge e = new edge();

        //Set Field not using setter
        final Field field = e.getClass().getDeclaredField("target");
        field.setAccessible(true);
        field.set(e, 3);


        final int result = e.getTarget();

        //then
        assertEquals("Target was not retrieved properly", result, 3);

    }


    @Test
    public void test_setTarget_SetsValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final edge e = new edge();

        e.setTarget(3);

        //get value not using getter
        final Field field = e.getClass().getDeclaredField("target");
        field.setAccessible(true);

        assertEquals("Target was not set properly", field.get(e), 3);
    }


    @Test
    public void tests_getLabel_GetsValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final edge e = new edge();

        //Set Field not using setter
        final Field field = e.getClass().getDeclaredField("label");
        field.setAccessible(true);
        field.set(e, "test");


        final String result = e.getLabel();

        //then
        assertEquals("Label was not retrieved properly", result, "test");

    }


    @Test
    public void test_setLabel_SetsValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final edge e = new edge();

        e.setLabel("test");

        //get value not using getter
        final Field field = e.getClass().getDeclaredField("label");
        field.setAccessible(true);
        assertEquals("Label was not set properly", field.get(e), "test");
    }


    @Test
    public void tests_getPostLabel_GetsValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final edge e = new edge();

        //Set Field not using setter
        final Field field = e.getClass().getDeclaredField("postLabel");
        field.setAccessible(true);
        field.set(e, "test");


        final String result = e.getPostLabel();

        //then
        assertEquals("PostLabel was not retrieved properly", result, "test");

    }


    @Test
    public void test_setPostLabel_SetsValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final edge e = new edge();

        e.setPostLabel("test");

        //get value not using getter
        final Field field = e.getClass().getDeclaredField("postLabel");
        field.setAccessible(true);
        assertEquals("Post Label was not set properly", field.get(e), "test");
    }

}
