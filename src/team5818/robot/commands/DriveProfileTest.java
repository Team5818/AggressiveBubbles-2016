package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.util.LinearLookupTable;

public class DriveProfileTest extends CommandGroup{
    
    double[] vels = {0,0,.4,.5};
    double[] dists = {0,100,200,300};
    LinearLookupTable table = new LinearLookupTable(dists,vels);
    DriveDistanceProfile drive = new DriveDistanceProfile(table, 500, 15);
    
    public DriveProfileTest(){
        this.addSequential(drive);
    }

}
