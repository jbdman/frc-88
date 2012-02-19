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
 * @author Mike Edgington
 */
public class Turret extends Subsystem {

    // Will become PID subsystem very soon...

    CANJaguar m_turretMotor;

    public Turret() {
        // configure turret motor
        try {
            m_turretMotor = new CANJaguar(Wiring.turretMotorCANID);
        } catch (CANTimeoutException ex) {
            System.err.println("##### CAN Timeout ####");
        }
    }

    public void enable() {
        try {
            m_turretMotor.configEncoderCodesPerRev(360);
            m_turretMotor.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
        } catch (CANTimeoutException ex) {
            System.err.println("##### CAN Timeout ####");
        }
        
    }

    public void setAngle(double angle) {

    }

    public void set(double power) {
        try {
            m_turretMotor.setX(power);
        } catch (CANTimeoutException ex) {
            System.err.println("##### CAN Timeout ####");
        }
    }

    public double getAngle() {
        double angle = 0.0;

        try {
            angle = m_turretMotor.getPosition();
        } catch (CANTimeoutException ex) {
            System.err.println("##### CAN Timeout ####");
        }

        return angle;
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}