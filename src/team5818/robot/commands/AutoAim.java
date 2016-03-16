package team5818.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.RobotConstants;
import team5818.robot.modules.Arm;
import team5818.robot.modules.Track;
import team5818.robot.modules.drivetrain.DriveTrain;

public class AutoAim extends Command {

    public Track aim;
    public double maxTime;
    public double imgHeight;
    public double imgWidth;
    public double blobCenterX;
    public double blobCenterY;
    public double blobWidth;
    public double blobHeight;
    public double setX;
    public double setY;
    public double blobOffset;
    public boolean isCenteredX;
    public boolean isCenteredY;
    public double slopX;
    public boolean done;
    public double slopY;

    public AutoAim() {
        aim = RobotCommon.runningRobot.Targeting;
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
        isCenteredX = false;
        isCenteredY = false;
        slopX = (RobotConstants.SLOP * blobCenterX);
        slopY = (RobotConstants.SLOP * blobCenterY);
        done = false;
    }

    @Override
    protected void initialize() {
        setTimeout(maxTime);
        aim.GetData();
        if (aim.blobCount > 0) {
            imgHeight = aim.imageHeight;
            imgWidth = aim.imageWidth;
            blobCenterX = aim.blobCenterX;
            blobCenterY = aim.blobCenterY;
            blobWidth = aim.blobWidth;
            blobHeight = aim.blobHeight;
            blobOffset = aim.blobOffsetY;
            aim();
            done = true;
        } else {
            DriverStation.reportError("No Blobs Found Did Not Aim", false);
        }
    }

    public void calculateX() {
        setX = (imgWidth - blobWidth) / 2;
    }

    public void calculateY() {
        setY = ((imgHeight - blobHeight) / 2) + blobOffset;
    }

    public void aim() {
        while (!isCenteredX) {
            checkCenter();
        }
        while (!isCenteredY) {
            checkCenter();
        }
    }

    public void checkCenter() {
        calculateX();
        if (setX < slopX) {
            new SetDrivePower(4,-4);
            calculateX();
        } else if (setX > slopY) {
            new SetDrivePower(-4,4);
            calculateX();
        }

        calculateY();
        if (setY < slopY + blobOffset) {
            new SetArmPower(.7);
            calculateY();
        } else if (setY > slopY + blobOffset) {
            new SetArmPower(-.7);
            calculateY();
        }
    }

    @Override
    protected void execute() {
        // TODO Auto-generated method stub

    }

    @Override
    protected boolean isFinished() {
        if (done || isTimedOut()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void end() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void interrupted() {
        // TODO Auto-generated method stub

    }

}
