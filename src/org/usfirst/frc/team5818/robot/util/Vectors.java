package org.usfirst.frc.team5818.robot.util;

import edu.wpi.first.wpilibj.Joystick;

public final class Vectors {

	public static Vector2d fromJoystick(Joystick stick) {
		return new Vector2d(stick.getX(), stick.getY());
	}
	
	private Vectors() {
	}

}
