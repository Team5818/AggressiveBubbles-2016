package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;


public class TestDriveVel extends CommandGroup{
    
    private DriveVelocityCommand setVel = new DriveVelocityCommand();
    
    public TestDriveVel(){
        this.addSequential(setVel);
    }

}
