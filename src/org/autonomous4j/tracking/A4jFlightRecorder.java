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
package org.autonomous4j.tracking;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mark Heckler (mark.heckler@gmail.com, @mkheck)
 */
public class A4jFlightRecorder {
    public enum Action {FORWARD, BACKWARD, LEFT, RIGHT, UP, DOWN, 
        HOVER, TAKEOFF, LAND};
    private float xDelta = 0;
    private float yDelta = 0;
    private float zDelta = 0;
    private final static int DEFAULT_SPEED = 20;
    private final List<Movement> recording = new ArrayList<>();
    private static PrintStream flightInProgress = null;

    public A4jFlightRecorder() {
        flightInProgress = openLog("InProgress.afr");
    }

    public PrintStream openLog(String fileName) {
        PrintStream logFile = null;
        
        try {
            logFile = new PrintStream(new FileOutputStream(new File(fileName)), true);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(A4jFlightRecorder.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return logFile;
    }
    
    public void shutdown() {
        if (flightInProgress != null) {
            flightInProgress.close();            
        }
        
        try (PrintStream flightComplete = openLog("LastFlight.afr")) {
            recording.stream().forEach((curMov) -> {
                flightComplete.println(curMov.getFlightRecordEntry());
            });
        }
    }
    
    public void recordAction(Action action) {
        // Default to reasonable speed
        recordAction(action, DEFAULT_SPEED);
    }
    
    public void recordAction(Action action, int speed) {
        Movement curMov = new Movement(action, speed, 0);
        recording.add(curMov);
        flightInProgress.println(curMov.getFlightRecordEntry());
    }
    
    public void recordDuration(long duration) {
        // Update the last recorded movement's duration
        recording.get(recording.size()-1).setDuration(duration);
        
        // Track the movement delta for this flight
        switch (recording.get(recording.size()-1).getAction()) {
            case FORWARD:
                xDelta += duration;
                break;
            case BACKWARD:
                xDelta -= duration;
                break;
            case RIGHT:
                yDelta += duration;
                break;
            case LEFT:
                yDelta -= duration;
                break;
            case UP:
                zDelta += duration;
                break;
            case DOWN:
                zDelta -= duration;
                break;
            //default:
            // No measured adjustments for takeoff, hover, or land.
        }
    }

    public List<Movement> getRecording() {
        return recording;
    }
    
    public List<Movement> home() {
        // MAH: need to compare speeds of all steps. If same, can use deltas to 
        // calculate a shortcut; otherwise, just return steps for retracing.
        return recording;
    }
    
    public static void recordMovement(String reading) {
        if (flightInProgress != null) {
            flightInProgress.println(reading);
        } else {
            System.out.println(reading);
        }
    }

    public class Movement {
        Action action;
        int speed = 0;
        long duration = 0;

        public Movement(Action action, int speed, long duration) {
            this.action = action;
            this.speed = speed;
            this.duration = duration;
        }
        
        public Action getAction() {
            return action;
        }

        public void setAction(Action action) {
            this.action = action;
        }

        public void setAction(Action action, int speed) {
            this.speed = speed;
            setAction(action);
        }
        
        public int getSpeed() {
            return speed;
        }
        
        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
            this.duration = duration;
        }
        
        public String getFlightRecordEntry() {
            return "{" + getActionString() + "," 
                    + speed + "," 
                    + duration + "}";
        }
        
        @Override
        public String toString() {
            return "Movement\tAction(" + getActionString() 
                    + ")\tSpeed(" + speed + ")\tDuration(" + duration + ")";
        }
        
        public String getActionString() {
            if (action == Action.FORWARD) {
                return "FORWARD";
            } else if (action == Action.BACKWARD) {
                return "BACKWARD";
            } else if (action == Action.RIGHT) {
                return "RIGHT";
            } else if (action == Action.LEFT) {
                return "LEFT";
            } else if (action == Action.UP) {
                return "UP";
            } else if (action == Action.DOWN) {
                return "DOWN";
            } else if (action == Action.HOVER) {
                return "HOVER";
            } else if (action == Action.TAKEOFF) {
                return "TAKEOFF";
            } else if (action == Action.LAND) {
                return "LAND";
            } else {
                return "UNKNOWN";
            }
        }
    }
}
