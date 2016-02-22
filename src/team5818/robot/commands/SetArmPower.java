package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.Arm;

/**
 * Command that sets the power to the arm for manual control.
 *
 */
public class SetArmPower extends Command {
    
    private double power;
    public Arm arm;
    private static boolean hasInitialized = false;
    private double zeroTime;
    private double maxTime = 2 * 1E9;

    /**
     * @param power the desired power to set to the arm.
     */
    public SetArmPower(double power) {
        arm = RobotCommon.runningRobot.arm;
    }

    @Override
    protected void initialize() {
        arm.setPower(power);
        zeroTime = System.nanoTime();
    }

    @Override
    protected void execute() {
        if(!hasInitialized) {
            hasInitialized = true;
            initialize();
        }

    }

    @Override
    protected boolean isFinished() {
        if(System.nanoTime() - zeroTime > maxTime) {
            return true;
        }
        return true;
    }

    @Override
    protected void end() {
        hasInitialized  = false;
    }

    @Override
    protected void interrupted() {
        arm.setPower(0);
    }
    
    public static void reInit() {
        hasInitialized = false;
    }

}
