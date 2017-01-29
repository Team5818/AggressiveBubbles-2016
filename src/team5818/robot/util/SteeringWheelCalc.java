package team5818.robot.util;

public class SteeringWheelCalc {

    public static double calculate(double original, boolean cap) {
        double locked = cap ? MathUtil.cap(original, -0.5, 0.5) : original;
        double span = locked * 2;
        double linearized = linearize(span);
        return linearized;
    }

    private static double linearize(double span) {
        final double lowMultiplier = 0.05;
        return span * ((Math.abs(span) * (1 - lowMultiplier)) + lowMultiplier);
    }

}
