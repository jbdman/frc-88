/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author tj2
 */
public class Elevator {

    CANJaguar m_jag;
    int m_id;

    double m_power;
    // assumption is that elevator is at top when it
    // starts to stall on upward command
    boolean m_atTop;
    boolean m_freefall;

    public Elevator(int CANId) {
        try {
            m_jag = new CANJaguar(CANId, CANJaguar.ControlMode.kPercentVbus);
        } catch (CANTimeoutException e) {
            System.err.println("CAN " + CANId + " init failure");
        }
        m_id = CANId;
        if(m_jag != null) {
            try {
                m_jag.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            } catch (CANTimeoutException e) {
                System.err.println("CAN " + m_id + " timeout");
            }
        }
        m_power = 0.0;
        m_atTop = false;
        m_freefall = false;
    }

    public void configEncoder(int codesPerRev) {
        if(m_jag != null) {
            try {
                m_jag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                m_jag.configEncoderCodesPerRev(codesPerRev);
            } catch (CANTimeoutException e) {
                System.err.println("CAN " + m_id + " timeout");
            }
        }
    }

    public void up(double power) {

        power = (power < 0.0)? -power: power;

        if(m_jag != null) {
            try {
                m_jag.setX(power);
            } catch (CANTimeoutException e) {
                System.err.println("CAN " + m_id + " timeout");
            }
        }
        m_power = power;
        // check at top?
    }

    public double getCurrent() {
        double current = 0.0;
        if(m_jag != null) {
            try {
                m_jag.getOutputCurrent();
            } catch (CANTimeoutException e) {
                System.err.println("CAN " + m_id + " timeout");
            }
        }
        return current;
    }

    public void freefall(boolean fall) {
        if(m_freefall != fall) {
            if(m_jag != null) {
                try {
                    if(fall) {
                        m_jag.configNeutralMode(CANJaguar.NeutralMode.kCoast);
                    } else {
                        m_jag.configNeutralMode(CANJaguar.NeutralMode.kBrake);
                    }
                } catch (CANTimeoutException e) {
                    System.err.println("CAN timeout (elevator)");
                }
            }
            m_freefall = fall;
        }
    }

    public void set(double power) {

        if(power != m_power) {
            if(m_jag != null) {
                try {
                    m_jag.setX(power);
                } catch (CANTimeoutException e) {
                    System.err.println("CAN " + m_id + " timeout");
                }
            }
            m_power = power;
        }
//        if(power > 0.0) {
//            if(m_jag != null) {
//                try {
//                    amps = m_jag.getOutputCurrent();
//                } catch (CANTimeoutException e) {
//                    System.err.println("CAN " + m_id + " timeout");
//                }
//            }
//
//        }
    }

}
