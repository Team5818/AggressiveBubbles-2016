package team5818.robot.modules.drivetrain;

import team5818.robot.util.MathUtil;
import team5818.robot.util.Vector2d;

/**
 * A {@link DriveCalculator} that computes values for arcade drive when using a
 * steering wheel.
 */
public enum WheelDriveCalculator implements DriveCalculator {
    /**
     * The only instance of this calculator.
     */
    INSTANCE;

    @Override
    public Vector2d compute(Vector2d in) {
        double rotateValue = in.getX() * 0.5;
        double moveValue = -in.getY();
        double leftMotorSpeed;
        double rightMotorSpeed;

        double addMultiplier = 1 - Math.abs(moveValue);
        double multiplyMultiplier = Math.abs(moveValue);

        double additive = addMultiplier * rotateValue;
        double multiplicative =
                multiplyMultiplier * (1 - Math.abs(rotateValue));

        double baseValue = moveValue * multiplicative;

        baseValue = MathUtil.cap(baseValue, -1 + Math.abs(additive),
                1 - Math.abs(additive));

        leftMotorSpeed = baseValue + additive;
        rightMotorSpeed = baseValue - additive;

        return new Vector2d(leftMotorSpeed, rightMotorSpeed);
    }

}
