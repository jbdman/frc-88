/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author David and minions
 */
public class DriveClosed extends CommandBase {
    
    private double m_speedLeft;
    private double m_speedRight;
    private double m_timeout;
    private double m_distance;
    private boolean m_backwards = false;
    
    public DriveClosed(double leftSpeed, double rightSpeed, double time, double distance) {
        
        super("DriveClosed(" + leftSpeed + ", " + rightSpeed + ", " + time + ", " + distance +")");

//        setInterruptible(false); // this may work, but doesn't solve our problem

        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(drive);
        if(leftSpeed + rightSpeed < 0.0) {
            m_backwards = true;
        } 
        m_speedLeft = leftSpeed;
        m_speedRight = rightSpeed;
        m_distance = Math.abs(distance);
        m_timeout = time;
    }

//    public boolean isInterruptible() {        
//        return false;
//    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
        double currentDistance;
        
        if(!drive.isClosedLoop()) {
            drive.enableClosedLoop();
        }
        currentDistance = drive.getAverageDistance();
        if(m_backwards) {
            m_distance = currentDistance - m_distance;
        } else {
            m_distance = currentDistance + m_distance;
        }
        setTimeout(m_timeout);
        drive.driveTankClosedLoop(m_speedLeft, m_speedRight);

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        boolean done = isTimedOut();

        if(!done) {
            double currentDistance = drive.getAverageDistance();
            if(m_backwards) {
                done = currentDistance <= m_distance;
            } else {
                done = currentDistance >= m_distance;
            }
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
