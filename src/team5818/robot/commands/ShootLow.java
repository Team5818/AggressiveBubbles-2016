package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.Arm;
import team5818.robot.modules.Collector;

public class ShootLow extends Command {

    private Collector collector;
    
    private double zeroTime;
    private double maxShootTime = 4 * 1E9;
    
    public ShootLow()
    {
        collector = RobotCommon.runningRobot.collector;
    }
    
    @Override
    protected void initialize() {
        collector.setPower(-Collect.MAX_COLLECT_POWER);
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
        collector.setPower(0);
    }

    @Override
    protected void interrupted() {
        end();
    }

}
