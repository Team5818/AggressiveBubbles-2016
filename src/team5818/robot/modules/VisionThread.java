package team5818.robot.modules;

import edu.wpi.first.wpilibj.DriverStation;

public class VisionThread implements Runnable, Module {

    private boolean isRunning = false;
    private boolean cvRunning = false;
    public ComputerVision See;
    private Thread captureThread;

    public VisionThread() {
        try {
            See = new ComputerVision();
            cvRunning = true;
            isRunning = true;
            captureThread = new Thread(this);
            captureThread.setName("Camera Capture Thread");
        } catch (Exception e) {
            DriverStation.reportError("Could not create a Vision Thread",
                    false);
            cvRunning = false;
            isRunning = true;
        }
    }

    @Override
    public void run() {
        while (isRunning) {
            if (cvRunning) {
                See.runCV();
            }
        }
        See.EndCV();
    }

    public void StopThread() {
        isRunning = false;
    }

    public void stopCV() {
        cvRunning = false;
    }

    public static void stopThread() {
    }

    @Override
    public void initModule() {
        if (captureThread != null)
            captureThread.start();

    }

    @Override
    public void teleopPeriodicModule() {

    }

    @Override
    public void endModule() {

    }

    @Override
    public void initTest() {

    }

    @Override
    public void initTeleop() {

    }

    @Override
    public void initAutonomous() {

    }

    @Override
    public void testPeriodic() {

    }

}
