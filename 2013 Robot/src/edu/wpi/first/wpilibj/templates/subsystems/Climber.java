/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.templates.commands.ClimberStop;
import edu.wpi.first.wpilibj.DigitalInput;
/**
 *
 * @author David
 */
public class Climber extends Subsystem {
    CANJaguar ClimbJag;
    //speeds can change if needed as of now full power will give you full power
    private boolean m_climbfault = false;
    private static final double defaultDownSpeed = 1;
    private static final double defaultUpSpeed = -1;
    private static final double defaultMaxSpeed = 1;
    
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public Climber() {
        
        try {
                ClimbJag = new CANJaguar(Wiring.climberCANID);
                
                //encoders and stuff needs to be determinec
                
                // Need to determine encoder codes per rev
                //ClimbJag.configEncoderCodesPerRev(360);
                //ClimbJag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                //ClimbJag.changeControlMode(CANJaguar.ControlMode.kPosition);
                //ClimbJag.setPID(0.005,0.02,0);
            }
            catch (CANTimeoutException ex) {
            }
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new ClimberStop());
    }
    public void stop() {
        ClimbOpenLoop(0.0);
    }
    public void up() {
        ClimbOpenLoop(defaultUpSpeed);
    }
    
    public void down() {
        ClimbOpenLoop(defaultDownSpeed);
    }
    
    public boolean lowerLimitTripped() {
        try {
            // Not sure it this should be forward or reverse limit
            // Also not sure if it returns true or false when the switch is tripped
            return ClimbJag.getForwardLimitOK();
        } catch(CANTimeoutException ex) {
            m_climbfault = true;
            System.err.println("****************CAN timeout***********");
            return true;
        }
    }
    

    public boolean upperLimitTripped() {
        try {
            // Not sure it this should be forward or reverse limit
            // Also not sure if it returns true or false when the switch is tripped
            return ClimbJag.getReverseLimitOK();
        } catch(CANTimeoutException ex) {
            m_climbfault = true;
            System.err.println("****************CAN timeout***********");
            return true;
        }
    }
    

    
    public void ClimbOpenLoop(double power) {

        if(ClimbJag != null) {
            try {
                //play with stuff under to see if it needs to be inverted
                ClimbJag.setX(-power * defaultMaxSpeed);
            } catch(CANTimeoutException ex) {
                m_climbfault = true;
                System.err.println("****************CAN timeout***********");
            }
        }            
      
}
  public void ClimbClosedLoop(double vertical) {
        // Change to distance stuff
        int distance = 100;
        if(ClimbJag != null) {
            try {
                //the formula below will probably be subject to change
                //also play with stuff under to see if it needs to be inverted
                ClimbJag.setX(-vertical * distance);
            } catch(CANTimeoutException ex) {
                m_climbfault = true;
                System.err.println("****************CAN timeout***********");
            }
        }   
    }
}