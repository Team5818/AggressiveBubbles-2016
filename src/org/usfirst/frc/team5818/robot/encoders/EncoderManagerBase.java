package org.usfirst.frc.team5818.robot.encoders;

public abstract class EncoderManagerBase implements EncoderManager {

    private static final double NO_DISTANCE = 0;
    private static final long NO_DISTANCE_BITS =
            Double.doubleToLongBits(NO_DISTANCE);
    protected double distanceToDrive;
    protected double distanceDriven;
    
    protected void setDistanceDriven(double dist) {
        distanceDriven = dist;
    }

    @Override
    public void setDriveDistance(double dist) {
        if (-DEFAULT_MARGIN_OF_ERROR < dist || dist < DEFAULT_MARGIN_OF_ERROR) {
            throw new IllegalArgumentException(
                    "Cannot drive nowhere. To reset, use resetDistance().");
        }
        distanceToDrive = dist;
        distanceDriven = 0;
    }

    @Override
    public double getDistanceNotCovered() {
        if (Double.doubleToLongBits(distanceToDrive) == NO_DISTANCE_BITS) {
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
        return getDistanceToCover() < errorMargin;
    }

}
