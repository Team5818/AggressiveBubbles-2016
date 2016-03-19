package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class DriveDistanceTest extends CommandGroup{
    
    private DriveDistanceCommand drive = new DriveDistanceCommand(60);
    
    public DriveDistanceTest(){
        this.addSequential(drive);
    }


}
