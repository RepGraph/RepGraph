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



}
