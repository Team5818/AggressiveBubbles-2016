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
        requires(RobotCommon.runningRobot.driveTrain);
        dist = distanceInches;
        maxP = maxPower;
        timeout = timeoutSeconds;
    }
    
    public DriveDistanceCommand(double distanceInches, double maxPower) {
        requires(RobotCommon.runningRobot.driveTrain);
        dist = distanceInches;
        maxP = maxPower;
        timeout = 5.0;
    }
    
    public DriveDistanceCommand(double distanceInches) {
        requires(RobotCommon.runningRobot.driveTrain);
        dist = distanceInches;
        maxP = .5;
        timeout = 5.0;
    }
    
    protected void end()
    {
        RobotCommon.runningRobot.driveTrain.getLeftMotors().setPower(0.0);
        RobotCommon.runningRobot.driveTrain.getRightMotors().setPower(0.0);
    }
    
    @Override
    protected void initialize() {
        this.setTimeout(timeout);
        RobotCommon.runningRobot.driveTrain
                .setDriveDistance(dist, maxP);
    }

    @Override
    protected void execute() {
        
    }

    @Override
    protected boolean isFinished() {
        DriveSide ls = RobotCommon.runningRobot.driveTrain.getLeftMotors();
        DriveSide rs = RobotCommon.runningRobot.driveTrain.getRightMotors();
                
        boolean timedOut = isTimedOut(); 
        boolean onTarget = (ls.getPIDController().onTarget() || rs.getPIDController().onTarget());
        return (timedOut || onTarget);
    }

    @Override
    protected void interrupted() {
        this.end();
        
    }
}
