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

    double m_upperRPM;
    double m_lowerRPM;

    public PitcherSpeed(double upperRPM, double lowerRPM) {
        // Use requires() here to declare subsystem dependencies
        super("PitcherSpeed(" + upperRPM + ", " + lowerRPM + ")");
        requires(wheelSpinner);

        m_upperRPM = upperRPM;
        m_lowerRPM = lowerRPM;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        wheelSpinner.setSpeed(m_upperRPM, m_lowerRPM);
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