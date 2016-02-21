package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.Arm;

public class ShootHigh extends Command {
    public static final double shootVelocity = 144;
    
    private SetFlyWheelVelocity setFlyVelocity;
    private Arm arm;
    
    /**
     * The time when the command starts in nano seconds.
     * Retrieved using System.nanoTime();
     */
    private double zeroTime;
    
    /**
     * The maximum time the shooter can be on in nano seconds.
     */
    private double maxShootTime = 4 * 1E9;
    
    public ShootHigh(SetFlyWheelVelocity sfv) {
        setFlyVelocity = sfv;
        arm = RobotCommon.runningRobot.arm;
    }
    
    public ShootHigh()
    {
        this(new SetFlyWheelVelocity());
        
    }
    
    @Override
    protected void initialize() {
        setFlyVelocity.setVelocity(shootVelocity);
        arm.setCollectorPower(Collect.COLLECT_POWER);
        zeroTime = System.nanoTime();
        //TODO make ShootHigh move arm to angle using PID.
    }

    @Override
    protected void execute() {

    }

    @Override
    protected boolean isFinished() {
        if(System.nanoTime() - zeroTime >= maxShootTime)
            return true;
        return false;
    }

    @Override
    protected void end() {
        setFlyVelocity.cancel();
        

    }

    @Override
    protected void interrupted() {
        end();
    }

}
