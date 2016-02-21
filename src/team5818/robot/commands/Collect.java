package team5818.robot.commands;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.RobotConstants;

public class Collect extends Command {

    public static final double MAX_COLLECT_POWER = 1;
    public static final double COLLECT_POWER = 0.6;
    
    private double endSequanceZeroTime;
    /**
     * Time to run the motor
     */
    //TODO tune the ballFeedOutTime number to the correct timing.
    private double ballFeedOutTime = 0.4 * 1E9;
    private double maxCollectTime = 4 * 1E9;
    private double zeroTime;
    
    private boolean hasStartedEndSequance = false;
    private boolean hasFinished = false;
    
    
    @Override
    protected void initialize() {
        RobotCommon.runningRobot.arm.setCollectorPower(COLLECT_POWER);
        zeroTime = System.nanoTime();
    }

    @Override
    protected void execute() {
        if(RobotCommon.runningRobot.arm.isCollectorStaling() || hasStartedEndSequance)
        {
            endSequance();
        }
    }
    
    private void endSequance()
    {
        if(!hasStartedEndSequance) {
            hasStartedEndSequance = true;
            RobotCommon.runningRobot.arm.setCollectorPower(-COLLECT_POWER/2);
            endSequanceZeroTime = System.nanoTime();
        } else {
            double deltaTime = System.nanoTime() - endSequanceZeroTime;
            if(deltaTime >= ballFeedOutTime) {
                RobotCommon.runningRobot.arm.setCollectorPower(0);
                hasFinished  = true;
            }
        }   
    }

    @Override
    protected boolean isFinished() {
        if(System.nanoTime() - zeroTime >= maxCollectTime) {
            end();
            return true;
        }
        return hasFinished;
    }

    @Override
    protected void end() {
        RobotCommon.runningRobot.arm.setCollectorPower(0);
    }

    @Override
    protected void interrupted() {
        end();
    }

}
