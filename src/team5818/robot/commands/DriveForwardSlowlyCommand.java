package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.util.Vector2d;

public class DriveForwardSlowlyCommand extends Command {

    private boolean hasRun;

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        RobotCommon.runningRobot.driveTrain.setPower(new Vector2d(0.25, 0.25));
        hasRun = true;
    }

    @Override
    protected boolean isFinished() {
        return hasRun;
    }

    @Override
    protected void end() {
        RobotCommon.runningRobot.driveTrain.setPower(new Vector2d(0, 0));
    }

    @Override
    protected void interrupted() {
    }

}
