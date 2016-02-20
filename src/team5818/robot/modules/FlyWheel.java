package team5818.robot.modules;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.command.PIDSubsystem;

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
public class FlyWheel extends PIDSubsystem {

    /**
     * The CANTalonWithPIDModes motor controller for the upper wheel.
     */
    private final CANTalon talon;
    
    /**
     * The gearbox ratio between the motor shaft to the wheel.
     */
    //TODO fix this number
    private double gearBoxRatio = 24.0 / 60;
    
    /**
     * Weather to invert the output and sensor output.
     */
    private boolean inverted = false;
    
    public static final double MAX_VELOCITY = 240;

    /**
     * The constants for the PID loop. kp = P constant ki = I constant kd = D
     * constant izone = Integration Zone for when to cut off the integral klrr =
     * Closed Loop Ramp Rate constant.
     */
    public static final double KP = 0, KI = 0, KD = 0, KIZONE = 0, KCLRR = 0;

    /**
     * 
     * @param talon
     *            The upper CANTalonWithPIDModes motor on the flywheel
     * @param reversed
     *            Specifies weather to reverse the sensor and output of the
     *            motor.
     */
    public FlyWheel(CANTalon talon, boolean reversed) {
        super(KP, KI, KD, 1.0 / MAX_VELOCITY);
        setAbsoluteTolerance(0.005);
        this.talon = talon;
        this.inverted = reversed;
    }
    
    public void setPID(double kp, double ki, double kd)
    {
        this.getPIDController().setPID(kp, ki, kd);
    }
    
    /**
     * Only works for when PID is enabled through enablePID method.
     * 
     * @param setVelocity
     *            the desired velocity
     */
    public void setVelocity(double vel) {
        setSetpoint(vel);
        DriverStation.reportError(vel + "\n", false);
        enable();
    }

    /**
     * Directly sets the power of the upper motor to the specified number.
     * Ranging from 1 to -1 having 1 cause the ball to move forward.
     * 
     * @param power
     *            the power desired to be set, from 1 to -1
     */
    public void setPower(double pow) {

        disable();
        if (pow > 1)
            pow = 1;
        if (pow < -1)
            pow = -1;
        set(pow);
        DriverStation.reportError(pow + "\n", false);
    }

    /**
     * Sets the power to the actual motor. This will invert the power
     * if it needs to be inverted.
     * @param output the desired value to be set to the motor.
     */
    private void set(double output)
    {
        if(inverted)
            output *= -1;
        talon.set(output);
    }
    
    /**
     * Returns the Revolotions Per Second of the actual wheel.
     * 
     * @return Revolotions Per Second
     */
    public double getRPS() {
        
        double ticks = talon.getEncVelocity();
        if(inverted)
            ticks *= -1;
        
        return ticks * 10.0 / 6.0 * gearBoxRatio;
    }

    @Override
    protected double returnPIDInput() {
        return getRPS();
    }

    @Override
    protected void usePIDOutput(double output) {
        set(output);
    }

    /**
     * Not implemented
     */
    @Override
    protected void initDefaultCommand() {
        
    }

}
