
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.KinectStick;
import edu.wpi.first.wpilibj.templates.commands.LifterUp;
import edu.wpi.first.wpilibj.templates.commands.LifterStop;
import edu.wpi.first.wpilibj.templates.commands.LifterDown;
import edu.wpi.first.wpilibj.templates.commands.PitcherUp;
import edu.wpi.first.wpilibj.templates.commands.PitcherDown;
import edu.wpi.first.wpilibj.templates.commands.PitcherSpeedDelta;
import edu.wpi.first.wpilibj.templates.commands.PitcherFireandReload;
import edu.wpi.first.wpilibj.templates.commands.PitcherFire;
import edu.wpi.first.wpilibj.templates.commands.PitcherSpeed;
import edu.wpi.first.wpilibj.templates.commands.FootDown;
import edu.wpi.first.wpilibj.templates.commands.FootUp;
import edu.wpi.first.wpilibj.templates.commands.RampPusherUp;
import edu.wpi.first.wpilibj.templates.commands.RampPusherDown;
import edu.wpi.first.wpilibj.templates.commands.TurretAuto;

public class OI {
    // Process operator interface input here.
    Joystick driverController = new Joystick(1);
    Joystick operatorController = new Joystick(2);
    KinectStick leftKinectStick = new KinectStick(2);
    KinectStick rightKinectStick = new KinectStick(1);

    // driver can deploy foot
    private Button driverButtonA = new JoystickButton(driverController, 1);
//    private Button driverButtonB = new JoystickButton(driverController, 2);
//    private Button driverButtonX = new JoystickButton(driverController, 3);
//    private Button driverButtonY = new JoystickButton(driverController, 4);
    private Button driverButtonLeftBumper = new JoystickButton(driverController, 5);
    private Button driverButtonRightBumper = new JoystickButton(driverController, 6);
    private LowAxisButton driverButtonRightTrigger = new LowAxisButton(driverController);
    private HighAxisButton driverButtonLeftTrigger = new HighAxisButton(driverController);

    // operator stuff
    private Button operatorButtonA = new JoystickButton(operatorController, 1);
    private Button operatorButtonB = new JoystickButton(operatorController, 2);
    private Button operatorButtonX = new JoystickButton(operatorController, 3);
    private Button operatorButtonY = new JoystickButton(operatorController, 4);
    private Button operatorButtonLeftThumb = new JoystickButton(operatorController, 9);
    private Button operatorButtonLeftBumper = new JoystickButton(operatorController, 5);
    private Button operatorButtonRightBumper = new JoystickButton(operatorController, 6);
    private LowAxisButton operatorButtonRightTrigger = new LowAxisButton(operatorController);
    private HighAxisButton operatorButtonLeftTrigger = new HighAxisButton(operatorController);
    private LowAxisButton operatorButtonLeftThumbUp = new LowAxisButton(operatorController, 2);
    private HighAxisButton operatorButtonLeftThumbDown = new HighAxisButton(operatorController, 2);

    private static final double pitcherFineSpeedDelta = 25;
    private static final double pitcherCoarseSpeedDelta = 100;

    public OI() {
        // fire ball control
        operatorButtonA.whenPressed(new PitcherFireandReload());
        // shooter angle control
        operatorButtonB.whenPressed(new PitcherDown());
        operatorButtonX.whenPressed(new PitcherUp());
        // turret auto steer mode
//        operatorButtonLeftThumb.whileHeld(new TurretAuto());
        // shooter speed control
        operatorButtonLeftBumper.whenPressed(new PitcherSpeedDelta(-pitcherFineSpeedDelta));
        operatorButtonRightBumper.whenPressed(new PitcherSpeedDelta(pitcherFineSpeedDelta));
        operatorButtonLeftTrigger.whenPressed(new PitcherSpeedDelta(-pitcherCoarseSpeedDelta));
        operatorButtonRightTrigger.whenPressed(new PitcherSpeedDelta(pitcherCoarseSpeedDelta));
        operatorButtonY.whenPressed(new PitcherSpeed(1250));
        operatorButtonLeftThumbUp.whenPressed(new LifterUp());
        operatorButtonLeftThumbUp.whenReleased(new LifterStop());
        operatorButtonLeftThumbDown.whenPressed(new LifterDown());
        operatorButtonLeftThumbDown.whenReleased(new LifterStop());


        // foot control
        driverButtonA.whenPressed(new FootDown());
        driverButtonA.whenReleased(new FootUp());
        // lifter control
        driverButtonLeftTrigger.whenPressed(new LifterDown());
        driverButtonLeftTrigger.whenReleased(new LifterStop());
        driverButtonRightTrigger.whenPressed(new LifterUp());
        driverButtonRightTrigger.whenReleased(new LifterStop());
        // ramp push control
        driverButtonLeftBumper.whenPressed(new RampPusherDown());
        driverButtonRightBumper.whileHeld(new RampPusherUp());
    }

    public double getTurretTurnStick() {
        return map(operatorController.getX());
    }

    public double getFwdLeftStick() {
        return map(-driverController.getY());
    }

    public double getSideStick() {
        return map(driverController.getX());
    }

    public double getTurnStick() {
        return map(driverController.getRawAxis(4));
    }

    public double getFwdRightStick() {
        return map(-driverController.getRawAxis(5));
    }

    public double getKinectLeftStick() {
        return -leftKinectStick.getY();
    }

    public double getKinectRightStick() {
        return -rightKinectStick.getY();
    }

    private static final double deadZone = 0.2;
    private static final double scale = 1.0/(1.0 - deadZone);

    private double map(double in) {
        double out = 0.0;

        if(in > deadZone) {
            out = (in - deadZone) * scale;
        } else if(in < -deadZone) {
            out = (in + deadZone) * scale;
        }
        return out;
    }
}