package team5818.robot.commands;

import org.usfirst.frc.team5818.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
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
        requires(Robot.runningRobot.driveTrain);
    }

    @Override
    protected void initialize() {
        Robot.runningRobot.driveTrain
                .setPower(new Vector2d(pLeft, pRight));
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
