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

    // TODO:switch for actual robot
    private int BRIGHTNESS_DEFAULT = 50;
    private int EXPOSURE_DEFAULT = 20;

    /**
     * The driving camera that is directed with the direction we collect.
     * 
     */
    public static final int CAMERA_DRIVER = 1;

    /**
     * The shooter Camera that faces with the flywheel.
     *
     */
    public static final int CAMERA_SHOOTER = 2;

    private USBCam camDriver;
    private USBCam camShooter;
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

        // Set up camDriver
        try {
            camDriver = new USBCam("cam0");

            if (camDriver != null) {
                camDriver.setSize(640, 360);
                camDriver.setFPS(30);
                camDriver.updateSettings();
                camDriver.openCamera();
            }
        } catch (Exception e) {

            DriverStation.reportError("camDriver is not attached.\n", false);
        }

        // Set up camShooter
        try {
            camShooter = new USBCam("cam1");

            if (camShooter != null) {
                camShooter.setSize(320, 240);
                camShooter.setFPS(30);
                camShooter.updateSettings();
                camShooter.openCamera();
            }
        } catch (Exception e) {

            DriverStation.reportError("camShooter is not attached.\n", false);
        }

        // Set Frame, Current Camera, and Restart Capture
        frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        currcam = camDriver;
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
        if (currcam == camDriver)
            camDriver.stopCapture();
        else
            camShooter.stopCapture();
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
            //TODO:switch for actual robot
            if (i == CAMERA_SHOOTER && camShooter != null) {
                if (camShooter != null) {

                    camDriver.stopCapture();
                    // Set Brightness according to preferences
                    int Brightness = Preferences.getInstance()
                            .getInt("ShooterCamBrightness", BRIGHTNESS_DEFAULT);
                    // Set Brightness according to preferences
                    int Exposure = Preferences.getInstance()
                            .getInt("ShooterCamExposure", EXPOSURE_DEFAULT);
                    // Toggle Auto Exposure and brightness according to
                    // preferences
                    boolean AutoPref = Preferences.getInstance()
                            .getBoolean("ShooterCamAutoPref", false);

                    if (AutoPref == false) {
                        // Set Camera Settings
                        camShooter.setBrightness(Brightness);
                        camShooter.setExposureManual(EXPOSURE_DEFAULT);
                        camShooter.updateSettings();
                    }
                    currcam = camShooter;
                    camShooter.startCapture();
                }
            } else if (i == CAMERA_DRIVER && camDriver != null) {
                if (camShooter != null) {
                    camShooter.stopCapture();
                    currcam = camDriver;
                    camDriver.startCapture();
                }

            }

        } catch (Exception e) {
            // DriverStation.reportError("Camera Feed Switching Error\n",
            // false);
            DriverStation.reportError(e.getMessage(), false);
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