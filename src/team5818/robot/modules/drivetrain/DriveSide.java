package team5818.robot.modules.drivetrain;

import java.util.Objects;
import java.util.stream.Stream;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.RobotConstants;
import team5818.robot.encoders.EncoderManager;
import team5818.robot.util.BetterPIDController;
import team5818.robot.util.PIDSourceBase;

/**
 * A drive side is an arbitrary amount of talons that can be manipulated as a
 * whole set. The arbitrary amount of talons that can be manipulated may be an
 * integer between 1 and 3 inclusive.
 */
public class DriveSide implements EncoderManager, PIDOutput, MovingControl {


    /**
     * The Maximum velocity the flywheel can reach.
     */
    public static final double MAX_VELOCITY = 175;

    /**
     * The mode for setting direct power to the drive side.
     */
    public static final int MODE_POWER = 0;
    /**
     * The mode for setting velocity to the drive side.
     */
    public static final int MODE_VELOCITY = 1;
    /**
     * The mode for setting drive distance to the drive side.
     */
    public static final int MODE_DISTANCE = 2;

    // The default max power output.
    private static final double DEFAULT_MAX_POWER = 1.0;

    // The driving mode that the robot is in.
    public static int driveMode = MODE_POWER;

    // Initialize all the pid constants.
    private static double distanceKp = 0.12;
    private static double distanceKi = 0.00000001;
    private static double distanceKd = 0.05;
    private static double velocityKp = 0.0012;
    private static double velocityKi = 0.0001;
    private static double velocityKd = 0.0;
    private static double velocityKf = 0.0;
    private double encoderScale = -.020603;
   private double pidInput = 0.0; 

    // Initialize objects.
    private final CANTalon mainTalon;
    private final CANTalon secondaryTalon;
    private final CANTalon thirdTalon;
    private final PIDSourceBase pidSource;
    private BetterPIDController pidLoop;
    private int cyclesUntilAttemptStop;

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

        distanceKp = Preferences.getInstance().getDouble("DistanceKp", 0.12);
        distanceKi =
                Preferences.getInstance().getDouble("DistanceKi", 0.00000001);
        distanceKd = Preferences.getInstance().getDouble("DistanceKd", 0.05);
        velocityKp = Preferences.getInstance().getDouble("VelocityKp", 0.0012);
        velocityKi = Preferences.getInstance().getDouble("VelocityKi", 0.0001);
        velocityKd =
                Preferences.getInstance().getDouble("Velocity" + "Kd", 0.0);
        velocityKf = Preferences.getInstance().getDouble("VelocityKf", 0.0);
        encoderScale = Preferences.getInstance().getDouble("EncoderScale", -.020603);

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
        pidSource = new PIDSourceBase() {

            @Override
            public double pidGet() {
                double val;
                if (this.getPIDSourceType() == PIDSourceType.kRate) {
                    val = getVelocity();
                } else {
                    val = getEncPosAbs();
                }
                pidInput = val;
                return val;
            }

        };

        resetPIDLoop();
        pidLoop.disable();
        configureEncoderTalon();
    }

    private void resetPIDLoop() {
        if (pidLoop != null) {
            // pidLoop.reset();
            // pidLoop.free();
        } else {
            pidLoop = new BetterPIDController(distanceKp, distanceKi,
                    distanceKd, pidSource, this);
            pidLoop.setAbsoluteTolerance(.5);
        }
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
        setDriveDistance(dist, DEFAULT_MAX_POWER);
    }

    @Override
    public double getEncPosAbs() {
        return mainTalon.getPosition() * encoderScale;
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
                * encoderScale;
        if (mainTalon.getInverted()) {
            vel = -vel;
        }
        return vel;
    }
    
    public double getPIDInput(){
        return pidInput;
    }

    /**
     * Returns the current driving mode. ie velocity, distance, power.
     * 
     * @return the current driving mode.
     */
    public static int getMode() {
        return driveMode;
    }

    public BetterPIDController getPIDController() {
        return pidLoop;
    }

    @Override
    public void setPower(double power) {
        if (pidLoop.isEnabled() || DriveTrain.getDriveMode() != DriveTrain.MODE_POWER) {
            pidLoop.disable();
        }
        setCoast(false);
        setDriveMode(MODE_POWER);
        pidWrite(power);
    }

    private void setDriveMode(int dm) {
        driveMode = dm;
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
        vel = Math.min(DriveTrain.MAX_VELOCITY, Math.max(-DriveTrain.MAX_VELOCITY, vel));
        if (DriveTrain.getDriveMode() != DriveTrain.MODE_VELOCITY) {
            resetPIDLoop();
            pidSource.setPIDSourceType(PIDSourceType.kRate);
        }
        pidLoop.enable();
        pidLoop.setPID(velocityKp, velocityKi, velocityKd, velocityKf);
        // setPIDFromSmart();
        pidLoop.setOutputRange(-DEFAULT_MAX_POWER, DEFAULT_MAX_POWER);
        pidLoop.setContinuous();
        setCoast(true);
        pidLoop.setSetpoint(vel);
    }

    private void setCoast(boolean b) {
        mainTalon.enableBrakeMode(!b);
        secondaryTalon.enableBrakeMode(!b);
        thirdTalon.enableBrakeMode(!b);
    }

    @Override
    public void setDriveDistance(double dist, double maxPower) {
        setDriveMode(MODE_DISTANCE);
        resetPIDLoop();
        pidLoop.setPID(distanceKp, distanceKi, distanceKd);
        // setPIDFromSmart();
        cyclesUntilAttemptStop = 5;
        pidSource.setPIDSourceType(PIDSourceType.kDisplacement);
        pidLoop.setOutputRange(-maxPower, maxPower);
        mainTalon.setPosition(0);
        pidLoop.setContinuous();
        setCoast(false);
        pidLoop.setSetpoint(dist);
        SmartDashboard.putNumber("distance", dist);
        pidLoop.enable();
    }

    public void setPIDFromSmart() {
        try {
            double p = Double.parseDouble(
                    SmartDashboard.getString("pS", "" + velocityKp));
            double i = Double.parseDouble(
                    SmartDashboard.getString("iS", "" + velocityKi));
            double d = Double.parseDouble(
                    SmartDashboard.getString("dS", "" + velocityKd));
            double f =
                    Double.parseDouble(SmartDashboard.getString("fS", "0.0"));
            pidLoop.setPID(p, i, d, f);
        } catch (NumberFormatException e) {
        }
    }

    public void attemptStopIfOnTarget() {
        cyclesUntilAttemptStop = Math.max(0, cyclesUntilAttemptStop - 1);
        if (cyclesUntilAttemptStop <= 0) {
            try {
                if (pidLoop.onTarget()) {
                    pidLoop.disable();
                }
            } catch (Exception e) {
            }
        }
    }

}
