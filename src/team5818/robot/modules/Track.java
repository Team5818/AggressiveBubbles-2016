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
        RoboData.setUpdateRate(.1);

    }

    public void GetData() {
        blobCount = RoboData.getNumber("BLOB_COUNT", 0.0);
        imageWidth = RoboData.getNumber("IMAGE_WIDTH", 0.0);
        imageHeight = RoboData.getNumber("IMAGE_HEIGHT", 0.0);
        blobWidth = RoboData.getNumber("WIDTH", 0.0);
        blobHeight = RoboData.getNumber("HEIGHT", 0.0);
        blobLocX = RoboData.getNumber("COG_X", 0.0);
        blobLocY = RoboData.getNumber("COG_Y", 0.0);
        DriverStation.reportError("" + blobLocX + "\n", false);
    }

    @Override
    public void initModule() {

    }
}
