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
    // 1/24/12 drive motors may have to be inverted because they are subject to switch
    CANJaguar leftJag;
    CANJaguar rightJag;
    RobotDrive drive;
    private boolean m_fault = false;
    
    /**
     * Initializes Jaguars and sets up PID control.
     */
    public Drive()  {
            try {
                leftJag = new CANJaguar(Wiring.driveLeftCANID);
                // Need to determine encoder codes per rev
                leftJag.configEncoderCodesPerRev(360);
                leftJag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                leftJag.changeControlMode(CANJaguar.ControlMode.kPosition);
                leftJag.setPID(0.005,0.02,0);
                leftJag.enableControl();
            }
            catch (CANTimeoutException ex) {
                m_fault = true;
            }
            try {
                rightJag = new CANJaguar(Wiring.driveRightCANID);
                 //Need to determine encoder codes per rev
                rightJag.configEncoderCodesPerRev(360);
                rightJag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                rightJag.changeControlMode(CANJaguar.ControlMode.kPosition);
                rightJag.setPID(0.005,0.02,0);
                rightJag.enableControl();
            }
            catch  (CANTimeoutException ex) {
                m_fault = true;
            }
        }
    /**
     * Sets the default command to DriveWithController so that when nothing
     * else is happening with the Drive, we can use controllers to move.
     */
    public void initDefaultCommand() {
        setDefaultCommand(new DrivewithController());
    }
    
    /**
     * Open loop control of the Drive, using voltage percentage.
     * 
     * @param   left    The voltage for the left side of the Drive.  Value
     *                  should be specified between -1.0 and 1.0.
     * @param   right   The voltage for the right side of the Drive. Value
     *                  should be specified between -1.0 and 1.0.
     */
    public void driveTankOpenLoop(double left, double right) {

        if(leftJag != null) {
            try {
                leftJag.setX(left);
                leftJag.disableControl();
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        if(rightJag != null) {
            try {
                rightJag.setX(-right);
                rightJag.disableControl();
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
    }
    
    /**
     * Close loop control for the Drive, using encoders to control speed.
     * 
     * @param   left    The speed for the left side of the Drive.  Value
     *                  should be positive and be below the Drive's max speed.
     * @param   right   The speed for the right side of the Drive.  Value
     *                  should be positive and be below the Drive's max speed.
     */
    public void driveTankClosedLoop(double left, double right) {
        // Change to max speed wanted
        // Why are we multiplying by maxRPM?
        int maxRPM = 100;
        if(leftJag != null) {
            try {
                leftJag.setX(left * maxRPM);
                leftJag.enableControl();
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        if(rightJag != null) {
            try {
                rightJag.setX(-right * maxRPM);
                rightJag.enableControl();
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
    }
}
