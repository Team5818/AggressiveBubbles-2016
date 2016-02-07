package org.usfirst.frc.team5818.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;

public class Arm {

    private static final double MULTIPLIER = -1.0;
    private static final Encoder ARM_ENCODER = new Encoder(RobotConstants.ARM_ENCODER_CHANNEL_A, RobotConstants.ARM_ENCODER_CHANNEL_B);
    private static final CANTalon ARM_MOTOR = new CANTalon(RobotConstants.TALON_ARM_MOTOR);
    private PIDController armPID = new PIDController(.1, .1, .1, ARM_ENCODER, ARM_MOTOR);
    
    private double power;
    private double maxPower = .3;  //max and min power are for PID and aim adjusts
    private double minPower = -.3;

    private double angle;

    public Arm() {
        ARM_ENCODER.reset();
        ARM_ENCODER.setDistancePerPulse(RobotConstants.ARM_ENCODER_SCALE); // Angle per pulse in our case
        ARM_MOTOR.setInverted(true);
        armPID.setOutputRange(minPower,maxPower);

    }


    /**
     * Sets power of  arm, keeping arm power within max and minimum
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
    
    /**
     * sets encoder val to zero
     */
    public void resetEncoder(){
        ARM_ENCODER.reset();
    }

    /**
     * @param up
     * moves arm slightly up or down
     */
    public void aimAdjust(Boolean up) {
        if (up) {
            this.setPower(maxPower);
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (!up) {
            this.setPower(minPower);
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
    
    /**
     * @param objective
     * PIDs to the given objective
     */
    public void goToAngle(double objective){
        armPID.reset();
        armPID.setSetpoint(objective);
        armPID.enable();
    }


}
