package team5818.robot.modules.drivetrain;

import java.util.Objects;
import java.util.stream.Stream;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.RobotConstants;
import team5818.robot.encoders.EncoderManager;
import team5818.robot.util.PIDSourceBase;

/**
 * A drive side is an arbitrary amount of talons that can be manipulated as a
 * whole set. The arbitrary amount of talons that can be manipulated may be an
 * integer between 1 and 3 inclusive.
 */
public class DriveSide implements EncoderManager, PIDOutput, MovingControl {

    private static final double DEFAULT_MAX_POWER = 1.0;

    private final CANTalon mainTalon;
    private final CANTalon secondaryTalon;
    private final CANTalon thirdTalon;
    private final PIDController pidLoop;
    private final PIDSourceBase pidSource;

    /**
     * Creates a new DriveSide that controls the talons given.
     * 
     * @param mainTalon
     *            - The first talon to control
     * @param secondaryTalon
     *            - The second talon to control
     * @param thirdTalon
     *            - The second talon to control
     */
    public DriveSide(CANTalon mainTalon, CANTalon secondaryTalon,
            CANTalon thirdTalon) {
        this(mainTalon, secondaryTalon, thirdTalon, false);
    }

    /**
     * Creates a new DriveSide that controls the talons given, and may be
     * inverted.
     * 
     * @param mainTalon
     *            - The first talon to control
     * @param secondaryTalon
     *            - The second talon to control
     * @param thirdTalon
     *            - The second talon to control
     * @param inverted
     *            - {@code true} if the argument of {@link #pidWrite(double)}
     *            should be negated
     */
    public DriveSide(CANTalon mainTalon, CANTalon secondaryTalon,
            CANTalon thirdTalon, boolean inverted) {
        if (mainTalon == null) {
            throw new IllegalArgumentException("mainTalon cannot be null");
        }
        this.mainTalon = mainTalon;
        this.secondaryTalon = secondaryTalon;
        this.thirdTalon = thirdTalon;

        Stream.of(this.mainTalon, this.secondaryTalon, this.thirdTalon)
                .filter(Objects::nonNull).forEach(talon -> {
                    talon.setInverted(inverted);
                    talon.reverseOutput(inverted);
                });

        this.mainTalon.reverseSensor(inverted);

        pidLoop = new PIDController(RobotConstants.PID_LOOP_P_TERM,
                RobotConstants.PID_LOOP_I_TERM, RobotConstants.PID_LOOP_D_TERM,
                pidSource = new PIDSourceBase() {

                    @Override
                    public double pidGet() {
                        double val;
                        if (this.getPIDSourceType() == PIDSourceType.kRate) {
                            val = getVelocity();
                        } else {
                            val = getEncPosAbs();
                        }
                        SmartDashboard.putNumber("RightVals", val);
                        return val;
                    }

                }, this);
        pidLoop.disable();
        configureEncoderTalon();
    }

    private void configureEncoderTalon() {
        mainTalon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
        mainTalon.setPosition(0);
    }

    @Override
    public void pidWrite(double output) {
        this.mainTalon.set(output);
        dumpPower(this.mainTalon);
        if (this.secondaryTalon != null) {
            this.secondaryTalon.set(output);
            dumpPower(this.secondaryTalon);
        }
        if (this.thirdTalon != null) {
            this.thirdTalon.set(output);
            dumpPower(this.thirdTalon);
        }
    }

    public double dumpPower(CANTalon talon) {
        return talon.get();
    }

    public double dumpPower(int talonNum) {
        CANTalon talon;
        if (talonNum == 3) {
            talon = this.thirdTalon;
        }
        if (talonNum == 2) {
            talon = this.secondaryTalon;
        } else {
            talon = this.mainTalon;
        }
        return talon.get();
    }

    @Override
    public void setDriveDistance(double dist) {
        pidLoop.setSetpoint(getEncPosAbs() + dist);
    }

    @Override
    public double getEncPosAbs() {
        return mainTalon.getPosition() * RobotConstants.ROBOT_ENCODER_SCALE;
    }

    @Override
    public double getEncPosRaw() {
        return mainTalon.getPosition();
    }

    /**
     * The velocity of the wheel in units of Inches/Seconds.
     * 
     * @return Velocity of wheels in Inches/Seconds.
     */
    public double getVelocity() {
        double vel = mainTalon.getEncVelocity() * 10.0
                * RobotConstants.ROBOT_ENCODER_SCALE;
        return vel;
    }

    public PIDController getPIDController() {
        return pidLoop;
    }

    @Override
    public void setPower(double power) {
        pidLoop.disable();
        // Delegate to power.
        pidWrite(power);
    }

    /**
     * Sets the speed of the motor. The ratio calculates using the gearbox of
     * ratios and wheel size.
     * 
     * @param vel
     *            The desired velocity in inches per second.
     */
    @Override
    public void setVelocity(double vel) {
        pidSource.setPIDSourceType(PIDSourceType.kRate);
        pidLoop.setOutputRange(-DEFAULT_MAX_POWER, DEFAULT_MAX_POWER);
        pidLoop.setSetpoint(vel);
        pidLoop.enable();
    }

    @Override
    public void setDriveDistance(double dist, double maxPower) {
        pidLoop.disable();
        pidSource.setPIDSourceType(PIDSourceType.kDisplacement);
        pidLoop.setOutputRange(-maxPower, maxPower);
        mainTalon.setEncPosition(0);
        pidLoop.setSetpoint(dist);
        pidLoop.setPercentTolerance(80);
        pidLoop.enable();
    }

}
