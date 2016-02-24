package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.Arm;
import team5818.robot.modules.Collector;

public class ShootLow extends Command {
    public static final double shootAngle = 0;
    
    private SetArmAngle setArmAngle; 
    private Collector collector;

    
    /**
     * The maximum time the shooter can be on in nano seconds.
     */
    private double maxShootTime = 4;
    
    public ShootLow(){
        setArmAngle = new SetArmAngle(shootAngle);
        collector = RobotCommon.runningRobot.collector;
    }
    
    
    @Override
    protected void initialize() {
        setArmAngle.start();
        setTimeout(maxShootTime);
        //TODO make ShootHigh move arm to angle using PID.
    }

    @Override
    protected void execute() {
        if(setArmAngle.isFinished()){
            collector.setPower(-Collect.COLLECT_POWER);
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
        setArmAngle.cancel();
        end();
    }

}