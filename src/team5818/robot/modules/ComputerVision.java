package team5818.robot.modules;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.vision.USBCamera;

public class ComputerVision {
    
    /**
     * The driving camera that is directed with the direction we collect.
     */
    public static final int CAMERA_DRIVER = 2;
    
    /**
     * The shooter Camera that faces with the flywheel.
     */
    public static final int CAMERA_SHOOTER = 1;
    
    private int session;
    private USBCamera cam;
    private USBCamera cam2;
    private USBCamera currcam;
    private Image frame;

    public ComputerVision() {
        try {
            cam = new USBCamera("cam1");
            cam2 = new USBCamera("cam2");
            currcam = cam;

            if (cam != null || cam2 != null) {

                cam.setSize(640, 360);
                cam.setFPS(30);
                cam.updateSettings();
                cam.openCamera();

                cam2.setSize(320, 240);
                cam2.setFPS(30);
                cam2.updateSettings();
                cam2.openCamera();

            }

            frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

            // the camera name (ex "cam0") can be found through the roborio web
            // interface

            // session = NIVision.IMAQdxOpenCamera("cam1",
            // NIVision.IMAQdxCameraControlMode.CameraControlModeController);
            // NIVision.IMAQdxConfigureGrab(session);
            // NIVision.IMAQdxStartAcquisition(session);

            currcam.startCapture();
        } catch (Exception e) {

            DriverStation.reportError(
                    "Either both or one of the cameras are not attached.\n",
                    false);
            try {
                throw new Exception(
                        "Could not connect to camera ports on Robot\n");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }

    public synchronized void runCV() {
        // grab image and set it.
        // NIVision.IMAQdxGrab(session, frame, 1);
        currcam.getImage(frame);
        CameraServer.getInstance().setImage(frame);

        // CameraServer.getInstance().setImage(frame);
    }

    public synchronized void EndCV() {
        // NIVision.IMAQdxStopAcquisition(session);

        cam.stopCapture();
        // cam2.stopCapture();
    }

    public synchronized void ChangeFeed(int i) {
        if (currcam == cam && i == 1) {
            cam.stopCapture();
            currcam = cam2;
            cam2.startCapture();
        } else if (currcam == cam2 && i == 2) {
            cam2.stopCapture();
            currcam = cam;
            cam.startCapture();
        }
    }

}