package team5818.robot.modules;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.networktables.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Track implements Module {

    public double BlobCount = -1;
    public double BlobX = 0;
    public double BlobY = 0;
    public double BlobSize = 0;
    public double ImageWidth = 0;
    public double ImageHeight = 0;
    public NetworkTable RoboData;

    public Track() {
        RoboData = NetworkTable.getTable("Targeting");

    }

    public void GetData() {
        BlobCount = RoboData.getDouble("BLOB_COUNT");
        ImageWidth = RoboData.getDouble("IMAGE_WIDTH");
        ImageHeight = RoboData.getDouble("IMAGE_HEIGHT");
        BlobX = RoboData.getDouble("COG_X");
        BlobY = RoboData.getDouble("COG_Y");
        BlobSize = RoboData.getDouble("COG_BOX_SIZE");

        SmartDashboard.putNumber("blobs", BlobCount);
        SmartDashboard.putNumber("BLocation", BlobX);
    }

    @Override
    public void initModule() {

    }

}
