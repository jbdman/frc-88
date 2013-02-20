/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author Ag
 */

    /**
     * Command for the making the Tilter go to its tallest position.
     * Making the climber closer to horizontal
     */
public class TilterClimbAngle extends CommandBase {
    
    private double upperlimit = 4.05;
    private double lowerlimit = 3.95;
    private double count = 0;
    private static final int COUNTSTOP = 5;
    
    public TilterClimbAngle() {
        super("TilterPosSetMax");
        requires(tilter);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        tilter.climb();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if ((tilter.getAngle() > upperlimit) && (tilter.getAngle() < lowerlimit)) {
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
