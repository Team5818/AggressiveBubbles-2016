package team5818.robot.modules;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.networktables.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Track implements Module {

    public double BlobCount = -1;
    public NetworkTable RoboData;

    public Track() {
        RoboData = NetworkTable.getTable("Targeting");

    }

    public void SendData() {
        BlobCount = RoboData.getDouble("BLOB_COUNT");
        SmartDashboard.putNumber("blobs", BlobCount);
    }

    @Override
    public void initModule() {

    }

}
