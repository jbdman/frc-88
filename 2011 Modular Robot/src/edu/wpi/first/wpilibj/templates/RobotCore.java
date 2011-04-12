/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Gyro;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.DriverStationLCD.Line;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

public class RobotCore extends IterativeRobot {

    /**
     * Construct everything we need - controller, sensors, digital I/O's,
     * jaguars, and any variables we're going to use
     */

    // private Watchdog watchdog;
    private Joystick driveControl;
    private Joystick operatorControl;
//    private Mechanum drive;
    private CANJaguar frontLeft;
    private CANJaguar rearLeft;
    private CANJaguar frontRight;
    private CANJaguar rearRight;
    private Elevator elevator;
//    private CANJaguar elevatorDrive;
    private Gyro gyro;
    private AnalogChannel pressureSensor;
    private DriverStationLCD DSmessage;
    private RobotUtil util;
    private int iterator = 0;

    // Pneumatic objects
    private Compressor compressor;
    private Arm arm;
    private Solenoid minibotLauncher;

    // Line sensor object
    private LineSensor lineSensor;

    // logoLight objects
    private LogoLight logoLight;
    private Solenoid logoLightPower;      // TEMPORARY!!!

    private double xDriveValue;
    private double yDriveValue;
    private double turnDriveValue;

    public double elevatorDriveSet;
    public double elevatorAutoStart;
    public double elevatorAutoDrive;
    public double elevatorAutoCurrent;
    public double elevatorAutoTiming;
    public boolean elevatorClockStart;
    int elevatorControlManual;
    boolean elevatorControlUp;
    boolean elevatorControlDown;
    double gyroReading;
    public double x;

    // timer for locking minibot launcher
    public Timer timer;

    // control turning sensitivity
    public double turnSensitivity;

    // construct watchdog


    public Watchdog watchdog;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */

    public void robotInit() {

    /**
     * Initialize the joystick, the gyro, and set local variables to their
     * initial values.
     */

    driveControl= new Joystick(1);
    operatorControl = new Joystick(2);
    xDriveValue=0;
    yDriveValue=0;
    turnDriveValue=0;
    turnSensitivity = 0.5;

    util = new RobotUtil();

    DSmessage = DSmessage.getInstance();

    String errorMsg = "ERR: CAN init";

    // set up compressor with pressure switch
    compressor = new Compressor(Wiring.DIGITAL_IN_COMPRESSOR_PRESSURE_SWITCH,
            Wiring.RELAY_COMPRESSOR_POWER);
    compressor.start();

    timer = new Timer();
    timer.start();

    /**
     * The following umpteen lines of code attempt to initialize each of the
     * jaguars.  If any of them aren't found, the program will choke up.
     */
System.out.println("Configuring Jags...");

     try{
    rearRight = new CANJaguar(2,CANJaguar.ControlMode.kPercentVbus);
    if(rearRight != null) {
        rearRight.configNeutralMode(CANJaguar.NeutralMode.kBrake);
        rearRight.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
        rearRight.configEncoderCodesPerRev(250);
    }
        }
        catch(CANTimeoutException e) {
            errorMsg += " (id 2)";
            logMessageSet(errorMsg);
            System.err.println(errorMsg);
        }

    try {
        rearLeft = new CANJaguar(3,CANJaguar.ControlMode.kPercentVbus);
        if(rearLeft != null) {
            rearLeft.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            rearLeft.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            rearLeft.configEncoderCodesPerRev(250);
            }
        }
        catch(CANTimeoutException e) {
            errorMsg += " (id 3)";
            logMessageSet(errorMsg);
            System.err.println(errorMsg);
        }
    try{
        frontRight = new CANJaguar(4,CANJaguar.ControlMode.kPercentVbus);
        if(frontRight != null) {
            frontRight.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            frontRight.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            frontRight.configEncoderCodesPerRev(250);
        }
    } catch(CANTimeoutException e) {
            errorMsg += " (id 4)";
            logMessageSet(errorMsg);
            System.err.println(errorMsg);
        }

    try {
        frontLeft = new CANJaguar(5,CANJaguar.ControlMode.kPercentVbus);
        if(frontLeft != null) {
            frontLeft.configNeutralMode(CANJaguar.NeutralMode.kBrake);
            frontLeft.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
            frontLeft.configEncoderCodesPerRev(250);
        }
    } catch(CANTimeoutException e) {
        errorMsg += " (id 5)";
        logMessageSet(errorMsg);
            System.err.println(errorMsg);
        }


//    try {
//        elevatorDrive = new CANJaguar(6,CANJaguar.ControlMode.kPercentVbus);
//        if(elevatorDrive != null) {
//            elevatorDrive.configNeutralMode(CANJaguar.NeutralMode.kBrake);
//            elevatorDrive.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
//            elevatorDrive.configEncoderCodesPerRev(360);
//        }
//    } catch(CANTimeoutException e) {
//        errorMsg += " (elevator)";
//        logMessageSet(errorMsg);
//        logMessageSet(errorMsg);
//        System.err.println(errorMsg);
//    }
        elevator = new Elevator(Wiring.CAN_ID_ELEVATOR);

System.out.println("Configuring Jags...Done");

    pressureSensor = new AnalogChannel(Wiring.ANALOG_PRESSURE_SENSOR);

    arm = new Arm(Wiring.SOLENOID_ELBOW_VALVE, Wiring.SOLENOID_GRIPPER_VALVE,
            Wiring.SOLENOID_PUSHER_VALVE);
    minibotLauncher = new Solenoid(4);

    // Line Sensor contructors
    lineSensor = new LineSensor(Wiring.SOLENOID_LINESENSOR_POWER, Wiring.DIGITAL_IN_LINESENSOR_LEFT,
            Wiring.DIGITAL_IN_LINESENSOR_MID, Wiring.DIGITAL_IN_LINESENSOR_RIGHT);

    logoLightPower = new Solenoid(Wiring.SOLENOID_LOGOLIGHT_POWER);
    logoLight = new LogoLight(Wiring.DIGITAL_OUT_LOGOLIGHT_A, Wiring.DIGITAL_OUT_LOGOLIGHT_B);

    watchdog=Watchdog.getInstance();
    watchdog.setExpiration(.75);
    watchdog.setEnabled(false);

    elevatorDriveSet=0;

    // send error messages to driver station
    logMessageCommit();

}

    public void disabledPeriodic() {
        util.updateDashboard();
    }

    private double autoFwd;
    private double autoSide;
    private double autoTurn;
    private double autoElevator;
    // *really* missing enums...
    private final int kStateInit = 0;
    private final int kStateSearchingForLine = 1;
    private final int kStateLostLine = 2;
    private final int kStateFoundLineStart = 3;
    private final int kStateSearchingForEnd = 4;
    private final int kStateRaiseArm = 5;
    private final int kStateScoreNudgeFwd = 6;
    private final int kStateScoreStart = 7;
    private final int kStateScoreReleaseTube = 8;
    private final int kStateScorePunchTube = 9;
    private final int kStateBackupStart = 10;
    private final int kStateBackup = 11;
    private final int kStateDone = 12;

    private int autoState;
    private double lastAutoTime;
    private int lastAutoLine;
    private double lastAutoDistance;

    public void autonomousInit() {
        lineSensor.enable();
        timer.reset();
        lastAutoTime = 0.0;
        autoState = kStateInit;
        autoFwd = 0.0;
        autoSide = 0.0;
        lastAutoLine = 0;
        elevatorAutoDrive = 0;
        elevatorAutoStart = 0;
        elevatorAutoCurrent = 0;
        elevatorAutoTiming = 0;
        elevatorClockStart = true;
        x = 0;
    }



    public void autonomousPeriodic()
    {
        boolean left, mid, right;
        double distance;

        left = lineSensor.isLeft();
        mid = lineSensor.isMid();
        right = lineSensor.isRight();

        distance = getDistance();
        switch (autoState) {
            case kStateInit:
            {
                autoFwd = 0.0;
                autoSide = 0.0;
                autoTurn = 0.0;
                autoState = kStateSearchingForLine;
                lastAutoTime = timer.get();
                break;
            }
            case kStateSearchingForLine:
            {
                logMessageSet("Looking...  ");
                autoFwd = 0.3;
                autoTurn = 0.1;
                autoElevator = 0.4;
                if(elevatorCurrent() > 35.0) {
                    autoElevator = 0.0;
                }
                if(left || mid || right) {
                    // found line
                    autoState = kStateFoundLineStart;
                }
                break;
            }
            case kStateLostLine:
            {
                logMessageSet("Lost line :(  ");
                autoFwd = 0.3;
                if(elevatorCurrent() > 35.0) {
                    autoElevator = 0.0;
                }
                // give up
                if(distance > 300) {
                    autoState = kStateDone;
                }
                if(left || mid || right) {
                    // found line
                    autoState = kStateFoundLineStart;
                } else {
                    autoTurn = -0.1;
                    if(lastAutoLine == 1) {
                        autoTurn = 0.1;
                    }
                }
                break;
            }
            case kStateFoundLineStart:
            {
                logMessageSet("Found line :)  ");
                autoFwd = 0.4;
                autoElevator = 1.0;
                if(elevatorCurrent() > 35.0) {
                    autoElevator = 0.0;
                }
                // about halfway between should be distance measure
                if(distance >  105) {
                    autoState = kStateSearchingForEnd;
                    break;
                }
                if(left || mid || right) {
                    // still got line
                    autoTurn = 0.0;
                    lastAutoLine = 0;
                    if(left) {
                        lastAutoLine = 1;
                    } if(right) {
                        lastAutoLine = -1;
                    }
                    if(!mid) {
                        autoTurn = left? 0.1: -0.1;
                    }
                } else {
                    // lost line
                    autoState = kStateLostLine;
//                    if(lastAutoLine == 1) {
//                        autoState = kStateLostLineToLeft;
//                    } else {
//                        autoState = kStateLostLineToRight;
//                    }
                }
                break;
            }
            case kStateSearchingForEnd:
            {
                logMessageSet("Nearly there   ");
                autoFwd = 0.2;
                autoElevator = 1.0;
                if(elevatorCurrent() > 35.0) {
                    autoElevator = 0.0;
                }
                if((left && mid && right) ||
                    distance > 215){
                    // got end - stop
                    autoFwd = 0.0;
                    autoSide = 0.0;
                    autoTurn = 0.0;
                    lastAutoTime = timer.get();
                    if(autoElevator > 0.0) {
                        autoState = kStateRaiseArm;
                    } else {
                        autoState = kStateScoreStart;
                    }
                    break;
                }
                if(left || mid || right) {
                    autoTurn = 0.0;
                    if(left) {
                        lastAutoLine = 1;
                    } if(right) {
                        lastAutoLine = -1;
                    }
                    if (!mid) {
                        autoTurn = left? 0.1: -0.1;
                    }
                } else {
                    autoState = kStateLostLine;
                }
                break;
            }
            case kStateRaiseArm:
            {
                autoFwd = 0.0;
                autoSide = 0.0;
                autoTurn = 0.0;
                autoElevator = 0.8;
                if(elevatorCurrent() > 35.0) {
                    autoElevator = 0.0;
                    arm.release(true);
                    autoState = kStateScoreNudgeFwd;
                    lastAutoTime = timer.get();
                    lastAutoDistance = distance;
                }
                break;
            }
            case kStateScoreNudgeFwd:
            {
                autoFwd = 0.4;
                if((timer.get()-lastAutoTime) > 0.4 ||
                        (distance - lastAutoDistance) > 6) {
                    autoFwd = 0.0;
                    autoState = kStateScoreStart;
                }
                break;
            }
            case kStateScoreStart:
            {
                logMessageSet("Auto Score!    ");
                autoState = kStateScoreReleaseTube;
                lastAutoTime = timer.get();
                lastAutoDistance = distance;
                arm.release(true);
                break;
            }
            case kStateScoreReleaseTube:
            {
                if(timer.get()-lastAutoTime> 0.5) {
                    arm.punch(true);
                    autoState = kStateScorePunchTube;
                }
                break;
            }
            case kStateScorePunchTube:
            {
                if(timer.get()-lastAutoTime> 1.0) {
                    arm.punch(false);
                    autoState = kStateBackupStart;
                }
                break;
            }
            case kStateBackupStart:
            {
                logMessageSet("Backup...    ");
                autoFwd = -0.3;
                autoState = kStateBackup;
                lastAutoTime = timer.get();
                break;
            }
            case kStateBackup:
            {
                if((timer.get()-lastAutoTime) > 0.5 ||
                    (distance - lastAutoDistance) > 12) {
                    arm.release(false);
                    autoState = kStateDone;
                    elevator.freefall(true);
                }
                break;
            }
            case kStateDone:
            {
                autoFwd = 0.0;
                autoSide = 0.0;
                autoTurn = 0.0;

                logMessageSet("Auto Done     ");
                autoElevator = 0.0;
                break;
            }

        }
        drive(-autoFwd, autoSide, autoTurn);

        System.out.println("Timer: " + timer.get()
                + "; " + autoState + "; " + left + " " + mid + " " + right
                + "; Elevator: " + autoElevator
                + "; Drive: " + autoFwd + ", " + autoSide + ", " + autoTurn
                + " dist: " + distance);

       //raising the ubertube and scoring it



//        if (elevatorClockStart)
//           {
//        elevatorAutoStart = timer.get();
//        elevatorClockStart = false;
//            }
//
//
//        elevatorAutoCurrent = timer.get();
//        elevatorAutoTiming = elevatorAutoCurrent - elevatorAutoStart;
//
//        if (elevatorAutoTiming < 3.9)
//            elevatorAutoDrive = 1.0;
//        else
//            if ((elevatorAutoTiming >6.8)&& (elevatorAutoTiming) < 9 )
//            elevatorAutoDrive = -1.0;
//            else
//                elevatorAutoDrive = 0;
//
//        if ((elevatorAutoTiming > 4.0) && (elevatorAutoTiming < 4.25))
//            xDriveAuto = -0.4;
//        else if ((elevatorAutoTiming > 6.4) && (elevatorAutoTiming < 6.8))
//            xDriveAuto = 0.4;
//        else
//            xDriveAuto = 0;
//
//        if ((elevatorAutoTiming > 4.9) && (elevatorAutoTiming < 6.4))
//            arm.set(true);
//
//        if ((elevatorAutoTiming > 5.4) && (elevatorAutoTiming <6.4))
//            pusher.set(true);

//    try {
//        elevatorDrive.setX(autoElevator);
//
//    } catch (CANTimeoutException e) {
//        autoElevator = 0.0;
//        logMessageSet("CAN timeout (elevator)");
//    }
        elevator.set(autoElevator);

        // send error messages to driver station
        logMessageCommit();

        if(iterator%2==0) {
            util.updateDashboard();
        }

        iterator++;
    }

    public void disabledInit() {
        lineSensor.disable();
    }

    public void teleopInit() {
        lineSensor.disable();
        timer.reset();
        elevator.freefall(false);
        logoLightPower.set(true);
   }

   public void teleopPeriodic() {
      int posn = 0;

//      gyroReading = gyro.getAngle();
//    System.out.println("Gyro says " + gyroReading);

    //axis 2 is 'up and down' on the left XBox joystick
    //axis 1 is 'left and right' on the right XBox joystick
    //axis 4 is 'left and right' on the right XBox joystick
    yDriveValue = joystickMap(driveControl.getRawAxis(2));
    xDriveValue = -joystickMap(driveControl.getRawAxis(1));
    turnDriveValue = turnSensitivity * -joystickMap(driveControl.getRawAxis(4));

    //Operator Control is now a separate controller.
    elevatorDriveSet = operatorControl.getRawAxis(3);


    logoLight();

    drive(yDriveValue, xDriveValue, turnDriveValue);

    System.out.println("distance is " + getDistance());

//    try {
//    System.out.println("RR=" + 100*rearRight.getPosition() +
//            "; RL=" + 100*rearLeft.getPosition() +
//            "; FL=" + 100*frontLeft.getPosition() +
//            "; FR=" + 100*frontRight.getPosition());
//    } catch (CANTimeoutException e) {
//        String errorMsg = "CAN posn timeout";
//        logMessageSet(errorMsg);
//    }

  if(elevatorDriveSet < -0.5) {
      elevatorDriveSet = -0.5;
  }

//    try {
//        elevatorDrive.setX(elevatorDriveSet);
//        posn = (int)(100*elevatorDrive.getPosition());
//    } catch (CANTimeoutException e) {
//        logMessageSet("CAN timeout (elevator)");
//    }
        elevator.set(elevatorDriveSet);

    // read XBox bumper buttons for arm pneumatics
    arm.drop(operatorControl.getRawButton(5));
    arm.release(operatorControl.getRawButton(6));
    arm.punch(operatorControl.getRawButton(2));


    // interlock to prevent minibot launching before 15s from end

//    if(timer.get()>105000) {
    if(timer.get()>0) {
        minibotLauncher.set(operatorControl.getRawButton(9));
    }

        if(iterator%2 == 0) {
            util.updateDashboard();
        }

        // send error messages to driver station
        logMessageCommit();

        watchdog.feed();

        // keep count of passes through function
        iterator++;
    }

   // ignore deadzone (-0.1 to +0.1) and map to [-1.0, 1.0]
   private double joystickMap(double raw) {
       final double zone = 0.1;
       double out = 0.0;

       if(raw > zone) {
           out = (raw - zone)/(1.0 - zone);
       } else if(raw < -zone) {
           out = (raw + zone)/(1.0 - zone);
       }

       return out;
   }

    private double elevatorCurrent() {
        double current = 0.0;
        current = elevator.getCurrent();
        return current;
    }

    private double getDistance() {
        double dist = 0.0;
        double posnA = 0.0;
        double posnB = 0.0;

        try {
            posnA = frontRight.getPosition()/2.27;
            posnB = -frontLeft.getPosition()/2.27;
        } catch (CANTimeoutException e) {
            logMessageSet("CAN timeout (wheels)");
        }

        // average of both wheel distances in inches, just for the "English" fans
        dist = 18.85 * (posnA + posnB)/ 2;

        return dist;
    }

    private void drive(double forward, double sideways, double turn) {

        double maxWheelSpeed;
        double wheelFLSet, wheelFRSet;
        double wheelRLSet, wheelRRSet;

        //System.out.print("X: " + xDriveValue + " Y: " + yDriveValue + " R: " + turnDriveValue);

        wheelFLSet=(forward + sideways + turn);
        wheelFRSet=(forward - sideways - turn);
        wheelRLSet=(forward - sideways + turn);
        wheelRRSet=(forward + sideways - turn);

        maxWheelSpeed = Math.max(Math.max(Math.abs(wheelFLSet),
                Math.abs(wheelFRSet)), Math.max(Math.abs(wheelRLSet),
                Math.abs(wheelRRSet)));

        if(maxWheelSpeed > 1.0) {
            double speedScaling;
            speedScaling = 1.0 / maxWheelSpeed;
            wheelFLSet *= speedScaling;
            wheelFRSet *= speedScaling;
            wheelRLSet *= speedScaling;
            wheelRRSet *= speedScaling;
        }

        //System.out.println(" FL:" + wheelFLSet + " FR:" + wheelFRSet +
        //        " RL:" + wheelRLSet + " RR:" + wheelRRSet);
        try {
            frontLeft.setX(wheelFLSet);
            rearLeft.setX(wheelRLSet);
            // remember that the right motors are backward (-ve is clockwise)
            frontRight.setX(-wheelFRSet);
            rearRight.setX(-wheelRRSet);
        } catch (CANTimeoutException e) {
            logMessageSet("CAN timeout (wheels)");
        }

    }

    private double lastButton = 0.0;

    private void logoLight() {

        double button = operatorControl.getRawAxis(6);

        if(button != lastButton) {
            if(button == -1.0) {
                logoLight.prevColor();
            } else if(button == 1.0) {
                logoLight.nextColor();
            }
            logMessageSet("Color is " + logoLight.getColorName());
            logMessageCommit();
        }

        lastButton = button;

    }

    private boolean logMessageWaiting = false;

    private void logMessageSet(String msg) {
        int posn = (DSmessage.kLineLength - msg.length())/2;
        posn = (posn>0)? posn: 1;

        DSmessage.println(Line.kUser4, posn, msg);
        logMessageWaiting = true;
    }

    private void logMessageCommit() {
        if(logMessageWaiting) {
            DSmessage.updateLCD();
            logMessageWaiting = false;
        }
    }

}