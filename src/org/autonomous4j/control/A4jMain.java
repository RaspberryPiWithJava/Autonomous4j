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
        final A4jBrain brain = A4jBrain.getInstance();
        if (brain.connect("192.168.1.1")) {
            brain.takeoff();
            brain.hold(6000);
            brain.playLedAnimation(LedAnimation.DOUBLE_MISSILE, 10, 5);
            brain.forward(20).doFor(600);
            brain.hover().hold(2000);
            brain.goRight(20).doFor(600);
            brain.hover().hold(2000);
            brain.backward(20).doFor(200);
            brain.hover().hold(2000);
            brain.goHome();
            
            brain.land();
//            brain.hold(2000);
//            brain.replay();
        }
        brain.disconnect();

        System.out.println("Exiting. So long and thanks for all the fish.");
        System.exit(0);
    }
}
