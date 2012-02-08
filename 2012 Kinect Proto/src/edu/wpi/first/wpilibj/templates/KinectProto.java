/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.KinectStick;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class KinectProto extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */

    // safety factor so Kinect drives more slowly (for now)
    private double kinectSensitivity = 0.5;
    // we also need to scale back rotation...
    private double turnSensitivity   = 0.5;

    // Jaguar object for drive
    private CANJaguar frontLeft;
    private CANJaguar frontRight;
    private CANJaguar backLeft;
    private CANJaguar backRight;

    //inirializing xbox 360 controller control
    private Joystick joystick;

    //initializing joystick control
    private KinectStick kinectRight;
    private KinectStick kinectLeft;
    public void robotInit() {
        //joystick initializing
        kinectRight = new KinectStick(1);
        kinectLeft  = new KinectStick(2);
        joystick = new Joystick (1);
        System.out.println("CANinitializing");

        try{
            frontLeft = new CANJaguar(3);
            frontRight = new CANJaguar(5);
            backLeft = new CANJaguar(6);
            backRight = new CANJaguar(2);
        }
        catch (CANTimeoutException e) {
            System.err.println("CAN failure");
        }

        System.out.println("systemDone");
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        double yDriveValue =  kinectSensitivity * -kinectRight.getY();
        double xDriveValue = 0;
        double turnDriveValue = kinectSensitivity * turnSensitivity * kinectLeft.getY();

        drive(yDriveValue, xDriveValue, turnDriveValue);

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {

        double yDriveValue =  -joystick.getY();
        double xDriveValue = joystick.getX();
        double turnDriveValue = turnSensitivity * joystick.getRawAxis(4);

        drive(yDriveValue, xDriveValue, turnDriveValue);
    }

    private void drive (double forward, double sideways, double turn ) {

        double setFrontLeft  = forward + sideways + turn;
        double setFrontRight = forward - sideways - turn;
        double setBackLeft   = forward - sideways + turn;
        double setBackRight  = forward + sideways - turn;

        try {
            frontLeft.setX(-setFrontLeft);
            frontRight.setX(setFrontRight);
            backLeft.setX(-setBackLeft);
            backRight.setX(setBackRight);
        }
        catch(CANTimeoutException e) {
            System.err.println("CAN timeout");
        }
    }
}