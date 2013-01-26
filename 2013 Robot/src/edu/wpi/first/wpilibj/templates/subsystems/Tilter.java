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
    // Tilter Dimensions, specified in inches
    private static final double dimensionA = 5.0;
    private static final double dimensionB = 5.0;
    private double angleAddition;
    private double angleMultiplier;
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
        angleAddition = (dimensionA * dimensionA) + (dimensionB * dimensionB);
        angleMultiplier = 2 * dimensionA * dimensionB;
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
    
    //I've commented this out because I'm not sure its relevant for the Titler
    //public boolean LimitTripped() {
      //  try {
            // Also not sure if it returns true or false when the switch is tripped
        //    return TilterJag.getForwardLimitOK();
       // } catch(CANTimeoutException ex) {
         //   m_tiltJag = true;
           // System.err.println("****************CAN timeout***********");
            //return true;
       // }
    //}
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
    
    public double distanceFromAngle(double angle) {
        double distance = 0.0;
        distance = Math.sqrt(angleAddition - (angleMultiplier * Math.cos(angle)));
        return distance;
    }
}

