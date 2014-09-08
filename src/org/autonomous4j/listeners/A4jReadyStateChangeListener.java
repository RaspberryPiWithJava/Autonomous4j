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
package org.autonomous4j.listeners;

import org.autonomous4j.interfaces.A4jPublisher;
import com.dronecontrol.droneapi.listeners.ReadyStateChangeListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * @author Mark Heckler (mark.heckler@gmail.com, @mkheck)
 */
public class A4jReadyStateChangeListener implements A4jPublisher, ReadyStateChangeListener {
    private final static String TOP_LEVEL_TOPIC = "a4jreadystatedata";
    private ReadyState rs;
    private MqttClient client;
    private final MqttMessage msg;

    public A4jReadyStateChangeListener() {
        this.rs = ReadyState.NOT_READY;
        msg = new MqttMessage();
        
        try {
            client = new MqttClient("tcp://localhost:1883", "a4jreadystatechangelistener");
            client.connect();            
        } catch (MqttException ex) {
            Logger.getLogger(A4jReadyStateChangeListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getTopLevelTopic() {
        return TOP_LEVEL_TOPIC;
    }

    @Override
    public void publish() {
        try {
            msg.setPayload((rs == ReadyState.READY ? "READY" : "NOT READY").getBytes());
            client.publish(TOP_LEVEL_TOPIC + "/state", msg);
        } catch (MqttException ex) {
            Logger.getLogger(A4jReadyStateChangeListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onReadyStateChange(ReadyState rs) {
        if (this.rs != rs) {
            publish();
        }
    }    
}
