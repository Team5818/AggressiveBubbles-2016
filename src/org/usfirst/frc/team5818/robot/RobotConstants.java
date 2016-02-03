package org.usfirst.frc.team5818.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Joystick.ButtonType;

/**
 * Controllers for the robot.
 */
public final class RobotConstants {

    private static final int CAN_OFFSET = 1;

    /**
     * The talon controlling the left-front wheels.
     */
    public static final CANTalon TALON_LEFT_FRONT =
            new CANTalon(0 + CAN_OFFSET);
    /**
     * The talon controlling the left-back wheels.
     */
    public static final CANTalon TALON_LEFT_BACK = new CANTalon(1 + CAN_OFFSET);

    /**
     * The talon controlling the right-front wheels.
     */
    public static final CANTalon TALON_RIGHT_FRONT =
            new CANTalon(2 + CAN_OFFSET);
    /**
     * The talon controlling the left-back wheels.
     */
    public static final CANTalon TALON_RIGHT_BACK =
            new CANTalon(3 + CAN_OFFSET);

    /**
     * The talon controlling the left arm.
     */
    public static final CANTalon TALON_LEFT_ARM_MOTOR =
            new CANTalon(4 + CAN_OFFSET);
    /**
     * The talon controlling the right arm.
     */
    public static final CANTalon TALON_RIGHT_ARM_MOTOR =
            new CANTalon(5 + CAN_OFFSET);

    /**
     * Driver 1's 1st joystick.
     */
    public static final Joystick JOYSTICK_A = new Joystick(0);
    /**
     * Driver 1's 2nd joystick.
     */
    public static final Joystick JOYSTICK_B = new Joystick(1);
    /**
     * Driver 2's 1st joystick.
     */
    public static final Joystick JOYSTICK_C = new Joystick(2);
    /**
     * Driver 2's 2nd joystick.
     */
    public static final Joystick JOYSTICK_D = new Joystick(3);

    /**
     * An encoder that helps control the arm.
     */
    public static final Encoder ARM_ENCODER = new Encoder(0, 1);
    /**
     * The scale for {@link #ARM_ENCODER}.
     */
    public static final double ARM_ENCODER_SCALE = 1 / 375.0;

    /**
     * The button known as "trigger" on the joystick.
     */
    public static final int TRIGGER_BUTTON = ButtonType.kTrigger.value + 1;
    /**
     * The button that resets the arm offset.
     */
    public static final int ARM_RESET_BUTTON = 8;
    /**
     * The button that prints the arm offset.
     */
    public static final int PRINT_ANGLE_BUTTON = 7;
    /**
     * The button that increases the angle the right arm.
     */
    public static final int RIGHT_UP_ANGLE_BUTTON = 5;
    /**
     * The button that decreases the angle of the right arm.
     */
    public static final int RIGHT_DOWN_ANGLE_BUTTON = 3;
    /**
     * The button that increases the angle the left arm.
     */
    public static final int LEFT_UP_ANGLE_BUTTON = 6;
    /**
     * The button that decreases the angle of the left arm.
     */
    public static final int LEFT_DOWN_ANGLE_BUTTON = 4;

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
