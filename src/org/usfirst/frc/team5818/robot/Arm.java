package org.usfirst.frc.team5818.robot;

import edu.wpi.first.wpilibj.Encoder;

public class Arm {
	
    private static final double MULTIPLIER = -1.0;
    private double power;
    private double maxPower = MULTIPLIER;
    private double minPower = -MULTIPLIER;

    private double angle;

    public Arm(){
	RobotConstants.ARM_ENCODER.reset();
	RobotConstants.ARM_ENCODER.setDistancePerPulse(RobotConstants.ARM_ENCODER_SCALE); // Angle per pulse in our case
    }

   public Arm(Encoder e){
		
    }
	
    /**
     * Sets power of left arm, keeping arm power within max and minimum parameters .5
     * and -.5
     * 
     * @param power
     *            - power value
     */

    public void setPower(double power, char arm) {
    	
    	if(arm == 'r')
    	RobotConstants.TALON_LEFT_ARM_MOTOR.set(power);
    	if(arm == 'l')
    	RobotConstants.TALON_RIGHT_ARM_MOTOR.set(power);
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
/// Updated upstream
    
    public void aimAdjustLeft(Boolean up){
    	if(up){
    	/*	for(int i = 0; i < 101; i++)
    			this.setPower(maxPower);
    	}*/
//    		double angl = this.getAngle();
//    		DriverStation.reportError("" + angl + "\n", false);
//    		while((angl - 400) != angl) {
//    			this.setPower(maxPower);	
//    		}
    		this.setPower(maxPower * -.3, 'l');
    		try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	if(!up){
    	/*	for(int i = 0; i < 101; i++)
    			this.setPower(minPower);
    	*/
//	    	double angl = this.getAngle();
//    		DriverStation.reportError("" + angl + "\n", false);
//			while((angl + 400) != angl) {
//				this.setPower(minPower);
//			}
    		this.setPower(minPower * -.3, 'l');
    		try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    public void aimAdjustRight(Boolean up){
    	if(up){
    	/*	for(int i = 0; i < 101; i++)
    			this.setPower(maxPower);
    	}*/
//    		double angl = this.getAngle();
//    		DriverStation.reportError("" + angl + "\n", false);
//    		while((angl - 400) != angl) {
//    			this.setPower(maxPower);	
//    		}
    		this.setPower(maxPower * .3, 'r');
    		try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	if(!up){
    	/*	for(int i = 0; i < 101; i++)
    			this.setPower(minPower);
    	*/
//	    	double angl = this.getAngle();
//    		DriverStation.reportError("" + angl + "\n", false);
//			while((angl + 400) != angl) {
//				this.setPower(minPower);
//			}
    		this.setPower(minPower * .3, 'r');
    		try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
    public void stabilize(){
    	
    }
    
        
//// Stashed changes
	public void armTeleopPeriodic(){

		this.setPower(-MULTIPLIER*RobotConstants.JOYSTICK_C.getY(), 'l');
		this.setPower(MULTIPLIER*RobotConstants.JOYSTICK_C.getY(), 'r');
	}

}
