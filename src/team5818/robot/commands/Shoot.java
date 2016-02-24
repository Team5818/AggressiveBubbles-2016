package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.Collector;

public class Shoot extends Command{
    public double shootUpperVelocity;
    public double shootLowerVelocity;
    public double shootAngle;
    
    private SetFlywheelVelocity setFlyVelocity;
    private SetArmAngle setArmAngle;
    private SetFlywheelPower flyToZero;
    
    private Collector collector = RobotCommon.runningRobot.collector;
    
    /**
     * The maximum time the shooter can be on in nano seconds.
     */
    private double maxShootTime = 4;
    
    public Shoot(double angle, double flyUpVel, double flyLoVel){
        shootUpperVelocity = flyUpVel;
        shootLowerVelocity = flyLoVel;
        shootAngle = flyUpVel;
        setFlyVelocity = new SetFlywheelVelocity(shootUpperVelocity,shootLowerVelocity);
        setArmAngle = new SetArmAngle(shootAngle);
        flyToZero = new SetFlywheelPower(0);
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
