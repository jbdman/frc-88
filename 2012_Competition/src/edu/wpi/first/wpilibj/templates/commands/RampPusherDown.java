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
        requires(rampPusherSimple);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
//        if(!rampPusher.isLimitSwitchPressed() || !rampPusher.isDown()) {
//            rampPusher.down();
//        }

            rampPusherSimple.down();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

            rampPusherSimple.down();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
//        boolean done = false;
//        done = rampPusher.isDown();
        return Math.abs(rampPusherSimple.getCurrent()) > rampPusherSimple.maxDownCurrent;
    }

    // Called once after isFinished returns true
    protected void end() {
        rampPusherSimple.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}