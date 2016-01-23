package org.usfirst.frc.team5818.robot.powerpc;

import org.usfirst.frc.team5818.robot.util.Vector2d;

/**
 * A Calculator that computes values for tank drive.
 */
public final class TankDriveCalculator implements Calculator {

	public Vector2d compute(Vector2d in) {
		double v = in.getY() * (1 - Math.abs(in.getY())) + in.getX();
		double w = in.getY() * (1 - Math.abs(in.getX())) + in.getY();
		return new Vector2d((v - w) / 2, (v + w) / 2);
	}

}
