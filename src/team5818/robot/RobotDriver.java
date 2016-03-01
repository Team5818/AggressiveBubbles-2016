package team5818.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.commands.Collect;
import team5818.robot.commands.DrivePower;
import team5818.robot.commands.SetArmAngle;
import team5818.robot.commands.SetDrivePower;
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

    // First driver Joystick.
    private static final Joystick FIRST_JOYSTICK =
            new Joystick(RobotConstants.DRIVER_FIRST_JOYSTICK_PORT);
    // Second driver Joystick.
    private static final Joystick SECOND_JOYSTICK =
            new Joystick(RobotConstants.DRIVER_SECOND_JOYSTICK_PORT);

    // First Joystick Buttons
    private static final int BUT_ONESTICK_ARCADE = 11;
    private static final int BUT_INVERT = 8;
    private static final int BUT_UNINVERT = 9;
    private static final int BUT_TWOSTICK_TANK = 7;
    private static final int BUT_TWOSTICK_ARCADE = 6;

    // First Joystick Buttons
    private static final int BUT_ARM_ANGLE_HIGH = 5;
    private static final int BUT_ARM_ANGLE_LOW = 4;
    private static final int BUT_ARM_ANGLE_ZERO = 3;
    private static final int BUT_ARM_ANGLE_GROUND = 2;

    // Second Joystick Buttons
    private static final int BUT_DEBUG = 12;
    private static final int BUT_UNCOLLECT = 2;
    private static final int BUT_COLLECT = 1;

    private DriveType driveType = DriveType.ARCADE;
    private InputMode inputMode = InputMode.TWO_STICKS;

    // Weather to invert the throttle
    private static boolean invertThrottle = false;

    private double armAngleHigh = 85;
    private double armAngleLow = 40;
    private double armAngleCollect = 2.3;
    private double armAngleGround = -6;

    //Initializing the JoystickButtons
    private JoystickButton butCollect =
            new JoystickButton(SECOND_JOYSTICK, BUT_COLLECT);
    private JoystickButton butUncollect =
            new JoystickButton(SECOND_JOYSTICK, BUT_UNCOLLECT);
    private JoystickButton setArmAngleHigh =
            new JoystickButton(FIRST_JOYSTICK, BUT_ARM_ANGLE_HIGH);
    private JoystickButton setArmAngleLow =
            new JoystickButton(FIRST_JOYSTICK, BUT_ARM_ANGLE_LOW);
    private JoystickButton setArmAngleCollect =
            new JoystickButton(FIRST_JOYSTICK, BUT_ARM_ANGLE_ZERO);
    private JoystickButton setArmAngleGround =
            new JoystickButton(FIRST_JOYSTICK, BUT_ARM_ANGLE_GROUND);
    

    @Override
    public void initModule() {
        //Setting Preference values.
        armAngleLow =
                Preferences.getInstance().getDouble("ArmAngleLow", armAngleLow);
        armAngleHigh = Preferences.getInstance().getDouble("ArmAngleHigh",
                armAngleHigh);
        armAngleCollect = Preferences.getInstance().getDouble("ArmAngleCollect",
                armAngleCollect);
        armAngleGround = Preferences.getInstance().getDouble("ArmAngleGround",
                armAngleGround);

        //Setting local objects of singletons for easy access.
        DriveSide leftDriveSide =
                RobotCommon.runningRobot.driveTrain.getLeftMotors();
        DriveSide rightDriveSide =
                RobotCommon.runningRobot.driveTrain.getRightMotors();
        
        //Adding the DriveSide PID Controllers to the smart Smart Dashboard.
        LiveWindow.addActuator("DriveSide", "Left",
                leftDriveSide.getPIDController());
        LiveWindow.addActuator("DriveSide", "Right",
                rightDriveSide.getPIDController());

        //Assigning commands to their respective buttons.
        butCollect.whenPressed(new Collect(Collect.COLLECT_POWER));
        butCollect.whenReleased(new Collect(0));
        butUncollect.whenPressed(new Collect(-Collect.COLLECT_POWER));
        butUncollect.whenReleased(new Collect(0));
        setArmAngleHigh.whenPressed(new SetArmAngle(armAngleHigh));
        setArmAngleLow.whenPressed(new SetArmAngle(armAngleLow));
        setArmAngleCollect.whenPressed(new SetArmAngle(armAngleCollect));
        setArmAngleGround.whenPressed(new SetArmAngle(armAngleGround));
        
        //Setting driving mode to power.
        new SetDrivePower(0, 0).start();
    }

    @Override
    public void teleopPeriodicModule() {
        Vector2d thePowersThatBe;

        if (RobotCoDriver.isOverrideDriver()
                || DriveSide.getMode() == DriveSide.MODE_POWER)
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

        // Computing Driving Code
        ArcadeDriveCalculator arcadeCalc = ArcadeDriveCalculator.INSTANCE;
        TankDriveCalculator tankCalc = TankDriveCalculator.INSTANCE;
        switch (inputMode) {
            case ONE_STICK:
                thePowersThatBe =
                        arcadeCalc.compute(Vectors.fromJoystick(FIRST_JOYSTICK, invertThrottle));
                break;
            case TWO_STICKS:
                if (driveType == DriveType.TANK) {
                    thePowersThatBe = tankCalc.compute(Vectors.fromJoystickTank(FIRST_JOYSTICK,
                            SECOND_JOYSTICK, invertThrottle));
                } else {
                    thePowersThatBe = arcadeCalc.compute(Vectors.fromJoystick(FIRST_JOYSTICK,
                            SECOND_JOYSTICK, invertThrottle));
                }
                break;
            default:
                throw new IllegalStateException(
                        "Don't know what mode " + inputMode + " does");
        }
        
        
        RobotCommon.runningRobot.driveTrainController.setPowerDirectly(thePowersThatBe);
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
