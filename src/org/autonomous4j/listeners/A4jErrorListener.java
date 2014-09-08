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

import com.dronecontrol.droneapi.listeners.ErrorListener;
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
public class A4jErrorListener implements A4jPublisher, ErrorListener {
    private final static String TOP_LEVEL_TOPIC = "a4jerrordata";
    private MqttClient client;
    private final MqttMessage msg;
    private String errorMsg;

    public A4jErrorListener() {
        msg = new MqttMessage();
        
        try {
            client = new MqttClient("tcp://localhost:1883", "a4jerrorlistener");
            client.connect();            
        } catch (MqttException ex) {
            Logger.getLogger(A4jErrorListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getTopLevelTopic() {
        return TOP_LEVEL_TOPIC;
    }

    @Override
    public void publish() {
        try {
            msg.setPayload(errorMsg.getBytes());
            client.publish(TOP_LEVEL_TOPIC + "/error", msg);
        } catch (MqttException ex) {
            Logger.getLogger(A4jErrorListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onError(Throwable thrwbl) {
        errorMsg = thrwbl.getLocalizedMessage();
        publish();
    }
}
