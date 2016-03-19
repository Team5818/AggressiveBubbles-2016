package team5818.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.commands.AutoAim;
import team5818.robot.commands.Collect;
import team5818.robot.commands.SetArmAngle;
import team5818.robot.commands.SetArmPower;
import team5818.robot.commands.SetDrivePower;
import team5818.robot.commands.SetFlywheelPower;
import team5818.robot.commands.SetFlywheelVelocity;
import team5818.robot.commands.SwitchFeed;
import team5818.robot.commands.LEDToggle;
import team5818.robot.modules.Arm;
import team5818.robot.modules.ComputerVision;
import team5818.robot.modules.ComputerVision;
import team5818.robot.modules.FlyWheel;
import team5818.robot.modules.Module;
import team5818.robot.modules.drivetrain.ArcadeDriveCalculator;
import team5818.robot.util.Vector2d;
import team5818.robot.util.Vectors;

/**
 * The secondary robot driver. Responsible for the arm.
 */
public class RobotCoDriver implements Module {

    /**
     * The object for the first CoDriver Joystick.
     */
    public static final Joystick firstJoystick =
            new Joystick(RobotConstants.CODRIVER_FIRST_JOYSTICK_PORT);
    /**
     * The object for the second CoDriver Joystick.
     */
    public static final Joystick secondJoystick =
            new Joystick(RobotConstants.CODRIVER_SECOND_JOYSTICK_PORT);

    // Weather CoDriver is overriding driver control.
    private static boolean overrideDriver = false;
    private boolean hasStoppedArm = false;
    private boolean hasStoppedDrive = false;

    // Joystick One Buttons
    private static final int BUT_PRINT_ANGLE = 7;
    private static final int BUT_SHOOT_ANGLE_HIGH = 5;
    private static final int BUT_SHOOT_ANGLE_LOW = 4;
    private static final int BUT_SHOOT_ANGLE_MED = 3;
    private static final int BUT_SET_ARM_POWER = 2;
    private static final int BUT_AIM = 1;

    // Joystick Two Buttons
    private static final int BUT_SWITCH_SHOOT_FEED = 12;
    private static final int BUT_SWITCH_DRIVER_FEED = 11;
    private static final int BUT_LED_ON = 8;
    private static final int BUT_LED_OFF = 7;
    private static final int BUT_SPIN_FLYWHEEL = 2;
    private static final int BUT_COLLECT = 1;

    // Different arm angles
    private double shootAngleHigh = 60;
    private double shootAngleMed = 40;
    private double shootAngleLow = 30;

    // First Joystick Buttons
    JoystickButton butSetArmPower =
            new JoystickButton(firstJoystick, BUT_SET_ARM_POWER);
    JoystickButton butShootAngleLow =
            new JoystickButton(firstJoystick, BUT_SHOOT_ANGLE_LOW);
    JoystickButton butShootAngleMed =
            new JoystickButton(firstJoystick, BUT_SHOOT_ANGLE_MED);
    JoystickButton butShootAngleHigh =
            new JoystickButton(firstJoystick, BUT_SHOOT_ANGLE_HIGH);
    JoystickButton butAutoAim = new JoystickButton(firstJoystick, BUT_AIM);

    // Second Joystick Buttons
    JoystickButton butSpinFlywheel =
            new JoystickButton(secondJoystick, BUT_SPIN_FLYWHEEL);
    JoystickButton butCollect = new JoystickButton(secondJoystick, BUT_COLLECT);
    JoystickButton butLedOn = new JoystickButton(secondJoystick, BUT_LED_ON);
    JoystickButton butLedOff = new JoystickButton(secondJoystick, BUT_LED_OFF);
    JoystickButton butSwitchShootFeed =
            new JoystickButton(secondJoystick, BUT_SWITCH_SHOOT_FEED);
    JoystickButton butSwitchDriverFeed =
            new JoystickButton(secondJoystick, BUT_SWITCH_DRIVER_FEED);

    private FlyWheel lowerFlywheel;
    private FlyWheel upperFlywheel;
    private Arm arm;

    @Override
    public void initModule() {
        // Setting the singletons to local fields for easy access.
        arm = RobotCommon.runningRobot.arm;
        lowerFlywheel = RobotCommon.runningRobot.lowerFlywheel;
        upperFlywheel = RobotCommon.runningRobot.upperFlywheel;

        // Adding PID's to SmartDashboard
        LiveWindow.addActuator("Flywheel", "Lower PID",
                lowerFlywheel.getPIDController());
        LiveWindow.addActuator("Flywheel", "Upper PID",
                upperFlywheel.getPIDController());

        // Settings the preferences
        shootAngleHigh = Preferences.getInstance().getDouble("ShootAngleHigh",
                shootAngleHigh);
        shootAngleMed = Preferences.getInstance()
                .getDouble("ShootAngleMed", shootAngleMed);
        shootAngleLow = Preferences.getInstance().getDouble("ShootAngleLow",
                shootAngleLow);

        // Making the command groups
        CommandGroup startFlywheel = new CommandGroup();
        startFlywheel.addParallel(new SetFlywheelVelocity(
                FlyWheel.SHOOT_VELOCITY_UPPER, FlyWheel.SHOOT_VELOCITY_LOWER));
        startFlywheel
                .addParallel(new SwitchFeed(ComputerVision.CAMERA_SHOOTER));
        CommandGroup stopFlywheel = new CommandGroup();
        stopFlywheel.addParallel(new SetFlywheelPower(0));
        stopFlywheel.addParallel(new SwitchFeed(ComputerVision.CAMERA_DRIVER));
        stopFlywheel.addParallel(new SetDrivePower(0, 0));

        // Assigning commands to the buttons
        butSpinFlywheel.whenPressed(startFlywheel);
        butSpinFlywheel.whenReleased(stopFlywheel);
        butShootAngleHigh.whenPressed(new SetArmAngle(shootAngleHigh));
        butShootAngleMed.whenPressed(new SetArmAngle(shootAngleMed));
        butShootAngleLow.whenPressed(new SetArmAngle(shootAngleLow));
        butSetArmPower.whenPressed(new SetArmPower(0));
        butLedOn.whenPressed(new LEDToggle(true));
        butLedOff.whenPressed(new LEDToggle(false));
        butCollect.whenPressed(new Collect(Collect.COLLECT_POWER));
        butCollect.whenReleased(new Collect(0));
        butLedOn.whenPressed(new LEDToggle(true));
        butLedOff.whenPressed(new LEDToggle(false));
        butCollect.whenPressed(new Collect(Collect.COLLECT_POWER));
        butCollect.whenReleased(new Collect(0));
        butAutoAim.whenPressed(new AutoAim());

        /*
         * TODO Check if this call is needed butSwitchShootFeed .whenPressed(new
         * SwitchFeed(ComputerVision.CAMERA_SHOOTER)); TODO:switch for actual
         * robot
         */
        butSwitchShootFeed
                .whenPressed(new SwitchFeed(ComputerVision.CAMERA_SHOOTER));
        butSwitchDriverFeed
                .whenPressed(new SwitchFeed(ComputerVision.CAMERA_DRIVER));
    }

    @Override
    public void teleopPeriodicModule() {
        // Printing the Arm Angle on the SmartDashboard.
        if (firstJoystick.getRawButton(BUT_PRINT_ANGLE)) {
            SmartDashboard.putNumber("Arm Angle = ", arm.getAngle());
            SmartDashboard.putNumber("Upper flywheel", upperFlywheel.getRPS());
            SmartDashboard.putNumber("Lower flywheel", lowerFlywheel.getRPS());
        }

        // Overrides Driver Control.
        if (secondJoystick.getRawButton(BUT_SPIN_FLYWHEEL)) {
            setOverrideDriver(true);
            stopDrive();
        } else {
            setOverrideDriver(false);
        }

        if (usingSecondStick()) {
            moveArm();
            hasStoppedArm = false;
        } else {
            if(!hasStoppedArm) {
                hasStoppedArm = true;
                stopArm();
            }
        }
        if (usingFirstStick()) {
            drive();
            hasStoppedDrive = false;
        } else {
            if(!hasStoppedDrive) {
                hasStoppedDrive = true;
                stopDrive();
            }
        }
    }

    private boolean usingFirstStick() {
        if (Math.abs(firstJoystick.getX()) < RobotConstants.JOYSTICK_DEADBAND
                && Math.abs(firstJoystick
                        .getY()) < RobotConstants.JOYSTICK_DEADBAND) {
            return false;
        }
        return true;
    }

    private boolean usingSecondStick() {
        if (Math.abs(secondJoystick.getX()) < RobotConstants.JOYSTICK_DEADBAND
                && Math.abs(secondJoystick
                        .getY()) < RobotConstants.JOYSTICK_DEADBAND) {
            return false;
        }
        return true;
    }

    private void drive() {
        if (isOverrideDriver()) {
            RobotCommon.runningRobot.driveTrain
                    .setPower(ArcadeDriveCalculator.INSTANCE.compute(
                            Vectors.fromJoystick(firstJoystick, true)));
        }
    }

    private void moveArm() {
        if (!arm.getPIDMode()) {
            arm.setPower(arm.getMaxPower() * secondJoystick.getY());
        }
    }

    private void stopArm() {
        arm.setPower(0);
    }

    private void stopDrive() {
        RobotCommon.runningRobot.driveTrain.setPower(new Vector2d(0, 0));
    }

    @Override
    public void endModule() {
        arm = null;
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
        SmartDashboard.putNumber("FlyWheel Upper RPS", upperFlywheel.getRPS());
        SmartDashboard.putNumber("FlyWheel Lower RPS", lowerFlywheel.getRPS());
    }

    /**
     * @return Weather CoDriver is overriding Driver control.
     */
    public static boolean isOverrideDriver() {
        return overrideDriver;
    }

    /**
     * 
     * @param od
     *            weather to override driver or not.
     */
    public static void setOverrideDriver(boolean od) {
        overrideDriver = od;
    }

}
