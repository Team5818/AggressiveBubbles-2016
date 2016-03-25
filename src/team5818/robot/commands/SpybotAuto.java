package team5818.robot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.modules.FlyWheel;

public class SpybotAuto extends CommandGroup{

    public double velocityRatio = 2.66;
    
    public double shootAngle = 60.0;
    
    public double flyUpVel =
            Preferences.getInstance().getDouble("UpperFlyVel", FlyWheel.SHOOT_VELOCITY_UPPER);
    public double flyLoVel =
            Preferences.getInstance().getDouble("LowerFlyVel", FlyWheel.SHOOT_VELOCITY_LOWER);
    
    private SetFlywheelVelocity setFlyVel = new SetFlywheelVelocity(flyUpVel, flyLoVel, 3);
    private SetArmAngle aim = new SetArmAngle(shootAngle);
    private Shoot shoot = new Shoot();
    private LowerArmToGround armToGround = new LowerArmToGround();
    private DriveDistanceCommand backUp = new DriveDistanceCommand(-60);
    private DriveVelocityCommand driveArc = new DriveVelocityCommand(20*velocityRatio,20, 51.84);
    private DriveDistanceCommand throughLowbar = new DriveDistanceCommand(-200);
    private Collect collect = new Collect(-.8);
    private SpinRobot rotate = new SpinRobot(180,2,.05);
    private DriveDistanceCommand backThrough = new DriveDistanceCommand(180);        
    
    public SpybotAuto(){
        this.addSequential(setFlyVel);
        this.addSequential(aim);
        this.addSequential(shoot);
        this.addSequential(armToGround);
        this.addSequential(backUp);
        this.addSequential(driveArc);
        this.addSequential(throughLowbar);
        this.addSequential(collect);
        this.addSequential(rotate);
        this.addSequential(backThrough);
    }
    
}
