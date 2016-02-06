# Introduction #

Each subsystem that uses relative position control should have a _homing_ routine. This operates as soon as the robot is able to move (i.e. first enabled state) and moves the actuators to a defined location.

# (Mast) Climber homing routine #

The home position of the mast is at the maximum upward position, i.e. when the lower proximity sensor is against the target.

...more info...

State machine in the next diagram

![http://frc-88.googlecode.com/svn/images/MastHomeFSM.png](http://frc-88.googlecode.com/svn/images/MastHomeFSM.png)