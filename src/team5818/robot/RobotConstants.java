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
    public static final int TALON_FIRST_ARM_MOTOR = 12;
    /**
     * The talon controlling the arm.
     */
    public static final int TALON_SECOND_ARM_MOTOR = 7;
    /**
     * talon controlling collector
     */
    public static final int TALON_COLLECTOR_MOTOR = 10;

    /**
     * The talon controller for the upper flywheel motor.
     */
    public static final int TALON_FLYWHEEL_UPPER = 11;

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
    public static final double ARM_POTENTIOMETER_SCALE = 1;//.0657 on kitbot;
    /**
     * intercept for arm potentiometer
     */
    public static final double ARM_POTENTIOMETER_INTERCEPT = 0;// -70.15 on kitbot;
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

    public static final double DISTANCE_PID_LOOP_P_TERM = 0.12;

    public static final double DISTANCE_PID_LOOP_I_TERM = 0.00000001;

    public static final double DISTANCE_PID_LOOP_D_TERM = 0.05;

    public static final double VELOCITY_PID_LOOP_P_TERM = 0.12;

    public static final double VELOCITY_PID_LOOP_I_TERM = 0.00000001;

    public static final double VELOCITY_PID_LOOP_D_TERM = 0.05;

    public static final double MAX_VELOCITY = 175;
    public static final double ONE_OVER_MAX_VEL = 1 / MAX_VELOCITY;

    // TODO redo the math for ROBOT_ENCODER_SCALE
    /**
     * The encoder scale to convert to inches/tick.
     */
    public static final double ROBOT_ENCODER_SCALE = 0.02843601895734597;

    private RobotConstants() {
    }

}
