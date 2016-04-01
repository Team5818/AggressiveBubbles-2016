package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCoDriver;
import team5818.robot.RobotCommon;
import team5818.robot.modules.Arm;

/**
 * @author Petey sets the angle of the arm using PID
 *
 */
public class SetArmAngle extends Command {

    public double targetAngle;
    public Arm arm;
    private boolean hasInitialized = false;
    private final static double maxTime = 3;

    /**
     * @param angle
     *            target angle
     */
    public SetArmAngle(double angle) {
        this(angle, maxTime);
    }
    
    /**
     * @param angle
     *            target angle
     */
    public SetArmAngle(double angle, double timeout) {
        targetAngle = angle;
        arm = RobotCommon.runningRobot.arm;
        requires(arm);
        setTimeout(timeout);
    }

    @Override
    protected void initialize() {
        double offset = RobotCoDriver.secondJoystick.getY() * -2;
        arm.goToAngle(targetAngle + offset);
        hasInitialized = true;
    }

    @Override
    protected void execute() {
        if (!hasInitialized) {
            initialize();
        }

    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }

    @Override
    protected void end() {
        hasInitialized = false;
    }

    @Override
    protected void interrupted() {
        arm.disablePID();
    }

}
