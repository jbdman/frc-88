/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Joystick.ButtonType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
/**
 *
 * @author TJ2
 */
public class Lifter extends Subsystem {
      
    private Joystick controller;
    private CANJaguar m_lifterMotor;
   double lifterMotorPower;
    public Lifter() {
        try {
            m_lifterMotor = new CANJaguar(Wiring.lifterMotorCANID);
        } catch (CANTimeoutException ex) {
            System.err.println("CAN Init error: ID " + Wiring.lifterMotorCANID);
            
            try{
                m_lifterMotor.setX(lifterMotorPower);
            }
            catch (CANTimeoutException fx) {
            System.err.println("CAN Timeout");
            }
            }
        }

   public void teleopInit() {
        lifterMotorPower = 0;
    }

  public void teleop(){
      double x = controller.getX();
      final int max = 100;
      final int offset = 1;
      
  
    if(x>.2) {
        lifterMotorPower = offset;
        }
  if (x<-.2) {
        lifterMotorPower -= offset;
        }

      //limits on motor

  if (lifterMotorPower>max) {
        lifterMotorPower = max;
      }
  if (lifterMotorPower<0) {
        lifterMotorPower = 0;
        
      }
  }
    public void initDefaultCommand() {    
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
