/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Ultrasonic;

import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.templates.commands.CameraJoystick;

/**
 *
 * @author TJ2
 */
public class Base extends Subsystem {
    
    Servo cameraServoLeft = null;
    Servo cameraServoRight = null;
    Ultrasonic rangeFinder = null;
    private boolean m_fault = false;
    
    public Base() {
          cameraServoLeft = new Servo(Wiring.CameraServoLeft);
          cameraServoRight = new Servo(Wiring.CameraServoRight);

          rangeFinder = new Ultrasonic(Wiring.RangeFinderPing, Wiring.RangeFinderEcho);
          rangeFinder.setAutomaticMode(true);
    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        setDefaultCommand(new CameraJoystick());
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }

    /**
     * Angle to set both cameras
     * 
     * @param angle new angle in degrees  
     */
    public void setCameraAngle(double angle) {
        if(cameraServoLeft != null) {
                cameraServoLeft.setAngle(angle);
        }
        if(cameraServoRight != null) {
                cameraServoRight.setAngle(angle);
        }
    }

    /**
     * Get the camera angle
     * 
     * @return average angle of both cameras 
     */
    public double getCameraAngle() {

        double angle = 0.0;
        
        angle += cameraServoLeft.getAngle();
        angle += cameraServoRight.getAngle();
        
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
        if(cameraServoLeft != null) {
            angle = cameraServoLeft.getAngle();
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
        if(cameraServoRight != null) {
            angle = cameraServoRight.getAngle();
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

        if(rangeFinder.isRangeValid()) {
            dist = rangeFinder.getRangeInches();
        }
        return dist;
    }

} 
