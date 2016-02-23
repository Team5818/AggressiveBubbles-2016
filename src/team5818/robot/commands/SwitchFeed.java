package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;

public class SwitchFeed extends QuickCommand {

    private final int feed;

    public SwitchFeed(int feed) {
        this.feed = feed;
    }

    @Override
    protected void subexecute() {
        RobotCommon.runningRobot.vision.See.ChangeFeed(feed);
    }

}
