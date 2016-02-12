package org.usfirst.frc.team5818.robot;

import edu.wpi.first.wpilibj.Joystick.ButtonType;

/**
 * Controllers for the robot.
 */
public final class RobotConstants {

    private static final int CAN_OFFSET = 1;

    /**
     * The talon controlling the left-front wheels.
     */
    public static final int TALON_LEFT_FRONT = 0 + CAN_OFFSET;
    /**
     * The talon controlling the left-back wheels.
     */
    public static final int TALON_LEFT_BACK = 1 + CAN_OFFSET;
    /**
     * The talon controlling the right-front wheels.
     */
    public static final int TALON_RIGHT_FRONT = 2 + CAN_OFFSET;
    /**
     * The talon controlling the left-back wheels.
     */
    public static final int TALON_RIGHT_BACK = 3 + CAN_OFFSET;

    /**
     * The talon controlling the arm.
     */
    public static final int TALON_ARM_MOTOR = 5 + CAN_OFFSET;

    /**
     * The talon controller for the upper flywheel motor.
     */
    public static final int TALON_FLYWHEEL_UPPER = 6 + CAN_OFFSET;

    /**
     * The talon controller for the lower flywheel motor.
     */
    public static final int TALON_FLYWHEEL_LOWER = 4 + CAN_OFFSET;

    /**
     * The aChannel for the arm encoder.
     */
    public static final int ARM_ENCODER_CHANNEL_A = 0;
    /**
     * The bChannel for the arm encoder.
     */
    public static final int ARM_ENCODER_CHANNEL_B = 1;

    /**
     * The scale for the arm encoder.
     */
    public static final double ARM_ENCODER_SCALE = 1 / 375.0;

    /**
     * The button known as "trigger" on the joystick.
     */
    public static final int TRIGGER_BUTTON = ButtonType.kTrigger.value + 1;

    /**
     * Port for the main driver's 1st joystick.
     */
    public static final int DRIVER_FIRST_JOYSTICK_PORT = 0;
    /**
     * Port for the main driver's 2nd joystick.
     */
    public static final int DRIVER_SECOND_JOYSTICK_PORT = 1;
    /**
     * Port for the co-driver's 1st joystick.
     */
    public static final int CODRIVER_FIRST_JOYSTICK_PORT = 2;
    /**
     * Port for the co-driver's 2nd joystick.
     */
    public static final int CODRIVER_SECOND_JOYSTICK_PORT = 3;

    public static final double PID_LOOP_P_TERM = 0;

    public static final double PID_LOOP_I_TERM = 0;

    public static final double PID_LOOP_D_TERM = 0;

    // inches/rotation
    public static final double ROBOT_ENCODER_SCALE = 0.02843601895734597;

    private RobotConstants() {
    }

}
