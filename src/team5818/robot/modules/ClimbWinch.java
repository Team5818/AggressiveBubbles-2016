package team5818.robot.modules;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Preferences;

public class ClimbWinch implements PIDSource, PIDOutput {

    private final CANTalon talon;
    private final double scale;

    public ClimbWinch(int tal, boolean rev) {
        this.talon = new CANTalon(tal);
        talon.setInverted(rev);
        scale  = Preferences.getInstance().getDouble("ClimbEncScale", 1);
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
