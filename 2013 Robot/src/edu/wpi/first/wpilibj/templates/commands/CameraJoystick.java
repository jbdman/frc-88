/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author TJ2
 */
public class CameraJoystick extends CommandBase {
    
    private double m_angle;
    private static final double JOYSTICK_SCALE = 10.0;
    
    public CameraJoystick() {
        super("CameraJoystick");
        requires(base);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        m_angle = base.getCameraAngle();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double change;
        
        change = -oi.getRightVerticalAxis();
        change = JOYSTICK_SCALE * change * Math.abs(change);
        
        if((change > 0 && m_angle < 255) || (change < 0 && m_angle > 0)) {
            m_angle += change;
            base.setCameraAngle(m_angle);
            SmartDashboard.putNumber("CameraAngle", base.getCameraAngle());
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
