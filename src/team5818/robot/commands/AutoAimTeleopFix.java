package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.RobotCommon;
import team5818.robot.RobotConstants;
import team5818.robot.modules.FlyWheel;
import team5818.robot.util.LinearLookupTable;

public class AutoAimTeleopFix extends CommandGroup {


    AutoAim autoAim;
    public AutoAimTeleopFix(double xOffset, double yOffset, double flyup, double flylo, double timeout) {
        
        autoAim = new AutoAim(xOffset, yOffset, flyup, flylo, timeout);
        this.addSequential(new DoNothingAuto(timeout));
        this.addSequential(autoAim);
    }

    public AutoAimTeleopFix(double xOffset, double yOffset, double timeout) {
        this(xOffset, yOffset, FlyWheel.SHOOT_VELOCITY_LOWER, FlyWheel.SHOOT_VELOCITY_LOWER, timeout);
    }
    
    public AutoAimTeleopFix(double xOffset, double yOffset) {
        this(xOffset, yOffset, 2);
    }

    public AutoAimTeleopFix() {
        this(0, 0);
    }
    
    public void pidX() {
        autoAim.pidX();
    }
    
    public void pidY() {
        autoAim.pidX();
    }
    
    public void setYOffset(double of) {
        autoAim.setYOffset(of);
    }
    
    public void setXOffset(double of) {
        autoAim.setYOffset(of);
    }
    
    
}
