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
package org.autonomous4j.physical;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 * A4jLandController orchestrates communication between the A4jBrainL class, 
 * which operates ALVIN, and the underlying microcontroller.
 * 
 * @author Mark Heckler (mark.heckler@gmail.com, @mkheck)
 */
public class A4jLandController extends Observable {
    private boolean isConnected;
    private String readBuffer = "";
    private String curCmd = "";
    private List<String> commands = new ArrayList<>();
    private final A4jSerial serial = new A4jSerial();
    private CompletableFuture<String> future;
    private CompletableFuture<String> response;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    Properties applicationProps = new Properties();
    
//    private ReadingPublisher dataMQTT;
    
    public boolean connect() throws Exception {
        // Initialize the log (PrintStream with autoflush)
        // ALWAYS start the logging FIRST!
        // Load application properties
        loadProperties();

        // Log detected ports
        A4jSerial.listPorts();

        String portName = getProperty("serialPort");
        if (portName.isEmpty()) {
            // Get out of here!
            logIt("Exception: Property 'serialPort' missing from A4jBrain.properties file.");
            Exception e = new Exception("Exception: Property 'serialPort' missing from A4jBrain.properties file.");
            throw e;
        } else {
            try {
                logIt("Connecting to serial port " + portName);
                executor.submit(new SerialThread(portName));
                //isConnected = true; This is set in the serial.connect() method for realz...
            } catch (Exception e) {
                logIt("Exception: Connection to serial port " + portName + " failed: "
                        + e.getMessage());
                isConnected = false;
            }
        }

//        String uriDataMQTT = getProperty("uriDataMQTT");
//        if (uriWebSocket.isEmpty()) {
//            // Get out of here!
//            logIt("ERROR: Property 'uriDataMQTT' missing from PiRemote.properties file.");
//            Exception e = new Exception("ERROR: Property 'uriDataMQTT' missing from PiRemote.properties file.");
//            throw e;
//        } else {
//            dataMQTT = new ReadingPublisher(uriDataMQTT, sensorID, "data");
//            serial.addPublisher(dataMQTT);
//        }

        return isConnected;
    }

    public boolean disconnect() {
//        logIt("Stopping websockets");
//        //dataMQTT.
        
        if (this.countObservers() > 0) {
            logIt("Disconnecting observers");
            this.deleteObservers();
        }

        if (isConnected) {
            logIt("Closing serial port");
            isConnected = serial.disconnect();
            executor.shutdownNow();
        }
        
        return isConnected;
    }

    public static void logIt(String reading) {
        System.out.println(reading);
    }

    /*
     Configuration file methods - begin
     */
    private void loadProperties() {
        FileInputStream in = null;
        File propFile = new File("A4jBrain.properties");

        if (!propFile.exists()) {
            // If it doesn't exist, create it.
            try {
                propFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            in = new FileInputStream(propFile);
            applicationProps.load(in);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getProperty(String propKey) {
        if (applicationProps.containsKey(propKey)) {
            return applicationProps.getProperty(propKey, "");
        } else {
            logIt("ERROR: Property not found: '" + propKey + "'.");
        }
        return "";
    }

    private CompletableFuture<String> writeToSerial(String command) {
        future = new CompletableFuture<>();
        // Place command into the queue so the Serial thread can pick it up
        curCmd = command + "\r\n";
        return future;
    }

    @Override
    public void notifyObservers(Object arg) {
        super.notifyObservers(arg);
    }

    public void forward(long distance) {
        commands.add(DroneCommand.FORWARD.getDescription() + " " + distance + " cm.");
        response = writeToSerial(DroneCommand.FORWARD.getCommand() + distance);
        try {
            response.get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(A4jLandController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void back(long distance) {
        commands.add(DroneCommand.BACK.getDescription() + " " + distance + " cm.");
        response = writeToSerial(DroneCommand.BACK.getCommand() + distance);
        try {
            response.get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(A4jLandController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void left(long degrees) {
        commands.add(DroneCommand.LEFT.getDescription() + " " + degrees + " degrees.");
        response = writeToSerial(DroneCommand.LEFT.getCommand() + degrees);
        try {
            response.get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(A4jLandController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void right(long degrees) {
        commands.add(DroneCommand.RIGHT.getDescription() + " " + degrees + " degrees.");
        response = writeToSerial(DroneCommand.RIGHT.getCommand() + degrees);
        try {
            response.get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(A4jLandController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stop() {
        commands.add(DroneCommand.STOP.getDescription());
        response = writeToSerial(DroneCommand.STOP.getCommand());
        try {
            response.get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(A4jLandController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public long pingForward() {
        commands.add(DroneCommand.PINGF.getDescription());
        response = writeToSerial(DroneCommand.PINGF.getCommand());
        return pingDistance();
    }
    
    public long pingLeft() {
        commands.add(DroneCommand.PINGL.getDescription());
        response = writeToSerial(DroneCommand.PINGL.getCommand());
        return pingDistance();
    }
    
    public long pingRight() {
        commands.add(DroneCommand.PINGR.getDescription());
        response = writeToSerial(DroneCommand.PINGR.getCommand());
        return pingDistance();
    }
    
    private long pingDistance() {
        int pos = 0;
        long distance = 0;
        String distStr;
        try {
            distStr = response.get();
            if ((pos = distStr.indexOf(" ")) > -1) {
                distStr = distStr.substring(0, pos);
            }
            
            distance = Long.parseLong(distStr);
        } catch (InterruptedException | ExecutionException | NumberFormatException ex) {
            Logger.getLogger(A4jLandController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return distance;
    }
    
    private class SerialThread implements Runnable, SerialPortEventListener {
        //String cmdOnTheWire = "";
        
        public SerialThread(String portName) {
            System.out.println("Creating SerialThread...");
            try {
                isConnected = serial.connect(portName, this);
            } catch (Exception ex) {
                Logger.getLogger(A4jLandController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        @Override
        public void serialEvent(SerialPortEvent event) {
            int pos;
            
            if (event.isRXCHAR() && event.getEventValue() > 0) { // Data is available
                try {
                    // Read all available data from serial port and add to buffer
                    readBuffer += serial.getSerialPort().readString(event.getEventValue());
                    if (readBuffer.contains("\n")) {
                        String[] lines = readBuffer.split(">");
                        for (String line: lines) {
                            if ((pos = line.indexOf(":")) > -1) {
                                System.out.println("COMMAND ECHO RECEIVED: " + line);

                                // Provide the number value accompanying the command, 
                                // removing the newline if present.
                                if (line.length() > pos + 1) {
                                    future.complete(line.substring(pos + 1, 
                                            line.endsWith("\n") ? line.length() - 2 : line.length() - 1));                                    
                                } else {    // Nothing after the : (most commands)
                                    future.complete("");
                                }
                                setChanged();
                                
                                // May need to match command & return, esp if 
                                // this changes to async treatment.
                                if (!commands.isEmpty()) {
                                    notifyObservers(commands.remove(0));
                                }
                            } else {
                                // Reading feedback from microcontroller
                                System.out.println("Direct passthrough: " + line);
                            }
                        }
                        if (lines[lines.length - 1].endsWith("\n")) {
                            readBuffer = "";
                        } else {
                            // Partial transmission in last parsed bucket; append to it.
                            readBuffer = lines[lines.length - 1];
                        }
                    }
                } catch (SerialPortException ex) {
                    logIt("Exception reading serial port: " + ex.getLocalizedMessage());
                }
            } else if (event.isCTS()) {     // CTS line has changed state
                if (event.getEventValue() == 1) { // Line is ON
                    logIt("CTS ON");
                } else {
                    logIt("CTS OFF");
                }
            } else if (event.isDSR()) {     // DSR line has changed state
                if (event.getEventValue() == 1) { // Line is ON
                    logIt("DSR ON");
                } else {
                    logIt("DSR OFF");
                }
            }
        }        

        @Override
        public void run() {
            while (isConnected) {
                if (!curCmd.isEmpty()) {
                    try {
                        serial.getSerialPort().writeString(curCmd);
                        //cmdOnTheWire = curCmd;  // Save entire command for publishing via MQTT
                        curCmd = "";
                    } catch (SerialPortException ex) {
                        Logger.getLogger(A4jLandController.class.getName()).log(Level.SEVERE, null, ex);
                        logIt("Exception writing to serial port: " + ex.getLocalizedMessage());
                        future.complete(curCmd);
                    }                
                }
            }
        }
    }
}
