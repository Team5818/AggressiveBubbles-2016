package team5818.robot.modules;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.vision.USBCamera;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * Handles camera transmission and light managing for the camera.
 */
public class ComputerVision {

    /**
     * The driving camera that is directed with the direction we collect.
     */
    public static final int CAMERA_DRIVER = 2;

    /**
     * The shooter Camera that faces with the flywheel.
     */
    public static final int CAMERA_SHOOTER = 1;

    private USBCamera cam;
    private USBCamera cam2;
    private USBCamera currcam;
    private Image frame;
    private Solenoid LEDLight;

    /**
     * Initializes the light ring and the cameras. Begins the capture.
     */
    public ComputerVision() {

        LEDLight = new Solenoid(0);
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

    /**
     * Starts the camera feed transmission to dashboard.
     */
    public synchronized void runCV() {
        currcam.getImage(frame);
        CameraServer.getInstance().setImage(frame);
    }

    /**
     * Stops the camera feed transmission to dashboard.
     */
    public synchronized void EndCV() {
        if (currcam == cam)
            cam.stopCapture();
        else
            cam2.stopCapture();
    }

    /**
     * Switches the camera feed to the specified value. See public Constants in
     * ComputerVision.
     * 
     * @param i
     *            The desired camera number.
     */
    public synchronized void ChangeFeed(int i) {
        try {
            if (currcam == cam && i == 1) {
                cam.stopCapture();
                currcam = cam2;
                cam2.startCapture();
            } else if (currcam == cam2 && i == 2) {
                cam2.stopCapture();
                currcam = cam;
                cam.startCapture();
            }
        } catch (Exception e) {
            DriverStation.reportError("Camera Feed Switching Error", false);
        }
    }

    /**
     * Switches the state of the LED
     * 
     * @param toggle
     *            Whether to run the light.
     */
    public void LEDToggle(Boolean toggle) {
        LEDLight.set(toggle);
    }

}