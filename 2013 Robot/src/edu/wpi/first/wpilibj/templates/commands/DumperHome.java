/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author Ag Despopoulos
 */
public class DumperHome extends CommandBase {
    private double desiredPosition = 0.0;
    private double startPosition = 0.0;
    private double increment = 0.05;
    private double maxSeek = 0.20;
    private int state = 0;
    
    // state constants because we can't use an enum
    private final int STATE_START = 0;
    private final int STATE_SEEK_LEFT = 1;
    private final int STATE_SEEK_RIGHT = 2;
    private final int STATE_SUCCESS = 3;
    private final int STATE_FAILURE = 4;
    
    public DumperHome() {
        super("DumperHome");
        requires(dumper);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        if (dumper.isLimitSwitchPressed()){ 
            dumper.stop();
            state = STATE_SUCCESS;
        }
        else {
            startPosition = dumper.getPosition();
            desiredPosition = startPosition + increment;
            dumper.DumpDown();
            state = STATE_SEEK_LEFT;
        }
    }
    
    

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (dumper.isLimitSwitchPressed()) {
            dumper.stop();
            state = STATE_SUCCESS;
        }
        if (increment >= maxSeek) {
            dumper.stop();
            state = STATE_FAILURE;
        }
        switch (state) {
            case STATE_SEEK_LEFT:
                if (dumper.getPosition() >= desiredPosition) {
                    desiredPosition = startPosition - increment;
                    dumper.DumpUp();
                    state = STATE_SEEK_RIGHT;
                }
                break;
            case STATE_SEEK_RIGHT:
                if (dumper.getPosition() <= desiredPosition) {
                    increment += 0.05;
                    desiredPosition = startPosition + increment;
                    dumper.DumpDown();
                    state = STATE_SEEK_LEFT;
                }
                break;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (state == STATE_SUCCESS) {
            // Success
            return true;
        }
        if (state == STATE_FAILURE) {
            // Failure
            return true;
        }
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        // stop motor, reset position on the dumper, enable closed loop
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
