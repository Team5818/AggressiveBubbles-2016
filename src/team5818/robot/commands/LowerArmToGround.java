package team5818.robot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.Arm;

public class LowerArmToGround extends Command{
    
    double armPower = -.6;
    double stallBuffer = 0;
    double currentAngle;
    double lastAngle;
    
    private Arm arm;
    private boolean hasInitialized = false;
    private double maxTime = 3;
    
    @Override
    protected void initialize() {
        arm = RobotCommon.runningRobot.arm;
        hasInitialized = true;
        lastAngle = 2000;
        currentAngle = 1000;
        setTimeout(maxTime);
    }

    @Override
    protected void execute() {
    arm.setPower(armPower);
    lastAngle = currentAngle;
    currentAngle = arm.getAngle();
    try {
        Thread.sleep(100);
    } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    }

    @Override
    protected boolean isFinished() {
         if((currentAngle - lastAngle > stallBuffer) || isTimedOut()){
             return true;
         }
    return false;     
    }

    @Override
    protected void end() {
        arm.setPower(0);     
    }

    @Override
    protected void interrupted() {
         arm.setPower(0);        
    }

}
