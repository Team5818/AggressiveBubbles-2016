package org.usfirst.frc.team5818.robot.encoders;

/**
 * EncoderManager partial implementation that provides shared methods. All
 * implementations <b>MUST</b> call {@link #setDistanceDriven(double)} when any
 * distance is driven in order to have this class calculate correct values.
 */
public abstract class EncoderManagerBase implements EncoderManager {

    private static final double NO_DISTANCE = 0;
    private static final long NO_DISTANCE_BITS =
            Double.doubleToLongBits(NO_DISTANCE);
    protected double distanceToDrive;
    protected double distanceDriven;

    private boolean targetIsUndefined() {
        return Double.doubleToLongBits(distanceToDrive) == NO_DISTANCE_BITS;
    }

    /**
     * Sets the distance the robot has driven towards {@link #distanceToDrive}.
     * For convenience, calls to this are IGNORED instead of being erroneous
     * when there is no target.
     * 
     * @param dist
     *            - The distance
     */
    protected void setDistanceDriven(double dist) {
        if (targetIsUndefined()) {
            return;
        }
        distanceDriven = dist;
    }

    @Override
    public void setDriveDistance(double dist) {
        if (DEFAULT_MARGIN_OF_ERROR >= Math.abs(dist)) {
            throw new IllegalArgumentException(
                    "Cannot drive nowhere. To reset, use resetDistance().");
        }
        distanceToDrive = dist;
        distanceDriven = 0;
    }

    @Override
    public double getDistanceNotCovered() {
        if (targetIsUndefined()) {
            return 0;
        }
        return distanceToDrive - distanceDriven;
    }

    @Override
    public double getDistanceToCover() {
        return distanceToDrive;
    }

    @Override
    public double getDistanceCovered() {
        return distanceDriven;
    }

    @Override
    public void resetDistance() {
        distanceToDrive = NO_DISTANCE;
        distanceDriven = 0;
    }

    @Override
    public boolean isDistanceCovered(double errorMargin) {
        if (targetIsUndefined()) {
            return true;
        }
        return getDistanceToCover() < errorMargin;
    }

}
