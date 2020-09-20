package com.RepGraph;

import org.junit.Test;

import java.lang.reflect.Field;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
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
        assertEquals("Lemma was not retrieved properly", result, "testlemma");

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
        assertEquals("Carg was not retrieved properly", result, "testcarg");

    }

    @Test
    public void test_setIndex_SetsValueCorrectly() throws NoSuchFieldException, IllegalAccessException {


        final token t = new token();

        t.setIndex(3);

        //get value not using getter
        final Field field = t.getClass().getDeclaredField("index");
        field.setAccessible(true);
        assertEquals("Index was not set properly", field.get(t), 3);
    }

    @Test
    public void test_setForm_SetsValueCorrectly() throws NoSuchFieldException, IllegalAccessException {


        final token t = new token();

        t.setForm("testform");

        //get value not using getter
        final Field field = t.getClass().getDeclaredField("form");
        field.setAccessible(true);
        assertEquals("Form was not set properly", field.get(t), "testform");
    }

    @Test
    public void test_setLemma_SetsValueCorrectly() throws NoSuchFieldException, IllegalAccessException {


        final token t = new token();

        t.setLemma("testlemma");

        //get value not using getter
        final Field field = t.getClass().getDeclaredField("lemma");
        field.setAccessible(true);
        assertEquals("Lemma was not set properly", field.get(t), "testlemma");
    }

    @Test
    public void test_setCarg_SetsValueCorrectly() throws NoSuchFieldException, IllegalAccessException {


        final token t = new token();

        t.setCarg("testcarg");

        //get value not using getter
        final Field field = t.getClass().getDeclaredField("carg");
        field.setAccessible(true);
        assertEquals("Carg was not set properly", field.get(t), "testcarg");
    }


    @Test
    public void test_equal_TwoTokensWithDifferentValues() {

        final token t1 = new token(0,"form1", "lemma1", "carg1");
        final token t2 = new token(1,"form2", "lemma2", "carg2");


        assertFalse("Equals does not work with two tokens with different values.", t1.equals(t2));
    }

    @Test
    public void test_equal_IdenticalTokensWithSameValues() {

        final token t1 = new token(0,"form1", "lemma1", "carg1");
        final token t2 = new token(0,"form1", "lemma1", "carg1");

        assertTrue("Equals does not work with two identical tokens with the same values.", t1.equals(t2));
    }

    @Test
    public void test_equal_TwoObjectsOfDifferentClasses() {

        final token t1 = new token(0,"form1", "lemma1", "carg1");

        assertFalse("Equals does not work with two objects of different classes", t1.equals(new anchors(0, 1)));
    }

    @Test
    public void test_equal_EqualToItself() {

        final token t1 = new token(0,"form1", "lemma1", "carg1");

        assertTrue("Equals does not work with a token equalling itself.", t1.equals(t1));
    }


}
