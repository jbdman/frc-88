/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.templates.subsystems.DriveTrain;

/**
 *
 * @author Michael_Edgington
 */
public class DriveWithKinect extends CommandBase {

    public DriveWithKinect() {
        // Use requires() here to declare subsystem dependencies
        super("DriveWithKinect");
        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        driveTrain.driveTank(oi.getKinectLeftStick(),
                             oi.getKinectRightStick());
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