
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.commands.BaseTalonHook;
import edu.wpi.first.wpilibj.templates.commands.BaseTalonRelease;
import edu.wpi.first.wpilibj.templates.commands.ClimberHome;
import edu.wpi.first.wpilibj.templates.commands.TilterStop;
import edu.wpi.first.wpilibj.templates.commands.DumperBackward;
import edu.wpi.first.wpilibj.templates.commands.DumperForward;
import edu.wpi.first.wpilibj.templates.commands.HomeGroup;
import edu.wpi.first.wpilibj.templates.commands.ClimberPosition;
import edu.wpi.first.wpilibj.templates.commands.ClimbLevelOne;
import edu.wpi.first.wpilibj.templates.commands.ClimbLevelTwo;
import edu.wpi.first.wpilibj.templates.commands.DumperStop;


/**
 * This class binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    private Joystick driverController = new Joystick(1);
    private Joystick operatorController = new Joystick(2);
    
    private static final int LEFT_HORIZ_AXIS = 1;
    private static final int LEFT_VERT_AXIS = 2;
    private static final int TRIGGER_AXIS = 3;
    private static final int RIGHT_HORIZ_AXIS = 4;
    private static final int RIGHT_VERT_AXIS = 5;
    
    //first controller button
    private Button driverButtonA = new JoystickButton(driverController, 1);
    private Button driverButtonB = new JoystickButton(driverController, 2);
    private Button driverButtonX = new JoystickButton(driverController, 3);
    private Button driverButtonY = new JoystickButton(driverController, 4);
    private Button driverButtonLeftBumper = new JoystickButton(driverController, 5);
    private Button driverButtonRightBumper = new JoystickButton(driverController, 6);
    
    //2nd controller buttons
    private Button operatorButtonA = new JoystickButton(operatorController, 1);
    private Button operatorButtonB = new JoystickButton(operatorController, 2);
    private Button operatorButtonX = new JoystickButton(operatorController, 3);
    private Button operatorButtonY = new JoystickButton(operatorController, 4);
    private Button operatorButtonLeftBumper = new JoystickButton(operatorController, 5);
    private Button operatorButtonRightBumper = new JoystickButton(operatorController, 6);
    
    public OI() {
        //these buttons are for the bumpers and they are the open loop commands for the dumper
        operatorButtonLeftBumper.whenPressed(new DumperBackward());
        operatorButtonLeftBumper.whenReleased(new DumperStop());
        operatorButtonRightBumper.whenPressed(new DumperForward());
        operatorButtonRightBumper.whenReleased(new DumperStop());
        
        // Buttons to control the base talons.  Currently assigned to the
        // driver's bumpers.
        driverButtonLeftBumper.whenPressed(new BaseTalonRelease());
        driverButtonRightBumper.whenPressed(new BaseTalonHook());

        operatorButtonY.whenPressed(new HomeGroup());
        operatorButtonB.whenPressed(new ClimbLevelOne());
//          operatorButtonX.whenPressed(new ClimbLevelTwo());
    }
    
    /**
     * operator controller's left vertical axis
     */
    public double getOperatorLeftVerticalAxis() {
        return deadZoneMap(-operatorController.getRawAxis(2));
    }
    /**
     * operator controller's left horizontal axis
     */
    public double getOperatorRightVerticalAxis() {
        return deadZoneMap(operatorController.getRawAxis(5));
    }

    /**
     * Position of the driver controller triggers.
     * Note that we can't read the values individually, and only get the sum
     * of the left [0, 1] and right [0, -1] positions. In effect we get a difference
     * between positions, so both pressed reads the same as neither pressed!
     * 
     * @return single value sum of trigger positions
     */
    public double getDriveTrigger() {
        return driverController.getRawAxis(3);
    }

    public double getOperatorTrigger() {
        return operatorController.getRawAxis(3);
    }

    public boolean getDriveLeftBumper() {
        return driverController.getRawButton(5);
    }

    public boolean getDriveRightBumper() {
        return driverController.getRawButton(6);
    }

    /**
     * Position of the operator controller right stick Y axis (normalized)
     * 
     * @return value of stick position
     */
    public double getOperatorRightHorizontalAxis() {
        return deadZoneMap(-operatorController.getRawAxis(4));
    }
    
    public double getDriveRightVerticalAxis() {
        return deadZoneMap(-driverController.getRawAxis(5));
    }
    
    public double getDriveRightHorizontalAxis() {
        return deadZoneMap(-driverController.getRawAxis(4));
    }
    
    public double getDriveLeftVerticalAxis() {
        return deadZoneMap(-driverController.getRawAxis(2));
    }     
    public double getDriveLeftHorizontalAxis() {
        return deadZoneMap(driverController.getRawAxis(1));
    }

    private static final double deadZone = 0.1;
    private static final double scale = 1.0/(1.0 - deadZone);
    
    /**
     *  Dead Zone for the joysticks
     */
    private double deadZoneMap(double in) {
        double out = 0.0;

        if(in > deadZone) {
            out = (in - deadZone) * scale;
        } else if(in < -deadZone) {
            out = (in + deadZone) * scale;
        }
        return out;
    }

    //// CREATING BUTTONS
    // One type of button is a joystick button which is any button on a joystick.
    // You create one by telling it which joystick it's on and which button
    // number it is.
    // Joystick stick = new Joystick(port);
    // Button button = new JoystickButton(stick, buttonNumber);
    
    // Another type of button you can create is a DigitalIOButton, which is
    // a button or switch hooked up to the cypress module. These are useful if
    // you want to build a customized operator interface.
    // Button button = new DigitalIOButton(1);
    
    // There are a few additional built in buttons you can use. Additionally,
    // by subclassing Button you can create custom triggers and bind those to
    // commands the same as any other Button.
    
    //// TRIGGERING COMMANDS WITH BUTTONS
    // Once you have a button, it's trivial to bind it to a button in one of
    // three ways:
    
    // Start the command when the button is pressed and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenPressed(new ExampleCommand());
    
    // Run the command while the button is being held down and interrupt it once
    // the button is released.
    // button.whileHeld(new ExampleCommand());
    
    // Start the command when the button is released  and let it run the command
    // until it is finished as determined by it's isFinished method.
    // button.whenReleased(new ExampleCommand());
}

