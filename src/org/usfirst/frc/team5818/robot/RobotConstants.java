package org.usfirst.frc.team5818.robot;

import edu.wpi.first.wpilibj.Joystick.ButtonType;

/**
 * Controllers for the robot.
 */
public final class RobotConstants {

    private static final int CAN_OFFSET = 1;

    /**
     * The right train motor that is closer to the front.
     */
    public static final int TALON_RIGHT_FRONT = 1;
    /**
     * The right train motor that is closer to the top.
     */
    public static final int TALON_RIGHT_TOP = 2;
    /**
     * The right train motor that is closer to the back.
     */
    public static final int TALON_RIGHT_BACK = 3;
    /**
     * The left train motor that is closer to the front.
     */
    public static final int TALON_LEFT_FRONT = 4;
    /**
     * The left train motor that is closer to the top.
     */
    public static final int TALON_LEFT_TOP = 5;
    /**
     * The left train motor that is closer to the back.
     */
    public static final int TALON_LEFT_BACK = 6;
    /**
     * The talon controlling the arm.
     */
    public static final int TALON_ARM_MOTOR = 7;

    /**
     * The talon controller for the upper flywheel motor.
     */
    public static final int TALON_FLYWHEEL_UPPER = 8;

    /**
     * The talon controller for the lower flywheel motor.
     */
    public static final int TALON_FLYWHEEL_LOWER = 9;

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

    private RobotConstants() {
    }

}
