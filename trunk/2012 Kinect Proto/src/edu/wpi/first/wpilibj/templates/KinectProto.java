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
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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

    //Jaguar objects for shooter, turret, and string theory
    private CANJaguar rampPusher;
    private CANJaguar stringTheoryFront;
    private CANJaguar stringTheoryVertical;
    private CANJaguar turretMotor;
    private CANJaguar shooterUpper;
    private CANJaguar shooterLower;

    //limit switch for ramp pusher
    
    private DigitalInput rampPusherLimitSwitch;

    //variables
    public double turretGo;
    public double turretDriveValue;
    public double pusherPower;
    public boolean rampPusherRaise;
    public boolean rampPusherLower;
    public double rampPusherLastButton;
    public boolean rampPusherLimitSwitchReading;
    public double shooterUpperPower;
    public double stringTheoryFrontDrive;
    public double stringTheoryBackDrive;
    public double stringTheoryVerticalDrive;
    public double shooterSwitchControl;
    public boolean shooterSwitch;
    public double shooterSpeedControl;
    public double shooterTilt;
    public boolean shooterTiltAngle;
    public double stringTheorySwitchInput;
    public boolean stringTheorySwitch;

    //initializing pneumatics
    public Compressor compressor;
    public Solenoid landingGear;
    public Solenoid shooterLoader;
    public Solenoid shooterTilter;

    //inirializing xbox 360 controller control
    private Joystick driverJoystick;
    private Joystick operatorJoystick;

    //initializing joystick control
    private KinectStick kinectRight;
    private KinectStick kinectLeft;


    public void robotInit() {
        //joystick initializing
        kinectRight = new KinectStick(1);
        kinectLeft  = new KinectStick(2);
        driverJoystick = new Joystick (1);
        operatorJoystick = new Joystick (2);

        //initializing ramp pusher states
        rampPusherRaise=true;
        rampPusherLower=true;
        rampPusherLastButton=0;

        //initializing compressor & pneumatics
        compressor = new Compressor (3,8);              //FIX THIS!! <-- correct as of 2/15
        compressor.start();
        shooterLoader = new Solenoid (4);               //FIX THIS!! <-- correct as of 2/15
        shooterTilter = new Solenoid (5);               //FIX THIS!!
        landingGear = new Solenoid (6);                 //FIX THIS!! <-- correct as of 2/15


        //initializing sensors
        rampPusherLimitSwitch = new DigitalInput(1);

        System.out.println("CANinitializing");

        try{
            frontLeft = new CANJaguar(10);              //FIX THIS!!
            frontRight = new CANJaguar(2);              //FIX THIS!!
            backLeft = new CANJaguar(8);                //FIX THIS!!
            backRight = new CANJaguar(11);              //FIX THIS!!
            rampPusher = new CANJaguar(3);              //FIX THIS!!
            stringTheoryFront = new CANJaguar(7);       //FIX THIS!!
            stringTheoryVertical = new CANJaguar(4);    //FIX THIS!!
            turretMotor = new CANJaguar(9);             //FIX THIS!!
            shooterUpper = new CANJaguar(6);            //FIX THIS!!
            shooterLower = new CANJaguar (5);           //FIX THIS!!
            //As of 2/15, Jag listings are correct.  Jag #4 is physically non-functional
            //and needs replacement.
        }

        catch (CANTimeoutException e) {
            System.err.println("******************CAN failure******************");
        }

        // configure shooter geartooth sensors
        try {
            shooterLower.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
            shooterUpper.setSpeedReference(CANJaguar.SpeedReference.kEncoder);
            shooterLower.configEncoderCodesPerRev(11);
            shooterUpper.configEncoderCodesPerRev(11);
        } catch (CANTimeoutException ex) {
            System.err.println("******************CAN timeout******************");
        }
        System.out.println("systemDone");
    }

    // Timer for autonomous
    Timer autoTime = new Timer();

    public void autonomousInit() {
        // set up timer
        autoTime.start();
        autoTime.reset();
        
    }
        /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        double time;

        time = autoTime.get();

        // start shooter motors
        if(time <0.2) {
            try {
                shooterUpper.setX(0.30);
                shooterLower.setX(0.45);
            } catch (CANTimeoutException ex) {
                System.err.println("########## CAN Timeout ##########");
            }
            shooterTilter.set(false);
        }
        // start turret motor
//        if(time > 0.5 && time < 0.6) {
//            try {
//                turretMotor.setX(0.8);
//            } catch (CANTimeoutException ex) {
//                System.err.println("########## CAN Timeout ##########");
//            }
//        }

//        // stop turret motor
//        if(time > 1.5 && time < 1.6) {
//            try {
//                turretMotor.setX(0.0);
//            } catch (CANTimeoutException ex) {
//                System.err.println("########## CAN Timeout ##########");
//            }
//        }
//
        if(time > 3.0 && time < 3.1) {
            shooterLoader.set(true);
            try {
                stringTheoryVertical.setX(1.0);
                stringTheoryFront.setX(1.0);
            } catch (CANTimeoutException ex) {
                System.err.println("########## CAN Timeout ##########");
            }
        }
        if(time > 4.0 && time < 4.1) {
            shooterLoader.set(false);
            shooterTilter.set(true);
        }
        if(time > 6.0 && time < 6.1) {
            shooterLoader.set(true);
        }

        if(time > 7.0 && time < 7.1) {
            shooterLoader.set(false);
            try {
                shooterUpper.setX(0.0);
                shooterLower.setX(0.0);
            } catch (CANTimeoutException ex) {
                System.err.println("########## CAN Timeout ##########");
            }
            shooterTilter.set(false);
        }

        if(autoTime.get() > 5.0) {
            double yDriveValue =  kinectSensitivity * -kinectRight.getY();
            double xDriveValue = 0;
            double turnDriveValue = kinectSensitivity * turnSensitivity * kinectLeft.getY();


            drive(yDriveValue, xDriveValue, turnDriveValue);
        }

        updateDashboard();

    }

    public void disabledPeriodic() {
        updateDashboard();
    }


    public void teleopInit(){

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {

        //code added for quick test of drive / shooter / turret
        double turretGo = 0;
        double turretDriveValue = 0;
        double stringTheoryFrontDrive = 0;
        double stringTheoryVerticalDrive = 0;
        double shooterSwitchControl = -2;
        boolean shooterSwitch = false;
        double shooterSpeedControl = 0.5;
        double shooterTilt = 2;
        boolean shooterTiltAngle = false;
        double stringTheorySwitchControl = -2;
        boolean stringTheorySwitch = false;
      

        boolean slow = false;

        if(Math.abs(driverJoystick.getZ()) > 0.5) {
            slow = true;
        }

        double yDriveValue =  -map(driverJoystick.getY());
        double xDriveValue = map(driverJoystick.getX());
        double turnDriveValue = turnSensitivity * map(driverJoystick.getRawAxis(4));


        if(slow) {
            yDriveValue *=0.5;
        }

        drive(yDriveValue, xDriveValue, turnDriveValue);

        //Right thumbpad to the right will cause the turret motor to drive 'forward'
        if (operatorJoystick.getRawAxis(4)>0.25){
            turretDriveValue = operatorJoystick.getRawAxis(4)*0.5;
            turretGo = 0.1 + turretDriveValue;
             try {
                turretMotor.setX(turretGo);
            }
            catch(CANTimeoutException e) {
                System.err.println("****************CAN timeout***********");
            }
        }
        else  if (operatorJoystick.getRawAxis(4) < -0.25) {
            turretDriveValue = operatorJoystick.getRawAxis(4)*0.5;
            turretGo = -0.1 + turretDriveValue;
             try {
                turretMotor.setX(turretGo);
            }
            catch(CANTimeoutException e) {
                System.err.println("****************CAN timeout***********");
            }
        }
        else {
            try {
                turretMotor.setX(0);
            }
            catch(CANTimeoutException e) {
                System.err.println("****************CAN timeout***********");
            }
        }

        //The Front Left Trigger on the operator control causes String Theory
        //to turn on if it's off or off if it's on;
         if (operatorJoystick.getRawButton(5)){
             stringTheorySwitchControl = stringTheorySwitchControl * -1;
         }

         if (stringTheorySwitchControl>0){
             stringTheorySwitch = true;
         }
            else stringTheorySwitch = false;


         if (stringTheorySwitch){
            stringTheoryFrontDrive = 0.75;
            stringTheoryVerticalDrive = 0.75;
            try {
                stringTheoryFront.setX(stringTheoryFrontDrive);
                stringTheoryVertical.setX(stringTheoryVerticalDrive);
            }
            catch(CANTimeoutException e) {
                System.err.println("****************CAN timeout***********");
            }
        }
            else try {
                stringTheoryFront.setX(0);
                stringTheoryVertical.setX(0);
            }
            catch(CANTimeoutException e) {
                System.err.println("****************CAN timeout***********");
            }

        //the following lines control the ramp pusher
        pusherPower = 0.0;
        
        if (driverJoystick.getRawButton(3)){
            pusherPower = 1;
            rampPusherLastButton=1;
            rampPusherLower = true;
        } else if (driverJoystick.getRawButton(2)){
            pusherPower = -1;
            rampPusherLastButton=2;
            rampPusherRaise=true;
        }
        else
             pusherPower = 0;

        rampPusherLimitSwitchReading = rampPusherLimitSwitch.get();

        if (!rampPusherLimitSwitchReading){
            pusherPower = 0;
        }


        try {
                rampPusher.setX(pusherPower);
            }
            catch(CANTimeoutException e) {
                System.err.println("****************CAN timeout***********");
            }
        
         //The Front Right trigger button will cause both shooter motors to turn on, if they are off,
        // or off, if they are on.  The 'X' and 'B' buttons will change the speed of the shooter motors
         if (operatorJoystick.getRawButton(6)) {
            shooterSwitchControl = 1;
            shooterSpeedControl=0.2 + operatorJoystick.getRawAxis(3)*0.5;
            }
            else shooterSwitchControl = -1;

        if (shooterSwitchControl>0){
            shooterSwitch = true;
        }
            else shooterSwitch = false;


        if(shooterSwitch){

            try {
                shooterUpper.setX(shooterSpeedControl);
                shooterLower.setX(shooterSpeedControl);
            }
            catch(CANTimeoutException e) {
                System.err.println("****************CAN timeout***********");
            }
        }
            else{
           try {
                shooterUpper.setX(0);
                shooterLower.setX(0);
            }
            catch(CANTimeoutException e) {
                System.err.println("****************CAN timeout***********");
            }
 }

         //Button 'A' on the operator controller fires the pneumatic
    //piston that loads the shooter
        if (operatorJoystick.getRawButton(1)){
            shooterLoader.set(true);
        }
             else shooterLoader.set(false);

        //Button 'Y' on the operator controller fires the pneumatic piston that lowers
        // the landing gear
         if (operatorJoystick.getRawButton(10)){
            landingGear.set(true);
        }
             else landingGear.set(false);

        //Button 'Y on the operator controller causes the shooter to switch
        //its tilt position. Please note... the 'SET' for that solenoid is
        //OUTSIDE of the if statement.  The 'IF' should change the direction
        //of the set when pressed, but then the tilt should stay there

        if (operatorJoystick.getRawButton(4)){
            shooterTilt = shooterTilt * -1;
        }

        if (shooterTilt>1){
            shooterTiltAngle = true;
        }
        
        else{
            shooterTiltAngle = false;
        }
        shooterTilter.set(shooterTiltAngle);

        updateDashboard();
    }

    private int updateCount = 0;

    private void updateDashboard() {

        if(updateCount % 5 == 0) {
            double upperRPM = 0.0;
            double lowerRPM = 0.0;

            try {
                upperRPM = shooterUpper.getSpeed();
                lowerRPM = shooterLower.getSpeed();
            } catch (CANTimeoutException ex) {
                System.err.println("******************CAN timeout******************");
            }

            SmartDashboard.putDouble("Upper rpm", upperRPM);
            SmartDashboard.putDouble("Lower rpm", lowerRPM);

            updateCount++;
        }
    }

    private double map(double val) {

        double out = 0.0;
        if(val > 0.2) {
            out = (val - 0.2)/0.8;
        } else{
            if(val < -0.2) {
            out = (val + 0.2)/0.8;
            }
        }

        return out;
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
            System.err.println("****************CAN timeout***********");
        }
    }
}
