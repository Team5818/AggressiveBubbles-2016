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
    
    public double[] leftVels = {24,50,50,50,24};
    public double[] rightVels = {24,50,50,35,24};
    public double[] dists = {0,24,120,168,200};
    LinearLookupTable leftTable = new LinearLookupTable(dists, leftVels);
    LinearLookupTable rightTable = new LinearLookupTable(dists, rightVels);
    
    private SetArmAngle armToCollect = new SetArmAngle(collectAngle);
    private CommandGroup driveUnder = new CommandGroup();
    private ArmPower armToGround = new ArmPower(LowerArmToGround.ARM_POWER); 
    private DriveVelocityProfile arcToShot =
            new DriveVelocityProfile(leftTable, rightTable, 200);
    private CommandGroup prepareShot = new CommandGroup();
    private SpinRobot spin = new SpinRobot(-180, 2, 0.5);
    private SetArmAngle findTarget = new SetArmAngle(shootAngle, 2);
    private SetFlywheelVelocity setFlyVel = new SetFlywheelVelocity(FlyWheel.SHOOT_VELOCITY_UPPER, FlyWheel.SHOOT_VELOCITY_LOWER, 0);
    private LEDToggle lightUp = new LEDToggle(true);
    private SwitchFeed switchCam = new SwitchFeed(ComputerVision.CAMERA_SHOOTER);
    private AutoAim autoAim = new AutoAim(0,0, 15);;
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
