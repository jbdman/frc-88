/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.templates.Wiring;
/**
 *
 * @author TJ2
 */
public class StringTheory extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private CANJaguar m_front_BackMotor;
    private CANJaguar m_sidewaysMotor;
    private CANJaguar m_lifterMotor;

    public StringTheory(){
        //side side string motor
        try{
            m_sidewaysMotor=new CANJaguar(Wiring.right_leftStringCanID);
        }
        catch (CANTimeoutException ex) {
            System.err.println("CAN Init error: ID " + Wiring.right_leftStringCanID);
        }
        //lifting strings
        try{
            m_lifterMotor=new CANJaguar(Wiring.liftingStringsCANID);
        }
        catch (CANTimeoutException ex) {
            System.err.println("CAN Init error: ID " + Wiring.liftingStringsCANID);
        }
        //forwards backwards motor
        try{
            m_front_BackMotor=new CANJaguar(Wiring.front_backStingCANID);
        }
        catch (CANTimeoutException ex) {
            System.err.println("CAN Init error: ID " + Wiring.front_backStingCANID);
        }

    }
    public void setFixedMotorPower(){
        
    }

    public CANJaguar getM_front_BackMotor() {
        return m_front_BackMotor;
    }

    public void setM_front_BackMotor(CANJaguar m_front_BackMotor) {
        this.m_front_BackMotor = m_front_BackMotor;
    }

    public CANJaguar getM_lifterMotor() {
        return m_lifterMotor;
    }

    public void setM_lifterMotor(CANJaguar m_lifterMotor) {
        this.m_lifterMotor = m_lifterMotor;
    }

    public CANJaguar getM_sidewaysMotor() {
        return m_sidewaysMotor;
    }

    public void setM_sidewaysMotor(CANJaguar m_sidewaysMotor) {
        this.m_sidewaysMotor = m_sidewaysMotor;
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}