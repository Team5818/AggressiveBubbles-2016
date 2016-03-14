package team5818.robot.modules;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.RobotConstants;

public class Collector extends Subsystem implements Module {
    
    private static final CANTalon collectorMotor = new CANTalon(RobotConstants.TALON_COLLECTOR_MOTOR);
    private static final double STALL_CURRENT = 41;
    
    public Collector(boolean inverted){
        collectorMotor.setInverted(inverted);
    }
    
    public void setPower(double power){
        collectorMotor.set(power);
    }
    
    public boolean isStalled(){
        boolean stalled = false;
        if(collectorMotor.isAlive()) {
            double current = collectorMotor.getOutputCurrent();
            if(current >= STALL_CURRENT - 10){
                stalled = true;
            }
        }
        return stalled;
    }
    
    @Override
    public void initModule() {
    }

    @Override
    public void teleopPeriodicModule() {
        int status = 0;
        if (isStalled()) {
            status = 100;
        }
        SmartDashboard.putNumber("Stall_Indicator", status);
    }

    @Override
    protected void initDefaultCommand() {
        
        
    }    
}