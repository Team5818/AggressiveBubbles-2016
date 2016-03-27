package team5818.robot.modules;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.networktables.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Track implements Module {

    public double blobCount = 0;
    public double blobWidth = 0;
    public double blobHeight = 0;
    public double blobOffsetFar = 0;
    public double blobOffsetClose = 0;
    public double imageWidth = 0;
    public double imageHeight = 0;
    public double blobLocX = 0;
    public double blobLocY = 0;
    public NetworkTable RoboData;

    public Track() {
        RoboData = NetworkTable.getTable("Targeting");
        NetworkTable.setUpdateRate(.2);
    }

    public void GetData() {
        blobCount = RoboData.getNumber("BLOB_COUNT", -1.0);
        imageWidth = 320;//RoboData.getNumber("IMAGE_WIDTH", 0.0);
        imageHeight = 240;
       // blobWidth = //RoboData.getNumber("WIDTH", 320.0);
       // blobHeight = RoboData.getNumber("HEIGHT", 240.0);
        blobLocX = RoboData.getNumber("COG_X", 0.0);
        blobLocY = RoboData.getNumber("COG_Y", 0.0);
    }

    @Override
    public void teleopPeriodicModule() {
        SmartDashboard.putNumber("Blob Count", blobCount);
        SmartDashboard.putNumber("Image Width", imageWidth);
        SmartDashboard.putNumber("Image Height", imageHeight);
        SmartDashboard.putNumber("Blob Width", blobWidth);
        SmartDashboard.putNumber("Blob Height", blobHeight);
        SmartDashboard.putNumber("blobLocX", blobLocX);
        SmartDashboard.putNumber("blobLocY", blobLocY);
        
        
    }
    @Override
    public void initModule() {

    }
}
