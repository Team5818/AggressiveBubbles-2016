package team5818.robot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.modules.ComputerVision;
import team5818.robot.modules.FlyWheel;
import team5818.robot.util.LinearLookupTable;

public class AutoMoatArc extends CommandGroup{
    private double findTargetAngle;
    public double crossingAngle =
            Preferences.getInstance().getDouble("ArmAngleMid", 30);
    public double flyUpVel = FlyWheel.SHOOT_VELOCITY_UPPER;
    public double flyLoVel = FlyWheel.SHOOT_VELOCITY_LOWER;
    double xOffset = Preferences.getInstance().getDouble("AutoLowbarXOffset", 0);
    double yOffset = Preferences.getInstance().getDouble("AutoLowbarYOffset", -8);
    double shootAngle = Preferences.getInstance().getDouble("ArmAngleShooting", 40);
    double spinAngle;
    double dist;
    boolean cw;
    
    LinearLookupTable leftTable;
    LinearLookupTable rightTable;
    
    private SwitchFeed switchCam = new SwitchFeed(ComputerVision.CAMERA_SHOOTER);
    private LEDToggle lightUp = new LEDToggle(true);
    private CommandGroup driveOverMoat = new CommandGroup();
    private SetArmAngle armToPosition = new SetArmAngle(crossingAngle);
    private DriveVelocityProfile driveOver;  
    private DoNothing pause = new DoNothing(1);
    private CommandGroup prepareShot = new CommandGroup();
    private SetFlywheelVelocity setFlyVel = new SetFlywheelVelocity(flyUpVel, flyLoVel);
    private ScanForTarget scan;
    private Shoot dontMiss = new Shoot();

    /**
     * move arm down
     * pass under lowbar
     * spin clockwise 10 deg
     * shoot
     * spin counter clockwise
     * move back under
     */
    public AutoMoatArc(int position){
        double xOffset = 0;
        double yOffset = 0;
        if(position == 2){
            double[] leftVels = {-24,-50,-50,-24};
            double[] rightVels = {-24,-50,-50,-24};
            double[] dists = {0,24,120,250};
            dist = -250;
            leftTable = new LinearLookupTable(dists, leftVels);
            rightTable = new LinearLookupTable(dists, rightVels);
            spinAngle = 90;
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis2XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis2YOffset", yOffset);
            cw = true;
        }
        else if(position == 3){
            double[] leftVels = {-45,-50,-50,-24};
            double[] rightVels = {-45,-50,-50,-24};
            double[] dists = {0,24,100,190};
            dist = -190;
            leftTable = new LinearLookupTable(dists, leftVels);
            rightTable = new LinearLookupTable(dists, rightVels);
            spinAngle = 60;
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis3XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis3YOffset", yOffset);
            cw = true;
        }
        
        else if(position==4){
            double[] leftVels = {-24,-50,-50,-24};
            double[] rightVels = {-24,-50,-50,-24};
            double[] dists = {0,24,100,190};
            dist = -190;
            leftTable = new LinearLookupTable(dists, leftVels);
            rightTable = new LinearLookupTable(dists, rightVels);
            spinAngle = 60;
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis4XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis4YOffset", yOffset);
            cw = false;
        }
        else{
            double[] leftVels = {-24,-50,-50,-24};
            double[] rightVels = {-24,-50,-50,-24};
            double[] dists = {0,24,100,190};
            dist = -190;
            leftTable = new LinearLookupTable(dists, leftVels);
            rightTable = new LinearLookupTable(dists, rightVels);
            spinAngle = 60;
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis5XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis5YOffsets", yOffset);
            cw = false;
        }
        
        driveOver = new DriveVelocityProfile(leftTable, rightTable, dist);
        AutoAim autoAim = new AutoAim(xOffset,yOffset, 3);
        scan = new ScanForTarget(cw);
        
        driveOverMoat.addParallel(armToPosition);
        driveOverMoat.addParallel(driveOver);
        
        prepareShot.addParallel(scan);
        prepareShot.addParallel(setFlyVel);
        
        
        this.addSequential(lightUp);
        this.addSequential(switchCam); 
        this.addSequential(driveOverMoat);
        this.addSequential(pause);
        this.addSequential(prepareShot);
        this.addSequential(autoAim);
        this.addSequential(dontMiss);

    }

}
