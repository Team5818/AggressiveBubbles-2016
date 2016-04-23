package team5818.robot.modules;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Preferences;

public class ClimbWinch implements PIDSource, PIDOutput {

    private final CANTalon talon;
    private final double scale;
    private PIDController pid;

    public ClimbWinch(int tal, boolean rev) {
        this.talon = new CANTalon(tal);
        talon.setInverted(rev);
        scale  = Preferences.getInstance().getDouble("ClimbEncScale", 1);
        double kp = Preferences.getInstance().getDouble("WinchKp", 0.000001);
        double ki = Preferences.getInstance().getDouble("WinchKi", 0);
        double kd = Preferences.getInstance().getDouble("WinchKd", 0);
        double kf = Preferences.getInstance().getDouble("WInchKf", 0);
        pid = new PIDController(kp, ki, kd, kf, this, this, 50);
    }
    
    public void setPower(double output) {
        pidWrite(output);
    }
    
    public void setRPS(double rps) {
        //talon.set(output);
    }
    

    @Override
    public void pidWrite(double output) {
        talon.set(output);
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
        // TODO Auto-generated method stub
        
    }
    
    public double getRPS() {
        double rps = talon.getEncVelocity() * scale;
        return rps;
    }

    @Override
    public double pidGet() {
        // TODO Auto-generated method stub
        return getRPS();
    }
    
    @Override
    public PIDSourceType getPIDSourceType() {
        // TODO Auto-generated method stub
        return null;
    }

}
