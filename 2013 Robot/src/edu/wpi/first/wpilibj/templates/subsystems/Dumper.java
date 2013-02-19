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

public class Dumper extends Subsystem {
    CANJaguar DumperJag = null;
    private boolean m_fault = false;
    private static final double defaultBackwardSpeed= 0.35;
    private static final double defaultForwardSpeed = -0.35;
    private DigitalInput m_limitSwitch;
    private static final int linesPerRotation = 100;
    private static final double lowerScorePosition = -3;
    private static final double upperScorePosition = 3;
    private static final double feedPosition = 1;
    //^^^these numbers affect speed and can be changed
    private static final double revPerInch = 0.1;
    //this # i am almost 100% sure has to be changed
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
        DumpClosedLoop(feedPosition);
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
    
    /**
     * Gets the position of the Dumper by reading the position of the encoder
     * from the Jaguar.  A position of 0 is the home position (vertical).
     * A positive position is a ??? position, and a negative position is a
     * ??? position.  (Front or back, not actually sure which is which yet)
     * 
     * @return  The position of the Dumper.
     */
    
    public double getPosition() {
      double position = 0.0;
      try {
        //the formula below will probably be subject to change
        //also play with stuff under to see if it needs to be inverted
                position = DumperJag.getPosition();
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
      return position;
  }

    /**
     * Returns the value of the fault flag
     *
     */
    public boolean getFault() {
        return m_fault;
    }
    
}
