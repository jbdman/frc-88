/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;


/**
 *
 * @author TJ2
 */
public class DriveAuto extends CommandBase {

    private double m_leftPower;
    private double m_rightPower;
    private double m_timeout = -1;

    public DriveAuto(double left, double right) {

        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);

        super("DriveAuto(" + left + "," + right + ")");
        m_leftPower=left;
        m_rightPower=right;

        requires(driveTrain);
    }

    public DriveAuto(double left, double right, double timeout) {

        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);

        super("DriveAuto(" + left + "," + right + "," + timeout + ")");
        m_leftPower=left;
        m_rightPower=right;
        m_timeout = timeout;

        requires(driveTrain);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        if(m_timeout >= 0.0) {
            setTimeout(m_timeout);
        }
        driveTrain.driveTank(m_leftPower, m_rightPower);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        boolean done = false;
        if(m_timeout >= 0.0) {
            done = isTimedOut();
        }
        return done;
    }

    // Called once after isFinished returns true
    protected void end() {
        driveTrain.driveTank(0.0, 0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}