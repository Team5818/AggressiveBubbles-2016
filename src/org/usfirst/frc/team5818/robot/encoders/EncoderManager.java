package org.usfirst.frc.team5818.robot.encoders;

/**
 * Implementations of this interface allow for control of driving to a target
 * that is a certain distance away, especially useful during autonomous.
 */
public interface EncoderManager {

    /**
     * Get the position of this EncoderManager's encoder in absolute terms.
     * 
     * @return absolute position of the Encoder.
     */
    double getEncPosAbs();

    /**
     * Get the position of this EncoderManager's encoder in absolute terms, with
     * no conversions.
     * 
     * @return absolute position of the Encoder.
     */
    double getEncPosRaw();

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

}
