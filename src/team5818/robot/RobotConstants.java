package team5818.robot;

import edu.wpi.first.wpilibj.Joystick.ButtonType;

/**
 * Controllers for the robot.
 */
public final class RobotConstants {

    /**
     * The talon controlling the left-front wheels.
     */
    public static final int TALON_LEFT_FRONT = 5;
    /**
     * The talon controlling the left-front wheels.
     */
    public static final int TALON_LEFT_MIDDLE = 4;
    /**
     * The talon controlling the left-back wheels.
     */
    public static final int TALON_LEFT_BACK = 6;
    /**
     * The talon controlling the right-front wheels.
     */
    public static final int TALON_RIGHT_FRONT = 2;
    /**
     * The talon controlling the right-front wheels.
     */
    public static final int TALON_RIGHT_MIDDLE = 1;
    /**
     * The talon controlling the right-back wheels.
     */
    public static final int TALON_RIGHT_BACK = 3;

    /**
     * The talon controlling the arm.
     */
    public static final int TALON_ARM_MOTOR = 9;
    /**
     * talon controlling collector
     */
    public static final int TALON_COLLECTOR_MOTOR = 10;

    /**
     * The talon controller for the upper flywheel motor.
     */
    public static final int TALON_FLYWHEEL_UPPER = 6;

    /**
     * The talon controller for the lower flywheel motor.
     */
    public static final int TALON_FLYWHEEL_LOWER = 9;

    /**
     * The channel for the arm potentiometer.
     */
    public static final int ARM_POTENTIOMETER_CHANNEL = 0;
    /**
     * The scale for the arm potentiometer.
     */
    public static final double ARM_POTENTIOMETER_SCALE = .0657;
    /**
     * intercept for arm potentiometer
     */
    public static final double ARM_POTENTIOMETER_INTERCEPT = -70.15;
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

    public static final double PID_LOOP_P_TERM = 0.001;

    public static final double PID_LOOP_I_TERM = 0.0007;

    public static final double PID_LOOP_D_TERM = 0;

    /**
     * inches/tick
     */
    public static final double ROBOT_ENCODER_SCALE = 0.02843601895734597;

    private RobotConstants() {
    }

}
