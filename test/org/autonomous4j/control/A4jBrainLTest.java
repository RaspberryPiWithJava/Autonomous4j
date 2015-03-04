/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autonomous4j.control;

import java.util.List;
import org.autonomous4j.tracking.A4jBlackBox;
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
public class A4jBrainLTest {
    
    public A4jBrainLTest() {
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
     * Test of getInstance method, of class A4jBrainL.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        A4jBrainL expResult = null;
        A4jBrainL result = A4jBrainL.getInstance();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of connect method, of class A4jBrainL.
     */
    @Test
    public void testConnect_0args() {
        System.out.println("connect");
        A4jBrainL instance = null;
        boolean expResult = false;
        boolean result = instance.connect();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of connect method, of class A4jBrainL.
     */
    @Test
    public void testConnect_String() {
        System.out.println("connect");
        String ipAddress = "";
        A4jBrainL instance = null;
        boolean expResult = false;
        boolean result = instance.connect();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of disconnect method, of class A4jBrainL.
     */
    @Test
    public void testDisconnect() {
        System.out.println("disconnect");
        A4jBrainL instance = null;
        instance.disconnect();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of doFor method, of class A4jBrainL.
     */
    @Test
    public void testDoFor() {
        System.out.println("doFor");
        long ms = 0L;
        A4jBrainL instance = null;
        A4jBrainL expResult = null;
        A4jBrainL result = instance.doFor(ms);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hold method, of class A4jBrainL.
     */
    @Test
    public void testHold() {
        System.out.println("hold");
        long ms = 0L;
        A4jBrainL instance = null;
        A4jBrainL expResult = null;
        A4jBrainL result = instance.hold(ms);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of stay method, of class A4jBrainL.
     */
    @Test
    public void testStay() {
        System.out.println("stay");
        A4jBrainL instance = null;
        A4jBrainL expResult = null;
        A4jBrainL result = instance.stay();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of forward method, of class A4jBrainL.
     */
    @Test
    public void testForward_0args() {
        System.out.println("forward");
        A4jBrainL instance = null;
        A4jBrainL expResult = null;
        A4jBrainL result = instance.forward();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of forward method, of class A4jBrainL.
     */
    @Test
    public void testForward_int() {
        System.out.println("forward");
        A4jBrainL instance = null;
        A4jBrainL expResult = null;
        A4jBrainL result = instance.forward();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of backward method, of class A4jBrainL.
     */
    @Test
    public void testBackward_0args() {
        System.out.println("backward");
        A4jBrainL instance = null;
        A4jBrainL expResult = null;
        A4jBrainL result = instance.backward();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of backward method, of class A4jBrainL.
     */
    @Test
    public void testBackward_int() {
        System.out.println("backward");
        A4jBrainL instance = null;
        A4jBrainL expResult = null;
        A4jBrainL result = instance.backward();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of goRight method, of class A4jBrainL.
     */
    @Test
    public void testGoRight() {
        System.out.println("goRight");
        A4jBrainL instance = null;
        A4jBrainL expResult = null;
        A4jBrainL result = instance.right();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of goLeft method, of class A4jBrainL.
     */
    @Test
    public void testGoLeft() {
        System.out.println("goLeft");
        A4jBrainL instance = null;
        A4jBrainL expResult = null;
        A4jBrainL result = instance.left();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of goHome method, of class A4jBrainL.
     */
    @Test
    public void testGoHome() {
        System.out.println("goHome");
        A4jBrainL instance = null;
        A4jBrainL expResult = null;
        A4jBrainL result = instance.goHome();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of replay method, of class A4jBrainL.
     */
    @Test
    public void testReplay() {
        System.out.println("replay");
        A4jBrainL instance = null;
        A4jBrainL expResult = null;
        A4jBrainL result = instance.replay();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of left method, of class A4jBrainL.
     */
    @Test
    public void testLeft() {
        System.out.println("left");
        A4jBrainL instance = null;
        A4jBrainL expResult = null;
        A4jBrainL result = instance.left();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of right method, of class A4jBrainL.
     */
    @Test
    public void testRight() {
        System.out.println("right");
        A4jBrainL instance = null;
        A4jBrainL expResult = null;
        A4jBrainL result = instance.right();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of processRecordedMovements method, of class A4jBrainL.
     */
    @Test
    public void testProcessRecordedMovements() {
        System.out.println("processRecordedMovements");
        List<A4jBlackBox.Movement> moves = null;
        A4jBrainL instance = null;
        instance.processRecordedMovements(moves);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of move method, of class A4jBrainL.
     */
//    @Test
//    public void testMove() {
//        System.out.println("move");
//        float roll = 0.0F;
//        float pitch = 0.0F;
//        float gaz = 0.0F;
//        float yaw = 0.0F;
//        A4jBrainL instance = null;
//        A4jBrainL expResult = null;
//        A4jBrainL result = instance.move(roll, pitch, gaz, yaw);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
