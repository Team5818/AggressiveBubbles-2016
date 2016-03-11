package team5818.robot.modules;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Preferences;
import team5818.robot.modules.USBCam;
import edu.wpi.first.wpilibj.Solenoid;

/**
 * Handles camera transmission and light managing for the camera.
 */
public class ComputerVision {

    private int BRIGHTNESS_DEFAULT = 50;
    private int EXPOSURE_DEFAULT = 10;

    /**
     * The driving camera that is directed with the direction we collect.
     */
    public static final int CAMERA_DRIVER = 1;

    /**
     * The shooter Camera that faces with the flywheel.
     */
    public static final int CAMERA_SHOOTER = 2;

    private USBCam cam1;
    private USBCam cam2;
    private USBCam currcam;
    private Image frame;
    private Solenoid LEDLight;

    /**
     * Initializes the light ring and the cameras. Begins the capture.
     */
    public ComputerVision() {

        // Try to set up LED Ring
        try {
            LEDLight = new Solenoid(0);
        } catch (Exception e) {
            DriverStation.reportError("LED Ring Not Set Up", false);
        }

        // Set up Cam1
        try {
            cam1 = new USBCam("cam0");

            if (cam1 != null) {
                cam1.setSize(640, 360);
                cam1.setFPS(30);
                cam1.updateSettings();
                cam1.openCamera();
            }
        } catch (Exception e) {

            DriverStation.reportError("Cam1 is not attached.\n", false);
        }

        // Set up Cam2
        try {
            cam2 = new USBCam("cam1");

            if (cam2 != null) {
                cam2.setSize(320, 240);
                cam2.setFPS(30);
                cam2.setExposureManual(10);
                cam2.updateSettings();
                cam2.openCamera();
            }
        } catch (Exception e) {

            DriverStation.reportError("Cam2 is not attached.\n", false);
        }

        // Set Frame, Current Camera, and Restart Capture
        frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        currcam = cam1;
        // TODO change to Camera_Driver when done working on vision
        this.ChangeFeed(CAMERA_SHOOTER);
        currcam.startCapture();
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
        if (currcam == cam1)
            cam1.stopCapture();
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
            if (i == CAMERA_DRIVER && cam2 != null) {
                if (cam2 != null) {
                    cam1.stopCapture();
                    currcam = cam2;
                    cam2.startCapture();
                }
            } else if (i == CAMERA_SHOOTER && cam1 != null) {
                if (cam2 != null) {
                    cam2.stopCapture();
                }
                // Set Brightness according to preferences
                int Brightness = Preferences.getInstance()
                        .getInt("ShooterCamBrightness", BRIGHTNESS_DEFAULT);
                // Set Brightness according to preferences
                int Exposure = Preferences.getInstance()
                        .getInt("ShooterCamExposure", EXPOSURE_DEFAULT);
                // Toggle Auto Exposure and brightness according to preferences
                boolean AutoPref = Preferences.getInstance()
                        .getBoolean("ShooterCamAutoPref", false);
                if (AutoPref == false) {
                    // Set Camera Settings
                    cam1.setBrightness(Brightness);
                    cam1.setExposureManual(Exposure);
                    cam1.updateSettings();
                }
            }
            currcam = cam1;
            cam1.startCapture();
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