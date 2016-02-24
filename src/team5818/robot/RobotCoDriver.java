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
import team5818.robot.util.Vectors;

/**
 * The secondary robot driver. Responsible for the arm.
 */
public class RobotCoDriver implements Module {
    
    private static boolean overrideDriver = false;

    private static final int BUT_PRINT_ANGLE = 7;
    private static final int BUT_SPIN_FLYWHEEL = 2;
    private static final int BUT_COLLECT = 1;

    JoystickButton butHighAngle = new JoystickButton(firstJoystick, 5);
    JoystickButton butMedAngle = new JoystickButton(firstJoystick, 3);
    JoystickButton butCollectAngle = new JoystickButton(firstJoystick, 4);
    JoystickButton butSetPower = new JoystickButton(firstJoystick, 2);
    JoystickButton butSwitchFeed1 = new JoystickButton(secondJoystick, 6);
    JoystickButton butSwitchFeed2 = new JoystickButton(secondJoystick, 4);
    JoystickButton butSpinFlywheel = new JoystickButton(secondJoystick, BUT_SPIN_FLYWHEEL);
    JoystickButton butCollect = new JoystickButton(secondJoystick, BUT_COLLECT);
    
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
            Preferences.getInstance().getDouble("ArmCollectAngle", -6);
    double medAngle =
            Preferences.getInstance().getDouble("ArmMedAngle", 35);
    double highAngle =
            Preferences.getInstance().getDouble("ArmHighAngle", 60);

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

        butHighAngle.whenPressed(new SetArmAngle(highAngle));
        butMedAngle.whenPressed(new SetArmAngle(medAngle));
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
            arm.setPower(arm.getMaxPower()*firstJoystick.getY());
        }
        
        if(secondJoystick.getRawButton(BUT_SPIN_FLYWHEEL)) {
            setOverrideDriver(true);
        } else {
            setOverrideDriver(false);
        }
        
        if(isOverrideDriver()) {
            RobotCommon.runningRobot.driveTrain.setPower(Vectors.fromJoystick(firstJoystick, false));
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
     * 
     * @return Weather CoDriver is overriding Driver control.
     */
    public static boolean isOverrideDriver() {
        return overrideDriver;
    }
    
    /**
     * 
     * @param od weather to override driver or not.
     */
    public static void setOverrideDriver(boolean od) {
        overrideDriver = od;
    }

}
