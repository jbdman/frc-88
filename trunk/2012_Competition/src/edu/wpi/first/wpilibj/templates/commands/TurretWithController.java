/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;


/**
 *
 * @author Michael_Edgington
 */
public class TurretWithController extends CommandBase {

    final double sensitivity = 0.4;

    public TurretWithController() {
        // Use requires() here to declare subsystem dependencies
        super("TurretWithController");
        requires(turret);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        turret.disable();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        double controller=oi.getTurretTurnStick();

        if(controller>0){
            controller=controller*controller;
        }

        else
            if(controller<0){
            controller=-controller*controller;
            }
        turret.setPower(sensitivity * controller);
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