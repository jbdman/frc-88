/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.templates.commands.CameraJoystick;
/**
 *
 * @author TJ2
 */
public class CameraControl extends Subsystem {
    
    Servo Servo1 = null;
    Servo Servo2 = null;
    private boolean m_fault = false;
    
    public CameraControl() {
          Servo1 = new Servo(Wiring.Servo1ID);
          Servo2 = new Servo(Wiring.Servo2ID);

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
    public void setAngle(double angle) {
        if(Servo1 != null) {
                Servo1.setAngle(angle);
        }
        if(Servo2 != null) {
                Servo2.setAngle(angle);
        }    

    }
}
 
