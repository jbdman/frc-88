package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.OI;
import edu.wpi.first.wpilibj.templates.subsystems.Drive;
import edu.wpi.first.wpilibj.templates.subsystems.Climber;
import edu.wpi.first.wpilibj.templates.subsystems.Tilter;
import edu.wpi.first.wpilibj.templates.subsystems.Dumper;
import edu.wpi.first.wpilibj.templates.subsystems.Base;
import com.ni.rio.NiFpga;


/**
 * The base for all commands. All atomic commands should subclass CommandBase.
 * CommandBase stores creates and stores each control system. To access a
 * subsystem elsewhere in your code in your code use CommandBase.exampleSubsystem
 * @author Author
 */
public abstract class CommandBase extends Command {

    public static OI oi;
    // Create a single static instance of all of your subsystems
    //public static ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
    public static Drive drive = new Drive();
    public static Climber climber = new Climber();
    public static Tilter tilter = new Tilter();
    public static Dumper dumper = new Dumper();
    public static Base base = new Base();
    
    public static void init() {
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        oi = new OI();

       // Show what command your subsystem is running on the SmartDashboard
        SmartDashboard.putData(tilter);
        SmartDashboard.putData(climber);
        SmartDashboard.putData(dumper);
        SmartDashboard.putData(drive);
        SmartDashboard.putData(base);
    }
    
    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }

    public static void reportCANError(int CANId, String str) {
        
        SmartDashboard.putString("CAN Error ", "ID " + CANId + ": " + str);
    }
    
    private static int iterator = 0;
    public static void updateDashboard() {

        // put data on dashboard every 10th call
        if(iterator % 10 == 0) {
            // Subsystem faults lights
            SmartDashboard.putBoolean("Climber ", !climber.getFault());
            SmartDashboard.putBoolean("Tilter ", !tilter.getFault());
            SmartDashboard.putBoolean("Dumper ", !dumper.getFault());
            SmartDashboard.putBoolean("Drive ", !drive.getFault());
        }

       // put data on dashboard every 10th call
        if(iterator % 10 == 5) {
            // Various indicators
            SmartDashboard.putNumber("Climber position ", climber.getPosition());
            SmartDashboard.putNumber("Tilter angle ", tilter.getAngle());
            SmartDashboard.putNumber("Drive (left) ", drive.getLeftSpeed());
            SmartDashboard.putNumber("Drive (right) ", drive.getRightSpeed());
            SmartDashboard.putNumber("DriveDist (left) ", drive.getLeftDistance());
            SmartDashboard.putNumber("DriveDist (right) ", drive.getRightDistance());
            SmartDashboard.putNumber("Range Finder ", base.getRangeFinderDist());
            
        }
        
            SmartDashboard.putNumber("DumperJag Current", dumper.getCurrent());

        iterator++;
    }

}
