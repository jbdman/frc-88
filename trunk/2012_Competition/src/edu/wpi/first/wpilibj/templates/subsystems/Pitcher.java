/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.templates.commands.PitcherRefresh;

/**
 *
 * @author TJ2
 */
public class Pitcher extends Subsystem {


    // Member variables

    private CANJaguar m_upperMotor = null;
    private CANJaguar m_lowerMotor = null;
    private double m_averageSpeedSetpoint = 0.0;
    private static final double defaultSpeedDelta = 300;

    private double m_upperSetPoint = 0.0;
    private double m_lowerSetPoint = 0.0;

    private boolean m_fault = false;

    private static final int teethPerGearUpper = 10;
    /*************************************************************
     * NOTE: PRACTICE BOT HAS 11 teeth in lower sensing sprocket *
     *       COMPETITION ROBOT HAS 10                            *
     *************************************************************/
    private static final int teethPerGearLower = 10;

    //Here is the Constructor
    public Pitcher() {

        // create motor objects
        try {
            m_upperMotor = new CANJaguar(Wiring.pitcherUpperMotorCANID);
        } catch (CANTimeoutException ex) {
            m_fault = true;
            System.err.println("CAN Init error: ID " + Wiring.pitcherUpperMotorCANID);
        }
        try {
            m_lowerMotor = new CANJaguar(Wiring.pitcherLowerMotorCANID);
        }
        catch (CANTimeoutException ex) {
            m_fault = true;
            System.err.println("CAN Init error: ID " + Wiring.pitcherLowerMotorCANID);
        }

        // set up speed reference encoders
        if(m_upperMotor != null) {
            try {
                m_upperMotor.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
                m_upperMotor.configNeutralMode(CANJaguar.NeutralMode.kCoast);
                m_upperMotor.configEncoderCodesPerRev(teethPerGearUpper);
            }
            catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN timeout");
            }
        }

        if(m_lowerMotor != null) {
            try {
                m_lowerMotor.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
                m_lowerMotor.configNeutralMode(CANJaguar.NeutralMode.kCoast);
                m_lowerMotor.configEncoderCodesPerRev(teethPerGearLower);
            }
            catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN timeout");
            }
        }
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    public void enable() {

        if(m_upperMotor != null) {
            try {
                m_upperMotor.changeControlMode(CANJaguar.ControlMode.kSpeed);
                m_upperMotor.setPID(1.0, 0.005, 0.0);
                m_upperMotor.enableControl();
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN timeout");
            }
        }
        if(m_lowerMotor != null) {
            try {
                m_lowerMotor.changeControlMode(CANJaguar.ControlMode.kSpeed);
                m_lowerMotor.setPID(1.0, 0.005, 0.0);
                m_lowerMotor.enableControl();
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN timeout");
            }
        }
    }
    /**
     *
     * @param averageRPM
     */
    public void setAverageSpeed(double averageRPM) {
        setSpeed(averageRPM - defaultSpeedDelta, averageRPM + defaultSpeedDelta);
    }

    /**
     *
     */
    public void setSpeed(double upperRPM, double lowerRPM) {

        if(upperRPM < 0.0) {
            upperRPM = 0.0;
        }
        if(lowerRPM < 0.0) {
            lowerRPM = 0.0;
        }

        m_upperSetPoint = upperRPM;
        m_lowerSetPoint = lowerRPM;
        m_averageSpeedSetpoint = (upperRPM + lowerRPM)/2;

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

    /**
     * "Reminds" the pitcher by refreshing the same setpoints
     */
    public void refreshSpeed() {
        setSpeed(m_upperSetPoint, m_lowerSetPoint);
    }

    /**
     *
     * @return averageSpeedSetpoint
     */
    public double getAverageSpeedSetpoint() {
        return m_averageSpeedSetpoint;
    }

    /**
     *
     * @return AverageSpeed of the upper and lower spinners
     */
    public double getAverageSpeed() {
        return (getSpeedUpper() + getSpeedLower())/2;
    }

    /**
     *
     * @return SpeedOfUpperWheels
     */
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

    /**
     *
     * @return SpeedOfLowerWheels
     */
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

    /**
     *
     * @return currentDrawnByUpper
     */
    public double getCurrentUpper(){
        double current = 0.0;
        if(m_upperMotor != null) {
            try {
                current = m_upperMotor.getOutputCurrent();
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN Timeout");
            }
        }
        return current;
    }

    /**
     * 
     * @return currentDrawnByLower
     */
    public double getCurrentLower(){
        double current = 0.0;
        if(m_lowerMotor != null) {
            try {
                current = m_lowerMotor.getOutputCurrent();
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN Timeout");
            }
        }
        return current;
    }

    public boolean getFault() {
        return m_fault;
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new PitcherRefresh());
    }
}