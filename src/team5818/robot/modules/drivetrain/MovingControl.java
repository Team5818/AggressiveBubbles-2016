package team5818.robot.modules.drivetrain;

/**
 * For controlling things that move.
 */
public interface MovingControl {

    /**
     * Set the power directly to the underlying talons.
     * 
     * @param power
     *            - the power
     */
    void setPower(double power);

    /**
     * Set the velocity that should be maintained.
     * 
     * @param vel
     *            - the velocity
     */
    void setVelocity(double vel);

    /**
     * Set the distance that should be driven.
     * 
     * @param dist
     *            - the distance
     * @param maxPower
     *            - the max power to use to drive the distance
     */
    void setDriveDistance(double dist, double maxPower);

}
