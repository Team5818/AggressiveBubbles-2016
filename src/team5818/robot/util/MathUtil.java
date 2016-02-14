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
        return radius * Math.toRadians(degrees);
    }

    private MathUtil() {
    }

}
