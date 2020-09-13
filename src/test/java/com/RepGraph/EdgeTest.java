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
        assertEquals("field wasn't retrieved properly", result, 3);

    }


}
