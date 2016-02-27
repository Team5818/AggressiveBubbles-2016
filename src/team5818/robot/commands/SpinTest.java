package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class SpinTest extends CommandGroup{
    
    private SpinRobot aim = new SpinRobot(-20.0);
    
public SpinTest(){
        
        this.addSequential(aim);
    }

}
