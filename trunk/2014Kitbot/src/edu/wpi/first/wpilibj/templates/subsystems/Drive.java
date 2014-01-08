/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.templates.commands.DrivewithController;

/**
 * @author David
 */
public class Drive extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private CANJaguar leftJag = null;
    private CANJaguar rightJag = null;
    private CANJaguar leftJag2 = null;
    private CANJaguar rightJag2 = null;
    private boolean m_fault = false;
    private static final int DRIVE_ENCODER_LINES = 250;
    private static final double DISTANCE_PER_REVOLUTION = 14.660;
    private boolean m_closedLoop;
    public Drive()  {

        try {
                
                leftJag = new CANJaguar(Wiring.leftCANDrive);
                leftJag2 = new CANJaguar(Wiring.leftCANDrive2);
                leftJag.disableControl();
                //if (leftJag != null) {
                 //   leftJag.configEncoderCodesPerRev(DRIVE_ENCODER_LINES);
                  //  leftJag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                    //leftJag.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
                    //leftJag.configNeutralMode(CANJaguar.NeutralMode.kCoast);
                    //leftJag.setX(0);
                    //leftJag2.setX(0);
                    
                //}
                
                //leftJag.changeControlMode(CANJaguar.ControlMode.kVoltage);
                //leftJag2.changeControlMode(CANJaguar.ControlMode.kVoltage);
                
            }
            catch (CANTimeoutException ex) {
                System.out.println("***CAN ERROR***");
                m_fault = true;
            }
        
        try {
                rightJag = new CANJaguar(Wiring.rightCANDrive);
                rightJag2 = new CANJaguar(Wiring.rightCANDrive2);
                rightJag2.disableControl();
                //if (rightJag2 != null) {
                    //rightJag2.configEncoderCodesPerRev(DRIVE_ENCODER_LINES);
                    //rightJag2.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                    //rightJag2.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
                    //rightJag2.configNeutralMode(CANJaguar.NeutralMode.kCoast);
                //}
                
            }
            catch  (CANTimeoutException ex) {
                System.out.println("***CAN ERROR***");
                m_fault = true;
            }
    }
    
    /**
     * Enables ClosedLoop control Driving. It sets it to speed.
     */
    //public void enableClosedLoop() {
    //    double position;

        //if(rightJag2 != null && leftJag != null) {
            //try {
                // set the right motor to closed loop
                //rightJag2.changeControlMode(CANJaguar.ControlMode.kSpeed);
                //rightJag2.setPID(1, 0.0, 0.0);
                //position = rightJag2.getPosition();
                //rightJag2.enableControl();
                // now the left motor
                //leftJag.changeControlMode(CANJaguar.ControlMode.kSpeed);
                //leftJag.setPID(1, 0.0, 0.0);
                //position = leftJag.getPosition();
                //m_closedLoop = true;
                //leftJag.enableControl();
            //} catch (CANTimeoutException ex) {
               // m_fault = true;
                //System.err.println("CAN timeout");
                //SmartDashboard.putString("Drive Fault", ex.getMessage());
            //}
        //}
    //}
    
     /**
     * Disables the Drive closed loop and puts it into open loop.
     */
//    public void disableClosedLoop() {
//        if(rightJag2 != null && leftJag != null) {
//            try {
//                // set the right motor to open loop
//                rightJag2.disableControl();
//                rightJag2.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
//                // now the left motor
//                leftJag.disableControl();
//                leftJag.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
//                m_closedLoop = false;
//            } catch (CANTimeoutException ex) {
//                m_fault = true;
//                System.err.println("CAN timeout");
//                SmartDashboard.putString("Drive Fault", ex.getMessage());
//            }
//        }
//    }
//    
//    /**
//     * Returns whether or not Drive is in ClosedLoop. If it is it will return true and if it is not
//     * it will return false.
//     */
//    public boolean isClosedLoop() {
//        return m_closedLoop;
//    }
//    
//    public void driveTankClosedLoop(double speedLeft, double speedRight) {
//        double scaleToRPM = 60.0 / DISTANCE_PER_REVOLUTION;
//
//        if(leftJag != null) {
//            try {
//                leftJag.setX(speedLeft * scaleToRPM);
//                leftJag2.setX(speedLeft * scaleToRPM);
//            } catch(CANTimeoutException ex) {
//                m_fault = true;
//                System.err.println("****************CAN timeout***********");
//                SmartDashboard.putString("Drive Fault", ex.getMessage());
//            }
//        }
//        if(rightJag != null) {
//            try {
//                rightJag.setX(-speedRight * scaleToRPM);
//                rightJag2.setX(-speedRight * scaleToRPM);
//            } catch(CANTimeoutException ex) {
//                m_fault = true;
//                System.err.println("****************CAN timeout***********");
//                SmartDashboard.putString("Drive Fault", ex.getMessage());
//            }
//        }
//    }

    public void driveTankOpenLoop(double left, double right) {

        if(leftJag != null || leftJag2 !=null) {
            try {
                leftJag.setX(left);
                leftJag2.setX(left);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
                SmartDashboard.putString("Drive Fault", ex.getMessage());
            }
        }
        if(rightJag != null || rightJag2 != null) {
            try {
                rightJag.setX(-right);
                rightJag2.setX(-right);
                //rightJag2.getFaults();
                
                
//                rightJag.disableControl();
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
                SmartDashboard.putString("Drive Fault", ex.getMessage());
            }
        }
    }
        private double encoderToDistance(double revolutions) {
        /* convert from revolutions at geabox output shaft to distance in inches
         * ouput sproket has 18 teeth, and wheel sproket has 22 teeth
         * wheel diameter is nominal 6 inches.
         */
        return DISTANCE_PER_REVOLUTION * revolutions;
    }
            /**
     * Current speed of left wheel
     * 
     * @return speed of left wheel in inches per second
     */
    public double getLeftSpeed() {

        double speed = 0.0;
        try {
            speed = leftJag.getSpeed();
        } catch(CANTimeoutException ex) {
            m_fault = true;
            System.err.println("****************CAN timeout***********");
            SmartDashboard.putString("Drive Fault", ex.getMessage());
        }
        // convert rpm to inches per second
        return encoderToDistance(speed)/60.0;
    }
    
    /**
     * Current speed of right wheel
     * 
     * @return speed of right wheel in inches per second
     */
    public double getRightSpeed() {

        double speed = 0.0;
        try {
            speed = -rightJag.getSpeed();
        } catch(CANTimeoutException ex) {
            m_fault = true;
            System.err.println("****************CAN timeout***********");
            SmartDashboard.putString("Drive Fault", ex.getMessage());
        }
        return encoderToDistance(speed)/60.0;
    }
    
    /**
     * Returns the value of the fault flag
     *
     */
    public boolean getFault() {
        return m_fault;
    }


    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new DrivewithController());
    }
}
