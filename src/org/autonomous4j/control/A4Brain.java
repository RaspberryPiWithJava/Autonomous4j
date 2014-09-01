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

import com.dronecontrol.droneapi.DroneController;
import com.dronecontrol.droneapi.ParrotDroneController;
import com.dronecontrol.droneapi.data.Config;
import com.dronecontrol.droneapi.data.NavData;
import com.dronecontrol.droneapi.listeners.NavDataListener;
import com.dronecontrol.droneapi.listeners.VideoDataListener;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.autonomous4j.tracking.A4jFlightRecorder;
import org.autonomous4j.tracking.A4jFlightRecorder.Action;
import org.autonomous4j.tracking.A4jFlightRecorder.Movement;

/**
 *
 * @author Mark A. Heckler
 */
public class A4Brain {
    private static final A4Brain brain = new A4Brain();
    private DroneController controller;
    //private NavData currentNav;
    private final A4jFlightRecorder recorder;
    private boolean isRecording;

    private A4Brain() {
        this.recorder = new A4jFlightRecorder();
        isRecording = true;
    }

    public static A4Brain getInstance() {
        return brain;
    }

    public boolean connect() {
        return connect("192.168.1.1");
    }

    public boolean connect(String ipAddress) {
        try {
            controller = ParrotDroneController.build();
            Config cfg = new Config("Autonomous4j Test", "My Profile", 0);
            controller.start(cfg);

            controller.addVideoDataListener(new VideoDataListener() {
                @Override
                public void onVideoData(BufferedImage bi) {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });

            controller.addNavDataListener(new NavDataListener() {
                @Override
                public void onNavData(NavData nd) {
//                    currentNav.
                    System.out.println("NavData received: " + nd.toString());
                    System.out.println("NavData VisionData --> " + nd.getVisionData().getTags().toString());
                    System.out.println("NavData VisionData TS --> " + nd.getVisionData().toString());
                }
            });
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
    }

    /**
     * Convenience (pass-through) method for more fluent API.
     * @param ms Long variable specifying a number of milliseconds.
     * @return A4Brain object (allows command chaining/fluency.
     * @see #hold(long)
     */
    public A4Brain doFor(long ms) {
        return hold(ms);
    }
    
    public A4Brain hold(long ms) {
        System.out.println("Hold for " + ms + " milliseconds...");
        try {
            Thread.sleep(ms);
            if (isRecording) {
                recorder.recordDuration(ms);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Logger.getLogger(A4Brain.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return this;
    }

    public A4Brain hover() {
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

    public A4Brain takeoff() {
        System.out.println("Takeoff!");
        controller.takeOff();
        if (isRecording) {
            recorder.recordAction(A4jFlightRecorder.Action.TAKEOFF);
        }
        
        return this;
    }

    public A4Brain forward(int speed) {
        System.out.println("Forward @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jFlightRecorder.Action.FORWARD, speed);
        }
        
        return move(0f, -perc2float(speed), 0f, 0f);
    }

    public A4Brain backward(int speed) {
        System.out.println("Backward @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jFlightRecorder.Action.BACKWARD, speed);
        }

        return move(0f, perc2float(speed), 0f, 0f);
    }

//  MAH: will need to revisit this to accommodate either 90 degree turns ONLY or odd angles.
//    public A4Brain spinRight(int speed) {
//        System.out.println("spinRight @" + speed);
//        //recorder.recordAction(A4jFlightRecorder.Action.FORWARD);
//        
//        return move(0f, 0f, 0f, perc2float(speed));
//    }
//
//    public A4Brain spinLeft(int speed) {
//        System.out.println("spinLeft @" + speed);
//        return move(0f, 0f, 0f, -perc2float(speed));
//    }

    public A4Brain up(int speed) {
        System.out.println("up @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jFlightRecorder.Action.UP, speed);
        }

        return move(0f, 0f, perc2float(speed), 0f);
    }

    public A4Brain down(int speed) {
        System.out.println("down @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jFlightRecorder.Action.DOWN, speed);
        }

        return move(0f, 0f, -perc2float(speed), 0f);
    }

    public A4Brain goRight(int speed) {
        System.out.println("goRight @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jFlightRecorder.Action.RIGHT, speed);
        }

        return move(perc2float(speed), 0f, 0f, 0f);
    }

    public A4Brain goLeft(int speed) {
        System.out.println("goLeft @" + speed);
        if (isRecording) {
            recorder.recordAction(A4jFlightRecorder.Action.LEFT, speed);
        }

        return move(-perc2float(speed), 0f, 0f, 0f);
    }

    public A4Brain replay() {
        List<Movement> recording = recorder.getRecording();
        
        // Disable recording for playback
        isRecording = false;

        for (Movement curMov : recording) {
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
            }
            hold(curMov.getDuration());
            System.out.println(curMov);
        }
            
        // Re-enable recording
        isRecording = true;
        return this;
    }
    
    private float limit(float f, float min, float max) {
        return (f > max ? max : (f < min ? min : f));
    }

    public A4Brain move(float roll ,float pitch, float gaz, float yaw) {
        roll = limit(roll, -1f, 1f);
        pitch = limit(pitch, -1f, 1f);
        gaz = limit(gaz, -1f, 1f);
        yaw = limit(yaw, -1f, 1f);
        
        controller.move(roll, pitch, gaz, yaw);
        
        return this;
    }

    private float perc2float(int speed) {
        return (float) (speed / 100.0f);
    }
}
