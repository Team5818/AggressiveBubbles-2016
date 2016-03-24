package team5818.robot.commands;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.Field;
import team5818.robot.modules.ComputerVision;
import team5818.robot.modules.FlyWheel;

/**
 * @author Petey
 * basic auto routine for portcullis
 *
 */

public class AutoRampartsUniversal extends CommandGroup{
    double  defenseWidth = 48;
    private double robotVel = 70;
    private double redirectDist;
    private double redirectAngle;
    private double redirectTimeout = 3;
    public double defenseDist = 80;
    public double defenseLength = 40;
    public double flyUpVel =
            Preferences.getInstance().getDouble("UpperFlyVel", FlyWheel.SHOOT_VELOCITY_UPPER);
    public double flyLoVel =
            Preferences.getInstance().getDouble("LowerFlyVel", FlyWheel.SHOOT_VELOCITY_LOWER);
    
    private SwitchFeed switchCam = new SwitchFeed(ComputerVision.CAMERA_SHOOTER);
    private LEDToggle lightUp = new LEDToggle(true);
    private DriveDistanceCommand driveToRamparts =
            new DriveDistanceCommand(defenseDist, .3, 5);
    private LowerArmToGround armToGround = new LowerArmToGround();
    private DriveVelocityCommand driveOverRamparts = new DriveVelocityCommand(robotVel, defenseLength);
    private SpinRobot redirect;
    private DriveDistanceCommand driveDiagonal;
    private SetArmAngle findTarget = new SetArmAngle(40);
    private SpinRobot spin;
    private SetFlywheelVelocity setFlyVel = new SetFlywheelVelocity(flyUpVel, flyLoVel);
    private AutoAim aim = new AutoAim();
    private Shoot dontMiss = new Shoot();

    /**
     * move arm down
     * pass under lowbar
     * spin clockwise 10 deg
     * shoot
     * spin counter clockwise
     * move back under
     */
    public AutoRampartsUniversal(int position) {
        if(position == 2 || position == 5){
            redirectAngle = -39;
        }
        else if(position == 3){
            redirectAngle = 39;
        }
        
        else{
            redirectAngle = 0;
            redirectTimeout = 0;
        }
        
        redirectDist = Math.sqrt(Math.pow(defenseWidth,2) + Math.pow(60, 2));
        
        redirect = new SpinRobot(redirectAngle, redirectTimeout);
        driveDiagonal = new DriveDistanceCommand(redirectDist);
        spin = new SpinRobot(-redirectAngle);
        

        this.addSequential(lightUp);
        this.addSequential(switchCam);
        this.addSequential(driveToRamparts);
        this.addSequential(armToGround);
        this.addSequential(driveOverRamparts);
        this.addSequential(redirect);
        this.addSequential(driveDiagonal);
        this.addSequential(findTarget);
        this.addSequential(spin);
        this.addSequential(setFlyVel);
        this.addSequential(aim);
        this.addSequential(dontMiss);

    }


}