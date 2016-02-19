package team5818.robot;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.modules.Arm;
import team5818.robot.modules.Module;
import team5818.robot.modules.Shooter;

/**
 * The secondary robot driver. Responsible for the arm.
 */
public class RobotCoDriver implements Module {

    /**
     * The button that resets the arm offset.
     */
    public static final int ARM_RESET_BUTTON = 8;
    /**
     * The button that prints the arm offset.
     */
    public static final int PRINT_ANGLE_BUTTON = 7;
    /**
     * The button that increases the angle the arm.
     */
    public static final int UP_ANGLE_BUTTON = 5;
    /**
     * The button that decreases the angle of the arm.
     */
    public static final int DOWN_ANGLE_BUTTON = 3;
    /**
     * puts arm in PID mode
     */
    public static final int ENTER_PID_BUTTON = 4;
    /**
     * takes arm out of PID mode
     */
    public static final int EXIT_PID_BUTTON = 6;
    /**
     * goes to 45 degrees
     */
    public static final int GO_TO_ANGLE_BUTTON = 9;
    
    public static final int BUT_STARTPID = 12;
    public static final int BUT_STOPPID = 11;
    
    public static final int BUT_PRINT_RPS = 10;
    
    private boolean hasStartedPID = false;
    private boolean hasStopedPID = false;
    private boolean hasPrintedRPS = false;
    
    /**
     * returns error from arm PID
     */
    public static final int ERROR_BUTTON = 1;

    private static final Joystick FIRST_JOYSTICK =
            new Joystick(RobotConstants.CODRIVER_FIRST_JOYSTICK_PORT);
    private static final Joystick SECOND_JOYSTICK =
            new Joystick(RobotConstants.CODRIVER_SECOND_JOYSTICK_PORT);

    private Arm arm;

    private Shooter shooter = new Shooter();
    private boolean setAngleMode = false;

    @Override
    public void initModule() {
        arm = RobotCommon.runningRobot.arm;
        
        
    }

    @Override
    public void teleopPeriodicModule() {
        // Arm teleop
        
        if (FIRST_JOYSTICK.getRawButton(ENTER_PID_BUTTON)) {
            setAngleMode = true;
            if (setAngleMode) {
                DriverStation.reportError("Entering PID Mode", false);
            }
        }

        if (FIRST_JOYSTICK.getRawButton(EXIT_PID_BUTTON)) {
            setAngleMode = false;
            if (setAngleMode) {
                DriverStation.reportError("Exiting PID Mode", false);
            }
        }
        // arm.armTeleopPeriodic(); don't use in setAngleMode

        if (setAngleMode) {
            double target;
            try {
                target = Double
                        .valueOf(SmartDashboard.getString("DB/String 8"));
            } catch (Exception e) {
                throw e;
            }
            if (FIRST_JOYSTICK.getRawButton(GO_TO_ANGLE_BUTTON)) {
                arm.goToAngle(target);
            }
            if (FIRST_JOYSTICK.getRawButton(ERROR_BUTTON)) {
                DriverStation.reportError("" + arm.getError() + "\n", false);
            }
        } else if (FIRST_JOYSTICK.getRawButton(UP_ANGLE_BUTTON)) {
            arm.aimAdjust(true);
        } else if (FIRST_JOYSTICK.getRawButton(DOWN_ANGLE_BUTTON)) {
            arm.aimAdjust(false);
        }

        if (!setAngleMode) {
            arm.setPower(FIRST_JOYSTICK.getY());
        }

        if (FIRST_JOYSTICK.getRawButton(ARM_RESET_BUTTON)) {
            // arm.resetEncoder();
        }
        if (FIRST_JOYSTICK.getRawButton(PRINT_ANGLE_BUTTON)) {
            SmartDashboard.putString("DB/String 7",
                    "" + arm.getPotentiometerVal());
        }
        
        /* Flywheel Testing Stuff */
        /*
         * Button 12 will set the PID loop with the constants on 
         * slider 1, 2, and 3 as p, i, and d.
         * Button 10 will stop the PID by setting the velocity to 0.
         * Button 11 will print the RPS of the lower motor.
         */
        if(SECOND_JOYSTICK.getRawButton(BUT_STARTPID)) {
            if(!hasStartedPID) {
                RobotCommon.runningRobot.shooter.setFlywheelVelocity(100);
                hasStartedPID = true;
            }
        } else {
            
            hasStartedPID = false;
        }
        
        
        if(SECOND_JOYSTICK.getRawButton(BUT_STOPPID)) {
            if(!hasStopedPID) {
                RobotCommon.runningRobot.shooter.setFlywheelPower(0);
                
                hasStopedPID = true;
            }
        } else {
            
            hasStopedPID = false;
        }
        SmartDashboard.putNumber("Lower Flywheel RPS", RobotCommon.runningRobot.shooter.getLowerFlywheel().getRPS());
        
        /*
        if(SECOND_JOYSTICK.getRawButton(BUT_PRINT_RPS)) {
            if(!hasPrintedRPS) {
                hasStartedPID = true;
            }
        } else {
            
            hasPrintedRPS = false;
        }
        */

    }

    @Override
    public void endModule() {
        arm = null;
    }

    @Override
    public void initTest() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void initTeleop() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void initAutonomous() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void testPeriodic() {
    }

}
