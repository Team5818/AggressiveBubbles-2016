package team5818.robot.commands;


import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.Collector;

public class ClearCollector extends Command{
    double collectPower = Collect.COLLECT_POWER;
    private Collector collector = RobotCommon.runningRobot.collector;
    boolean hasInitialized = false;
    private double maxSpitTime = .25;
    private double maxTime = 4;

    @Override
    protected void initialize() {
        // TODO Auto-generated method stub
        requires(collector);
        hasInitialized = true;
        collector.setPower(-collectPower/2);
        setTimeout(maxTime);
    }

    @Override
    protected void execute() {
        if(!hasInitialized){
            initialize();
        }
        if(timeSinceInitialized() >= maxSpitTime) {
            collector.setPower(collectPower);
        }
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
