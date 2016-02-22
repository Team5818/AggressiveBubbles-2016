package team5818.robot.modules;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANSpeedController.ControlMode;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import team5818.robot.RobotCommon;
import team5818.robot.RobotConstants;

/**
 * @author Petey Class to control 5818's robot arm
 */
public class Arm implements Module, PIDSource, PIDOutput {

    // TODO redesign arm to use encoder on final robot.
    protected static final double DEFAULT_SCALE = 0.047;
    protected static final double DEFAULT_OFFSET = -9.587;
    protected static final double DEFAULT_MAXPOWER = 0.8;
    
    private double scale;
    private double offset;
    private double maxPower;
    private double minPower = -maxPower;

    private static final AnalogInput armPotentiometer =
            new AnalogInput(RobotConstants.ARM_POTENTIOMETER_CHANNEL);
    private static final CANTalon firstArmMotor =
            new CANTalon(RobotConstants.TALON_FIRST_ARM_MOTOR);
    private static final CANTalon secondArmMotor =
            new CANTalon(RobotConstants.TALON_SECOND_ARM_MOTOR);

    private PIDController armPID =
            new PIDController(.008, .0005, 0, this, firstArmMotor);

    public Arm() {
        firstArmMotor.setInverted(true);
        if (secondArmMotor != null)
            secondArmMotor.setInverted(false);
        armPID.setOutputRange(minPower, maxPower);
        armPID.setAbsoluteTolerance(5);
    }

    @Override
    public void initModule() {
        try {
            scale = RobotCommon.runningRobot.prefs.getDouble("ArmPotScale", DEFAULT_SCALE);
            offset = RobotCommon.runningRobot.prefs.getDouble("ArmPotOffset",
                    DEFAULT_OFFSET);
            maxPower = RobotCommon.runningRobot.prefs.getDouble("MaxArmPower", .8);
        } catch(Exception e) {
            DriverStation.reportError("Could not get preferences from SmartDashboard.", false);
            scale = DEFAULT_SCALE;
            offset = DEFAULT_OFFSET;
            maxPower = DEFAULT_MAXPOWER;
        }
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
     * gives max power
     */
    public double getMaxPower() {
        return maxPower;
    }

    /**
     * @return angle measured by pot
     */

    public double getAngle() {
        double a1 = armPotentiometer.getValue() * scale;
        double aFinal = a1 + offset;
        return aFinal;
    }

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
            this.setPower(minPower / 3);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * returns error from PID controller
     * 
     * @return error
     */

    public double getError() {
        return armPID.getError();
    }

    /**
     * @param objectiveAngle
     *            PIDs to the given objective
     */
    public void goToAngle(double objectiveAngle) {

        armPID.reset();
        armPID.setSetpoint(objectiveAngle);
        armPID.enable();
    }

    /**
     * @return whether PID has reached target
     */
    public boolean onTarget() {
        boolean finished = armPID.onTarget();
        return finished;
    }

    /**
     * stops PID loop
     */
    public void disablePID() {
        armPID.disable();
    }

    @Override
    public void teleopPeriodicModule() {
    }

    @Override
    public void endModule() {
    }

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
        return getAngle();
    }

    @Override
    public void pidWrite(double output) {
        this.setPower(output);

    }

}