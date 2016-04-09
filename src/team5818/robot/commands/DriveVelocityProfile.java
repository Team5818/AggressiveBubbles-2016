package team5818.robot.commands;


import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.drivetrain.DriveSide;
import team5818.robot.modules.drivetrain.DriveTrain;
import team5818.robot.util.LinearLookupTable;
import team5818.robot.util.Vector2d;

public class DriveVelocityProfile extends Command{
    
    private double distance;
    private double initialDist;
    private double initialLeft;
    private double initialRight;
    private LinearLookupTable leftTable;
    private LinearLookupTable rightTable;
    private DriveTrain train = RobotCommon.runningRobot.driveTrain;
    private DriveSide leftSide = RobotCommon.runningRobot.driveTrain.getLeftMotors();;
    private DriveSide rightSide = RobotCommon.runningRobot.driveTrain.getRightMotors();

    
    public DriveVelocityProfile(LinearLookupTable left, LinearLookupTable right, double dist){
        leftTable = left;
        rightTable = right;
        distance = dist;
        requires(RobotCommon.runningRobot.driveTrain);
        setTimeout(5);        
    }
    
    public DriveVelocityProfile(LinearLookupTable table, double dist){
        this(table, table, dist);
    }

    @Override
    protected void initialize() {
        initialDist = train.getAverageDistance();
        initialLeft = leftSide.getEncPosAbs();
        initialRight = rightSide.getEncPosAbs();
    }

    @Override
    protected void execute() {
        double leftDist =  leftSide.getEncPosAbs()-initialLeft;
        double rightDist = rightSide.getEncPosAbs()-initialRight;
        double avDist = train.getAverageDistance() - initialDist;
        double velocityL = leftTable.getEstimate(avDist);
        double velocityR = rightTable.getEstimate(avDist);
        
        train.setVelocity(new Vector2d(velocityL, velocityR));
        
    }

    @Override
    protected boolean isFinished() {
        return (Math.abs(rightSide.getEncPosAbs() - initialRight) >= Math.abs(distance) || isTimedOut());

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