package team5818.robot.util;

import edu.wpi.first.wpilibj.AnalogInput;

public class ScaledAnalogIn extends AnalogInput{

    double SCALE;
    double OFFSET;
    
    public ScaledAnalogIn(int channel, double scale, double offset) {
        super(channel);
        SCALE = scale;
        OFFSET = offset;
    }
    
    @Override
    public double pidGet(){
        return getAverageVoltage()*SCALE + OFFSET;
    }
    

}
