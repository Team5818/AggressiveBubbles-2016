package org.usfirst.frc.team5818.robot.encoders;

/**
 * Implementations of this interface allow for control of driving to a target
 * that is a certain distance away, especially useful during autonomous.
 */
public interface EncoderManager {

    /**
     * A margin of error that is considered to be good, usually due to testing,
     * but usually just for fun.
     */
    static final double DEFAULT_MARGIN_OF_ERROR = 0.001d;

    /**
     * Get the position of this EncoderManager's encoder in absolute terms.
     * 
     * @return absolute position of the Encoder.
     */
    double getEncPosAbs();

    /**
     * Get the position the Encoder has moved since getEncDelta() was last
     * called. If getEncPosAbs() is called twice in succession without moving,
     * the second call should return 0 - ie this call should somehow reset the
     * counter for this same call.
     * 
     * @return the position the Encoder has moved since getEncDelta() was last
     *         called.
     */
    double getEncDelta();

    /**
     * Get the position the Encoder has moved since getEncDelta() was last
     * called. This is a non-destructive form of getEncDelta(), it should not
     * change the state of the object in any way.
     * 
     * @return the position the Encoder has moved since getEncDelta() was last
     *         called.
     */
    double peekEncDelta();

    /**
     * Sets the distance for the encoder to move. If used, this assumes that the
     * EncoderManager will regularly be fed getPowerAccordingToDistance(time) in
     * order to keep updated.
     * 
     * @param dist
     *            - The distance, in arbitrary units defined by implementation,
     *            that this encoder is requested to move.
     */
    void setDriveDistance(double dist);

    /**
     * After having called setDriveDistance(), this function must be called
     * regularly and the power returned should be fed into both left and right
     * motors.
     * 
     * @param time
     *            - the time in seconds since the last call to setDriveDistance.
     *            Critical for proper PID loops if used in implementation
     *            (specifically the I &amp; D components)
     * @return A power between -1.0 and 1.0 that motors are to be run at to
     *         achieve this distance. If this EncoderManager has no MovementGoal
     *         (reset has been called before the last setDriveDistance() or
     *         setDriveDistance() has never been called), returns 0.0;
     */
    double getPowerAccordingToDistance(double time);

    /**
     * 
     * @return the distance remaining to the current target as set in
     *         setDriveDistance(). If this EncoderManager has no target, ie
     *         resetDistance() has been called more recently than the last
     *         setDriveDistance() or setDriveDistance() has not been called at
     *         all, this returns 0.0
     */
    double getDistanceNotCovered();

    /**
     * 
     * @return the distance originally set in setDriveDistance(), or 0.0 if
     *         resetDistance() has been called more recently than the last
     *         setDriveDistance() or setDriveDistance() has never been called.
     */
    double getDistanceToCover();

    /**
     * 
     * @return shorthand for getDistanceToCover() - getDistanceNotCovered()
     */
    double getDistanceCovered();

    /**
     * Resets the target, stopping any in-progress tracking and so forth. Will
     * not be triggered by default, requires somebody to call this.
     */
    void resetDistance();

    /**
     * Shorthand for isDistanceCovered(errorMargin) with errorMargin predefined
     * to some constant we select based on tests.
     * 
     * @return see isDistanceCovered(double errorMargin)
     * @see #isDistanceCovered(double)
     */
    default boolean isDistanceCovered() {
        return isDistanceCovered(DEFAULT_MARGIN_OF_ERROR);
    }

    /**
     * Returns true iff Math.abs(getDistanceToCover) &lt;&lt; errorMargin
     * 
     * @param errorMargin
     *            - Margin of Error, the distance the EncoderManager can be away
     *            from the goal and still consider itself done.
     * @return true iff Math.abs(getDistanceToCover) &lt;&lt; errorMargin
     */
    default boolean isDistanceCovered(double errorMargin) {
        return Math.abs(getDistanceToCover()) < errorMargin;
    }
}
