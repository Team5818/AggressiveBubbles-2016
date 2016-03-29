package team5818.robot.commands;

import java.io.IOException;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.RobotCommon;
import team5818.robot.RobotConstants;
import team5818.robot.RobotDriver;
import team5818.robot.modules.Arm;
import team5818.robot.modules.FlyWheel;
import team5818.robot.modules.Track;
import team5818.robot.modules.drivetrain.DriveTrain;
import team5818.robot.util.Vector2d;

public class AutoAim extends Command {

    public static final double DEFAULT_Y_OFFSET = -5;
    private Track track;
    private DriveTrain drive;

    private double camFOV;

    public static double tolerance = FlyWheel.TOLERANCE;
    public double flyUpVel;
    public double flyLoVel;

    public static double defaultFlyUpVel = Preferences.getInstance()
            .getDouble("UpperFlyVel", FlyWheel.SHOOT_VELOCITY_UPPER);
    public static double defaultFlyLoVel = Preferences.getInstance()
            .getDouble("LowerFlyVel", FlyWheel.SHOOT_VELOCITY_LOWER);

    private static final FlyWheel flyUp =
            RobotCommon.runningRobot.upperFlywheel;
    private static final FlyWheel flyLo =
            RobotCommon.runningRobot.lowerFlywheel;

    public static boolean udp = true;

    public double imgHeight;
    public double imgWidth;
    public double blobCenterX;
    public double blobCenterY;
    public double blobWidth;
    public double blobHeight;
    private double power;
    // public double setX;
    // public double setY;
    public double locX;
    public double locY;
    public double blobOffset;
    public double slopX;
    public double slopY;

    public boolean isCenteredX;
    public boolean isCenteredY;
    public boolean done;
    private double MAX_POWER = 0.2;
    private double MIN_POWER = 0.08;

    /**
     * the offset in degrees.
     * 
     * @param offset
     */
    public AutoAim(double offset, double flyup, double flylo, double timeout) {
        track = RobotCommon.runningRobot.targeting;
        camFOV = RobotConstants.CAMFOV;

        this.flyUpVel = flyup;
        this.flyLoVel = flylo;

        setTimeout(timeout);

        imgHeight = 480;
        imgWidth = 320;
        blobCenterX = 0;
        blobCenterY = 0;
        blobWidth = 0;
        blobHeight = 0;
        blobOffset = offset;
        locX = 0;
        locY = 0;

        isCenteredX = false;
        isCenteredY = false;
        slopX = (RobotConstants.SLOP);
        slopY = (RobotConstants.SLOP);
        requires(RobotCommon.runningRobot.driveTrain);
        requires(RobotCommon.runningRobot.arm);
    }

    public AutoAim(double timeout) {
        this(DEFAULT_Y_OFFSET, defaultFlyUpVel, defaultFlyLoVel, timeout);
    }

    public AutoAim() {
        this(DEFAULT_Y_OFFSET, defaultFlyUpVel, defaultFlyLoVel, 2);
    }

    @Override
    protected void initialize() {
        SmartDashboard.putNumber("Is tracked", 0);
        track.GetData();

        if (track.blobCount > 0) {
            // imgHeight = track.imageHeight;
            // imgWidth = track.imageWidth;
            // blobWidth = track.blobWidth;
            // blobHeight = track.blobHeight;
            locX = track.blobLocX;
            locY = track.blobLocY + blobOffset;
            aimY();
            aim();
        }

        else {
            DriverStation.reportError("No Blobs Found Did Not track", false);
        }
    }

    public double calculateAngleX() {
        double setX = (((imgWidth / 2 - (locX))) / imgWidth * camFOV);
        SmartDashboard.putNumber("AutoAim X Error", setX);
        return setX;

    }

    public double calculateAngleY() {
        double setY = RobotCommon.runningRobot.arm.getAngle()
                + ((imgHeight / 2 - (locY))) / imgHeight * camFOV / 2
                + blobOffset;
        if (locY > imgHeight / 2) {
            setY -= 0;
        } else {
            setY -= 0;
        }
        return setY - 3;
    }

    public void aim() {
        if ((locX < (imgWidth / 2) + (slopX * (imgWidth))
                || (locX > (imgWidth / 2) + (slopX * imgWidth)))) {
        } else if (locX == (imgWidth / 2) + (slopX * imgWidth)) {
        } else {
            DriverStation.reportError("did not align", false);
        }

    }

    public void aimY() {
        if ((locY < (imgHeight / 2) + (slopY * imgHeight) + blobOffset)
                || (locY > (imgHeight / 2) + (slopY * imgHeight)
                        + blobOffset)) {
            RobotCommon.runningRobot.arm.goToAngle(calculateAngleY());
        } else if (locY == (imgHeight / 2) + (slopY * imgHeight) + blobOffset) {
        } else {
            DriverStation.reportError("did not align", false);
        }
    }

    @Override
    protected void execute() {
        track.GetData();

        if (track.blobCount > 0) {
            locX = track.blobLocX;
            locY = track.blobLocY;
            SmartDashboard.putNumber("locX", locX);
            calculateAngleX();
            double angle = calculateAngleX();
            power = angle * .015;
            if (power > MAX_POWER)
                power = MAX_POWER;
            else if (power < -MAX_POWER)
                power = -MAX_POWER;
            if (power > 0 && power < MIN_POWER) {
                power = MIN_POWER;
            } else if (power < 0 && power > -MIN_POWER) {
                power = -MIN_POWER;
            }
            RobotCommon.runningRobot.driveTrain
                    .setPower(new Vector2d(-power, power));
        }
    }

    @Override
    protected boolean isFinished() {
        boolean flyToSpeed = (flyUpVel <= flyUp.getRPS() + tolerance
                && flyUpVel >= flyUp.getRPS() - tolerance
                && flyLoVel <= flyLo.getRPS() + tolerance
                && flyLoVel >= flyLo.getRPS() - tolerance);

        boolean atTarget = Math.abs(calculateAngleX()) <= 1.5;

        if (isTimedOut()) {
            DriverStation.reportError("Timedout AutoAim", false);
            DriverStation.reportError("Flywheel to speed: " + flyToSpeed
                    + "At angle: " + atTarget, false);
            return true;
        }

        return ((flyToSpeed && atTarget) || this.isTimedOut());
    }

    @Override
    protected void end() {
        SmartDashboard.putNumber("Is aimed", 100);
        RobotCommon.runningRobot.driveTrain.setPower(new Vector2d(0, 0));
    }

    @Override
    protected void interrupted() {
        RobotCommon.runningRobot.driveTrain.setPower(new Vector2d(0, 0));
    }
}
