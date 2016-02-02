package org.usfirst.frc.team5818.robot.modules;


/**
 * 
 * This is a frame for every system/subsystem in this robot. This sets a layout for how every system in the robot should
 * be organized. Things like Arm, DriveTrain, ComputerVision etc.
 * 
 */
public interface Module 
{
    /**
     * Called first right before a robot enters a state. Such as teleopInit.
     */
    public abstract void initModule();
    
    /**
     * The periodic method for this module. Should not be used unless very necessary. Should have commands that can
     * achieve the function that this module should do.
     */
    public abstract void teleopPeriodicModule();
    
    /**
     * This method is called when the robot is about to exit from a state. This is used to clear any variables that
     * need to be reset.
     */
    public abstract void endModule();
    
    
}
