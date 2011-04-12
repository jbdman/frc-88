/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DigitalOutput;

/**
 * LogoLight displays one of the logo colors and shapes.
 *
 * @author TJ2
 */
public class LogoLight {

    /**
     * Constant value of lights off
     */
    static final int kNone = 0;
    /**
     * Constant value of Red Triangle light
     */
    static final int kRedTriangle = 1;
    /**
     * Constant value of White Circle light
     */
    static final int kWhiteCircle = 2;
    /**
     * Constant value of Blue Square light
     */
    static final int kBlueSquare = 3;

    /**
     * Digital o/p object for high bit light selector.
     */
    private DigitalOutput m_dataA;
    /**
     * Digital o/p object for low bit light selector.
     */
    private DigitalOutput m_dataB;
    /**
     * Private value of current light setting
     */
    private int m_color;

    /**
     * Create LogoLight object using default digital module
     *
     * @param channelA Digital o/p channel for high order data bit
     * @param channelB Digital o/p channel for low order data bit
     */
    public LogoLight(int channelA, int channelB) {
        logolight(2, channelA, 2, channelB);
    }

    /**
     * Create LogoLight object specifying digital module slots
     * 
     * @param slotA Digital module slot for high order data bit
     * @param channelA Digital o/p channel for high order data bit
     * @param slotB Digital module slot for low order data bit
     * @param channelB Digital o/p channel for low order data bit
     */
    public LogoLight(int slotA, int channelA, int slotB, int channelB) {
        logolight(slotA, channelA, slotB, channelB);
    }

    /**
     * Private method for creating the logolight object
     */
    private void logolight(int slotA, int chanA, int slotB, int chanB) {

        m_dataA = new DigitalOutput(slotA, chanA);
        m_dataB = new DigitalOutput(slotB, chanB);

        m_color = kNone;
        this.set(m_color);

    }

    /**
     * Get the value of the current light color.
     *
     * @return Value of the current color
     */
    public int getColor() {

        return m_color;
    }

    /**
     * Get the string name of the current light color.
     *
     * @return String representing current color
     */
    public String getColorName() {
        switch (m_color) {
            case kNone:
                return "Off";
            case kRedTriangle:
                return "Red Triangle";
            case kWhiteCircle:
                return "White Circle";
            case kBlueSquare:
                return "Blue Square";
        }
        return "Unknown";
    }

    /**
     * Set the output color.
     * This is the public method which correctly deals with
     * negative and overflow values of the color parameter.
     *
     * @param color color to display
     */
    public void setColor(int color) {
        // deal with negative numbers
        if(color < 0) {
            color = (color%4)+4;
        }
        color = color % 4;
        if(color != m_color) {
            set(color);
        }
    }

    /**
     *  Cycle the logolight to the next color.
     */
    public void nextColor() {
        set((m_color+1) % 4);
    }

    /**
     *  Cycle the logolight to the previous color.
     */
    public void prevColor() {
        // (c-1)%4 goes negative when c==0, so use (c+4-1)%4 instead
        set((m_color+3) % 4);
    }

    /**
     * Set the output data lines to generate the color.
     * Private method which sets both digital outputs to
     * generate the appropriate color logo.
     *
     * @param color value of the color to display
     */
    private void set(int color) {

        m_color = color;
        m_dataA.set((color&2) != 0);
        m_dataB.set((color&1) != 0);
    }

}
