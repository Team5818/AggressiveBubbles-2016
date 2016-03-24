package team5818.robot.modules;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANSpeedController.ControlMode;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.RobotCommon;
import team5818.robot.RobotConstants;
import team5818.robot.commands.SetArmAngle;
import team5818.robot.commands.SetArmPower;

/**
 * Class to control 5818's robot arm
 */
public class Arm extends Subsystem implements Module, PIDSource, PIDOutput {
    
    public double teamNum =  Preferences.getInstance().getDouble("TeamNumber", 1717);
    protected static final double DEFAULT_SCALE = 0.047;
    protected static final double DEFAULT_OFFSET = -9.587;
    protected static final double DEFAULT_MAXPOWER = 0.8;
    protected static final double DEFAULT_MINPOWER = -.4;
    protected static final double DEFAULT_KP = 0.01;
    protected static final double DEFAULT_KI = 0.0;
    protected static final double DEFAULT_KD = 0.0;

    private double scale;
    private double offset;
    private double maxPower;
    private double minPower;
    private double armMotorRatio = 1;
    private double armPowerIdle = 0.1;
    private boolean pidMode = false;

    private static final AnalogInput armPotentiometer =
            new AnalogInput(RobotConstants.ARM_POTENTIOMETER_CHANNEL);
    private static final CANTalon firstArmMotor =
            new CANTalon(RobotConstants.TALON_FIRST_ARM_MOTOR);
    private static CANTalon secondArmMotor =
            new CANTalon(RobotConstants.TALON_SECOND_ARM_MOTOR);

    private PIDController armPID;
    private double armPowerIdleRatio = 1;
    private double armAngleGround = -7;

    public Arm() {
        if (secondArmMotor != null)
            secondArmMotor.setInverted(true);
    }

    @Override
    public void initModule() {
        double kp, ki, kd;
        try {
            scale = Preferences.getInstance().getDouble("ArmPotScale",
                    DEFAULT_SCALE);
            offset = Preferences.getInstance().getDouble("ArmPotOffset",
                    DEFAULT_OFFSET);
            armAngleGround  = Preferences.getInstance().getDouble("ArmAngleGround", -7);
            maxPower = Preferences.getInstance().getDouble("ArmMaxPower", DEFAULT_MAXPOWER);
            minPower = Preferences.getInstance().getDouble("ArmMinPower", DEFAULT_MINPOWER);
            armMotorRatio = Preferences.getInstance().getDouble("ArmMotorRatio", armMotorRatio);
            armPowerIdle = Preferences.getInstance().getDouble("ArmPowerIdle", armPowerIdle);
            armPowerIdleRatio  = Preferences.getInstance().getDouble("ArmPowerIdleRatio", armPowerIdleRatio);
            kp = Preferences.getInstance().getDouble("ArmKp", DEFAULT_KP);//remove .'s
            ki = Preferences.getInstance().getDouble("ArmKi", DEFAULT_KI);
            kd = Preferences.getInstance().getDouble("ArmKd", DEFAULT_KD);

        } catch (Exception e) {
            DriverStation.reportError(
                    "Could not get preferences from SmartDashboard.\n", false);
            scale = DEFAULT_SCALE;
            offset = DEFAULT_OFFSET;
            maxPower = DEFAULT_MAXPOWER;
            kp = DEFAULT_KP;
            ki = DEFAULT_KI;
            kd = DEFAULT_KD;
        }
        armPID = new PIDController(kp, ki, kd, this, this);
        armPID.setOutputRange(-maxPower, maxPower);
        armPID.setAbsoluteTolerance(10);
        LiveWindow.addActuator("Arm", "PID Controller", armPID);
    }
    
    @Override
    public void teleopPeriodicModule() {
        SmartDashboard.putNumber("Potentiometer Angle", getAngle());
        SmartDashboard.putNumber("Potentiometer Raw", getRawPot());
        if(getAngle() <= armAngleGround) {
            SmartDashboard.putNumber("Arm Lowest", 100);
        } else {
            SmartDashboard.putNumber("Arm Lowest", 0);
        }
    }

    /**
     * Sets power to arm motors
     * 
     * @param power
     *            The power value
     */
    public synchronized void setPower(double power) {
        firstArmMotor.enableBrakeMode(true);
        if(secondArmMotor != null);
            secondArmMotor.enableBrakeMode(true);
        pidMode = false;
        armPID.disable();
        pidWrite(power);
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
    
    public double getRawPot() {
        return armPotentiometer.getValue();
    }

    public synchronized boolean getPIDMode() {
        return pidMode;
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
            this.setPower(-maxPower / 3);
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
    public synchronized void goToAngle(double objectiveAngle) {
        firstArmMotor.enableBrakeMode(false);
        if(secondArmMotor != null);
            secondArmMotor.enableBrakeMode(false);
        pidMode = true;
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
    public void setPIDSourceType(PIDSourceType pidSource) {
    }

    @Override
    public PIDSourceType getPIDSourceType() {
        return PIDSourceType.kDisplacement;
    }

    @Override
    public double pidGet() {
        double angle = getAngle();
        return angle;
    }

    @Override
    public void pidWrite(double power) {
        
        power += 0.095 * Math.abs(Math.cos(getAngle()/180*Math.PI));
        firstArmMotor.set(power * armMotorRatio);
        if (secondArmMotor != null) {
            secondArmMotor.set(power);
        }

    }

    @Override
    protected void initDefaultCommand() {
        // TODO Auto-generated method stub

    }
    
    public void autoPeriodicModule(){
        SmartDashboard.putNumber("Potentiometer Angle", getAngle());

    }

}