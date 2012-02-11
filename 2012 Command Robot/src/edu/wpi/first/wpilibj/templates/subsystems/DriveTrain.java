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

    public void pitcher(){
        //rear right motor
        try{
            m_rearRightMotor=new CANJaguar(Wiring.driveRearRightCANID);
        }
        catch (CANTimeoutException ex) {
            System.err.println("CAN Init error: ID " + Wiring.driveRearRightCANID);
        }
        //rear left motor
        try{
            m_rearLeftMotor=new CANJaguar(Wiring.driveRearLeftCANID);
        }
        catch (CANTimeoutException ex) {
            System.err.println("CAN Init error: ID " + Wiring.driveRearLeftCANID);
        }
        //front left motor
        try{
            m_frontLeftMotor=new CANJaguar(Wiring.driveFrontLeftCANID);
        }
        catch (CANTimeoutException ex) {
            System.err.println("CAN Init error: ID " + Wiring.driveFrontLeftCANID);
        }
        //front right motor
        try{
            m_frontRightMotor=new CANJaguar(Wiring.driveFrontRightCANID);
        }
        catch (CANTimeoutException ex) {
            System.err.println("CAN Init error: ID " + Wiring.driveFrontRightCANID);
        }
    }
    public void drive(){

    }
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}