package team5818.robot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.modules.ComputerVision;
import team5818.robot.modules.FlyWheel;
import team5818.robot.util.LinearLookupTable;

public class AutoChavalArc extends CommandGroup{
    private double findTargetAngle;
    public double driveOverAngle =
            Preferences.getInstance().getDouble("ArmAngleMid", 1.5);
    public double collectAngle =
            Preferences.getInstance().getDouble("ArmAngleCollect", 1.5);
    public double flyUpVel = FlyWheel.SHOOT_VELOCITY_UPPER;
    public double flyLoVel = FlyWheel.SHOOT_VELOCITY_LOWER;
    double xOffset = Preferences.getInstance().getDouble("AutoLowbarXOffset", 0);
    double yOffset = Preferences.getInstance().getDouble("AutoLowbarYOffset", -8);
    double shootAngle = Preferences.getInstance().getDouble("ArmAngleShooting", 40);
    double spinAngle;
    double dist;
    double[] powers = {18,32,32,18};
    double[] distances = {0,24,48,72};
    LinearLookupTable driveStraightTable = new LinearLookupTable(distances, powers);
    
    LinearLookupTable leftTable;
    LinearLookupTable rightTable;
    
    private SwitchFeed switchCam = new SwitchFeed(ComputerVision.CAMERA_SHOOTER);
    private LEDToggle lightUp = new LEDToggle(true);
    
    private SetArmAngle armToCrossAngle = new SetArmAngle(driveOverAngle);
    private DriveVelocityProfile driveToDefense = new DriveVelocityProfile(driveStraightTable, 72);
    
    private LowerArmToGround lowerChaval = new LowerArmToGround(); 

    private CommandGroup driveOverChaval = new CommandGroup();
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
            double[] leftVels = {24,50,50,60,24};
            double[] rightVels = {24,50,50,10,24};
            double[] dists = {0,24,120,140,200};
            dist = 200;
            leftTable = new LinearLookupTable(dists, leftVels);
            rightTable = new LinearLookupTable(dists, rightVels);
            spinAngle = -110;
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis2XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis2YOffset", yOffset);
        }
        else if(position == 3){
            double[] leftVels = {24,70,70,24};
            double[] rightVels = {24,70,70,24};
            double[] dists = {0,24,65,100};
            dist = 100;
            leftTable = new LinearLookupTable(dists, leftVels);
            rightTable = new LinearLookupTable(dists, rightVels);
            spinAngle = 180;
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis3XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis3YOffset", yOffset);
        }
        
        else if(position==4){
            double[] leftVels = {24,70,70,24};
            double[] rightVels = {24,70,70,24};
            double[] dists = {0,24,65,100};
            dist = 100;
            leftTable = new LinearLookupTable(dists, leftVels);
            rightTable = new LinearLookupTable(dists, rightVels);
            spinAngle = 180;
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis4XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis4YOffset", yOffset);
        }
        else{
            double[] leftVels = {24,70,70,24};
            double[] rightVels = {24,70,70,24};
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
        spin = new SpinRobot(spinAngle);
        
        driveOverChaval.addParallel(armBackUp);
        driveOverChaval.addParallel(driveOver);
        
        prepareShot.addParallel(setFlyVel);
        prepareShot.addParallel(spin);
        prepareShot.addParallel(findTarget); 
        
        this.addSequential(lightUp);
        this.addSequential(switchCam); 
        this.addSequential(armToCrossAngle);
        this.addSequential(driveToDefense);
        this.addSequential(lowerChaval);
        this.addSequential(driveOverChaval);
        this.addSequential(prepareShot);
        this.addSequential(aim);
        this.addSequential(dontMiss);

    }
}
