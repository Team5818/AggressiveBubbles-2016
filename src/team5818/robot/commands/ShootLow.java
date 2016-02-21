package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.Arm;

public class ShootLow extends Command {

    private Arm arm;
    
    private double zeroTime;
    private double maxShootTime = 4 * 1E9;
    
    public ShootLow()
    {
        arm = RobotCommon.runningRobot.arm;
    }
    
    @Override
    protected void initialize() {
        arm.setCollectorPower(-Collect.MAX_COLLECT_POWER);
        zeroTime = System.nanoTime();
    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        if(System.nanoTime() - zeroTime > maxShootTime)
            return true;
        return false;
    }

    @Override
    protected void end() {
        arm.setCollectorPower(0);
    }

    @Override
    protected void interrupted() {
        end();
    }

}
