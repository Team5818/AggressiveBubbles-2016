package org.usfirst.frc.team5818.robot.modules.drivetrain;

import org.usfirst.frc.team5818.robot.RobotConstants;
import org.usfirst.frc.team5818.robot.encoders.EncoderManagerBase;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;

/**
 * A drive side is an arbitrary amount of talons that can be manipulated as a
 * whole set. The arbitrary amount of talons that can be manipulated may be an
 * integer between 1 and 2 inclusive.
 */
public class DriveSide extends EncoderManagerBase implements PIDOutput {

    private static final double powerLimit = 0.5;
    private static final boolean cubeCurve = true;

    private final CANTalon mainTalon;
    private final CANTalon secondaryTalon;
    private final Encoder encoder;
    private final boolean inverted;
    private PIDController pidLoop;

    /**
     * Creates a new DriveSide that controls the talons given.
     * 
     * @param encoder
     *            - The encoder for this side
     * @param mainTalon
     *            - The first talon to control
     * @param secondaryTalon
     *            - The second talon to control
     */
    public DriveSide(Encoder encoder, CANTalon mainTalon,
            CANTalon secondaryTalon) {
        this(encoder, mainTalon, secondaryTalon, false);
    }

    /**
     * Creates a new DriveSide that controls the talons given, and may be
     * inverted.
     * 
     * @param encoder
     *            - The encoder for this side
     * @param mainTalon
     *            - The first talon to control
     * @param secondaryTalon
     *            - The second talon to control
     * @param inverted
     *            - {@code true} if the argument of {@link #pidWrite(double)}
     *            should be negated
     */
    public DriveSide(Encoder encoder, CANTalon mainTalon,
            CANTalon secondaryTalon, boolean inverted) {
        if (mainTalon == null) {
            throw new IllegalArgumentException("mainTalon cannot be null");
        }
        if (encoder == null) {
            throw new IllegalArgumentException("encoder cannot be null");
        }
        this.encoder = encoder;
        this.mainTalon = mainTalon;
        this.secondaryTalon = secondaryTalon;
        this.inverted = inverted;
        encoder.setDistancePerPulse(RobotConstants.ROBOT_ENCODER_SCALE);
    }

    @Override
    public void pidWrite(double output) {
        if (inverted) {
            output *= -1;
        }

        if (cubeCurve)
            output = output * output * output;

        // Limit output.
        output = Math.signum(output) * Math.min(Math.abs(output), powerLimit);

        this.mainTalon.set(output);
        if (this.secondaryTalon != null) {
            this.secondaryTalon.set(output);
        }
    }

    @Override
    public void setDriveDistance(double dist) {
        encoder.reset();
        super.setDriveDistance(dist);
    }

    @Override
    public double getEncPosAbs() {
        return encoder.getDistance();
    }

    public void createPIDLoop(PIDSource source) {
        if (pidLoop != null) {
            pidLoop.free();
        }
        pidLoop = new PIDController(RobotConstants.PID_LOOP_P_TERM,
                RobotConstants.PID_LOOP_I_TERM, RobotConstants.PID_LOOP_D_TERM,
                source, this);
    }

    @Override
    public double getEncDelta() {
        return 0;
    }

    @Override
    public double peekEncDelta() {
        return 0;
    }

    @Override
    public double getPowerAccordingToDistance(double time) {
        return 0;
    }

}
