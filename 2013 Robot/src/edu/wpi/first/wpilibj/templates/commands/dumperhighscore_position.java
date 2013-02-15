/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author David
 */

    /**
     * Closed loop command for setting the dumper to score on the top of the pyramid.
     * As of 2/3/13 wont work
     */
public class dumperhighscore_position extends CommandBase {
    
    private double upperlimit = 3.05;
    private double lowerlimit = 2.95;
    private double count = 0;
    private static final int COUNTSTOP = 5;
    
    public dumperhighscore_position() {
        super ("dumper is in feed position");
        requires (dumper);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        dumper.highScorePosition();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
         if ((dumper.getPosition() > upperlimit) && (dumper.getPosition() < lowerlimit)) {
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
