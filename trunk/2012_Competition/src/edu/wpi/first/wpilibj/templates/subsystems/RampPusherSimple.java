
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
import edu.wpi.first.wpilibj.DigitalInput;
/**
 *
 * @author TJ2
 */
public class RampPusherSimple extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private CANJaguar m_rampPusher = null;
    private boolean m_fault = false;

    private DigitalInput m_limitSwitch;
    private DigitalInput m_downSwitch;

    private static final double defaultDownPower = 1.0;
    private static final double defaultUpPower = -0.5;

    public static final double maxDownCurrent = 20.0;
    public static final double maxUpCurrent = 10.0;

    public RampPusherSimple(){
        try {
            m_rampPusher = new CANJaguar(Wiring.rampPushingMotorCANID);
        }
        catch (CANTimeoutException ex) {
            m_fault = true;
            System.err.println("CAN Init error: " + Wiring.rampPushingMotorCANID);
        }

        m_limitSwitch = new DigitalInput(Wiring.rampPusherMainLimitSwitch);
        m_downSwitch = new DigitalInput(Wiring.rampPusherDownSwitch);

    }
    public boolean isUpLimitSwitchPressed(){
        return m_limitSwitch.get();
    }

   public boolean isLimitSwitchPressed() {
        return m_downSwitch.get();
    }

    public void down() {
        set(defaultDownPower);
    }

    public void up() {
        set(defaultUpPower);
    }

    public void stop() {
        set(0.0);
    }

    public void setPower(double power) {
        set(power);
    }

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

    public boolean getFault(){
        return m_fault;
    }
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new RampPusherStop());
    }
}