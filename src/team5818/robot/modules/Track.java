package team5818.robot.modules;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.networktables.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Track implements Module {

    public double blobCount = -1;
    public double blobWidth = 0;
    public double blobHeight = 0;
    public double blobOffsetY = 0;
    public double imageWidth = 0;
    public double imageHeight = 0;
    public double blobCenterX = 0;
    public double blobCenterY = 0;
    public NetworkTable RoboData;

    public Track() {
        RoboData = NetworkTable.getTable("Targeting");

    }

    public void GetData() {
        blobCount = RoboData.getDouble("BLOB_COUNT");
        imageWidth = RoboData.getDouble("IMAGE_WIDTH");
        imageHeight = RoboData.getDouble("IMAGE_HEIGHT");
        blobWidth = RoboData.getDouble("target_width");
        blobHeight = RoboData.getDouble("target_height");
    }

    public void CalculateCenter() {
        blobCenterX = blobWidth / 2;
        blobCenterY = blobHeight / 2;
        blobOffsetY = blobHeight;
    }
 

    @Override
    public void initModule() {

    }

}
