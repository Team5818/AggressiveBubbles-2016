package team5818.robot.commands;

import team5818.robot.RobotCommon;
import team5818.robot.util.Vector2d;

public class DriveVelocityCommand extends QuickCommand {

    public DriveVelocityCommand() {
        requires(RobotCommon.runningRobot.driveTrainController);
    }

    @Override
    protected void subexecute() {
        RobotCommon.runningRobot.driveTrainController
                .setVelocity(new Vector2d(100, 100));
    }

}
