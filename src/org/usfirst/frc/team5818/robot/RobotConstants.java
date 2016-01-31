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

    public static final CANTalon TALON_LEFT_FRONT =
            new CANTalon(0 + CAN_OFFSET);
    public static final CANTalon TALON_LEFT_BACK = new CANTalon(1 + CAN_OFFSET);

    public static final CANTalon TALON_RIGHT_FRONT =
            new CANTalon(2 + CAN_OFFSET);
    public static final CANTalon TALON_RIGHT_BACK =
            new CANTalon(3 + CAN_OFFSET);

    public static final CANTalon TALON_LEFT_ARM_MOTOR =
            new CANTalon(4 + CAN_OFFSET);
    public static final CANTalon TALON_RIGHT_ARM_MOTOR =
            new CANTalon(5 + CAN_OFFSET);

    public static final Joystick JOYSTICK_A = new Joystick(0);
    public static final Joystick JOYSTICK_B = new Joystick(1);
    public static final Joystick JOYSTICK_C = new Joystick(2);
    public static final Joystick JOYSTICK_D = new Joystick(3);

    public static final Encoder ARM_ENCODER = new Encoder(0, 1);
    public static final double ARM_ENCODER_SCALE = 1 / 375.0;
    public static final int TRIGGER_BUTTON = ButtonType.kTrigger.value + 1;

    public static final int ARM_RESET = 8;
    public static final int PRINT_ANGLE = 7;

    public static final int RIGHT_UP_ANGLE_BUTTON = 5;
    public static final int RIGHT_DOWN_ANGLE_BUTTON = 3;

    public static final int LEFT_UP_ANGLE_BUTTON = 6;
    public static final int LEFT_DOWN_ANGLE_BUTTON = 4;

    private RobotConstants() {
    }

}
