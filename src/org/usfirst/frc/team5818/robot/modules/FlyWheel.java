package org.usfirst.frc.team5818.robot.modules;

import org.usfirst.frc.team5818.robot.RobotConstants;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

public class FlyWheel implements Module{
    
    private final CANTalon talonFlyUpper;
    private final CANTalon talonFlyLower;
    
    /**
     * Weather the flywheel is running or not.
     */
    private boolean running = true;
    
    
    /**
     * The current set speed for the upper flywheel.
     */
    private double flyUpperPower = 0;
    
    /**
     * The current set speed for the lower flywheel.
     */
    private double flyLowerPower = 0;
    
    public FlyWheel(CANTalon talonU, CANTalon talonL)
    {
        talonFlyUpper = talonU;
        talonFlyLower = talonL;
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

        if(running)
        {
            talonFlyUpper.set(flyUpperPower);
            talonFlyLower.set(flyLowerPower * (-1));
        }
        else
        {
            talonFlyUpper.set(0);
            talonFlyLower.set(0);
        }
    }

    @Override
    public void endModule() {
        
    }
    
    public void startFlyWheels()
    {
        running = true;
    }
    
    public void stopFlyWheels()
    {
        running = false;
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
        
        return talonFlyLower.getEncVelocity() * (-1);
    }


}
