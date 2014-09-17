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
import org.autonomous4j.interfaces.A4jPublisher;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * @author Mark Heckler (mark.heckler@gmail.com, @mkheck)
 */
public class A4jFlightRecorder implements A4jPublisher {
    private final static String TOP_LEVEL_TOPIC = "a4jflight";
    public enum Action {FORWARD, BACKWARD, LEFT, RIGHT, UP, DOWN, 
        HOVER, TAKEOFF, LAND, LIGHTS};
    private float xDelta = 0;
    private float yDelta = 0;
    private float zDelta = 0;
    private final static int DEFAULT_SPEED = 20;
    private final List<Movement> recording = new ArrayList<>();
    private static PrintStream flightInProgress = null;
    private MqttClient client;
    private final MqttMessage msg;

    public A4jFlightRecorder() {
        flightInProgress = openLog("InProgress.afr");
        msg = new MqttMessage();
        
        try {
            client = new MqttClient("tcp://localhost:1883", "a4jflightrecorder");
            client.connect();            
        } catch (MqttException ex) {
            Logger.getLogger(A4jFlightRecorder.class.getName()).log(Level.SEVERE, null, ex);
        }
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
            try {
                flightInProgress.close();
                client.disconnect();
            } catch (MqttException ex) {
                Logger.getLogger(A4jFlightRecorder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        try (PrintStream flightComplete = openLog("LastFlight.afr")) {
            recording.stream().forEach((curMov) -> {
                flightComplete.println(curMov.getFlightRecordEntry());
            });
            flightComplete.close();
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
        publish();
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
            // No measured adjustments for takeoff, hover, land, or lights.
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
    
//    public static void recordMovement(String reading) {
//        if (flightInProgress != null) {
//            flightInProgress.println(reading);
//        } else {
//            System.out.println(reading);
//        }
//    }

    @Override
    public String getTopLevelTopic() {
        return TOP_LEVEL_TOPIC;
    }

    @Override
    public void publish() {
        try {
            msg.setPayload((recording.get(recording.size()-1).getActionString() 
                    + "," + recording.get(recording.size()-1).getSpeed()).getBytes());
            client.publish(TOP_LEVEL_TOPIC + "/movement", msg);
        } catch (MqttException ex) {
            Logger.getLogger(A4jFlightRecorder.class.getName()).log(Level.SEVERE, null, ex);
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
            } else if (action == Action.LIGHTS) {
                return "LIGHTS";
            } else {
                return "UNKNOWN";
            }
        }
    }
}
