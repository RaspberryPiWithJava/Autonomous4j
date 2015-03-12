/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autonomous4j.physical;

import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPortEvent;
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
public class A4jLandControllerTest {
    A4jLandController instance;
    
    public A4jLandControllerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        try {
            instance = new A4jLandController();
            instance.connect();
        } catch (Exception ex) {
            Logger.getLogger(A4jLandControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @After
    public void tearDown() {
        instance.disconnect();
    }

    /**
     * Test of connect method, of class LandController.
     */
    /*
    @Test
    public void testConnect() throws Exception {
        try {
            System.out.println("connect");
            if (!instance.connect()) {
                fail("Failed to connect.");
            }
            instance.disconnect();
        } catch (Exception ex) {
            Logger.getLogger(A4jLandControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    */

    /**
     * Test of disconnect method, of class LandController.
     */
    /*
    @Test
    public void testDisconnect() {
        try {
            System.out.println("disconnect");
            if (!instance.connect()) {
                fail("Failed to connect in disconnect test.");
            } else {
                if (!instance.disconnect()) {                
                    fail("Failed to disconnect.");
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(A4jLandControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    */

    /**
     * Test of logIt method, of class A4jLandController.
     */
    @Test
    public void testLogIt() {
        System.out.println("logIt");
        String entry = "Test entry.";
        A4jLandController.logIt(entry);
//        fail("The test case is a prototype.");
    }

    /**
     * Test of serialEvent method, of class A4jLandController.
     * Moved to inner class - MAH.
     */
//    @Test
//    public void testSerialEvent() {
//        try {
//            System.out.println("serialEvent");
//            SerialPortEvent event = new SerialPortEvent("/dev/tty.usbmodem1a1221", 
//                    SerialPortEvent.RXCHAR, SerialPortEvent.RXCHAR);
//            instance.serialEvent(event);
//        } catch (Exception ex) {
//            Logger.getLogger(A4jLandControllerTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }    

    /**
     * Test of forward method, of class A4jLandController.
     */
    @Test
    public void testForward() {
        try {
            System.out.println("forward");
            instance.forward(50);
        } catch (Exception ex) {
            Logger.getLogger(A4jLandControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of back method, of class A4jLandController.
     */
    @Test
    public void testBack() {
        try {
            System.out.println("back");
            instance.back(50);
        } catch (Exception ex) {
            Logger.getLogger(A4jLandControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of left method, of class A4jLandController.
     */
    @Test
    public void testLeft() {
        try {
            System.out.println("left");
            instance.left(50);
        } catch (Exception ex) {
            Logger.getLogger(A4jLandControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of right method, of class A4jLandController.
     */
    @Test
    public void testRight() {
        try {
            System.out.println("right");
            instance.right(50);
        } catch (Exception ex) {
            Logger.getLogger(A4jLandControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of stop method, of class A4jLandController.
     */
    @Test
    public void testStop() {
        try {
            System.out.println("stop");
            instance.stop();
        } catch (Exception ex) {
            Logger.getLogger(A4jLandControllerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of connect method, of class A4jLandController.
     */
    @Test
    public void testConnect() throws Exception {
        System.out.println("connect");
        A4jLandController instance = new A4jLandController();
        boolean expResult = false;
        boolean result = instance.connect();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of disconnect method, of class A4jLandController.
     */
    @Test
    public void testDisconnect() {
        System.out.println("disconnect");
        A4jLandController instance = new A4jLandController();
        boolean expResult = false;
        boolean result = instance.disconnect();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of notifyObservers method, of class A4jLandController.
     */
    @Test
    public void testNotifyObservers() {
        System.out.println("notifyObservers");
        Object arg = null;
        A4jLandController instance = new A4jLandController();
        instance.notifyObservers(arg);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
