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
        if(rampPusher.isCalibrated()) {
            state = kNormal;
        } else {
            if(rampPusher.isLimitSwitchPressed()) {
                state = kCalibrateDown;
            } else {
                state = kCalibrateUp;
            }
        }
        if(state == kCalibrateDown) {
            rampPusher.down();
        } else if(state == kCalibrateUp) {
            rampPusher.up();
        } else if(rampPusher.getAngle() > 30 || !rampPusher.isLimitSwitchPressed()) {
            rampPusher.up();
        }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(state == kCalibrateDown && !rampPusher.isLimitSwitchPressed()) {
            state = kCalibrateUp;
            rampPusher.up();
        }
        if(state == kCalibrateUp && rampPusher.isLimitSwitchPressed()) {
            state = kNormal;
            rampPusher.resetAngle();
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        boolean done = false;
//        FIX - takes a fraction of a second to check angle - so better to have a slowly
//        checked variable for "nearTop"
        if(state == kNormal && rampPusher.getAngle() < 30 && rampPusher.isLimitSwitchPressed()) {
            done = true;
        }
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