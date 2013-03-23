/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.templates.commands.LightsDefault;

/**
 *
 * @author Ag
 */
public class Lights extends Subsystem {
    private int activeMode = 0;
    private double leftAnalogOutput = 0.0;
    private double rightAnalogOutput = 0.0;
    
    private static final double ANALOG_LOWER_THRESHHOLD = -1.0;
    private static final double ANALOG_UPPER_THRESHHOLD = 1.0;
    
    private DigitalOutput digitalOut1;
    private DigitalOutput digitalOut2;
    private DigitalOutput digitalOut3;
    private DigitalOutput digitalOut4;
    // We aren't actually using Jags, but this gives us an easy way
    // to control PWM outputs.
    private Jaguar pwmOut1;
    private Jaguar pwmOut2;
    
    public static final int MODE_RAINBOW_FILL = 0;
    public static final int MODE_BLUE_ALLIANCE = 1;
    public static final int MODE_RED_ALLIANCE = 2;
    public static final int MODE_DRIVE_FILL = 3;
    public static final int MODE_CLIMB_FILL = 4;
    public static final int MODE_BLINKY = 5;
    public static final int MODE_FLASH_GREEN = 6;
    
    public static final int ANALOG_CHANNEL_LEFT = 0;
    public static final int ANALOG_CHANNEL_RIGHT = 1;
    
    public Lights() {
        digitalOut1 = new DigitalOutput(Wiring.lightDigitalOutPin1);
        digitalOut2 = new DigitalOutput(Wiring.lightDigitalOutPin2);
        digitalOut3 = new DigitalOutput(Wiring.lightDigitalOutPin3);
        digitalOut4 = new DigitalOutput(Wiring.lightDigitalOutPin4);
        pwmOut1 = new Jaguar(Wiring.lightPwmOutPin1);
        pwmOut2 = new Jaguar(Wiring.lightPwmOutPin2);
    }
    
    public void initDefaultCommand() {
        setDefaultCommand(new LightsDefault());
    }
    
    private void updateOutput() {
        // Set each of the digital outputs to a bit of the mode value
        // Currently mode is only supported as 4 bits, even though it's actually
        // a 32 bit int in this subsystem.  We shouldn't need more than 4 bits.
        digitalOut1.set((activeMode & 1) == 1);
        digitalOut2.set(((activeMode >> 1) & 1) == 1);
        digitalOut3.set(((activeMode >> 2) & 1) == 1);
        digitalOut4.set(((activeMode >> 3) & 1) == 1);
        
        // Also need to set the analog outputs
        // OH GOD I THINK WE CAN ONLY DO ANALOG INPUTS OH GOD NO
        // MikeE: we can use the PWM output and pass it through a simple RC filter to give us an analog signal
        // I checked the rules and there's nothing to say that PWM can only go to servo/motor controller
        pwmOut1.set(leftAnalogOutput);
        pwmOut2.set(rightAnalogOutput);
    }
    
    public void setMode(int mode) {
        activeMode = mode;
        updateOutput();
    }
    
    public int getMode() {
        return activeMode;
    }
    
    public void setAnalog(int channel, double value) {
        // Value should never go out of the range of 0.0 and 1.0, so we limit it
        //value = Math.abs(value);
        if (value > ANALOG_UPPER_THRESHHOLD) {
            value = ANALOG_UPPER_THRESHHOLD;
        } else if (value < ANALOG_LOWER_THRESHHOLD) {
            value = ANALOG_LOWER_THRESHHOLD;
        }
        
        // Set the value to the approriate output channel
        if (channel == ANALOG_CHANNEL_LEFT) {
            leftAnalogOutput = value;
        } else if (channel == ANALOG_CHANNEL_RIGHT) {
            rightAnalogOutput = value;
        }
        updateOutput();
        
        // a -1.0 PWM has a pulse width of 0.68 ms
        // a 1.0 PWM has a pulse width of 2.29 ms
    }
}
