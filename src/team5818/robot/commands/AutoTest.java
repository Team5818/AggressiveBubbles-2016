package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.RobotCommon;
import team5818.robot.modules.FlyWheel;
import team5818.robot.util.LinearLookupTable;
import team5818.robot.util.Vector2d;

public class AutoTest extends CommandGroup {
    double[] pows = {.2,.5,.5,.2};
    double[] vels = {24,50,50,24};
    double[] dists = {0,24,36,72};
    LinearLookupTable powerTable = new LinearLookupTable(dists,pows);
    LinearLookupTable velTable = new LinearLookupTable(dists,vels);
    DriveDistanceProfile drivePower = new DriveDistanceProfile(powerTable, 108, 10);
    DriveVelocityProfile driveVel = new DriveVelocityProfile(velTable,72);

    public AutoTest() {
        //this.addSequential(drive);
        //this.addSequential(driveVel);
    }
    
    @Override
    public void initialize() {
        FlyWheel fly = RobotCommon.runningRobot.lowerFlywheel;
        fly.setPower(.2);
    }
    
    @Override
    public void execute() {
        
    }
    
    @Override
    public void end() {
        RobotCommon.runningRobot.driveTrain.setPower(new Vector2d(0,0));
    }
    
    @Override
    public void interrupted() {
        end();
    }

}
