package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.drivetrain.DriveTrain;
import team5818.robot.util.Vector2d;

public class DrivePower extends Command{
    
    private DriveTrain train = RobotCommon.runningRobot.driveTrain;
    private double driveDistance;
    private double drivePower;

    public DrivePower(double distance, double power, double timeout){
        driveDistance = distance;
        drivePower = power;
        setTimeout(timeout);
    }
    
    @Override
    protected void initialize() {
        Vector2d vector = new Vector2d(drivePower, drivePower);
        train.setPower(vector);
        
    }

    @Override
    protected void execute() {
        
    }

    @Override
    protected boolean isFinished() {
        return (train.getAverageDistance() >= driveDistance || isTimedOut());

    }

    @Override
    protected void end() {
        Vector2d stopVec = new Vector2d(0,0);
        train.setPower(stopVec);       
    }

    @Override
    protected void interrupted() {
        end();
        
    }

}
