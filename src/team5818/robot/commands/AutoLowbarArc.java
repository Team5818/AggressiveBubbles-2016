package team5818.robot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.modules.ComputerVision;
import team5818.robot.modules.FlyWheel;
import team5818.robot.modules.drivetrain.DriveTrain;
import team5818.robot.util.LinearLookupTable;

public class AutoLowbarArc extends CommandGroup{

    public double collectAngle =
            Preferences.getInstance().getDouble("ArmAngleCollect", 1.5);
    public double flyUpVel = FlyWheel.SHOOT_VELOCITY_UPPER;
    public double flyLoVel = FlyWheel.SHOOT_VELOCITY_LOWER;
    double xOffset = Preferences.getInstance().getDouble("AutoLowbarXOffset", 0);
    double yOffset = Preferences.getInstance().getDouble("AutoLowbarYOffset", -8);
    double shootAngle = Preferences.getInstance().getDouble("ArmAngleShooting", 40);
    
    public double[] leftVels = {};
    public double[] rightVels = {};
    public double[] dists = {};
    LinearLookupTable leftTable = new LinearLookupTable(leftVels, dists);
    LinearLookupTable rightTable = new LinearLookupTable(rightVels, dists);
    
    private SetArmAngle armToCollect = new SetArmAngle(collectAngle);
    private CommandGroup driveUnder = new CommandGroup();
    private ArmPower armToGround = new ArmPower(LowerArmToGround.ARM_POWER); 
    private DriveVelocityProfile arcToShot =
            new DriveVelocityProfile(leftTable, rightTable, 200);
    private CommandGroup prepareShot;
    private SpinRobot spin = new SpinRobot(-180, 2, 0.5);
    private SetArmAngle findTarget = new SetArmAngle(shootAngle, 2);;
    private SetFlywheelVelocity setFlyVel = new SetFlywheelVelocity(FlyWheel.SHOOT_VELOCITY_UPPER, FlyWheel.SHOOT_VELOCITY_LOWER, 0);
    private LEDToggle lightUp = new LEDToggle(true);
    private SwitchFeed switchCam = new SwitchFeed(ComputerVision.CAMERA_SHOOTER);
    private AutoAim autoAim = new AutoAim(0,1, 15);;
    private Shoot dontMiss = new Shoot();

    /**
     * move arm down
     * pass under lowbar
     * spin clockwise 10 deg
     * shoot
     * spin counter clockwise
     * move back under
     */
    public AutoLowbarArc(){

        
        driveUnder.addParallel(armToGround);
        driveUnder.addParallel(arcToShot);
        
        prepareShot.addParallel(setFlyVel);
        prepareShot.addParallel(spin);
        prepareShot.addParallel(findTarget);        
        
        this.addSequential(lightUp);
        this.addSequential(switchCam);
        this.addSequential(armToCollect);
        this.addSequential(driveUnder);
        this.addSequential(prepareShot);
        this.addSequential(autoAim);
        this.addSequential(dontMiss);

    }
}
