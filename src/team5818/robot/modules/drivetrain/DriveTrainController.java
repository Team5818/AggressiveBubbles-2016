package team5818.robot.modules.drivetrain;

import edu.wpi.first.wpilibj.command.Subsystem;
import team5818.robot.util.MathUtil;
import team5818.robot.util.Vector2d;

/**
 * Wraps around {@link DriveTrain} to help drive certain distances and control
 * different ways of driving.
 */
public class DriveTrainController extends Subsystem {

    private static final double ROBOT_WIDTH_IN_INCHES = 2.3 * 12;

    private final DriveTrain driveTrain;
    private DriveCalculator driveCalculator;

    public DriveTrainController(DriveTrain driveTrain,
            DriveCalculator driveCalculator) {
        this.driveTrain = driveTrain;
        setDriveCalculator(driveCalculator);
    }

    public void setDriveCalculator(DriveCalculator driveCalculator) {
        if (driveCalculator == null) {
            throw new NullPointerException("fix your code genius.");
        }
        this.driveCalculator = driveCalculator;
    }

    public void driveToTargetXInchesAway(double inches) {
        driveTrain.getLeftMotors().setDriveDistance(inches);
        driveTrain.getRightMotors().setDriveDistance(inches);
    }

    public void setPowerDirectly(Vector2d power) {
        driveTrain.setPower(power);
    }

    public void setVelocity(Vector2d vector2d) {
        driveTrain.getLeftMotors().setVelocity(vector2d.getX());
        driveTrain.getRightMotors().setVelocity(vector2d.getY());
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
        double distance =
                MathUtil.distanceOfArc(ROBOT_WIDTH_IN_INCHES, degrees);
        driveTrain.getLeftMotors().setDriveDistance(distance);
        driveTrain.getRightMotors().setDriveDistance(-distance);
    }

    @Override
    protected void initDefaultCommand() {
    }

}
