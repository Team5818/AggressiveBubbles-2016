package team5818.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.commands.ArmPower;
import team5818.robot.commands.Collect;
import team5818.robot.commands.DrivePower;
import team5818.robot.commands.LEDToggle;
import team5818.robot.commands.LowerArmToGround;
import team5818.robot.commands.SetArmAngle;
import team5818.robot.commands.SetDrivePower;
import team5818.robot.commands.SetFlywheelPower;
import team5818.robot.commands.SetFlywheelVelocity;
import team5818.robot.commands.SpinRobot;
import team5818.robot.commands.SwitchFeed;
import team5818.robot.modules.ComputerVision;
import team5818.robot.modules.FlyWheel;
import team5818.robot.modules.Module;
import team5818.robot.modules.drivetrain.ArcadeDriveCalculator;
import team5818.robot.modules.drivetrain.ArcadeVelocityCalculator;
import team5818.robot.modules.drivetrain.DriveSide;
import team5818.robot.modules.drivetrain.TankDriveCalculator;
import team5818.robot.util.Vector2d;
import team5818.robot.util.Vectors;

/**
 * The primary robot driver. Responsible for driving the robot.
 */
public class RobotDriver implements Module {

    private enum DriveType {
        TANK, ARCADE, ARCADE_VELOCITY;
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
    private static final int BUT_DRIVE_VELOCITY = 11;
    private static final int BUT_DRIVE_POWER = 10;
    private static final int BUT_INVERT = 9;
    private static final int BUT_UNINVERT = 8;
    private static final int BUT_OVERRIDE_DRIVER = 5;
    private static final int BUT_ARM_ANGLE_SHOOTING = 4;
    private static final int BUT_ARM_ANGLE_COLLECT = 3;
    private static final int BUT_ARM_ANGLE_GROUND = 2;
    private static final int BUT_OVERRIDE_CODRIVER = 1;

    // Second Joystick Buttons
    private static final int BUT_SWITCH_FEED_SHOOT = 12;
    private static final int BUT_SWITCH_FEED_DRIVE = 11;
    private static final int BUT_ROTATE_CW_90 = 6;
    private static final int BUT_ROTATE_CCW_90 = 5;
    private static final int BUT_ROTATE_CW_180 = 4;
    private static final int BUT_ROTATE_CCW_180 = 3;
    private static final int BUT_UNCOLLECT = 2;
    private static final int BUT_COLLECT = 1;
    private static final int BUT_ENTER_CLIMB = 11;
    private static final int BUT_EXIT_CLIMB = 12;

    private DriveType driveType = DriveType.ARCADE;
    private InputMode inputMode = InputMode.TWO_STICKS;

    // Weather to invert the throttle
    private static boolean invertThrottle = true;
    private boolean hasStoppedRobot = false;
    private boolean climbMode = false;

    private double armAngleShooting = 40;
    private double armAngleCollect = -1;

    // Initializing the JoystickButtons
    private JoystickButton butCollect =
            new JoystickButton(SECOND_JOYSTICK, BUT_COLLECT);
    private JoystickButton butUncollect =
            new JoystickButton(SECOND_JOYSTICK, BUT_UNCOLLECT);
    private JoystickButton butOverrideDriver =
            new JoystickButton(FIRST_JOYSTICK, BUT_OVERRIDE_DRIVER);
    private JoystickButton butOverrideCoDriver =
            new JoystickButton(FIRST_JOYSTICK, BUT_OVERRIDE_CODRIVER);
    private JoystickButton butArmAngleShooting =
            new JoystickButton(FIRST_JOYSTICK, BUT_ARM_ANGLE_SHOOTING);
    private JoystickButton butArmAngleCollect =
            new JoystickButton(FIRST_JOYSTICK, BUT_ARM_ANGLE_COLLECT);
    private JoystickButton butArmAngleGround =
            new JoystickButton(FIRST_JOYSTICK, BUT_ARM_ANGLE_GROUND);
    private JoystickButton rotateCW90 =
            new JoystickButton(SECOND_JOYSTICK, BUT_ROTATE_CW_90);
    private JoystickButton rotateCCW90 =
            new JoystickButton(SECOND_JOYSTICK, BUT_ROTATE_CCW_90);
    private JoystickButton rotateCW180 =
            new JoystickButton(SECOND_JOYSTICK, BUT_ROTATE_CW_180);
    private JoystickButton rotateCCW180 =
            new JoystickButton(SECOND_JOYSTICK, BUT_ROTATE_CCW_180);
    private JoystickButton butSwitchFeedDrive =
            new JoystickButton(SECOND_JOYSTICK, BUT_SWITCH_FEED_DRIVE);
    private JoystickButton butSwitchFeedShoot =
            new JoystickButton(SECOND_JOYSTICK, BUT_SWITCH_FEED_SHOOT);
    private boolean hasStartedDriving = false;

    @Override
    public void initModule() {
        // Setting Preference values.
        armAngleShooting = Preferences.getInstance()
                .getDouble("ArmAngleShooting", armAngleShooting);
        armAngleCollect = Preferences.getInstance().getDouble("ArmAngleCollect",
                armAngleCollect);

        // Setting local objects of singletons for easy access.
        DriveSide leftDriveSide =
                RobotCommon.runningRobot.driveTrain.getLeftMotors();
        DriveSide rightDriveSide =
                RobotCommon.runningRobot.driveTrain.getRightMotors();

        // Adding the DriveSide PID Controllers to the smart Smart Dashboard.
        LiveWindow.addActuator("DriveSide", "Left",
                leftDriveSide.getPIDController());
        LiveWindow.addActuator("DriveSide", "Right",
                rightDriveSide.getPIDController());

        // Command Groups
        CommandGroup armToGround = new CommandGroup();
        armToGround.addSequential(new SetArmAngle(armAngleCollect));
        armToGround.addSequential(new ArmPower(LowerArmToGround.ARM_POWER));
        CommandGroup switchFeedShoot = new CommandGroup();
        switchFeedShoot
                .addParallel(new SwitchFeed(ComputerVision.CAMERA_SHOOTER));
        switchFeedShoot.addParallel(new LEDToggle(true));
        CommandGroup switchFeedDrive = new CommandGroup();
        switchFeedDrive
                .addParallel(new SwitchFeed(ComputerVision.CAMERA_DRIVER));
        switchFeedDrive.addParallel(new LEDToggle(false));
        CommandGroup overrideDriver = new CommandGroup();
        //overrideDriver.addParallel(new SetArmAngle(armAngleShooting));
        overrideDriver
                .addParallel(new SwitchFeed(ComputerVision.CAMERA_SHOOTER));
        overrideDriver.addParallel(new SetFlywheelVelocity(
                FlyWheel.SHOOT_VELOCITY_LOWER, FlyWheel.SHOOT_VELOCITY_LOWER));
        CommandGroup overrideCoDriver = new CommandGroup();
        overrideCoDriver
                .addParallel(new SwitchFeed(ComputerVision.CAMERA_DRIVER));
        overrideCoDriver.addParallel(new SetFlywheelPower(0));

        // Assigning commands to their respective buttons.
        // butInvert.whenPressed(new SwitchFeed(ComputerVision.CAMERA_BACK));
        // butUnInvert.whenPressed(new
        // SwitchFeed(ComputerVision.CAMERA_DRIVER));
        butCollect.whenPressed(new Collect(Collect.COLLECT_POWER));
        butCollect.whenReleased(new Collect(0));
        butUncollect.whenPressed(new Collect(-Collect.COLLECT_POWER));
        butUncollect.whenReleased(new Collect(0));
        butOverrideDriver.whenPressed(overrideDriver);
        butOverrideCoDriver.whenPressed(overrideCoDriver);
        butArmAngleShooting.whenPressed(new SetArmAngle(armAngleShooting));
        butArmAngleCollect.whenPressed(new SetArmAngle(armAngleCollect));
        butArmAngleGround.whenPressed(armToGround);
        rotateCW90
                .whenPressed(new SpinRobot(81, SpinRobot.DEFAULT_TIMEOUT, 0.6));
        rotateCCW90.whenPressed(
                new SpinRobot(-81, SpinRobot.DEFAULT_TIMEOUT, 0.6));
        rotateCW180.whenPressed(
                new SpinRobot(175, SpinRobot.DEFAULT_TIMEOUT, 0.6));
        rotateCCW180.whenPressed(
                new SpinRobot(-175, SpinRobot.DEFAULT_TIMEOUT, 0.6));
        butSwitchFeedShoot.whenPressed(switchFeedShoot);
        butSwitchFeedDrive.whenPressed(switchFeedDrive);

        // Setting driving mode to power.
        stopMovement();
    }

    @Override
    public void teleopPeriodicModule() {
        // Drives the robot if it should be done so by Driver.
        performButtonActions();
        if (!RobotCoDriver.isOverrideDriver() && !climbMode) {
            if (usingJoystick()) {
                drive();
                hasStoppedRobot = false;
            } else {
                hasStartedDriving = false;
                if (!hasStoppedRobot) {
                    stopMovement();
                    hasStoppedRobot = true;
                }
            }
        }
    }

    /**
     * Stops the robot and maintains the current driving type.
     */
    public void stopMovement() {
        new DrivePower(0, 0).start();
    }

    /**
     * Checks the buttons that need to be checked and perfroms the intended
     * operation.
     */
    public void performButtonActions() {

        // Inverting buttons
        if (FIRST_JOYSTICK.getRawButton(BUT_INVERT)) {
            invertThrottle = true;
        }
        if (FIRST_JOYSTICK.getRawButton(BUT_UNINVERT)) {
            invertThrottle = false;
        }

        if (FIRST_JOYSTICK.getRawButton(BUT_OVERRIDE_CODRIVER)) {
            RobotCoDriver.setOverrideDriver(false);
        }

        if (FIRST_JOYSTICK.getRawButton(BUT_OVERRIDE_DRIVER)) {
            RobotCoDriver.setOverrideDriver(true);
        }

        if (FIRST_JOYSTICK.getRawButton(BUT_DRIVE_VELOCITY)) {
            driveType = DriveType.ARCADE_VELOCITY;
            stopMovement();
            if (FIRST_JOYSTICK.getRawButton(BUT_ENTER_CLIMB)) {
                climbMode = true;
            }
            if (FIRST_JOYSTICK.getRawButton(BUT_EXIT_CLIMB)) {
                climbMode = false;
            }
        } else if (FIRST_JOYSTICK.getRawButton(BUT_DRIVE_POWER)) {
            driveType = DriveType.ARCADE;
            stopMovement();
        }
    }

    /**
     * Performs the driving calculation.
     */
    public void drive() {
        if (!hasStartedDriving) {
            new DrivePower(0, 0);
            hasStartedDriving = true;
        }
        Vector2d thePowersThatBe;

        // Computing Driving Code
        ArcadeDriveCalculator arcadeCalc = ArcadeDriveCalculator.INSTANCE;
        TankDriveCalculator tankCalc = TankDriveCalculator.INSTANCE;
        ArcadeVelocityCalculator velCalc = ArcadeVelocityCalculator.INSTANCE;
        switch (inputMode) {
            case ONE_STICK:
                thePowersThatBe = arcadeCalc.compute(
                        Vectors.fromJoystick(FIRST_JOYSTICK, invertThrottle));
                break;
            case TWO_STICKS:
                if (driveType == DriveType.TANK) {
                    thePowersThatBe = tankCalc.compute(Vectors.fromJoystickTank(
                            FIRST_JOYSTICK, SECOND_JOYSTICK, invertThrottle));
                } else if (driveType == DriveType.ARCADE) {
                    thePowersThatBe = arcadeCalc.compute(Vectors.fromJoystick(
                            FIRST_JOYSTICK, SECOND_JOYSTICK, invertThrottle));
                } else {
                    thePowersThatBe =
                            velCalc.compute(Vectors.fromJoystick(FIRST_JOYSTICK,
                                    SECOND_JOYSTICK, invertThrottle));

                }
                break;
            default:
                throw new IllegalStateException(
                        "Don't know what mode " + inputMode + " does");
        }
        if (driveType == DriveType.ARCADE_VELOCITY) {
            RobotCommon.runningRobot.driveTrain.setVelocity(thePowersThatBe);
        } else {
            RobotCommon.runningRobot.driveTrain.setPower(thePowersThatBe);
        }

    }

    @Override
    public void endModule() {
    }

    @Override
    public void initTest() {
    }

    @Override
    public void initTeleop() {
        new SwitchFeed(ComputerVision.CAMERA_DRIVER).start();
    }

    @Override
    public void initAutonomous() {

    }

    @Override
    public void testPeriodic() {
        LiveWindow.run();
    }

    private boolean usingFirstJoy(){
        return (Math.abs(FIRST_JOYSTICK.getX()) < RobotConstants.JOYSTICK_DEADBAND
                && Math.abs(FIRST_JOYSTICK.getY()) < RobotConstants.JOYSTICK_DEADBAND);
    }

    private boolean usingSecondJoy() {
        return (Math.abs(SECOND_JOYSTICK.getX()) < RobotConstants.JOYSTICK_DEADBAND
                && Math.abs(SECOND_JOYSTICK.getY()) < RobotConstants.JOYSTICK_DEADBAND);
    }

    private boolean usingJoystick() {
        if (Math.abs(FIRST_JOYSTICK.getX()) < RobotConstants.JOYSTICK_DEADBAND
                && Math.abs(FIRST_JOYSTICK
                        .getY()) < RobotConstants.JOYSTICK_DEADBAND
                && Math.abs(SECOND_JOYSTICK
                        .getX()) < RobotConstants.JOYSTICK_DEADBAND
                && Math.abs(SECOND_JOYSTICK
                        .getY()) < RobotConstants.JOYSTICK_DEADBAND) {
            return false;
        }
        return true;
    }

}