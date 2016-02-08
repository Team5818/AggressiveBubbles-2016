package org.usfirst.frc.team5818.robot;

import org.usfirst.frc.team5818.robot.modules.Module;
import org.usfirst.frc.team5818.robot.modules.drivetrain.ArcadeDriveCalculator;
import org.usfirst.frc.team5818.robot.modules.drivetrain.DriveCalculator;
import org.usfirst.frc.team5818.robot.util.Vector2d;
import org.usfirst.frc.team5818.robot.util.Vectors;

import edu.wpi.first.wpilibj.Joystick;

/**
 * The primary robot driver. Responsible for driving the robot.
 */
public class RobotDriver implements Module {

    private static final Joystick FIRST_JOYSTICK =
            new Joystick(RobotConstants.DRIVER_FIRST_JOYSTICK_PORT);

    private static final Joystick SECOND_JOYSTICK =
            new Joystick(RobotConstants.DRIVER_SECOND_JOYSTICK_PORT);

    private static final DriveCalculator driveCalculator =
            ArcadeDriveCalculator.INSTANCE;

    @Override
    public void initModule() {
    }

    @Override
    public void teleopPeriodicModule() {
        Vector2d talonPowers =
                driveCalculator.compute(Vectors.fromJoystick(FIRST_JOYSTICK));
        Robot.runningRobot.driveTrain.setPower(talonPowers);

    }

    @Override
    public void endModule() {
    }

}
