package team5818.robot.commands;

import team5818.robot.RobotCommon;

public class DriveForwardSlowlyCommand extends QuickCommand {

    public DriveForwardSlowlyCommand() {
        requires(RobotCommon.runningRobot.driveTrainController);
    }

    @Override
    protected void subexecute() {
        RobotCommon.runningRobot.driveTrainController
                .driveToTargetXInchesAway(5);
    }
}
