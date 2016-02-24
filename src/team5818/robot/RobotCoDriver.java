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
import team5818.robot.modules.ComputerVision;
import team5818.robot.modules.FlyWheel;
import team5818.robot.modules.Module;
import team5818.robot.util.Vectors;

/**
 * The secondary robot driver. Responsible for the arm.
 */
public class RobotCoDriver implements Module {
    
    private static boolean overrideDriver = false;
    
    public static final Joystick firstJoystick =
            new Joystick(RobotConstants.CODRIVER_FIRST_JOYSTICK_PORT);
    public static final Joystick secondJoystick =
            new Joystick(RobotConstants.CODRIVER_SECOND_JOYSTICK_PORT);
    //Arm 1 Arm 2
    // 435 | 5  6
    //  2  |  34 
    private static final int BUT_PRINT_ANGLE = 7;
    private static final int BUT_ARM_ANGLE_HOME = 5;
    private static final int BUT_ARM_ANGLE_ZERO = 4;
    private static final int BUT_SHOOT_ANGLE_HIGH = 3;
    private static final int BUT_SHOOT_ANGLE_MED_HIGH= 4;
    private static final int BUT_SHOOT_ANGLE_MED_LOW = 3;
    private static final int BUT_SHOOT_ANGLE_LOW = 4;
    private static final int BUT_ARM_SET_POWER = 2;
    private static final int BUT_SPIN_FLYWHEEL = 2;
    private static final int BUT_COLLECT = 1;
    
    private double shootAngleHigh = 60;
    private double shootAngleMedHigh = 50;
    private double shootAngleMedLow = 40;
    private double shootAngleLow = 30;
    private double armAngleZero = 0;
    private double armAngleHome = 85;
    
    JoystickButton butSetPower = new JoystickButton(firstJoystick, BUT_ARM_SET_POWER);
    JoystickButton butSpinFlywheel = new JoystickButton(secondJoystick, BUT_SPIN_FLYWHEEL);
    JoystickButton butCollect = new JoystickButton(secondJoystick, BUT_COLLECT);
    JoystickButton butShootAngleLow = new JoystickButton(secondJoystick, BUT_SHOOT_ANGLE_LOW);
    JoystickButton butShootAngleMedLow = new JoystickButton(secondJoystick, BUT_SHOOT_ANGLE_MED_LOW);
    JoystickButton butShootAngleMedHigh = new JoystickButton(secondJoystick, BUT_SHOOT_ANGLE_MED_HIGH);
    JoystickButton butShootAngleHigh = new JoystickButton(secondJoystick, BUT_SHOOT_ANGLE_HIGH);
    JoystickButton butArmAngleHome = new JoystickButton(firstJoystick, BUT_ARM_ANGLE_HOME);
    JoystickButton butArmAngleZero = new JoystickButton(firstJoystick, BUT_ARM_ANGLE_ZERO);
    
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
        
        shootAngleHigh = Preferences.getInstance().getDouble("ShootAngleHigh", shootAngleHigh);
        shootAngleMedHigh = Preferences.getInstance().getDouble("ShootAngleMedHigh", shootAngleMedHigh);
        shootAngleMedLow = Preferences.getInstance().getDouble("ShootAngleMedLow", shootAngleMedLow);
        shootAngleLow = Preferences.getInstance().getDouble("ShootAngleLow", shootAngleLow);
        armAngleHome = Preferences.getInstance().getDouble("ArmAngleHome", armAngleHome);
        armAngleZero = Preferences.getInstance().getDouble("ArmAngleZero", armAngleZero);

        butSetPower.whenPressed(new SetArmPower(0));
        butSpinFlywheel.whenPressed(new SetFlywheelVelocity(FlyWheel.SHOOT_VELOCITY_UPPER, FlyWheel.SHOOT_VELOCITY_LOWER));
        butSpinFlywheel.whenReleased(new SetFlywheelPower(0));
        butShootAngleHigh.whenPressed(new SetArmAngle(shootAngleHigh));
        butShootAngleMedHigh.whenPressed(new SetArmAngle(shootAngleMedHigh));
        butShootAngleMedLow.whenPressed(new SetArmAngle(shootAngleMedLow));
        butShootAngleLow.whenPressed(new SetArmAngle(shootAngleLow));
        butArmAngleHome.whenPressed(new SetArmAngle(armAngleHome));
        butArmAngleZero.whenPressed(new SetArmAngle(armAngleZero));
    }

    @Override
    public void teleopPeriodicModule() {
        if (firstJoystick.getRawButton(BUT_PRINT_ANGLE)) {
            SmartDashboard.putNumber("Arm Angle = ", arm.getAngle());
            SmartDashboard.putNumber("Upper flywheel", upperFlywheel.getRPS());
            SmartDashboard.putNumber("Lower flywheel", lowerFlywheel.getRPS());
        }
        
        if (!arm.getPIDMode()) {
            arm.setPower(secondJoystick.getY());
        }
        
        if(secondJoystick.getRawButton(BUT_SPIN_FLYWHEEL)) {
            
            setOverrideDriver(true);
            RobotCommon.runningRobot.vision.See.ChangeFeed(ComputerVision.CAMERA_SHOOTER);
            
        } else {
            setOverrideDriver(false);
            RobotCommon.runningRobot.vision.See.ChangeFeed(ComputerVision.CAMERA_DRIVER);
            
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
