package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;


public class SwitchFeed extends Command {
    
    private final int feed;
    private boolean hasStarted;
    private boolean hasEnded;
    
    public SwitchFeed(int feed) {
        this.feed = feed;
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        hasStarted = true;
        hasEnded = false;
        RobotCommon.runningRobot.vision.See.ChangeFeed(feed);
        hasEnded = true;
    }

    @Override
    protected boolean isFinished() {
        return hasStarted && hasEnded;
    }

    @Override
    protected void end() {
        hasStarted = false;
        hasEnded = false;
    }

    @Override
    protected void interrupted() {
        end();
    }

}
