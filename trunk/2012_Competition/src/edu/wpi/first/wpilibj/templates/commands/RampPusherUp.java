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

    public RampPusherUp() {
        super("RampPusherUp");
        requires(rampPusherSimple);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
//        if(!rampPusher.isLimitSwitchPressed() || rampPusher.isDown()) {
//            rampPusher.up();
//        }

            rampPusherSimple.up();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Called once after isFinished returns true
    protected void end() {
        rampPusherSimple.stop();
    }
    public boolean isFinished(){
        if(Math.abs(rampPusherSimple.getCurrent()) > rampPusherSimple.maxUpCurrent||
                (rampPusherSimple.isUpLimitSwitchPressed())==true){
            return true;
        }
        else{
            return false;
        }

    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}