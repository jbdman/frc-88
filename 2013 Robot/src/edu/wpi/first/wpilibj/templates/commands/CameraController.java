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
public class CameraController extends CommandBase {
    
    private double m_angle;
    private static final double JOYSTICK_SCALE = 3.0;
    
    public CameraController() {
        super("CameraController");
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
        double change = 0.0;
        
        if(oi.getDriveTrigger() < 0) {
                change = 1.0;
        } else if(oi.getDriveRightBumper()) {
            change = -1.0;
        }
        change = JOYSTICK_SCALE * change;
        
        if((change > 0.0 && m_angle < 255) || (change < 0.0 && m_angle > 0)) {
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
