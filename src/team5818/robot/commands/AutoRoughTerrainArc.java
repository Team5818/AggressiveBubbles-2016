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
    
    LinearLookupTable leftTable;
    LinearLookupTable rightTable;
    
    private SwitchFeed switchCam = new SwitchFeed(ComputerVision.CAMERA_SHOOTER);
    private LEDToggle lightUp = new LEDToggle(true);
    private SetArmAngle armToPosition = new SetArmAngle(crossingAngle);
    private DriveVelocityProfile driveOver;  
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
    public AutoRoughTerrainArc(int position) {
        double xOffset = 0;
        double yOffset = 0;
        if(position == 2){
            double[] leftVels = {-24,-50,-50,-10,-24};
            double[] rightVels = {-24,-50,-50,-60,-24};
            double[] dists = {0,-24,-120,-140,-200};
            dist = -200;
            leftTable = new LinearLookupTable(dists, leftVels);
            rightTable = new LinearLookupTable(dists, rightVels);
            spinAngle = 90;
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis2XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis2YOffset", yOffset);
        }
        else if(position == 3){
            double[] leftVels = {-24,-50,-50,-25,24};
            double[] rightVels = {-24,-50,-50,-50,24};
            double[] dists = {0,-24,-120,-150,-160};
            dist = -160;
            leftTable = new LinearLookupTable(dists, leftVels);
            rightTable = new LinearLookupTable(dists, rightVels);
            spinAngle = (-90);
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis3XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis3YOffset", yOffset);
        }
        
        else if(position==4){
            double[] leftVels = {-24,-50,-50,-24};
            double[] rightVels = {-24,-50,-50,-24};
            double[] dists = {0,-24,-120,-140};
            dist = -140;
            leftTable = new LinearLookupTable(dists, leftVels);
            rightTable = new LinearLookupTable(dists, rightVels);
            spinAngle = (0);
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis4XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis4YOffset", yOffset);
        }
        else{
            double[] leftVels = {-24,-50,-50,-50,-24};
            double[] rightVels = {-24,-50,-50,-25,-24};
            double[] dists = {0,-24,-120,-150,-160};
            dist = -160;
            leftTable = new LinearLookupTable(dists, leftVels);
            rightTable = new LinearLookupTable(dists, rightVels);
            spinAngle = 90;
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis5XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis5YOffset", yOffset);
        }
        
        driveOver = new DriveVelocityProfile(leftTable, rightTable, dist);
        aim = new AutoAim(xOffset, yOffset, 3);
        spin = new SpinRobot(spinAngle);
         
        
        prepareShot.addParallel(setFlyVel);
        prepareShot.addParallel(spin);
        
        this.addSequential(lightUp);
        this.addSequential(switchCam); 
        this.addSequential(armToPosition);
        this.addSequential(driveOver);
        this.addSequential(prepareShot);
        this.addSequential(aim);
        this.addSequential(dontMiss);

    }
}


