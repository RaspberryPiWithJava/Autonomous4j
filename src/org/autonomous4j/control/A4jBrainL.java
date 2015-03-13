/*
 * The MIT License
 *
 * Copyright 2015 Mark A. Heckler
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

import java.util.ArrayList;
import org.autonomous4j.interfaces.A4jBrain2D;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.autonomous4j.listeners.xy.A4jLandListener;
import org.autonomous4j.physical.A4jLandController;
import org.autonomous4j.tracking.A4jBlackBox;
import org.autonomous4j.tracking.A4jBlackBox.Movement;

/**
 *
 * @author Mark Heckler (mark.heckler@gmail.com, @mkheck)
 */
public class A4jBrainL implements A4jBrain2D {
    private static final A4jBrainL brain = new A4jBrainL();
    private final A4jLandController controller = new A4jLandController();
    private final List<A4jLandListener> listeners = new ArrayList<>();
    //private NavData currentNav;
    //private final A4jBlackBox recorder;
    private boolean isRecording;

    private A4jBrainL() {
        //this.recorder = new A4jBlackBox();
        isRecording = true;
    }

    public static A4jBrainL getInstance() {
        return brain;
    }

    @Override
    public boolean connect() {
        try {
            controller.connect();
            // Local MQTT server
            listeners.add(new A4jLandListener());
            // Remote MQTT cloud servers
//            listeners.add(new A4jLandListener("tcp://m11.cloudmqtt.com:14655")
//                    .setUserName("<userID>")
//                    .setPassword("<password>"));
            listeners.add(new A4jLandListener("tcp://iot.eclipse.org:1883"));
            
            listeners.stream().forEach((listener) -> controller.addObserver(listener.connect()));

        } catch (Exception ex) {
            System.err.println("Exception creating new drone connection: " + ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void disconnect() {
        if (controller != null) {
            if (!listeners.isEmpty()) {
                listeners.stream().forEach((listener) -> listener.disconnect());
                
                controller.deleteObservers();
            }
            
            controller.disconnect();
        }
        //recorder.shutdown();
    }

    /**
     * Convenience (pass-through) method for more fluent API.
     * @param ms Long variable specifying a number of milliseconds.
     * @return A4jBrainL object (allows command chaining/fluency.
     * @see #hold(long)
     */
    @Override
    public A4jBrainL doFor(long ms) {
        return hold(ms);
    }
    
    @Override
    public A4jBrainL hold(long ms) {
        System.out.println("Hold for " + ms + " milliseconds...");
        try {
            Thread.sleep(ms);
            if (isRecording) {
                //recorder.recordDuration(ms);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Logger.getLogger(A4jBrainL.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return this;
    }

    @Override
    public A4jBrainL stay() {
        System.out.println("--Stay--");
        controller.stop();
        if (isRecording) {
            //recorder.recordAction(A4jBlackBox.Action.STAY);
        }
        
        return this;
    }

    @Override
    public A4jBrainL forward(long distance) {
        System.out.println("Forward");
        if (isRecording) {
            //recorder.recordAction(A4jBlackBox.Action.FORWARD, speed);
        }

        controller.forward(distance);
        return this;
    }

    @Override
    public A4jBrainL backward(long distance) {
        System.out.println("Backward");
        if (isRecording) {
            //recorder.recordAction(A4jBlackBox.Action.BACKWARD, speed);
        }

        controller.back(distance);
        return this;
    }

    @Override
    public A4jBrainL goHome() {
        //processRecordedMovements(recorder.home());
        return this;
    }
    
    @Override
    public A4jBrainL replay() {
        //processRecordedMovements(recorder.getRecording());
        return this;
    }
    
    @Override
    public A4jBrainL left(long degrees) {
        System.out.println("Turn left " + degrees + " degrees.");
        if (isRecording) {
            //recorder.recordAction(A4jBlackBox.Action.LEFT, speed);
        }

        // MAH: Add in speed/duration/bearing.
        // MAH: Add in direction/distance? (enh)
        controller.left(degrees);
        return this;
    }

    @Override
    public A4jBrainL right(long degrees) {
        System.out.println("Turn right " + degrees + " degrees.");
        if (isRecording) {
            //recorder.recordAction(A4jBlackBox.Action.RIGHT, speed);
        }

        controller.right(degrees);
        return this;
    }
    
    @Override
    public void processRecordedMovements(List<Movement> moves) {
        // Disable recording for playback
        isRecording = false;

        // MAH: Fix this to replay (after recording) distances/degrees.
        for (Movement curMov : moves) {
            switch(curMov.getAction()) {
                case FORWARD:
                    forward(0);
                    break;
                case BACKWARD:
                    backward(0);
                    break;
                case RIGHT:
                    right(0);
                    break;
                case LEFT:
                    left(0);
                    break;
                case STAY:
                    stay();
                    break;
            }
            hold(curMov.getDuration());
            System.out.println(curMov);
        }
            
        // Re-enable recording
        isRecording = true;    
    }    
}
