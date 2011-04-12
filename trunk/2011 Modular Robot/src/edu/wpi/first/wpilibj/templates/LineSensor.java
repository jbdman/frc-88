/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * LineSensor class
 * Unit consists of 3 light sensors powered from a solenoid output.
 * Since they are powered from 12V/24V, they are open collector allowing
 * the DIO pull up resistors to read them as 0/5V. This means that the
 * active signal is low (0V, logical "false") so the logic is inverted
 * in the individual access methods
 *
 * @author tj2
 */
public class LineSensor {

    private Solenoid m_power;
    private DigitalInput m_left;
    private DigitalInput m_mid;
    private DigitalInput m_right;

    private boolean m_enabled;

    public LineSensor(int powerChan, int inputChanLeft, int inputChanMid, int inputChanRight) {

        m_power = new Solenoid(powerChan);

        m_left = new DigitalInput(inputChanLeft);
        m_mid = new DigitalInput(inputChanMid);
        m_right = new DigitalInput(inputChanRight);

    }

    public void enable() {
        m_power.set(true);
        m_enabled = true;
    }

    public void disable() {
        m_power.set(false);
        m_enabled = false;
    }

    public boolean isEnabled() {
        return m_enabled;
    }

    public boolean isLeft() {
        return m_enabled && !m_left.get();
    }

    public boolean isMid() {
        return m_enabled && !m_mid.get();
    }

    public boolean isRight() {
        return m_enabled && !m_right.get();
    }

    /*
     * get() method returns the line sensor readings as a bit vector
     */
    public int get() {
        int bits = 0;

        if(m_enabled) {
            bits |= (m_left.get()? 0: 4);
            bits |= (m_mid.get()? 0: 2);
            bits |= (m_right.get()? 0: 1);
        }

        return bits;
    }
}
