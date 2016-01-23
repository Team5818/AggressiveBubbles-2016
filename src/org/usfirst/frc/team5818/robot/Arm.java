package org.usfirst.frc.team5818.robot;

public class Arm {
	
	private double power;
	private double maxPower = .5;
	private double minPower = -.5;
	
	private double angle;
	
	/**
	 * Sets power of arm, keeping arm power within max and minimum parameters .5 and -.5
	 * 
	 * @param power
	 *            - power value
	 */
	
	public void setPower(double power){
		if(power < maxPower && power > minPower){
			this.power = power;
			RobotConstants.TALON_ARM_MOTOR.set(power);
		}else if(power < minPower){
			this.power = -.5;
		}else{
			this.power = .5;
		}
	}
	
	/**
	 * Gets power of arm
	 * 
	 * @return power of arm
	 */
	
	public double getPower(){
		return this.power;
	}
	
	/**
	 * Sets arm angle
	 * 
	 * @param y
	 *            - angle value
	 */
	
	public void setAngle(double y){
		this.angle = y;
	}
	
	/**
	 * Gets arm angle
	 * 
	 * @return arm angle
	 */
	
	public double getAngle(){
		return this.angle;
	}

}
