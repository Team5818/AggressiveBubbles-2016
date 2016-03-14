package team5818.robot.commands;

import team5818.robot.RobotCommon;

public class DriveForwardSlowlyCommand extends QuickCommand {

    public DriveForwardSlowlyCommand() {
        requires(RobotCommon.runningRobot.driveTrain);
    }

    @Override
    protected void subexecute() {
        RobotCommon.runningRobot.driveTrain
                .setDriveDistance(5);
    }
}
