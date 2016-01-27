package org.usfirst.frc.team5818.robot.util;

import edu.wpi.first.wpilibj.Joystick;

/**
 * Helper class for vectors.s
 */
public final class Vectors {

    /**
     * Creates a vector from the joystick axis values.
     * 
     * @param stick
     *            - The joystick to pull values from
     * @return A new vector of the values
     */
    public static Vector2d fromJoystick(Joystick stick) {
        return new Vector2d(stick.getX(), -stick.getY());
    }

    /**
     * Cannot be constructed.
     */
    private Vectors() {
    }

}
