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
            Preferences.getInstance().getDouble("ArmAngleCollect", 1.5);
    public double flyUpVel =
            Preferences.getInstance().getDouble("UpperFlyVel", FlyWheel.SHOOT_VELOCITY_UPPER);
    public double flyLoVel =
            Preferences.getInstance().getDouble("LowerFlyVel", FlyWheel.SHOOT_VELOCITY_LOWER);
    public double lowbarDist = 210;

    private SetArmAngle armToCollect = new SetArmAngle(collectAngle);
    private CommandGroup driveUnder = new CommandGroup();
    private ArmPower armToGround = new ArmPower(LowerArmToGround.ARM_POWER); 
    private DriveDistanceCommand goUnderLowbar =
            new DriveDistanceCommand(lowbarDist, .6, 5);
    private SpinRobot spin = new SpinRobot(-120, 2, 0.5);
    private SetArmAngle findTarget = new SetArmAngle(25);
    private SetFlywheelVelocity setFlyVel = new SetFlywheelVelocity(flyUpVel, flyLoVel, 0);
    private LEDToggle lightUp = new LEDToggle(true);
    private SwitchFeed switchCam = new SwitchFeed(ComputerVision.CAMERA_SHOOTER);
    private AutoAim autoAim = new AutoAim(-8,3);
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
        driveUnder.addParallel(armToGround);
        driveUnder.addParallel(goUnderLowbar);
        
        this.addSequential(lightUp);
        this.addSequential(switchCam);
        this.addSequential(armToCollect);
        this.addSequential(driveUnder);
        this.addSequential(findTarget);
        this.addSequential(spin);
        this.addSequential(setFlyVel);
        this.addSequential(new DriveDistanceCommand(-57, .6, 5));
        this.addSequential(autoAim);
        this.addSequential(dontMiss);

    }

}