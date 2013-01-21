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
/**
 *
 * @author David
 */
public class Climber extends Subsystem {
    CANJaguar ClimbJag;
    //dont know what the stuff under this does just threw it in - David
    private boolean m_climbfault = false;
    private static final double defaultDownSpeed = -0.5;
    private static final double defaultUpSpeed = .05;
    
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
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
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
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
    public void ClimbOpenLoop(double vertical) {

        if(ClimbJag != null) {
            try {
                //play with stuff under to see if it needs to be inverted
                ClimbJag.setX(-vertical);
            } catch(CANTimeoutException ex) {
                //see line 20 same thing here - David
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