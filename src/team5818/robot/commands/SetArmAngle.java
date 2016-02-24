package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.Arm;

public class SetArmAngle extends Command {

    public double targetAngle;
    public Arm arm;
    private boolean hasInitialized = false;
    private double maxTime = 3;

    public SetArmAngle(double angle) {
        targetAngle = angle;
        arm = RobotCommon.runningRobot.arm;
        requires(arm);
    }

    @Override
    protected void initialize() {
        arm.goToAngle(targetAngle);
        hasInitialized = true;
        setTimeout(maxTime);
    }

    @Override
    protected void execute() {
        if(!hasInitialized) {
            initialize();
        }

    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }

    @Override
    protected void end() {
        hasInitialized  = false;
    }

    @Override
    protected void interrupted() {
        arm.disablePID();
    }

}
