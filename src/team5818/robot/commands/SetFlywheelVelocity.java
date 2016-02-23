package team5818.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.FlyWheel;

public class SetFlywheelVelocity extends Command {

    /**
     * The velocity to set the flywheel to.
     */
    private double velocity = 0;
    /**
     * The backspin multiplier for the flywheel. In percent 1 being 100%.
     */
    private double backSpin = 0;
    private double upVel = velocity - velocity * backSpin;
    private double lowVel = velocity + velocity * backSpin;

    private static final FlyWheel flyUp =
            RobotCommon.runningRobot.upperFlywheel;
    private static final FlyWheel flyLo =
            RobotCommon.runningRobot.lowerFlywheel;

    /**
     * @param vel
     *            The velocity desired to be set in units of
     */
    public SetFlywheelVelocity(double velUp, double velLow) {
        upVel = velUp - velUp * backSpin;
        lowVel = velLow + velLow * backSpin;
        requires(flyUp);
        requires(flyLo);
        
    }

    @Override
    protected void initialize() {
        flyUp.setVelocity(upVel);
        flyLo.setVelocity(lowVel);
    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {        
        return hasReachedVelocity();
    }

    @Override
    protected void end() {}

    @Override
    protected void interrupted() {
        flyUp.setPower(0);
        flyLo.setPower(0);

    }

    private boolean hasReachedVelocity() {
        double tolerance = FlyWheel.TOLERANCE;
        if (upVel <= flyUp.getRPS() + tolerance
                && upVel >= flyUp.getRPS() - tolerance
                && lowVel <= flyLo.getRPS() + tolerance
                && lowVel >= flyLo.getRPS() - tolerance)
            return true;
        return false;
    }

}
