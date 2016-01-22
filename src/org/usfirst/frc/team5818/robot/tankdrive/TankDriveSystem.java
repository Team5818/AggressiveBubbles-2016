package org.usfirst.frc.team5818.robot.tankdrive;

import org.usfirst.frc.team5818.robot.DriveSystem;

public abstract class TankDriveSystem extends DriveSystem {

	public TankDriveSystem(int talonAChannel, int talonBChannel) {
		super(talonAChannel, talonBChannel);
	}

	public abstract double getForwardPower();

	public abstract double getTurnPower();

	public double getV() {
		return getTurnPower() * (1 - Math.abs(getTurnPower())) + getForwardPower();
	}

	public double getW() {
		return getTurnPower() * (1 - Math.abs(getForwardPower())) + getTurnPower();
	}

	@Override
	public double getPowerLeft() {
		return (getV() - getW()) / 2;
	}

	@Override
	public double getPowerRight() {
		return (getV() + getW()) / 2;
	}

}
