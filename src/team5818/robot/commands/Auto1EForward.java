package team5818.robot.commands;


import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.Field;
import team5818.robot.modules.ComputerVision;
import team5818.robot.modules.FlyWheel;

/**
 * @author Petey
 * basic auto routine, goes forward through lowbar and shoots 
 *
 */
public class Auto1EForward extends CommandGroup {

    public double collectAngle =
            Preferences.getInstance().getDouble("ArmAngleZero", 1.5);
    public double shootAngle =
            Preferences.getInstance().getDouble("ArmShootHigh", 40.0);
    public double flyUpVel =
            Preferences.getInstance().getDouble("UpperFlyVel", FlyWheel.SHOOT_VELOCITY_UPPER);
    public double flyLoVel =
            Preferences.getInstance().getDouble("LowerFlyVel", FlyWheel.SHOOT_VELOCITY_LOWER);
    public double lowbarDist = 180;

    private CommandGroup driveOver = new CommandGroup();
    private SetArmAngle putArmDown = new SetArmAngle(collectAngle);
    private DriveDistanceCommand goUnderLowbar =
            new DriveDistanceCommand(lowbarDist, .6, 5);
    private SpinRobot spin = new SpinRobot(-140, 2, 0.5);
    private SetArmAngle findTarget = new SetArmAngle(40);
    private SetFlywheelVelocity setFlyVel = new SetFlywheelVelocity(flyUpVel, flyLoVel, 0);
    private LEDToggle lightUp = new LEDToggle(true);
    private SwitchFeed switchCam = new SwitchFeed(ComputerVision.CAMERA_SHOOTER);
    private LowerArmToGround lowerArm = new LowerArmToGround();
    private AutoAim autoAim = new AutoAim();
    private Shoot dontMiss = new Shoot();

    /**
     * move arm down
     * pass under lowbar
     * spin clockwise 10 deg
     * shoot
     * spin counter clockwise
     * move back under
     */
    public Auto1EForward() {
        driveOver.addParallel(putArmDown);
        driveOver.addParallel(goUnderLowbar);
        
        this.addSequential(lightUp);
        this.addSequential(switchCam);
        this.addSequential(driveOver);
        this.addSequential(setFlyVel);
        this.addSequential(findTarget);
        this.addSequential(spin);
        this.addSequential(autoAim);
        this.addSequential(dontMiss);

    }

}