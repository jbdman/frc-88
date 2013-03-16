/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.Wiring;

/**
 *
 * @author tj2
 */
public class BaseTalon extends Subsystem {
    private Solenoid talonPiston;
    
    // This value for HOOKED_POSITION is a theory based on what the piston looks
    // to be doing, but will need to be verified.
    public static boolean HOOKED_POSITION = true;
    public static boolean RELEASED_POSITION = !HOOKED_POSITION;
    
    public BaseTalon() {
        // Initialize our solenoid and set it to a default position.
        talonPiston = new Solenoid(Wiring.talonSolenoid);
        setReleased();
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    /*
     * Changes the position of the talons to be the opposite of the current position.
     */
    public void toggleTalon() {
        talonPiston.set(!getTalon());
    }
    
    /*
     * Sets the talon position to the desired position.
     * 
     * @param   on  The position to set the talons to.  Although the input is
     *              a bool, the constants HOOKED_POSITION and RELEASED_POSITION
     *              should be used for consistancy.
     */
    private void setTalon(boolean on) {
        talonPiston.set(on);
        SmartDashboard.putBoolean("Talon Position", getTalon());
    }
    
    /*
     * Sets the talons to be the in the forward, hooked position.
     */
    public void setHooked() {
        setTalon(HOOKED_POSITION);
    }
    
    /*
     * Sets the talons to be in the pulled back, released position.
     */
    public void setReleased() {
        setTalon(RELEASED_POSITION);
    }
    
    /*
     * Gets the current talon position.  This position is where the solenoid is
     * set, and is not guaranteed to be it's actual, physical location.
     * 
     * @return  Returns the position of the solenoid as a boolean.  The boolean
     *          value is whether or not the solenoid is in the "on" position.
     *          For constistancy, this value should be compared to the consetants
     *          HOOKED_POSITION and RELEASED_POSITION to see what position it is in.
     */
    private boolean getTalon() {
        return talonPiston.get();
    }
    
    /*
     * Returns whether or not the talons are in the forward, hooked position.
     * This position is where the solenoid is set, and is not guaranteed to be
     * it's actual, physical location.
     */
    public boolean isHooked() {
        return getTalon() == HOOKED_POSITION;
    }
    
    /*
     * Returns whether or not the talons are in the pulled back, released position.
     * This position is where the solenoid is set, and is not guaranteed to be
     * it's actual, physical location.
     */
    public boolean isReleased() {
        return getTalon() == RELEASED_POSITION;
    }
}
