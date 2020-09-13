package com.RepGraph;

import org.junit.Test;

import java.lang.reflect.Field;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class TokenTest {


    @Test
    public void test_constructor_CreatesAndAssignsMemberVariables() throws NoSuchFieldException, IllegalAccessException {

        final token t = new token(3, "testform", "testlemma", "testcarg");

        final Field index = t.getClass().getDeclaredField("index");
        index.setAccessible(true);

        final Field form = t.getClass().getDeclaredField("form");
        form.setAccessible(true);

        final Field lemma = t.getClass().getDeclaredField("lemma");
        lemma.setAccessible(true);

        final Field carg = t.getClass().getDeclaredField("carg");
        carg.setAccessible(true);

        assertEquals("index value was not assigned properly in constructor", index.get(t), 3);
        assertEquals("form value was not assigned properly in constructor", form.get(t), "testform");
        assertEquals("lemma value was not assigned properly in constructor", lemma.get(t), "testlemma");
        assertEquals("carg value was not assigned properly in constructor", carg.get(t), "testcarg");

    }

    @Test
    public void test_getIndex_GetsValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final token t = new token();

        //Set Field not using setter
        final Field field = t.getClass().getDeclaredField("index");
        field.setAccessible(true);
        field.set(t, 3);


        final int result = t.getIndex();

        //then
        assertEquals("Index was not retrieved properly", result, 3);

    }

    @Test
    public void test_getForm_GetsValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final token t = new token();

        //Set Field not using setter
        final Field field = t.getClass().getDeclaredField("form");
        field.setAccessible(true);
        field.set(t, "testform");


        final String result = t.getForm();

        //then
        assertEquals("Form was not retrieved properly", result, "testform");

    }

    @Test
    public void test_getLemma_GetsValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final token t = new token();

        //Set Field not using setter
        final Field field = t.getClass().getDeclaredField("lemma");
        field.setAccessible(true);
        field.set(t, "testlemma");


        final String result = t.getLemma();

        //then
        assertEquals("Index was not retrieved properly", result, "testlemma");

    }

    @Test
    public void test_getCarg_GetsValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final token t = new token();

        //Set Field not using setter
        final Field field = t.getClass().getDeclaredField("carg");
        field.setAccessible(true);
        field.set(t, "testcarg");


        final String result = t.getCarg();

        //then
        assertEquals("Index was not retrieved properly", result, "testcarg");

    }





}
