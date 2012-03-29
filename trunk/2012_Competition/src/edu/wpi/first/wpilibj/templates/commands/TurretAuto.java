/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.templates.subsystems.Turret;

/**
 *
 * @author Michael_Edgington
 */
public class TurretAuto extends CommandBase {

    /**************************
     * FUDGE FACTOR IN DEGREES
     **************************/
    private static final double fudgeFactorAngle = 6.0;

    public TurretAuto() {
        // Use requires() here to declare subsystem dependencies
        super("TurretAuto");
        requires(turret);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        turret.enable();
        turret.setFindLowestTarget(true);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(turret.foundTarget()) {
            if(turret.isNewTarget()) {
                double angle = turret.getTargetAngle();
                turret.setAngle(angle + fudgeFactorAngle);
                // should set pitcher speed based on distance...
            }
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