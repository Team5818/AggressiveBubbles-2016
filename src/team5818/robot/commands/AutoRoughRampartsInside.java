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
public class AutoRoughRampartsInside extends CommandGroup{

    public double flyUpVel =
            Preferences.getInstance().getDouble("UpperFlyVel", FlyWheel.SHOOT_VELOCITY_UPPER);
    public double flyLoVel =
            Preferences.getInstance().getDouble("LowerFlyVel", FlyWheel.SHOOT_VELOCITY_LOWER);
    double defenseDist = 140;
    double armAngle = Preferences.getInstance().getDouble("ArmAngleLow", 20.0);
    
    private SetArmAngle lowerArm = new SetArmAngle(armAngle);
    private DriveVelocityCommand driveVel = new DriveVelocityCommand(-50,  -defenseDist);
    private SetArmAngle findTarget = new SetArmAngle(30);
    private AutoAim aim = new AutoAim();
    private SetFlywheelVelocity setFlyVel = new SetFlywheelVelocity(flyUpVel, flyLoVel);
    private Shoot shoot = new Shoot();
    private SwitchFeed switchCam = new SwitchFeed(ComputerVision.CAMERA_SHOOTER);
    private LEDToggle lightUp = new LEDToggle(true);

    
    public AutoRoughRampartsInside(){
        this.addSequential(lightUp);
        this.addSequential(switchCam);
        this.addSequential(lowerArm);
        this.addSequential(driveVel);
        this.addSequential(findTarget);
        this.addSequential(setFlyVel);
        this.addSequential(aim);
        this.addSequential(shoot);
        
    }
}
