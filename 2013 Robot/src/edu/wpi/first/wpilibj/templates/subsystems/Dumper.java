/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.templates.commands.feed_position;
import edu.wpi.first.wpilibj.Encoder;
/**
 *
 * @author David
 */
/*************************************************************
     * NOTE: The commands need work. Please fix them. Someone. Also
     * this is a cool box, is it not?
     *************************************************************/
// more of this must be shown to me or Ag before we can start
public class Dumper extends Subsystem {
    CANJaguar DumperJag;
    private boolean m_dumpfault = false;
    private static final double defaultDownSpeed = .5;
    private static final double defaultUpSpeed = -.4;
    private DigitalInput m_limitSwitch;
    private static final int linesPerRotation = 100;
    //^^^these numbers affect speed and can be changed
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public Dumper(){
        try {
                DumperJag = new CANJaguar(Wiring.DumperCANID);
                m_limitSwitch = new DigitalInput(Wiring.dumperLimitSwitch);
                //encoders and stuff needs to be determined for angle
                //this stuff controls the close loop control, they may be subject to change
                // Need to determine encoder codes per rev
                //DumperJag.configEncoderCodesPerRev(360);
                //DumperJag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                //DumperJag.changeControlMode(CANJaguar.ControlMode.kPosition);
                //DumperJag.setPID(0.005,0.02,0);
            }
            catch (CANTimeoutException ex) {
            }
        
        if(DumperJag != null) {
            try {
                DumperJag.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
                DumperJag.configEncoderCodesPerRev(linesPerRotation);
            }
            catch (CANTimeoutException ex) {
                m_dumpfault = true;
                System.err.println("CAN timeout");
            }
        }
    }
    
    
    public void initDefaultCommand() {
        setDefaultCommand(new feed_position());
        //^^ this should make it so the dumper will go into feed position
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    //these commands REALLY need work because the positions are not up or down they are set positions, ask mr e
    public void high_score_position(){
    
    }
    public void low_score_positon(){
        
    }
    public void feed_position(){
        DumpOpenLoop(0.0);
    }
    public void stop() {
        DumpOpenLoop(0.0);
    }
    //ln 54 for stuff above
    public void DumpUp(){
        DumpOpenLoop(defaultUpSpeed);
    }
    public void DumpDown(){
        DumpOpenLoop(defaultDownSpeed);
    }
    public boolean isLimitSwitchPressed() {
        return m_limitSwitch.get();
    }
    public double getPosition(){
        double position = 0.0;
        if(DumperJag != null) {
            try {
                position = DumperJag.getPosition();
            } catch (CANTimeoutException ex) {
                m_dumpfault = true;
                System.err.println("CAN Timeout");
            }
        }
        return position;
    }
        
    public void DumpOpenLoop(double power) {

        if(DumperJag != null) {
            try {
                //play with stuff under to see if it needs to be inverted
                DumperJag.setX(power);
            } catch(CANTimeoutException ex) {
                m_dumpfault = true;
                System.err.println("****************CAN timeout***********");
            }
        }            
      
}
  public void DumpClosedLoop(double power) {
        // Really dont know what the variable should be something needs to be built for me to see.
        int variable = 100;
        if(DumperJag != null) {
            try {
                //the formula below will need to change
                //also play with stuff under to see if it needs to be inverted
                DumperJag.setX(power * variable);
            } catch(CANTimeoutException ex) {
                m_dumpfault = true;
                System.err.println("****************CAN timeout***********");
            }
        }   
    }
}
