package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.util.Vector2d;

/**
 * Directly sets the power to the motors based on input. Ends imediatly after
 * setting power, but motor will stay at set power until set again through any
 * setting mean.
 */
public class SetDrivePower extends Command {

    private int pLeft, pRight;

    public SetDrivePower(int pl, int pr) {
        pLeft = pl;
        pRight = pr;
    }

    @Override
    protected void initialize() {
        RobotCommon.runningRobot.driveTrainController
                .setPowerDirectly(new Vector2d(pLeft, pRight));
    }

    @Override
    protected void execute() {
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
