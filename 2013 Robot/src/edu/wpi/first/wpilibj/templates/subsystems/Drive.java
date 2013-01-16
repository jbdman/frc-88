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
import edu.wpi.first.wpilibj.templates.commands.DrivewithController;
/**
 *
 * @author David
 */
public class Drive extends Subsystem {
    CANJaguar frontLeftJag;
    CANJaguar frontRightJag;
    CANJaguar backLeftJag;
    CANJaguar backRightJag;
    RobotDrive drive;
    Joystick joystick;
    private boolean m_fault = false;
    
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public Drive()  {
        
        }
        
    public void initDefaultCommand() {
        //David reqs. help
        // Two jags commented out because they don't have encoders plugged in, must be uncommented for speed control
            try {
                frontLeftJag = new CANJaguar(12);
                // frontLeftJag.setPID(0.005,0.02,0);
            }
            catch (CANTimeoutException ex) {
            }
            try {
                frontRightJag = new CANJaguar(5);
            }
            catch  (CANTimeoutException ex) {
            }
            try {
                frontRightJag.setPID(0.005,0.02,0);
            }
            catch  (CANTimeoutException ex) {
            }
            try {
                backLeftJag = new CANJaguar(17);
            }
            catch (CANTimeoutException ex) {
            }
            try {
                backLeftJag.setPID(0.005,0.02,0);
            }
            catch (CANTimeoutException ex) {
            }
            try {
                backRightJag = new CANJaguar(16);
            }
            catch  (CANTimeoutException ex)  {
            }
            // backRightJag.setPID(0.005,0.02,0);
            
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
        public void driveTankOpenLoop(double left, double right) {

        if(frontLeftJag != null) {
            try {
                frontLeftJag.setX(-left);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        if(frontRightJag != null) {
            try {
                frontRightJag.setX(right);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        if(backLeftJag != null) {
            try {
                backLeftJag.setX(-left);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        if(backRightJag != null) {
            try {
                backRightJag.setX(right);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
    }
}
