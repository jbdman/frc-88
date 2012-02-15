/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.templates.Wiring;

/**
 *
 * @author TJ2
 */
public class DriveTrain extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private CANJaguar m_rearRightMotor;
    private CANJaguar m_rearLeftMotor;
    private CANJaguar m_frontRightMotor;
    private CANJaguar m_frontLeftMotor;

    public void DriveTrain(){
        //rear right motor
        try{
            m_rearRightMotor= new CANJaguar(Wiring.driveRearRightCANID);
        }
        catch (CANTimeoutException ex) {
            System.err.println("CAN Init error: ID " + Wiring.driveRearRightCANID);
        }
        //rear left motor
        try{
            m_rearLeftMotor= new CANJaguar(Wiring.driveRearLeftCANID);
        }
        catch (CANTimeoutException ex) {
            System.err.println("CAN Init error: ID " + Wiring.driveRearLeftCANID);
        }
        //front left motor
        try{
            m_frontLeftMotor= new CANJaguar(Wiring.driveFrontLeftCANID);
        }
        catch (CANTimeoutException ex) {
            System.err.println("CAN Init error: ID " + Wiring.driveFrontLeftCANID);
        }
        //front right motor
        try{
            m_frontRightMotor= new CANJaguar(Wiring.driveFrontRightCANID);
        }
        catch (CANTimeoutException ex) {
            System.err.println("CAN Init error: ID " + Wiring.driveFrontRightCANID);
        }
    }

    // Closed loop control...
    public void driveSpeed(double forward, double turn, double sideways) {

    }

    // Open loop control...
    public void drivePower(double forward, double turn, double sideways) {

        double setFrontLeft  = forward + sideways + turn;
        double setFrontRight = forward - sideways - turn;
        double setBackLeft   = forward - sideways + turn;
        double setBackRight  = forward + sideways - turn;

        try {
            m_frontLeftMotor.setX(-setFrontLeft);
            m_frontRightMotor.setX(setFrontRight);
            m_rearLeftMotor.setX(-setBackLeft);
            m_rearRightMotor.setX(setBackRight);
        }
        catch(CANTimeoutException e) {
            System.err.println("****************CAN timeout***********");
        }
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}