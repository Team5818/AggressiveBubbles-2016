package team5818.robot.modules;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.command.Subsystem;
import team5818.robot.RobotConstants;

public class RopeClimber extends Subsystem{
    
    private CANTalon leftMotor;
    private CANTalon rightMotor;
    
    public RopeClimber(){
        leftMotor = new CANTalon(RobotConstants.TALON_ROPE_LEFT);
        leftMotor.setInverted(true);
        rightMotor = new CANTalon(RobotConstants.TALON_ROPE_RIGHT);
        rightMotor.setInverted(false);

    }
    
    public void setPower(double pow){
        leftMotor.set(pow);
        rightMotor.set(pow);
    }
    @Override
    protected void initDefaultCommand() {}

}
