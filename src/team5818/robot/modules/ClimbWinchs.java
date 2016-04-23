package team5818.robot.modules;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ClimbWinchs{
    
    private double scale = 1;
    
    private ClimbWinch left;
    private ClimbWinch right;

    public ClimbWinchs(int rightTalon, int leftTalon, boolean leftRev, boolean rightRev) {
        left = new ClimbWinch(leftTalon, leftRev);
        right = new ClimbWinch(rightTalon, rightRev);
        
    }
    
    public void updatePIDConstants() {
        left.updatePIDConstants();
        right.updatePIDConstants();
        
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
        setRPS(rps,rps);
    }
    
    public void setRPS(double l, double r) {
        left.setRPS(l);
        right.setRPS(r);
    }
    
    
    public ClimbWinch getLeft() {
        return left;
    }
    public ClimbWinch getRight() {
        return right;
    }

}
