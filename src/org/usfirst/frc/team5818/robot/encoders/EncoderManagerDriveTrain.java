package org.usfirst.frc.team5818.robot.encoders;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class EncoderManagerDriveTrain extends EncoderManagerBase{

    Encoder enc;
    double lastPos = 0;
    PIDController pid;
    double currentPower = 0;
    
    private class EncoderPIDSource implements PIDSource{
        
        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {
            // We really could not care less if we tried. Ignore it.
            
        }

        @Override
        public PIDSourceType getPIDSourceType() {
            // Return null because screw you too.
            return null;
        }

        @Override
        public double pidGet() {
            // Wow an actual method.
            return EncoderManagerDriveTrain.this.PIDGet();
        }
        
    }
    
    private class EncoderPIDOutput implements PIDOutput{

        @Override
        public void pidWrite(double output) {
            EncoderManagerDriveTrain.this.setOutputPower(output);
        }
    }
    
    public EncoderManagerDriveTrain(int aChannel, int bChannel, double scaleFactor, double Kp, double Ki, double Kd)
    {
        enc = new Encoder(aChannel, bChannel);
        enc.setDistancePerPulse(scaleFactor);
        pid = new PIDController(Kp, Ki, Kd, new EncoderPIDSource(), new EncoderPIDOutput());
        this.distanceDriven = 0;
        this.distanceToDrive = 0;
    }
    
    @Override
    public double getEncPosAbs() {
        return enc.getDistance();
    }

    @Override
    public double getEncDelta() {
        double temp = lastPos;
        lastPos = enc.getDistance();
        return enc.getDistance() - temp;
    }

    @Override
    public double peekEncDelta() {
        return enc.getDistance() - lastPos;
    }

    @Override
    public double getPowerAccordingToDistance(double time) {
        
        return currentPower;
    }
    
    public double PIDGet()
    {
        return this.getDistanceNotCovered();
    }
    
    public void setOutputPower(double output)
    {
        currentPower = output;
        currentPower = Math.min(Math.max(0, currentPower), 1);
    }

}
