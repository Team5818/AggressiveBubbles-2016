package team5818.robot;

import org.usfirst.frc.team5818.robot.Robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.commands.ActuateServo;
import team5818.robot.commands.AutoAim;
import team5818.robot.commands.Collect;
import team5818.robot.commands.DrivePower;
import team5818.robot.commands.SetArmPower;
import team5818.robot.commands.SetDrivePower;
import team5818.robot.commands.SetFlywheelPower;
import team5818.robot.commands.SetFlywheelVelocity;
import team5818.robot.commands.SwitchFeed;
import team5818.robot.modules.Arm;
import team5818.robot.modules.ComputerVision;
import team5818.robot.modules.FlyWheel;
import team5818.robot.modules.Module;
import team5818.robot.modules.drivetrain.ArcadeDriveCalculator;
import team5818.robot.util.Vector2d;

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

    /**
     * Button indices for first joystick
     */
    private static final int BUT_MODE_CLIMB = 11;
    private static final int BUT_EXTEND_SERVO = 9;
    private static final int BUT_RETRACT_SERVO = 8;
    private static final int BUT_NOT_MODE_CLIMB = 6;
    private static final int BUT_INC_AUTO_X_OFFSET = 5;
    private static final int BUT_DEC_AUTO_X_OFFSET = 4;
    private static final int BUT_INC_AUTO_Y_OFFSET = 3;
    private static final int BUT_DEC_AUTO_Y_OFFSET = 2;
    private static final int BUT_AUTO_AIM = 1;

    /**
     * Button indices for second joystick
     */
    private static final int BUT_SWITCH_SHOOT_FEED = 12;
    private static final int BUT_SWITCH_DRIVER_FEED = 11;
    private static final int BUT_ZERO_POT = 9;
    private static final int BUT_UNCOLLECT = 5;
    private static final int BUT_OVERRIDE_DRIVER = 4;
    private static final int BUT_STOP_FLYWHEEL = 3;
    private static final int BUT_SPIN_FLYWHEEL = 2;
    private static final int BUT_COLLECT = 1;

    /**
     * Creating all button objects for first joystick
     */
    JoystickButton butAutoAim = new JoystickButton(firstJoystick, BUT_AUTO_AIM);
    JoystickButton butExtendServo =
            new JoystickButton(firstJoystick, BUT_EXTEND_SERVO);
    JoystickButton butRetractServo =
            new JoystickButton(firstJoystick, BUT_RETRACT_SERVO);

    /**
     * Crating all button objects for second joystick
     */
    JoystickButton butSpinFlywheel =
            new JoystickButton(secondJoystick, BUT_SPIN_FLYWHEEL);
    JoystickButton butStopFlywheel =
            new JoystickButton(secondJoystick, BUT_STOP_FLYWHEEL);
    JoystickButton butCollect = new JoystickButton(secondJoystick, BUT_COLLECT);
    JoystickButton butUncollect =
            new JoystickButton(secondJoystick, BUT_UNCOLLECT);
    JoystickButton butSwitchShootFeed =
            new JoystickButton(secondJoystick, BUT_SWITCH_SHOOT_FEED);
    JoystickButton butSwitchDriverFeed =
            new JoystickButton(secondJoystick, BUT_SWITCH_DRIVER_FEED);
    JoystickButton butOverrideDriver =
            new JoystickButton(secondJoystick, BUT_OVERRIDE_DRIVER);

    /**
     * Declaring robot components
     */
    private FlyWheel lowerFlywheel;
    private FlyWheel upperFlywheel;
    private Arm arm;

    /**
     * Boolean variables defining state of the robot
     */
    private static boolean overrideDriver = false;
    private boolean hasStoppedArm = false;
    private boolean hasStoppedDrive = false;
    private boolean modeClimb = false;

    private boolean hasStoppedPidX = false;
    private boolean hasSetAutoYOffset;
    private boolean hasSetAutoXOffset;

    /**
     * Various angles for the robot's  arm
     */
    private double armAngleShooting = 35;
    private double armAngleCollect = 2.5;
    

    private double coDriveDamp = 0.25;
    private AutoAim autoAim;

    private double autoAimYOffset;
    private double autoAimXOffset;


    @Override
    public void initModule() {
        getAutoAimOffset();
        autoAim = new AutoAim(autoAimXOffset, autoAimYOffset);
        arm = Robot.runningRobot.arm;
        lowerFlywheel = Robot.runningRobot.lowerFlywheel;
        upperFlywheel = Robot.runningRobot.upperFlywheel;

        initTeleopButtons();
    }

    /**
     * Assigns all teleop commands to buttons
     */
    public void initTeleopButtons() {
        // Setting various constants from Smart Dashboard preferences
        coDriveDamp = Preferences.getInstance().getDouble("CoDriveDamp", 0.25);
        armAngleShooting = Preferences.getInstance()
                .getDouble("ArmAngleShooting", armAngleShooting);
        armAngleCollect = Preferences.getInstance().getDouble("ArmAngleCollect",
                armAngleCollect);

        // Making command groups for more complicated tasks 
        CommandGroup startFlywheel = new CommandGroup();
        startFlywheel.addParallel(new SetFlywheelVelocity(
                FlyWheel.SHOOT_VELOCITY_UPPER, FlyWheel.SHOOT_VELOCITY_LOWER));
        startFlywheel
                .addParallel(new SwitchFeed(ComputerVision.CAMERA_SHOOTER));
        
        CommandGroup overrideDriver = new CommandGroup();
        overrideDriver
                .addParallel(new SwitchFeed(ComputerVision.CAMERA_SHOOTER));
        overrideDriver.addParallel(new SetFlywheelVelocity(
                FlyWheel.SHOOT_VELOCITY_UPPER, FlyWheel.SHOOT_VELOCITY_LOWER));



        /**
         * Assign commands to buttons
         */
        butSpinFlywheel.whenPressed(startFlywheel);
        butStopFlywheel.whenPressed(new SetFlywheelPower(0));
        butCollect.whenPressed(new Collect(Collect.COLLECT_POWER));
        butCollect.whenReleased(new Collect(0));
        butUncollect.whenPressed(new Collect(-Collect.COLLECT_POWER));
        butUncollect.whenReleased(new Collect(0));
        butAutoAim.whenPressed(autoAim);
        butOverrideDriver.whenPressed(overrideDriver);
        butSwitchShootFeed.whenPressed(new SwitchFeed(ComputerVision.CAMERA_SHOOTER));
        butSwitchDriverFeed.whenPressed(new SwitchFeed(ComputerVision.CAMERA_DRIVER));
        butRetractServo.whenPressed(new ActuateServo(ActuateServo.RETRACTED));
        butExtendServo.whenPressed(new ActuateServo(ActuateServo.EXTENDED));
    }

    /**
     * stops all motion and commands to prepare for endgame
     */
    public void initClimb() {
        modeClimb = true;
        Robot.runningRobot.driver.initClimb();
        this.stopArm();
        this.stopDrive();
        this.stopWinch();
        Scheduler.getInstance().removeAll();
    }
    
    /**
     * Stops all climbing systems to resume normal teleop driving
     */
    public void exitClimb() {
        Robot.runningRobot.driver.exitClimb();
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
            Robot.runningRobot.arm
                    .setPower(Robot.runningRobot.arm.getMaxPower()
                            * secondJoystick.getY());
            hasStoppedArm = false;
        } else {
            if (!hasStoppedArm) {
                hasStoppedArm = true;
                new SetArmPower(0).start();
            }
        }
        if (usingFirstStick()) {
            Robot.runningRobot.driveTrain.setPower(
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
            Robot.runningRobot.driveTrain
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
        Robot.runningRobot.winch.setPower(0);
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
            exitClimb();

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
        

        // Overrides Driver Control.
        if (secondJoystick.getRawButton(BUT_OVERRIDE_DRIVER)) {
            stopDrive();
            setOverrideDriver(true);
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
        Robot.runningRobot.driveTrain.setPower(new Vector2d(0, 0));
    }
    
    /**
     * Updates the fine tuning for auto aim in light of driver feedback
     */
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
