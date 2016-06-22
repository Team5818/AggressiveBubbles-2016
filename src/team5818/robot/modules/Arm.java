package team5818.robot.modules;


import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.RobotConstants;


/**
 * Class to control 5818's robot arm
 */
public class Arm extends Subsystem implements PIDSource, PIDOutput {
    
    protected static final double DEFAULT_SCALE = 0.047;
    protected static final double DEFAULT_OFFSET = -9.587;
    protected static final double DEFAULT_MAXPOWER = 1;
    protected static final double DEFAULT_MINPOWER = -.4;
    protected static final double DEFAULT_KP = 0.01;
    protected static final double DEFAULT_KI = 0.0;
    protected static final double DEFAULT_KD = 0.0;

    private double scale;
    private double offset;
    private double maxPower;
    private double armMotorRatio = 1;
    private double armPowerIdle = 0.1;
    private boolean pidMode = false;
    private boolean angleCapping = true;
    private double armPowerIdleRatio = 1;

    private static final AnalogInput armPotentiometer =
            new AnalogInput(RobotConstants.ARM_POTENTIOMETER_CHANNEL);
    private static final CANTalon firstArmMotor =
            new CANTalon(RobotConstants.TALON_FIRST_ARM_MOTOR);
    private static CANTalon secondArmMotor =
            new CANTalon(RobotConstants.TALON_SECOND_ARM_MOTOR);

    private PIDController armPID;


    /**
     * The arm of the robot. Used for crossing defenses, collecting, and shooting.
     */
    public Arm() {
        if (secondArmMotor != null)
            secondArmMotor.setInverted(true);
        double kp, ki, kd;
        scale = Preferences.getInstance().getDouble("ArmPotScale",
                    DEFAULT_SCALE);
        offset = Preferences.getInstance().getDouble("ArmPotOffset",
                    DEFAULT_OFFSET);
        maxPower = Preferences.getInstance().getDouble("ArmMaxPower", DEFAULT_MAXPOWER);
        armMotorRatio = Preferences.getInstance().getDouble("ArmMotorRatio", armMotorRatio);
        armPowerIdle = Preferences.getInstance().getDouble("ArmPowerIdle", armPowerIdle);
        armPowerIdleRatio  = Preferences.getInstance().getDouble("ArmPowerIdleRatio", armPowerIdleRatio);
        kp = Preferences.getInstance().getDouble("ArmKp", DEFAULT_KP);//remove .'s
        ki = Preferences.getInstance().getDouble("ArmKi", DEFAULT_KI);
        kd = Preferences.getInstance().getDouble("ArmKd", DEFAULT_KD);

        armPID = new PIDController(kp, ki, kd, this, this);
        armPID.setOutputRange(-maxPower, maxPower);
        armPID.setAbsoluteTolerance(10);
        LiveWindow.addActuator("Arm", "PID Controller", armPID);
    }

    /**
     * Sets power to arm motors
     * 
     * @param power
     *            The power value
     */
    public synchronized void setPower(double power) {
        firstArmMotor.enableBrakeMode(true);
        secondArmMotor.enableBrakeMode(true);
        pidMode = false;
        armPID.disable();
        pidWrite(power);
    }

    /**
     * gives max power
     * @return the max power of the arm
     */
    public double getMaxPower() {
        return maxPower;
    }

    /**
     * @return angle measured by pot
     */

    public double getAngle() {
        double a1 = armPotentiometer.getValue() + offset;
        double aFinal = a1*scale;
        return aFinal;
    }
    
    /**
     * @return Unadjusted reading from potentiometer
     */
    public double getRawPot() {
        return armPotentiometer.getValue();
    }

    /**
     * @return Whether or not the arm's PID loop is running
     */
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
        
        if(this.getAngle() >= 95 && power >= 0 && angleCapping){
           firstArmMotor.set(0);
           secondArmMotor.set(0);
        }
        else{
            power += armPowerIdle * Math.abs(Math.cos(getAngle()/180*Math.PI));
            firstArmMotor.set(power * armMotorRatio);
            if (secondArmMotor != null) {
                secondArmMotor.set(power);
             }
        }    

    }
    
    /**
     * make the current arm angle 0 degrees
     */
    public void zeroPot(){
        Preferences.getInstance().putDouble("ArmPotOffset", -getRawPot());
        offset = -getRawPot();
        
    }
    
    /**
     * Turn on the max angle of the arm
     */
    public void capAngle(){
        angleCapping = true;
    }
    
    /**
     * Turn off angle capping
     */
    public void uncapAngle(){
        angleCapping = false;
    }
    

    @Override
    protected void initDefaultCommand() {
        // TODO Auto-generated method stub

    }
    
    /**
     * Print pot angle to SmartDash
     */
    public void autoPeriodicModule(){
        SmartDashboard.putNumber("Potentiometer Angle", getAngle());

    }

}