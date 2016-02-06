package org.usfirst.frc.team5818.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;

public class Arm {

    private static final double MULTIPLIER = -1.0;
    private static final Encoder ARM_ENCODER = new Encoder(RobotConstants.ARM_ENCODER_CHANNEL_A, RobotConstants.ARM_ENCODER_CHANNEL_B);
    private static final CANTalon ARM_MOTOR = new CANTalon(RobotConstants.TALON_ARM_MOTOR);
    private PIDController armPID = new PIDController(0,0,0, ARM_ENCODER, ARM_MOTOR);
    
    private double power;
    private double maxPower = MULTIPLIER;
    private double minPower = -MULTIPLIER;

    private double angle;

    public Arm() {
        ARM_ENCODER.reset();
        ARM_ENCODER.setDistancePerPulse(RobotConstants.ARM_ENCODER_SCALE); // Angle per pulse in our case

    }


    /**
     * Sets power of left arm, keeping arm power within max and minimum
     * parameters .5 and -.5
     * 
     * @param power
     *            - power value
     */
    
    public void setPower(double power){
        armPID.disable();
        ARM_MOTOR.set(power);
    }


    /**
     * Gets power of arm
     * 
     * @return power of arm
     */

    public double getPower() {
        return this.power;
    }

    public double getEncoderVal() {
        return ARM_ENCODER.getDistance(); //raw value
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
    
    public void resetEncoder(){
        ARM_ENCODER.reset();
    }

    public void aimAdjust(Boolean up) {
        armPID.disable();
        if (up) {
            this.setPower(maxPower * -.3);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (!up) {
            this.setPower(minPower * -.3);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    public void stabilize() {

    }
    
    public void goToAngle(double objective){
        armPID.reset();
        armPID.setSetpoint(objective);
        armPID.enable();
    }


}
