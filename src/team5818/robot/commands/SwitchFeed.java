package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.ComputerVision;

/**
 * Switches the camera feed. Also switches the state of the LED based on camera.
 * Shooting camera making the led on.
 */
public class SwitchFeed extends Command {

    private final int feed;

    /**
     * @param feed
     *            The desired camera feed.
     */
    public SwitchFeed(int feed) {
        this.feed = feed;
    }

    @Override
    protected void initialize() {
        RobotCommon.runningRobot.vision.See.ChangeFeed(feed);
        if (feed == ComputerVision.CAMERA_DRIVER)
            new LEDToggle(false).start();
        else
            new LEDToggle(true).start();
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

    @Override
    protected void execute() {

    }

}
