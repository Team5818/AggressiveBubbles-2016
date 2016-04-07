package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.drivetrain.DriveSide;
import team5818.robot.modules.drivetrain.DriveTrain;
import team5818.robot.util.LinearLookupTable;
import team5818.robot.util.Vector2d;

public class DriveDistanceProfile extends Command {

    double dist;
    double timeout;
    double initialLeft;
    double initialRight;
    private LinearLookupTable leftTable;
    private LinearLookupTable rightTable;
    private DriveTrain train;
    private DriveSide leftSide;
    private DriveSide rightSide;
    
    public DriveDistanceProfile(LinearLookupTable left, LinearLookupTable right, double distanceInches, double timeoutSeconds) {
        requires(RobotCommon.runningRobot.driveTrain);
        train = RobotCommon.runningRobot.driveTrain;
        leftSide = RobotCommon.runningRobot.driveTrain.getLeftMotors();
        rightSide = RobotCommon.runningRobot.driveTrain.getRightMotors();
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
        RobotCommon.runningRobot.driveTrain
                .setDriveDistance(dist);
        initialLeft = leftSide.getEncPosAbs();
        initialRight = rightSide.getEncPosAbs();
    }

    @Override
    protected void execute() {
        double leftDist =  leftSide.getEncPosAbs()-initialLeft;
        double rightDist = rightSide.getEncPosAbs()-initialRight;
        double leftPower = leftTable.getEstimate(leftDist);
        double rightPower = rightTable.getEstimate(rightDist);
        leftSide.setMaxPower(leftPower);
        rightSide.setMaxPower(rightPower);
    }

    @Override
    protected boolean isFinished() {
        DriveSide ds = RobotCommon.runningRobot.driveTrain.getLeftMotors();
        
        System.out.println(ds.getPIDController().getError()+ " onTarget "+ds.getPIDController().onTarget() );
        
        boolean timedOut = isTimedOut(); 
        boolean onTarget = RobotCommon.runningRobot.driveTrain.getLeftMotors().getPIDController().onTarget();
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