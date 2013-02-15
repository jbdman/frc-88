/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author David
 */
public class ClimberTop extends CommandBase {
    
    private double upperlimit = 6.05;
    private double lowerlimit = 5.95;
    private double count = 0;
    private static final int COUNTSTOP = 5;
    public ClimberTop() {
        super("ClimberTop");
        requires(climber);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    
    /**
     * This should set the climber to its tallest position. However, as of 2/3/12
     * has not been tested
     */
    protected void initialize() {
        //so far I don't know how to determine the actual # so its guess and check for now
        climber.tall();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if ((climber.getPosition() > upperlimit) && (climber.getPosition() < lowerlimit)) {
            count ++;
        }
        else {
            count = 0;
        }
        if (count==COUNTSTOP) {
         return true;   
        }
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
