package org.usfirst.frc.team5818.robot.modules.drivetrain;

import org.usfirst.frc.team5818.robot.RobotConstants;
import org.usfirst.frc.team5818.robot.encoders.EncoderManager;
import org.usfirst.frc.team5818.robot.util.PIDSourceBase;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;

/**
 * A drive side is an arbitrary amount of talons that can be manipulated as a
 * whole set. The arbitrary amount of talons that can be manipulated may be an
 * integer between 1 and 2 inclusive.
 */
public class DriveSide implements EncoderManager, PIDOutput {

    private static final double POWER_LIMIT = 0.5;

    private final CANTalon mainTalon;
    private final CANTalon secondaryTalon;
    private final boolean inverted;
    private final PIDController pidLoop;

    /**
     * Creates a new DriveSide that controls the talons given.
     * 
     * @param mainTalon
     *            - The first talon to control
     * @param secondaryTalon
     *            - The second talon to control
     */
    public DriveSide(CANTalon mainTalon, CANTalon secondaryTalon) {
        this(mainTalon, secondaryTalon, false);
    }

    /**
     * Creates a new DriveSide that controls the talons given, and may be
     * inverted.
     * 
     * @param mainTalon
     *            - The first talon to control
     * @param secondaryTalon
     *            - The second talon to control
     * @param inverted
     *            - {@code true} if the argument of {@link #pidWrite(double)}
     *            should be negated
     */
    public DriveSide(CANTalon mainTalon, CANTalon secondaryTalon,
            boolean inverted) {
        if (mainTalon == null) {
            throw new IllegalArgumentException("mainTalon cannot be null");
        }
        this.mainTalon = mainTalon;
        this.secondaryTalon = secondaryTalon;
        this.inverted = inverted;
        pidLoop = new PIDController(RobotConstants.PID_LOOP_P_TERM,
                RobotConstants.PID_LOOP_I_TERM, RobotConstants.PID_LOOP_D_TERM,
                new PIDSourceBase() {

                    @Override
                    public double pidGet() {
                        return mainTalon.getPosition();
                    }

                }, this);
        configureEncoderTalon();
    }

    private void configureEncoderTalon() {
        mainTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        mainTalon.setPosition(0);
    }

    @Override
    public void pidWrite(double output) {
        if (inverted) {
            output *= -1;
        }

        // Limit output.
        output = Math.signum(output) * Math.min(Math.abs(output), POWER_LIMIT);

        this.mainTalon.set(output);
        if (this.secondaryTalon != null) {
            this.secondaryTalon.set(output);
        }
    }

    @Override
    public void setDriveDistance(double dist) {
        mainTalon.setPosition(0);
        pidLoop.setSetpoint(dist);
    }

    @Override
    public double getEncPosAbs() {
        return mainTalon.getPosition() * RobotConstants.ROBOT_ENCODER_SCALE;
    }

    @Override
    public double getEncPosRaw() {
        return mainTalon.getPosition();
    }

}
