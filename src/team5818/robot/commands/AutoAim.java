package team5818.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.RobotCommon;
import team5818.robot.RobotConstants;
import team5818.robot.RobotDriver;
import team5818.robot.modules.Arm;
import team5818.robot.modules.Track;
import team5818.robot.modules.drivetrain.DriveTrain;
import team5818.robot.util.Vector2d;

public class AutoAim extends Command {

    public static final double DEFAULT_Y_OFFSET = -12;
    private Track track;
    private DriveTrain drive;

    private double camFOV;

    public double maxTime;
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
    private double MAX_POWER = 0.3;
    private double MIN_POWER = 0.14;

    /**
     * the offset in degrees.
     * 
     * @param offset
     */
    public AutoAim(double offset) {
        track = RobotCommon.runningRobot.targeting;
        camFOV = RobotConstants.CAMFOV;

        maxTime = 4;
        setTimeout(maxTime);

        imgHeight = 0;
        imgWidth = 0;
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
    
    public AutoAim() {
        this(DEFAULT_Y_OFFSET);
    }

    @Override
    protected void initialize() {
        SmartDashboard.putNumber("Is tracked", 0);
        track.GetData();
        if (track.blobCount > 0) {
            imgHeight = track.imageHeight;
            imgWidth = track.imageWidth;
            blobWidth = track.blobWidth;
            blobHeight = track.blobHeight;
            locX = track.blobLocX;
            locY = track.blobLocY + blobOffset;
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
        return setY;
    }

    /*
     * public void setDrive(double left, double right) { Vector2d power = new
     * Vector2d(left, right); drive.setPower(power); }
     */

    public void aim() {
        if ((locX < (imgWidth / 2) + (slopX * (imgWidth))
                || (locX > (imgWidth / 2) + (slopX * imgWidth)))) {
            // new SpinRobot(calculateAngleX()).start();
        } else if (locX == (imgWidth / 2) + (slopX * imgWidth)) {
        } else {
            DriverStation.reportError("did not align", false);
        }

        // calculateAngleY();
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
            imgHeight = track.imageHeight;
            imgWidth = track.imageWidth;
            blobWidth = track.blobWidth;
            blobHeight = track.blobHeight;
            locX = track.blobLocX;
            locY = track.blobLocX;
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
            /*
             * if (blobWidth < 70) { blobOffset = track.blobOffsetClose; } else
             * { blobOffset = track.blobOffsetFar; }
             */
        }
    }

    @Override
    protected boolean isFinished() {
        if(isTimedOut()) {
            DriverStation.reportError("Timedout AutoAim", false);
            return true;
        }
        return Math.abs(calculateAngleX()) <= 0.5;

    }

    @Override
    protected void end() {
        SmartDashboard.putNumber("Is aimed", 100);
        RobotCommon.runningRobot.driveTrain
        .setPower(new Vector2d(0, 0));
    }

    @Override
    protected void interrupted() {
        RobotCommon.runningRobot.driveTrain
        .setPower(new Vector2d(0, 0));
    }
}
