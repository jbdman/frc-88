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
public class RampPusherSpeed extends CommandBase {

    private double m_power = 0.0;
    private static final double maxPositiveCurrent = 20.0;
    private static final double maxNegativeCurrent = 20.0;

    public RampPusherSpeed(double power) {
        super("RampMover(" + power + ")");
        requires(rampPusher);

        m_power = power;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        rampPusher.setPower(m_power);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return (rampPusher.getCurrent() > maxPositiveCurrent ||
                rampPusher.getCurrent() < maxNegativeCurrent);
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}