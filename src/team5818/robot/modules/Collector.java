package team5818.robot.modules;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Subsystem;
import team5818.robot.RobotConstants;

public class Collector extends Subsystem implements Module {
    
    private static final CANTalon collectorMotor = new CANTalon(RobotConstants.TALON_COLLECTOR_MOTOR);
    private static final double STALL_THRESHOLD = 41;
    
    public Collector(boolean inverted){
        collectorMotor.setInverted(inverted);
    }
    
    public void setPower(double power){
        collectorMotor.set(power);
    }
    
    public boolean isStalled(){
        boolean stalled = false;
        double current = collectorMotor.getOutputCurrent();
        if(current >= STALL_THRESHOLD - 10){
            stalled = true;
        }
        return stalled;
    }
    
    @Override
    public void initModule() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void initTest() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void initTeleop() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void initAutonomous() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void teleopPeriodicModule() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void testPeriodic() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void endModule() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void initDefaultCommand() {
        
        
    }    
}