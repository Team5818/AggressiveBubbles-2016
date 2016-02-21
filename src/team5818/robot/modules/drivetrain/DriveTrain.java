package team5818.robot.modules.drivetrain;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.RobotConstants;
import team5818.robot.modules.Module;
import team5818.robot.util.Vector2d;

/**
 * The entire drive thing.
 */
public class DriveTrain implements Module {

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
        left.pidWrite(power.getX());
        right.pidWrite(power.getY());
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
        try {
            double p = Double.parseDouble(SmartDashboard.getString("pS",
                    "" + RobotConstants.PID_LOOP_P_TERM));
            double i = Double.parseDouble(SmartDashboard.getString("iS",
                    "" + RobotConstants.PID_LOOP_I_TERM));
            double d = Double.parseDouble(SmartDashboard.getString("dS",
                    "" + RobotConstants.PID_LOOP_D_TERM));
            right.getPIDController().setPID(p, i, d);
        } catch (NumberFormatException e) {
        }
        try {
            if (left.getPIDController().onTarget()) {
                left.getPIDController().disable();
            }
        } catch (Exception e) {
        }
        try {
            if (right.getPIDController().onTarget()) {
                right.getPIDController().disable();
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void endModule() {
    }

    @Override
    public void initTest() {
    }

    @Override
    public void initTeleop() {
        SmartDashboard.putString("pS", "" + RobotConstants.PID_LOOP_P_TERM);
        SmartDashboard.putString("iS", "" + RobotConstants.PID_LOOP_I_TERM);
        SmartDashboard.putString("dS", "" + RobotConstants.PID_LOOP_D_TERM);
    }

    @Override
    public void initAutonomous() {
        // TODO Auto-generated method stub

    }

    @Override
    public void testPeriodic() {
        // TODO Auto-generated method stub

    }

}
