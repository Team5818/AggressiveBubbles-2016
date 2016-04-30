package team5818.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.commands.ActuateServo;
import team5818.robot.commands.AutoAim;
import team5818.robot.commands.AutoAimTeleopFix;
import team5818.robot.commands.Collect;
import team5818.robot.commands.DrivePower;
import team5818.robot.commands.FlywheelVelocityProfile;
import team5818.robot.commands.SetArmAngle;
import team5818.robot.commands.SetArmPower;
import team5818.robot.commands.SetDrivePower;
import team5818.robot.commands.SetFlywheelPower;
import team5818.robot.commands.SetFlywheelVelocity;
import team5818.robot.commands.ShootIfReady;
import team5818.robot.commands.SwitchFeed;
import team5818.robot.commands.LEDToggle;
import team5818.robot.modules.Arm;
import team5818.robot.modules.ClimbWinch;
import team5818.robot.modules.ClimbWinchs;
import team5818.robot.modules.ComputerVision;
import team5818.robot.modules.ComputerVision;
import team5818.robot.modules.FlyWheel;
import team5818.robot.modules.Module;
import team5818.robot.modules.drivetrain.ArcadeDriveCalculator;
import team5818.robot.modules.drivetrain.DriveTrain;
import team5818.robot.util.LinearLookupTable;
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

    // Joystick One Buttons
    private static final int BUT_MODE_CLIMB = 11;
    private static final int BUT_ADD_ANGLE_CAP = 10;
    private static final int BUT_EXTEND_SERVO = 9;
    private static final int BUT_WINCH_MODE_VELOCITY = 9; // Climb Mode
    private static final int BUT_RETRACT_SERVO = 8;
    private static final int BUT_WINCH_MODE_POWER = 8; // Climb Mode
    private static final int BUT_REMOVE_ANGLE_CAP = 7;
    private static final int BUT_NOT_MODE_CLIMB = 6;
    private static final int BUT_INC_AUTO_X_OFFSET = 5;
    private static final int BUT_DEC_AUTO_X_OFFSET = 4;
    private static final int BUT_INC_AUTO_Y_OFFSET = 3;
    private static final int BUT_DEC_AUTO_Y_OFFSET = 2;
    // private static final int BUT_SET_ARM_POWER = 2;
    private static final int BUT_AUTO_AIM = 1;

    // Joystick Two Buttons
    private static final int BUT_SWITCH_SHOOT_FEED = 12;
    private static final int BUT_SWITCH_DRIVER_FEED = 11;
    private static final int BUT_ZERO_POT = 9;
    private static final int BUT_LED_ON = 8;
    private static final int BUT_LED_OFF = 7;
    private static final int BUT_UNCOLLECT = 5;
    private static final int BUT_OVERRIDE_DRIVER = 4;
    private static final int BUT_STOP_FLYWHEEL = 3;
    private static final int BUT_SPIN_FLYWHEEL = 2;
    private static final int BUT_COLLECT = 1;

    // First Joystick Buttons
    // JoystickButton butTopSpinShot =
    // new JoystickButton(firstJoystick, BUT_TOPSPIN_SHOT);
    // JoystickButton butSetArmPower =
    // new JoystickButton(firstJoystick, BUT_SET_ARM_POWER);
    // JoystickButton butModeClimb =
    // new JoystickButton(firstJoystick, BUT_MODE_CLIMB);
    // JoystickButton butSwitchFeedShoot =
    // new JoystickButton(firstJoystick, BUT_SWITCH_FEED_SHOOTER);
    // JoystickButton butNotModeClimb =
    // new JoystickButton(firstJoystick, BUT_NOT_MODE_CLIMB);
    JoystickButton butAutoAim = new JoystickButton(firstJoystick, BUT_AUTO_AIM);
    JoystickButton butExtendServo =
            new JoystickButton(firstJoystick, BUT_EXTEND_SERVO);
    JoystickButton butRetractServo =
            new JoystickButton(firstJoystick, BUT_RETRACT_SERVO);

    // Second Joystick Buttons
    JoystickButton butSpinFlywheel =
            new JoystickButton(secondJoystick, BUT_SPIN_FLYWHEEL);
    JoystickButton butStopFlywheel =
            new JoystickButton(secondJoystick, BUT_STOP_FLYWHEEL);
    JoystickButton butCollect = new JoystickButton(secondJoystick, BUT_COLLECT);
    JoystickButton butUncollect =
            new JoystickButton(secondJoystick, BUT_UNCOLLECT);
    JoystickButton butLedOn = new JoystickButton(secondJoystick, BUT_LED_ON);
    JoystickButton butLedOff = new JoystickButton(secondJoystick, BUT_LED_OFF);
    JoystickButton butSwitchShootFeed =
            new JoystickButton(secondJoystick, BUT_SWITCH_SHOOT_FEED);
    JoystickButton butSwitchDriverFeed =
            new JoystickButton(secondJoystick, BUT_SWITCH_DRIVER_FEED);
    JoystickButton butOverrideDriver =
            new JoystickButton(secondJoystick, BUT_OVERRIDE_DRIVER);

    private FlyWheel lowerFlywheel;
    private FlyWheel upperFlywheel;
    private Arm arm;

    // Weather CoDriver is overriding driver control.
    private static boolean overrideDriver = false;
    private boolean hasStoppedArm = false;
    private boolean hasStoppedDrive = false;
    private boolean modeClimb = false;
    private boolean modeWinchVelocity = false;
    private boolean hasStoppedClimbArm = false;
    private boolean hasStoppedLeftClimbWinch = false;
    private boolean hasStoppedRightClimbWinch = false;

    // Different arm angles
    private double shootAngleHigh = 60;
    private double shootAngleMed = 40;
    private double shootAngleLow = 30;
    private double[] lowerFlyVels = { -17, -2, -1, -1, -1 };
    //private double[] lowerFlyVels = {0, 0, 0, 0, 0};
    // private double[] upperFlyVels = {0,0,0,0, 0};
    private double[] upperFlyVels = { -3.5, -3.6, -3.7, -6, -7 };
    private double[] flyTimes = { 0, 800, 1600, 2400, 3000 };
    private LinearLookupTable lowerTable =
            new LinearLookupTable(flyTimes, lowerFlyVels);
    private LinearLookupTable upperTable =
            new LinearLookupTable(flyTimes, upperFlyVels);

    private double coDriveDamp = 0.25;
    private AutoAimTeleopFix autoAim;
    private double armAngleShooting = 35;
    private double armAngleCollect = 2.5;
    private boolean hasStoppedPidX = false;
    private double autoAimYOffset;
    private double autoAimXOffset;
    private boolean hasSetAutoYOffset;
    private boolean hasSetAutoXOffset;

    @Override
    public void initModule() {
        getAutoAimOffset();
        autoAim = new AutoAimTeleopFix(autoAimXOffset, autoAimYOffset);
        // Setting the singletons to local fields for easy access.
        arm = RobotCommon.runningRobot.arm;
        lowerFlywheel = RobotCommon.runningRobot.lowerFlywheel;
        upperFlywheel = RobotCommon.runningRobot.upperFlywheel;

        // Adding PID's to SmartDashboard
        LiveWindow.addActuator("Flywheel", "Lower PID",
                lowerFlywheel.getPIDController());
        LiveWindow.addActuator("Flywheel", "Upper PID",
                upperFlywheel.getPIDController());

        initTeleopButtons();
    }

    public void initTeleopButtons() {
        // Settings the preferences
        shootAngleHigh = Preferences.getInstance().getDouble("ShootAngleHigh",
                shootAngleHigh);
        shootAngleMed = Preferences.getInstance().getDouble("ShootAngleMed",
                shootAngleMed);
        shootAngleLow = Preferences.getInstance().getDouble("ShootAngleLow",
                shootAngleLow);
        coDriveDamp = Preferences.getInstance().getDouble("CoDriveDamp", 0.25);
        double armTopSpinAngle =
                Preferences.getInstance().getDouble("ArmAngleTopSpin", 35);
        double flyTopSpinVelLow =
                Preferences.getInstance().getDouble("FlyTopSpinVelLow", 70);
        double flyTopSpinVelUp =
                Preferences.getInstance().getDouble("FlyTopSpinVelUp", 70);
        armAngleShooting = Preferences.getInstance()
                .getDouble("ArmAngleShooting", armAngleShooting);
        armAngleCollect = Preferences.getInstance().getDouble("ArmAngleCollect",
                armAngleCollect);

        // Making the command groups
        CommandGroup startFlywheel = new CommandGroup();
        startFlywheel.addParallel(new SetFlywheelVelocity(
                FlyWheel.SHOOT_VELOCITY_UPPER, FlyWheel.SHOOT_VELOCITY_LOWER));
        startFlywheel
                .addParallel(new SwitchFeed(ComputerVision.CAMERA_SHOOTER));
        CommandGroup stopFlywheel = new CommandGroup();
        // stopFlywheel.addParallel(new SetFlywheelVelocity(0,0));
        // stopFlywheel.addParallel(new SetFlywheelPower(0));
        stopFlywheel.addSequential(new SetFlywheelPower(0));
        //stopFlywheel.addSequential(
        //        new FlywheelVelocityProfile(lowerTable, upperTable, 3));
        CommandGroup switchFeedShoot = new CommandGroup();
        switchFeedShoot
                .addParallel(new SwitchFeed(ComputerVision.CAMERA_SHOOTER));
        switchFeedShoot.addParallel(new LEDToggle(true));
        CommandGroup switchFeedDrive = new CommandGroup();
        switchFeedDrive
                .addParallel(new SwitchFeed(ComputerVision.CAMERA_DRIVER));
        switchFeedDrive.addParallel(new LEDToggle(false));
        CommandGroup topSpinShot = new CommandGroup();
        topSpinShot.addParallel(
                new SetFlywheelVelocity(flyTopSpinVelUp, flyTopSpinVelLow));
        topSpinShot.addParallel(new SetArmAngle(armTopSpinAngle));
        CommandGroup overrideDriver = new CommandGroup();
        overrideDriver
                .addParallel(new SwitchFeed(ComputerVision.CAMERA_SHOOTER));
        // overrideDriver.addParallel(new SetArmAngle(armAngleShooting));
        overrideDriver.addParallel(new SetFlywheelVelocity(
                FlyWheel.SHOOT_VELOCITY_UPPER, FlyWheel.SHOOT_VELOCITY_LOWER));

        // Making command for AutoAim and Shoot
        // CommandGroup aimAndShoot = new CommandGroup();
        // aimAndShoot.addSequential(new AutoAim(false));
        // aimAndShoot.addSequential(startFlywheel);
        // aimAndShoot.addSequential(new Collect(Collect.COLLECT_POWER));

        // Making command for stopping AutoAim and Shooting
        // CommandGroup stopAimAndShoot = new CommandGroup();
        // stopAimAndShoot.addSequential(stopFlywheel);
        // stopAimAndShoot.addSequential(new AutoAim(true));

        // Assigning commands to the buttons
        butSpinFlywheel.whenPressed(startFlywheel);
        butStopFlywheel.whenPressed(stopFlywheel);
        // butSwitchFeedShoot
        // .whenPressed(new SwitchFeed(ComputerVision.CAMERA_SHOOTER));
        // butSetArmPower.whenPressed(new SetArmPower(0));
        butLedOn.whenPressed(new LEDToggle(true));
        butLedOff.whenPressed(new LEDToggle(false));
        butLedOn.whenPressed(new LEDToggle(true));
        butLedOff.whenPressed(new LEDToggle(false));
        butCollect.whenPressed(new Collect(Collect.COLLECT_POWER));
        butCollect.whenReleased(new Collect(0));
        butUncollect.whenPressed(new Collect(-Collect.COLLECT_POWER));
        butUncollect.whenReleased(new Collect(0));
        butAutoAim.whenPressed(autoAim);
        // this.butTopSpinShot.whenPressed(topSpinShot);
        butOverrideDriver.whenPressed(overrideDriver);
        // butAutoAim.whenActive(command);
        // butAutoAim.whenReleased(aimAndShoot);
        butSwitchShootFeed.whenPressed(switchFeedShoot);
        butSwitchDriverFeed.whenPressed(switchFeedDrive);
        butRetractServo.whenPressed(new ActuateServo(ActuateServo.ACT_TO_0));
        butExtendServo.whenPressed(new ActuateServo(ActuateServo.ACT_TO_90));
    }

    public void initClimbButtons() {
        Scheduler.getInstance().removeAll();
    }

    public void initClimb() {
        modeClimb = true;
        RobotCommon.runningRobot.driver.initClimb();
        this.stopArm();
        this.stopDrive();
        this.stopWinch();
        initClimbButtons();
    }

    public void unInitClimb() {
        RobotCommon.runningRobot.driver.unInitClimb();
        modeClimb = false;
        this.stopArm();
        this.stopDrive();
        this.stopWinch();
        initTeleopButtons();
    }

    @Override
    public void teleopPeriodicModule() {
        performButtonActions();
        if (modeClimb)
            climbPeriodic();
        else
            normalPeriodic();
    }

    private void normalPeriodic() {
        if (usingSecondStick()) {
            moveArm();
            hasStoppedArm = false;
        } else {
            if (!hasStoppedArm) {
                hasStoppedArm = true;
                stopArm();
            }
        }
        if (usingFirstStick()) {
            drive();
            if (autoAim.isRunning())
                autoAim.cancel();
            hasStoppedDrive = false;
        } else {
            if (!hasStoppedDrive) {
                hasStoppedDrive = true;
                stopDrive();
            }
        }
    }

    private void climbPeriodic() {
        if (usingSecondStick()) {
            RobotCommon.runningRobot.arm
                    .setPower(RobotCommon.runningRobot.arm.getMaxPower()
                            * secondJoystick.getY());
            hasStoppedArm = false;
        } else {
            if (!hasStoppedArm) {
                hasStoppedArm = true;
                new SetArmPower(0).start();
            }
        }
        if (usingFirstStick()) {
            RobotCommon.runningRobot.driveTrain.setPower(
                    ArcadeDriveCalculator.INSTANCE.computeDefault(new Vector2d(
                            firstJoystick.getX() * 0.5, firstJoystick.getY())));
            hasStoppedDrive = false;
        } else {
            if (!hasStoppedDrive) {
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
                        .getY()) < RobotConstants.JOYSTICK_DEADBAND
                && Math.abs(secondJoystick
                        .getZ()) < RobotConstants.JOYSTICK_DEADBAND * 3) {
            return false;
        }
        return true;
    }

    private void drive() {
        if (isOverrideDriver()) {
            RobotCommon.runningRobot.driveTrain
                    .setPower(ArcadeDriveCalculator.INSTANCE.computeDefault(
                            new Vector2d(firstJoystick.getX() * coDriveDamp,
                                    firstJoystick.getY() * coDriveDamp)));
        }
    }

    private void moveArm() {
        if (!arm.getPIDMode()) {
            arm.setPower(arm.getMaxPower() * secondJoystick.getY());
        }
    }

    private void stopWinch() {
        RobotCommon.runningRobot.winch.setPower(0);
    }

    private void stopArm() {
        new SetArmPower(0).start();
    }

    private void stopDrive() {
        new DrivePower(0, 0).start();
    }

    private void performButtonActions() {

        if (firstJoystick.getRawButton(BUT_INC_AUTO_X_OFFSET)) {
            if(!hasSetAutoXOffset)
                autoAimXOffset -= 0.5;
            updateAutoAim();
            putAutoAimOffset();
            hasSetAutoXOffset = true;
        } else if (firstJoystick.getRawButton(BUT_DEC_AUTO_X_OFFSET)) {
            if(!hasSetAutoXOffset)
                autoAimXOffset += 0.5;
            updateAutoAim();
            putAutoAimOffset();
            hasSetAutoXOffset = true;
        } else {
            hasSetAutoXOffset = false;
        }
        
        if (firstJoystick.getRawButton(BUT_INC_AUTO_Y_OFFSET)) {
            if(!hasSetAutoYOffset)
                autoAimYOffset -= 0.5;
            hasSetAutoYOffset = true;
            updateAutoAim();
            putAutoAimOffset();
        } else if (firstJoystick.getRawButton(BUT_DEC_AUTO_Y_OFFSET)) {
            if(!hasSetAutoYOffset)
                autoAimYOffset += 0.5;
            hasSetAutoYOffset = true;
            updateAutoAim();
            putAutoAimOffset();
        } else {
            hasSetAutoYOffset = false;
        }

        if (firstJoystick.getRawButton(BUT_MODE_CLIMB))
            initClimb();
        if (firstJoystick.getRawButton(BUT_NOT_MODE_CLIMB))
            unInitClimb();

        if (modeClimb) { /* Do not do non climb buttons. */
            if (firstJoystick.getRawButton(BUT_WINCH_MODE_VELOCITY))
                modeWinchVelocity = true;
            if (firstJoystick.getRawButton(BUT_WINCH_MODE_POWER))
                modeWinchVelocity = false;

            return;
        }

        if (firstJoystick.getRawButton(BUT_AUTO_AIM) && !autoAim.isRunning()) {
            autoAim.pidX();
            autoAim.pidY();
            hasStoppedPidX = false;
        } else if (!hasStoppedPidX) {
            hasStoppedPidX = true;
            new SetDrivePower(0, 0).start();
            new SetArmPower(0).start();
            autoAim.cancel();
        }

        if (secondJoystick.getRawButton(BUT_ZERO_POT)) {
            arm.zeroPot();
        }
        
        if (firstJoystick.getRawButton(BUT_REMOVE_ANGLE_CAP)){
            arm.uncapAngle();
        }
        
        if (firstJoystick.getRawButton(BUT_ADD_ANGLE_CAP)){
            arm.capAngle();
        }

        // Overrides Driver Control.
        if (secondJoystick.getRawButton(BUT_OVERRIDE_DRIVER)) {
            stopDrive();
            setOverrideDriver(true);
            RobotCommon.runningRobot.enableGetData();
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
        RobotCommon.runningRobot.driveTrain.setPower(new Vector2d(0, 0));
    }
    
    public void updateAutoAim() {
        autoAim.setXOffset(autoAimXOffset);
        autoAim.setYOffset(autoAimYOffset);
    }
    
    /**
     * updates the smartdashboard to the current auto offsets.
     */
    public void putAutoAimOffset() {
        Preferences.getInstance().putDouble("AutoAimXOffsetTeleop", autoAimXOffset);
        Preferences.getInstance().putDouble("AutoAimYOffsetTeleop", autoAimYOffset);
    }
    
    /**
     * Updates the fields with the auto offsets from the smartdashboard.
     */
    public void getAutoAimOffset() {
        autoAimXOffset = Preferences.getInstance().getDouble("AutoAimXOffsetTeleop", autoAimXOffset);
        autoAimYOffset = Preferences.getInstance().getDouble("AutoAimYOffsetTeleop", autoAimYOffset);
    }

}
