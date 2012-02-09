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

    // Drive system
    public static final int driveFrontLeftCANID  =  9;    // FIX THIS!
    public static final int driveFrontRightCANID = 10;    // FIX THIS!
    public static final int driveRearLeftCANID   = 11;    // FIX THIS!
    public static final int driveRearRightCANID  = 12;    // FIX THIS!
    // Pitcher
    public static final int pitcherUpperMotorCANID = 2;
    public static final int pitcherLowerMotorCANID = 5;

    /*
     * ### Digital IO ports ###
     */
    // Pitcher
    public static final int pitcherUpperSpeedSensor = 1;
    public static final int pitcherLowerSpeedSensor = 2;

}
