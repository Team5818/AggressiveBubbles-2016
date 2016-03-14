package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.util.Vector2d;

/**
 * Command to make the robot driver forward. Will go until it is
 * {@link #interrupted()}.
 */
public class DriveStraight extends Command {

    private static final Vector2d POWER = new Vector2d(0.25, 0.25);
    private boolean ended = false;

    public DriveStraight() {
        requires(RobotCommon.runningRobot.driveTrainController);
    }

    @Override
    protected void initialize() {
        ended = false;
    }

    @Override
    protected void execute() {
        RobotCommon.runningRobot.driveTrainController.setPowerDirectly(POWER);
    }

    @Override
    protected boolean isFinished() {
        return !ended;
    }

    @Override
    protected void end() {
    }

    @Override
    protected void interrupted() {
        ended = true;
    }

}
