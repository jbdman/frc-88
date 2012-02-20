/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.templates.commands.LifterUp;

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
/**
 *  @author TJ2
 */
public class BallLifter extends Subsystem {
    
    private CANJaguar m_lifterMotor;
    private int m_ID;
    private double m_lifterMotorPower;

    private static final double defaultUpSpeed = 0.8;
    private static final double defaultDownSpeed = -0.8;

    public BallLifter() {
        try {
            m_lifterMotor = new CANJaguar(Wiring.lifterMotorCANID);
            m_ID = Wiring.lifterMotorCANID;
        } catch (CANTimeoutException ex) {
            System.err.println("CAN Init error: ID " + m_ID);
        }
    }

    public void setPower(double power) {
        set(power);
    }

    public void stop() {
        set(0.0);
    }

    //
    public void up() {
        set(defaultUpSpeed);
    }

    //
    public void down() {
        set(defaultDownSpeed);
    }

    //
    private void set(double power) {
        try {
            m_lifterMotor.setX(power);
        }
        catch (CANTimeoutException ex) {
            System.err.println("CAN Timeout: ID " + m_ID);
        }
    }

    public boolean isStopped() {
        return (m_lifterMotorPower == 0.0);
    }

    public boolean isGoingUp() {
        return (m_lifterMotorPower >= 0.0);
    }

    public boolean isGoingDown() {
        return (m_lifterMotorPower <= 0.0);
    }

    public double getPower() {
        return (m_lifterMotorPower);
    }

    public void initDefaultCommand() {

        // Set the default command for a subsystem here.
        //setDefaultCommand(new LifterUp());
    }
}