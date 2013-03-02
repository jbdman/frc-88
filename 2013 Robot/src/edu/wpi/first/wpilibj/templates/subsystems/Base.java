/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.ADXL345_I2C;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Ultrasonic;

import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.templates.commands.CameraController;

/**
 *
 * @author TJ2
 */
public class Base extends Subsystem {
    
    private Servo m_cameraServoLeft = null;
    private Servo m_cameraServoRight = null;
    private Ultrasonic m_rangeFinder = null;
    private DigitalInput m_leftTalon = null;
    private DigitalInput m_rightTalon = null;    
    private boolean m_fault = false;
    
    private ADXL345_I2C m_accelerometer;
    
    public Base() {
        // camera servos
        m_cameraServoLeft = new Servo(Wiring.cameraServoLeft);
        m_cameraServoRight = new Servo(Wiring.cameraServoRight);

        // talon sensors
        m_leftTalon = new DigitalInput(Wiring.leftTalonSensor);
        m_rightTalon = new DigitalInput(Wiring.rightTalonSensor);
        
        // ultrasonic range finder(s)
        m_rangeFinder = new Ultrasonic(Wiring.rangeFinderPing, Wiring.rangeFinderEcho);
        m_rangeFinder.setAutomaticMode(true);
        
        // add accelerometer
        m_accelerometer = new ADXL345_I2C(1, ADXL345_I2C.DataFormat_Range.k2G);
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        setDefaultCommand(new CameraController());
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    /**
     * Angle to set both cameras
     * 
     * @param angle new angle in degrees  
     */
    public void setCameraAngle(double angle) {
        if(m_cameraServoLeft != null) {
            m_cameraServoLeft.setAngle(angle);
        }
        if(m_cameraServoRight != null) {
            m_cameraServoRight.setAngle(angle);
        }
    }

    /**
     * Get the camera angle
     * 
     * @return average angle of both cameras 
     */
    public double getCameraAngle() {

        double angle = 0.0;
        
        angle += m_cameraServoLeft.getAngle();
        angle += m_cameraServoRight.getAngle();
        
        angle /= 2.0;

        return angle;
    }

    /**
     * Get the angle of the left camera
     * 
     * @return angle of the left camera
     */
    public double getLeftCameraAngle() {
        double angle = 0.0;
        if(m_cameraServoLeft != null) {
            angle = m_cameraServoLeft.getAngle();
        }
        return angle;
    }
    
    /**
     * Get the angle of the right camera
     * 
     * @return angle of the right camera in degrees
     */
    public double getRightCameraAngle() {
        double angle = 0.0;
        if(m_cameraServoRight != null) {
            angle = m_cameraServoRight.getAngle();
        }
        return angle;
    }

    /**
     * Get the distance from the rangefinder, or -1 for no distance
     * 
     * @return rangefinder distance in inches
     */
    public double getRangeFinderDist() {
        double dist = -1.0;

        if(m_rangeFinder.isRangeValid()) {
            dist = m_rangeFinder.getRangeInches();
        }
        return dist;
    }

    /**
     * Status of left talon sensor
     * 
     * @return true if talon closed/engaged 
     */
    public boolean getLeftTalon() {
        return m_leftTalon.get();
    }

    /**
     * Status of right talon sensor
     * 
     * @return true if talon closed/engaged 
     */
    public boolean getRightTalon() {
        return m_rightTalon.get();
    }
    
    public double getGravity() {
        return m_accelerometer.getAcceleration(ADXL345_I2C.Axes.kZ);
    }
    
    /**
     * Returns the fault status of the subsystem
     * 
     * @return subsystem fault status
     */
    public boolean getFault() {
        return m_fault;
    }
} 
