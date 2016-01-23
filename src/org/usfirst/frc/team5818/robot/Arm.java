package org.usfirst.frc.team5818.robot;

public class Arm {

	private static final double MULTIPLIER = 0.5;
    private double power;
    private double maxPower = MULTIPLIER;
    private double minPower = -MULTIPLIER;

    private double angle;

	public Arm(){
		RobotConstants.ARM_ENCODER.reset();
		RobotConstants.ARM_ENCODER.setDistancePerPulse(RobotConstants.ARM_ENCODER_SCALE); // Angle per pulse in our case
	}

    /**
     * Sets power of arm, keeping arm power within max and minimum parameters .5
     * and -.5
     * 
     * @param power
     *            - power value
     */

    public void setPower(double power) {

    	RobotConstants.TALON_ARM_MOTOR.set(power);
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
        return RobotConstants.ARM_ENCODER.getDistance();
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
    	
    	this.angle = RobotConstants.ARM_ENCODER.getDistance();
        return this.angle;
    }
    
	public void armTeleopPeriodic(){

		this.setPower(-MULTIPLIER*RobotConstants.JOYSTICK_C.getY());
	}

}
