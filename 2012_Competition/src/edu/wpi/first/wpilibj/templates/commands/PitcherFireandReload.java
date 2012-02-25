/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 * @author David
 */
public class PitcherFireandReload extends CommandGroup {

    public PitcherFireandReload() {
        addSequential(new PitcherFire());
        addSequential(new PitcherReload());
        // may include auto ball reloading
//        addParallel(new PitcherDown());
//        addSequential(new LifterUp());

    }
}