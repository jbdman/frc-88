/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author David
 */
public class DriveClosed extends CommandBase {
    
    private double m_speedLeft;
    private double m_speedRight;
    private double m_timeout;
//    private double m_distance;
    
    public DriveClosed(double leftSpeed, double rightSpeed, double time) {
        
        super("DriveClosed(" + leftSpeed + ", " + rightSpeed + ", " + time + ")");

        setInterruptible(false); // this may work, but doesn't solve our problem

        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(drive);

        m_speedLeft = leftSpeed;
        m_speedRight = rightSpeed;
//        m_distance = distance;
        m_timeout = time;
    }

//    public boolean isInterruptible() {        
//        return false;
//    }
    
    // Called just before this Command runs the first time
    protected void initialize() {

        System.out.println(this.getName() + " Init @ " + System.currentTimeMillis());

        if(!drive.isClosedLoop()) {
            drive.enableClosedLoop();
        }
        setTimeout(m_timeout);
        drive.driveTankClosedLoop(m_speedLeft, m_speedRight);

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
        System.out.println(this.getName() + " Ended @ " + System.currentTimeMillis());
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        System.out.println(this.getName() + " Interrupted @ " + System.currentTimeMillis());
    }
}
