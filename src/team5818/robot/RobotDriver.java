package team5818.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import team5818.robot.commands.DriveForwardSlowlyCommand;
import team5818.robot.commands.ResetEncoderCommand;
import team5818.robot.modules.Module;
import team5818.robot.modules.drivetrain.ArcadeDriveCalculator;
import team5818.robot.modules.drivetrain.DriveSide;
import team5818.robot.modules.drivetrain.DriveTrain;
import team5818.robot.modules.drivetrain.TankDriveCalculator;
import team5818.robot.util.Vector2d;
import team5818.robot.util.Vectors;

/**
 * The primary robot driver. Responsible for driving the robot.
 */
public class RobotDriver implements Module {

    private enum DriveType {
        TANK, ARCADE;
    }

    private enum InputMode {
        ONE_STICK, TWO_STICKS;
    }

    public static final int BUT_DEBUG = 3;
    public static final int debugCycleTicks = 30;
    public static int currTicks = 0;

    public static final int BUT_TWOSTICK_ARCADE = 6;
    public static final int BUT_TWOSTICK_TANK = 7;
    public static final int BUT_ONESTICK_ARCADE = 11;
    public static final int BUT_INVERT = 9;
    public static final int BUT_NO_INVERT = 8;

    private static final Joystick FIRST_JOYSTICK =
            new Joystick(RobotConstants.DRIVER_FIRST_JOYSTICK_PORT);

    private static final Joystick SECOND_JOYSTICK =
            new Joystick(RobotConstants.DRIVER_SECOND_JOYSTICK_PORT);

    private DriveType driveType = DriveType.TANK;
    private InputMode inputMode = InputMode.ONE_STICK;

    private boolean invertThrottle = false;

    @Override
    public void initModule() {
        JoystickButton resetEncoder = new JoystickButton(FIRST_JOYSTICK, 10);
        resetEncoder.whenPressed(new ResetEncoderCommand());
        JoystickButton driveForwardSlowly =
                new JoystickButton(FIRST_JOYSTICK, 3);
        driveForwardSlowly.whenActive(new DriveForwardSlowlyCommand());
        DriveSide leftDriveSide =
                RobotCommon.runningRobot.driveTrain.getLeftMotors();
        DriveSide rightDriveSide =
                RobotCommon.runningRobot.driveTrain.getRightMotors();
        LiveWindow.addActuator("DriveSide", "Left",
                leftDriveSide.getPIDController());
        LiveWindow.addActuator("DriveSide", "Right",
                rightDriveSide.getPIDController());
    }

    @Override
    public void teleopPeriodicModule() {
        if (1 == 1 + 1 - 1) {
            return;
        }

        if (FIRST_JOYSTICK.getRawButton(BUT_DEBUG)) {
            currTicks = (currTicks + 1) % debugCycleTicks;
            if (currTicks == 0) {
                DriveTrain dt = RobotCommon.runningRobot.driveTrain;

                System.out.println("LP: " + dt.getLeftMotors().dumpPower(1)
                        + " RP: " + dt.getRightMotors().dumpPower(1));
            }
        }

        if (FIRST_JOYSTICK.getRawButton(BUT_TWOSTICK_ARCADE)) {
            RobotCommon.runningRobot.driveTrainController
                    .setDriveCalculator(ArcadeDriveCalculator.INSTANCE);
            driveType = DriveType.ARCADE;
            inputMode = InputMode.TWO_STICKS;
        } else if (FIRST_JOYSTICK.getRawButton(BUT_TWOSTICK_TANK)) {
            RobotCommon.runningRobot.driveTrainController
                    .setDriveCalculator(TankDriveCalculator.INSTANCE);
            driveType = DriveType.TANK;
            inputMode = InputMode.TWO_STICKS;
        } else if (FIRST_JOYSTICK.getRawButton(BUT_ONESTICK_ARCADE)) {
            RobotCommon.runningRobot.driveTrainController
                    .setDriveCalculator(ArcadeDriveCalculator.INSTANCE);
            driveType = DriveType.ARCADE;
            inputMode = InputMode.ONE_STICK;
        }

        if (FIRST_JOYSTICK.getRawButton(BUT_INVERT))
            invertThrottle = true;
        else if (FIRST_JOYSTICK.getRawButton(BUT_NO_INVERT))
            invertThrottle = false;

        Vector2d thePowersThatBe;

        switch (inputMode) {
            case ONE_STICK:
                thePowersThatBe =
                        Vectors.fromJoystick(FIRST_JOYSTICK, invertThrottle);
                break;
            case TWO_STICKS:
                if (driveType == DriveType.TANK) {
                    thePowersThatBe = Vectors.fromJoystickTank(FIRST_JOYSTICK,
                            SECOND_JOYSTICK, invertThrottle);
                } else {
                    thePowersThatBe = Vectors.fromJoystick(FIRST_JOYSTICK,
                            SECOND_JOYSTICK, invertThrottle);
                }
                break;
            default:
                throw new IllegalStateException(
                        "Don't know what mode " + inputMode + " does");
        }

        RobotCommon.runningRobot.driveTrainController
                .recalculateAndSetPower(thePowersThatBe);
    }

    @Override
    public void endModule() {
    }

    @Override
    public void initTest() {
    }

    @Override
    public void initTeleop() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initAutonomous() {
        // TODO Auto-generated method stub

    }

    @Override
    public void testPeriodic() {
        LiveWindow.run();
    }

}
