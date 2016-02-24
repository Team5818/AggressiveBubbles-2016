package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.Arm;
import team5818.robot.modules.Collector;

public class ShootHigh extends Command {
    public static final double shootUpperVelocity = 240;
    public static final double shootLowerVelocity = 144;
    public static final double shootAngle = 60;
    
    private SetFlywheelVelocity setFlyVelocity;
    private SetArmAngle setArmAngle;
    private SetFlywheelPower flyToZero;
    
    private Collector collector;
    
    /**
     * The maximum time the shooter can be on in nano seconds.
     */
    private double maxShootTime = 4;
    
    public ShootHigh(){
        setFlyVelocity = new SetFlywheelVelocity(shootUpperVelocity,shootLowerVelocity);
        setArmAngle = new SetArmAngle(shootAngle);
        flyToZero = new SetFlywheelPower(0);
        collector = RobotCommon.runningRobot.collector;
    }
    
    
    @Override
    protected void initialize() {
        setArmAngle.start();
        setFlyVelocity.start();
        setTimeout(maxShootTime);
        //TODO make ShootHigh move arm to angle using PID.
    }

    @Override
    protected void execute() {
        if(setArmAngle.isFinished() && setFlyVelocity.isFinished()){
            collector.setPower(Collect.COLLECT_POWER);
        }
        
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }

    @Override
    protected void end() {
        collector.setPower(0);
        flyToZero.start();
    }

    @Override
    protected void interrupted() {
        setArmAngle.cancel();
        end();
    }

}
