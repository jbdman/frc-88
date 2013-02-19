/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author 
 */

//this will be the command string to run the autonomous program when subsystems and commands are done. - David
public class AutoSimple extends CommandBase {
    
    private static final double speed = -30.0;
    private double startDist = 0.0;
    
    public AutoSimple() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        super("AutoSimple");
        requires(drive);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        startDist = drive.getRightDistance();
        drive.driveTankClosedLoop(speed, speed);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if(drive.getRightDistance() < startDist - 200.0) {
            dumper.backward();
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return drive.getRightDistance() < startDist - 235.0;
    }

    // Called once after isFinished returns true
    protected void end() {
        drive.driveTankClosedLoop(0.0, 0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
