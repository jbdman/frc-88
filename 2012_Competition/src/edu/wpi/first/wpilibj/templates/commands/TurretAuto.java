/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.templates.subsystems.Tracking;
import edu.wpi.first.wpilibj.templates.subsystems.Turret;

/**
 *
 * @author Michael_Edgington
 */
public class TurretAuto extends CommandBase {

    public TurretAuto() {
        // Use requires() here to declare subsystem dependencies
        super("TurretAuto");
        requires(turret);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        turret.enable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(tracker.foundTarget()) {
            if(tracker.isNewTarget()) {
                double deltaAngle = tracker.getTargetAngle();
                turret.setDeltaAngle(deltaAngle);
                // should set pitcher speed based on distance...
            
            }
        } else {
            turret.setPower(0.0);
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