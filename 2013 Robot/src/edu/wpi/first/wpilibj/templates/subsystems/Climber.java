/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.templates.commands.ClimberStop;
import edu.wpi.first.wpilibj.templates.commands.ClimberJoysick;
import edu.wpi.first.wpilibj.camera.AxisCamera;
import edu.wpi.first.wpilibj.camera.AxisCameraException;

/**
 * @author David
 */
public class Climber extends Subsystem {
    CANJaguar ClimbJag;
    //speeds can change if needed as of now full power will give you full power
    private boolean m_fault = false;
    private boolean m_calibrated = false;
    
    private static final double revPerInch = 0.1;
    private static final double defaultDownSpeed = -1.0;
    private static final double defaultUpSpeed = 1.0;
    private static final double defaultMaxSpeed = 1;
    private double m_setPoint = 0.0;
    private AxisCamera camera;

    public Climber() {
        
        try {
                ClimbJag = new CANJaguar(Wiring.climberCANID);
                
                // encoder configuration
                ClimbJag.configEncoderCodesPerRev(100);
                ClimbJag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                // default to open loop
                ClimbJag.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                //ClimbJag.setPID(100.0,0.10,0);
            }
            catch (CANTimeoutException ex) {
            }

        // shouldn't this be somewhere more logical?
        camera = AxisCamera.getInstance();
    }
    
    public void initDefaultCommand() {
        setDefaultCommand(new ClimberJoysick());
    }

    /**
     * Stops the climber motor.
     */
    public void stop() {
        ClimbOpenLoop(0.0);
    }
    /**
     * Drives the climber up at the default speed.
     */
    public void up() {
        ClimbOpenLoop(defaultUpSpeed);
    }
    /**
     * Drives the climber down at the default speed.
     */
    public void down() {
        ClimbOpenLoop(defaultDownSpeed);
    }
    /**
     * Sets m_climbfault to true, indicating an error with the
     * climber system.
     */
    public void setfault() {
        m_fault = true;
    }

    /**
     * Sets calibration flag, which means the homing has been completed
     */
    public void setCalibrated() {
        m_calibrated = true;
    }

    /**
     * Resets calibration flag, which means the homing has not completed
     */
    public void unsetCalibrated() {
        m_calibrated = false;
    }

    /**
     * Returns the value of the calibration flag
     * 
     * @return  True if the Climber subsystem has been calibrated
     */
    public boolean isCalibrated() {
        return m_calibrated;
    }

    /**
     * Gets whether or not the lower limit of the Climber mast has been tripped,
     * i.e. the mast is at it's *highest* position.
     * This sensor is also a hard limit switch for the Jaguar.
     * 
     * @return  True if the Climber has tripped the sensor indicating full up position.
     */
    public boolean lowerLimitTripped() {
        try {
            /**
             * The sensor is wired to the forward limit switch of the Jaguar
             * The getForwardLimitOK() method returns false when the switch is tripped
            **/
            return !ClimbJag.getForwardLimitOK();
        } catch(CANTimeoutException ex) {
            m_fault = true;
            System.err.println("****************CAN timeout***********");
            return true;
        }
    }

    /**
     * Gets whether or not the upper limit of the Climber mast has been tripped,
     * i.e. the mast is at it's *lowest* position.
     * This sensor is also a hard limit switch for the Jaguar.
     * 
     * @return  True if the Climber has tripped the sensor indicating full down position.
     */
    public boolean upperLimitTripped() {
        try {
            /**
             * The sensor is wired to the reverse limit switch of the Jaguar
             * The getReverseLimitOK() method returns false when the switch is tripped
            **/
            return !ClimbJag.getReverseLimitOK();
        } catch(CANTimeoutException ex) {
            m_fault = true;
            System.err.println("****************CAN timeout***********");
            return true;
        }
    }
    

    /**
     * Open loop control for the climb mast, using voltage percentage.
     * 
     * @param   power   The voltage of the climb motor specified
     *                  from -1.0 to 1.0.
     */
    public void ClimbOpenLoop(double power) {

        if(ClimbJag != null) {
            try {
                //play with stuff under to see if it needs to be inverted
                ClimbJag.setX(power * defaultMaxSpeed);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }            
      
}
    /**
     * Closed loop control for the climb mast.
     * 
     * @param   vertical    The vertical distance to drive to.  A distance of 0
     *                      is the lower limit of the mast.  The distance is
     *                      specified in ??? and should always be positive.
     */
    public void ClimbClosedLoop(double vertical) {
        // Need to change vertical distance (specified in inches) to something
        // the Jaguar can use (revs)
        // Also ensure that vertical is positive.
      // convert from inches to revolutions
      double revolution = vertical * revPerInch;
      m_setPoint = revolution;
        if(ClimbJag != null) {
            try {
                //the formula below will probably be subject to change
                // may need to be inverted (is this inversion correct?)  2/9
                ClimbJag.setX(revolution);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }   
    }

  public boolean atSetpoint() {
      return getRevolution() == m_setPoint;
      //// THIS WILL NOT WORK!!!!
      // SHOULD CHECK FOR WITHIN TOLERANCE
  }
  
  private double getRevolution() {
      double revolution = 0.0;
      try {
        //the formula below will probably be subject to change
        //also play with stuff under to see if it needs to be inverted
                revolution = ClimbJag.getPosition();
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
      return revolution;
  }
     
    public double getPosition() {
      return getRevolution() / revPerInch; 
    }
}
