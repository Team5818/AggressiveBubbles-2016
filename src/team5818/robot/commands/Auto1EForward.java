package team5818.robot.commands;


import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.Field;
import team5818.robot.modules.ComputerVision;
import team5818.robot.modules.FlyWheel;
import team5818.robot.modules.drivetrain.DriveTrain;

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

    private SetArmAngle armToCollect = new SetArmAngle(collectAngle);
    private CommandGroup driveUnder = new CommandGroup();
    private ArmPower armToGround = new ArmPower(LowerArmToGround.ARM_POWER); 
    private DriveDistanceCommand goUnderLowbar =
            new DriveDistanceCommand(130, .6, 5);
    private DriveVelocityCommand getCloseToGoal = new DriveVelocityCommand(DriveTrain.MAX_VELOCITY*0.48,DriveTrain.MAX_VELOCITY*0.52, 80);
    
    
    private SpinRobot spin = new SpinRobot(-195, 2, 0.5);
    private SetArmAngle findTarget;
    private SetFlywheelVelocity setFlyVel = new SetFlywheelVelocity(flyUpVel, flyLoVel, 0);
    private LEDToggle lightUp = new LEDToggle(true);
    private SwitchFeed switchCam = new SwitchFeed(ComputerVision.CAMERA_SHOOTER);
    private AutoAim autoAim;
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
        double xOffset = Preferences.getInstance().getDouble("AutoLowbarXOffset", 0);
        double yOffset = Preferences.getInstance().getDouble("AutoLowbarYOffset", -8);
        double shootAngle = Preferences.getInstance().getDouble("ArmAngleShooting", 40);
        findTarget = new SetArmAngle(shootAngle);
        autoAim = new AutoAim(0, 0, 3);
        //lowbarDist = Preferences.getInstance().getDouble("AutoLowbarDriveDist", lowbarDist);
        driveUnder.addParallel(armToGround);
        driveUnder.addParallel(goUnderLowbar);
        
        CommandGroup lineUp = new CommandGroup();
        lineUp.addParallel(spin);
        lineUp.addParallel(findTarget);
        lineUp.addParallel(setFlyVel);
        
        this.addSequential(lightUp);
        this.addSequential(switchCam);
        this.addSequential(armToCollect);
        this.addSequential(driveUnder);
        this.addSequential(new SpinRobot(60));
        this.addSequential(new DriveDistanceCommand(60));
        this.addSequential(lineUp);
        this.addSequential(autoAim);
        this.addSequential(dontMiss);

    }

}