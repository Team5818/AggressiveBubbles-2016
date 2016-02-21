package team5818.robot.modules;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANSpeedController.ControlMode;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import team5818.robot.RobotConstants;

public class Arm implements Module, PIDSource, PIDOutput {

    // TODO redesign arm to use encoder on final robot.
    private static final AnalogInput armPotentiometer =
            new AnalogInput(RobotConstants.ARM_POTENTIOMETER_CHANNEL);
    private static final CANTalon firstArmMotor =
            new CANTalon(RobotConstants.TALON_FIRST_ARM_MOTOR);
    private static final CANTalon secondArmMotor =
            new CANTalon(RobotConstants.TALON_SECOND_ARM_MOTOR);

    private static final CANTalon collectorMotor =
            new CANTalon(RobotConstants.TALON_COLLECTOR_MOTOR);
    //TODO have the PID use both motors for arm.
    private PIDController armPID =
            new PIDController(.008, .0005, 0, this, firstArmMotor);

    private double power;
    private double maxPower = .5; // max and min power are for PID and aim
                                  // adjusts
    private double minPower = -.5;

    private double angle;
    /**
     * The current at which the collector motor will stall.
     */
    private double collectorStallCurrent = 41;

    public Arm() {
        // ARM_ENCODER.reset();
        // ARM_ENCODER.setDistancePerPulse(RobotConstants.ARM_ENCODER_SCALE); //
        // Angle per pulse in our case
        firstArmMotor.setInverted(true);
        if (secondArmMotor != null)
            secondArmMotor.setInverted(false);
        armPID.setOutputRange(minPower, maxPower);

    }

    /**
     * Sets power to arm motors
     * 
     * @param power
     *            The power value
     */
    public void setPower(double power) {
        armPID.disable();
        firstArmMotor.set(power);
        if (secondArmMotor != null) {
            secondArmMotor.set(power);
        }
    }

    /**
     * Sets power collector
     */
    public void setCollectorPower(double power) {
        collectorMotor.set(power);
    }

    /**
     * Checks weather the collector motor is stalling.
     * 
     * @return Returns true if collector motor is stalling
     */
    public boolean isCollectorStaling() {
        double threshold = collectorStallCurrent - 10;
        if (collectorMotor.getOutputCurrent() > threshold)
            return true;
        return false;
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
        double a1 = armPotentiometer.getValue()
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

    @Override
    public void pidWrite(double output) {
        this.setPower(output);

    }

}