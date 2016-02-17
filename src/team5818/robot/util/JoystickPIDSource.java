package team5818.robot.util;

import java.util.function.DoubleSupplier;

import edu.wpi.first.wpilibj.Joystick;

public class JoystickPIDSource extends PIDSourceBase {

    public enum Axis {
        X, Y;
    }

    private final Joystick joystick;
    private final Axis pullFromAxis;
    private final transient DoubleSupplier pidGet;

    public JoystickPIDSource(Joystick joystick, Axis pullFromAxis) {
        this.joystick = joystick;
        this.pullFromAxis = pullFromAxis;
        this.pidGet = pullFromAxis == Axis.X ? joystick::getX : joystick::getY;
    }

    @Override
    public double pidGet() {
        return pidGet.getAsDouble();
    }

}
