package team5818.robot.util;

/**
 * Helpful math functions.
 */
public class MathUtil {

    /**
     * Calculates the distance/length of an arc defined as part of a circle with
     * radius {@code radius} plus a central angle of {@code degrees}.
     * 
     * @param radius
     *            - The radius of the arc/circle
     * @param degrees
     *            - The central angle of the arc
     * @return the length of the arc
     */
    public static double distanceOfArc(double radius, double degrees) {
        return radius * degrees / 180 * Math.PI;
    }
    
    public static double cap(double original, double lowerBound, double upperBound) {
        if (original < lowerBound) {
            return lowerBound;
        } else if (original > upperBound) {
            return upperBound;
        }
        return original;
    }

    private MathUtil() {
    }

}
