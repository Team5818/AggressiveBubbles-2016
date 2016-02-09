package org.usfirst.frc.team5818.robot.modules.drivetrain;

import org.usfirst.frc.team5818.robot.Robot;
import org.usfirst.frc.team5818.robot.encoders.EncoderManager;
import org.usfirst.frc.team5818.robot.util.MathUtil;
import org.usfirst.frc.team5818.robot.util.Vector2d;

/**
 * Wraps around {@link DriveTrain} to help drive certain distances and control
 * different ways of driving.
 */
public class DriveTrainController {

    private static final double ROBOT_WIDTH_IN_FEET = 0;

    private final DriveTrain driveTrain = Robot.runningRobot.driveTrain;
    private DriveCalculator driveCalculator;

    private EncoderManager tmp() {
        // TODO change + inline when DriveTrain implements EncoderManager
        return (EncoderManager) driveTrain;
    }

    private EncoderManager tmp1() {
        // TODO change + inline when DriveSide implements EncoderManager
        return (EncoderManager) driveTrain.getLeftMotors();
    }

    private EncoderManager tmp2() {
        // TODO change + inline when DriveSide implements EncoderManager
        return (EncoderManager) driveTrain.getRightMotors();
    }

    public DriveTrainController(DriveCalculator driveCalculator) {
        setDriveCalculator(driveCalculator);
    }

    public void setDriveCalculator(DriveCalculator driveCalculator) {
        if (driveCalculator == null) {
            throw new NullPointerException("fix your code genius.");
        }
        this.driveCalculator = driveCalculator;
    }

    public void driveToTargetXFeetAway(double feet) {
        tmp().setDriveDistance(feet);
    }

    public void setPowerDirectly(Vector2d power) {
        driveTrain.setPower(power);
    }

    public void recalculateAndSetPower(Vector2d joystickData) {
        setPowerDirectly(driveCalculator.compute(joystickData));
    }

    public void rotateDegrees(double degrees, boolean clockwise) {
        // Force degrees to 0-360.
        degrees %= 360d;
        if (degrees < 0) {
            // Already modulo-360, so this will get us positive
            degrees += 360;
        }
        if (!clockwise) {
            // Invert on clockwise.
            degrees *= -1;
        }
        // degrees is now a normalized value between -360 and 360.
        rotateNormalizedDegrees(degrees);
    }

    private void rotateNormalizedDegrees(double degrees) {
        // To rotate X degrees, simply move left side forward by W
        // and move right side backwards by W
        double distance = MathUtil.distanceOfArc(ROBOT_WIDTH_IN_FEET, degrees);
        tmp1().setDriveDistance(distance);
        tmp2().setDriveDistance(-distance);
    }

}
