/*
 * The MIT License
 *
 * Copyright 2014 Mark A. Heckler
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.autonomous4j.control;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mark
 */
public class A4jBrainATest {
    
    public A4jBrainATest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getInstance method, of class A4jBrainA.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        A4jBrainA instance = null;
        //A4Brain expResult = null;
        A4jBrainA expResult = A4jBrainA.getInstance();
        A4jBrainA result = A4jBrainA.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of connect method, of class A4jBrainA.
     */
    @Test
    public void testConnect_0args() {
        System.out.println("connect");
        //A4Brain instance = new A4jBrainA();
        A4jBrainA instance = A4jBrainA.getInstance();
        boolean expResult = true;
        boolean result = instance.connect();
        assertEquals(expResult, result);
        instance.disconnect();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of connect method, of class A4jBrainA.
     */
    @Test
    public void testConnect_String() {
        System.out.println("connect");
        String ipAddress = "192.168.1.1";
        A4jBrainA instance = A4jBrainA.getInstance();
        boolean expResult = true;
        boolean result = instance.connect(ipAddress);
        assertEquals(expResult, result);
        instance.disconnect();
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of disconnect method, of class A4jBrainA.
     */
    @Test
    public void testDisconnect() {
        // Tested as part of connect() methods above.
//        System.out.println("disconnect");
//        // Have to connect to (independently) test disconnect method.
//        A4jBrainA instance = A4jBrainA.getInstance();
//        instance.connect();
//        instance.disconnect();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }
    
}
