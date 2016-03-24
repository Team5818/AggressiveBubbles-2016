package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.Arm;

public class ArmPower extends Command{
    Arm arm = RobotCommon.runningRobot.arm;
    double armPower;
    double maxTime;

    public ArmPower(double power, double timeout){
        armPower = power;
        maxTime = timeout;
        
    }
    
    public ArmPower(double power){
        this(power, .5);
    }
    @Override
    protected void initialize() {
        arm.setPower(armPower);
        setTimeout(maxTime);
        
    }

    @Override
    protected void execute() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }

    @Override
    protected void end() {
        arm.setPower(0);
        
    }

    @Override
    protected void interrupted() {
        end();
        
    }
    

}
