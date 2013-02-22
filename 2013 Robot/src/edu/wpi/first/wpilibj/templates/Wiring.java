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
    public static final int driveLeftCANID    = 5; 
    public static final int driveRightCANID   = 2;
    public static final int TilterCANID = 3; 
    public static final int climberCANID = 4;
    public static final int DumperCANID = 6;

    // ### PWM / Servos ###
    //servo ids
    public static final int CameraServoLeft = 1;
    public static final int CameraServoRight = 2;

    // ### Digital Input/Outputs ###
    public static final int dumperLimitSwitch = 1;
    public static final int RangeFinderPing = 2;
    public static final int RangeFinderEcho = 3;

    /* ### Pneumatics ###
     * 
     */
    // There are no pneumatics this year!
}
