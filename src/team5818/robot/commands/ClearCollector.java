package team5818.robot.commands;


import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.Collector;

public class ClearCollector extends Command{
    double collectPower = Collect.COLLECT_POWER;
    private Collector collector = RobotCommon.runningRobot.collector;
    boolean hasInitialized = false;
    private double maxSpitTime = .25 * 1E9;
    private double maxTime = 4* 1E9;
    private double zeroTime;

    @Override
    protected void initialize() {
        // TODO Auto-generated method stub
        zeroTime = System.nanoTime();
        requires(collector);
        hasInitialized = true;
        collector.setPower(-collectPower/2);
        
    }

    @Override
    protected void execute() {
        if(!hasInitialized){
            initialize();
        }
        if(System.nanoTime() - zeroTime >= maxSpitTime) {
            collector.setPower(collectPower);
        }
    }

    @Override
    protected boolean isFinished() {
        if(System.nanoTime() - zeroTime >= maxTime) {
            return true;
        }
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
