package org.usfirst.frc.team5818.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Talon;

/**
 * A talon set holds an arbitrary amount of talons that can be manipulated as a
 * whole set. The arbitrary amount of talons that can be manipulated may be an
 * integer between 1 and 2 inclusive.
 */
public class DriveSide implements PIDOutput {

    private final CANTalon mainTalon;
    private final CANTalon secondaryTalon;

    /**
     * Creates a new DriveSide that controls the talons given.
     * 
     * @param mainTalon
     *            - The first talon to control
     */
    public DriveSide(CANTalon mainTalon, CANTalon secondaryTalon) {
        if (mainTalon == null) {
            throw new IllegalArgumentException("mainTalon cannot be null");
        }
        this.mainTalon = mainTalon;
        this.secondaryTalon = secondaryTalon;
    }

    @Override
    public void pidWrite(double output) {
        this.mainTalon.set(output);
        if (this.secondaryTalon != null) {
            this.secondaryTalon.set(output);
        }
    }

}
