package team5818.robot;

import edu.wpi.first.wpilibj.Joystick.ButtonType;

/**
 * Controllers for the robot.
 */
public final class RobotConstants {

    /**
     * width of the robot
     */
    public static final double ROBOT_WIDTH_IN_INCHES = 25;
    /**
     * The talon controlling the left-front wheels.
     */
    public static final int TALON_LEFT_FRONT = 4;
    /**
     * The talon controlling the left-front wheels.
     */
    public static final int TALON_LEFT_MIDDLE = 6;
    /**
     * The talon controlling the left-back wheels.
     */
    public static final int TALON_LEFT_BACK = 5;
    /**
     * The talon controlling the right-front wheels.
     */
    public static final int TALON_RIGHT_FRONT = 1;
    /**
     * The talon controlling the right-front wheels.
     */
    public static final int TALON_RIGHT_MIDDLE = 3;
    /**
     * The talon controlling the right-back wheels.
     */
    public static final int TALON_RIGHT_BACK = 2;
    /**
     * Talon for left climbing winch
     */
    public static final int TALON_WINCH_LEFT = 8;

    /**
     * Talon for right climbing winch
     */
    public static final int TALON_WINCH_RIGHT = 13;

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
    //public static final int TALON_COLLECTOR_MOTOR = 10;
    public static final int TALON_COLLECTOR_MOTOR = -99;
    
    /**
     * Talon for right rope climber
     */
    public static final int TALON_ROPE_RIGHT = 10;

    /**
     * The talon controller for the upper flywheel motor.
     */
//    public static final int TALON_FLYWHEEL_UPPER = 11;
    public static final int TALON_FLYWHEEL_UPPER = 99;
    
  /**
   * Talon for left rope climber
   */
    public static final int TALON_ROPE_LEFT = 11;

    /**
     * The talon controller for the lower flywheel motor.
     */
    public static final int TALON_FLYWHEEL_LOWER = 9;

    /**
     * The channel for the arm potentiometer.
     */
    public static final int ARM_POTENTIOMETER_CHANNEL = 0;
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


    /**
     * The deadband on the joysticks. It ranges from 0 with no deadband, and 1
     * with the stick not working
     */
    public static final double JOYSTICK_DEADBAND = 0.15;

    /**
     * The Slop factor for Targeting
     */
    public static final double SLOP = .02;
    /**
     * The Camera feild of view for shooting
     */
    public static final double CAMFOV = 120;
    
    private RobotConstants() {
    }

}
