package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.FlyWheel;
import team5818.robot.modules.drivetrain.DriveSide;
import team5818.robot.modules.drivetrain.DriveTrain;
import team5818.robot.util.LinearLookupTable;
import team5818.robot.util.Vector2d;

public class FlywheelVelocityProfile extends Command{
    
    private double startTime;
    private LinearLookupTable lowerTable;
    private LinearLookupTable upperTable;
    private FlyWheel flyLo = RobotCommon.runningRobot.lowerFlywheel;
    private FlyWheel flyUp = RobotCommon.runningRobot.upperFlywheel;

    

    
    public FlywheelVelocityProfile(LinearLookupTable lower, LinearLookupTable upper, double time){
        lowerTable = lower;
        upperTable = upper;
        requires(RobotCommon.runningRobot.driveTrain);
        setTimeout(time);        
    }
    
    public FlywheelVelocityProfile(LinearLookupTable table, double time){
        this(table, table, time);
    }

    @Override
    protected void initialize() {
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void execute() {
        double currentTime = System.currentTimeMillis() - startTime;       
        double velocityLower = lowerTable.getEstimate(currentTime);
        double velocityUpper = upperTable.getEstimate(currentTime);
        flyLo.setVelocity(velocityLower);
        flyUp.setVelocity(velocityUpper);
        
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();

    }

    @Override
    protected void end() {
        flyLo.setPower(0);
        flyUp.setPower(0);
    }

    @Override
    protected void interrupted() {
        end();        
    }


}
