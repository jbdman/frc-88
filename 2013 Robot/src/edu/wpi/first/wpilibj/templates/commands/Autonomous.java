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
//        addParallel(new HomeGroup());
//        addSequential(new DriveClosed(-10.0, -12.0, 0.5, 40.0));
//        addSequential(new DriveClosed(-40.0, -40.0, 2.0, 40.0));
//        addSequential(new DumperBackward());
//        addSequential(new DriveClosed(-10.0, -10.0, 1.5, 20.0));
//        addSequential(new WaitCommand(1.0));
//        addSequential(new DriveClosed(10.0, 20.0, 1.0, 20.0));
//        addSequential(new DriveClosed(50.0, 50.0, 2.0, 100.0));
//    
          addSequential(new DriveClosed(-10.0, -10.0, 0.5, 5000.0));
          addSequential(new DriveClosed(-20.0, -35.0, 3.0, 60.0));
          addSequential(new DriveClosed(-30.0, -30.0, 3.0, 50.0));
          
 
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
