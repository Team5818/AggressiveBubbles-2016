package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.RobotConstants;
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

    /**
     * @param angle
     *            - number of degrees to rotate robot
     * @param timeout
     *            - max amount of time command runs until finished
     */
    public SpinRobot(double angle, double timeout) {
        spinAngle = angle;
        arcLength = MathUtil.distanceOfArc(RobotConstants.ROBOT_WIDTH_IN_INCHES,
                spinAngle);
        setTimeout(timeout);
        requires(RobotCommon.runningRobot.driveTrainController);
    }

    /**
     * @param angle
     *            - number of degrees to spin robot sets default timeout of 6s
     */
    public SpinRobot(double angle) {
        this(angle, defaultTimeout);
    }

    @Override
    protected void initialize() {
        double negArc = -arcLength;
        train.getLeftMotors().setDriveDistance(arcLength);
        train.getRightMotors().setDriveDistance(negArc);

    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        return (train.getAverageDistance() >= Math.abs(arcLength)
                || isTimedOut());

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
