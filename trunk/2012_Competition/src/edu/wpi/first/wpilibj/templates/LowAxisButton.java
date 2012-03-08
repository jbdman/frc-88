/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.Joystick;

/**
 * Logical button based on the value of the joystick axis.
 *
 * @author Mike_Edgington
 */
public class LowAxisButton extends Button {

    private Joystick joystick;
    private int axis = 3;
    private double trigger = -0.5;

    /**
     * Logical button based on the value of the joystick axis.
     * Uses defaults of axis 3 (L/R triggers on XBox controller) and -50% for trigger value
     *
     * @param stick the joystick to read
     *
     */
    public LowAxisButton(Joystick stick) {
        joystick = stick;
    }

    /**
     * Logical button based on the value of the joystick axis.
     * Uses default of -50% for trigger value
     *
     * @param stick the joystick to read
     * @param axis the raw axis to read
     *
     */
    public LowAxisButton(Joystick stick, int axis) {
        joystick = stick;
        this.axis = axis;
    }

    /**
     * Logical button based on the value of the joystick axis
     *
     * @param stick the joystick to read
     * @param axis the raw axis to read
     * @param trigger the trigger value - true if less than this value
     *
     */
     public LowAxisButton(Joystick stick, int axis, double trigger) {
        joystick = stick;
        this.axis = axis;
        this.trigger = trigger;
    }

    /**
     * Returns logical button value depending on the value of the joystick axis
     *
     * @return true if the axis is less than the trigger value (default -50%)
     */
    public boolean get() {
        return (joystick.getRawAxis(axis) < trigger);
    }
}
