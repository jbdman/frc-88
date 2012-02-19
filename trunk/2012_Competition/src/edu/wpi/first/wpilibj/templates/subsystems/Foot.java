/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.templates.commands.FootUp;

/**
 *
 * @author Michael_Edgington
 */
public class Foot extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    Solenoid m_foot;

    public Foot() {
        m_foot = new Solenoid(Wiring.footSolenoid);
        m_foot.set(false);
    }

    public void up() {
        m_foot.set(false);
    }

    public void down() {
        m_foot.set(false);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new FootUp());
    }
}