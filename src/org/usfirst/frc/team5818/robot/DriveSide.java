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
    private final boolean inverted;

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
        this.inverted = false;
    }
    
    public DriveSide(CANTalon mainTalon, CANTalon secondaryTalon, boolean inverted) {
        if (mainTalon == null) {
            throw new IllegalArgumentException("mainTalon cannot be null");
        }
        this.mainTalon = mainTalon;
        this.secondaryTalon = secondaryTalon;
        this.inverted = inverted;
    }

    @Override
    public void pidWrite(double output) {
        if(inverted)
        {
            output *= -1;
        }
        this.mainTalon.set(output);
        if (this.secondaryTalon != null) {
            this.secondaryTalon.set(output);
        }
    }

}
