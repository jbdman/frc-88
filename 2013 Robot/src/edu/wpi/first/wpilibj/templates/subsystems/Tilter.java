 /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.templates.commands.TilterJoystick;

/**
 *
 * @author David + Ag
 */
//must wait until there is a more definitive idea ie. must be built or described better - David
public class Tilter extends Subsystem {
    CANJaguar TilterJag = null;
    
    private boolean m_calibrated = false;
    private boolean m_fault = false;
    private double encoderHome = 0.0;
    //these numbers will have to be changed depending on the speed of the motors
    private static final double defaultDownSpeed = -0.7;
    private static final double defaultUpSpeed = 0.7;
    private static final double defaultTiltMaxSpeed = 0.7;
    // These angles are currently arbitrary and should be changed to be useful
    public static final double HomeAngle = 0;
    public static final double DownAngle = -3;
    // Tilter Dimensions, specified in inches
    private static final double dimensionA = 8.25;
    private static final double dimensionB = 8.5;
    
    private static final double INCHES_PER_REV = 0.25;
    private static final double FORWARD_SOFT_LIMIT_INCHES  = 4.0;
    private static final int ENCODER_LINES = 100;

    // Values used in angle-to-distance conversion, precomputed in the
    // constructor to make the calculations faster.
    private double angleAddition;
    private double angleMultiplier;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public Tilter()  {
        // Initialize the Jaguar and its PID control
        try {
            TilterJag = new CANJaguar(Wiring.TilterCANID);
//            TilterJag.changeControlMode(CANJaguar.ControlMode.kPosition);
//            TilterJag.enableControl();
            TilterJag.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
            TilterJag.configEncoderCodesPerRev(ENCODER_LINES);
            TilterJag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);      
            TilterJag.setVoltageRampRate(20.0);      
        } catch (CANTimeoutException ex) {
            System.out.println("***CAN ERROR***");
            m_fault = true;
        }
        // Calculate the values used in the angle-to-distance conversion
        angleAddition = (dimensionA * dimensionA) + (dimensionB * dimensionB);
        angleMultiplier = 2 * dimensionA * dimensionB;
    }
    
    public void initDefaultCommand() {
        setDefaultCommand(new TilterJoystick());
    }
    /**
     * Move the Tilter down (towards horizontal).
     */
    public void down(){
        TilterOpenLoop(defaultDownSpeed);
    }
    /**
     * Move the Tilter up (towards vertical).
     */
    public void up(){
        TilterOpenLoop(defaultUpSpeed);
    }
    public void tall() {
        TilterClosedLoop(7);
    }
    public void small () {
        TilterClosedLoop(-7);
    }
    
    
    /**
     * Stop the Tilter by setting the motor speed to 0.  Does not do any sort of
     * compensation to keep the Tilter at its current position.  However, the
     * Tilter uses a lead screw and is powered by a window motor, so there is
     * likely to be little if any back drive.
     */
    public void Tiltstop(){
        TilterOpenLoop(0.0);
    }
      public void calibrateEncoder() {
        m_calibrated = true;
        encoderHome = getRevolution();
        try {
            TilterJag.configSoftPositionLimits(encoderHome - 1, encoderHome + FORWARD_SOFT_LIMIT_INCHES / INCHES_PER_REV);
        }
        catch (CANTimeoutException ex) {
            m_fault = true;
        }
    }
      /**
     * Returns the value of the calibration flag
     * 
     * @return  True if the Climber subsystem has been calibrated
     */
    public boolean isCalibrated() {
        return m_calibrated;
    }
   /**
     * Enables closed loop control of the Tilter.
     */
    public void enableClosedLoop(){
                try {
                    TilterJag.changeControlMode(CANJaguar.ControlMode.kPosition);
                    TilterJag.setPID(0.005,0.02,0);
                    TilterJag.enableControl();
                }
                catch (CANTimeoutException ex){
                    System.err.println("****************CAN timeout*************");
                    
                }
    }
    /**
     * Enabled open loop control of the Tilter.
     */
    public void enableOpenLoop(){
        try{
            TilterJag.disableControl();
        }
        catch (CANTimeoutException ex){
        }
    }
    /**
     * Gets whether or not the vertical limit of the Tilter has been tripped.
     * This sensor is also a stop for the Jaguar.
     * 
     * @return  True if the Tilter has tripped the sensor indicating vertical
     *          position.
     */
    public boolean LimitTripped() {
        try {
            return !TilterJag.getForwardLimitOK();
        } catch(CANTimeoutException ex) {
            m_fault = true;
            System.err.println("****************CAN timeout***********");
            return true;
        }
    }

    /**
     * Open loop control of the Tilter, using voltage percentage.
     * 
     * @param   power   The voltage of the Tilter motor specified from -1.0 
     *                  to 1.0.  A positive value is upward motion, negative
     *                  is downward motion.
     */
    public void TilterOpenLoop(double power){
        if(TilterJag != null) {
            try {
                // Is this inversion correct?  I may have broken everything. 2/9
                TilterJag.setX(power);
                // Do we need to be disabling control every time we drive?  2/9
//                TilterJag.disableControl();
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        
    }
    /**
     * Closed loop control for the Tilter, using an encoder.
     * 
     * @param   distance    The travel distance of the lead screw to change the
     *                      position of the Tilter.  A position of 0 is the home
     *                      position (vertical), and position should always be
     *                      a positive value.  Specified in inches.
     */
    public void TilterClosedLoop(double distance) {
        // need to convert distance input (inches) to something the jag can use (rotations)
        // check to make sure distance input is positive
        if(TilterJag != null) {
            try {
                TilterJag.setX(distance);
                // Need to determine encoder codes per rev
                // Should we be doing all of this every time we move under closed loop?
                TilterJag.configEncoderCodesPerRev(360);
                TilterJag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                TilterJag.changeControlMode(CANJaguar.ControlMode.kPosition);
                TilterJag.setPID(0.005,0.02,0);
                TilterJag.enableControl();
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
    }
    /**
     * Given a desired angle, calculates the distance the Tilter should be
     * set to in order to get to the angle.
     * 
     * @param   angle   The angle to calculate distance for, specified in radians. (?)
     * @return  The distance that the Tilter should be set to in order to
     *          get to the angle.
     */
    private double distanceFromAngle(double angle) {
        double distance = 0.0;
        // Law of cosines, in case you're wondering.
        distance = Math.sqrt(angleAddition - (angleMultiplier * Math.cos(angle)));
        return distance;
    }
    
    private double angleFromDistance(double distance) {
        double cosAngle = 0.0;
        cosAngle = (angleAddition - distance * distance) / angleMultiplier;
        return cosAngle;
    }
    
    private double getRevolution() {
        double revolution = 0.0;
        try {
            revolution = TilterJag.getPosition();
        } catch(CANTimeoutException ex) {
            m_fault = true;
            System.err.println("****************CAN timeout***********");
        }
      return revolution;
    }
        
    public double getAngle() {
        // distance is simply rotations times the pitch of the leadscrew
//        return angleFromDistance(LEADSCREW_PITCH * getRevolution()); 
        return (INCHES_PER_REV * getRevolution()); 
    }

    /**
     * Returns the value of the fault flag
     *
     */
    public boolean getFault() {
        return m_fault;
    }

}

