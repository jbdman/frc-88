/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author Ag
 */
public class ClimberHome extends CommandBase {
    
    private boolean movingDown = false;
    private boolean broke = false;
    
    public ClimberHome() {
        super ("ClimberHome");
        requires(climber);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        if (climber.upperLimitTripped() && climber.lowerLimitTripped() ) {
            System.out.println("WIRE IS UNPLUGGED");
            climber.stop();
            broke = true;
            climber.setfault();
                    
        } else {
        
            if (climber.lowerLimitTripped()){ 
                climber.down();
                movingDown = true;
            }
            
            else {
                climber.up();
                movingDown = false;
            }
        }
    }
    

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        
        if (!climber.lowerLimitTripped() && movingDown) {
            climber.up();
        }
       
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (climber.lowerLimitTripped() && !movingDown) {
            return true;
        }
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        climber.stop();
        // Reset the encoder
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
