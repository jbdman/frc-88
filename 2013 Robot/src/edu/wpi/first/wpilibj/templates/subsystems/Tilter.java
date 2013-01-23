/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.can.CANTimeoutException;


/**
 *
 * @author 
 */
//must wait until there is a more definitive idea ie. must be built or described better - David
public class Tilter extends Subsystem {
    CANJaguar TilterJag;
    
    private boolean m_tiltJag = false;
    //these numbers will have to be changed depending on the speed of the motors
    private static final double defaultDownSpeed = 1;
    private static final double defaultUpSpeed = -1;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public Tilter()  {
         try {
                TilterJag = new CANJaguar(Wiring.climberCANID);
                
                //encoders and stuff needs to be determined (think it needs to set up for distance)
                
                // Need to determine encoder codes per rev
                //TilterJag.configEncoderCodesPerRev(360);
                //TilterJag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                //TilterJag.changeControlMode(CANJaguar.ControlMode.kPosition);
                //TilerJag.setPID(0.005,0.02,0);
            }
            catch (CANTimeoutException ex) {
            }
        
    }
    public void increase(){
        TilterOpenLoop(defaultDownSpeed);
    }
    public void decrease(){
        TilterOpenLoop(defaultUpSpeed);
    }
    public void Tiltstop(){
        TilterOpenLoop(0.0);
    }
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void TilterOpenLoop(double side){
        if(TilterJag != null) {
            try {
                TilterJag.setX(side);
            } catch(CANTimeoutException ex) {
                m_tiltJag = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        
    }
    public void TilterClosedLoop(double side) {
        //stuff underneath tbd
        int distance = 100;
        if(TilterJag != null) {
            try {
                TilterJag.setX(side * distance);
            } catch(CANTimeoutException ex) {
                m_tiltJag = true;
                System.err.println("****************TiltCAN timeout***********");
            }
        }
       
    }
    }

