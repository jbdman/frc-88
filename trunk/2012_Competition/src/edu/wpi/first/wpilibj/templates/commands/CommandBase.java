package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.OI;
import edu.wpi.first.wpilibj.templates.subsystems.Pitcher;
//import edu.wpi.first.wpilibj.templates.subsystems.WheelSpinner;
import edu.wpi.first.wpilibj.templates.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.templates.subsystems.RampPusher;
import edu.wpi.first.wpilibj.templates.subsystems.BallLifter;
import edu.wpi.first.wpilibj.templates.subsystems.Foot;
import edu.wpi.first.wpilibj.templates.subsystems.Shooter;
import edu.wpi.first.wpilibj.templates.subsystems.Turret;

/**
 * The base for all commands. All atomic commands should subclass CommandBase.
 * CommandBase stores creates and stores each control system. To access a
 * subsystem elsewhere in your code in your code use CommandBase.exampleSubsystem
 * @author Author
 */
public abstract class CommandBase extends Command {

    public static OI oi;

    // Create a single static instance of all of your subsystems
    public static BallLifter lifter = new BallLifter();
    public static RampPusher rampPusher = new RampPusher();
    public static DriveTrain driveTrain = new DriveTrain();
    public static Foot foot = new Foot();
    public static Turret turret = new Turret();
//    public static WheelSpinner wheelSpinner = new WheelSpinner();
    public static Shooter shooter = new Shooter();
    public static Pitcher pitcher = new Pitcher();

    public static void init() {
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        oi = new OI();

        turret.enable();
        pitcher.enable();

        // Show what command your subsystem is running on the SmartDashboard
        SmartDashboard.putData(lifter);
        SmartDashboard.putData(pitcher);
        SmartDashboard.putData(driveTrain);
        SmartDashboard.putData(rampPusher);
        SmartDashboard.putData(foot);
        SmartDashboard.putData(shooter);
        SmartDashboard.putData(turret);
//        SmartDashboard.putData(wheelSpinner);
    }

    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }

    public static void updateDashboard() {
        SmartDashboard.putDouble("Pitcher upper ", pitcher.getSpeedUpper());
        SmartDashboard.putDouble("Pitcher lower ", pitcher.getSpeedLower());
        // Subsystem faults lights
        SmartDashboard.putBoolean("Pitcher Fault ", !pitcher.getFault());
        SmartDashboard.putBoolean("Turret Fault ", !turret.getFault());
        SmartDashboard.putBoolean("RampPusher Fault ", !rampPusher.getFault());
        SmartDashboard.putBoolean("Foot Fault ", !foot.getFault());
//        SmartDashboard.putBoolean("Drivetrain Fault ", !driveTrain.getFault());
//        SmartDashboard.putBoolean("Lifter Fault ", !lifter.getFault());
    }


}
