/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.templates.subsystems.Climber;
import edu.wpi.first.wpilibj.templates.commands.CommandBase;
/**
 *
 * @author David
 */
public class ClimbLevelTwo extends CommandGroup {
    
    public ClimbLevelTwo() {
        addSequential(new ClimberPosition(-20.0));
        addParallel(new TilterPosition(3));
        addSequential(new ClimberPosition(0));
        addSequential(new TilterPosition(3.5));
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

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
