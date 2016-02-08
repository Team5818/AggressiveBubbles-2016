package org.usfirst.frc.team5818.robot.modules;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;

public class ComputerVision {

    int session;
    Image frame;

    public ComputerVision() {
        frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

        // the camera name (ex "cam0") can be found through the roborio web
        // interface

        session = NIVision.IMAQdxOpenCamera("cam1",
                NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        NIVision.IMAQdxConfigureGrab(session);
        NIVision.IMAQdxStartAcquisition(session);
    }

    public void runCV() {
        // grab image and set it.
        NIVision.IMAQdxGrab(session, frame, 1);

        CameraServer.getInstance().setImage(frame);
    }

    public void EndCV() {
        // TODO Auto-generated method stub
        NIVision.IMAQdxStopAcquisition(session);
    }

}