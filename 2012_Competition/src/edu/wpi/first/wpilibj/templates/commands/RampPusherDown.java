/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;


/**
 *
 * @author TJ2
 */
public class RampPusherDown extends CommandBase {

    public RampPusherDown() {
        // Use requires() here to declare subsystem dependencies
        super("RampPusherDown");
        requires(rampPusher);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        rampPusher.down();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        boolean done = false;
        if(!rampPusher.isCalibrated()) {
            done = rampPusher.isLimitSwitchPressed();
        } else {
            done = (rampPusher.getAngle() > 30 && rampPusher.isLimitSwitchPressed());
        }
        
        return done || Math.abs(rampPusher.getCurrent()) > rampPusher.maxDownCurrent;
    }

    // Called once after isFinished returns true
    protected void end() {
        rampPusher.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}