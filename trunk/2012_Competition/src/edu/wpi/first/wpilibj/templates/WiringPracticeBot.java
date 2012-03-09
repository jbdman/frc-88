package edu.wpi.first.wpilibj.templates;

/**
 * The Wiring is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class WiringPracticeBot {
    /*
     * ### CAN Motors ###
     */
    // Drive system (motors and encoders)
    public static final int driveFrontLeftCANID    = 10;
    public static final int driveFrontRightCANID   = 11;
    public static final int driveRearLeftCANID     = 8;
    public static final int driveRearRightCANID    = 2;

    //String theory
    public static final int lifterMotorCANID  =  7;

    //RampPusher
    public static final int rampPushingMotorCANID  = 3;

    //Turret
    public static final int pitcherUpperMotorCANID = 6;
    public static final int pitcherLowerMotorCANID = 5;
    public static final int turretMotorCANID       = 9;
    
    /*
     * ### Solenoids ###
     */
    public static final int shooterLoadSolenoid    = 4;
    public static final int shooterAngleSolenoid   = 5;
    public static final int footSolenoid           = 6;

    // Analog
    public static final int robotPositionAccelerometer =  1;    // not used yet
    public static final int robotPositionGyro      = 2;     // not used yet

    /*
     * ### Digital Relay ports ###
     */
    public static final int compressorPowerRelay = 8;

    /*
     * ### Digital IO ports ###
     */
    public static final int compressorPressureSwitch = 3;
    public static final int turretLimitSwitch = 14;
    public static final int rampPusherMainLimitSwitch = 1;
    public static final int rampPusherDownSwitch = 5;

}