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

import org.autonomous4j.interfaces.A4jBrain2D;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.autonomous4j.physical.LandController;
//import org.autonomous4j.listeners.A4jErrorListener;
//import org.autonomous4j.listeners.A4jNavDataListener;
//import org.autonomous4j.listeners.A4jReadyStateChangeListener;
//import org.autonomous4j.listeners.A4jVideoDataListener;
import org.autonomous4j.tracking.A4jBlackBox;
import org.autonomous4j.tracking.A4jBlackBox.Movement;

/**
 *
 * @author Mark Heckler (mark.heckler@gmail.com, @mkheck)
 */
public class A4jBrainL implements A4jBrain2D {
    private static final A4jBrainL brain = new A4jBrainL();
    private final LandController controller = new LandController();
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
//            controller = ParrotDroneController.build();
//            //cfg = new Config("Autonomous4j Test", "My Profile", 0);
//            controller.start();
//
//            //controller.addVideoDataListener(new A4jVideoDataListener());
//            controller.addNavDataListener(new A4jNavDataListener());    
//            controller.addReadyStateChangeListener(new A4jReadyStateChangeListener());
//            controller.addErrorListener(new A4jErrorListener());
            controller.connect();
        } catch (Exception ex) {
            System.err.println("Exception creating new drone connection: " + ex.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void disconnect() {
        if (controller != null) {
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
    public A4jBrainL forward() {
        System.out.println("Forward");
        if (isRecording) {
            //recorder.recordAction(A4jBlackBox.Action.FORWARD, speed);
        }

        controller.forward();
        return this;
    }

    @Override
    public A4jBrainL backward() {
        System.out.println("Backward");
        if (isRecording) {
            //recorder.recordAction(A4jBlackBox.Action.BACKWARD, speed);
        }

        controller.back();
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
    public A4jBrainL left() {
        System.out.println("Turn Left");
        if (isRecording) {
            //recorder.recordAction(A4jBlackBox.Action.LEFT, speed);
        }

        // MAH: Add in speed/duration/bearing.
        // MAH: Add in direction/distance? (enh)
        controller.left();
        return this;
    }

    @Override
    public A4jBrainL right() {
        System.out.println("Turn Right");
        if (isRecording) {
            //recorder.recordAction(A4jBlackBox.Action.RIGHT, speed);
        }

        controller.right();
        return this;
    }
    
    @Override
    public void processRecordedMovements(List<Movement> moves) {
        // Disable recording for playback
        isRecording = false;

        for (Movement curMov : moves) {
            switch(curMov.getAction()) {
                case FORWARD:
                    forward();
                    break;
                case BACKWARD:
                    backward();
                    break;
                case RIGHT:
                    right();
                    break;
                case LEFT:
                    left();
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
    
    /*
    private float limit(float f, float min, float max) {
        return (f > max ? max : (f < min ? min : f));
    }

    public A4jBrainL move(float roll ,float pitch, float gaz, float yaw) {
        roll = limit(roll, -1f, 1f);
        pitch = limit(pitch, -1f, 1f);
        gaz = limit(gaz, -1f, 1f);
        yaw = limit(yaw, -1f, 1f);
        
//        controller.move(roll, pitch, yaw, gaz);
        
        return this;
    }

    private float perc2float(int speed) {
        return (float) (speed / 100.0f);
    }
    */
}
