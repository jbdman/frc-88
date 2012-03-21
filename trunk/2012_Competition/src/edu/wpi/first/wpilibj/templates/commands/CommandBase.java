package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.OI;
import edu.wpi.first.wpilibj.templates.subsystems.Pitcher;
//import edu.wpi.first.wpilibj.templates.subsystems.WheelSpinner;
import edu.wpi.first.wpilibj.templates.subsystems.DriveTrain;
import edu.wpi.first.wpilibj.templates.subsystems.RampPusherSimple;
import edu.wpi.first.wpilibj.templates.subsystems.BallLifter;
import edu.wpi.first.wpilibj.templates.subsystems.Foot;
import edu.wpi.first.wpilibj.templates.subsystems.Shooter;
import edu.wpi.first.wpilibj.templates.subsystems.Turret;
import edu.wpi.first.wpilibj.templates.Wiring;

/**
 * The base for all commands. All atomic commands should subclass CommandBase.
 * CommandBase stores creates and stores each control system. To access a
 * subsystem elsewhere in your code in your code use CommandBase.exampleSubsystem
 * @author Author
 */
public abstract class CommandBase extends Command {

    public static OI oi;

    // non "subsystem" subsystems
    public static Compressor compressor = new Compressor(Wiring.compressorPressureSwitch,
            Wiring.compressorPowerRelay);

    // Create a single static instance of all of your subsystems
    public static BallLifter lifter = new BallLifter();
    public static RampPusherSimple rampPusherSimple = new RampPusherSimple();
    public static DriveTrain driveTrain = new DriveTrain();
    public static Foot foot = new Foot();
    public static Turret turret = new Turret();
    public static Shooter shooter = new Shooter();
    public static Pitcher pitcher = new Pitcher();

    public static void init() {
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        oi = new OI();

        // Initialize "non-subsystems" i.e. compressor
        compressor.start();

        // not sure this is the right place - should be in default command
        pitcher.enable();
        pitcher.setAverageSpeed(1200);

        // Show what command your subsystem is running on the SmartDashboard
        SmartDashboard.putData(lifter);
        SmartDashboard.putData(pitcher);
        SmartDashboard.putData(driveTrain);
        SmartDashboard.putData(rampPusherSimple);
        SmartDashboard.putData(foot);
        SmartDashboard.putData(shooter);
        SmartDashboard.putData(turret);
    }

    public CommandBase(String name) {
        super(name);
    }

    public CommandBase() {
        super();
    }

    public static void continuous() {
        turret.processImage();
    }

    private static int iterator = 0;
    public static void updateDashboard() {
        if(iterator % 5 == 0) {
            // Subsystem faults lights
            SmartDashboard.putBoolean("Pitcher ", !pitcher.getFault());
            SmartDashboard.putBoolean("Shooter ", !shooter.getFault());
            SmartDashboard.putBoolean("Turret ", !turret.getFault());
            SmartDashboard.putBoolean("RampPusher ", !rampPusherSimple.getFault());
            SmartDashboard.putBoolean("Foot ", !foot.getFault());
            SmartDashboard.putBoolean("DriveTrain ", !driveTrain.getFault());
            SmartDashboard.putBoolean("Lifter ", !lifter.getFault());
        }

        if(iterator % 3 == 1) {
            // get non-CAN data
            SmartDashboard.putDouble("Pitcher Target ", pitcher.getAverageSpeedSetpoint());

            // get Target Data
            SmartDashboard.putBoolean("Target found ", turret.foundTarget());
            SmartDashboard.putDouble("Target angle ", turret.getTargetAngle());
            SmartDashboard.putDouble("Target dist  ", turret.getTargetDistance());
        }

        if(iterator % 3 == 2) {
            double upper = pitcher.getSpeedUpper();
            double lower = pitcher.getSpeedLower();
            boolean stopped = (upper < 50.0 || lower < 50.0);
            SmartDashboard.putBoolean("Running ", !stopped);
            SmartDashboard.putDouble("Pitcher upper ", upper);
            SmartDashboard.putDouble("Pitcher lower ", lower);
        }
//        if(iterator % 5 == 2) {
//            // DEBUG STUFF FOR RAMPPUSHER
//            SmartDashboard.putBoolean("Pusher LimitSwitch ", rampPusher.isLimitSwitchPressed());
//            SmartDashboard.putBoolean("Pusher Down ", rampPusher.isDown());
////            SmartDashboard.putDouble("Pusher Angle ", rampPusher.getAngle());
//            SmartDashboard.putDouble("Pusher Current ", rampPusher.getCurrent());
//
//            SmartDashboard.putDouble("Lifter IOut", lifter.getCurrent());
//            SmartDashboard.putDouble("Turret Posn ", turret.getAngle());
//            SmartDashboard.putBoolean("Turret Switch ", turret.isLimitSwitchPressed());
//        }
        iterator++;
    }


}
