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
package org.autonomous4j.physical;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 *
 * @author Mark Heckler (mark.heckler@gmail.com, @mkheck)
 */
public class A4jLandController extends Observable {
    private boolean isConnected;
    private String readBuffer = "";
    private String curCmd = "";
    private CompletableFuture<String> future;
    private CompletableFuture<String> response;
    private final Executor executor = Executors.newSingleThreadExecutor();

    Properties applicationProps = new Properties();
    
    private static final String FORWARD = "F:";
    private static final String BACK = "B:";
    private static final String LEFT = "L:";
    private static final String RIGHT = "R:";
    private static final String STOP = "S:";

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
                executor.execute(new SerialThread(portName));
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
        /*
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Disconnecting via shutdown hook");
            this.disconnect();
        }));
        */
            
        return isConnected;
    }

    public boolean disconnect() {
//        logIt("Stopping websockets");
//        //dataMQTT.
        
        if (this.countObservers() > 0) {
            logIt("Disconnecting observers");
            this.deleteObservers();
        }
        
        logIt("Closing serial port");
        //return serial.disconnect();
        // Kill the thread.
        return true;//  Fix this (MAH)
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
        super.notifyObservers(arg); //To change body of generated methods, choose Tools | Templates.
    }

    public void forward(long distance) {
        response = writeToSerial(FORWARD + distance);
        try {
            response.get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(A4jLandController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void back(long distance) {
        response = writeToSerial(BACK + distance);
        try {
            response.get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(A4jLandController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void left(long degrees) {
        response = writeToSerial(LEFT + degrees);
        try {
            response.get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(A4jLandController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void right(long degrees) {
        response = writeToSerial(RIGHT + degrees);
        try {
            response.get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(A4jLandController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void stop() {
        response = writeToSerial(STOP);
        try {
            response.get();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(A4jLandController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class SerialThread implements Runnable, SerialPortEventListener {
        A4jSerial serial = new A4jSerial();
        
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
            System.out.println("In serialEvent()");

            if (event.isRXCHAR() && event.getEventValue() > 0) { // Data is available
                System.out.println("We have a character!");
                try {
                    // Read all available data from serial port and add to buffer
                    readBuffer += serial.getSerialPort().readString(event.getEventValue());
                    System.out.println("Buffer=" + readBuffer);
                    if (readBuffer.contains("\n")) {
                        System.out.println("Has a newline...");
                        String[] lines = readBuffer.split(">");
                        for (String line: lines) {
                            if ((pos = line.indexOf(":")) > -1) {
                                System.out.println("COMMAND ECHO RECEIVED: " + line);
                                future.complete(line);
                                System.out.println("LINE RECEIVED: " + line);
                                setChanged();
                                notifyObservers(line);
                            }
                        }
                        if (lines[lines.length - 1].endsWith("\n")) {
                            readBuffer = "";
                        } else {
                            // Partial transmission in last parsed bucket; append to it.
                            readBuffer = lines[lines.length - 1];
                        }

                        // Send new request
                        // ???
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
            System.out.println("In RUN!");
            while (true) {
                if (!curCmd.isEmpty()) {
                    try {
                        System.out.println("Writing " + curCmd);
                        serial.getSerialPort().writeString(curCmd);
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
