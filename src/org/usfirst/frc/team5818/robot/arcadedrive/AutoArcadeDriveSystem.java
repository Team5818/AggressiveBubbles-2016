package org.usfirst.frc.team5818.robot.arcadedrive;

public class AutoArcadeDriveSystem extends ArcadeDriveSystem {

	private double rotatePower;
	private double movePower;

	public AutoArcadeDriveSystem(int talonAChannel, int talonBChannel) {
		super(talonAChannel, talonBChannel);
	}

	public void setRotatePower(double rotatePower) {
		this.rotatePower = rotatePower;
	}

	@Override
	public double getRotatePower() {
		return this.rotatePower;
	}

	public void setMovePower(double movePower) {
		this.movePower = movePower;
	}

	@Override
	public double getMovePower() {
		return this.movePower;
	}

}
