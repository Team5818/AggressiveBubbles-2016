package team5818.robot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.RobotCommon;
import team5818.robot.modules.Arm;

public class LowerArmToGround extends CommandGroup{
    
    double armPower = -.6;
    double collectAngle = Preferences.getInstance().getDouble("ArmAngleCollect", 3.0);
    SetArmAngle goToCollect = new SetArmAngle(collectAngle);
    ArmPower armToGround = new ArmPower(armPower);
    
    public LowerArmToGround(){
        this.addSequential(goToCollect);
        this.addSequential(armToGround);
    }
    

}
