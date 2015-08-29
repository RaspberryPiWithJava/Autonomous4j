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
package org.autonomous4j.listeners.xyz;

import com.dronecontrol.droneapi.data.NavData;
import com.dronecontrol.droneapi.data.VisionTagData;
import com.dronecontrol.droneapi.listeners.NavDataListener;
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
public class A4jNavDataListener implements A4jPublisher, NavDataListener {
    private final static String TOP_LEVEL_TOPIC = "a4jnavdata";
    private final static int NEW_DATA_DECIMAL_PLACES = 3;
    private final NavData nd;
    private MqttClient client;
    private final MqttMessage msg;
    private boolean isNewAltitude;
    private boolean isNewBatteryLevel;
    private boolean isNewPitch;
    private boolean isNewRoll;
    private boolean isNewYaw;
    private boolean isNewSpeedX;
    private boolean isNewSpeedY;
    private boolean isNewSpeedZ;
    private boolean isNewVisionData;

    public A4jNavDataListener() {
        nd = new NavData();
        msg = new MqttMessage();
        
        try {
            client = new MqttClient("tcp://localhost:1883", "a4jnavdatalistener");
            client.connect();            
        } catch (MqttException ex) {
            Logger.getLogger(A4jNavDataListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void onNavData(NavData nd) {
        if (isNewData(nd)) {
            update(nd);
            publish();
        } else {
            System.out.println("No change in NavData from listener.");
        }
    }    

    private boolean isNewData(NavData nd) {
        long shift = (long) Math.pow(10, NEW_DATA_DECIMAL_PLACES);
        
        isNewAltitude = this.nd.getAltitude() != nd.getAltitude();
        isNewBatteryLevel = this.nd.getBatteryLevel() != nd.getBatteryLevel();
        
        // Compare the specified number of decimal places.
        // This allows us to adjust the flow of data.
        isNewPitch = 
                (float)Math.round(this.nd.getPitch()*shift)/shift != (float)Math.round(nd.getPitch()*shift)/shift;
        isNewRoll = 
                (float)Math.round(this.nd.getRoll()*shift)/shift != (float)Math.round(nd.getRoll()*shift)/shift;
        isNewYaw = 
                (float)Math.round(this.nd.getYaw()*shift)/shift != (float)Math.round(nd.getYaw()*shift)/shift;
        isNewSpeedX = 
                (float)Math.round(this.nd.getSpeedX()*shift)/shift != (float)Math.round(nd.getSpeedX()*shift)/shift;
        isNewSpeedY = 
                (float)Math.round(this.nd.getSpeedY()*shift)/shift != (float)Math.round(nd.getSpeedY()*shift)/shift;
        isNewSpeedZ = 
                (float)Math.round(this.nd.getSpeedZ()*shift)/shift != (float)Math.round(nd.getSpeedZ()*shift)/shift;
        
        //isNewVisionData = this.nd.getVisionData().getTags() != nd.getVisionData().getTags();
        isNewVisionData = true;
        
        return isNewAltitude || isNewBatteryLevel || isNewPitch || isNewRoll ||
                isNewYaw || isNewSpeedX || isNewSpeedY || isNewSpeedZ || 
                isNewVisionData;
    }
    
    private void update(NavData nd) {
        //  this.nd.setOnlyHeaderPresent(nd.isOnlyHeaderPresent());
        this.nd.setSequenceNumber(nd.getSequenceNumber());
        this.nd.setState(nd.getState());
        this.nd.setAltitude(nd.getAltitude());
        this.nd.setBatteryLevel(nd.getBatteryLevel());
        this.nd.setPitch(nd.getPitch());
        this.nd.setRoll(nd.getRoll());
        this.nd.setYaw(nd.getYaw());
        this.nd.setSpeedX(nd.getSpeedX());
        this.nd.setSpeedY(nd.getSpeedY());
        this.nd.setSpeedZ(nd.getSpeedZ());
        this.nd.setVisionData(nd.getVisionData());
    }

    @Override
    public void publish() {
        try {
            if (isNewAltitude) {
                msg.setPayload(String.valueOf(this.nd.getAltitude()).getBytes());
                client.publish(TOP_LEVEL_TOPIC + "/altitude", msg);
            }
            if (isNewBatteryLevel) {
                msg.setPayload(String.valueOf(this.nd.getBatteryLevel()).getBytes());
                client.publish(TOP_LEVEL_TOPIC + "/battery", msg);
            }
            if (isNewPitch) {
                msg.setPayload(String.valueOf(this.nd.getPitch()).getBytes());
                client.publish(TOP_LEVEL_TOPIC + "/pitch", msg);
            }
            if (isNewRoll) {
                msg.setPayload(String.valueOf(this.nd.getRoll()).getBytes());
                client.publish(TOP_LEVEL_TOPIC + "/roll", msg);
            }
            if (isNewSpeedX) {
                msg.setPayload(String.valueOf(this.nd.getSpeedX()).getBytes());
                client.publish(TOP_LEVEL_TOPIC + "/speedx", msg);
            }
            if (isNewSpeedY) {
                msg.setPayload(String.valueOf(this.nd.getSpeedY()).getBytes());
                client.publish(TOP_LEVEL_TOPIC + "/speedy", msg);
            }
            if (isNewSpeedZ) {
                msg.setPayload(String.valueOf(this.nd.getSpeedZ()).getBytes());
                client.publish(TOP_LEVEL_TOPIC + "/speedz", msg);
            }
            if (isNewYaw) {
                msg.setPayload(String.valueOf(this.nd.getYaw()).getBytes());
                client.publish(TOP_LEVEL_TOPIC + "/yaw", msg);
            }
            if (isNewVisionData) {
//                msg.setPayload(String.valueOf(this.nd.getVisionData()).getBytes());
//                client.publish(TOP_LEVEL_TOPIC + "/visiondata", msg);

                if (!nd.getVisionData().getTags().isEmpty()) {
                    String vInfo;
                    for (VisionTagData vtd : nd.getVisionData().getTags()) {
                        vInfo = "Distance (" + vtd.getDistance() + 
                                ") Height (" + vtd.getHeight() + 
                                ") Width (" + vtd.getWidth() + 
                                ") X (" + vtd.getX() + 
                                ") Y (" + vtd.getY() +
                                ") Orientation Angle (" + vtd.getOrientationAngle() + ")";
                        msg.setPayload(vInfo.getBytes());
                        client.publish(TOP_LEVEL_TOPIC + "/visiondata", msg);
                    }
                }
            }
        } catch (MqttException ex) {
            Logger.getLogger(A4jNavDataListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getTopLevelTopic() {
        return TOP_LEVEL_TOPIC;
    }
}
