package team5818.robot;

import java.text.Format;
import java.util.Formatter;

import javax.swing.text.NumberFormatter;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.commands.Collect;
import team5818.robot.commands.SetFlyWheelVelocity;
import team5818.robot.commands.ShootHigh;
import team5818.robot.modules.Arm;
import team5818.robot.modules.Collector;
import team5818.robot.modules.FlyWheel;
import team5818.robot.modules.Module;

/**
 * The secondary robot driver. Responsible for the arm.
 */
public class RobotCoDriver implements Module {

    /**
     * The button that resets the arm offset.
     */
    public static final int BUT_ARM_RESET = 8;
    /**
     * The button that prints the arm offset.
     */
    public static final int BUT_PRINT_ANGLE = 7;
    /**
     * The button that increases the angle the arm.
     */
    public static final int BUT_UP_ANGLE = 5;
    /**
     * The button that decreases the angle of the arm.
     */
    public static final int BUT_DOWN_ANGLE = 3;
    /**
     * puts arm in PID mode
     */
    public static final int BUT_ENTER_PID = 4;
    /**
     * takes arm out of PID mode
     */
    public static final int BUT_EXIT_PID = 6;
    /**
     * goes to 45 degrees
     */
    public static final int GO_TO_ANGLE_BUTTON = 9;

    public static final int BUT_START_FLYWHEEL = 12;
    public static final int BUT_STOP_FLYWHEEL = 11;
    public static final int BUT_COLLECT = 1;
    public static final int BUT_SHOOT_HIGH = 1;

    private boolean hasStartedCollect = false;
    private boolean hasStartedShoot = false;
    private boolean hasStartedFlywheel = false;
    private boolean hasStopedFlywheel = false;

    /**
     * returns error from arm PID
     */
    public static final int ERROR_BUTTON = 1;

    private static final Joystick FIRST_JOYSTICK =
            new Joystick(RobotConstants.CODRIVER_FIRST_JOYSTICK_PORT);
    private static final Joystick SECOND_JOYSTICK =
            new Joystick(RobotConstants.CODRIVER_SECOND_JOYSTICK_PORT);

    private Collect collect;
    private FlyWheel lowerFlywheel;
    private FlyWheel upperFlywheel;
    private Collector collector;
    private Arm arm;
    private SetFlyWheelVelocity setFlyVelocity;
    private ShootHigh shootHigh;

    private boolean setAngleMode = false;
    private boolean hasStopedCollect = false;

    @Override
    public void initModule() {
        arm = RobotCommon.runningRobot.arm;
        collector = RobotCommon.runningRobot.collector;
        lowerFlywheel = RobotCommon.runningRobot.lowerFlywheel;
        upperFlywheel = RobotCommon.runningRobot.upperFlywheel;
        collect = new Collect();
        shootHigh = new ShootHigh();
        setFlyVelocity = new SetFlyWheelVelocity();
        LiveWindow.addActuator("Flywheel", "Lower PID",
                lowerFlywheel.getPIDController());
        LiveWindow.addActuator("Flywheel", "Upper PID",
                upperFlywheel.getPIDController());
        LiveWindow.addActuator("Flywheel", "Lower PID",
                lowerFlywheel.getPIDController());
        LiveWindow.addActuator("Flywheel", "Upper PID",
                upperFlywheel.getPIDController());

    }

    @Override
    public void teleopPeriodicModule() {
        /* Arm Teleop Code Stuff */
        /*
         * FIRST JoyStick: Button 9: Got To Angle if PID enabled. Button 4:
         * Enter PID. Button 6: Exit PID. Button 3: Down Angle. Button 5: Up
         * Angle. Button 8: Arm Reset. Button 7: Print Angle. Y Axis : Set Power
         * Arm if PID disabled.
         */
        // TODO Get rid of RobotCoDriver arm PID test code.
        if (FIRST_JOYSTICK.getRawButton(BUT_ENTER_PID)) {
            setAngleMode = true;
            if (setAngleMode) {
                DriverStation.reportError("Entering PID Mode", false);
            }
        }

        if (FIRST_JOYSTICK.getRawButton(BUT_EXIT_PID)) {
            setAngleMode = false;
            if (setAngleMode) {
                DriverStation.reportError("Exiting PID Mode", false);
            }
        }

        // arm.armTeleopPeriodic(); don't use in setAngleMode

        if (setAngleMode) {
            double target = 45 * (1 - FIRST_JOYSTICK.getThrottle());
            arm.goToAngle(target);
            if (FIRST_JOYSTICK.getRawButton(ERROR_BUTTON)) {
                DriverStation.reportError("" + arm.getError() + "\n", false);
            }
        } else if (FIRST_JOYSTICK.getRawButton(BUT_UP_ANGLE)) {
            arm.aimAdjust(true);
        } else if (FIRST_JOYSTICK.getRawButton(BUT_DOWN_ANGLE)) {
            arm.aimAdjust(false);
        }

        if (!setAngleMode) {
            arm.setPower(FIRST_JOYSTICK.getY());
        }

        if (FIRST_JOYSTICK.getRawButton(BUT_ARM_RESET)) {
            // arm.resetEncoder();
        }
        if (FIRST_JOYSTICK.getRawButton(BUT_PRINT_ANGLE)) {
            SmartDashboard.putString("DB/String 7", "" + arm.getAngle());
        }

        /* Flywheel Code Stuff */
        /*
         * Second Joysick: Button 1 (Trigger): Spin up flywheel.
         */
        if (SECOND_JOYSTICK.getRawButton(BUT_START_FLYWHEEL)) {
            if (!hasStartedFlywheel) {
                hasStartedFlywheel = true;
                hasStopedFlywheel = false;
                double v = 144;
                setFlyVelocity.setVelocity(v);
                setFlyVelocity.start();
            }
        } else {
            if (!hasStopedFlywheel) {
                hasStopedFlywheel = true;
                setFlyVelocity.cancel();
            }
        }

        /*
         * Collector Code Stuff First - Joystick Button 1 (Trigger): Collect
         * Command
         */
        if (FIRST_JOYSTICK.getRawButton(BUT_COLLECT)) {
            collector.setPower((Collect.MAX_COLLECT_POWER));
            if (hasStartedCollect) {
                // hasStopedCollect = false;
                // hasStartedCollect = true;
                // collect.start();
            }
        } else {
            hasStartedCollect = false;
            if (!hasStopedCollect) {
                hasStopedCollect = true;
                collector.setPower(0);
            }
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

}
