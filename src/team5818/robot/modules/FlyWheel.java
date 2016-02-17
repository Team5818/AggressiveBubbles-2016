package team5818.robot.modules;

import edu.wpi.first.wpilibj.PIDController;
import team5818.robot.util.CANTalonWithPIDModes;

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
public class FlyWheel implements Module {

    /**
     * The CANTalonWithPIDModes motor controller for the upper wheel.
     */
    private final CANTalonWithPIDModes talon;

    private PIDController pid;

    /**
     * Weather the flywheel is running or not.
     */
    private boolean running = true;

    /**
     * The current set speed for the upper flywheel.
     */
    private double power = 0;

    /**
     * The gearbox ratio between the motor shaft to the wheel.
     */
    private double gearBoxRatio = 24 / 60;

    /**
     * Weather to use PID on the CANTalonWithPIDModes or use setPower.
     */
    private boolean usingPID = false;

    /**
     * The velocity set to have the PID loop use as it's setPoint.
     */
    private double setVelocity = 0;

    /**
     * The constants for the PID loop. kp = P constant ki = I constant kd = D
     * constant izone = Integration Zone for when to cut off the integral klrr =
     * Closed Loop Ramp Rate constant.
     */
    private double kp, ki, kd, kizone, kclrr;

    /**
     * 
     * @param talon
     *            The upper CANTalonWithPIDModes motor on the flywheel
     * @param reversed
     *            Specifies weather to reverse the sensor and output of the
     *            motor.
     */
    public FlyWheel(CANTalonWithPIDModes talon, boolean usingPID,
            boolean reversed) {

        this.talon = talon;

        // TODO tune the following values.
        kp = 1;
        ki = 1;
        kd = 1;
        kizone = 1;
        kclrr = 1;

        pid = new PIDController(kp, ki, kd, talon, talon);

        if (usingPID)
            enablePID();
        else
            disablePID();

        // TODO doesn't actually work, not sure why. Make it work.
        talon.reverseOutput(reversed);
        // TODO doesn't actually work, not sure why. Make it work.
        talon.reverseSensor(reversed);
    }

    /**
     * This is the initialization method for the Shooter. This initializes the
     * flywheel motor controllers to the correct channels.
     */
    @Override
    public void initModule() {

    }

    @Override
    public void teleopPeriodicModule() {

        if (running) {

            if (!usingPID)
                talon.set(power);

        } else {

            talon.set(0);
        }
    }

    @Override
    public void endModule() {
        stop();
        disablePID();
        talon.disableControl();
    }

    /**
     * Sets the running state of the flywheel to true. This will not set change
     * the speed, it will only set an indicator for the program loop to take
     * care of.
     */
    public void start() {

        running = true;
        enablePID();
    }

    /**
     * Sets the running state of the flywheel to false. This will not set change
     * the speed, it will only set an indicator for the program loop to take
     * care of.
     */
    public void stop() {

        running = false;
        disablePID();
        setSetVelocity(0);
    }

    /**
     * enables all dependences for using a PID loop.
     */
    public void enablePID() {
        if (!usingPID) {
            usingPID = true;
            pid.enable();

        }
    }

    /**
     * disables all dependences when in PID mode so that you can safely use
     * directly set the power.
     */
    public void disablePID() {
        if (usingPID) {

            usingPID = false;
            pid.disable();
        }
    }

    /**
     * Only works for when PID is enabled through enablePID method.
     * 
     * @param setVelocity
     *            the desired velocity
     * @return returns weather it actually set the velocity, or it didn't
     *         because PID is not enabled.
     */
    public void setSetVelocity(double setVelocity) {
        enablePID();
        pid.setSetpoint(setVelocity);

    }

    /**
     * @return The current desired velocity from the PID loop.
     */
    public double getSetVelocity() {
        return setVelocity;
    }

    /**
     * Directly sets the power of the upper motor to the specified number.
     * Ranging from 1 to -1 having 1 cause the ball to move forward.
     * 
     * @param power
     *            the power desired to be set, from 1 to -1
     */
    public void setPower(double power) {

        disablePID();

        if (power > 1)
            power = 1;
        if (power < -1)
            power = -1;
        this.power = power;
    }

    /**
     * Returns the power to be set to the upper flywheel.
     * 
     * @return The power of the upper fly wheel.
     */
    public double getPower() {

        return power;
    }

    /**
     * Returns the Revolotions Per Second of the actual wheel.
     * 
     * @return Revolotions Per Second
     */
    public double getRPS() {

        return talon.getEncVelocity() * 10 / 6 * gearBoxRatio;
    }

}
