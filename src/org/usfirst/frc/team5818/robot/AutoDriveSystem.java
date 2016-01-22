package org.usfirst.frc.team5818.robot;

public class AutoDriveSystem extends DriveSystem {

	private double powerLeft;
	private double powerRight;

	public AutoDriveSystem(int talonAChannel, int talonBChannel) {
		super(talonAChannel, talonBChannel);
	}

	@Override
	public double getPowerLeft() {
		return this.powerLeft;
	}

	public void setPowerLeft(double powerLeft) {
		if (1 < powerLeft || powerLeft < -1) {
			throw new IllegalArgumentException("power must be between -1 and 1");
		}
		this.powerLeft = powerLeft;
	}

	@Override
	public double getPowerRight() {
		return this.powerRight;
	}

	public void setPowerRight(double powerRight) {
		if (1 < powerRight || powerRight < -1) {
			throw new IllegalArgumentException("power must be between -1 and 1");
		}
		this.powerRight = powerRight;
	}

}
