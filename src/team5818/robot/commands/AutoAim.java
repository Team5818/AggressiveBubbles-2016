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


    public  static final double DEFAULT_X_OFFSET = 1 + 0.5 + 0.75;
    public static final double DEFAULT_Y_OFFSET = -1.8; //calibrated for lowbar
    public static final double DEFAULT_TIMEOUT = 3;
    public static boolean UDP = true;
    private boolean hasFoundTarget = false;

    private static double defaultFlyUpVel = Preferences.getInstance()
            .getDouble("UpperFlyVel", FlyWheel.SHOOT_VELOCITY_UPPER);
    private static double defaultFlyLoVel = Preferences.getInstance()
            .getDouble("LowerFlyVel", FlyWheel.SHOOT_VELOCITY_LOWER);
    
    private static final FlyWheel flyUp =
            RobotCommon.runningRobot.upperFlywheel;
    private static final FlyWheel flyLo =
            RobotCommon.runningRobot.lowerFlywheel;
    
    public static double toleranceFly = FlyWheel.TOLERANCE;
    
    private LinearLookupTable lookupTable;
    private static Track track;
    private DriveTrain drive;
    
    // in inches.
    private static double towerHeight = 205;
    private double flyUpVel;
    private double flyLoVel;
    private static double camFOV;
    private static double imgHeight;
    private static double imgWidth;
    //private static double locX;
    //private static double locY;
    private static double slopX;
    private static double slopY;

    private double MAX_POWER_X = 0.25;
    private double MIN_POWER_X = 0.08;
    private double xpower;
    private double xKp = 0.04, xKi = 0.0041, xKd = 0;
    private double xerrSum = 0;
    private double xerr = 0;
    private int xerrCount = 0;
    private static double xOffset = 0;
    public static double toleranceX = 0.5;
    
    private double MAX_POWER_Y = 1;
    private double MIN_POWER_Y = 0.15;
    private double ypower;
    private double yKp = 0.09, yKi = 0.0081, yKd = 0;
    private double yerrSum = 0;
    private double yerr = 0;
    private int yerrCount = 0;
    private static double yOffset = 0;
    public static double toleranceY = 2;
    

    /**
     * the offset in degrees.
     * 
     * @param offset
     */
    public AutoAim(double xOffset, double yOffset, double flyup, double flylo, double timeout) {
        double[] xArr = {84};
        double[] yArr = {8};
        lookupTable = new LinearLookupTable(xArr, yArr);
        
        track = RobotCommon.runningRobot.targeting;
        camFOV = RobotConstants.CAMFOV;

        this.flyUpVel = flyup;
        this.flyLoVel = flylo;

        setTimeout(timeout);

        imgHeight = 240;
        imgWidth = 320;
        this.yOffset = yOffset;
        this.xOffset = xOffset;
        
        slopX = (RobotConstants.SLOP);
        slopY = (RobotConstants.SLOP);
        requires(RobotCommon.runningRobot.driveTrain);
        requires(RobotCommon.runningRobot.arm);
        
    }

    public AutoAim(double xOffset, double yOffset, double timeout) {
        this(xOffset, yOffset, defaultFlyUpVel, defaultFlyLoVel, timeout);
    }
    
    public AutoAim(double xOffset, double yOffset) {
        this(xOffset, yOffset, defaultFlyUpVel, defaultFlyLoVel, DEFAULT_TIMEOUT);
    }

    public AutoAim() {
        this(0, 0, defaultFlyUpVel, defaultFlyLoVel, DEFAULT_TIMEOUT);
    }
    
    public void setXOffset(double x) {
        this.xOffset = x;
    }
    
    public void setYOffset(double y) {
        this.yOffset = y;
    }

    @Override
    protected void initialize() {
        track.GetData();
        xKp = Preferences.getInstance().getDouble("AutoAimKpX", xKp);
        xKi = Preferences.getInstance().getDouble("AutoAimKiX", xKi);
        xKd = Preferences.getInstance().getDouble("AutoAimKdX", xKd);
        //xOffset = Preferences.getInstance().getDouble("AutoAimXOffset", xOffset);
        
        xerrSum = 0;
        xerrCount = 0;
        xerr = 0;
        ypower = 0;
        
        yKp = Preferences.getInstance().getDouble("AutoAimKpY", yKp);
        yKi = Preferences.getInstance().getDouble("AutoAimKiY", yKi);
        yKd = Preferences.getInstance().getDouble("AutoAimKdY", yKd);
        //yOffset = Preferences.getInstance().getDouble("AutoAimYOffset", yOffset);
        
        
        yerrSum = 0;
        yerrCount = 0;
        yerr = 0;

        if (track.blobCount > 0) {
            // imgHeight = track.imageHeight;
            // imgWidth = track.imageWidth;
            // blobWidth = track.blobWidth;
            // blobHeight = track.blobHeight;
            aimY();
            aim();
        }

        else {
            DriverStation.reportError("No Blobs Found Did Not track", false);
        }
    }

    public static double calculateAngleX() {
        double setX = (((imgWidth / 2 - (track.blobLocX))) / imgWidth * camFOV/2);
        setX += xOffset + DEFAULT_X_OFFSET;
        SmartDashboard.putNumber("AutoAim X Error", setX);
        return setX;

    }

    public static double calculateAngleY() {
        double setY = ((imgHeight / 2 - (track.blobLocY))) / imgHeight * camFOV/2;
        SmartDashboard.putNumber("AutoAim Y Angle", setY);
        setY += yOffset + DEFAULT_Y_OFFSET;

//      double setY = RobotCommon.runningRobot.arm.getAngle()
//              + ((imgHeight / 2 - (locY))) / imgHeight * camFOV / 2;
//      setY += lookupTable.getEstimate(getDistanceFromGoal(setY));
      
        return setY;
    }

    public void aim() {
        if ((track.blobLocX < (imgWidth / 2) + (slopX * (imgWidth))
                || (track.blobLocX > (imgWidth / 2) + (slopX * imgWidth)))) {
        } else if (track.blobLocX == (imgWidth / 2) + (slopX * imgWidth)) {
        } else {
            DriverStation.reportError("did not align", false);
        }

    }

    public void aimY() {
        if ((track.blobLocY < (imgHeight / 2) + (slopY * imgHeight) + yOffset)
                || (track.blobLocY > (imgHeight / 2) + (slopY * imgHeight)
                        + yOffset)) {
        } else if (track.blobLocY == (imgHeight / 2) + (slopY * imgHeight) + yOffset) {
        } else {
            DriverStation.reportError("did not align", false);
        }
    }

    @Override
    protected void execute() {
        SmartDashboard.putNumber("BlobCount", track.blobCount);
        pidX();
        pidY();
    }
    
    public void pidY() {
        if (track.blobCount > 0) {
            //track.GetData();
            if(Math.abs(calculateAngleY()) <= 2) {
                yerrSum = 0;
                yerrCount = 0;
                //DriverStation.reportError("Dropping Y I Epsilon", true);
            }
            if(Math.signum(yerr) != Math.signum(calculateAngleY()))
            {
                yerrSum = 0;
                yerrCount = 0;
                //DriverStation.reportError("Dropping Y I Sign", true);
            }
            
            double D = (-yerr + (yerr = calculateAngleY())) * yKd;
            double P = yerr * yKp;
            yerrSum += yerr;
            yerrCount += 1;
            double I = yerrSum / yerrCount * yKi;
            //DriverStation.reportError("Kp = " + yKp, false);
            
            ypower = P + I + D;
            ypower = keepInBounds(ypower, MIN_POWER_Y, MAX_POWER_Y);
            RobotCommon.runningRobot.arm.setPower(ypower);
        } else {
            RobotCommon.runningRobot.arm.setPower(0);
        }
    }

    public void pidX() { 
        if (track.blobCount > 0) {
            double something = calculateAngleX();
            if(Math.abs(something) <= 1) {
                xerrSum = 0;
                xerrCount = 0;
                //DriverStation.reportError("Dropping X I Epsilon", false);
            }
            if((Math.signum(xerr) != Math.signum(something)))
            {
                xerrSum = 0;
                xerrCount = 0;
                //DriverStation.reportError("Dropping X I Sign", false);
            }
            
            double D = (-xerr + (xerr = calculateAngleX())) * xKd;
            double P = xerr * xKp;
            xerrSum += xerr;
            xerrCount += 1;
            double I = xerrSum / xerrCount * xKi;
            
            xpower = P + I + D;
            xpower = keepInBounds(xpower, MIN_POWER_X, MAX_POWER_X);
            RobotCommon.runningRobot.driveTrain
                    .setPower(new Vector2d(-xpower, xpower));
        } else {
            RobotCommon.runningRobot.driveTrain
            .setPower(new Vector2d(-0, 0));
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
        return towerHeight / Math.tan(Math.PI - Math.toRadians(angle));
    }
    
    @Override
    protected boolean isFinished() {
        boolean flyToSpeed = (flyUpVel <= flyUp.getRPS() + toleranceFly
                && flyUpVel >= flyUp.getRPS() - toleranceFly
                && flyLoVel <= flyLo.getRPS() + toleranceFly
                && flyLoVel >= flyLo.getRPS() - toleranceFly);
       if(flyToSpeed){
           //DriverStation.reportError("Aiming ready", false);

       }

        boolean atTargetX = Math.abs(calculateAngleX()) <= 0.39;
        if(atTargetX){
            //DriverStation.reportError("Aiming ready", false);

        }
        boolean atTargetY = Math.abs(calculateAngleY()) <= 0.1;
        if(atTargetY){
            //DriverStation.reportError("Aiming ready", false);
        }
        

        if (isTimedOut()) {
            //DriverStation.reportError("Timedout AutoAim", false);
            //DriverStation.reportError("Flywheel to speed: " + flyToSpeed
                  //  + "At angle: " + atTargetX, false);
            return true;
        }
        if(((atTargetX && atTargetY))) {
            DriverStation.reportError("Aiming ready", false);
        }
        hasFoundTarget = true;
        return ((atTargetX && atTargetY) || this.isTimedOut());
    }
    
    public boolean foundTarget() {
        return hasFoundTarget;
    }

    @Override
    protected void end() {
        SmartDashboard.putNumber("Is aimed", 100);
        RobotCommon.runningRobot.driveTrain.setPower(new Vector2d(0, 0));
        RobotCommon.runningRobot.arm.setPower(0);
    }

    @Override
    protected void interrupted() {
        RobotCommon.runningRobot.driveTrain.setPower(new Vector2d(0, 0));
        RobotCommon.runningRobot.arm.setPower(0);
    }
}
