package team5818.robot.util;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.Joystick;

public class JoystickPIDSource extends PIDSourceBase {

    public enum Axis {
        X, Y, Z;
    }

    private final Joystick joystick;
    private final Axis pullFromAxis;
    private final transient DoubleSupplier pidGet;

    public JoystickPIDSource(Joystick joystick, Axis pullFromAxis) {
        this.joystick = joystick;
        this.pullFromAxis = pullFromAxis;
        switch (pullFromAxis) {
            case X:
                this.pidGet = joystick::getX;
                break;
            case Y:
                this.pidGet = joystick::getY;
                break;
            case Z:
                this.pidGet = joystick::getZ;
                break;
            default:
                throw new IllegalArgumentException(
                        "Illegal Axis " + pullFromAxis);
        }
    }

    @Override
    public double pidGet() {
        return pidGet.getAsDouble();
    }

}
