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
    enum BoxSize {SMALL, LARGE};
    final int OPP_THRUST = 6;
    final int HOVER_TIME = 2000;
    final A4jBrainA brain = A4jBrainA.getInstance();
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            showHelp();
        } else {
            A4jMain controller = new A4jMain();
            
            if (controller.connect()) {
                switch (args[0]) {
                    case "BOX":
                        controller.doDemoFlightBox(BoxSize.LARGE);
                        break;
                    case "HOME":
                        controller.doDemoFlightHome();
                        break;
                    case "TAKEOFFLAND":
                        controller.doDemoTakeoffLand();
                        break;
                    case "LEDS":
                        controller.doDemoFlightLeds();
                        break;
                    case "COCARDE":
                        controller.doDemoFlightCocarde();
                        break;
                    case "REPLAYBOX":
                        controller.doDemoFlightReplay(BoxSize.LARGE);
                        break;
                    case "REPLAYSMALLBOX":
                        controller.doDemoFlightReplay(BoxSize.SMALL);
                        break;
                    default: //"SMALLBOX"
                        controller.doDemoFlightBox(BoxSize.SMALL);
                        break;                    
                }
            }
            controller.disconnect();

            System.out.println("Exiting. So long and thanks for all the fish.");
            System.exit(0);
        }
    }

    private static void showHelp() {
        System.out.println("\nAutoFly (Autonomous4jGA) cheat sheet:\n");
        System.out.println("java -jar AutoFly <pattern>");
        System.out.println("...where <pattern> is replaced by one of the following:\n");
        System.out.println("BOX\t\tNavigates a rather large box (exercise caution).");
        System.out.println("SMALLBOX\tFlies a smaller, more manageable box pattern.");
        System.out.println("HOME\t\tFlies pattern, then returns directly to departure point.");
        System.out.println("TAKEOFFLAND\tTakes off, hovers a few seconds, & lands.");
        System.out.println("LEDS\t\tCycles through LED patterns while hovering.");
        System.out.println("COCARDE\t\tHovers over cocarde/roundel for short 'push' demo.");
        System.out.println("REPLAYBOX\tPerforms BOX pattern, lands, then repeats from memory.\t");
        System.out.println("REPLAYSMALLBOX\tPerforms SMALLBOX pattern, lands, then repeats from memory.\t");
        System.out.println("\nRecommended pattern: SMALLBOX");
        System.out.println("\nIf comm/control is lost, fly TAKEOFFLAND to restore & recover.\n\n");
    }

    private boolean connect() {
        return brain.connect("192.168.1.1");
    }
    
    private void disconnect() {
        brain.disconnect();
    }
    
    private void doDemoTakeoffLand() {
        brain.takeoff().hold(3000);
        brain.land();
    }
    
    private void doDemoFlightBox(BoxSize size) {
        int[] duration;
        
        if (size == BoxSize.SMALL) {
            duration = new int[] {600, 600, 400, 900, 500};
        } else {    // BoxSize.LARGE
            duration = new int[] {900, 900, 700, 1000, 700};
        }
                
        brain.takeoff().hold(HOVER_TIME * 3);
        
        // "Offsetting" thrust maneuvers provide better indoor self-control :)
        brain.forward(30).doFor(duration[0]);
        brain.backward(30).doFor(duration[0]/OPP_THRUST);
        brain.hover().hold(HOVER_TIME);
        
        brain.goRight(30).doFor(duration[1]);
        brain.goLeft(30).doFor(duration[1]/OPP_THRUST);
        brain.hover().hold(HOVER_TIME);
        
        brain.backward(30).doFor(duration[2]);
        brain.forward(30).doFor(duration[2]/OPP_THRUST);
        brain.hover().hold(HOVER_TIME);
        
        brain.goLeft(30).doFor(duration[3]);
        brain.goRight(30).doFor(duration[3]/OPP_THRUST);
        brain.hover().hold(HOVER_TIME);
        
        brain.forward(30).doFor(duration[4]);
        brain.backward(30).doFor(duration[4]/OPP_THRUST);
        brain.hover().hold(HOVER_TIME);
        
        brain.land();
    }

    private void doDemoFlightHome() {
        final int flightTime = 400;
        
        brain.takeoff().hold(6000);

        brain.forward(20).doFor(flightTime);
        brain.backward(20).doFor(flightTime/OPP_THRUST);
        brain.stay().hold(HOVER_TIME);
        
        brain.goRight(20).doFor(flightTime);
        brain.goLeft(20).doFor(flightTime/OPP_THRUST);
        brain.stay().hold(HOVER_TIME);
        
        brain.forward(20).doFor(flightTime);
        brain.backward(20).doFor(flightTime/OPP_THRUST);
        brain.stay().hold(HOVER_TIME);
        
        brain.goRight(20).doFor(flightTime);
        brain.goLeft(20).doFor(flightTime/OPP_THRUST);
        brain.stay().hold(HOVER_TIME);
        
        brain.backward(20).doFor(flightTime);
        brain.forward(20).doFor(flightTime/OPP_THRUST);
        brain.stay().hold(HOVER_TIME);

        brain.goHome();
        brain.stay().hold(HOVER_TIME);

        brain.land();
    }
    
    private void doDemoFlightReplay(BoxSize size) {
        doDemoFlightBox(size);
        
        // Added two-second wait while on ground for effect
        brain.hold(HOVER_TIME);
        brain.replay();
    }
    
    private void doDemoFlightCocarde() {
        brain.takeoff().hold(6000);
        // Hover in place over cocarde/roundel, allowing demo of push/recover
        brain.stay().doFor(3000);
        brain.playLedAnimation(LedAnimation.LEFT_GREEN_RIGHT_RED, 10, 3);
        brain.stay().doFor(3000);
        
        brain.land();
    }        

    private void doDemoFlightLeds() {
        brain.takeoff().hold(6000);
        
        brain.playLedAnimation(LedAnimation.BLING_GREEN, 10, 3);
        brain.stay().hold(HOVER_TIME);
        
        brain.playLedAnimation(LedAnimation.BLINK_RED, 10, 3);
        brain.stay().hold(HOVER_TIME);
        
        brain.playLedAnimation(LedAnimation.FIRE, 10, 3);
        brain.stay().hold(HOVER_TIME);
        
        brain.playLedAnimation(LedAnimation.DOUBLE_MISSILE, 10, 3);
        brain.stay().hold(HOVER_TIME);
        
        brain.land();
    }
}
