/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.templates.subsystems.Climber;

/**
 *
 * @author David
 */

    /**
     * command for moving the climber to a set position
     */
public class ClimberPosition extends CommandBase {
    
    private double m_position;
    private double m_epsilon = 0.2;
    private int m_countUntilStable = 5;
    private int m_count = 0;
    private double m_timeout = 2.5;
    
    public ClimberPosition(double position) {
        super("ClimberPosition");
        requires (climber);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        m_position = position;
    }

    public ClimberPosition(double position, double timeout) {
        super("ClimberPosition");
        requires (climber);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        m_position = position;
        m_timeout = timeout;
    }

    // Called just before this Command runs the first time
    
    /**
     * initialize closed loop if necessary
     */
    protected void initialize() {
        if(!climber.isClosedLoop()) {
            climber.enableClosedLoop();
        }
        climber.ClimbClosedLoop(m_position);
        m_count = 0;
        setTimeout(m_timeout);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {

        boolean done = isTimedOut();
        
        if (Math.abs(climber.getPosition()) < m_epsilon) {
            m_count ++;
        } else {
            m_count = 0;
        }
        if (m_count >= m_countUntilStable) {
            done = true;   
        }
        return done;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
