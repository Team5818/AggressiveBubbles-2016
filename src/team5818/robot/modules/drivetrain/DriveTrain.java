package team5818.robot.modules.drivetrain;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.RobotConstants;
import team5818.robot.modules.Module;
import team5818.robot.util.Vector2d;

/**
 * The entire drive thing.
 */
public class DriveTrain implements Module {

    private static final CANTalon LEFT_FRONT =
            new CANTalon(RobotConstants.TALON_LEFT_FRONT);
    private static final CANTalon LEFT_MIDDLE =
            new CANTalon(RobotConstants.TALON_LEFT_MIDDLE);
    private static final CANTalon LEFT_BACK =
            new CANTalon(RobotConstants.TALON_LEFT_BACK);
    private static final CANTalon RIGHT_FRONT =
            new CANTalon(RobotConstants.TALON_RIGHT_FRONT);
    private static final CANTalon RIGHT_MIDDLE =
            new CANTalon(RobotConstants.TALON_RIGHT_MIDDLE);
    private static final CANTalon RIGHT_BACK =
            new CANTalon(RobotConstants.TALON_RIGHT_BACK);

    private final DriveSide left =
            new DriveSide(LEFT_FRONT, LEFT_MIDDLE, LEFT_BACK, false);
    // Right motors are reversed.
    private final DriveSide right =
            new DriveSide(RIGHT_FRONT, RIGHT_MIDDLE, RIGHT_BACK, true);

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
        //SmartDashboard.putString("DB/String 7", power.toString());
        left.pidWrite(power.getX());
        right.pidWrite(power.getY());
    }

    @Override
    public void initModule() {
    }

    @Override
    public void teleopPeriodicModule() {
    }

    @Override
    public void endModule() {
    }

    public void runTalon(int talon) {
        if (talon == 0) {
            LEFT_FRONT.set(0.25);
        }
        if (talon == 1) {
            LEFT_MIDDLE.set(0.25);
        }
        if (talon == 2) {
            LEFT_BACK.set(0.25);
        }
        if (talon == 3) {
            RIGHT_FRONT.set(0.25);
        }
        if (talon == 4) {
            RIGHT_MIDDLE.set(0.25);
        }
        if (talon == 5) {
            RIGHT_BACK.set(0.25);
        }
    }

}
