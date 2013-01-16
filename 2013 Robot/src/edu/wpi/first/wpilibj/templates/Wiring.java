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
    // Drive system (motors and encoders)
    public static final int driveFrontLeftCANID    = 12;
    public static final int driveFrontRightCANID   = 5;
    public static final int driveBackLeftCANID     = 17;
    public static final int driveBackRightCANID    = 16;
}