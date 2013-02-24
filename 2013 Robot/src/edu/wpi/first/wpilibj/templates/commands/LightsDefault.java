/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.DriverStation;

/**
 *
 * @author Ag
 */
public class LightsDefault extends CommandBase {
    
    public LightsDefault() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(lights);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (DriverStation.getInstance().isAutonomous()) {
            if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.kBlue) {
                // We're in auto and we're on the blue alliance.
                lights.setMode(lights.MODE_BLUE_ALLIANCE);
            }
            else {
                // Otherwise, we must be on the red alliance.
                lights.setMode(lights.MODE_RED_ALLIANCE);
            }
        } else if (DriverStation.getInstance().isOperatorControl()) {
            // We're in teleop
            // Need to set getting values for the analog input from drive motors
            lights.setMode(lights.MODE_BLINKY);
        } else if (DriverStation.getInstance().isDisabled()) {
            // We're disabled.  This should trigger at the end of the match.
            lights.setMode(lights.MODE_FLASH_GREEN);
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
