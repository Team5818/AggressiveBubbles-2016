package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.drivetrain.DriveTrain;
import team5818.robot.util.Vector2d;

public class DriveVelocityCommand extends Command{
    
    private double velocityL;
    private double velocityR;
    private double distance;
    private double initialDist;
    DriveTrain train = RobotCommon.runningRobot.driveTrain;

    
    public DriveVelocityCommand(double velL, double velR, double dist){
        velocityL = velL;
        velocityR = velR;
        distance = dist;
        requires(RobotCommon.runningRobot.driveTrain);
        setTimeout(5);        
    }
    
    public DriveVelocityCommand(double vel, double dist){
        this(vel, vel, dist);
    }

    @Override
    protected void initialize() {
        initialDist = train.getAverageDistance();
        train.setVelocity(new Vector2d(velocityL, velocityR));
    }

    @Override
    protected void execute() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected boolean isFinished() {
        return (Math.abs(train.getAverageDistance() - initialDist) >= Math.abs(distance) || isTimedOut());

    }

    @Override
    protected void end() {
        train.setPower(new Vector2d(0,0));        
    }

    @Override
    protected void interrupted() {
        end();        
    }



}
