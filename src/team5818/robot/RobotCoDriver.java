package team5818.robot;

import java.text.Format;
import java.util.Formatter;

import javax.swing.text.NumberFormatter;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.commands.Collect;
import team5818.robot.commands.SetFlywheelVelocity;
import team5818.robot.commands.SetArmAngle;
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
     * goes to angle specified by throttle
     */
    JoystickButton BUT_THROTTLE_ANGLE = new JoystickButton(firstJoystick, 8);
    /**
     * goes to high shooting angle
     */
    JoystickButton BUT_HIGH_ANGLE = new JoystickButton(firstJoystick, 5);
    /**
     * goes to 0 degrees
     */
    JoystickButton BUT_ZERO_DEG = new JoystickButton(firstJoystick, 3);
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
    public static final int BUT_SHOOT_HIGH = 1;
    /**
     * returns error from arm PID
     */
    public static final int ERROR_BUTTON = 1;

    /**
     * Button starts the flywheel.
     */
    private static final int BUT_START_FLYWHEEL = 1;
    /**
     * Button stops the flywheel.
     */
    private static final int BUT_STOP_FLYWHEEL = 2;

    private static final Joystick firstJoystick =
            new Joystick(RobotConstants.CODRIVER_FIRST_JOYSTICK_PORT);
    private static final Joystick secondJoystick =
            new Joystick(RobotConstants.CODRIVER_SECOND_JOYSTICK_PORT);

    private Collect collect;
    private FlyWheel lowerFlywheel;
    private FlyWheel upperFlywheel;
    private Collector collector;
    private Arm arm;

    private boolean pidMode = false;

    @Override
    public void initModule() {
        arm = RobotCommon.runningRobot.arm;
        collector = RobotCommon.runningRobot.collector;
        lowerFlywheel = RobotCommon.runningRobot.lowerFlywheel;
        upperFlywheel = RobotCommon.runningRobot.upperFlywheel;
        collect = new Collect();
        LiveWindow.addActuator("Flywheel", "Lower PID",
                lowerFlywheel.getPIDController());
        LiveWindow.addActuator("Flywheel", "Upper PID",
                upperFlywheel.getPIDController());
        
        JoystickButton butStartFlywheel = new JoystickButton(firstJoystick, BUT_START_FLYWHEEL);
        butStartFlywheel.whenPressed(new SetFlywheelVelocity(144));
        JoystickButton butStopFlywheel = new JoystickButton(firstJoystick, BUT_STOP_FLYWHEEL);
        butStopFlywheel.whenPressed(new SetFlywheelVelocity(0));
        
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
        if (firstJoystick.getRawButton(BUT_ENTER_PID)) {
            pidMode = true;
            DriverStation.reportError("Entering PID Mode", false);
        }
        
        if (firstJoystick.getRawButton(BUT_EXIT_PID)) {
            pidMode = false;
            DriverStation.reportError("Exiting PID Mode", false);
        }
        
        if (firstJoystick.getRawButton(BUT_PRINT_ANGLE)) {
            SmartDashboard.putString("DB/String 7", "" + arm.getAngle());
        }
        
        if (pidMode) {
            double target = 45 * (1 - firstJoystick.getThrottle());
            BUT_THROTTLE_ANGLE.whenPressed(new SetArmAngle(target));
            BUT_HIGH_ANGLE.whenPressed(new SetArmAngle(60));
            BUT_ZERO_DEG.whenPressed(new SetArmAngle(0));
        }
        
        if (!pidMode) {
        
            arm.setPower(firstJoystick.getY());
            if (firstJoystick.getRawButton(BUT_UP_ANGLE)) {
                arm.aimAdjust(true);
            }
            if (firstJoystick.getRawButton(BUT_DOWN_ANGLE)) {
                arm.aimAdjust(false);
            }
        }

        collector.setPower((Collect.MAX_COLLECT_POWER));

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
