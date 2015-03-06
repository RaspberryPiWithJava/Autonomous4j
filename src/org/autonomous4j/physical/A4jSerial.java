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

//import gnu.io.CommPort;
//import gnu.io.CommPortIdentifier;
//import gnu.io.SerialPort;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;
import static org.autonomous4j.physical.A4jLandController.logIt;
//import org.thehecklers.piremote.model.LastReading;
//import org.thehecklers.piremote.model.Reading;
//import org.autonomous4j.mqtt.MQTTPublisher;
/**
 * Serial connects with JSSC library to specified serial port starting two
 * threads to read and write.
 *
 * SerialReader thread reads a line from the serial port, parses it, and 
 * stores it as ???.
 *
 * On undeploy, the serial port is closed.
 * 
 * @author Mark Heckler (mark.heckler@gmail.com, @mkheck)
 */

/**
 * A4jSerial connects with JSSC library to specified serial port starting two
 threads to read and write.SerialReader thread reads a line from the serial port, parses it, and 
 stores it as ???.

 On undeploy, the serial port is closed.
 * @author Mark Heckler (mark.heckler@gmail.com, @mkheck)
 */
public class A4jSerial {    
//    private CommPort commPort;
    private SerialPort serialPort;

    private boolean isConnected = false;
    
//    private OutputStream out;

//    private List<MQTTPublisher> publishers = new ArrayList<>();

    public boolean connect(String portName, SerialPortEventListener listener) throws Exception {
        isConnected = connect(portName);
        serialPort.addEventListener(listener);
        
        return isConnected;
    }
    
    public boolean connect(String portName) throws Exception {
        serialPort = new SerialPort(portName);
        try{
            if (serialPort.openPort()) {
                logIt("Port '" + portName + "' open.");
                serialPort.setParams(SerialPort.BAUDRATE_9600, 
                        SerialPort.DATABITS_8, 
                        SerialPort.STOPBITS_1, 
                        SerialPort.PARITY_NONE);

                /*
                This sleep(2000) is minimum required to allow for Arduino to 
                retrieve values from the serial port. No touchy!  ;)
                */
                sleep(2000);
                isConnected = true;
            }            
        } catch (SerialPortException | InterruptedException e) {
            System.out.println("Exception trying to open port: " + e.getLocalizedMessage());
        }
        
        return isConnected;
    }

    public void addEventListener(SerialPortEventListener listener) throws Exception {
        serialPort.addEventListener(listener);
    }
    
    public boolean isConnected() {
        return isConnected;
    }

    public boolean disconnect(){
        if (serialPort != null) {
            try {
                try {
                    serialPort.removeEventListener();
                } catch (Exception ex) {
                    // Absorb it. Replace visibility/accessibility to underlying
                    // serialPort (directly) and handle properly. MAH
                }
                serialPort.closePort();
                isConnected = false;
            } catch (SerialPortException ex) {
                Logger.getLogger(A4jSerial.class.getName()).log(Level.SEVERE, null, ex);
            }
            logIt("Disconnecting: serial port closed.");
        }
        
        return !isConnected;
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }

//    public OutputStream getOutputStream(){
//        return out;
//    }

//    public void addPublisher(MQTTPublisher publisher) {
//        publishers.add(publisher);
//    }
//
//    public void removePublisher(MQTTPublisher publisher) {
//        publishers.remove(publisher);
//    }

    public static void listPorts()
    {
        logIt("Ports detected:");
        String[] portNames = SerialPortList.getPortNames();
        for (String portName : portNames) {
            logIt(portName);
        }
    }
}
