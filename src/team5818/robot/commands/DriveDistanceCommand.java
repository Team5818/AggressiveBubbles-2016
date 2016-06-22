package team5818.robot.commands;

import org.usfirst.frc.team5818.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.modules.drivetrain.DriveSide;
import team5818.robot.util.Vector2d;

public class DriveDistanceCommand extends Command {

    double dist;
    double maxP;
    double timeout;
    
    public DriveDistanceCommand(double distanceInches, double maxPower, double timeoutSeconds) {
        requires(Robot.runningRobot.driveTrain);
        dist = distanceInches;
        maxP = maxPower;
        timeout = timeoutSeconds;
    }
    
    public DriveDistanceCommand(double distanceInches, double maxPower) {
        requires(Robot.runningRobot.driveTrain);
        dist = distanceInches;
        maxP = maxPower;
        timeout = 5.0;
    }
    
    public DriveDistanceCommand(double distanceInches) {
        requires(Robot.runningRobot.driveTrain);
        dist = distanceInches;
        maxP = .5;
        timeout = 5.0;
    }
    
    protected void end()
    {
        Robot.runningRobot.driveTrain.getLeftMotors().setPower(0.0);
        Robot.runningRobot.driveTrain.getRightMotors().setPower(0.0);
    }
    
    @Override
    protected void initialize() {
        this.setTimeout(timeout);
        Robot.runningRobot.driveTrain
                .setDriveDistance(dist, maxP);
    }

    @Override
    protected void execute() {
        
    }

    @Override
    protected boolean isFinished() {
        DriveSide ls = Robot.runningRobot.driveTrain.getLeftMotors();
        DriveSide rs = Robot.runningRobot.driveTrain.getRightMotors();
                
        boolean timedOut = isTimedOut(); 
        boolean onTarget = (ls.getPIDController().onTarget() || rs.getPIDController().onTarget());
        return (timedOut || onTarget);
    }

    @Override
    protected void interrupted() {
        this.end();
        
    }
}
