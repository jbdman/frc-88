/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.templates.commands.PitcherSpeed;

/**
 *
 * @author Chris
 */
public class WheelSpinner extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private CANJaguar m_upperMotor = null;
    private CANJaguar m_lowerMotor = null;
    private boolean m_fault;
    private static final int teethPerGear = 11;

    public WheelSpinner(){
        try {
            m_upperMotor = new CANJaguar(Wiring.pitcherUpperMotorCANID);
        } catch (CANTimeoutException ex) {
            m_fault = true;
            System.err.println("CAN Init error: ID " + Wiring.pitcherUpperMotorCANID);
        }
        try {
            m_lowerMotor = new CANJaguar(Wiring.pitcherLowerMotorCANID);
        } catch (CANTimeoutException ex) {
            m_fault = true;
            System.err.println("CAN Init error: ID " + Wiring.pitcherLowerMotorCANID);
        }

        if(m_upperMotor != null) {
            try {
                m_upperMotor.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
                m_upperMotor.configEncoderCodesPerRev(teethPerGear);
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN timeout");
            }
        }
    
        if(m_lowerMotor != null) {
            try {
                m_lowerMotor.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
                m_lowerMotor.configEncoderCodesPerRev(teethPerGear);
            }
            catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN timeout");
            }
        }
    }
    public void enable() {
        if(m_upperMotor != null) {
            try {
                m_upperMotor.setPID(1.0, 0.0, 0.0);
                m_upperMotor.changeControlMode(CANJaguar.ControlMode.kSpeed);
                m_upperMotor.enableControl();
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN timeout");
            }
        }
        
        if(m_lowerMotor != null) {
            try {
                m_lowerMotor.setPID(1.0, 0.0, 0.0);
                m_lowerMotor.changeControlMode(CANJaguar.ControlMode.kSpeed);
                m_lowerMotor.enableControl();
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN timeout");
            }
        }
    }
    
    public void setPower(double upperMotorPower, double lowerMotorPower) {

        if(m_upperMotor != null) {
            try {
                m_upperMotor.setX(upperMotorPower);
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN Timeout");
            }
        }
        if(m_lowerMotor != null) {
            try {
                m_lowerMotor.setX(lowerMotorPower);
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN Timeout");
            }
        }

    }

    public void setSpeed(double upperRPM, double lowerRPM) {

        if(m_upperMotor != null) {
            try {
                m_upperMotor.setX(upperRPM);
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN Timeout");
            }
        }
        if(m_lowerMotor != null) {
            try {
                m_lowerMotor.setX(lowerRPM);
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN Timeout");
            }
        }
    }
    
    public double getSpeedUpper(){
        double speed = 0.0;
        if(m_upperMotor != null) {
            try {
                speed = m_upperMotor.getSpeed();
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN Timeout");
            }
        }
        return speed;
    }

    public double getSpeedLower(){
        double speed = 0.0;
        if(m_lowerMotor != null) {
            try {
                speed = m_lowerMotor.getSpeed();
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN Timeout");
            }
        }
        return speed;
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}