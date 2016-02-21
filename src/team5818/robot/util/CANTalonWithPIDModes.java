package team5818.robot.util;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DriverStation;

/**
 * 
 * TODO Later add reason for making this class.
 */
public class CANTalonWithPIDModes extends CANTalon {

    public int PIDSM_POSITION = 0;
    public int PIDSM_VELOCITY = 1;

    private int pidSourceMode = PIDSM_POSITION;

    public CANTalonWithPIDModes(int deviceNumber) {
        super(deviceNumber);

    }

    public CANTalonWithPIDModes(int deviceNumber, int controlPeriodMs) {
        super(deviceNumber, controlPeriodMs);

    }

    public CANTalonWithPIDModes(int deviceNumber, int controlPeriodMs,
            int enablePeriodMs) {
        super(deviceNumber, controlPeriodMs, enablePeriodMs);

    }

    public void setPIDSourceMode(int mode) {
        pidSourceMode = mode;
    }

    public int getPIDSourceMode() {
        return pidSourceMode;
    }

    @Override
    public double pidGet() {
        if (pidSourceMode == PIDSM_POSITION)
            return getPosition();
        else if (pidSourceMode == PIDSM_VELOCITY)
            return getEncVelocity();
        else {
            DriverStation.reportError("The set PID Source Mode " + pidSourceMode
                    + " not recognized.", false);
            return super.pidGet();
        }
    }

}
