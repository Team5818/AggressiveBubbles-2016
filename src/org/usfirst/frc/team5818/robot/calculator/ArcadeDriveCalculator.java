package org.usfirst.frc.team5818.robot.calculator;

import org.usfirst.frc.team5818.robot.util.Vector2d;

/**
 * A {@link DriveCalculator} that computes values for arcade drive.
 */
public enum ArcadeDriveCalculator implements DriveCalculator {
    /**
     * The only instance of this calculator.
     */
    INSTANCE;

    public Vector2d compute(Vector2d in) {
        double rotateValue = -in.getX();
        double moveValue = in.getY();

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

        double leftMotorSpeed;
        double rightMotorSpeed;
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
        return new Vector2d(leftMotorSpeed, rightMotorSpeed);
    }

}
