/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;


/**
 *
 * @author Michael_Edgington
 */
public class FootUp extends CommandBase {

    private static final double footUpTime = 0.5;

    public FootUp() {
        // Use requires() here to declare subsystem dependencies
        super("FootUp");
        requires(foot);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        setTimeout(footUpTime);
        foot.up();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}