package team5818.robot.modules;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import team5818.robot.RobotConstants;

public class Arm implements Module, PIDSource {

    private static final AnalogInput ARM_POTENTIOMETER =
            new AnalogInput(RobotConstants.ARM_POTENTIOMETER_CHANNEL);
    private static final CANTalon ARM_MOTOR =
            new CANTalon(RobotConstants.TALON_ARM_MOTOR);

    private static final CANTalon COLLECTOR_MOTOR =
            new CANTalon(RobotConstants.TALON_COLLECTOR_MOTOR);
    private PIDController armPID =
            new PIDController(.008, .0005, 0, this, ARM_MOTOR);

    private double power;
    private double maxPower = .5; // max and min power are for PID and aim
                                  // adjusts
    private double minPower = -.5;

    private double angle;

    public Arm() {
        // ARM_ENCODER.reset();
        // ARM_ENCODER.setDistancePerPulse(RobotConstants.ARM_ENCODER_SCALE); //
        // Angle per pulse in our case
        ARM_MOTOR.setInverted(true);
        armPID.setOutputRange(minPower, maxPower);

    }

    /**
     * Sets power of arm, keeping arm power within max and minimum parameters .5
     * and -.5
     * 
     * @param power
     *            - power value
     */

    public void setPower(double power) {
        armPID.disable();
        ARM_MOTOR.set(power);
    }

    /**
     * Sets power collector
     */

    public void setCollectorPower(double power) {
        COLLECTOR_MOTOR.set(power);
    }

    /**
     * Gets power of arm
     * 
     * @return power of arm
     */

    public double getPower() {
        return this.power;
    }

    /**
     * @return angle measured by encoder
     */
    public double getPotentiometerVal() {
        double a1 = ARM_POTENTIOMETER.getValue()
                * RobotConstants.ARM_POTENTIOMETER_SCALE;
        double aFinal = a1 + RobotConstants.ARM_POTENTIOMETER_INTERCEPT;
        return aFinal;
    }

    /**
     * Sets arm angle
     * 
     * @param y
     *            - angle value
     */

    public void setAngle(double y) {
        this.angle = y;
    }

    /**
     * Gets arm angle
     * 
     * @return arm angle
     */

    public double getAngle() {

        return this.angle;
    }

    /**
     * sets encoder val to zero
     */
    // public void resetEncoder(){
    // ARM_ENCODER.reset();
    // }

    /**
     * @param up
     *            moves arm slightly up or down
     */
    public void aimAdjust(Boolean up) {
        if (up) {
            this.setPower(maxPower);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (!up) {
            this.setPower(minPower);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * don't know what this is supposed to do
     */
    public void stabilize() {

    }

    /**
     * returns error from PID controller
     */

    public double getError() {
        return armPID.getError();
    }

    /**
     * @param objective
     *            PIDs to the given objective
     */
    public void goToAngle(double objectiveAngle) {

        armPID.reset();
        armPID.setSetpoint(objectiveAngle);
        armPID.enable();
    }

    public boolean onTarget() {
        return armPID.onTarget();
    }

    public void disablePID() {
        armPID.disable();
    }

    @Override
    public void initModule() {
    }

    @Override
    public void teleopPeriodicModule() {
    }

    @Override
    public void endModule() {
    }

    @Override
    public void initTest() {

    }

    @Override
    public void initTeleop() {

    }

    @Override
    public void initAutonomous() {

    }

    @Override
    public void testPeriodic() {

    }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {

    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return PIDSourceType.kDisplacement;
    }

    @Override
    public double pidGet() {
        return getPotentiometerVal();
    }

}