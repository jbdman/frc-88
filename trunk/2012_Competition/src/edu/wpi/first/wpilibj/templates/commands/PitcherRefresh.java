/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;


/**
 *
 * @author Michael_Edgington
 */
public class PitcherRefresh extends CommandBase {

    // refresh rate in seconds
    private final double refreshRate = 1.0;

    public PitcherRefresh() {
        // Use requires() here to declare subsystem dependencies
        super("PitcherRefresh");
        requires(pitcher);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        setTimeout(refreshRate);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(isTimedOut()) {
            pitcher.refreshSpeed();
            setTimeout(refreshRate);
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