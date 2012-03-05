/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;


/**
 *
 * @author Michael_Edgington
 */
public class PitcherSpeed extends CommandBase {

    private double m_upperRPM;
    private double m_lowerRPM;

    private static final double settlingTime = 1.0; // should be replaced with checking for speeds

    public PitcherSpeed(double upperRPM, double lowerRPM) {
        // Use requires() here to declare subsystem dependencies
        super("PitcherSpeed(" + upperRPM + ", " + lowerRPM + ")");
        requires(pitcher);

        m_upperRPM = upperRPM;
        m_lowerRPM = lowerRPM;
    }

    public PitcherSpeed(double averageRPM) {
        // Use requires() here to declare subsystem dependencies
        super("PitcherSpeed(" + averageRPM + ")");
        requires(pitcher);

        m_upperRPM = averageRPM - 250;
        m_lowerRPM = averageRPM + 250;
    }

    public PitcherSpeed() {
        // Use requires() here to declare subsystem dependencies
        super("PitcherSpeed");
        requires(pitcher);

        double averageRPM = pitcher.getAverageSpeedSetpoint();
        m_upperRPM = averageRPM - 250;
        m_lowerRPM = averageRPM + 250;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        pitcher.setSpeed(m_upperRPM, m_lowerRPM);
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