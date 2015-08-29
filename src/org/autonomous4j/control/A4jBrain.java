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

import com.dronecontrol.droneapi.DroneController;
import com.dronecontrol.droneapi.ParrotDroneController;
import com.dronecontrol.droneapi.commands.composed.PlayLedAnimationCommand;
import com.dronecontrol.droneapi.data.Config;
import com.dronecontrol.droneapi.data.LoginData;
import com.dronecontrol.droneapi.data.enums.LedAnimation;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.autonomous4j.listeners.A4jErrorListener;
import org.autonomous4j.listeners.A4jNavDataListener;
import org.autonomous4j.listeners.A4jReadyStateChangeListener;
import org.autonomous4j.listeners.A4jVideoDataListener;
import org.autonomous4j.tracking.A4jFlightRecorder;
import org.autonomous4j.tracking.A4jFlightRecorder.Movement;

/**
 *
 * @author Mark Heckler (mark.heckler@gmail.com, @mkheck)
 */
public class A4jBrain {
    private static final A4jBrain brain = new A4jBrain();
    private DroneController controller;
    private Config cfg;
    //private NavData currentNav;
    private final A4jFlightRecorder recorder;
    private boolean isRecording;

    private A4jBrain() {
        cfg = new Config("Autonomous4j Test", "My Profile", 0);
        this.recorder = new A4jFlightRecorder();
        isRecording = true;
    }

    public static A4jBrain getInstance() {
        return brain;
    }

    public boolean connect() {
        return connect("192.168.1.1");
    }

    public boolean connect(String ipAddress) {
        try {
            controller = ParrotDroneController.build();
            //cfg = new Config("Autonomous4j Test", "My Profile", 0);
            controller.start(cfg);

            //controller.addVideoDataListener(new A4jVideoDataListener());
            controller.addNavDataListener(new A4jNavDataListener());    
            controller.addReadyStateChangeListener(new A4jReadyStateChangeListener());
            controller.addErrorListener(new A4jErrorListener());
        } catch (Exception ex) {
            System.err.println("Exception creating new drone connection: " + ex.getMessage());
            return false;
        }
        return true;
    }

    public void disconnect() {
        if (controller != null) {
            controller.stop();
        }
        recorder.shutdown();
    }

    /**
     * Convenience (pass-through) method for more fluent API.
     * @param ms Long variable specifying a number of milliseconds.
     * @return A4jBrain object (allows command chaining/fluency.
     * @see #hold(long)
     */
    public A4jBrain doFor(long ms) {
        return hold(ms);
    }
    
    public A4jBrain hold(long ms) {
        System.out.println("Hold for " + ms + " milliseconds...");
        try {
            Thread.sleep(ms);
            if (isRecording) {
                recorder.recordDuration(ms);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Logger.getLogger(A4jBrain.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return this;
    }

    public A4jBrain hover() {
        System.out.println("--Hover--");
        controller.move(0, 0, 0, 0);
        if (isRecording) {
            recorder.recordAction(A4jFlightRecorder.Action.HOVER);
        }
        
        return this;
    }

    public void land() {
        System.out.println("Land.");
        controller.land();
        if (isRecording) {
            recorder.recordAction(A4jFlightRecorder.Action.LAND);
        }
    }

    public A4jBrain takeoff() {
        System.out.println("Takeoff!");
        controller.takeOff();
        if (isRecording) {
            recorder.recordAction(A4jFlightRecorder.Action.TAKEOFF);
        }
        
        return this;
    }

    public A4jBrain forward(int speed) {
        System.out.println("Forward @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jFlightRecorder.Action.FORWARD, speed);
        }
        
        return move(0f, -perc2float(speed), 0f, 0f);
    }

    public A4jBrain backward(int speed) {
        System.out.println("Backward @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jFlightRecorder.Action.BACKWARD, speed);
        }

        return move(0f, perc2float(speed), 0f, 0f);
    }

//  MAH: will need to revisit this to accommodate either 90 degree turns ONLY or odd angles.
//    public A4jBrain spinRight(int speed) {
//        System.out.println("spinRight @" + speed);
//        //recorder.recordAction(A4jFlightRecorder.Action.FORWARD);
//        
//        return move(0f, 0f, 0f, perc2float(speed));
//    }
//
//    public A4jBrain spinLeft(int speed) {
//        System.out.println("spinLeft @" + speed);
//        return move(0f, 0f, 0f, -perc2float(speed));
//    }

    public A4jBrain up(int speed) {
        System.out.println("up @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jFlightRecorder.Action.UP, speed);
        }

        return move(0f, 0f, perc2float(speed), 0f);
    }

    public A4jBrain down(int speed) {
        System.out.println("down @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jFlightRecorder.Action.DOWN, speed);
        }

        return move(0f, 0f, -perc2float(speed), 0f);
    }

    public A4jBrain goRight(int speed) {
        System.out.println("goRight @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jFlightRecorder.Action.RIGHT, speed);
        }

        return move(perc2float(speed), 0f, 0f, 0f);
    }

    public A4jBrain goLeft(int speed) {
        System.out.println("goLeft @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jFlightRecorder.Action.LEFT, speed);
        }

        return move(-perc2float(speed), 0f, 0f, 0f);
    }

    public A4jBrain playLedAnimation(float frequency, int durationSeconds) {
        // "Default" LED animation sequence is blank for now
        playLedAnimation(LedAnimation.BLANK, frequency, durationSeconds);
        return this;
    }
    
    public A4jBrain playLedAnimation(LedAnimation animation, float frequency, int durationSeconds) {
        if (isRecording) {
            recorder.recordAction(A4jFlightRecorder.Action.LIGHTS, (int) frequency);
        }
        controller.executeCommandsAsync(
                new PlayLedAnimationCommand(cfg.getLoginData(), 
                        animation, frequency, durationSeconds));
//        controller.executeCommands(
//                new PlayLedAnimationCommand(cfg.getLoginData(),
//                        animation, frequency, durationSeconds));
        hold(durationSeconds * 1000);
        System.out.println("Blinking " + animation.name() + ", Frequency: " + 
            frequency + " for " + durationSeconds + " seconds.");
        return this;
    }
    
    public A4jBrain goHome() {
        processRecordedMovements(recorder.home());
        return this;
    }
    
    public A4jBrain replay() {
        processRecordedMovements(recorder.getRecording());
        return this;
    }
    
    private void processRecordedMovements(List<Movement> moves) {
        // Disable recording for playback
        isRecording = false;

        for (Movement curMov : moves) {
            switch(curMov.getAction()) {
                case FORWARD:
                    forward(curMov.getSpeed());
                    break;
                case BACKWARD:
                    backward(curMov.getSpeed());
                    break;
                case RIGHT:
                    goRight(curMov.getSpeed());
                    break;
                case LEFT:
                    goLeft(curMov.getSpeed());
                    break;
                case UP:
                    up(curMov.getSpeed());
                    break;
                case DOWN:
                    down(curMov.getSpeed());
                    break;
                case HOVER:
                    hover();
                    break;
                case TAKEOFF:
                    takeoff();
                    break;
                case LAND:
                    land();
                    break;
                case LIGHTS:
                    playLedAnimation(curMov.getSpeed(), (int) curMov.getDuration()/1000);
                    break;
            }
            hold(curMov.getDuration());
            System.out.println(curMov);
        }
            
        // Re-enable recording
        isRecording = true;    
    }
    
    private float limit(float f, float min, float max) {
        return (f > max ? max : (f < min ? min : f));
    }

    public A4jBrain move(float roll ,float pitch, float gaz, float yaw) {
        roll = limit(roll, -1f, 1f);
        pitch = limit(pitch, -1f, 1f);
        gaz = limit(gaz, -1f, 1f);
        yaw = limit(yaw, -1f, 1f);
        
        controller.move(roll, pitch, yaw, gaz);
        
        return this;
    }

    private float perc2float(int speed) {
        return (float) (speed / 100.0f);
    }
}
