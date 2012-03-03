/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.templates.commands.TurretWithController;
import edu.wpi.first.wpilibj.templates.commands.TurretAuto;
// for debugging
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author Mike Edgington
 */
public class Turret extends Subsystem {


    // Will become PID subsystem very soon...

    private CANJaguar m_turretMotor = null;
    private DigitalInput m_limitSwitch;
    private boolean m_fault = false;

    // ratio between drive wheel posn and turret angle
    private static final double driveRatio = 80.0;
    // offset for angle calculation
    private double posnOffset = 0.0;

    public Turret() {
        // configure turret motor
        try {
            m_turretMotor = new CANJaguar(Wiring.turretMotorCANID);
        } catch (CANTimeoutException ex) {
            m_fault = true;
            System.err.println("##### CAN Init Failure ID: " + Wiring.turretMotorCANID);
        }
        try {
            m_turretMotor.configEncoderCodesPerRev(360);
            m_turretMotor.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            m_turretMotor.changeControlMode(CANJaguar.ControlMode.kPosition);
            m_turretMotor.setPID(500.0, 2.0, 1.0);
        } catch (CANTimeoutException ex) {
            m_fault = true;
            System.err.println("##### CAN Timeout #####");
        }

        m_limitSwitch = new DigitalInput(Wiring.turretLimitSwitch);

    }

    public void enable() {
        if(m_turretMotor != null) {
            try {
                m_turretMotor.changeControlMode(CANJaguar.ControlMode.kPosition);
                m_turretMotor.setPID(500.0, 2.0, 1.0);
                m_turretMotor.enableControl();
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("##### CAN Timeout ####");
            }
        }
    }

    public void disable() {
        if(m_turretMotor != null) {
            try {
                m_turretMotor.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                m_turretMotor.disableControl();
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("##### CAN Timeout ####");
            }
        }
    }

    public void setDeltaAngle(double deltaAngle) {
        double deltaPosn = deltaAngle / driveRatio;
        SmartDashboard.putDouble("deltaposn", deltaPosn);
        if(m_turretMotor != null) {
            try {
                double posn = m_turretMotor.getPosition();
                m_turretMotor.setX(posn + deltaPosn);
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("##### CAN Timeout ####");
            }
        }
    }

    public void setAngle(double angle) {
        double posn = (angle / driveRatio) - posnOffset;
        if(m_turretMotor != null) {
            try {
                m_turretMotor.setX(posn);
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("##### CAN Timeout ####");
            }
        }
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
                angle = (m_turretMotor.getPosition() + posnOffset) * driveRatio;
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("##### CAN Timeout ####");
            }
        }
        return angle;
    }

    public boolean isLimitSwitchPressed() {
        return !m_limitSwitch.get();
    }

    public boolean getFault() {
        return m_fault;
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new TurretWithController());
//        setDefaultCommand(new TurretAuto());
    }
}