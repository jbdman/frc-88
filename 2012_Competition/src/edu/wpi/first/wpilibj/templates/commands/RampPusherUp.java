/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;


/**
 *
 * @author Michael_Edgington
 */
public class RampPusherUp extends CommandBase {

    private final int kUndefined = 0;
    private final int kCalibrateDown = 1;
    private final int kCalibrateUp = 2;
    private final int kNormal = 3;

    private int state = kUndefined;

    public RampPusherUp() {
        super("RampPusherUp");
        requires(rampPusher);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
//        if(!rampPusher.isLimitSwitchPressed() || rampPusher.isDown()) {
//            rampPusher.up();
//        }
        if(!rampPusher.isLimitSwitchPressed()) {
            rampPusher.up();
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        boolean done = rampPusher.isLimitSwitchPressed();
//        if(rampPusher.isLimitSwitchPressed() && !rampPusher.isDown()) {
//            done = true;
//        }
        return done || Math.abs(rampPusher.getCurrent()) > rampPusher.maxUpCurrent;
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