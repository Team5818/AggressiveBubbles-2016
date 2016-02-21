package team5818.robot.modules;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

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
public class FlyWheel implements PIDSource, PIDOutput {

    /**
     * The CANTalonWithPIDModes motor controller for the upper wheel.
     */
    private final CANTalon talon;

    /**
     * The gearbox ratio between the motor shaft to the wheel.
     */
    // TODO fix this number
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
    public static final double KP = 0.0001, KI = 0.0001, KD = 0.0001,
            KIZONE = 0.0001, KCLRR = 0.0001;

    private PIDController pid;

    /**
     * 
     * @param talon
     *            The upper CANTalonWithPIDModes motor on the flywheel
     * @param reversed
     *            Specifies weather to reverse the sensor and output of the
     *            motor.
     */
    public FlyWheel(CANTalon talon, boolean reversed) {
        pid = new PIDController(KP, KI, KD, 1.0 / MAX_VELOCITY, this, this);
        pid.setAbsoluteTolerance(0.05 * MAX_VELOCITY);
        this.talon = talon;
        this.inverted = reversed;
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
    public void setVelocity(double vel) {
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
        if (inverted)
            output *= -1;
        talon.set(output);
    }

    /**
     * Returns the Revolotions Per Second of the actual wheel.
     * 
     * @return Revolotions Per Second
     */
    public double getRPS() {

        double rps = talon.getEncVelocity() * 10.0 / 6.0 * gearBoxRatio;
        if (inverted)
            rps *= -1;
        return rps;
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
    public void pidWrite(double output) {
        set(output);

    }

}
