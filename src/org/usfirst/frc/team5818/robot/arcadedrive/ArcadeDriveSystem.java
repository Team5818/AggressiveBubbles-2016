package org.usfirst.frc.team5818.robot.arcadedrive;

import org.usfirst.frc.team5818.robot.DriveSystem;

public abstract class ArcadeDriveSystem extends DriveSystem {

	private double leftMotorSpeed;
	private double rightMotorSpeed;

	public ArcadeDriveSystem(int talonAChannel, int talonBChannel) {
		super(talonAChannel, talonBChannel);
	}

	public abstract double getRotatePower();

	public abstract double getMovePower();

	public void calculateMotorSpeeds() {
		double rotateValue = getRotatePower();
		double moveValue = getMovePower();

		// if (reversedTurn) {
		// rotateValue = -rotateValue;
		// }

		// if (squaredInputs) {
		// if (moveValue >= 0.0) {
		// moveValue = (moveValue * moveValue);
		// } else {
		// moveValue = -(moveValue * moveValue);
		// }
		// if (rotateValue >= 0.0) {
		// rotateValue = (rotateValue * rotateValue);
		// } else {
		// rotateValue = -(rotateValue * rotateValue);
		// }
		// }

		if (moveValue > 0.0) {
			if (rotateValue > 0.0) {
				leftMotorSpeed = moveValue - rotateValue;
				rightMotorSpeed = Math.max(moveValue, rotateValue);
			} else {
				leftMotorSpeed = Math.max(moveValue, -rotateValue);
				rightMotorSpeed = moveValue + rotateValue;
			}
		} else {
			if (rotateValue > 0.0) {
				leftMotorSpeed = -Math.max(-moveValue, rotateValue);
				rightMotorSpeed = moveValue + rotateValue;
			} else {
				leftMotorSpeed = moveValue - rotateValue;
				rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
			}
		}
	}

	@Override
	public double getPowerLeft() {
		calculateMotorSpeeds();
		return leftMotorSpeed;
	}

	@Override
	public double getPowerRight() {
		calculateMotorSpeeds();
		return rightMotorSpeed;
	}

}
