/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author Ag
 */
public class ClimberHome extends CommandBase {
    //For David- the lowerlimit is mounted higher on the robot...lower limit is when it is at the highest point
    private boolean movingDown = false;
    private boolean broken = false;
    
    public ClimberHome() {
        super ("ClimberHome");
        requires(climber);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    
    /**
     * This should calibrate the climber to start in relatively the same position every game
     * As of 2/3/12 has not been tested
     */
    protected void initialize() {
        if (climber.upperLimitTripped() && climber.lowerLimitTripped() ) {
            // If both the upper and lower sensors are tripped, something is wrong
            // So we print out the error and stop the climber.
            System.out.println("WIRE IS UNPLUGGED");
            climber.stop();
            broken = true;
            climber.setfault();
        } else {
            if (climber.lowerLimitTripped()){ 
                // If the lower limit is tripped, we need to move down before we
                // can know exactly where the climber is.
                climber.down();
                movingDown = true;
            } else {
                // Otherwise, we just need to move the climber up until it
                // trips the lower limit sensor.
                climber.up();
                movingDown = false;
            }
        }
    }
    

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (!climber.lowerLimitTripped() && movingDown) {
            // If we're moving down to clear the sensor and we have successfully
            // cleared the senser, we can start moving up to trip it again.
            climber.up();
            movingDown = false;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (climber.lowerLimitTripped() && !movingDown) {
            // If we're moving up to trip the sensor and we did trip it, we're done
            return true;
        }
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        climber.stop();
        climber.calibrateEncoder();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
