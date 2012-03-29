/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.Wiring;

/**
 *
 * @author David (the guy who is ill)
 */
public class Shooter extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private Solenoid  m_anglePiston;
    private Solenoid  m_firingPiston;
    private boolean m_fault = false;
    
    public Shooter (){
        m_anglePiston = new Solenoid(Wiring.shooterAngleSolenoid);

        // set up solenoid for firing
        m_firingPiston = new Solenoid(Wiring.shooterLoadSolenoid);
    }

    public void setFarAngle() {
        m_anglePiston.set(true);
    }

    public void setNearAngle() {
        m_anglePiston.set(false);
    }

    public boolean isFarAngle() {
        return !m_anglePiston.get();
    }

    public void fire() {
        m_firingPiston.set(true);
    }

    public void reload() {
        m_firingPiston.set(false);
    }

    public boolean getFault() {
        return m_fault;
    }

    protected void initDefaultCommand() {
    }
}