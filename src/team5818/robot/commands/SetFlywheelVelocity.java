package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.FlyWheel;

public class SetFlyWheelVelocity extends Command {

    /**
     * The velocity to set the flywheel to.
     */
    private double velocity = 0;
    /**
     * The backspin multiplier for the flywheel. In percent 1 being 100%.
     */
    private double backSpin = 0;
    private double upVel = velocity - velocity*backSpin;
    private double lowVel = velocity + velocity*backSpin;
    
    
    private static final FlyWheel flyUp = RobotCommon.runningRobot.upperFlywheel;
    private static final FlyWheel flyLo = RobotCommon.runningRobot.lowerFlywheel;
    /**
     * @param vel The velocity desired to be set in units of 
     */
    public void setVelocity(double vel)
    {
        velocity = vel;
    }

    @Override
    protected void initialize() {

        flyUp.setVelocity(upVel);
        flyUp.setVelocity(lowVel);
    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        flyUp.setPower(0);
        flyLo.setPower(0);
    }

    @Override
    protected void interrupted() {
        end();

    }

}
