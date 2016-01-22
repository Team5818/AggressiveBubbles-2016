package org.usfirst.frc.team5818.robot;

import edu.wpi.first.wpilibj.Talon;

public abstract class DriveSystem {

	private final Talon leftTalon;
	private final Talon rightTalon;

	public DriveSystem(int talonAChannel, int talonBChannel) {
		this.leftTalon = new Talon(talonAChannel);
		this.rightTalon = new Talon(talonBChannel);
	}

	public Talon getLeftTalon() {
		return this.leftTalon;
	}

	public Talon getRightTalon() {
		return this.rightTalon;
	}

	public abstract double getPowerLeft();

	public abstract double getPowerRight();

	public void transferPowerToTalons() {
		double left = getPowerLeft();
		double right = getPowerRight();
		if (1 < left || left < -1) {
			throw new IllegalStateException("Power left too much " + left);
		}
		if (1 < right || right < -1) {
			throw new IllegalStateException("Power right too much " + right);
		}
		this.leftTalon.set(getPowerLeft());
		this.rightTalon.set(getPowerRight());
	}

}
