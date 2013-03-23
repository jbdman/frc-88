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
public class TilterPosition extends CommandBase {
    
    private double m_position;
    private double m_epsilon = 0.1;
    private int m_countUntilStable = 5;
    private int m_count = 0;
    private double m_timeout = 2.5;
    
    public TilterPosition(double position) {
        super("TilterPosition");
        requires (tilter);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        m_position = position;
    }

    public TilterPosition(double position, double timeout) {
        super("TiltererPosition");
        requires (tilter);
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
        if(!tilter.isClosed()) {
            tilter.enableClosedLoop();
        }
        tilter.TilterClosedLoop(m_position);
        m_count = 0;
        setTimeout(m_timeout);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {

        boolean done = isTimedOut();
        
        // HEY SHOULD THIS BE TILTER INSTEAD OF CLIMBER?
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
