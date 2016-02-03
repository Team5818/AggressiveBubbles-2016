package org.usfirst.frc.team5818.robot;

import org.usfirst.frc.team5818.robot.util.Vector2d;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDOutput;

/**
 * The entire drive thing.
 */
public class DriveTrain {

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

    public PIDOutput getLeftMotors() {
        return left;
    }

    public PIDOutput getRightMotors() {
        return right;
    }
    
    public void setPower(Vector2d power) {
        left.pidWrite(power.getX());
        right.pidWrite(power.getY());
    }

}
