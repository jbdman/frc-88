/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.templates.commands.DumperStop;
import edu.wpi.first.wpilibj.Encoder;
/**
 *
 * @author David
 */
/*************************************************************
     * NOTE: The commands need work. Please fix them. Someone. Also
     * this is a cool box, is it not?
     *************************************************************/
// more of this must be shown to me or Ag before we can start
public class Dumper extends Subsystem {
    CANJaguar DumperJag = null;
    private boolean m_fault = false;
    private static final double defaultBackwardSpeed= -.5;
    private static final double defaultForwardSpeed = .5;
    private DigitalInput m_limitSwitch;
    private static final int linesPerRotation = 100;
    private static final double lowerScorePosition = -30;
    private static final double upperScorePosition = 30;
    //^^^these numbers affect speed and can be changed
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public Dumper(){
        try {
            
            //we got some got conflicts with this bit and line 60. They were done by different people and we need to
            //decide which is right. ATM one is commented out
            
                m_limitSwitch = new DigitalInput(Wiring.dumperLimitSwitch);
                DumperJag = new CANJaguar(Wiring.DumperCANID);
                if (DumperJag != null) {
                //encoders and stuff needs to be determined for angle
                //this stuff controls the close loop control, they may be subject to change
                // Need to determine encoder codes per rev
                //DumperJag.configEncoderCodesPerRev(360);
                //DumperJag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                //DumperJag.changeControlMode(CANJaguar.ControlMode.kPosition);
                //DumperJag.setPID(0.005,0.02,0);
            }
        }
            catch (CANTimeoutException ex) {
                System.out.println("***CAN ERROR***");
                m_fault = true;
            }
        // this and stuff above need to be combined or replaced or something
        if(DumperJag != null) {
            try {
                DumperJag.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
                DumperJag.configEncoderCodesPerRev(linesPerRotation);
            }
            catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN timeout");
            }
        }
    }
    
    /**
     * Set the default command to DumperStop so that when nothing else is
     * happening, the Dumper doesn't move.
     * 
     * DumperStop command needs to be created.
     */
    public void initDefaultCommand() {
        setDefaultCommand(new DumperStop());
    }
    /**
     * Moves the dumper to dump the discs into the score area at the top
     * of the pyramid.  Should only be used after climbing, otherwise it
     * will likely collide with the climber mast.
     * 
     * Uses an internal constant position.  This constant is currently
     * an arbitrary number.  Please change it.
     */
    public void highScorePosition(){
        DumpClosedLoop(upperScorePosition);
    }
    /**
     * Moves the dumper to dump the discs into the 1 point score area.
     * 
     * Uses an internal constant position.  This constant is currently
     * an arbitrary number.  Please change it.
     */
    public void lowScorePosition(){
        DumpClosedLoop(lowerScorePosition);
    }
    /**
     * Moves the dumper to the starting position.
     * 
     * Not currently implemented.  Should this be a command instead?
     */
    public void feed_position(){
        DumpOpenLoop(0.0);
    }
    /**
     * Stops the Dumper by setting the motor speed to 0.  Does not to any sort
     * of compensation to keep the Dumper position at the same level.  However,
     * the Dumper is powered by a window motor, so there is likely to be little
     * if any back drive.
     */
    public void stop() {
        DumpOpenLoop(0.0);
    }
    /**
     * Moves the dumper toward the front of the robot.  That is, toward the
     * Climber mast and the goal at the top of the pyramid once the robot
     * has climbed.
     */
    public void foward(){
        DumpOpenLoop(defaultForwardSpeed);
    }
    /**
     * Moves the dumper toward the back of the robot.  That is, away from the
     * Climber mast and towards the low scoring goal.
     */
    public void backward(){
        DumpOpenLoop(defaultBackwardSpeed);
    }
    /**
     * Indicated whether the Dumper is at the home position (vertical) by
     * reading the position sensor.
     * 
     * Should this be renamed to indicate that it is a position sensor
     * and not a limit switch?  I believe we're using one of the induction
     * sensors for this instead of a limit switch.
     * 
     * @return  Returns true if the position switch is tripped.  Returns
     *          false otherwise.
     */
    public boolean isLimitSwitchPressed() {
        return m_limitSwitch.get();
    }
    /**
     * Gets the position of the Dumper by reading the position of the encoder
     * from the Jaguar.  A position of 0 is the home position (vertical).
     * A positive position is a ??? position, and a negative position is a
     * ??? position.  (Front or back, not actually sure which is which yet)
     * 
     * @return  The position of the Dumper.
     */
    public double getPosition(){
        double position = 0.0;
        if(DumperJag != null) {
            try {
                position = DumperJag.getPosition();
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN Timeout");
            }
        }
        return position;
    }
    /**
     * Open loop control of the Dumper, using voltage percentage.
     * 
     * @param   power   The voltage for the motor of the Dumper.  Value
     *                  should be specified between -1.0 and 1.0.  A positive
     *                  value is forward motion, negative is backward.
     */
    public void DumpOpenLoop(double power) {
        if(DumperJag != null) {
            try {
                // may need to be inverted
                // positive should be forward, negative should be backward
                DumperJag.setX(power);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }            
      
    }    
    
    /**
     * Close loop control for the Dumper, using an encoder to control position.
     * 
     * Need to determine units position should be specified in (possibly degrees).
     * 
     * @param   position    The desired position for the Dumper to move to.  A
     *                      position of 0 is the home position (vertical).  A
     *                      positive position moves the dumper forward, and a
     *                      negative position moves it backwards.
     */
    public void DumpClosedLoop(double position) {
        if(DumperJag != null) {
            try {
                // may need to be inverted
                // positive should be forward movement, negative should be backward
                DumperJag.setX(position);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }   
    }
}
