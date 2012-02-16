/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.templates.commands.RampPusherStop;

/**
 *
 * @author TJ2
 */
public class RampPusher extends Subsystem {

    private CANJaguar m_rampPusher;

    public RampPusher(){
        try {
            m_rampPusher = new CANJaguar(Wiring.rampPushingMotorCANID);
        }
        catch (CANTimeoutException ex) {
            System.err.println("CAN Init error: " + Wiring.rampPushingMotorCANID);
        }

        try {
            m_rampPusher.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
        }
        catch (CANTimeoutException ex) {
            System.err.println("CAN Timeout");
        }

    }

    public void down() {
        try {
            m_rampPusher.setX(1.0);
        }
        catch (CANTimeoutException ex) {
            System.err.println("CAN Timeout");
        }
    }

    public void up() {
        try {
            m_rampPusher.setX(-1.0);
        }
        catch (CANTimeoutException ex) {
            System.err.println("CAN Timeout");
        }
    }

    public void stop() {
        try {
            m_rampPusher.setX(0.0);
        }
        catch (CANTimeoutException ex) {
            System.err.println("CAN Timeout");
        }
    }

    public void setPower(double power) {
        try {
            m_rampPusher.setX(power);
        }
        catch (CANTimeoutException ex) {
            System.err.println("CAN Timeout");
        }
    }

    public double getCurrent() {
        double current = 0.0;

        try {
            current = m_rampPusher.getOutputCurrent();
        }
        catch (CANTimeoutException ex) {
            System.err.println("CAN Timeout");
        }
        return current;
    }

    public double getPosition() {
        double posn = 0.0;

        try {
            posn = m_rampPusher.getPosition();
        }
        catch (CANTimeoutException ex) {
            System.err.println("CAN Timeout");
        }
        return posn;
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new RampPusherStop());
    }
}