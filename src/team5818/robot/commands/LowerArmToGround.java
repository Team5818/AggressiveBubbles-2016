package team5818.robot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class LowerArmToGround extends CommandGroup{

    double collectAngle = Preferences.getInstance().getDouble("ArmAngleCollect", 3.0);
    private SetArmAngle lowerToCollect = new SetArmAngle(collectAngle);
    private ArmDownUntilStopped armToGround = new ArmDownUntilStopped();
    
    public LowerArmToGround(){
        this.addSequential(lowerToCollect);
        this.addSequential(armToGround);
    }
}
