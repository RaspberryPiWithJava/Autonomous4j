/*
 * The MIT License
 *
 * Copyright 2014, 2015 Mark A. Heckler
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

import com.dronecontrol.droneapi.data.enums.LedAnimation;

/**
 *
 * @author Mark Heckler (mark.heckler@gmail.com, @mkheck)
 */
public class A4jMain {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        final A4jBrainA brain = A4jBrainA.getInstance();
        if (brain.connect("192.168.1.1")) {
            // Comment/uncomment for desired demo flight. May not want to combine
            // them without testing first in unoccupied room. :)
            doDemoFlightBox(brain);
//            doDemoFlightBoxSmall(brain);
//            doDemoFlightHome(brain);
//            doDemoFlightReplay(brain);
//            doDemoFlightLeds(brain);
//            doDemoFlightCocarde(brain);
//            doDemoTakeoffLand(brain);
        }
        brain.disconnect();

        System.out.println("Exiting. So long and thanks for all the fish.");
        System.exit(0);
    }

    private static void doDemoTakeoffLand(A4jBrainA brain) {
        brain.takeoff().hold(3000);
        brain.land();
    }
    
    private static void doDemoFlightBox(A4jBrainA brain) {
        brain.takeoff().hold(6000);
        
        brain.forward(30).doFor(900);
        brain.backward(30).doFor(150);
        brain.hover().hold(2000);
        
        brain.goRight(30).doFor(900);
        brain.goLeft(30).doFor(150);
        brain.hover().hold(2000);
        
        brain.backward(30).doFor(700);
        brain.forward(30).doFor(150);
        brain.hover().hold(2000);
        
        brain.goLeft(30).doFor(1000);
        brain.goRight(30).doFor(150);
        brain.hover().hold(2000);
        
        brain.forward(30).doFor(700);
        brain.backward(30).doFor(150);
        brain.hover().hold(2000);
        
        brain.land();
    }

    private static void doDemoFlightBoxSmall(A4jBrainA brain) {
        brain.takeoff().hold(6000);
        
        brain.forward(20).doFor(600);
        brain.stay().hold(2000);
        brain.goRight(20).doFor(600);
        brain.stay().hold(2000);
        brain.backward(20).doFor(400);
        brain.stay().hold(2000);
        brain.goLeft(20).doFor(900);
        brain.stay().hold(2000);
        brain.forward(20).doFor(500);
        brain.stay().hold(2000);
        
        brain.land();
    }

    private static void doDemoFlightHome(A4jBrainA brain) {
        brain.takeoff().hold(6000);

        brain.forward(20).doFor(400);
        brain.stay().hold(2000);
        brain.goRight(20).doFor(400);
        brain.stay().hold(2000);
        brain.forward(20).doFor(400);
        brain.stay().hold(2000);
        brain.goRight(20).doFor(400);
        brain.stay().hold(2000);
        brain.backward(20).doFor(400);
        brain.stay().hold(2000);

        brain.goHome();
        brain.stay().hold(2000);

        brain.land();
    }
    
    private static void doDemoFlightReplay(A4jBrainA brain) {
        doDemoFlightBox(brain);
        // Added two-second wait while on ground for effect
        brain.hold(2000);
        brain.replay();
    }
    
    private static void doDemoFlightCocarde(A4jBrainA brain) {
        brain.takeoff().hold(6000);
        // Hover in place over cocarde/roundel, allowing demo of push/recover
        brain.stay().doFor(3000);
        brain.playLedAnimation(LedAnimation.LEFT_GREEN_RIGHT_RED, 10, 3);
        brain.stay().doFor(3000);
        
        brain.land();
    }        

    private static void doDemoFlightLeds(A4jBrainA brain) {
        brain.takeoff().hold(6000);
        
        brain.playLedAnimation(LedAnimation.BLING_GREEN, 10, 3);
        brain.stay().hold(2000);
        brain.playLedAnimation(LedAnimation.BLINK_RED, 10, 3);
        brain.stay().hold(2000);
        brain.playLedAnimation(LedAnimation.FIRE, 10, 3);
        brain.stay().hold(2000);
        brain.playLedAnimation(LedAnimation.DOUBLE_MISSILE, 10, 3);
        brain.stay().hold(2000);
        
        brain.land();
    }
}
