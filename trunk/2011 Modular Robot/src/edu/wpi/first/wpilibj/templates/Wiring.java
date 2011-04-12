/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author tj2
 */
public class Wiring {

    /*******************
     *    CAN bus      *
     *******************/
    public static final int CAN_ID_FRONT_LEFT_WHEEL = 2;
    public static final int CAN_ID_FRONT_RIGHT_WHEEL = 3;
    public static final int CAN_ID_REAR_LEFT_WHEEL = 4;
    public static final int CAN_ID_REAR_RIGHT_WHEEL = 5;
    public static final int CAN_ID_ELEVATOR = 6;

    /*******************
     * Solenoid module *
     *******************/
    public static final int SOLENOID_ELBOW_VALVE = 1;
    public static final int SOLENOID_GRIPPER_VALVE = 2;
    public static final int SOLENOID_PUSHER_VALVE = 3;
    public static final int SOLENOID_LINESENSOR_POWER = 8;

    public static final int SOLENOID_LOGOLIGHT_POWER = 7;   // temporary!

    /*******************
     *  Analog module  *
     *******************/
    public static final int ANALOG_GYRO_CHANNEL = 1;
    public static final int ANALOG_PRESSURE_SENSOR = 2;

    /*******************
     * Digital Sidecar *
     *******************/
    // digital inputs
    public static final int DIGITAL_IN_COMPRESSOR_PRESSURE_SWITCH = 1;
    public static final int DIGITAL_IN_LINESENSOR_LEFT = 2;
    public static final int DIGITAL_IN_LINESENSOR_MID = 3;
    public static final int DIGITAL_IN_LINESENSOR_RIGHT = 4;
    // digital ouputs
    public static final int DIGITAL_OUT_LOGOLIGHT_A = 5;
    public static final int DIGITAL_OUT_LOGOLIGHT_B = 6;
    // relays
    public static final int RELAY_COMPRESSOR_POWER = 1;

}
