/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.templates.commands.TurretWithController;

/**
 *
 * @author Mike Edgington
 */
public class Turret extends Subsystem {


    // Will become PID subsystem very soon...

    private CANJaguar m_turretMotor = null;
    private boolean m_fault = false;

    public Turret() {
        // configure turret motor
        try {
            m_turretMotor = new CANJaguar(Wiring.turretMotorCANID);
        } catch (CANTimeoutException ex) {
            m_fault = true;
            System.err.println("##### CAN Init Failure ID: " + Wiring.turretMotorCANID);
        }
    }

    public void enable() {
        if(m_turretMotor != null) {
            try {
                m_turretMotor.configEncoderCodesPerRev(360);
                m_turretMotor.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("##### CAN Timeout ####");
            }
        }
    }

    public void setAngle(double angle) {

    }

    public void setPower(double power) {
        if(m_turretMotor != null) {
            try {
                m_turretMotor.setX(power);
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("##### CAN Timeout ####");
            }
        }
    }

    public double getAngle() {
        double angle = 0.0;

        if(m_turretMotor != null) {
            try {
                angle = m_turretMotor.getPosition();
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("##### CAN Timeout ####");
            }
        }
        return angle;
    }

    public boolean getFault() {
        return m_fault;
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new TurretWithController());
    }
}