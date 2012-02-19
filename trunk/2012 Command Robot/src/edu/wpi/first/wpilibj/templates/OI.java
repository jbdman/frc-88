
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.templates.commands.LifterUp;
import edu.wpi.first.wpilibj.templates.commands.LifterDown;
import edu.wpi.first.wpilibj.templates.commands.LifterPower;

public class OI {
    // Process operator interface input here.
    Joystick operatorController = new Joystick(1);
    private Button operatorButtonA = new JoystickButton(operatorController, 1);
    private Button operatorButtonB = new JoystickButton(operatorController, 2);
    private Button operatorButtonX = new JoystickButton(operatorController, 3);
    private Button operatorButtonY = new JoystickButton(operatorController, 4);


    public OI() {
        operatorButtonA.whenPressed(new LifterDown());
        operatorButtonB.whenPressed(new LifterPower(0.0));
        operatorButtonX.whenPressed(new LifterPower(0.0));
        operatorButtonY.whenPressed(new LifterPower(0.5));
    }
}
