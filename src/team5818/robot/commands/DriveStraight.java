package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.util.Vector2d;

/**
 * Command to make the robot driver forward. Will go until it is
 * {@link #interrupted()}.
 */
public class DriveStraight extends Command {

    private static final Vector2d POWER = new Vector2d(0.25, 0.25);
    private boolean started;
    private boolean running;

    @Override
    protected void initialize() {

    }

    @Override
    protected void execute() {
        try {
            running = started = true;
            while (running) {
                RobotCommon.runningRobot.driveTrain.setPower(POWER);
            }
        } finally {
            running = false;
        }
    }

    @Override
    protected boolean isFinished() {
        return started && !running;
    }

    @Override
    protected void end() {
        running = false;
    }

    @Override
    protected void interrupted() {
        running = false;
    }

}