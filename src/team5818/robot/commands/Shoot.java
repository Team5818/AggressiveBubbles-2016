package team5818.robot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.RobotCommon;
import team5818.robot.modules.Collector;

public class Shoot extends CommandGroup{
    public double shootUpperVelocity = Preferences.getInstance().getDouble("UpperFlyVel", 100);
    public double shootLowerVelocity = Preferences.getInstance().getDouble("LowerFlyVel", 60);
    public double shootAngle = Preferences.getInstance().getDouble("ArmAngleHigh", 60);
    
    private SetFlywheelVelocity setFlyVelocity = new SetFlywheelVelocity(shootUpperVelocity,shootLowerVelocity);
    private SetArmAngle setArmAngle = new SetArmAngle(shootAngle);
    private SetFlywheelPower flyToZero = new SetFlywheelPower(0);
    private Collect collectIn = new Collect(Collect.COLLECT_POWER, 2);
    private CommandGroup prepareShot = new CommandGroup();
    
    /**
     * The maximum time the shooter can be on in nano seconds.
     */
    private double maxShootTime = 4;
    
    public Shoot(double angle, double flyUpVel, double flyLoVel){
        prepareShot.addParallel(setArmAngle);
        prepareShot.addParallel(setFlyVelocity);
        this.addSequential(prepareShot);
        this.addSequential(collectIn);
        this.addSequential(flyToZero);
    }
    
    

}
