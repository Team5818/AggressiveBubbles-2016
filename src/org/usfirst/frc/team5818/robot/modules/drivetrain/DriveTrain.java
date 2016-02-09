package org.usfirst.frc.team5818.robot.modules.drivetrain;

import org.usfirst.frc.team5818.robot.RobotConstants;
import org.usfirst.frc.team5818.robot.modules.Module;
import org.usfirst.frc.team5818.robot.util.Vector2d;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDOutput;

/**
 * The entire drive thing.
 */
public class DriveTrain implements Module {

    private static final CANTalon LEFT_FRONT =
            new CANTalon(RobotConstants.TALON_LEFT_FRONT);
    private static final CANTalon LEFT_BACK =
            new CANTalon(RobotConstants.TALON_LEFT_BACK);
    private static final CANTalon RIGHT_FRONT =
            new CANTalon(RobotConstants.TALON_RIGHT_FRONT);
    private static final CANTalon RIGHT_BACK =
            new CANTalon(RobotConstants.TALON_RIGHT_BACK);

    private final DriveSide left = new DriveSide(LEFT_FRONT, LEFT_BACK, false);
    // Right motors are reversed.
    private final DriveSide right =
            new DriveSide(RIGHT_FRONT, RIGHT_BACK, true);

    /**
     * @return the {@link PIDOutput} for the left side
     */
    public PIDOutput getLeftMotors() {
        return left;
    }

    /**
     * @return the {@link PIDOutput} for the right side
     */
    public PIDOutput getRightMotors() {
        return right;
    }

    /**
     * Sets the power based on the {@link Vector2d} {@code x} and {@code y}.
     * 
     * @param power
     *            - The vector containing power values. Left is
     *            {@link Vector2d#getX()} and right is {@link Vector2d#getY()}
     */
    public void setPower(Vector2d power) {
        left.pidWrite(power.getX());
        right.pidWrite(power.getY());
    }

    @Override
    public void initModule() {
    }

    @Override
    public void teleopPeriodicModule() {
    }

    @Override
    public void endModule() {
    }

}
