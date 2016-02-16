package org.usfirst.frc.team5818.robot.modules;

import edu.wpi.first.wpilibj.DriverStation;

public class VisionThread implements Runnable, Module {

    private boolean isRunning = false;
    private boolean cvRunning = false;
    public ComputerVision See;
    private Thread captureThread;

    public VisionThread() {
        See = new ComputerVision();
        cvRunning = true;
        isRunning = true;
        captureThread = new Thread(this);
        captureThread.setName("Camera Capture Thread");
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
        // TODO Auto-generated method stub
        captureThread.start();

    }

    @Override
    public void teleopPeriodicModule() {
        // TODO Auto-generated method stub

    }

    @Override
    public void endModule() {
        // TODO Auto-generated method stub

    }

}
