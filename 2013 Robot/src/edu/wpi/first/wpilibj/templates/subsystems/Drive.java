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
    CANJaguar leftJag;
    CANJaguar rightJag;
    RobotDrive drive;
    private boolean m_fault = false;
    
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public Drive()  {
            try {
                leftJag = new CANJaguar(Wiring.driveLeftCANID);
                // Need to determine encoder codes per rev
                leftJag.configEncoderCodesPerRev(360);
                leftJag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                leftJag.changeControlMode(CANJaguar.ControlMode.kPosition);
                leftJag.setPID(0.005,0.02,0);
            }
            catch (CANTimeoutException ex) {
            }
            try {
                rightJag = new CANJaguar(Wiring.driveRightCANID);
                // Need to determine encoder codes per rev
                rightJag.configEncoderCodesPerRev(360);
                rightJag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                rightJag.changeControlMode(CANJaguar.ControlMode.kPosition);
                rightJag.setPID(0.005,0.02,0);
            }
            catch  (CANTimeoutException ex) {
            }
        }
        
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new DrivewithController());
    }
    
    public void driveTankOpenLoop(double left, double right) {

        if(leftJag != null) {
            try {
                leftJag.setX(-left);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        if(rightJag != null) {
            try {
                rightJag.setX(right);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
    }
    
    public void driveTankClosedLoop(double left, double right) {
        // Change to max speed wanted
        int maxRPM = 100;
        if(leftJag != null) {
            try {
                leftJag.setX(-left * maxRPM);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        if(rightJag != null) {
            try {
                rightJag.setX(right * maxRPM);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
    }
}
