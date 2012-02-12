/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.GearTooth;
import edu.wpi.first.wpilibj.templates.Wiring;

/**
 *
 * @author TJ2
 */
public class Pitcher extends Subsystem {

    /*
     * Member variables
     * 
     */
    private CANJaguar m_upperMotor;
    private CANJaguar m_lowerMotor;
    private GearTooth m_upperSpeedSensor;
    private GearTooth m_lowerSpeedSensor;

    private static final int teethPerGear = 11;
    private static final double stoppedPeriod = 0.1;

    //Here is the Constructor
    public Pitcher() {

        // create motor objects
        try {
            m_upperMotor = new CANJaguar(Wiring.pitcherUpperMotorCANID);
        } catch (CANTimeoutException ex) {
            System.err.println("CAN Init error: ID " + Wiring.pitcherUpperMotorCANID);
        }
        try {
            m_lowerMotor = new CANJaguar(Wiring.pitcherLowerMotorCANID);
        }
        catch (CANTimeoutException ex) {
            System.err.println("CAN Init error: ID " + Wiring.pitcherLowerMotorCANID);
        }
        try {
            m_upperMotor.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
            m_upperMotor.configEncoderCodesPerRev(11);
        }
        catch (CANTimeoutException ex) {
            System.err.println("CAN timeout (ID 2)");
        }
    }
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void setPower(double upperMotorPower, double lowerMotorPower) {

        try {
            m_upperMotor.setX(upperMotorPower);
            m_lowerMotor.setX(lowerMotorPower);
        } catch (CANTimeoutException ex) {
            System.err.println("CAN Timeout");
        }

    }

    public void setSpeed(int upperRPM, int lowerRPM) {
        // TBD
    }
    
    public int getSpeedUpper(){
        return rpm(m_upperSpeedSensor.getPeriod());
    }

    public int getSpeedLower(){
        return rpm(m_lowerSpeedSensor.getPeriod());
    }

    public void enable(){
        // TBD
    }

    public void disable(){
        //TBD
    }

    private int rpm(double period) {
        int rpm = 0;

        // calculate real RPM from time between gear teeth
        if(period < stoppedPeriod) {
            rpm = (int)(60.0 / (period * teethPerGear));
        }
        return rpm;
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}