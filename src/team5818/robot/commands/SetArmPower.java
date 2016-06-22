package team5818.robot.commands;

import org.usfirst.frc.team5818.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.modules.Arm;

/**
 * Command that sets the power to the arm for manual control.
 *
 */
public class SetArmPower extends Command {
    
    private double power;
    public Arm arm;
    private static boolean hasInitialized = false;

    /**
     * @param power the desired power to set to the arm.
     */
    public SetArmPower(double power) {
        arm = Robot.runningRobot.arm;
        requires(arm);
    }

    @Override
    protected void initialize() {
        arm.setPower(power);
        hasInitialized = true;
    }

    @Override
    protected void execute() {
        if(!hasInitialized) {
            initialize();
        }

    }

    @Override
    protected boolean isFinished() {
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
