package team5818.robot.modules;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Preferences;
import team5818.robot.util.BetterPIDController;

public class ClimbWinch implements PIDSource, PIDOutput {

    public static double MAX_VELOCITY = 375;
    private final CANTalon talon;
    private final double scale;
    private final double sign;
    private BetterPIDController pid;

    public ClimbWinch(int tal, boolean rev) {
        this.talon = new CANTalon(tal);
        talon.setInverted(rev);
        if(rev)
            sign = -1;
        else
            sign = 1;
        //scale  = Preferences.getInstance().getDouble("ClimbEncScale", 1);
        scale = 1;
        pid = new BetterPIDController(0.0001, 0, 0, 1/MAX_VELOCITY, this, this);
        updatePIDConstants();
        talon.setPosition(0);
    }
    
    public void updatePIDConstants() {
        double kp = Preferences.getInstance().getDouble("WinchKp", 0.000001);
        double ki = Preferences.getInstance().getDouble("WinchKi", 0);
        double kd = Preferences.getInstance().getDouble("WinchKd", 0);
        pid.setPID(kp, ki, kd, 1.0/MAX_VELOCITY);
    }
    
    public void setPower(double output) {
        if(pid.isEnabled()) {
            pid.disable();
            pid.reset();
        }
        pidWrite(output);
    }
    
    public void setRPS(double rps) {
        pid.setSetpoint(rps);
        pid.enable();
    }
    

    @Override
    public void pidWrite(double output) {
        talon.set(output);
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
        // TODO Auto-generated method stub
        
    }
    
    //@Deprecated
    public double getRPS() {
        double rps = talon.getEncVelocity() * scale * sign;
        return 0;
    }
    
    public CANTalon getTalon() {
        return talon;
    }

    @Override
    public double pidGet() {
        // TODO Auto-generated method stub
        return getRPS();
    }
    
    @Override
    public PIDSourceType getPIDSourceType() {
        return PIDSourceType.kRate;
    }

}
