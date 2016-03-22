package team5818.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.RobotConstants;
import team5818.robot.modules.drivetrain.DriveSide;
import team5818.robot.modules.drivetrain.DriveTrain;
import team5818.robot.util.MathUtil;

/**
 * @author Petey
 *
 */
public class SpinRobot extends Command {

    private DriveTrain train = RobotCommon.runningRobot.driveTrain;
    private double spinAngle;
    private double arcLength;
    private double drivePower;
    private static double defaultTimeout = 6;
    private double maxP;

    /**
     * @param angle
     *            - number of degrees to rotate robot
     * @param timeout
     *            - max amount of time command runs until finished
     */
    public SpinRobot(double angle, double timeout, double maxPower) {
        spinAngle = angle;
        arcLength = MathUtil.distanceOfArc(RobotConstants.ROBOT_WIDTH_IN_INCHES/2,
                spinAngle);
        setTimeout(15);
        requires(RobotCommon.runningRobot.driveTrain);
        maxP = maxPower;
    }

    /**
     * @param angle
     *            - number of degrees to spin robot sets default timeout of 6s
     */
    public SpinRobot(double angle, double timeout) {
        this(angle, timeout, .25);
    }
    
    public SpinRobot(double angle){
        this(angle, defaultTimeout, .25);
    }

    @Override
    protected void initialize() {
        train.setSpinAngle(spinAngle, maxP);

    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        DriveSide ds = RobotCommon.runningRobot.driveTrain.getLeftMotors();
        return (ds.getPIDController().onTarget() || ds.getEncPosAbs() >= arcLength || isTimedOut());

    }

    @Override
    protected void end() {
        train.getLeftMotors().setPower(0);
        train.getRightMotors().setPower(0);
    }

    @Override
    protected void interrupted() {
        end();

    }

}
