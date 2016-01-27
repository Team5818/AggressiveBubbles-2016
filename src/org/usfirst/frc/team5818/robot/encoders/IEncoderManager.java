package org.usfirst.frc.team5818.robot.encoders;

import edu.wpi.first.wpilibj.Encoder;

public interface IEncoderManager {
    
    /**
     * Get the position of this EncoderManager's encoder in absolute terms.
     * @return absolute position of the Encoder.
     */
    public abstract double getEncPosAbs();
    
    /**
     * Get the position the Encoder has moved since getEncDelta() was last called.
     * If getEncPosAbs() is called twice in succession without moving, the second call
     * should return 0 - ie this call should somehow reset the counter for this same call.
     * @return the position the Encoder has moved since getEncDelta() was last called.
     */
    public abstract double getEncDelta();
    
    /**
     * Get the position the Encoder has moved since getEncDelta() was last called.
     * This is a non-destructive form of getEncDelta(), it should not change the state
     * of the object in any way.
     * @return the position the Encoder has moved since getEncDelta() was last called.
     */
    public abstract double peekEncDelta();
    
    /**
     * Sets a goal for the encoder to move a certain distance. If used, this assumes that
     * the IEncoderManager will regularly be fed getPowerAccordingToGoal(time) in order
     * to keep updated.
     * @param dist - The distance, in arbitrary units defined by implementation, that this
     * encoder is requested to move.
     */
    public abstract void setMovementGoal(double dist);
    
    /**
     * After having called setMovementGoal(), this function must be called regularly and the
     * power returned should be fed into both left and right motors.
     * @param time - the time in seconds since the last call to setMovementGoal. Critical for
     * proper PID loops if used in implementation (specifically the I & D components)
     * @return A power between -1.0 and 1.0 that motors are to be run at to achieve this distance.
     * If this EncoderManager has no MovementGoal (reset has been called before the last 
     * setMovementGoal() or setMovementGoal() has never been called), returns 0.0;
     */
    public abstract double getPowerAccordingToGoal(double time);
    
    /**
     * 
     * @return the distance remaining to the current MovementGoal as set in SetMovementGoal(). If
     * this EncoderManager has no MovementGoal, ie resetGoal() has been called more recently than
     * the last setMovementGoal() or setMovementGoal() has not been called at all, this returns 0.0
     */
    public abstract double getGoalDistance();
    
    /**
     * 
     * @return the distance originally set in setMovementGoal(), or 0.0 if resetGoal() has been
     * called more recently than the last setMovementGoal() or setMovementGoal() has never been
     * called.
     */
    public abstract double getGoalDistanceOriginal();
    
    /**
     * 
     * @return shorthand for getGoalDistanceOriginal() - getGoalDistance()
     */
    public abstract double getDistanceCovered();
    
    /**
     * Resets the DistanceGoal, stopping any in-progress tracking and so forth. Will not be triggered
     * by default, requires somebody to call this.
     */
    public abstract void resetGoal();
    
    /**
     * Shorthand for isGoalComplete(MoE) with MoE predefined to some constant we select based on tests.
     * @return see isGoalComplete(double MoE);
     */
    public abstract boolean isGoalComplete();
    
    /**
     * Returns true iff Math.abs(getGoalDistance) <= MoE
     * @param MoE - Margin of Error, the distance the EncoderManager can be away from the goal and
     * still consider itself done.
     * @return true iff Math.abs(getGoalDistance) <= MoE
     */
    public abstract boolean isGoalComplete(double MoE);
}
