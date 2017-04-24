package com.basic.reflect;

import org.junit.Test;

/**
 * Created by dello on 2016/6/7.
 */
public class ClassUtilsTest {

    @org.junit.Before
    public void setUp() throws Exception {

    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @org.junit.Test
    public void testPrintClassMessage() throws Exception {
        String s="hello";
//      ClassUtils.printClassMessage(new Integer(2));
        ClassUtils.printClassMessage(s);
    }

    @Test
    public void testPrintMethodMessage() throws Exception {
        ClassUtils.printMethodMessage(new Integer(1));
    }

    @Test
    public void testPrintFieldMessage() throws Exception {

    }

    @Test
    public void testPrintConstrustMeassage() throws Exception {
        ClassUtils.printConstrustMeassage(new Integer(1));
    }
}
