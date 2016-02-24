package team5818.robot.commands;

import team5818.robot.modules.Collector;
import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;

/**
 * Runs the collector in a specified speed for a specified time.
 */
public class Collect extends Command {

    /**
     * The maximum collecting power that can be set.
     */
    public static final double MAX_COLLECT_POWER = 1;
    /**
     * The recommended power to collect a ball.
     */
    public static final double COLLECT_POWER = 0.6;

    private final double collectPower;
    private Collector collector;

    /**
     * @param power The desired power to run the collector
     */
    private Collect(double power) {
        collectPower = power;
        collector = RobotCommon.runningRobot.collector;
        requires(collector);
    }

    /**
     * @param power
     *            The desired power to run the collector.
     * @param timeout
     *            The time to have the command run.
     */
    public Collect(double power, double timeout) {
        this(power);
        this.setTimeout(timeout);
    }

    @Override
    protected void initialize() {
        collector.setPower(collectPower);
    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }

    @Override
    protected void end() {
        collector.setPower(0);
    }

    @Override
    protected void interrupted() {
        end();
    }

}
