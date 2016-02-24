package team5818.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.commands.Collect;
import team5818.robot.modules.Module;
import team5818.robot.modules.drivetrain.ArcadeDriveCalculator;
import team5818.robot.modules.drivetrain.DriveSide;
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

    private static final Joystick FIRST_JOYSTICK =
            new Joystick(RobotConstants.DRIVER_FIRST_JOYSTICK_PORT);
    private static final Joystick SECOND_JOYSTICK =
            new Joystick(RobotConstants.DRIVER_SECOND_JOYSTICK_PORT);

    private DriveType driveType = DriveType.ARCADE;
    private InputMode inputMode = InputMode.TWO_STICKS;

    private static boolean invertThrottle = false;

    private static final int BUT_DEBUG = 12;
    private static final int BUT_ONESTICK_ARCADE = 11;
    private static final int BUT_INVERT = 8;
    private static final int BUT_UNINVERT = 9;
    private static final int BUT_TWOSTICK_TANK = 7;
    private static final int BUT_TWOSTICK_ARCADE = 6;
    private static final int BUT_UNCOLLECT = 2;
    private static final int BUT_COLLECT = 1;

    private JoystickButton butCollect = new JoystickButton(SECOND_JOYSTICK, BUT_COLLECT);
    private JoystickButton butUncollect = new JoystickButton(SECOND_JOYSTICK, BUT_UNCOLLECT);

    @Override
    public void initModule() {
        DriveSide leftDriveSide =
                RobotCommon.runningRobot.driveTrain.getLeftMotors();
        DriveSide rightDriveSide =
                RobotCommon.runningRobot.driveTrain.getRightMotors();
        LiveWindow.addActuator("DriveSide", "Left",
                leftDriveSide.getPIDController());
        LiveWindow.addActuator("DriveSide", "Right",
                rightDriveSide.getPIDController());

        butCollect.whenPressed(new Collect(Collect.COLLECT_POWER));
        butCollect.whenReleased(new Collect(0));
        butUncollect.whenPressed(new Collect(-Collect.COLLECT_POWER));
        butUncollect.whenReleased(new Collect(0));
    }

    @Override
    public void teleopPeriodicModule() {
        Vector2d thePowersThatBe;
        
        if(RobotCoDriver.isOverrideDriver() || DriveSide.getMode() == DriveSide.MODE_POWER)
            return;
        
        // Puts the Raw Encoders in the SmartDashboard
        if (SECOND_JOYSTICK.getRawButton(BUT_DEBUG)) {
            SmartDashboard.putNumber("Drive Train Left Pos",
                    RobotCommon.runningRobot.driveTrain.getLeftMotors()
                            .getEncPosRaw());
            SmartDashboard.putNumber("Drive Train Right Pos",
                    RobotCommon.runningRobot.driveTrain.getRightMotors()
                            .getEncPosRaw());
        }
        
        // Inverts the controls if needed.
        if (FIRST_JOYSTICK.getRawButton(BUT_INVERT)) {
            invertThrottle = true;
        }
        if (FIRST_JOYSTICK.getRawButton(BUT_UNINVERT)) {
            invertThrottle = false;
        }
        
        // Changes the Control Mode
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

        //Computing Driving Code
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

        // Setting the Vectors to driving
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

    }

    @Override
    public void initAutonomous() {

    }

    @Override
    public void testPeriodic() {
        LiveWindow.run();
    }

}
