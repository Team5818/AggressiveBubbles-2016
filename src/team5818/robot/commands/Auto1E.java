package team5818.robot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.Field;

public class Auto1E extends CommandGroup{
   
    public double collectAngle = Preferences.getInstance().getDouble("ArmCollectAngle", -6.0);
    public double shootAngle = Preferences.getInstance().getDouble("ArmShootHigh", 40.0);
    public double flyUpVel = Preferences.getInstance().getDouble("UpperFlyVel", 100.0);
    public double flyLoVel = Preferences.getInstance().getDouble("LowerFlyVel", 60.0);
    public double lowbarDist = Field.AUTOSTART_TO_LOWBAR;
    
    private SetArmAngle putArmDown = new SetArmAngle(collectAngle);
    private DriveDistanceCommand goUnderLowbar = new DriveDistanceCommand(lowbarDist);
    private SpinRobot aim = new SpinRobot(20.0);
    private Shoot dontMiss = new Shoot(shootAngle, flyUpVel, flyLoVel);
    
    public Auto1E(){
        
        this.addSequential(putArmDown);
        this.addSequential(goUnderLowbar);
        this.addSequential(aim);
        this.addSequential(dontMiss);
    }

}
