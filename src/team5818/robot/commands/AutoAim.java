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
import team5818.robot.util.LinearLookupTable;
import team5818.robot.util.Vector2d;

public class AutoAim extends Command {

    public static final double DEFAULT_Y_OFFSET = -8; //calibrated for lowbar
    public static final double DEFAULT_TIMEOUT = 2;
    public static boolean UDP = true;

    private static double defaultFlyUpVel = Preferences.getInstance()
            .getDouble("UpperFlyVel", FlyWheel.SHOOT_VELOCITY_UPPER);
    private static double defaultFlyLoVel = Preferences.getInstance()
            .getDouble("LowerFlyVel", FlyWheel.SHOOT_VELOCITY_LOWER);
    
    private static final FlyWheel flyUp =
            RobotCommon.runningRobot.upperFlywheel;
    private static final FlyWheel flyLo =
            RobotCommon.runningRobot.lowerFlywheel;
    
    public static double tolerance = FlyWheel.TOLERANCE;
    
    private LinearLookupTable lookupTable;
    private Track track;
    private DriveTrain drive;
    
    // in inches.
    private double towerHeight = 205;
    private double flyUpVel;
    private double flyLoVel;
    private double camFOV;
    private double imgHeight;
    private double imgWidth;
    private double power;
    private double locX;
    private double locY;
    private double blobOffset;
    private double slopX;
    private double slopY;

    private double MAX_POWER = 0.2;
    private double MIN_POWER = 0.08;
    private double Kp = 0.04, Ki = 0.0041, Kd = 0;
    private double errSum = 0;
    private double err = 0;
    private int errCount = 0;
    

    /**
     * the offset in degrees.
     * 
     * @param offset
     */
    public AutoAim(double offset, double flyup, double flylo, double timeout) {
        double[] xArr = {84};
        double[] yArr = {8};
        lookupTable = new LinearLookupTable(xArr, yArr);
        
        track = RobotCommon.runningRobot.targeting;
        camFOV = RobotConstants.CAMFOV;

        this.flyUpVel = flyup;
        this.flyLoVel = flylo;

        setTimeout(timeout);

        imgHeight = 480;
        imgWidth = 320;
        blobOffset = offset;
        locX = 0;
        locY = 0;
        
        slopX = (RobotConstants.SLOP);
        slopY = (RobotConstants.SLOP);
        requires(RobotCommon.runningRobot.driveTrain);
        requires(RobotCommon.runningRobot.arm);
    }

    public AutoAim(double offset, double timeout) {
        this(offset, defaultFlyUpVel, defaultFlyLoVel, timeout);
    }
    
    public AutoAim(double offset) {
        this(offset, defaultFlyUpVel, defaultFlyLoVel, DEFAULT_TIMEOUT);
    }

    public AutoAim() {
        this(DEFAULT_Y_OFFSET, defaultFlyUpVel, defaultFlyLoVel, DEFAULT_TIMEOUT );
    }

    @Override
    protected void initialize() {
        SmartDashboard.putNumber("Is tracked", 0);
        track.GetData();
        Kp = Preferences.getInstance().getDouble("AutoAimKp", Kp);
        Ki = Preferences.getInstance().getDouble("AutoAimKi", Ki);
        Kd = Preferences.getInstance().getDouble("AutoAimKd", Kd);
        
        errSum = 0;
        errCount = 0;
        err = 0;

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

//      double setY = RobotCommon.runningRobot.arm.getAngle()
//              + ((imgHeight / 2 - (locY))) / imgHeight * camFOV / 2;
//      setY += lookupTable.getEstimate(getDistanceFromGoal(setY));
      
        return setY;
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
        pidX();
    }

    public void pidX() {
        track.GetData();

        if (track.blobCount > 0) {
            locX = track.blobLocX;
            locY = track.blobLocY;
            SmartDashboard.putNumber("locX", locX);
            
            double D = (-err + (err = calculateAngleX())) * Kd;
            double P = err * Kp;
            errSum += err;
            errCount += 1;
            double I = errSum / errCount * Ki;
            
            power = P + I + D;
            power = keepInBounds(power, MIN_POWER, MAX_POWER);
            RobotCommon.runningRobot.driveTrain
                    .setPower(new Vector2d(-power, power));
        }
    }

    private double keepInBounds(double pow, double min,
            double max) {
        if (pow > max)
            pow = max;
        else if (pow < -max)
            pow = -max;
        if (pow > 0 && pow < min) {
            pow = min;
        } else if (pow < 0 && pow > -min) {
            pow = -min;
        }
        return pow;
    }

    private double getDistanceFromGoal(double angle) {
        return towerHeight / Math.tan(Math.toRadians(angle) - Math.PI);
    }
    
    @Override
    protected boolean isFinished() {
        boolean flyToSpeed = (flyUpVel <= flyUp.getRPS() + tolerance
                && flyUpVel >= flyUp.getRPS() - tolerance
                && flyLoVel <= flyLo.getRPS() + tolerance
                && flyLoVel >= flyLo.getRPS() - tolerance);

        boolean atTarget = Math.abs(calculateAngleX()) <= 0.5;

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
