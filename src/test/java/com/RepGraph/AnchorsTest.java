package com.RepGraph;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.springframework.test.util.AssertionErrors.assertEquals;

public class AnchorsTest {


    @Test
    public void tests_getFrom_GetsValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final anchors anch = new anchors();

        //Set Field not using setter
        final Field field = anch.getClass().getDeclaredField("from");
        field.setAccessible(true);
        field.set(anch, 3);


        final int result = anch.getFrom();

        //then
        assertEquals("field wasn't retrieved properly", result, 3);

    }


    @Test
    public void test_setFrom_SetsValueCorrectly() throws NoSuchFieldException, IllegalAccessException {

        final anchors anch = new anchors();

        anch.setFrom(3);

        //get value not using getter
        final Field field = anch.getClass().getDeclaredField("from");
        field.setAccessible(true);
        assertEquals("Fields didn't match", field.get(anch), 3);
    }

}
