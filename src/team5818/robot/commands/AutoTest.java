package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.RobotCommon;
import team5818.robot.util.Vector2d;

public class AutoTest extends CommandGroup {

    public AutoTest() {
        this.addSequential(new SpinRobot(-180, 2, 0.5));
    }
    
    @Override
    public void initialize() {
        
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
