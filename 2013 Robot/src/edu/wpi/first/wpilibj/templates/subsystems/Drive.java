/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.templates.commands.DrivewithController;
/**
 *
 * @author David
 */
public class Drive extends Subsystem {
    // 1/24/12 drive motors may have to be inverted because they are subject to switch
    CANJaguar leftJag = null;
    CANJaguar rightJag = null;
//    RobotDrive drive;
    private boolean m_fault = false;
    /* Set the maximum change in voltage in 1 sec
     * Smaller values prevent the robot from rocking backwards under acceleration
     */
    private static final double MOTOR_RAMP_RATE = 15;
    
    /** 
     * Initializes Jaguars and sets up PID control.
     */
    public Drive()  {
        System.out.println("*** Drive constuctor start ***");

        System.out.println("*** Left drive CAN ***");
        
        try {
                
                leftJag = new CANJaguar(Wiring.driveLeftCANID);
                
                // Fix this to cope with failure to create leftJag
                if (leftJag != null) {
                // Need to determine encoder codes per rev
                    leftJag.configEncoderCodesPerRev(250);
                    leftJag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
//                    leftJag.changeControlMode(CANJaguar.ControlMode.kPosition);
//                    leftJag.setPID(0.005,0.02,0);
//                    leftJag.enableControl();
                    leftJag.setVoltageRampRate(MOTOR_RAMP_RATE);
                    leftJag.configNeutralMode(CANJaguar.NeutralMode.kCoast);
                }
            }
            catch (CANTimeoutException ex) {
                System.out.println("***CAN ERROR***");
                m_fault = true;
                System.out.println("*** CAN ERROR ***");
            }
        
        System.out.println("*** Right drive CAN ***");

        
        try {
                rightJag = new CANJaguar(Wiring.driveRightCANID);
                if (rightJag != null) {
                    rightJag.configEncoderCodesPerRev(250);
                    rightJag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
//                rightJag.changeControlMode(CANJaguar.ControlMode.kPosition);
//                rightJag.setPID(0.005,0.02,0);
//                rightJag.enableControl();
                    rightJag.setVoltageRampRate(MOTOR_RAMP_RATE);
                    rightJag.configNeutralMode(CANJaguar.NeutralMode.kCoast);
                }
            }
            catch  (CANTimeoutException ex) {
                System.out.println("***CAN ERROR***");
                m_fault = true;
            }
        System.out.println("*** Drive constuctor end ***");

    }
    /**
     * Sets the default command to DriveWithController so that when nothing
     * else is happening with the Drive, we can use controllers to move.
     */
    public void initDefaultCommand() {
        setDefaultCommand(new DrivewithController());
    }
 
    /**
     * Set the neutral mode to brake or coast for *both* drive motors
     * 
     * @param brake True for brake, false for coast
     * 
     */
    public void setBrake(boolean brake) {
        CANJaguar.NeutralMode mode = CANJaguar.NeutralMode.kCoast;
        
        if(brake) {
            mode = CANJaguar.NeutralMode.kBrake;
        }
        try {
            leftJag.configNeutralMode(mode);
            rightJag.configNeutralMode(mode);
        } catch  (CANTimeoutException ex) {
            System.out.println("***CAN ERROR***");
            m_fault = true;
        }
    }
    
    /**
     * Open loop control of the Drive, using voltage percentage.
     * 
     * @param   left    The voltage for the left side of the Drive.  Value
     *                  should be specified between -1.0 and 1.0.
     * @param   right   The voltage for the right side of the Drive. Value
     *                  should be specified between -1.0 and 1.0.
     */
    public void driveTankOpenLoop(double left, double right) {

        if(leftJag != null) {
            try {
                leftJag.setX(left);
//                leftJag.disableControl();
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        if(rightJag != null) {
            try {
                rightJag.setX(-right);
//                rightJag.disableControl();
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
    }
    
    /**
     * Close loop control for the Drive, using encoders to control speed.
     * 
     * @param   left    The speed for the left side of the Drive.  Value
     *                  should be positive and be below the Drive's max speed.
     * @param   right   The speed for the right side of the Drive.  Value
     *                  should be positive and be below the Drive's max speed.
     */
    public void driveTankClosedLoop(double speedLeft, double speedRight) {
        // Change to max speed wanted
        // Why are we multiplying by maxRPM?
        int maxRPM = 100;
        if(leftJag != null) {
            try {
                leftJag.setX(speedLeft * maxRPM);
                leftJag.enableControl();
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        if(rightJag != null) {
            try {
                rightJag.setX(-speedRight * maxRPM);
                rightJag.enableControl();
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
    }

    /**
     * The distance traveled by the left wheel since Jag powered on
     * 
     * @return Total distance traveled by left wheel in units TBD 
     */
    public double getLeftDistance() {

        double position = 0.0;
        try {
            position = leftJag.getPosition();
        } catch(CANTimeoutException ex) {
            m_fault = true;
            System.err.println("****************CAN timeout***********");
        }
        return position;
    }
    
    /**
     * The distance traveled by the right wheel since Jag powered on
     * 
     * @return Total distance traveled by right wheel in units TBD 
     */
    public double getRightDistance() {

        double position = 0.0;
        try {
            position = rightJag.getPosition();
        } catch(CANTimeoutException ex) {
            m_fault = true;
            System.err.println("****************CAN timeout***********");
        }
        return position;
    }
    
    /**
     * Current speed of left drive gearbox output? shaft in rpm
     * 
     * @return speed of left drive gearbox output in rpm
     */
    public double getLeftSpeed() {

        double speed = 0.0;
        try {
            speed = leftJag.getSpeed();
        } catch(CANTimeoutException ex) {
            m_fault = true;
            System.err.println("****************CAN timeout***********");
        }
        return speed;
    }
    
    /**
     * Current speed of right drive gearbox output? shaft in rpm
     * 
     * @return speed of right drive gearbox output in rpm
     */
    public double getRightSpeed() {

        double speed = 0.0;
        try {
            speed = rightJag.getSpeed();
        } catch(CANTimeoutException ex) {
            m_fault = true;
            System.err.println("****************CAN timeout***********");
        }
        return speed;
    }
    
    /**
     * Returns the value of the fault flag
     *
     */
    public boolean getFault() {
        return m_fault;
    }

}
