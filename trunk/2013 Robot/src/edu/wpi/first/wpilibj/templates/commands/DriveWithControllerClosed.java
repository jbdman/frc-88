/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.templates.subsystems.Drive;
/**
 *
 * @author David
 */

    /**
     * Command for driving the robot
     */
public class DriveWithControllerClosed extends CommandBase {
    
//    private static final double BRAKE_THRESH = 0.5;
    private static final double MAX_SPEED = 80.0;
    private static final double CLOSED_LOOP_RAMPRATE = 10.0;

    // used for ramp rate limit
    private double m_lastLeft = 0.0;
    private double m_lastRight = 0.0;
    private boolean m_brake = false;
    
//    private double last_left = 0.0;
//    private double last_right = 0.0;
    
    public DriveWithControllerClosed() {
        super("DriveWithControllerClosed");
        requires(drive);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        if(!drive.isClosedLoop()) {
            drive.enableClosedLoop();
        }
        drive.setBrake(m_brake);
    }

    // Called repeatedly when this Command is scheduled to run
    
    /**
     * Part that drives it
     */
    protected void execute() {
        
        boolean nextBrake = false;
        
        // read driver buttons/triggers to determine if breaking
        if(oi.getDriveLeftBumper()) {
            nextBrake = true;
        }
        
        // change braking state only if necessary
        if(m_brake != nextBrake) {
            m_brake = nextBrake;
            drive.setBrake(m_brake);
        }
        
        // drive the robot based on driver sticks
        double left = oi.getDriveLeftVerticalAxis();
        double right = oi.getDriveRightVerticalAxis();
        left = MAX_SPEED * left;
        right = MAX_SPEED * right;

        // limit the change in setpoint between calls
        // this is not a true ramp rate since we don't correct for the rate of update
        if(left - m_lastLeft > CLOSED_LOOP_RAMPRATE) {
            left = m_lastLeft + CLOSED_LOOP_RAMPRATE;
        } else if(left - m_lastLeft < -CLOSED_LOOP_RAMPRATE) {
            left = m_lastLeft - CLOSED_LOOP_RAMPRATE;
        }
        if(right - m_lastRight > CLOSED_LOOP_RAMPRATE) {
            right = m_lastRight + CLOSED_LOOP_RAMPRATE;
        } else if(right - m_lastRight < -CLOSED_LOOP_RAMPRATE) {
            right = m_lastRight - CLOSED_LOOP_RAMPRATE;
        }

        drive.driveTankClosedLoop(left, right);
        m_lastLeft = left;
        m_lastRight = right;
        
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
