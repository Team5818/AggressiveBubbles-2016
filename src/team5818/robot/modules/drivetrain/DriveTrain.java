package team5818.robot.modules.drivetrain;

import java.util.stream.Stream;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;
import team5818.robot.RobotConstants;
import team5818.robot.modules.Module;
import team5818.robot.util.MathUtil;
import team5818.robot.util.Vector2d;

/**
 * The entire drive thing.
 */
public class DriveTrain extends Subsystem implements Module {

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

    /**
     * The Maximum velocity the flywheel can reach.
     */
    public static final double MAX_VELOCITY = 175;

    // The default max power output.
    public static final double DEFAULT_MAX_POWER = 1.0;

    // The driving mode that the robot is in.
    private static int driveMode = MODE_POWER;

    private static final boolean SIX_TALONS =
            Preferences.getInstance().getBoolean("sixTalons", false);
    private static final CANTalon LEFT_FRONT =
            new CANTalon(RobotConstants.TALON_LEFT_FRONT);
    private static final CANTalon LEFT_MIDDLE;
    private static final CANTalon LEFT_BACK =
            new CANTalon(RobotConstants.TALON_LEFT_BACK);
    private static final CANTalon RIGHT_FRONT =
            new CANTalon(RobotConstants.TALON_RIGHT_FRONT);
    private static final CANTalon RIGHT_MIDDLE;
    private static final CANTalon RIGHT_BACK =
            new CANTalon(RobotConstants.TALON_RIGHT_BACK);

    static {
        CANTalon lm = null;
        if (SIX_TALONS) {
            lm = new CANTalon(RobotConstants.TALON_LEFT_MIDDLE);
        }
        CANTalon rm = null;
        if (SIX_TALONS) {
            rm = new CANTalon(RobotConstants.TALON_RIGHT_MIDDLE);
        }
        LEFT_MIDDLE = lm;
        RIGHT_MIDDLE = rm;
    }

    private final DriveSide left =
            new DriveSide(LEFT_FRONT, LEFT_MIDDLE, LEFT_BACK, false);
    // Right motors are reversed.
    private final DriveSide right =
            new DriveSide(RIGHT_FRONT, RIGHT_MIDDLE, RIGHT_BACK, true);

    private DriveCalculator driveCalculator = ArcadeDriveCalculator.INSTANCE;

    /**
     * @return the {@link PIDOutput} for the left side
     */
    public DriveSide getLeftMotors() {
        return left;
    }

    /**
     * @return the {@link PIDOutput} for the right side
     */
    public DriveSide getRightMotors() {
        return right;
    }

    /**
     * Sets the power based on the {@link Vector2d} {@code x} and {@code y}.
     * 
     * @param power
     *            - The vector containing power values. Left is
     *            {@link Vector2d#getX()} and right is {@link Vector2d#getY()}
     */
    public void setPower(Vector2d power) {
        left.setPower(power.getX());
        right.setPower(power.getY());
        setDriveMode(MODE_POWER);
    }

    public void setVelocity(Vector2d velocities) {
        left.setVelocity(velocities.getX());
        right.setVelocity(velocities.getY());
        setDriveMode(MODE_VELOCITY);
    }

    public double getAverageDistance() {
        double avgDist = (right.getEncPosAbs() + left.getEncPosAbs()) / 2;
        return avgDist;
    }

    public void setDriveDistance(double dist) {
        setDriveDistance(dist, DEFAULT_MAX_POWER);
    }

    public void setDriveDistance(double dist, double maxPower) {
        left.setDriveDistance(dist, maxPower);
        right.setDriveDistance(dist, maxPower);
        setDriveMode(MODE_DISTANCE);
    }

    public void setDriveDistance(double distLeft, double distRight,
            double maxPower) {
        left.setDriveDistance(distLeft, maxPower);
        right.setDriveDistance(distRight, maxPower);
        setDriveMode(MODE_DISTANCE);
    }

    public void setDriveCalculator(DriveCalculator driveCalculator) {
        if (driveCalculator == null) {
            throw new NullPointerException("fix your code genius.");
        }
        this.driveCalculator = driveCalculator;
    }

    /**
     * rotates the robot by the specified degrees. Make negative to rotate
     * counter clockwise
     * 
     * @param degrees
     *            Degrees to rotate the robot
     */
    public void setSpinAngle(double degrees) {
        setSpinAngle(degrees, DEFAULT_MAX_POWER);
    }

    /**
     * rotates the robot by the specified degrees. Make negative to rotate
     * counter clockwise
     * 
     * @param degrees
     *            Degrees to rotate the robot
     */
    public void setSpinAngle(double degrees, double maxPower) {
        // To rotate X degrees, simply move left side forward by W
        // and move right side backwards by W
        double distance = MathUtil
                .distanceOfArc(RobotConstants.ROBOT_WIDTH_IN_INCHES, degrees);
        setDriveDistance(distance, -distance, maxPower);
    }

    @Override
    public void initModule() {
        LiveWindow.addActuator("Left", "Front", LEFT_FRONT);
        if (SIX_TALONS) {
            LiveWindow.addActuator("Left", "Middle", LEFT_MIDDLE);
        }
        LiveWindow.addActuator("Left", "Back", LEFT_BACK);
        LiveWindow.addActuator("Right", "Front", RIGHT_FRONT);
        if (SIX_TALONS) {
            LiveWindow.addActuator("Right", "Middle", RIGHT_MIDDLE);
        }
        LiveWindow.addActuator("Right", "Back", RIGHT_BACK);
    }

    @Override
    public void teleopPeriodicModule() {
        SmartDashboard.putNumber("RightCounts", right.getEncPosRaw());
        SmartDashboard.putNumber("LeftCounts", left.getEncPosRaw());
        SmartDashboard.putNumber("RightVel", right.getVelocity());
        SmartDashboard.putNumber("LeftVel", left.getVelocity());
        SmartDashboard.putNumber("LeftPIDInput", left.getPIDInput());
        SmartDashboard.putNumber("RightPIDInput", right.getPIDInput());

    }

    @Override
    public void autoPeriodicModule() {
        SmartDashboard.putNumber("RightDist", right.getEncPosAbs());
        SmartDashboard.putNumber("LeftDist", left.getEncPosAbs());
        SmartDashboard.putNumber("RightVel", right.getVelocity());
        SmartDashboard.putNumber("LeftVel", left.getVelocity());

    }

    /**
     * @return current drive mode
     */
    public static int getDriveMode() {
        return driveMode;
    }

    /**
     * 
     * @return The drive calculator being used currently.
     */
    public DriveCalculator getDriveCalculator() {
        return driveCalculator;
    }

    /**
     * @param driveMode
     *            the drive mode you want to put the robot in
     */
    public void setDriveMode(int driveMode) {
        this.driveMode = driveMode;
    }

    @Override
    public void endModule() {
    }

    @Override
    public void initTest() {
    }

    @Override
    public void initTeleop() {
    }

    @Override
    public void initAutonomous() {
    }

    @Override
    public void testPeriodic() {

    }

    @Override
    protected void initDefaultCommand() {
        // TODO Auto-generated method stub

    }

}
