package team5818.robot;

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
     * The button that prints the arm angle.
     */
    public static final int BUT_PRINT_ANGLE = 7;
    /**
     * goes to high shooting angle degrees
     */
    public static final int BUT_HIGH_ANGLE = 8;
    /**
     * goes to 0 degrees
     */
    public static final int BUT_ZERO_DEG = 9;
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

    private boolean pidMode = false;

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
        /*
         * Arm teleop FIRST JoyStick: Button 8: Go to high shooting position
         * Button 9: Go to zero degrees Button 4: Enter PID Button 6: Exit PID
         * Button 3: Down Angle Button 5: Up Angle Button 7: Print Angle Y Axis
         * : Set Power Arm if PID disabled
         */
        // TODO Get rid of RobotCoDriver arm PID test code.
        if (FIRST_JOYSTICK.getRawButton(BUT_ENTER_PID)) {
            pidMode = true;
            DriverStation.reportError("Entering PID Mode", false);
        }

        if (FIRST_JOYSTICK.getRawButton(BUT_EXIT_PID)) {
            pidMode = false;
            DriverStation.reportError("Exiting PID Mode", false);
        }

        if (FIRST_JOYSTICK.getRawButton(BUT_PRINT_ANGLE)) {
            SmartDashboard.putString("DB/String 7", "" + arm.getAngle());
        }

        if (pidMode) {
            double target = 45 * (1 - FIRST_JOYSTICK.getThrottle());
            arm.goToAngle(target);
            if (FIRST_JOYSTICK.getRawButton(ERROR_BUTTON)) {
                DriverStation.reportError("" + arm.getError() + "\n", false);
            }
        }

        if (!pidMode) {

            arm.setPower(FIRST_JOYSTICK.getY());
            if (FIRST_JOYSTICK.getRawButton(BUT_UP_ANGLE)) {
                arm.aimAdjust(true);
            }
            if (FIRST_JOYSTICK.getRawButton(BUT_DOWN_ANGLE)) {
                arm.aimAdjust(false);
            }
            if (FIRST_JOYSTICK.getRawButton(BUT_HIGH_ANGLE)) {
                arm.goToAngle(60);
            }
            if (FIRST_JOYSTICK.getRawButton(BUT_ZERO_DEG)) {
                arm.goToAngle(0);
            }
        }

        /* Flywheel Code Stuff */
        /*
         * Second Joysick: Button 12: Start Flywheel Button 11: Stop Flywheel
         */
        if (SECOND_JOYSTICK.getRawButton(BUT_START_FLYWHEEL)) {
            if (!hasStartedFlywheel) {
                double v = 144;
                setFlyVelocity.setVelocity(v);
                setFlyVelocity.start();
                hasStartedFlywheel = true;
            }
        } else {

            hasStartedFlywheel = false;
        }

        if (SECOND_JOYSTICK.getRawButton(BUT_STOP_FLYWHEEL)) {
            if (!hasStopedFlywheel) {
                RobotCommon.runningRobot.lowerFlywheel.setPower(0);
                RobotCommon.runningRobot.lowerFlywheel.setPower(0);

                hasStopedFlywheel = true;
            }
        } else {

            hasStopedFlywheel = false;
        }
        SmartDashboard.putNumber("Lower Flywheel RPS",
                RobotCommon.runningRobot.lowerFlywheel.getRPS());
        SmartDashboard.putNumber("Upper Flywheel RPS",
                RobotCommon.runningRobot.upperFlywheel.getRPS());

        /*
         * Collector Code Stuff First Joystick Button 1 (Trigger): Collect
         * Command
         */
        if (FIRST_JOYSTICK.getRawButton(BUT_COLLECT)) {
            collector.setPower((Collect.MAX_COLLECT_POWER));
            // if(hasStartedCollect) {
            // hasStartedCollect = true;
            // collect.start();
            // }
        } else {
            collector.setPower(0);
            hasStartedCollect = false;
        }

        /*
         * Shooter Code Stuff Second Joystick: Button 1 (Trigger): Shooter
         * Command
         */
        if (SECOND_JOYSTICK.getRawButton(BUT_SHOOT_HIGH)) {
            if (hasStartedShoot) {
                hasStartedShoot = true;
                // if(!shootHigh.isRunning())
                // shootHigh.start();
            }
            lowerFlywheel.setVelocity(144);
            upperFlywheel.setVelocity(144);
        } else {
            hasStartedShoot = false;
            // if (!shootHigh.isRunning()) {
            // }
            lowerFlywheel.setPower(0);
            upperFlywheel.setPower(0);
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
