package team5818.robot.modules;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ClimbWinchs implements Module {
    
    private double scale = 1;
    
    private ClimbWinch left;
    private ClimbWinch right;

    public ClimbWinchs(int rightTalon, int leftTalon, boolean leftRev, boolean rightRev) {
        left = new ClimbWinch(leftTalon, leftRev);
        right = new ClimbWinch(rightTalon, rightRev);
        
    }
    
    /**
     * 
     * @param pow power for both winches
     */
    private void set(double pow) {
        set(pow, pow);
    }
    
    /**
     * 
     * @param l left winch power
     * @param r right winch power
     */
    private void set(double l, double r) {
        left.setPower(l);
        right.setPower(r);
    }
    
    /**
     * 
     * @param pow power for both winches.
     */
    public void setPower(double pow) {
        setPower(pow, pow);
    }
    
    /**
     * 
     * @param l left arm power
     * @param r right arm power
     */
    public void setPower(double l, double r) {
        set(l, r);
    }
    
    public void setRPS(double rps) {
        left.setRPS(rps);
        right.setRPS(rps);
    }
    
    @Override
    public void teleopPeriodicModule() {
        SmartDashboard.putNumber("Left Winch RPS", left.getRPS());
        SmartDashboard.putNumber("Right Winch RPS", right.getRPS());
        
    }

    @Override
    public void initModule() {
        // TODO Auto-generated method stub
        
    }

}
