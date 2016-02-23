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
    private double zeroTime;
    private double maxTime = 3 * 1E9;
    
    @Override
    protected void initialize() {
        arm = RobotCommon.runningRobot.arm;
        zeroTime = System.nanoTime();
        hasInitialized = true;
        goToCollectAngle.start();
        currentAngle = 0;
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
        if(System.nanoTime() - zeroTime > maxTime){
            return true;
        }    
        if(arm.getAngle() <= groundAngle){
            return true;
        }
        if(currentAngle - lastAngle > stallBuffer){
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
