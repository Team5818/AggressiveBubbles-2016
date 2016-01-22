package org.usfirst.frc.team5818.robot.tankdrive;

import edu.wpi.first.wpilibj.Joystick;

public class JoystickTankDriveSystem extends TankDriveSystem {

	private final Joystick joystickA;
	private final Joystick joystickB;

	public JoystickTankDriveSystem(int joystickAPort, int joystickBPort, int talonAChannel, int talonBChannel) {
		super(talonAChannel, talonBChannel);
		this.joystickA = new Joystick(joystickAPort);
		this.joystickB = new Joystick(joystickBPort);
	}

	public double getForwardPower() {
		return -joystickA.getY();
	}

	public double getTurnPower() {
		return joystickB.getX();
	}

}
