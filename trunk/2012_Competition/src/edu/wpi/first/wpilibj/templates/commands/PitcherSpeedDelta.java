/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author Dr. Edgington
 */
public class PitcherSpeedDelta extends CommandBase {

    private double m_speedDelta;
    private static final double settlingTime = 0.25; // should be replaced with checking for speeds

    public PitcherSpeedDelta(double delta) {
        // Use requires() here to declare subsystem dependencies
        super("PitcherSpeedDelta(" + delta + ")");
        requires(pitcher);

        m_speedDelta = delta;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        double speed = pitcher.getAverageSpeedSetpoint();
        pitcher.setAverageSpeed(speed + m_speedDelta);
        setTimeout(settlingTime);

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}