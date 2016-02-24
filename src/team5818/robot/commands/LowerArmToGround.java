package team5818.robot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.Arm;

public class LowerArmToGround extends Command{
    
    double collectAngle = Preferences.getInstance().getDouble("ArmCollectAngle",-6);
    double groundAngle = Preferences.getInstance().getDouble("ArmGroundAngle", -20);
    double armPower = Preferences.getInstance().getDouble("MaxArmPower",.8);
    double stallBuffer = -.2;
    double currentAngle;
    double lastAngle;
    
    private Arm arm;
    SetArmAngle goToCollectAngle = new SetArmAngle(collectAngle);
    private boolean hasInitialized = false;
    private double maxTime = 3;
    
    @Override
    protected void initialize() {
        arm = RobotCommon.runningRobot.arm;
        hasInitialized = true;
        goToCollectAngle.start();
        currentAngle = 0;
        setTimeout(maxTime);
    }

    @Override
    protected void execute() {
        lastAngle = currentAngle;
        currentAngle = arm.getAngle();
        if(goToCollectAngle.isFinished()){
            arm.setPower(-armPower);
        }
        
    }

    @Override
    protected boolean isFinished() {
        return (arm.getAngle() <= groundAngle || currentAngle - lastAngle > stallBuffer || isTimedOut());

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
