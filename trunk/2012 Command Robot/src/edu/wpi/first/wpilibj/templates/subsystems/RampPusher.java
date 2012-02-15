/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author TJ2
 */
public class RampPusher extends Subsystem {

    public static void setPower(double m_power) {
    }
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private CANJaguar m_RampPusher;
    private int rampPusherPower;
    private int m_ID;

    public RampPusher(){
        try {
            m_RampPusher = new CANJaguar(Wiring.rampPushingMotorCANID);
            m_ID = Wiring.rampPushingMotorCANID;
        }
        catch (CANTimeoutException ex) {
            System.err.println("CAN Init error: ID " + m_ID);
        }

    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}