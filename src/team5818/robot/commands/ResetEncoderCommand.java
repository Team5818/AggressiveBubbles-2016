package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;

public class ResetEncoderCommand extends Command {

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        RobotCommon.runningRobot.driveTrain.getLeftMotors().setDriveDistance(0);
        RobotCommon.runningRobot.driveTrain.getRightMotors()
                .setDriveDistance(0);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {
    }

    @Override
    protected void interrupted() {
    }

}
