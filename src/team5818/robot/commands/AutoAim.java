package team5818.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.RobotConstants;
import team5818.robot.RobotDriver;
import team5818.robot.modules.Arm;
import team5818.robot.modules.Track;
import team5818.robot.modules.drivetrain.DriveTrain;
import team5818.robot.util.Vector2d;

public class AutoAim extends Command {

    private Track aim;
    private DriveTrain drive;

    private double camFOV;

    public double maxTime;
    public double imgHeight;
    public double imgWidth;
    public double blobCenterX;
    public double blobCenterY;
    public double blobWidth;
    public double blobHeight;
    public double setX;
    public double setY;
    public double locX;
    public double locY;
    public double blobOffset;
    public double slopX;
    public double slopY;

    public boolean isCenteredX;
    public boolean isCenteredY;
    public boolean done;

    private double MAX_SPIN_POWER = 0.15;
    private double MAX_ARM_POWER = 0.5;

    public AutoAim() {
        aim = RobotCommon.runningRobot.targeting;
        drive = RobotCommon.runningRobot.driveTrain;
        RobotCommon.runningRobot.setDriveAutoAim();
        camFOV = RobotConstants.CAMFOV;

        maxTime = 5;

        imgHeight = 0;
        imgWidth = 0;
        blobCenterX = 0;
        blobCenterY = 0;
        blobWidth = 0;
        blobHeight = 0;
        setX = 0;
        setY = 0;
        blobOffset = 0;
        locX = 0;
        locY = 0;

        isCenteredX = false;
        isCenteredY = false;
        slopX = (RobotConstants.SLOP);
        slopY = (RobotConstants.SLOP);
        done = false;
        aim();
    }

    @Override
    protected void initialize() {
        setTimeout(1);
        aim.GetData();
        if (aim.blobCount > 0) {
            imgHeight = aim.imageHeight;
            imgWidth = aim.imageWidth;
            blobWidth = aim.blobWidth;
            blobHeight = aim.blobHeight;
            locX = aim.blobLocX;
            locY = aim.blobLocX;
            if (blobWidth < 70) {
                blobOffset = aim.blobOffsetClose;
            } else {
                blobOffset = aim.blobOffsetFar;
            }
        }

        else {
            DriverStation.reportError("No Blobs Found Did Not Aim", false);
        }
    }

    public void calculateAngleX() {
        setX = ((imgWidth - locX) / (camFOV));

    }

    public void calculateAngleY() {
        setY = ((imgHeight - (locY + blobOffset)) / (camFOV));
    }

    public void aim() {
        checkCenter();
    }

    public void setDrive(double left, double right) {
        Vector2d power = new Vector2d(left, right);
        drive.setPower(power);
    }

    public void checkCenter() {
        calculateAngleX();
        if (locX < (imgWidth / 2) + (slopX * imgWidth)) {
            setDrive(MAX_SPIN_POWER, -MAX_SPIN_POWER);
        } else if (locX > (imgWidth / 2) + (slopX * imgWidth)) {
            setDrive(-MAX_SPIN_POWER, MAX_SPIN_POWER);
        } else if (locX == (imgWidth / 2) + (slopX * imgWidth)) {
            setDrive(0, 0);
        } else {
            DriverStation.reportError("did not align", false);
        }

        calculateAngleY();
        if (locY < (imgHeight / 2) + (slopY * imgHeight) + blobOffset) {
            new SetArmPower(MAX_ARM_POWER);
        } else if (locY > (imgHeight / 2) + (slopY * imgHeight) + blobOffset) {
            new SetArmPower(-MAX_ARM_POWER);
        } else if (locY == (imgHeight / 2) + (slopY * imgHeight) + blobOffset) {
            new SetArmPower(0);
        } else {
            DriverStation.reportError("did not align", false);
        }

    }

    @Override
    protected void execute() {
        // TODO Auto-generated method stub
        aim.GetData();
        if (aim.blobCount > 0) {
            imgHeight = aim.imageHeight;
            imgWidth = aim.imageWidth;
            blobWidth = aim.blobWidth;
            blobHeight = aim.blobHeight;
            locX = aim.blobLocX;
            locY = aim.blobLocX;
            if (blobWidth < 70) {
                blobOffset = aim.blobOffsetClose;
            } else {
                blobOffset = aim.blobOffsetFar;
            }
        }
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }

    @Override
    protected void end() {
        RobotCommon.runningRobot.setDriveDefault();
    }

    @Override
    protected void interrupted() {
        // TODO Auto-generated method stub

    }
}
