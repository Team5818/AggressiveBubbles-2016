package team5818.robot.modules;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.RobotConstants;
import team5818.robot.util.CANTalonWithPIDModes;

/**
 * This is the shooter class that controls the motion of the shooter. It
 * commands the flywheel object that controls the flywheel motors.
 * 
 * This class should be the only class that communicates with the flywheel. Any
 * other commands or communication to the flywheel motors should come through
 * this class and this class only.
 *
 */
public class Shooter implements Module {

    /**
     * The Talon motor controller class that controls the upper fly wheel in the
     * shooter mechanism.
     */
    private CANTalonWithPIDModes talonU =
            new CANTalonWithPIDModes(RobotConstants.TALON_FLYWHEEL_UPPER);

    /**
     * The Talon motor controller class that controls the upper fly wheel in the
     * shooter mechanism.
     */
    private CANTalonWithPIDModes talonL =
            new CANTalonWithPIDModes(RobotConstants.TALON_FLYWHEEL_LOWER);

    /**
     * The number formatter for the outputting numbers as strings.
     */
    NumberFormat formatter = new DecimalFormat("#0.00");

    /**
     * The flywheel object that controls the motion of the flywheel.
     */
    private final FlyWheel wheelU, wheelL;

    private boolean hasStartedPID = false;

    /**
     * Initiates the flyWheel object to the correct talons so that it can be
     * controlled.
     */
    public Shooter() {

        wheelU = new FlyWheel(talonU, false, false);
        wheelL = new FlyWheel(talonL, false, true);

    }

    @Override
    public void initModule() {

        wheelU.initModule();
        wheelL.initModule();

    }

    @Override
    public void teleopPeriodicModule() {

        double vt = SmartDashboard.getNumber("DB/Slider 1");
        double vb = SmartDashboard.getNumber("DB/Slider 1");
        // TODO now actually make it set the velocity using a pid loop.

        if (SmartDashboard.getBoolean("DB/Button 1")) {

            if (!hasStartedPID) {

                wheelU.setSetVelocity(vt);
                wheelU.setSetVelocity(vb);

            }

        } else {
            hasStartedPID = false;
        }

        // double flyLP = SmartDashboard.getNumber("DB/Slider 0");
        // double flyUP = SmartDashboard.getNumber("DB/Slider 1");

        // wheelU.setPower(flyUP);
        // wheelL.setPower(flyLP);

        // Get rid of conflict in robotcommon to use commented code.
        // SmartDashboard.putString("DB/String 0", "LowerRPS = " +
        // formatter.format(wheelL.getRPS()));
        // SmartDashboard.putString("DB/String 5", "UpperRPS = " +
        // formatter.format(wheelU.getRPS()));

        wheelU.teleopPeriodicModule();
        wheelL.teleopPeriodicModule();

    }

    @Override
    public void endModule() {

        wheelU.endModule();
        wheelL.endModule();

    }

}
