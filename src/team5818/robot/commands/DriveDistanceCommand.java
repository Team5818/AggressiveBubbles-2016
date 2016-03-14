package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.drivetrain.DriveSide;
import team5818.robot.util.Vector2d;

public class DriveDistanceCommand extends Command {

    double dist;
    double maxP;
    double timeout;
    
    public DriveDistanceCommand(double distanceInches, double maxPower, double timeoutSeconds) {
        requires(RobotCommon.runningRobot.driveTrainController);
        dist = distanceInches;
        maxP = maxPower;
        timeout = timeoutSeconds;
    }
    
    public DriveDistanceCommand(double distanceInches, double maxPower) {
        requires(RobotCommon.runningRobot.driveTrainController);
        dist = distanceInches;
        maxP = maxPower;
        timeout = 10.0;
    }
    
    public DriveDistanceCommand(double distanceInches) {
        requires(RobotCommon.runningRobot.driveTrainController);
        dist = distanceInches;
        maxP = 1.0;
        timeout = 10.0;
    }
    
    protected void end()
    {
        RobotCommon.runningRobot.driveTrain.getLeftMotors().setPower(0.0);
        RobotCommon.runningRobot.driveTrain.getRightMotors().setPower(0.0);
    }
    
    @Override
    protected void initialize() {
        this.setTimeout(timeout);
        RobotCommon.runningRobot.driveTrainController
                .driveToTargetXInchesAway(dist, maxP);
    }

    @Override
    protected void execute() {
        
    }

    @Override
    protected boolean isFinished() {
        DriveSide ds = RobotCommon.runningRobot.driveTrain.getLeftMotors();
        
        System.out.println(ds.getPIDController().getError()+ " onTarget "+ds.getPIDController().onTarget() );
        return isTimedOut() || RobotCommon.runningRobot.driveTrain.getLeftMotors().getPIDController().onTarget();
    }

    @Override
    protected void interrupted() {
        this.end();
        
    }
}
