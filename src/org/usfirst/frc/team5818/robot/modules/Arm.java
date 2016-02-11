package org.usfirst.frc.team5818.robot.modules;

import org.usfirst.frc.team5818.robot.RobotConstants;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;

public class Arm implements Module{

    private static final double MULTIPLIER = -1.0;
    private static final Encoder ARM_ENCODER =
            new Encoder(RobotConstants.ARM_ENCODER_CHANNEL_A,
                    RobotConstants.ARM_ENCODER_CHANNEL_B);
    private static final CANTalon ARM_MOTOR =
            new CANTalon(RobotConstants.TALON_ARM_MOTOR);
    private PIDController armPID =
            new PIDController(.1, .1, .1, ARM_ENCODER, ARM_MOTOR);

    private double power;
    private double maxPower = .3; // max and min power are for PID and aim
                                  // adjusts
    private double minPower = -.3;

    private double angle;
    private boolean setAngleMode = false;
    
    private double targetAngle;

    public Arm() {
        ARM_ENCODER.reset();
        ARM_ENCODER.setDistancePerPulse(RobotConstants.ARM_ENCODER_SCALE); // Angle
                                                                           // per
                                                                           // pulse
                                                                           // in
                                                                           // our
                                                                           // case
        ARM_MOTOR.setInverted(true);
        armPID.setOutputRange(minPower, maxPower);

    }
    
    public void setMode(boolean angleMode){
        setAngleMode = angleMode;
    }
    
    public boolean getMode(){
        return setAngleMode;
    }
    
    public void toggleMode(){
        setAngleMode = !setAngleMode;
    }
    
    public void setTarget(double target){
        targetAngle = target;
    }
    
    public double getTarget(){
        return targetAngle;
    }

    /**
     * Sets power of motor to power variable 
     * 
     */

    public void writePower() {
        armPID.disable();
        ARM_MOTOR.set(power);
    }
    /**
     * sets power of motor to specified value
     * @param power- motor value
     */
    
    public void writePower(double power){
        armPID.disable();
        ARM_MOTOR.set(power);
    }
    /**
     * sets power variable to specified value
     * @param val- power value
     */
    
    public void setPower(double val){
        power = val;
    }

    /**
     * Gets power variable
     * 
     * @return power variable
     */

    public double getPower() {
        return this.power;
    }

    /**
     * @return angle measured by encoder
     */
    public double getEncoderVal() {
        return ARM_ENCODER.getDistance(); // raw value
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
    public void resetEncoder() {
        ARM_ENCODER.reset();
    }

    /**
     * @param up
     *            moves arm slightly up or down, doesn't work in setPowerMode
     */
    public void aimAdjust(Boolean up) {
        if(!setAngleMode){
            if (up) {
                this.writePower(maxPower);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (!up) {
                this.writePower(minPower);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }        
    }

    /**
     * don't know what this is supposed to do
     */
    public void stabilize() {

    }

    /**
     * @param objective
     *            PIDs to the given objective
     */
    public void goToAngle(double objective) {
        armPID.reset();
        armPID.setSetpoint(objective);
        armPID.enable();
    }

    @Override
    public void initModule() {
        
    }

    @Override
    /**
     * called during teleop
     * in angle mode, goes to target angle
     * in regular mode, sets power to power value
     */
    public void teleopPeriodicModule() {
        if(setAngleMode){
            goToAngle(targetAngle);
        }
        else{
            writePower();
        }   
    }

    @Override
    public void endModule() {
        // TODO Auto-generated method stub
        
    }

}
