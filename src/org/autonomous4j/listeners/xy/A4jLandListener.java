/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.autonomous4j.listeners.xy;

import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.autonomous4j.interfaces.A4jPublisher;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * @author mark
 */
public class A4jLandListener implements A4jPublisher, Observer {
    private final static String TOP_LEVEL_TOPIC = "a4jlandline";
    private String text;
    private MqttClient client;
    private final MqttMessage msg;

    public A4jLandListener() {
        msg = new MqttMessage();
        
        try {
            client = new MqttClient("tcp://localhost:1883", TOP_LEVEL_TOPIC + "listener");
            client.connect();
        } catch (MqttException ex) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
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
