package team5818.robot.commands;

import org.usfirst.frc.team5818.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.modules.Arm;
import team5818.robot.RobotCommon;

public class SetArmAngle extends Command {
    
    public double targetAngle;
    public Arm arm;
    
    public SetArmAngle(double angle){
        
        targetAngle = angle;
        arm = RobotCommon.runningRobot.arm;
    }
    
    @Override
    protected void initialize() {    
        arm.goToAngle(targetAngle);
    }

    @Override
    protected void execute() {
    
    }

    @Override
    protected boolean isFinished() {
        return arm.onTarget();
    }

    @Override
    protected void end() {
        arm.disablePID(); 
    }

    @Override
    protected void interrupted() {
        arm.disablePID();
    }

}
