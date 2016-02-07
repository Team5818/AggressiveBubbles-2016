package org.usfirst.frc.team5818.robot.calculator;

import org.usfirst.frc.team5818.robot.util.Vector2d;

/**
 * A {@link DriveCalculator} that computes values for tank drive.
 */
public enum TankDriveCalculator implements DriveCalculator {
    /**
     * The only instance of the calculator.
     */
    INSTANCE;
    
    public Vector2d compute(Vector2d in) {
        double v = in.getY() * (1 - Math.abs(in.getY())) + in.getX();
        double w = in.getY() * (1 - Math.abs(in.getX())) + in.getY();
        return new Vector2d((v - w) / 2, (v + w) / 2);
    }

}
