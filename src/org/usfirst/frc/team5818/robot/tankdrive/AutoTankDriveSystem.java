package org.usfirst.frc.team5818.robot.tankdrive;

public class AutoTankDriveSystem extends TankDriveSystem {

	private double forwardPower;
	private double turnPower;

	public AutoTankDriveSystem(int talonAChannel, int talonBChannel) {
		super(talonAChannel, talonBChannel);
	}

	public void setForwardPower(double forwardPower) {
		this.forwardPower = forwardPower;
	}

	@Override
	public double getForwardPower() {
		return forwardPower;
	}

	public void setTurnPower(double turnPower) {
		this.turnPower = turnPower;
	}

	@Override
	public double getTurnPower() {
		return turnPower;
	}

}
