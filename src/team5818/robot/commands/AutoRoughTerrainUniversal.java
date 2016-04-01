package team5818.robot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.modules.ComputerVision;
import team5818.robot.modules.FlyWheel;
import team5818.robot.modules.drivetrain.DriveTrain;

/**
 * 
 * @author Petey
 * basic auto routine, uses velocity control to go over ramparts, rough terain, moat
 * 
 *
 */
public class AutoRoughTerrainUniversal extends CommandGroup{
    double  defenseWidth = 48;
    private double redirectDist;
    private double redirectAngle;
    private double findTargetAngle;
    double defenseDist = 160;
    double armAngle = Preferences.getInstance().getDouble("ArmAngleShooting", 30.0);
    private double redirectTimeout = 3;
    public double flyUpVel = FlyWheel.SHOOT_VELOCITY_UPPER;
    public double flyLoVel = FlyWheel.SHOOT_VELOCITY_LOWER;

    
    private CommandGroup driveOver = new CommandGroup();
    private SetArmAngle lowerArm = new SetArmAngle(armAngle);
    private DriveVelocityCommand driveVel = new DriveVelocityCommand(-60,  -defenseDist);
    private SpinRobot redirect;
    private DriveDistanceCommand driveDiagonal;
    private SetArmAngle findTarget = new SetArmAngle(30);
    private SpinRobot spin;
    private AutoAim aim;
    private SetFlywheelVelocity setFlyVel = new SetFlywheelVelocity(flyUpVel, flyLoVel);
    private Shoot shoot = new Shoot();
    private SwitchFeed switchCam = new SwitchFeed(ComputerVision.CAMERA_SHOOTER);
    private LEDToggle lightUp = new LEDToggle(true);

    
    public AutoRoughTerrainUniversal(int position){
        double xOffset = 0;
        double yOffset = 0;
        setTimeout(15);
        if(position == 2){
            redirectAngle = 45;
            findTargetAngle = 0;
            redirectDist = -17;
            xOffset = Preferences.getInstance().getDouble("AutoRough2XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoRough2YOffset", yOffset);
        }
        else if(position == 3){
            redirectAngle = 30;
            findTargetAngle = 0;
            redirectDist = -12;
            xOffset = Preferences.getInstance().getDouble("AutoRough3XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoRough3YOffset", yOffset);
        }
        
        else if(position==4){
            redirectAngle = 0;
            redirectTimeout = 0;
            findTargetAngle = 0;
            redirectDist = 0;
            xOffset = Preferences.getInstance().getDouble("AutoRough4XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoRough4YOffset", yOffset);
        }
        else{
            redirectAngle = -30;
            findTargetAngle = 0;
            redirectDist = -12;
            xOffset = Preferences.getInstance().getDouble("AutoRough5XOffset", xOffset);
            yOffset = Preferences.getInstance().getDouble("AutoRough5YOffset", yOffset);
        }
        aim = new AutoAim(xOffset, AutoAim.DEFAULT_Y_OFFSET, 3);
        
        redirect = new SpinRobot(redirectAngle, redirectTimeout);
        driveDiagonal = new DriveDistanceCommand(redirectDist);
        //spin = new SpinRobot(findTargetAngle,2,0.5);
        
        driveOver.addParallel(lowerArm);
        driveOver.addParallel(driveVel);
        
        CommandGroup lookAtGoal = new CommandGroup();
        lookAtGoal.addParallel(setFlyVel);
        //lookAtGoal.addParallel(spin);
        lookAtGoal.addParallel(redirect);
        lookAtGoal.addParallel(findTarget);
        
        this.addParallel(lightUp);
        this.addParallel(switchCam);
        this.addSequential(driveOver);
        //this.addSequential(driveDiagonal);
        this.addSequential(lookAtGoal);
        this.addSequential(aim);
        this.addSequential(shoot);
        
    }
}