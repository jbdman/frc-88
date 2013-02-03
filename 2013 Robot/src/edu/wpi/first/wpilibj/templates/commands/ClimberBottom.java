/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author David
 */
//command wont work 2/2/12
public class ClimberBottom extends CommandBase {
    
    public ClimberBottom() {
        super("ClimberBottom");
        requires(climber);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    /**
     * This is suppose to set the lowest position for the climber. As of 2/3/12 has not been tested.
     */
    protected void initialize() {
        climber.ClimbClosedLoop(-6);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
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
