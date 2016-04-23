package team5818.robot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.modules.ComputerVision;
import team5818.robot.modules.FlyWheel;
import team5818.robot.util.LinearLookupTable;

public class AutoRoughTerrainArc extends CommandGroup{


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
    private SetArmAngle armToPosition = new SetArmAngle(crossingAngle);
    private DriveVelocityProfile driveOver;  
    private CommandGroup prepareShot = new CommandGroup();
    private SetFlywheelVelocity setFlyVel = new SetFlywheelVelocity(flyUpVel, flyLoVel);
    private SpinRobot spin;
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
    public AutoRoughTerrainArc(int position) {
        double xOffset = 0;
        double yOffset = 0;
        if(position == 2){
            double[] leftVels = {-24,-50,-50,-24};
            double[] rightVels = {-24,-50,-50,-24};
            double[] dists = {0,24,110,160};
            dist = -160;
            leftTable = new LinearLookupTable(dists, leftVels);
            rightTable = new LinearLookupTable(dists, rightVels);
            spinAngle = 90;
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis2XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis2YOffset", yOffset);
            cw = true;
        }
        else if(position == 3){
            double[] leftVels = {-24,-50,-50,-24};
            double[] rightVels = {-24,-50,-50,-24};
            double[] dists = {0,24,90,140};
            dist = -140;
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
            double[] dists = {0,24,90,140};
            dist = -140;
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
            double[] dists = {0,24,90,140};
            dist = -140;
            leftTable = new LinearLookupTable(dists, leftVels);
            rightTable = new LinearLookupTable(dists, rightVels);
            spinAngle = 60;
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis5XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis5YOffsets", yOffset);
            cw = false;
        }
        
        driveOver = new DriveVelocityProfile(leftTable, rightTable, dist);
        spin = new SpinRobot(spinAngle);
        AutoAim autoAim = new AutoAim(xOffset,yOffset, 15);
        scan = new ScanForTarget(cw);
        
        prepareShot.addParallel(setFlyVel);
        prepareShot.addParallel(scan);
        
        this.addSequential(lightUp);
        this.addSequential(switchCam); 
        this.addSequential(armToPosition);
        this.addSequential(driveOver);
        this.addSequential(prepareShot);
        this.addSequential(autoAim);
        this.addSequential(dontMiss);

    }
}


