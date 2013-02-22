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
                cameraServoLeft.setAngle(angle);
        }
    }
}
 
