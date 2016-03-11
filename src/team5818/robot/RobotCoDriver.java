package team5818.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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

    // Joystick One Buttons
    private static final int BUT_PRINT_ANGLE = 7;
    private static final int BUT_ARM_ANGLE_HOME = 5;
    private static final int BUT_ARM_ANGLE_ZERO = 4;
    private static final int BUT_SET_ARM_POWER = 2;

    // Joystick Two Buttons
    private static final int BUT_SWITCH_SHOOT_FEED = 12;
    private static final int BUT_SWITCH_DRIVER_FEED = 11;
    private static final int BUT_LED_ON = 8;
    private static final int BUT_LED_OFF = 7;
    private static final int BUT_SHOOT_ANGLE_HIGH = 5;
    private static final int BUT_SHOOT_ANGLE_MED_HIGH = 4;
    private static final int BUT_SHOOT_ANGLE_MED_LOW = 3;
    private static final int BUT_SHOOT_ANGLE_LOW = 4;
    private static final int BUT_SPIN_FLYWHEEL = 2;
    private static final int BUT_COLLECT = 1;

    // Different arm angles
    private double shootAngleHigh = 60;
    private double shootAngleMedHigh = 50;
    private double shootAngleMedLow = 40;
    private double shootAngleLow = 30;
    private double armAngleZero = 0;
    private double armAngleHome = 85;

    // First Joystick Buttons
    JoystickButton butSetArmPower =
            new JoystickButton(firstJoystick, BUT_SET_ARM_POWER);
    JoystickButton butArmAngleHome =
            new JoystickButton(firstJoystick, BUT_ARM_ANGLE_HOME);
    JoystickButton butArmAngleZero =
            new JoystickButton(firstJoystick, BUT_ARM_ANGLE_ZERO);

    // Second Joystick Buttons
    JoystickButton butSpinFlywheel =
            new JoystickButton(secondJoystick, BUT_SPIN_FLYWHEEL);
    JoystickButton butCollect = new JoystickButton(secondJoystick, BUT_COLLECT);
    JoystickButton butShootAngleLow =
            new JoystickButton(secondJoystick, BUT_SHOOT_ANGLE_LOW);
    JoystickButton butShootAngleMedLow =
            new JoystickButton(secondJoystick, BUT_SHOOT_ANGLE_MED_LOW);
    JoystickButton butShootAngleMedHigh =
            new JoystickButton(secondJoystick, BUT_SHOOT_ANGLE_MED_HIGH);
    JoystickButton butShootAngleHigh =
            new JoystickButton(secondJoystick, BUT_SHOOT_ANGLE_HIGH);
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
        shootAngleMedHigh = Preferences.getInstance()
                .getDouble("ShootAngleMedHigh", shootAngleMedHigh);
        shootAngleMedLow = Preferences.getInstance()
                .getDouble("ShootAngleMedLow", shootAngleMedLow);
        shootAngleLow = Preferences.getInstance().getDouble("ShootAngleLow",
                shootAngleLow);
        armAngleHome = Preferences.getInstance().getDouble("ArmAngleHome",
                armAngleHome);
        armAngleZero = Preferences.getInstance().getDouble("ArmAngleZero",
                armAngleZero);

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
        butShootAngleMedHigh.whenPressed(new SetArmAngle(shootAngleMedHigh));
        butShootAngleMedLow.whenPressed(new SetArmAngle(shootAngleMedLow));
        butShootAngleLow.whenPressed(new SetArmAngle(shootAngleLow));
        butArmAngleHome.whenPressed(new SetArmAngle(armAngleHome));
        butArmAngleZero.whenPressed(new SetArmAngle(armAngleZero));
        butLedOn.whenPressed(new LEDToggle(true));
        butLedOff.whenPressed(new LEDToggle(false));
        butCollect.whenPressed(new Collect(Collect.COLLECT_POWER));
        butCollect.whenReleased(new Collect(0));
        butArmAngleZero.whenPressed(new SetArmAngle(armAngleZero));
        butSetArmPower.whenPressed(new SetArmPower(0));
        butLedOn.whenPressed(new LEDToggle(true));
        butLedOff.whenPressed(new LEDToggle(false));
        butCollect.whenPressed(new Collect(Collect.COLLECT_POWER));
        butCollect.whenReleased(new Collect(0));
        /*
         * TODO Check if this call is needed
         * butSwitchShootFeed .whenPressed(new
         * SwitchFeed(ComputerVision.CAMERA_SHOOTER));
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

        // Manual Arm Mode
        if (!arm.getPIDMode()) {
            arm.setPower(arm.getMaxPower() * secondJoystick.getY());
        }

        // Overrides Driver Control.
        if (secondJoystick.getRawButton(BUT_SPIN_FLYWHEEL)) {
            setOverrideDriver(true);

        } else {
            setOverrideDriver(false);
        }

        // Control the DriverTrain if Overriding Drive Control
        if (isOverrideDriver()) {
            RobotCommon.runningRobot.driveTrainController
                    .setPowerDirectly(ArcadeDriveCalculator.INSTANCE.compute(
                            Vectors.fromJoystick(firstJoystick, true)));

        }
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
