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
     * Command for "homing" the tilter, starting it in the same position every time.
     * As of 2/3/12 does not work
     */
//this command cant be finished until the math for the correlation for angle to screw position
public class TilterHome extends CommandBase {

    private static final double TIMEOUT = 5.0;
    
    private boolean movehorizontal = false;
    public TilterHome() {
        super ("TilterHome");
        requires(tilter);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        setTimeout(TIMEOUT);
        boolean flag = tilter.LimitTripped();
        
        if (flag){ 
                tilter.down();
                movehorizontal = true;
            }
            
            else {
                tilter.up();
                movehorizontal = false;
            }
        }
    

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (!tilter.LimitTripped() && movehorizontal) {
            tilter.up();
            movehorizontal = false;
        }
       
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        boolean flag = isTimedOut();
        if (tilter.LimitTripped() && !movehorizontal) {
            flag=true;
        }
        return flag;
    }

    // Called once after isFinished returns true
    protected void end() {
        tilter.Tiltstop();
        tilter.calibrateEncoder();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        tilter.Tiltstop();
    }
}
