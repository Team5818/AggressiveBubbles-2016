package org.usfirst.frc.team5818.robot;

import org.usfirst.frc.team5818.robot.modules.Module;
import org.usfirst.frc.team5818.robot.modules.Shooter;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The secondary robot driver. Responsible for the arm.
 */
public class RobotCoDriver implements Module {
    
    /**
     * The button that resets the arm offset.
     */
    public static final int ARM_RESET_BUTTON = 6;
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
     * toggles arm's controll mode
     */
    public static final int ARM_MODE_TOGGLE_BUTTON = 4;
    
    

    private static final Joystick FIRST_JOYSTICK =
            new Joystick(RobotConstants.CODRIVER_FIRST_JOYSTICK_PORT);
    private static final Joystick SECOND_JOYSTICK =
            new Joystick(RobotConstants.CODRIVER_SECOND_JOYSTICK_PORT);

    private Arm arm;
    
    private Shooter shooter;
    private boolean setAngleMode = false;
    
    @Override
    public void initModule() {
        arm = new Arm();
        shooter = new Shooter();
        shooter.initModule();
    }

    @Override
    public void teleopPeriodicModule() {
        // Arm teleop
        
        shooter.teleopPeriodicModule(SECOND_JOYSTICK);
        
        
       if(FIRST_JOYSTICK
        .getRawButton(ARM_MODE_TOGGLE_BUTTON)){
           setAngleMode = !setAngleMode;
       }
        //arm.armTeleopPeriodic(); don't use in setAngleMode
        
        if(setAngleMode){
        arm.goToAngle((FIRST_JOYSTICK.getThrottle()+1)*45);
        }
        else if(FIRST_JOYSTICK
        .getRawButton(UP_ANGLE_BUTTON)) {
        arm.aimAdjust(true);
        }
        else if (FIRST_JOYSTICK
        .getRawButton(DOWN_ANGLE_BUTTON)) {
        arm.aimAdjust(false);
        }

        
        if(!setAngleMode){
           arm.setPower(FIRST_JOYSTICK.getY());
        }
        
        
        if
        (FIRST_JOYSTICK.getRawButton(ARM_RESET_BUTTON))
        {
        arm.resetEncoder();
        }
        if (FIRST_JOYSTICK
        .getRawButton(PRINT_ANGLE_BUTTON)) {
        DriverStation.reportError("" + arm.getAngle() + "\n", false);
        }

    }

    @Override
    public void endModule() {
        arm = null;
    }

}
