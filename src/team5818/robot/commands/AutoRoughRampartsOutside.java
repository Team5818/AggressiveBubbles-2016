package team5818.robot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.modules.FlyWheel;

/**
 * 
 * @author Petey
 * basic auto routine, uses velocity control to go over ramparts, rough terain, moat
 * 
 *
 */
public class AutoRoughRampartsOutside extends CommandGroup{

    public double flyUpVel =
            Preferences.getInstance().getDouble("UpperFlyVel", FlyWheel.SHOOT_VELOCITY_UPPER);
    public double flyLoVel =
            Preferences.getInstance().getDouble("LowerFlyVel", FlyWheel.SHOOT_VELOCITY_LOWER);
    double defenseDist = 180;
    double armAngle = Preferences.getInstance().getDouble("ArmAngleLow", 20.0);
    
    private SetArmAngle lowerArm = new SetArmAngle(armAngle);
    private DriveVelocityCommand driveVel = new DriveVelocityCommand(60,  -defenseDist);
    private SpinRobot spin1 = new SpinRobot (30);
    private DriveDistanceCommand driveDist = new DriveDistanceCommand(60);
    private SpinRobot spin2 = new SpinRobot(-30);
    private SetArmAngle findTarget = new SetArmAngle(30);
    private AutoAim aim = new AutoAim();
    private SetFlywheelVelocity setFlyVel = new SetFlywheelVelocity(flyUpVel, flyLoVel);
    private Shoot shoot = new Shoot();

    
    public AutoRoughRampartsOutside(){
        this.addSequential(lowerArm);
        this.addSequential(driveVel);
        this.addSequential(spin1);
        this.addSequential(driveDist);
        this.addSequential(spin2);
        this.addSequential(findTarget);
        this.addSequential(setFlyVel);
        this.addSequential(aim);
        this.addSequential(shoot);
        
    }
}