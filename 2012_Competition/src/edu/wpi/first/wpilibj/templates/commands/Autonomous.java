/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
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
        addParallel(new TurretAuto());
        addParallel(new PitcherUp());
        addParallel(new PitcherSpeed(950));
//        ***********delay**********
        //fires the trigger piston
        addSequential(new PitcherFireandReload());
        //loads the second ball
        addSequential(new TurretSetAngle(0));
        addSequential(new PitcherDown());
        addParallel(new LifterUp());

        //repeats for the second ball in the robot
        addParallel(new PitcherSpeed(950));
        addSequential(new PitcherUp());
        addSequential(new PitcherFireandReload());
        addParallel(new LifterStop());

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