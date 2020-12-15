package com.RepGraph;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;

public class AnchorsTest {

    @Test
    public void test_constructor_CreatesAndAssignsMemberVariables() throws NoSuchFieldException, IllegalAccessException {

        final Anchors anch = new Anchors(3, 5);

        final Field from = anch.getClass().getDeclaredField("from");
        from.setAccessible(true);

        final Field end = anch.getClass().getDeclaredField("end");
        end.setAccessible(true);

        assertEquals("from value was not assigned properly in constructor", from.get(anch), 3);
        assertEquals("end value was not assigned properly in constructor", end.get(anch), 5);

    }

    @Test
    public void test_getFrom_GetsValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final Anchors anch = new Anchors();

        //Set Field not using setter
        final Field field = anch.getClass().getDeclaredField("from");
        field.setAccessible(true);
        field.set(anch, 3);


        final int result = anch.getFrom();

        //then
        assertEquals("From value was not retrieved properly", result, 3);

    }


    @Test
    public void test_setFrom_SetsValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final Anchors anch = new Anchors();

        anch.setFrom(3);

        //get value not using getter
        final Field field = anch.getClass().getDeclaredField("from");
        field.setAccessible(true);
        assertEquals("From value was not set properly", field.get(anch), 3);
    }

    @Test
    public void test_getEnd_GetsValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final Anchors anch = new Anchors();

        //Set Field not using setter
        final Field field = anch.getClass().getDeclaredField("end");
        field.setAccessible(true);
        field.set(anch, 3);


        final int result = anch.getEnd();

        //then
        assertEquals("End value was not retrieved properly", result, 3);

    }

    @Test
    public void test_setEnd_SetsValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final Anchors anch = new Anchors();

        anch.setEnd(3);

        //get value not using getter
        final Field field = anch.getClass().getDeclaredField("end");
        field.setAccessible(true);
        assertEquals("End value was not set properly", field.get(anch), 3);
    }

    @Test
    public void test_equal_TwoAnchorsWithDifferentValues() {

        final Anchors a1 = new Anchors(0, 1);
        final Anchors a2 = new Anchors(2, 3);

        assertFalse("Equals does not work with two Anchors with different values.", a1.equals(a2));
    }

    @Test
    public void test_equal_IdenticalAnchorsWithSameValues() {

        final Anchors a1 = new Anchors(0, 1);
        final Anchors a2 = new Anchors(0, 1);

        assertTrue("Equals does not work with two identical Anchors with the same values.", a1.equals(a2));
    }

    @Test
    public void test_equal_TwoObjectsOfDifferentClasses() {

        ArrayList<Anchors> anchs = new ArrayList<Anchors>();
        anchs.add(new Anchors(0, 1));
        final Node n1 = new Node(3, "proper_q", anchs);

        final Anchors a1 = new Anchors(0, 1);

        assertFalse("Equals does not work with two objects of different classes", a1.equals(n1));
    }

    @Test
    public void test_equal_EqualToItself() {

        final Anchors a1 = new Anchors(0, 1);

        assertTrue("Equals does not work with a Node equalling itself.", a1.equals(a1));
    }



}
