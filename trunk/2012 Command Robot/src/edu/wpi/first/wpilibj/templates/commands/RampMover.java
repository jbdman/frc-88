/*  To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.subsystems.RampPusher;

/**
 *
 * @author TJ2
 */
public class RampMover extends CommandBase {

    double m_power = 0.0;

    public RampMover(double power) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        super("RampMover(" + power + ")");
        Subsystem RampPusher = null;
        requires(RampPusher);
        m_power = power;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        RampPusher.setPower(m_power);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

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