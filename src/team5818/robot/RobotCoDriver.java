package team5818.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.commands.Collect;
import team5818.robot.commands.SetArmAngle;
import team5818.robot.commands.SetArmPower;
import team5818.robot.commands.SetFlywheelPower;
import team5818.robot.commands.SetFlywheelVelocity;
import team5818.robot.commands.SwitchFeed;
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
     * Button to move the arm to high angle. 90 Degrees.
     */
    JoystickButton butHighAngle = new JoystickButton(firstJoystick, 5);

    /**
     * Button to move the arm to medium angle. 45 degrees.
     */
    JoystickButton butMedAngle = new JoystickButton(firstJoystick, 3);

    /**
     * Button to move the arm to 0 degrees.
     */
    JoystickButton butCollectAngle = new JoystickButton(firstJoystick, 4);
    /**
     * Button to turn arm into manual control.
     */
    JoystickButton butSetPower = new JoystickButton(firstJoystick, 2);
    /**
     * button to spin up and spin down flywheel
     */
    JoystickButton butSpinFlywheel = new JoystickButton(secondJoystick, 2);
    /**
     * button to switch to feed 1
     */
    JoystickButton butSwitchFeed1 = new JoystickButton(secondJoystick, 6);
    /**
     * button to switch feed 2
     */
    JoystickButton butSwitchFeed2 = new JoystickButton(secondJoystick, 4);
    /**
     * button load flywheel (spin collector)
     */
    JoystickButton butLoadShooter = new JoystickButton(secondJoystick, 1);

    private static final Joystick firstJoystick =
            new Joystick(RobotConstants.CODRIVER_FIRST_JOYSTICK_PORT);
    private static final Joystick secondJoystick =
            new Joystick(RobotConstants.CODRIVER_SECOND_JOYSTICK_PORT);

    private Collect collect;
    private FlyWheel lowerFlywheel;
    private FlyWheel upperFlywheel;
    private Collector collector;
    private Arm arm;
    double collectAngle =
            Preferences.getInstance().getDouble("ArmCollectAngke", -6);

    @Override
    public void initModule() {
        arm = RobotCommon.runningRobot.arm;
        collector = RobotCommon.runningRobot.collector;
        lowerFlywheel = RobotCommon.runningRobot.lowerFlywheel;
        upperFlywheel = RobotCommon.runningRobot.upperFlywheel;
        LiveWindow.addActuator("Flywheel", "Lower PID",
                lowerFlywheel.getPIDController());
        LiveWindow.addActuator("Flywheel", "Upper PID",
                upperFlywheel.getPIDController());

        butHighAngle.whenPressed(new SetArmAngle(90));
        butMedAngle.whenPressed(new SetArmAngle(45));
        butCollectAngle.whenPressed(new SetArmAngle(collectAngle));
        butSetPower.whenPressed(new SetArmPower(0));
        butSpinFlywheel.whenPressed(new SetFlywheelVelocity(FlyWheel.SHOOT_VELOCITY_UPPER, FlyWheel.SHOOT_VELOCITY_LOWER));
        butSpinFlywheel.whenReleased(new SetFlywheelPower(0));
        butSwitchFeed1.whenPressed(new SwitchFeed(1));
        butSwitchFeed2.whenPressed(new SwitchFeed(2));
    }

    @Override
    public void teleopPeriodicModule() {
        if (firstJoystick.getRawButton(BUT_PRINT_ANGLE)) {
            SmartDashboard.putNumber("Arm Angle = ", arm.getAngle());
            SmartDashboard.putNumber("Upper flywheel", upperFlywheel.getRPS());
            SmartDashboard.putNumber("Lower flywheel", lowerFlywheel.getRPS());
        }
        if (!arm.getPIDMode()) {
            arm.setPower(firstJoystick.getY());
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
