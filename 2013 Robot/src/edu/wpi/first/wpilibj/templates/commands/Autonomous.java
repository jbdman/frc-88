/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 * @author Michael_Edgington
 */
public class Autonomous extends CommandGroup {

    public Autonomous() {
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.        
        addParallel(new HomeGroup());
        addSequential(new DriveClosed(-10.0, -12.0, 0.5));
        addSequential(new DriveClosed(-20.0, -24.0, 0.5));
        addSequential(new DriveClosed(-30.0, -36.0, 1.0));
        addSequential(new DriveClosed(-40.0, -40.0, 2.0));
        addSequential(new DumperBackward());
        addSequential(new DriveClosed(-10.0, -10.0, 1.5));
        addSequential(new WaitCommand(1.0));
        addSequential(new DriveClosed(10.0, 20.0, 1));
        addSequential(new DriveClosed(50.0, 50.0, 1));
    
        /*
         * PROBLEM: instance 'drive2' overwrites the parameters of 'drive1'
         * behavior of autonomous is same as (-10, -10, 4):
         * [cRIO] DriveClosed(-20.0, -25.0, 3.0) Init @ 59120
         * [cRIO] DriveClosed(-20.0, -25.0, 3.0) Ended @ 61140
         * [cRIO] DriveClosed(-10.0, -10.0, 2.0) Init @ 61143
         * [cRIO] DriveClosed(-10.0, -10.0, 2.0) Ended @ 63161
         */

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
