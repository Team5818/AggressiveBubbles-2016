package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.Arm;
import team5818.robot.modules.FlyWheel;


/**
 * Command that sets the power to the flywheel for cancelling PID.
 *
 */
public class SetFlywheelPower extends Command {
    
    private double power;
    private static  FlyWheel flyUp;
    private static  FlyWheel flyLo;
    private static boolean hasInitialized = false;
    private double maxTime = 1;

    /**
     * @param power the desired power to set to the arm.
     */
    public SetFlywheelPower(double power) {
        flyUp = RobotCommon.runningRobot.upperFlywheel;
        flyLo = RobotCommon.runningRobot.lowerFlywheel;
        requires(flyUp);
        requires(flyLo);
        this.power = power;
    }

    @Override
    protected void initialize() {
        flyUp.setPower(power);
        flyLo.setPower(power);
        hasInitialized = true;
        setTimeout(maxTime);
        
    }

    @Override
    protected void execute() {
        if(!hasInitialized) {
            initialize();
        }

    }

    @Override
    protected boolean isFinished() {
       return isTimedOut(); 
    }

    @Override
    protected void end() {
        hasInitialized  = false;
    }

    @Override
    protected void interrupted() {
        flyUp.setPower(0);
        flyLo.setPower(0);
    }
    
    public static void reInit() {
        hasInitialized = false;
    }

}
