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

package org.autonomous4j.control;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mark Heckler (mark.heckler@gmail.com, @mkheck)
 */
public class A4jMain {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        AttitudeListener attLisn = new AttitudeListener() {
//            @Override
//            public void attitudeUpdated(float pitch, float roll, float yaw) {
//                System.out.println("AL#attitudeUpdated3 Pitch: " + pitch + " Roll: " + roll + " Yaw: " + yaw);
//            }
//
//            @Override
//            public void attitudeUpdated(float pitch, float roll) {
//                    // MAH Good info, but very similar to 3-parm method above.
//                //System.out.println("AL#attitudeUpdated2 Pitch: " + pitch + " Roll: " + roll);
//            }
//
//            @Override
//            public void windCompensation(float pitch, float roll) {
//                    //Example:
//                //AL#windCompensation Pitch: 0.0 Roll: 0.0
//                //
//                //System.out.println("AL#windCompensation Pitch: " + pitch + " Roll: " + roll);
//            }
//        };
//        AltitudeListener atlLisn = new AltitudeListener() {
//            @Override
//            public void receivedAltitude(int i) {
//                System.out.println("LL#receivedAltitude Height: " + i + " mm.");
//            }
//
//            @Override
//            public void receivedExtendedAltitude(Altitude altd) {
//                System.out.println("LL#receivedExtendedAltitude Altitude: " + altd.toString());
//            }
//        };
//        BatteryListener batLisn = new BatteryListener() {
//            @Override
//            public void batteryLevelChanged(int percentage) {
//                System.out.println("BL#batteryLevelChanged Battery: " + percentage + " %");
//            }
//
//            @Override
//            public void voltageChanged(int vbat_raw) {
//                    //Example:
//                //BL#voltageChanged Battery: 12499
//                //
//                //System.out.println("BL#voltageChanged Battery: " + vbat_raw);
//            }
//        };
//        VelocityListener velLisn = new VelocityListener() {
//            @Override
//            public void velocityChanged(float f, float f1, float f2) {
//                System.out.println("YL#velocityChanged f: " + f + ", f1: " + f1 + ", f2: " + f2);
//            }
//        };
//        VisionListener visLisn = new VisionListener() {
//            @Override
//            public void tagsDetected(VisionTag[] vts) {
//                System.out.println("VL#tagsDetected type: " + vts[0].getType());
////                    if (DroneControl.timeToLand) {
////                    //if (vts[0].getType() == 131072) {
////                        System.out.println("LANDING!");
////                        drone.getCommandManager().landing();
////                        if (drone != null) {
////                            drone.stop();
////                        }
////                        System.out.println("Found it, we're done here.");
////                        System.exit(0);
////                    }
//            }
//
//            @Override
//            public void trackersSend(TrackerData td) {
//                    //Example output:
//                //VL#trackersSend data: TrackersData 
//                //[trackers=[[[0, 0, 0]][[0, 0, 0]][[0, 0, 0]][[0, 0, 0]]
//                //[[0, 0, 0]]][[[0, 0, 0]][[0, 0, 0]][[0, 0, 0]][[0, 0, 0]]
//                //[[0, 0, 0]]][[[0, 0, 0]][[0, 0, 0]][[0, 0, 0]][[0, 0, 0]]
//                //[[0, 0, 0]]][[[0, 0, 0]][[0, 0, 0]][[0, 0, 0]][[0, 0, 0]]
//                //[[0, 0, 0]]][[[0, 0, 0]][[0, 0, 0]][[0, 0, 0]][[0, 0, 0]]
//                //[[0, 0, 0]]][[[0, 0, 0]][[0, 0, 0]][[0, 0, 0]][[0, 0, 0]]
//                //[[0, 0, 0]]]]
//                //
//                //System.out.println("VL#trackersSend data: " + td.toString());
//            }
//
//            @Override
//            public void receivedPerformanceData(VisionPerformance vp) {
//                    //Example output:
//                //VL#receivedPerformanceData vp: VisionPerormance 
//                //[time_szo=0.0, time_corners=0.0, time_compute=0.0, 
//                //time_tracking=0.0, time_trans=0.0, time_update=0.0, 
//                //time_custom=[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 
//                //0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0]]
//                //
//                //System.out.println("VL#receivedPerformanceData vp: " + vp.toString());
//            }
//
//            @Override
//            public void receivedRawData(float[] floats) {
//                    //Example:
//                //VL#receivedRawData data: [F@255a0908
//                //
//                //System.out.println("VL#receivedRawData data: " + floats.toString());
//            }
//
//            @Override
//            public void receivedData(VisionData vd) {
//                    //Example:
//                //VL#receivedData data: VisionData [vision_state=2, 
//                //vision_misc=0, vision_phi_trim=0.0, vision_phi_ref_prop=0.0, 
//                //vision_theta_trim=0.0, vision_theta_ref_prop=-0.0, 
//                //new_raw_picture=0, theta_capture=-0.005041081, 
//                //phi_capture=0.060272735, psi_capture=-0.017225504, 
//                //altitude_capture=243, time_capture_seconds=13, 
//                //time_capture_useconds=28290, body_v=[0.0, 0.0, 0.0], 
//                //delta_phi=0.0, delta_theta=0.0, delta_psi=0.0, 
//                //gold_defined=0, gold_reset=0, gold_x=0.0, gold_y=0.0]
//                //
//                //System.out.println("VL#receivedData data: " + vd.toString());
//            }
//
//            @Override
//            public void receivedVisionOf(float[] floats, float[] floats1) {
//                    //Example:
//                //VL#receivedVisionOf Float: [F@605893c7, Float1: [F@5cd59c16
//                //
//                //System.out.println("VL#receivedVisionOf Float: " + floats.toString() + ", Float1: " + floats1.toString());
//            }
//
//            @Override
//            public void typeDetected(int i) {
//                    //Example:
//                //VL#typeDetected Type: 3.
//                //
//                //System.out.println("VL#typeDetected Type: " + i + ".");
//            }
//        };
        
        final A4Brain brain = A4Brain.getInstance();
        if (brain.connect("192.168.1.1")) {
            brain.takeoff();
            brain.hold(5000);
            brain.forward(20).doFor(500);
            brain.hover().doFor(2000);
            brain.backward(20).doFor(400);
            brain.hover().doFor(2000);
            brain.land();
            brain.hold(2000);
            brain.replay();
        }
        brain.disconnect();

        System.out.println("Exiting. So long and thanks for all the fish.");
        System.exit(0);
    }
}
