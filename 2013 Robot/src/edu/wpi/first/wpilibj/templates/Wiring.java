package edu.wpi.first.wpilibj.templates;

/**
 * The Wiring is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class Wiring {
    /*
     * ### CAN Motors ###
     */
    // Added real CAN IDs from the practice robot
    public static final int driveLeftCANID    = 2; 
    public static final int driveRightCANID   = 4;
    public static final int TilterCANID = 10; 
    public static final int climberCANID = 5;
    public static final int DumperCANID = 8;
    
    //servo ids
    public static final int Servo1ID = 1;
    public static final int Servo2ID = 2;
    
    /* ### Pneumatics ###
     * 
     */
    // There are no pneumatics this year!
    
    /*
     * * ##limitSwitches##
     */
    public static final int dumperLimitSwitch = 1;
}