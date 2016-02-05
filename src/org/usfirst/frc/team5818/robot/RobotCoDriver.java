package org.usfirst.frc.team5818.robot;

import org.usfirst.frc.team5818.robot.modules.Module;

import edu.wpi.first.wpilibj.Joystick;

/**
 * The secondary robot driver. Responsible for the arm.
 */
public class RobotCoDriver implements Module {

    private static final Joystick FIRST_JOYSTICK =
            new Joystick(RobotConstants.CODRIVER_FIRST_JOYSTICK_PORT);
    private static final Joystick SECOND_JOYSTICK =
            new Joystick(RobotConstants.CODRIVER_SECOND_JOYSTICK_PORT);

    private Arm arm;

    @Override
    public void initModule() {
        arm = new Arm();
    }

    @Override
    public void teleopPeriodicModule() {
        // Arm teleop
        arm.armTeleopPeriodic();

        // if
        // (RobotConstants.JOYSTICK_C.getRawButton(RobotConstants.ARM_RESET_BUTTON))
        // {
        // RobotConstants.ARM_ENCODER.reset();
        // }
        // if (RobotConstants.JOYSTICK_C
        // .getRawButton(RobotConstants.PRINT_ANGLE_BUTTON)) {
        // DriverStation.reportError("" + arm.getAngle() + "\n", false);
        // }
        // if (RobotConstants.JOYSTICK_C
        // .getRawButton(RobotConstants.LEFT_UP_ANGLE_BUTTON)) {
        // arm.aimAdjustLeft(true);
        // }
        // if (RobotConstants.JOYSTICK_C
        // .getRawButton(RobotConstants.LEFT_DOWN_ANGLE_BUTTON)) {
        // arm.aimAdjustLeft(false);
        // }
        // if (RobotConstants.JOYSTICK_C
        // .getRawButton(RobotConstants.RIGHT_UP_ANGLE_BUTTON)) {
        // arm.aimAdjustRight(true);
        // }
        // if (RobotConstants.JOYSTICK_C
        // .getRawButton(RobotConstants.RIGHT_DOWN_ANGLE_BUTTON)) {
        // arm.aimAdjustRight(false);
        // }
    }

    @Override
    public void endModule() {
        arm = null;
    }

}
