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
import edu.wpi.first.wpilibj.templates.commands.TilterJoystick;

/**
 *
 * @author David + Ag
 */
//must wait until there is a more definitive idea ie. must be built or described better - David
public class Tilter extends Subsystem {
    CANJaguar TilterJag;
    
    private boolean m_tiltJag = false;
    //these numbers will have to be changed depending on the speed of the motors
    private static final double defaultDownSpeed = 1;
    private static final double defaultUpSpeed = -1;
    private static final double defaultTiltMax = 1;
    public static final double HomeAngle = 0;
    public static final double DownAngle = -3;
    // Tilter Dimensions, specified in inches
    private static final double dimensionA = 5.0;
    private static final double dimensionB = 5.0;
    private double angleAddition;
    private double angleMultiplier;
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public Tilter()  {
         try {
                TilterJag = new CANJaguar(Wiring.TilterCANID);
                TilterJag.enableControl();
                TilterJag.configEncoderCodesPerRev(360);
                TilterJag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                
            }
        catch (CANTimeoutException ex) {
        }
        angleAddition = (dimensionA * dimensionA) + (dimensionB * dimensionB);
        angleMultiplier = 2 * dimensionA * dimensionB;
    }
    /**
     * This increases the angle by a set default speed
     */
    public void increase(){
        TilterOpenLoop(defaultDownSpeed);
    }
    /**
     * This decreases the angle by a set default speed
     */
    public void decrease(){
        TilterOpenLoop(defaultUpSpeed);
    }
    /**
     * This stops the Tilter
     */
    public void Tiltstop(){
        TilterOpenLoop(0.0);
        
    }
   /**
     * This enables closed loop on the Tilter
     */
    public void enableClosedLoop(){
                try {
                    TilterJag.changeControlMode(CANJaguar.ControlMode.kPosition);
                    TilterJag.setPID(0.005,0.02,0);
                    TilterJag.enableControl();
                }
                catch (CANTimeoutException ex){
                    System.err.println("****************CAN timeout*************");
                    
                }
    }
    /**
     * This enables open loop
     */
    public void enableOpenLoop(){
        try{
            TilterJag.disableControl();
        }
        catch (CANTimeoutException ex){
        }
    }
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new TilterJoystick());
    }
    /**
     * This is the open loop stuff for the Tilter
     */
    public void TilterOpenLoop(double power){
        if(TilterJag != null) {
            try {
                TilterJag.setX(power);
                TilterJag.disableControl();
            } catch(CANTimeoutException ex) {
                m_tiltJag = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        
    }
    /**
     * This is the closed loop stuff for the Tilter
     */
    public void TilterClosedLoop(double distance) {
        //need to convert distance input (inches) to something the jag can use (rotations)
        if(TilterJag != null) {
            try {
                TilterJag.setX(distance);
                // Need to determine encoder codes per rev
                TilterJag.configEncoderCodesPerRev(360);
                TilterJag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                TilterJag.changeControlMode(CANJaguar.ControlMode.kPosition);
                TilterJag.setPID(0.005,0.02,0);
                TilterJag.enableControl();
            } catch(CANTimeoutException ex) {
                m_tiltJag = true;
                System.err.println("****************TiltCAN timeout***********");
            }
        }
    }
    /**
     * This the math for the Tilter, used in the Closed Loop
     */
    public double distanceFromAngle(double angle) {
        double distance = 0.0;
        distance = Math.sqrt(angleAddition - (angleMultiplier * Math.cos(angle)));
        return distance;
    }
}

