package org.usfirst.frc.team5818.robot.arcadedrive;

import edu.wpi.first.wpilibj.Joystick;

public class JoystickArcadeDriveSystem extends ArcadeDriveSystem {

	private final Joystick joystick;

	public JoystickArcadeDriveSystem(int joystickAPort, int talonAChannel, int talonBChannel) {
		super(talonAChannel, talonBChannel);
		this.joystick = new Joystick(joystickAPort);
	}

	@Override
	public double getRotatePower() {
		return joystick.getX();
	}

	@Override
	public double getMovePower() {
		return -joystick.getY();
	}

}
