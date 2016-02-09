package org.usfirst.frc.team5818.robot.modules;

import edu.wpi.first.wpilibj.CameraServer;

public class Eyes {

    CameraServer server;

    public void LookAtMe() {
        server = CameraServer.getInstance();
        server.setQuality(50);
        server.setSize(10);
        // the camera name (ex "cam0") can be found through the roborio web
        // interface
        server.startAutomaticCapture("cam0");
    }

}
