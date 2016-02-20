package team5818.robot.modules;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.RobotConstants;

/**
 * This is the shooter class that controls the motion of the shooter. It
 * commands the flywheel object that controls the flywheel motors.
 * 
 * This class should be the only class that communicates with the flywheel. Any
 * other commands or communication to the flywheel motors should come through
 * this class and this class only.
 *
 */
public class Shooter implements Module {

    /**
     * The Talon motor controller class that controls the upper fly wheel in the
     * shooter mechanism.
     */
    private CANTalon talonU =
            new CANTalon(RobotConstants.TALON_FLYWHEEL_UPPER);

    /**
     * The Talon motor controller class that controls the upper fly wheel in the
     * shooter mechanism.
     */
    private CANTalon talonL =
            new CANTalon(RobotConstants.TALON_FLYWHEEL_LOWER);

    /**
     * The number formatter for the outputting numbers as strings.
     */
    NumberFormat formatter = new DecimalFormat("#0.00");

    /**
     * The flywheel object that controls the motion of the flywheel.
     */
    private final FlyWheel wheelU, wheelL;

    /**
     * Initiates the flyWheel object to the correct talons so that it can be
     * controlled.
     */
    public Shooter() {

        wheelU = new FlyWheel(talonU, false);
        wheelL = new FlyWheel(talonL, true);
        
    }

    @Override
    public void initModule() {
        LiveWindow.addActuator("Lower Flywheel", "PID", wheelL.getPIDController());
        LiveWindow.addActuator("Lower Flywheel", "Talon", talonL);
    }

    @Override
    public void teleopPeriodicModule() {
        
    }

    @Override
    public void endModule() {

    }
    
    public void setPID(double kp, double ki, double kd)
    {
        //wheelU.setPID(kp, ki, kd);
        wheelL.setPID(kp, ki, kd);   
    }
    
    //TODO figure this \/ out later.
    /**
     * 
     * @param ratio
     */
    public void setBackspinRatio(double ratio)
    {
        
    }
    
    /**
     * Calls the set velocity method that uses PID for the flywheel.
     * Sets the power to the lower and upper wheels with the backspin ratio.
     * @param vel the desired Velocity
     */
    public void setFlywheelVelocity(double vel)
    {
        //wheelU.setVelocity(vel);
        wheelL.setVelocity(vel);
    }
    
    /**
     * Calls the set power method for the flywheel.
     * Sets the power to the lower and upper wheels with the backspin ratio.
     * @param vel the desired Velocity
     */
    public void setFlywheelPower(double pow)
    {
        //wheelU.setPower(pow);
        wheelL.setPower(pow);
    }
    
    public FlyWheel getUpperFlywheel()
    {
        return wheelU;
    }
    
    public FlyWheel getLowerFlywheel()
    {
        return wheelL;
    }

    @Override
    public void initTest() {
        
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
    public void testPeriodic() {
        // TODO Auto-generated method stub
        
    }

}
