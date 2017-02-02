package team5818.robot.modules;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is the class that handles all the control on the motors directly through
 * the CANTalonWithPIDModes objects. It will also "set the speed using a PID
 * loop"(unavailable currently), show the actual speed, and be able to directly
 * set the power.
 * 
 * Any direct communication with the motor controllers related to the flywheel
 * should go through this class.
 *
 */
public class FlyWheel extends Subsystem implements PIDSource {

    public static double TOLERANCE = 10;

    /**
     * The CANTalonWithPIDModes motor controller for the upper wheel.
     */
    private final CANTalon talon;

    /**
     * The gearbox ratio between the motor shaft to the wheel.
     */
    private final double gearBoxRatio;
    private final double maxVelocity;

    public static final double MAX_VELOCITY_UPPER = 240;
    public static final double MAX_VELOCITY_LOWER = 167.5;
    public static final double SHOOT_VELOCITY_UPPER = 100;
    public static final double SHOOT_VELOCITY_LOWER = 140;
    
    /**
     * The constants for the PID loop. kp = P constant ki = I constant kd = D
     * constant izone = Integration Zone for when to cut off the integral klrr =
     * Closed Loop Ramp Rate constant.
     */
    private double Kp, Ki, Kd, Kf;
    
    private PIDController pid;
    
    private int sensorSign = 1;

    private boolean lowerFly;

    /**
     * 
     * @param talon
     *            The upper CANTalonWithPIDModes motor on the flywheel
     * @param reversed
     *            Specifies weather to reverse the sensor and output of the
     *            motor.
     * @param gearRatio
     *            The gearRatio on the flywheel. output/input
     */
    public FlyWheel(CANTalon talon, double gearRatio, double maxVel, boolean lowerFly,
            boolean reversed) {
        gearBoxRatio = gearRatio;
        maxVelocity = maxVel;
        this.lowerFly = lowerFly;
        pid = new PIDController(0, 0, 0, 0, this, talon);
        updatePIDConstants();
        pid.setAbsoluteTolerance(TOLERANCE);
        this.talon = talon;
        talon.setInverted(reversed);
        //reverseSensor(reversed);
    }
    
    public void updatePIDConstants() {
        if(lowerFly) {
            Kp = Preferences.getInstance().getDouble("FlyLowerKp", 0.01);
            Ki = Preferences.getInstance().getDouble("FlyLowerKi", 0.0001);
            Kd = Preferences.getInstance().getDouble("FlyLowerKd", 0.001);
            Kf = 1.0/MAX_VELOCITY_LOWER;
        } else {
            Kp = Preferences.getInstance().getDouble("FlyUpperKp", 0.01);
            Ki = Preferences.getInstance().getDouble("FlyUpperKi", 0.0001);
            Kd = Preferences.getInstance().getDouble("FlyUpperKd", 0.001);
            Kf = 1.0/MAX_VELOCITY_UPPER;
        }
        pid.setPID(Kp, Ki, Kd, Kf);
    }

    private void reverseSensor(boolean reversed) {
        sensorSign  = 1;
        if(reversed)
            sensorSign = -1;
        
    }

    public void setPID(double kp, double ki, double kd) {
        pid.setPID(kp, ki, kd);
    }

    /**
     * Only works for when PID is enabled through enablePID method.
     * 
     * @param setVelocity
     *            the desired velocity
     */
    public void setVelocity(double 
            vel) {
        //DriverStation.reportError("" + vel, false);
        if(!pid.isEnabled() && pid.getSetpoint() != 0)
            pid.reset();
        pid.setSetpoint(vel);
        pid.enable();
    }

    /**
     * Directly sets the power of the upper motor to the specified number.
     * Ranging from 1 to -1 having 1 cause the ball to move forward.
     * 
     * @param power
     *            the power desired to be set, from 1 to -1
     */
    public void setPower(double pow) {

        pid.disable();
        set(pow);
    }

    /**
     * Sets the power to the actual motor. This will invert the power if it
     * needs to be inverted.
     * 
     * @param output
     *            the desired value to be set to the motor.
     */
    private void set(double output) {
        talon.set(output);
    }

    /**
     * Returns the Revolotions Per Second of the actual wheel.
     * 
     * @return Revolotions Per Second
     */
    public double getRPS() {
        try {
//            double rps = talon.getEncVelocity() * 10.0 / 6.0 * gearBoxRatio * sensorSign;
//            return rps;
        } catch (Exception e) {
//                DriverStation.reportError("Couldn't print flywheel velocity", false);
        }
        return 0;
    }

    public CANTalon getTalon() {
        return talon;
    }
    public PIDController getPIDController() {
        return pid;
    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return PIDSourceType.kRate;
    }

    @Override
    public double pidGet() {
        return getRPS();
    }

    @Override
    protected void initDefaultCommand() {
        // TODO Auto-generated method stub
        
    }

}
