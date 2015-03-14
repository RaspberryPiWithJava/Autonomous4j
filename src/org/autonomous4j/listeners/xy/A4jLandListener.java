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
package org.autonomous4j.listeners.xy;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.autonomous4j.interfaces.A4jPublisher;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * A4jLandListener is an MQTTClient class.
 * 
 * @author Mark Heckler (mark.heckler@gmail.com, @mkheck)
*/
public class A4jLandListener implements A4jPublisher, Observer {
    private final static String TOP_LEVEL_TOPIC = "a4jlandline";
    private String serverURI;
    private String text;
    private MqttClient client;
    private final MqttConnectOptions connectOptions;
    private final MqttMessage msg;

    public A4jLandListener() {
        this("tcp://localhost:1883");        
    }

    public A4jLandListener(String serverURI) {
        connectOptions = new MqttConnectOptions();
        msg = new MqttMessage();
        this.serverURI = serverURI;
    }

    public A4jLandListener setUserName(String userName) {
        this.connectOptions.setUserName(userName);
        return this;
    }
    
    public A4jLandListener setPassword(String password) {
        this.connectOptions.setPassword(password.toCharArray());
        return this;
    }

    public A4jLandListener connect() {
        try {
            client = new MqttClient(serverURI, TOP_LEVEL_TOPIC + "listener");
            client.connect(connectOptions);
            return this;
        } catch (MqttException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            return this;
        }
    }
    
    public void disconnect() {
        try {
            client.disconnect();
        } catch (MqttException ex) {
            Logger.getLogger(A4jLandListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public String getTopLevelTopic() {
        return TOP_LEVEL_TOPIC;
    }

    @Override
    public void publish() {
        try {
            msg.setPayload(this.text.getBytes());
            client.publish(TOP_LEVEL_TOPIC, msg);
        } catch (MqttException ex) {
            System.out.println("Exception publishing: " + ex.getLocalizedMessage());
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        this.text = (String) arg;
        publish();
    }  
}
