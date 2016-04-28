package team5818.robot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.modules.ComputerVision;
import team5818.robot.modules.FlyWheel;
import team5818.robot.util.LinearLookupTable;

public class AutoRampartsArc extends CommandGroup{
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
    private CommandGroup driveOverRamparts = new CommandGroup();
    private SetArmAngle armToPosition = new SetArmAngle(crossingAngle);
    private DriveVelocityProfile driveOver;  
    private CommandGroup prepareShot = new CommandGroup();
    private SetFlywheelVelocity setFlyVel = new SetFlywheelVelocity(flyUpVel, flyLoVel);
    private SpinRobot spin;
    private ScanForTarget scan;
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
    public AutoRampartsArc(int position) {
        double xOffset = 0;
        double yOffset = 0;
        if(position == 2){
            double[] leftVels = {-24,-85,-85,-24};
            double[] rightVels = {-24,-70,-70,-24};
            double[] dists = {0,24,110,240};
            dist = -240;
            leftTable = new LinearLookupTable(dists, leftVels);
            rightTable = new LinearLookupTable(dists, rightVels);
            spinAngle = 90;
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis2XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis2YOffset", yOffset);
            cw = true;
        }
        else if(position == 3){
            double[] leftVels = {-24,-85,-85,-24};
            double[] rightVels = {-24,-70,-70,-24};
            double[] dists = {0,24,90,185};
            dist = -185;
            leftTable = new LinearLookupTable(dists, leftVels);
            rightTable = new LinearLookupTable(dists, rightVels);
            spinAngle = (40);
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis3XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis3YOffset", yOffset);
            cw = true;
        }
        
        else if(position==4){
            double[] leftVels = {-24,-70,-70,-24};
            double[] rightVels = {-24,-70,-70,-24};
            double[] dists = {0,24,90,185};
            dist = -185;
            leftTable = new LinearLookupTable(dists, leftVels);
            rightTable = new LinearLookupTable(dists, rightVels);
            spinAngle = (20);
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis4XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis4YOffset", yOffset);
            cw = false;
        }
        else{
            double[] leftVels = {-24,-70,-70,-24};
            double[] rightVels = {-24,-70,-70,-24};
            double[] dists = {0,24,90,185};
            dist = -185;
            leftTable = new LinearLookupTable(dists, leftVels);
            rightTable = new LinearLookupTable(dists, rightVels);
            spinAngle = 0;
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis5XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis5YOffset", yOffset);
            cw = false;
        }
        
        driveOver = new DriveVelocityProfile(leftTable, rightTable, dist);
        aim = new AutoAim(xOffset, yOffset, 3);
        spin = new SpinRobot(spinAngle);
        scan = new ScanForTarget(cw);
        
        driveOverRamparts.addParallel(armToPosition);
        driveOverRamparts.addParallel(driveOver);
        
        prepareShot.addParallel(setFlyVel);
        prepareShot.addParallel(scan);
        
        this.addSequential(lightUp);
        this.addSequential(switchCam); 
        this.addSequential(driveOverRamparts);
        this.addSequential(prepareShot);
        this.addSequential(aim);
        this.addSequential(dontMiss);

    }
}
