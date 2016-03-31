package team5818.robot.commands;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.Field;
import team5818.robot.modules.ComputerVision;
import team5818.robot.modules.FlyWheel;

/**
 * @author Petey
 * basic auto routine for portcullis drives forward
 *
 */

public class AutoPortcullisUniversal extends CommandGroup{
    double  defenseWidth = 48;
    private double xOffSet = 0;
    private double redirectDist;
    private double redirectAngle;
    private double findTargetAngle;
    private double redirectTimeout = 3;
    public double defenseDist = 115;
    public double collectAngle =
            Preferences.getInstance().getDouble("ArmAngleCollect", 1.5);
    public double flyUpVel =
            Preferences.getInstance().getDouble("UpperFlyVel", FlyWheel.SHOOT_VELOCITY_UPPER);
    public double flyLoVel =
            Preferences.getInstance().getDouble("LowerFlyVel", FlyWheel.SHOOT_VELOCITY_LOWER);
    
    private SwitchFeed switchCam = new SwitchFeed(ComputerVision.CAMERA_SHOOTER);
    private LEDToggle lightUp = new LEDToggle(true);
    private SetArmAngle armToCollect = new SetArmAngle(collectAngle);
    private CommandGroup driveUnder = new CommandGroup();
    private ArmPower armToGround = new ArmPower(LowerArmToGround.ARM_POWER); 
    private DriveDistanceCommand driveToPortcullis =
            new DriveDistanceCommand(defenseDist, .6, 5);    
    private SpinRobot redirect;
    private DriveDistanceCommand driveDiagonal;
    private SetArmAngle findTarget = new SetArmAngle(25);
    private SpinRobot spin;
    private SetFlywheelVelocity setFlyVel = new SetFlywheelVelocity(flyUpVel, flyLoVel);
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
    public AutoPortcullisUniversal(int position) {
        setTimeout(15);
        double xOffset = Preferences.getInstance().getDouble("AutoPortcullisXOffset", 0);
        double yOffset = Preferences.getInstance().getDouble("AutoPortcullisYOffset", -8);
        if(position == 2){
            redirectAngle = -30;
            findTargetAngle = -130-redirectAngle;
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis2XOffset", 0);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis2YOffset", -8);
        }
        else if(position == 3){
            redirectAngle = 30;
            findTargetAngle = 180-redirectAngle;
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis3XOffset", 0);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis3YOffset", -8);
        }
        
        else if(position==4){
            redirectAngle = 0;
            redirectTimeout = 0;
            findTargetAngle = 180;
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis4XOffset", 0);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis4YOffset", -8);
        }
        else{
            redirectAngle = -30;
            findTargetAngle = -180-redirectAngle;  
            xOffset = Preferences.getInstance().getDouble("AutoPortcullis5XOffset", 0);
            yOffset = Preferences.getInstance().getDouble("AutoPortcullis5YOffset", -8);
        }
        aim = new AutoAim(xOffset, yOffset, 3);
        
        redirectDist = Math.sqrt(Math.pow(defenseWidth,2) + Math.pow(60, 2));
        
        redirect = new SpinRobot(redirectAngle, redirectTimeout);
        driveDiagonal = new DriveDistanceCommand(redirectDist);
        spin = new SpinRobot(findTargetAngle, 2, 0.5);
        
        driveUnder.addParallel(armToGround);
        driveUnder.addParallel(driveToPortcullis);
        
        CommandGroup armSpin = new CommandGroup();
        armSpin.addParallel(lightUp);
        armSpin.addParallel(switchCam);
        armSpin.addParallel(spin);
        armSpin.addParallel(findTarget);
        armSpin.addParallel(setFlyVel);
        
        
        this.addSequential(armToCollect);
        this.addSequential(driveUnder);
        this.addSequential(redirect);
        this.addSequential(driveDiagonal);
        this.addSequential(armSpin);
        this.addSequential(aim);
        this.addSequential(dontMiss);

    }


}
