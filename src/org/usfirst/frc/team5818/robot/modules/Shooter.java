package org.usfirst.frc.team5818.robot.modules;

import org.usfirst.frc.team5818.robot.RobotConstants;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;

public class Shooter implements Module {
    
    public static final int BUT_DEC_UPPER = 9;
    public static final int BUT_INC_UPPER = 10;
    public static final int BUT_DEC_LOWER = 11;
    public static final int BUT_INC_LOWER = 12;
    public static final int BUT_STOP_MOTOR = 7;
    public static final int BUT_START_MOTOR = 8;
    
    /**
     * The Talon motor controller class that controls
     * the upper fly wheel in the shooter mechanism.
     */
    private CANTalon talonFlyUpper =
            new CANTalon(RobotConstants.TALON_FLYWHEEL_UPPER);
    
    /**
     * The Talon motor controller class that controls
     * the upper fly wheel in the shooter mechanism.
     */
    private CANTalon talonFlyLower =
            new CANTalon(RobotConstants.TALON_FLYWHEEL_LOWER);

    /**
     * The incrementing value for the increment method.
     */
    private double flySpeedInc = 0.01;
    
    /**
     * Increment counter
     */
    private int counterUpper = 0;
    
    /**
     * Increment counter
     */
    private int counterLower = 0;
    
    /**
     * Increment counter
     */
    private int counterLength = 5;
    
    private double defaultSpeed = 0.6 - 0.6;
    
    private double backspinMult = 0.5;
    
    /**
     * Weather the flywheel is running or not.
     */
    private boolean running = false;
    
    /**
     * The current set volocity for the upper flywheel.
     */
    private double flyUpperVolocity;
    
    /**
     * The current set volocity for the lower flywheel.
     */
    private double flyLowerVolocity;
    
    /**
     * The current set speed for the upper flywheel.
     */
    private double flyUpperSpeed = defaultSpeed;
    
    /**
     * The current set speed for the lower flywheel.
     */
    private double flyLowerSpeed = defaultSpeed;
    
    private boolean printVolocitiesEnc = false;
    private boolean printSpeeds = true;
    private boolean printButtons = false;
    
    
    /**
     * This is the initialization method for the Shooter. This
     * initializes the flywheel motor controllers to the correct
     * channels.
     */
    @Override
    public void initModule() {
        
        //Setting the talon objects if they are null.
        if(talonFlyUpper == null)
            talonFlyUpper = new CANTalon(RobotConstants.TALON_FLYWHEEL_UPPER);
        if(talonFlyLower == null)
            talonFlyLower = new CANTalon(RobotConstants.TALON_FLYWHEEL_LOWER);
        
        counterLower = counterUpper = 0;        
    }

    @Override
    public void teleopPeriodicModule() {
        // TODO Auto-generated method stub\
    }
    
    /**
     * 
     * @param incBut Weather to increment or not
     * @param decBut Weather to deccrement or not
     * @param onBut Weather to run the wheel or not.
     * @param offbut Weather to stop the wheel or not.
     */
    public void teleopPeriodicModule(Joystick joystick) {
        
        boolean incUpperBut = joystick.getRawButton(Shooter.BUT_INC_UPPER);
        boolean decUpperBut = joystick.getRawButton(Shooter.BUT_DEC_UPPER);
        boolean incLowerBut = joystick.getRawButton(Shooter.BUT_INC_LOWER);
        boolean decLowerBut = joystick.getRawButton(Shooter.BUT_DEC_LOWER);
        boolean onBut = joystick.getRawButton(Shooter.BUT_START_MOTOR);
        boolean offBut = joystick.getRawButton(Shooter.BUT_STOP_MOTOR);
        
        if(onBut)
            running = true;
        if(offBut)
            running = false;
        
        if(incUpperBut)
        {
            if(counterUpper == counterLength)
            {
                incSpeedFlyUpper();
            }
            if(counterUpper > 5)
                counterUpper = 0;
            counterUpper++;
        }
        
        if(incLowerBut)
        {
            if(counterLower == counterLength)
            {
                incSpeedFlyLower();
            }
            if(counterLower > 5)
                counterLower = 0;
            counterLower++;
        }
        
        if(decUpperBut)
        {
            if(counterUpper == counterLength)
            {
                decSpeedFlyUpper();
            }
            if(counterUpper > 5)
                counterUpper = 0;
            counterUpper++;
        }
        
        if(decLowerBut)
        {
            if(counterLower == counterLength)
            {
                decSpeedFlyLower();
            }
            if(counterLower > 5)
                counterLower = 0;
            counterLower++;
        }
        
        double lowerVol, upperVol;
        
        if(running)
        {
            lowerVol = flyLowerSpeed;
            upperVol = flyUpperSpeed;
        }
        else
        {
            lowerVol = 0;
            upperVol = 0;
        }
        
        talonFlyUpper.set(upperVol);
        talonFlyLower.set(lowerVol);
        
        if(printSpeeds)
        {
            if(running)
            {    
                DriverStation.reportError("\nVu = " + getFlyUpperSpeed(), false);
                DriverStation.reportError("  |  Vl = " + getFlyLowerSpeed(), false);
            }
            else
            {
                DriverStation.reportError("\nVu = off(" + getFlyUpperSpeed() + ")", false);
                DriverStation.reportError("  |  Vl = off("  + getFlyLowerSpeed() + ")", false);

            }
            
        }
        
        if(printVolocitiesEnc)
        {
            if(running)
            {    
                DriverStation.reportError("\nVu = " + getFlyUpperVolocityEnc(), false);
                DriverStation.reportError("  |  Vl = " + getFlyLowerVolocityEnc(), false);
            }
            else
            {
                DriverStation.reportError("\nVu = off(" + getFlyUpperVolocityEnc() + ")", false);
                DriverStation.reportError("  |  Vl = off("  + getFlyLowerVolocityEnc() + ")", false);

            }
            
        }
        
        if(printButtons)
        {
            DriverStation.reportError("\nDecLowerBut = " + decLowerBut, false);
            DriverStation.reportError("  |  IncLowerBut = " + incLowerBut, false);
            DriverStation.reportError("  |  DecUpperBut = " + decUpperBut, false);
            DriverStation.reportError("  |  IncUpperBut = " + incUpperBut, false);
            DriverStation.reportError("  |  OffBut = " + offBut, false);
            DriverStation.reportError("  |  onBut = " + onBut, false);
        }
        
        
    }

    @Override
    public void endModule() {
        counterLower = counterUpper = 0;
        
    }
    
    /**
     * Discarded for now because we have the shooting mechanism
     * running on a toggle system.
     * @deprecated
     */
    public void shoot()
    {
    }
    
    /**
     * DO NOT USE: This does not work.
     * 
     * This sets a variable to the desired volocity. This blindly sets the
     * desired volocity to the motor controller.
     * 
     * @deprecated There is currently not working.
     * @param vol The volocity that is desired.
     */
    public void setFlyUpperVolocity(double vol)
    {
        //TODO make this method actually set the volocity
        
    }
    
    /**
     * DO NOT USE: This does not work.
     * 
     * This sets a variable to the desired volocity. This blindly sets the
     * desired volocity to the motor controller.
     * 
     * @deprecated There is currently not working.
     * @param vol The volocity that is desired.
     */
    public void setFlyLowerVolocity(double vol)
    {
      //TODO make this method actually set the volocity
    }
    
    /**
     * Sets the speed of the upper flywheel motor. Range from 1 to -1.
     * 
     * @param speed
     */
    public void setFlyUpperSpeed(double speed)
    {
        flyUpperSpeed = (int)(speed * 1000) * 0.001;
        DriverStation.reportError("\nFlywheel Speed = " + speed, false);
    }
    
    /**
     * Sets the speed of the upper flywheel motor. Range from 1 to -1.
     * 
     * @param speed
     */
    public void setFlyLowerSpeed(double speed)
    {
        flyLowerSpeed = (int)(speed * 1000) * 0.001;
        DriverStation.reportError("\nFlywheel Speed = " + speed, false);
    }
    
    public void incSpeedFlyUpper()
    {
        setFlyUpperSpeed(getFlyUpperSpeed() + flySpeedInc);
    }
    public void incSpeedFlyLower()
    {
        setFlyLowerSpeed(getFlyLowerSpeed() + flySpeedInc);
    }
    
    public void decSpeedFlyUpper()
    {
        setFlyUpperSpeed(getFlyUpperSpeed() - flySpeedInc);
    }
    public void decSpeedFlyLower()
    {
        setFlyLowerSpeed(getFlyLowerSpeed() - flySpeedInc);
    }
    
    /**
     * Returns the value of the current Lower Flywheel Volocity.
     * This value is what this class is setting to the motor controller
     * difference in actual volocity and set volocity can exist since
     * there is no feedback to correct for volocity error.
     * 
     * @return The current Flywheel Volocity of the Lower Flywheel.
     */
    public double getFlyUpperVolocitySet()
    {
        return flyUpperVolocity;
    }
    
    /**
     * Returns the value of the current Lower Flywheel Volocity.
     * This value is what this class is setting to the motor controller
     * difference in actual volocity and set volocity can exist since
     * there is no feedback to correct for volocity error.
     * 
     * @return The current Flywheel Volocity of the Lower Flywheel.
     */
    public double getFlyLowerVolocitySet()
    {
        return flyLowerVolocity;
    }
    
    /**
     * Returns the value of the current Lower Flywheel Volocity.
     * This value is what this class is setting to the motor controller
     * difference in actual volocity and set volocity can exist since
     * there is no feedback to correct for volocity error.
     * 
     * @return The current Flywheel Volocity of the Lower Flywheel.
     */
    public double getFlyUpperVolocityEnc()
    {
        return talonFlyUpper.getEncVelocity();
    }
    
    /**
     * Returns the value of the current Lower Flywheel Volocity.
     * This value is what this class is setting to the motor controller
     * difference in actual volocity and set volocity can exist since
     * there is no feedback to correct for volocity error.
     * 
     * @return The current Flywheel Volocity of the Lower Flywheel.
     */
    public double getFlyLowerVolocityEnc()
    {
        return talonFlyLower.getEncVelocity();
    }
    
    /**
     * Returns the value of the current Lower Flywheel Volocity.
     * This value is what this class is setting to the motor controller
     * difference in actual volocity and set volocity can exist since
     * there is no feedback to correct for volocity error.
     * 
     * @return The current Flywheel Volocity of the Lower Flywheel.
     */
    public double getFlyUpperSpeed()
    {
        return flyUpperSpeed;
    }
    
    /**
     * Returns the value of the current Lower Flywheel Volocity.
     * This value is what this class is setting to the motor controller
     * difference in actual volocity and set volocity can exist since
     * there is no feedback to correct for volocity error.
     * 
     * @return The current Flywheel Volocity of the Lower Flywheel.
     */
    public double getFlyLowerSpeed()
    {
        return flyLowerSpeed;
    }

}
