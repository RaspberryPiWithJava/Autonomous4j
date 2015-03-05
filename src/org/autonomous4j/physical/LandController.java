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
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;

/**
 *
 * @author Mark Heckler (mark.heckler@gmail.com, @mkheck)
 */
public class LandController extends Observable implements SerialPortEventListener {
    private Serial serial = null;
    private boolean connected;
    private String readBuffer = "";

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
        Serial.listPorts();

        String portName = getProperty("serialPort");
        if (portName.isEmpty()) {
            // Get out of here!
            logIt("Exception: Property 'serialPort' missing from A4jBrain.properties file.");
            Exception e = new Exception("Exception: Property 'serialPort' missing from A4jBrain.properties file.");
            throw e;
        } else {
            serial = new Serial();
            try {
                logIt("Connecting to serial port " + portName);
                serial.connect(portName, this);
                connected = serial.isConnected();
            } catch (Exception e) {
                logIt("Exception: Connection to serial port " + portName + " failed: "
                        + e.getMessage());
                connected = false;
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
            
        return connected;
    }

    public boolean disconnect() {
//        logIt("Stopping websockets");
//        //dataMQTT.
        
        if (this.countObservers() > 0) {
            logIt("Disconnecting observers");
            this.deleteObservers();
        }
        
        logIt("Closing serial port");
        return serial.disconnect();
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

    private void writeToSerial(String command) {
        try {
            serial.getSerialPort().writeString(command + "\r\n");
        } catch (SerialPortException ex) {
            Logger.getLogger(LandController.class.getName()).log(Level.SEVERE, null, ex);
            logIt("Exception writing to serial port: " + ex.getLocalizedMessage());
        }
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.isRXCHAR() && event.getEventValue() > 0) { // Data is available
            try {
                // Read all available data from serial port and add to buffer
                readBuffer += serial.getSerialPort().readString(event.getEventValue());
                if (readBuffer.contains("\n")) {
//                        .endsWith("\n")) { // Check for end of buffer string
                    String[] lines = readBuffer.split(">");
                    for (String line: lines) {
                        if (line.contains(":")) {
                            this.setChanged();
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
    public void notifyObservers(Object arg) {
        super.notifyObservers(arg); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void forward() {
        writeToSerial(FORWARD);
    }

    public void back() {
        writeToSerial(BACK);
    }

    public void left() {
        writeToSerial(LEFT);
    }

    public void right() {
        writeToSerial(RIGHT);
    }

    public void stop() {
        writeToSerial(STOP);
    }
}
