package team5818.robot.commands;

import team5818.robot.RobotCommon;
import team5818.robot.util.Vector2d;

public class DriveForwardCommand extends QuickCommand {

    @Override
    public synchronized void start() {
        RobotCommon.runningRobot.driveTrain.setPower(new Vector2d(0, 1));
    }

    @Override
    public synchronized void cancel() {
        end();
    }

    @Override
    protected void end() {
        super.end();
        RobotCommon.runningRobot.driveTrain.setPower(new Vector2d(0, 0));
    }

    @Override
    protected void subexecute() {
    }

}
