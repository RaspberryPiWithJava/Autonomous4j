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

import org.autonomous4j.interfaces.A4jBrain3D;
import com.dronecontrol.droneapi.DroneController;
import com.dronecontrol.droneapi.ParrotDroneController;
import com.dronecontrol.droneapi.commands.composed.PlayLedAnimationCommand;
import com.dronecontrol.droneapi.data.Config;
import com.dronecontrol.droneapi.data.LoginData;
import com.dronecontrol.droneapi.data.enums.LedAnimation;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.autonomous4j.listeners.xyz.A4jErrorListener;
import org.autonomous4j.listeners.xyz.A4jNavDataListener;
import org.autonomous4j.listeners.xyz.A4jReadyStateChangeListener;
import org.autonomous4j.listeners.xyz.A4jVideoDataListener;
import org.autonomous4j.tracking.A4jBlackBox;
import org.autonomous4j.tracking.A4jBlackBox.Movement;

/**
 *
 * @author Mark Heckler (mark.heckler@gmail.com, @mkheck)
 */
public class A4jBrainA implements A4jBrain3D {
    private static final A4jBrainA brain = new A4jBrainA();
    private DroneController controller;
    private Config cfg;
    //private NavData currentNav;
    private final A4jBlackBox recorder;
    private boolean isRecording;

    private A4jBrainA() {
        cfg = new Config("Autonomous4j Test", "My Profile", 0);
        this.recorder = new A4jBlackBox();
        isRecording = true;
    }

    public static A4jBrainA getInstance() {
        return brain;
    }

    @Override
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

    @Override
    public void disconnect() {
        if (controller != null) {
            controller.stop();
        }
        recorder.shutdown();
    }

    /**
     * Convenience (pass-through) method for more fluent API.
     * @param ms Long variable specifying a number of milliseconds.
     * @return A4jBrainA object (allows command chaining/fluency.
     * @see #hold(long)
     */
    @Override
    public A4jBrainA doFor(long ms) {
        return hold(ms);
    }
    
    @Override
    public A4jBrainA hold(long ms) {
        System.out.println("Hold for " + ms + " milliseconds...");
        try {
            Thread.sleep(ms);
            if (isRecording) {
                recorder.recordDuration(ms);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Logger.getLogger(A4jBrainA.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return this;
    }
    
    @Override
    public A4jBrainA stay() {
        return hover();
    }

    @Deprecated
    public A4jBrainA hover() {
        System.out.println("--Hover--");
        controller.move(0, 0, 0, 0);
        if (isRecording) {
            recorder.recordAction(A4jBlackBox.Action.STAY);
        }
        
        return this;
    }

    public void land() {
        System.out.println("Land.");
        controller.land();
        if (isRecording) {
            recorder.recordAction(A4jBlackBox.Action.LAND);
        }
    }

    public A4jBrainA takeoff() {
        System.out.println("Takeoff!");
        controller.takeOff();
        if (isRecording) {
            recorder.recordAction(A4jBlackBox.Action.TAKEOFF);
        }
        
        return this;
    }

    @Override
    public A4jBrainA forward() {
        return forward(100);
    }

    public A4jBrainA forward(int speed) {
        System.out.println("Forward @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jBlackBox.Action.FORWARD, speed);
        }
        
        return move(0f, -perc2float(speed), 0f, 0f);
    }

    @Override
    public A4jBrainA backward() {
        return backward(100);
    }
    
    public A4jBrainA backward(int speed) {
        System.out.println("Backward @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jBlackBox.Action.BACKWARD, speed);
        }

        return move(0f, perc2float(speed), 0f, 0f);
    }

//  MAH: will need to revisit this to accommodate either 90 degree turns ONLY or odd angles.
//    public A4jBrainA spinRight(int speed) {
//        System.out.println("spinRight @" + speed);
//        //recorder.recordAction(A4jBlackBox.Action.FORWARD);
//        
//        return move(0f, 0f, 0f, perc2float(speed));
//    }
//
//    public A4jBrainA spinLeft(int speed) {
//        System.out.println("spinLeft @" + speed);
//        return move(0f, 0f, 0f, -perc2float(speed));
//    }
    
    @Override
    public A4jBrain3D up() {
        return up(100);
    }

    public A4jBrainA up(int speed) {
        System.out.println("up @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jBlackBox.Action.UP, speed);
        }

        return move(0f, 0f, perc2float(speed), 0f);
    }

    @Override
    public A4jBrain3D down() {
        return down(100);
    }

    public A4jBrainA down(int speed) {
        System.out.println("down @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jBlackBox.Action.DOWN, speed);
        }

        return move(0f, 0f, -perc2float(speed), 0f);
    }

    public A4jBrainA goRight(int speed) {
        System.out.println("goRight @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jBlackBox.Action.RIGHT, speed);
        }

        return move(perc2float(speed), 0f, 0f, 0f);
    }

    public A4jBrainA goLeft(int speed) {
        System.out.println("goLeft @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jBlackBox.Action.LEFT, speed);
        }

        return move(-perc2float(speed), 0f, 0f, 0f);
    }

    public A4jBrainA playLedAnimation(float frequency, int durationSeconds) {
        // "Default" LED animation sequence is blank for now
        playLedAnimation(LedAnimation.BLANK, frequency, durationSeconds);
        return this;
    }
    
    public A4jBrainA playLedAnimation(LedAnimation animation, float frequency, int durationSeconds) {
        if (isRecording) {
            recorder.recordAction(A4jBlackBox.Action.LIGHTS, (int) frequency);
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
    
    @Override
    public A4jBrainA goHome() {
        processRecordedMovements(recorder.home());
        return this;
    }
    
    @Override
    public A4jBrainA replay() {
        processRecordedMovements(recorder.getRecording());
        return this;
    }
    
    @Override
    public A4jBrainA left() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("Turn to the left.");
        return this;
    }

    @Override
    public A4jBrainA right() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("Turn to the right.");
        return this;
    }
    
    @Override
    public void processRecordedMovements(List<Movement> moves) {
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
                case STAY:
                    stay();
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

    public A4jBrainA move(float roll ,float pitch, float gaz, float yaw) {
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
