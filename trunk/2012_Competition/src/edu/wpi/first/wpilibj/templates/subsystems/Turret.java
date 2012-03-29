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
// For camera tracking
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;
import edu.wpi.first.wpilibj.image.ColorImage;
import edu.wpi.first.wpilibj.image.BinaryImage;
import edu.wpi.first.wpilibj.image.ParticleAnalysisReport;
import edu.wpi.first.wpilibj.image.CriteriaCollection;
import edu.wpi.first.wpilibj.image.NIVision.MeasurementType;
import edu.wpi.first.wpilibj.image.NIVisionException;

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
    private double posnOffset = 0.0;

    // camera & tracking
    private AxisCamera camera;
    private CriteriaCollection filterCriteria;

    private boolean findLowermostTarget = false;

    private double m_targetAngle = 0.0;
    private double m_targetDistance = 0.0;
    private boolean m_isNewTarget = false;
    private boolean m_foundTarget = false;
    private int lastX = 0;
    private int lastY = 0;
    private int lastH = 0;

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
//            m_turretMotor.setPID(500.0, 2.0, 1.0);
            m_turretMotor.setPID(300.0, 2.0, 1.0);
        } catch (CANTimeoutException ex) {
            m_fault = true;
            System.err.println("##### CAN Timeout #####");
        }

        m_limitSwitch = new DigitalInput(Wiring.turretLimitSwitch);

        // configure camera
        camera = AxisCamera.getInstance();
        camera.writeWhiteBalance(AxisCamera.WhiteBalanceT.fixedFlour1);
        camera.writeResolution(AxisCamera.ResolutionT.k320x240);
        camera.writeBrightness(20);
        filterCriteria = new CriteriaCollection();
        filterCriteria.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_WIDTH, 20, 200, false);
        filterCriteria.addCriteria(MeasurementType.IMAQ_MT_BOUNDING_RECT_HEIGHT, 15, 200, false);
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
                m_turretMotor.setX(-power);
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

    /**
     * This is the image processing software.
     * It determines weather there is a new image, and then does some imge processing to determine the
     */
    public void processImage() {

        final int HUE_LO = 80;
        final int HUE_HI = 110;
        final int SAT_LO = 50;
        final int SAT_HI = 255;
        final int LUM_LO = 35;
        final int LUM_HI = 100;
        final double minBoundingRectArea = 100;

        if (camera.freshImage()) {

            try {
                double turretAngle = this.getAngle();
                ColorImage image = camera.getImage();
                BinaryImage colorMask = image.thresholdHSL(HUE_LO, HUE_HI, SAT_LO, SAT_HI, LUM_LO, LUM_HI);
                BinaryImage erodedImage = colorMask.removeSmallObjects(false, 2);
                BinaryImage convexHullImage = erodedImage.convexHull(false);
//                BinaryImage filteredImage = convexHullImage.particleFilter(filterCriteria);
                ParticleAnalysisReport p;

//                ParticleAnalysisReport[] particles = filteredImage.getOrderedParticleAnalysisReports(4);
                ParticleAnalysisReport[] particles = convexHullImage.getOrderedParticleAnalysisReports();

                m_foundTarget = false;
                if(particles.length > 0) {
                    // find highest target (i.e. lowest y)
                    int index = -1;
                    double bestY = 1.0;
                    if(findLowermostTarget) {
                        bestY = -1.0;
                    }
                    for(int i = 0; i < particles.length; i++) {
                        p = particles[i];
                        if(p.boundingRectHeight * p.boundingRectWidth > minBoundingRectArea) {
                            if(findLowermostTarget) {
                                if(p.center_mass_y_normalized > bestY) {
                                    index = i;
                                    bestY = p.center_mass_y_normalized;
                                }
                            } else {
                                if(p.center_mass_y_normalized < bestY) {
                                    index = i;
                                    bestY = p.center_mass_y_normalized;
                                }
                            }
                        }
                    }
                    if(index >= 0) {
                        // report target angle and height->distance
                        p = particles[index];

                        lastX = p.center_mass_x;
                        lastY = p.center_mass_y;
                        lastH = p.boundingRectHeight;
                        double angle = p.center_mass_x_normalized * 22; // FIX THE MAGIC NUMBER!
                        double distance = 480.0/p.boundingRectHeight; // FIX THE MAGIC NUMBER!

                        m_foundTarget = true;
                        m_isNewTarget = true;
                        m_targetAngle = turretAngle + angle;
                        m_targetDistance = distance;
                    }
                }
//                filteredImage.free();
                convexHullImage.free();
                erodedImage.free();
                colorMask.free();
                image.free();


            } catch (NIVisionException ex) {
                m_fault = true;
                System.out.println("Camera NI exception");
            } catch (AxisCameraException ex) {
                m_fault = true;
                System.out.println("Camera getImage() exception");
            }
        }

    }

    public void setFindLowestTarget(boolean value) {
        findLowermostTarget = value;
    }

    public boolean isNewTarget() {
        return m_isNewTarget;
    }

    public boolean foundTarget() {
        return m_foundTarget;
    }

    public double getTargetAngle() {
        m_isNewTarget = false;
        return m_targetAngle;
    }

    public double getTargetDistance() {
        m_isNewTarget = false;
        return m_targetDistance;
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