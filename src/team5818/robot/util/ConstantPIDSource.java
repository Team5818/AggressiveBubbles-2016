package team5818.robot.util;

public class ConstantPIDSource extends PIDSourceBase {

    private final double value;

    public ConstantPIDSource(double value) {
        this.value = value;
    }

    @Override
    public double pidGet() {
        return value;
    }

}
