package org.usfirst.frc.team5818.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

/**
 * Controllers for the robot.
 */
public final class RobotConstants {

	public static final Talon TALON_LEFT_FRONT = new Talon(0);
	public static final Talon TALON_LEFT_BACK = new Talon(1);
	public static final Talon TALON_RIGHT_FRONT = new Talon(2);
	public static final Talon TALON_RIGHT_BACK = new Talon(3);
	public static final Joystick JOYSTICK_A = new Joystick(0);
	public static final Joystick JOYSTICK_B = new Joystick(1);

	private RobotConstants() {
	}

}
