/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.templates.commands.DriveWithController;

/**
 *
 * @author TJ2
 */
public class DriveTrain extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private CANJaguar m_rearRightMotor = null;
    private CANJaguar m_rearLeftMotor = null;
    private CANJaguar m_frontRightMotor = null;
    private CANJaguar m_frontLeftMotor = null;

    private boolean m_fault = false;

    public DriveTrain(){
        //rear right motor
        try {
            m_rearRightMotor= new CANJaguar(Wiring.driveRearRightCANID);
        }
        catch (CANTimeoutException ex) {
            m_fault = true;
            System.err.println("##### CAN Init error: ID " + Wiring.driveRearRightCANID);
        }
        //rear left motor
        try {
            m_rearLeftMotor= new CANJaguar(Wiring.driveRearLeftCANID);
        }
        catch (CANTimeoutException ex) {
            m_fault = true;
            System.err.println("##### CAN Init error: ID " + Wiring.driveRearLeftCANID);
        }
        //front left motor
        try {
            m_frontLeftMotor= new CANJaguar(Wiring.driveFrontLeftCANID);
        }
        catch (CANTimeoutException ex) {
            m_fault = true;
            System.err.println("##### CAN Init error: ID " + Wiring.driveFrontLeftCANID);
        }
        //front right motor
        try {
            m_frontRightMotor= new CANJaguar(Wiring.driveFrontRightCANID);
        }
        catch (CANTimeoutException ex) {
            m_fault = true;
            System.err.println("##### CAN Init error: ID " + Wiring.driveFrontRightCANID);
        }

    }

    public void driveMechanum(double forward, double turn, double sideways) {

        double setFrontLeft  = forward + sideways + turn;
        double setFrontRight = forward - sideways - turn;
        double setBackLeft   = forward - sideways + turn;
        double setBackRight  = forward + sideways - turn;

        if(m_frontLeftMotor != null) {
            try {
                m_frontLeftMotor.setX(-setFrontLeft);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        if(m_frontRightMotor != null) {
            try {
                m_frontRightMotor.setX(setFrontRight);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        if(m_rearLeftMotor != null) {
            try {
                m_rearLeftMotor.setX(-setBackLeft);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        if(m_rearRightMotor != null) {
            try {
                m_rearRightMotor.setX(setBackRight);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
    }

    public void driveArcade(double forward, double turn) {

        driveMechanum(forward, turn, 0.0);

    }

    // Open loop control...
    public void driveTank(double left, double right) {

        if(m_frontLeftMotor != null) {
            try {
                m_frontLeftMotor.setX(left);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        if(m_frontRightMotor != null) {
            try {
                m_frontRightMotor.setX(-right);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        if(m_rearLeftMotor != null) {
            try {
                m_rearLeftMotor.setX(left);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        if(m_rearRightMotor != null) {
            try {
                m_rearRightMotor.setX(-right);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
    }

    /**
     * Returns the average speed of all wheels in meters per second
     * NOT YET IMPLEMENTED
     * 
     * @return Average speed of all wheels in ms^-1
     */
    public double getAveSpeed() {
        double speed = 0.0;

        return speed;
    }

    public double getRLSpeed() {
        double rpm = 0.0;

        try {
            rpm = m_rearLeftMotor.getSpeed();
        } catch (CANTimeoutException ex) {
            // should add a helper function for CAN Timeout errors
            m_fault = true;
            System.err.println("CAN timeout");
        }
        return speed(rpm);
    }

    /**
     * Converts the gearbox output shaft RPM to speed at the wheel in m/s
     *
     * @param gearboxRPM gearbox output shaft RPM as returned by gearbox encoder
     * @return speed in m/s based on gearbox output rpm
     */
    private double speed(double gearboxRPM) {
        final double wheelCircumference = 0.2;  // FIX THIS
        final double finalSprocketRatio = 3;    // FIX THIS

        return gearboxRPM * finalSprocketRatio * wheelCircumference;
    }

    public boolean getFault() {
        return m_fault;
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new DriveWithController());
    }
}