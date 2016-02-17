package org.usfirst.frc.team5818.robot.modules;

import edu.wpi.first.wpilibj.CANTalon;

/**
 *
 */
public class FlyWheel implements Module{
    
    private final CANTalon talonFlyUpper;
    private final CANTalon talonFlyLower;
    
    /**
     * Weather the flywheel is running or not.
     */
    private boolean running = true;
    
    /**
     * Weather to use PID loop to set the speed.
     */
    private boolean usingPID = false;
    
    /**
     * The current set speed for the upper flywheel.
     */
    private double flyUpperPower = 0;
    
    /**
     * The current set speed for the lower flywheel.
     */
    private double flyLowerPower = 0;
    
    /**
     * sets the final talon fields to the given values.
     * @param talonU The upper CANTalon motor controller.
     * @param talonL The lower CANTalon motor controller.
     */
    public FlyWheel(CANTalon talonU, CANTalon talonL) {
        talonFlyUpper = talonU;
        talonFlyLower = talonL;
        talonFlyLower.reverseOutput(true);
        
    }
    
    /**
     * This is the initialization method for the Shooter. This
     * initializes the flywheel motor controllers to the correct
     * channels.
     */
    @Override
    public void initModule() {
        
    }

    @Override
    public void teleopPeriodicModule() {

        if(running) {
         
            talonFlyUpper.set(flyUpperPower);
            talonFlyLower.set(flyLowerPower);
            
        } else {
            
            talonFlyUpper.set(0);
            talonFlyLower.set(0);
        }
    }

    @Override
    public void endModule() {
        
    }
    
    /**
     * Sets the variable that defines the flywheel as running or not.
     * This will not directly change anything in the system, it will
     * only set an identifier in the program main loop to start or stop 
     * the flywheels.
     * 
     * @param running weather or not to have the system run.
     */
    public void setFlyWheelsRunning(boolean running) {
        
        this.running = running;
    }
    
    
    /**
     * Directly sets the power of the upper motor to the specified number. Ranging from
     * 1 to -1 having 1 cause the ball to move forward. 
     * 
     * @param power the power desired to be set, from 1 to -1
     */
    public void setFlyUpperPower(double power) {
        
        if(power > 1)
            power = 1;
        if(power < -1)
            power = -1;
        flyUpperPower = power;
    }
    
    /**
     * Directly sets the power of the lower motor to the specified number. Ranging from
     * 1 to -1 having 1 cause the ball to move forward. 
     * 
     * @param power the power desired to be set, from 1 to -1
     */
    public void setFlyLowerPower(double power) {
        
        if(power > 1)
            power = 1;
        if(power < -1)
            power = -1;
        flyLowerPower = power;
    }
    
    /**
     * Returns the power to be set to the upper flywheel.
     * 
     * @return The power of the upper fly wheel.
     */
    public double getFlyUpperPower() {
        
        return flyUpperPower;
    }
    
    /**
     * Returns the power to be set to the lower flywheel.
     * 
     * @return The power of the lower fly wheel.
     */
    public double getFlyLowerPower() {
        
        return flyLowerPower;
    }
    
    /**
     * Returns the Revolotion Per Second of the actual wheel.
     * 
     * @return Revolotions Per Second
     */
    public double getFlyUpperRPS() {
        
        //conver motor to ticks per second
        double motorTPS = getMotorUpperRawEnc() * 10;
        //convert motor to revolotions per second.
        double motorRPS = motorTPS / 6;
        //convert to wheel gear ratio.
        double wheelRPS = motorRPS * 24 / 60;
        
        return wheelRPS;
    }
    
    /**
     * Returns the Revolotions Per Second of the actual wheel.
     * 
     * @return Revolotions Per Second
     */
    public double getFlyLowerRPS() {
        
        //conver motor to ticks per second
        double motorTPS = getMotorLowerRawEnc() * 10;
        //convert motor to revolotions per second.
        double motorRPS = motorTPS / 6;
        //convert to wheel gear ratio.
        double wheelRPS = motorRPS * 24 / 60;
        
        return wheelRPS;
    }
    
    /**
     * Returns the number of ticks per 100ms where there are 6 ticks per revolution.
     * 
     * @return Number of ticks per 100ms.
     */
    public double getMotorUpperRawEnc() {
        
        return talonFlyUpper.getEncVelocity();
    }
    
    /**
     * Returns the number of ticks per 100ms where there are 6 ticks per revolution.
     * 
     * @return Number of ticks per 100ms.
     */
    public double getMotorLowerRawEnc() {
        
        return talonFlyLower.getEncVelocity();
    }

}
