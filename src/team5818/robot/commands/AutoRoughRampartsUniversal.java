package team5818.robot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.modules.ComputerVision;
import team5818.robot.modules.FlyWheel;

/**
 * 
 * @author Petey
 * basic auto routine, uses velocity control to go over ramparts, rough terain, moat
 * 
 *
 */
public class AutoRoughRampartsUniversal extends CommandGroup{
    double  defenseWidth = 48;
    private double redirectDist;
    private double redirectAngle;
    double defenseDist = 120;
    double armAngle = Preferences.getInstance().getDouble("ArmAngleLow", 20.0);
    private double redirectTimeout = 3;
    public double flyUpVel =
            Preferences.getInstance().getDouble("UpperFlyVel", FlyWheel.SHOOT_VELOCITY_UPPER);
    public double flyLoVel =
            Preferences.getInstance().getDouble("LowerFlyVel", FlyWheel.SHOOT_VELOCITY_LOWER);

    
    private CommandGroup driveOver = new CommandGroup();
    private SetArmAngle lowerArm = new SetArmAngle(armAngle);
    private DriveVelocityCommand driveVel = new DriveVelocityCommand(-50,  -defenseDist);
    private SpinRobot redirect;
    private DriveDistanceCommand driveDiagonal;
    private SetArmAngle findTarget = new SetArmAngle(30);
    private SpinRobot spin;
    private AutoAim aim = new AutoAim();
    private SetFlywheelVelocity setFlyVel = new SetFlywheelVelocity(flyUpVel, flyLoVel);
    private Shoot shoot = new Shoot();
    private SwitchFeed switchCam = new SwitchFeed(ComputerVision.CAMERA_SHOOTER);
    private LEDToggle lightUp = new LEDToggle(true);

    
    public AutoRoughRampartsUniversal(int position){
        
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
        
        driveOver.addParallel(lowerArm);
        driveOver.addParallel(driveVel);
        
        this.addSequential(lightUp);
        this.addSequential(switchCam);
        this.addSequential(driveOver);
        this.addSequential(findTarget);
        this.addSequential(spin);
        this.addSequential(setFlyVel);
        this.addSequential(aim);
        this.addSequential(shoot);
        
    }
}