package team5818.robot.modules;

import edu.wpi.first.wpilibj.CANTalon;

/**
 * This is the class that handles all the control on the  motors directly through
 * the CANTalon objects. It will also "set the speed using a PID loop"(unavailable currently),
 * show the actual speed, and be able to directly set the power.
 * 
 * Any direct communication with the motor controllers related to the flywheel should 
 * go through this class.
 *
 */
public class FlyWheel implements Module{
    
    /**
     * The CANTalon motor controller for the upper wheel.
     */
    private final CANTalon talon;
    
    /**
     * Weather the flywheel is running or not.
     */
    private boolean running = true;
    
    /**
     * The current set speed for the upper flywheel.
     */
    private double power = 0;
    
    /**
     * The gearbox ratio between the motor shaft to the wheel.
     */
    private double gearBoxRatio = 24 / 60;
    
    private boolean usingPID = false;
    
    /**
     * 
     * @param talon The upper CANTalon motor on the flywheel
     * @param reversed Specifies weather to reverse the sensor and output of the motor.
     */
    public FlyWheel(CANTalon talon, boolean usingPID, boolean reversed) {
        
        this.talon = talon;
        this.usingPID = usingPID;
        
        if(usingPID)
            talon.changeControlMode(CANTalon.TalonControlMode.Speed);
        else
            talon.changeControlMode(CANTalon.TalonControlMode.Voltage);
        
        talon.enableControl();
        talon.reverseOutput(reversed);
        talon.reverseSensor(reversed);
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
            
            talon.set(power);
            
        } else {
            
            talon.set(0);
        }
    }

    @Override
    public void endModule() {
        
    }
    
    /**
     * Sets the running state of the flywheel to true. This will not set change
     * the speed, it will only set an indicator for the program loop to take care of.
     */
    public void start() {
        
        running = true;
    }
    
    /**
     * Sets the running state of the flywheel to false. This will not set change
     * the speed, it will only set an indicator for the program loop to take care of.
     */
    public void stop() {
        
        running = false;
    }
    
    public void enablePID()
    {
        talon.changeControlMode(CANTalon.TalonControlMode.Speed);
        talon.enableControl();
    }
    
    public void disablePID()
    {
        talon.changeControlMode(CANTalon.TalonControlMode.Voltage);
        talon.enableControl();
    }
    
    /**
     * Directly sets the power of the upper motor to the specified number. Ranging from
     * 1 to -1 having 1 cause the ball to move forward. 
     * 
     * @param power the power desired to be set, from 1 to -1
     */
    public void setPower(double power) {
        
        if(power > 1)
            power = 1;
        if(power < -1)
            power = -1;
        this.power = power;
    }
    
    /**
     * Returns the power to be set to the upper flywheel.
     * 
     * @return The power of the upper fly wheel.
     */
    public double getPower() {
        
        return power;
    }
    
    /**
     * Returns the Revolotions Per Second of the actual wheel.
     * 
     * @return Revolotions Per Second
     */
    public double getRPS() {
        
        return talon.getEncVelocity() * 10 / 6 * gearBoxRatio;
    }


}
