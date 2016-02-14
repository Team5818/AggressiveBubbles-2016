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
     * The PID constants for the upper and lower motors.
     */
    private double lKp = 1;

    private double lKi = 1;

    private double lKd = 1;

    private double uKp = 1;

    private double uKi = 1;

    private double uKd = 1;
    
    private double flyLowerSetVelocity;
    
    private double flyUpperSetVelocity;
    
    private double closeLoopRampRate = 0;
    
    private int izone = 10;
    
    /**
     * sets the final talon fields to the given values.
     * @param talonU The upper CANTalon motor controller.
     * @param talonL The lower CANTalon motor controller.
     */
    public FlyWheel(CANTalon talonU, CANTalon talonL) {
        talonFlyUpper = talonU;
        talonFlyLower = talonL;
        talonFlyLower.reverseOutput(true);
        
        talonFlyUpper.setPID(uKp, uKi, uKd, flyUpperSetVelocity, izone, closeLoopRampRate, 0);
        talonFlyLower.setPID(lKp, lKi, lKd, flyLowerSetVelocity, izone, closeLoopRampRate, 0);
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
            
            if(usingPID) {
                
                talonFlyLower.setF(flyLowerSetVelocity);
                talonFlyUpper.setF(flyUpperSetVelocity);
                
                
            } else {
                
                talonFlyUpper.set(flyUpperPower);
                talonFlyLower.set(flyLowerPower);
            }
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
     * Sets weather to use a PID loop on the current system or not.
     * If set to true setVelocity methods should be used to change
     * the motion of the flywheel. If false setPower methods should
     * be used instead/
     * @param upid
     */
    public void usingPID(boolean upid) {
        
        usingPID = upid;
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
     * Sets the desired rps for the PID loop to use when setting the power.
     * This only works when in PID mode.
     * 
     * @param Velocity the desired velocity.
     */
    public void setFlyUpperVelocity(double Velocity) {
        
        flyUpperSetVelocity = Velocity;
    }
    
    /**
     * Sets the desired rps for the PID loop to use when setting the power.
     * This only works when in PID mode.
     * 
     * @param Velocity the desired velocity.
     */
    public void setFlyLowerVelocity(double Velocity) {
        
        flyLowerSetVelocity = Velocity;
    }
    
    /**
     * Returns the desired velocity in the PID loop, not the
     * actual velocity in the system.
     * 
     * @return The desired velocity by the PID loop.
     */
    public double getFlyUpperVelocity() {
        
        return flyUpperSetVelocity;
    }
    
    /**
     * Returns the desired velocity in the PID loop, not the
     * actual velocity in the system.
     * 
     * @return The desired velocity by the PID loop.
     */
    public double getFlyLowerVelocity() {
        
        return flyLowerSetVelocity;
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
    
    public void setLowerKp(double k)
    {
        talonFlyLower.setP(k);
    }
    
    public void setLowerKi(double k)
    {
        talonFlyLower.setI(k);
    }
    
    public void setLowerKd(double k)
    {
        talonFlyLower.setD(k);
    }
    
    public void setLowerKf(double k)
    {
        talonFlyLower.setF(k);
    }
    
    public void setUpperKp(double k)
    {
        talonFlyUpper.setP(k);
    }
    
    public void setUpperKi(double k)
    {
        talonFlyUpper.setI(k);
    }
    
    public void setUpperKd(double k)
    {
        talonFlyUpper.setD(k);
    }
    
    public void setUpperKf(double k)
    {
        talonFlyUpper.setF(k);
    }
    
    public void setLowerIZone(int k)
    {
        talonFlyLower.setIZone(izone);
    }
    
    public void setUpperIZone(int k)
    {
        talonFlyUpper.setIZone(k);
    }


}
