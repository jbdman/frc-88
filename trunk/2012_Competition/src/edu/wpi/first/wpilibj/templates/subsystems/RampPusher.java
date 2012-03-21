/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.templates.commands.RampPusherStop;

/**
 *
 * @author TJ2
 */
public class RampPusher extends Subsystem {

    private CANJaguar m_rampPusher = null;
    private DigitalInput m_limitSwitch;
    private DigitalInput m_downSwitch;
    private boolean m_isCalibrated = false;

    private double m_posnOffset = 0.0;

    private boolean m_fault = false;

    private static final int ticksPerRev = 250;

    private static final double defaultDownPower = 1.0;
    private static final double defaultUpPower = -1.0;

    public static final double maxDownCurrent = 20.0;
    public static final double maxUpCurrent = 10.0;

    public RampPusher(){
        try {
            m_rampPusher = new CANJaguar(Wiring.rampPushingMotorCANID);
        }
        catch (CANTimeoutException ex) {
            m_fault = true;
            System.err.println("CAN Init error: " + Wiring.rampPushingMotorCANID);
        }

        if(m_rampPusher != null) {
            try {
                m_rampPusher.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                m_rampPusher.configEncoderCodesPerRev(ticksPerRev);
                m_rampPusher.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            }
            catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN Timeout");
            }
        }

        m_limitSwitch = new DigitalInput(Wiring.rampPusherMainLimitSwitch);
        m_downSwitch = new DigitalInput(Wiring.rampPusherDownSwitch);

    }

    /**
     * Sets the power to a preset default down power.
     */
    public void down() {
        set(defaultDownPower);
    }

    /**
     * Sets the power to a preset default up power.
     */
    public void up() {
        set(defaultUpPower);
    }

    /**
     * Sets the power equal to 0.
     */
    public void stop() {
        set(0.0);
    }

    /**
     * This sets power equal to the variable "power".
     *
     * @param power
     */
    public void setPower(double power) {
        set(power);
    }

    /**
     * This is the method that actually sets the power.
     *
     * @param power
     */
    private void set(double power) {
        if(m_rampPusher != null) {
            try {
                m_rampPusher.setX(power);
            }
            catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN Timeout");
            }
        }
    }

    public boolean isLimitSwitchPressed() {
        return m_limitSwitch.get();
    }

    public boolean isDown() {
        return !m_downSwitch.get();
    }

    public double getCurrent() {
        double current = 0.0;

        if(m_rampPusher != null) {
            try {
                current = m_rampPusher.getOutputCurrent();
            }
            catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN Timeout");
            }
        }
        return current;
    }

    public boolean isCalibrated() {
        return m_isCalibrated;
    }

    public double getAngle() {
        return 360 * (getAngleRaw() - m_posnOffset);
    }

    public void resetAngle() {
        m_posnOffset = getAngleRaw();
        m_isCalibrated = true;
    }

    private double getAngleRaw() {
        double posn = 0.0;

        if(m_rampPusher != null) {
            try {
                posn = m_rampPusher.getPosition();
            }
            catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN Timeout");
            }
        }
        return posn;
    }

    public boolean getFault() {
        return m_fault;
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new RampPusherStop());
    }
}