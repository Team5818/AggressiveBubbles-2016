package team5818.robot.commands;

import org.usfirst.frc.team5818.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.modules.drivetrain.DriveSide;
import team5818.robot.modules.drivetrain.DriveTrain;
import team5818.robot.util.LinearLookupTable;
import team5818.robot.util.Vector2d;

public class DriveDistanceProfile extends Command {

    double dist;
    double timeout;
    double initialLeft;
    double initialRight;
    double initialDist;
    private LinearLookupTable leftTable;
    private LinearLookupTable rightTable;
    private DriveTrain train;
    private DriveSide leftSide;
    private DriveSide rightSide;
    
    public DriveDistanceProfile(LinearLookupTable left, LinearLookupTable right, double distanceInches, double timeoutSeconds) {
        requires(Robot.runningRobot.driveTrain);
        train = Robot.runningRobot.driveTrain;
        leftSide = Robot.runningRobot.driveTrain.getLeftMotors();
        rightSide = Robot.runningRobot.driveTrain.getRightMotors();
        dist = distanceInches;
        timeout = timeoutSeconds;
        leftTable = left;
        rightTable = right;
    }
    
    public DriveDistanceProfile(LinearLookupTable table, double distance, double timeout){
        this(table, table, distance, timeout);
    }
    
 

    protected void end()
    {
        leftSide.setPower(0.0);
        rightSide.setPower(0.0);
    }
    
    @Override
    protected void initialize() {
        this.setTimeout(timeout);
        Robot.runningRobot.driveTrain
                .setDriveDistance(dist);
        initialDist = train.getAverageDistance();
        initialLeft = leftSide.getEncPosAbs();
        initialRight = rightSide.getEncPosAbs();
    }

    @Override
    protected void execute() {
        double leftDist =  leftSide.getEncPosAbs()-initialLeft;
        double rightDist = rightSide.getEncPosAbs()-initialRight;
        double avDist = train.getAverageDistance() - initialDist;
        double leftPower = leftTable.getEstimate(avDist);
        double rightPower = rightTable.getEstimate(avDist);
        leftSide.setMaxPower(leftPower);
        rightSide.setMaxPower(rightPower);
    }

    @Override
    protected boolean isFinished() {
        DriveSide ds = Robot.runningRobot.driveTrain.getLeftMotors();
        
        System.out.println(ds.getPIDController().getError()+ " onTarget "+ds.getPIDController().onTarget() );
        
        boolean timedOut = isTimedOut(); 
        boolean onTarget = Robot.runningRobot.driveTrain.getLeftMotors().getPIDController().onTarget();
        if(timedOut){
            return true;
        }
        if(onTarget){
            return true;
        }
        return false;
    }

    @Override
    protected void interrupted() {
        this.end();
        
    }
}