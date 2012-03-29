/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 * @author Chris
 */
public class Autonomous extends CommandGroup {

    public Autonomous() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.
        //sets the rampPusher to the limit switch at the top
        addParallel(new RampPusherUp());
        //auto-targets the turret; sets the shooter angle; and then tells the pitcher to spin
        addSequential(new PitcherSpeed(1325));
//        addParallel(new TurretAuto());
        addSequential(new WaitCommand(1.5));
        addSequential(new PitcherSpeed(1325));
        addParallel(new RampPusherDown());
        addSequential(new PitcherUp());
        //fires the trigger piston
        addSequential(new PitcherFireandReload());
        //loads the second ball
//        addSequential(new TurretSetAngle(0));
        addParallel(new LifterUp());
        addSequential(new PitcherDown());

        //repeats for the second ball in the robot
//        addParallel(new TurretAuto());
        addSequential(new PitcherSpeed(1350));
        addSequential(new PitcherUp());
        addSequential(new PitcherFireandReload());
//        addParallel(new DriveWithKinect());

        addSequential(new DriveAuto(-0.7, -0.7, 3.0)); // TOM - THE LAST NUMBER IS THE TIME DRIVE RUNS

        addSequential(new LifterStop());

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    }
}