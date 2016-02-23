package team5818.robot.commands;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.RobotCommon;
import team5818.robot.RobotConstants;
import team5818.robot.modules.Collector;

public class Collect extends Command {

    public static final double MAX_COLLECT_POWER = 1;
    public static final double COLLECT_POWER = 0.6;
    private Collector collector;
    
    /**
     * Time to run the motor
     */
    //TODO tune the ballFeedOutTime number to the correct timing.
    private double maxCollectTime = 4 * 1E9;
    private double zeroTime;
    
    private boolean hasFinished = false;
    
    
    @Override
    protected void initialize() {
        collector = RobotCommon.runningRobot.collector;
        collector.setPower(MAX_COLLECT_POWER);
        zeroTime = System.nanoTime();
        requires(collector);
    }

    @Override
    protected void execute() {
        if(collector.isStalled())
        {
            SmartDashboard.putNumber("Stall_Indicator", 100);
        }
    }
    


    @Override
    protected boolean isFinished() {
        if(System.nanoTime() - zeroTime >= maxCollectTime) {
            return true;
        }
        return hasFinished;
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
