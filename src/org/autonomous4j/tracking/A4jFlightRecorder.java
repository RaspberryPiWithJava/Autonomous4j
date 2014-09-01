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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mark
 */


public class A4jFlightRecorder {
    public enum Action {FORWARD, BACKWARD, LEFT, RIGHT, UP, DOWN, 
        HOVER, TAKEOFF, LAND};
    private float xDelta = 0;
    private float yDelta = 0;
    private float zDelta = 0;
    private final static int DEFAULT_SPEED = 20;
//    private final Movement currentMovement = new Movement();
    private final List<Movement> recording = new ArrayList<>();
    
    public void recordAction(Action action) {
        // Default to reasonable speed
        recordAction(action, DEFAULT_SPEED);
    }
    
    public void recordAction(Action action, int speed) {
        recording.add(new Movement(action, speed, 0));
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
        
        @Override
        public String toString() {
            String movement = "Movement\tAction(";
            
            switch (action) {
            case FORWARD:
                movement += "forward";
                break;
            case BACKWARD:
                movement += "backward";
                break;
            case RIGHT:
                movement += "right";
                break;
            case LEFT:
                movement += "left";
                break;
            case UP:
                movement += "up";
                break;
            case DOWN:
                movement += "down";
                break;
            case HOVER:
                movement += "hover";
                break;
            case TAKEOFF:
                movement += "takeoff";
                break;
            case LAND:
                movement += "land";
                break;
            }
            movement += ")\tSpeed(" + speed + ")\tDuration(" + duration + ")";
            
            return movement;
        }
    }
}
