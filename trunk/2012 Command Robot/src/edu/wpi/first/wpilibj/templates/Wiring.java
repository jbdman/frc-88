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
    public static final int landingGearCANID     =  5;    // FIX THIS!

    //String theory
    public static final int lifterMotorCANID  =  2;    // FIX THIS!
    public static final int right_leftStringCanID=  7;    // FIX THIS!
    public static final int front_backStingCANID =  6;    // FIX THIS!
    public static final int liftingStringsCANID = 0;   //FIX THIS!

    // Drive system (motors and encoders)
    public static final int driveFrontLeftCANID    =  9;    // FIX THIS!
    public static final int driveFrontRightCANID   = 10;    // FIX THIS!
    public static final int driveRearLeftCANID     = 11;    // FIX THIS!
    public static final int driveRearRightCANID    = 12;    // FIX THIS!

    //RampPusher
    public static final int rampPushingMotorCANID  = 13;

    //Turret
    public static final int pitcherUpperMotorCANID = 2;    // FIX THIS!
    public static final int pitcherLowerMotorCANID = 3;    // FIX THIS!
    public static final int turretSpinnerCANID     = 4;    // FIX THIS!
    
    /*
     * ### Solenoids ###
     */
    public static final int pitcherLoadSolenoid    = 1;    // FIX THIS!
    public static final int pitcherAngleSolenoid   = 2;    // FIX THIS!
    public static final int subdriveSolenoid       = 3;    // FIX THIS!

    //Analog
    public static final int turretRotationEncoder  =  5;    // FIX THIS!
    public static final int robotPositionAcelometer=  9;    // FIX THIS!
    public static final int robotPositionGyro      = 10;    // FIX THIS!
    /*
     * ### Digital IO ports ###
     */
    public static final int pitcherUpperSpeedSensor = 6;    // FIX THIS!
    public static final int pitcherLowerSpeedSensor = 7;    // FIX THIS!
    public static final int pneumaticPressureSwitch = 8;    // FIX THIS!
}