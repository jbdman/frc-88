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
public class DrivewithController extends CommandBase {
    
    private static final double BRAKE_THRESH = 0.5;
    private boolean m_brake = false;
    
    public DrivewithController() {
        super("DriveWithController");
        requires(drive);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        drive.setBrake(m_brake);
    }

    // Called repeatedly when this Command is scheduled to run
    
    /**
     * Part that drives it
     */
    protected void execute() {
        
        boolean nextBrake = false;
        
        // read driver buttons/triggers to determine if breaking
        if(Math.abs(oi.getDriveTrigger())> BRAKE_THRESH) {
            nextBrake = true;
        }
        
        // change braking state only if necessary
        if(m_brake != nextBrake) {
            m_brake = nextBrake;
            drive.setBrake(m_brake);
        }
        
        // drive the robot based on driver sticks
        drive.driveTankOpenLoop(oi.getFwdLeftStick(),
                                  oi.getFwdRightStick());
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
