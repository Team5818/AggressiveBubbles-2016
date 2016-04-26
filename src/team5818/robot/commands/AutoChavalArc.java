package team5818.robot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.modules.ComputerVision;
import team5818.robot.modules.FlyWheel;
import team5818.robot.util.LinearLookupTable;

public class AutoChavalArc extends CommandGroup{
    private double findTargetAngle;
    public double driveOverAngle = 18;
    public double collectAngle =
            Preferences.getInstance().getDouble("ArmAngleCollect", 1.5);
    public double flyUpVel = FlyWheel.SHOOT_VELOCITY_UPPER;
    public double flyLoVel = FlyWheel.SHOOT_VELOCITY_LOWER;
    double xOffset = Preferences.getInstance().getDouble("AutoLowbarXOffset", 0);
    double yOffset = Preferences.getInstance().getDouble("AutoLowbarYOffset", -8);
    double shootAngle = Preferences.getInstance().getDouble("ArmAngleShooting", 40);
    double spinAngle;
    double dist;
    double[] firstVels = {12,30,30,12};
    double[] distances = {0,9,27,36};
    LinearLookupTable driveStraightTable = new LinearLookupTable(distances, firstVels);
    
    LinearLookupTable leftTable;
    LinearLookupTable rightTable;
    
    private SwitchFeed switchCam = new SwitchFeed(ComputerVision.CAMERA_SHOOTER);
    private LEDToggle lightUp = new LEDToggle(true);
    
    private CommandGroup driveToChaval = new CommandGroup();
    private SetArmAngle armToCrossAngle = new SetArmAngle(driveOverAngle);
    private DriveVelocityProfile driveToDefense = new DriveVelocityProfile(driveStraightTable, 72);
    
    private ArmPower lowerChaval = new ArmPower(LowerArmToGround.ARM_POWER,1.3); 

    private CommandGroup driveOverChaval = new CommandGroup();
    private CommandGroup raiseArm = new CommandGroup();
    private DoNothing wait = new DoNothing(1);
    private SetArmAngle armBackUp = new SetArmAngle(driveOverAngle);
    private DriveVelocityProfile driveOver;  
    
    private SetArmAngle findTarget = new SetArmAngle(30);
    private CommandGroup prepareShot = new CommandGroup();
    private SetFlywheelVelocity setFlyVel = new SetFlywheelVelocity(flyUpVel, flyLoVel);
    private SpinRobot spin;
    private AutoAim aim;
    private Shoot dontMiss = new Shoot();

    /**
     * move arm down
     * pass under lowbar
     * spin clockwise 10 deg
     * shoot
     * spin counter clockwise
     * move back under
     */
    public AutoChavalArc(int position) {
        double xOffset = 0;
        double yOffset = 0;
        if(position == 2){
            double[] leftVels = {60,50,50,24};
            double[] rightVels = {60,50,50,24};
            double[] dists = {0,24,85,160};
            dist = 160;
            leftTable = new LinearLookupTable(dists, leftVels);
            rightTable = new LinearLookupTable(dists, rightVels);
            spinAngle = -110;
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis2XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis2YOffset", yOffset);
        }
        else if(position == 3){
            double[] leftVels = {60,50,50,24};
            double[] rightVels = {60,50,50,24};
            double[] dists = {0,24,65,100};
            dist = 100;
            leftTable = new LinearLookupTable(dists, leftVels);
            rightTable = new LinearLookupTable(dists, rightVels);
            spinAngle = 180;
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis3XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis3YOffset", yOffset);
        }
        
        else if(position==4){
            double[] leftVels = {60,50,50,24};
            double[] rightVels = {60,50,50,24};
            double[] dists = {0,24,65,100};
            dist = 100;
            leftTable = new LinearLookupTable(dists, leftVels);
            rightTable = new LinearLookupTable(dists, rightVels);
            spinAngle = 180;
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis4XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis4YOffset", yOffset);
        }
        else{
            double[] leftVels = {60,50,50,24};
            double[] rightVels = {60,50,50,24};
            double[] dists = {0,24,65,100};
            dist = 100;
            leftTable = new LinearLookupTable(dists, leftVels);
            rightTable = new LinearLookupTable(dists, rightVels);
            spinAngle = 1800;
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis5XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis5YOffset", yOffset);
        }
        
        driveOver = new DriveVelocityProfile(leftTable, rightTable, dist);
        aim = new AutoAim(xOffset, yOffset, 3);
        spin = new SpinRobot(spinAngle, 2, 0.5);

        
        driveToChaval.addParallel(armToCrossAngle,1.5);
        driveToChaval.addParallel(driveToDefense, 2);
        
        raiseArm.addSequential(wait);
        raiseArm.addSequential(armBackUp);
        
        driveOverChaval.addParallel(raiseArm, 3);
        driveOverChaval.addParallel(driveOver, 3.5);
        
        prepareShot.addParallel(setFlyVel);
        prepareShot.addParallel(spin);
        prepareShot.addParallel(findTarget); 
        
        this.addSequential(lightUp);
        this.addSequential(switchCam); 
        this.addSequential(driveToChaval);
        this.addSequential(lowerChaval);
        this.addSequential(driveOverChaval);
        this.addSequential(prepareShot);
        this.addSequential(aim);
        this.addSequential(dontMiss);

    }
}
