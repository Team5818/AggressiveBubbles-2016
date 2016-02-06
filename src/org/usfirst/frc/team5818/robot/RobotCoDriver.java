package org.usfirst.frc.team5818.robot;

import org.usfirst.frc.team5818.robot.modules.Module;
import org.usfirst.frc.team5818.robot.modules.Shooter;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The secondary robot driver. Responsible for the arm.
 */
public class RobotCoDriver implements Module {

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
        arm.armTeleopPeriodic();
        
        shooter.teleopPeriodicModule(SECOND_JOYSTICK);
        

        // if
        // (RobotConstants.JOYSTICK_C.getRawButton(RobotConstants.ARM_RESET_BUTTON))
        // {
        // RobotConstants.ARM_ENCODER.reset();
        // }
        // if (RobotConstants.JOYSTICK_C
        // .getRawButton(RobotConstants.PRINT_ANGLE_BUTTON)) {
        // DriverStation.reportError("" + arm.getAngle() + "\n", false);
        // }
        // if (RobotConstants.JOYSTICK_C
        // .getRawButton(RobotConstants.LEFT_UP_ANGLE_BUTTON)) {
        // arm.aimAdjustLeft(true);
        // }
        // if (RobotConstants.JOYSTICK_C
        // .getRawButton(RobotConstants.LEFT_DOWN_ANGLE_BUTTON)) {
        // arm.aimAdjustLeft(false);
        // }
        // if (RobotConstants.JOYSTICK_C
        // .getRawButton(RobotConstants.RIGHT_UP_ANGLE_BUTTON)) {
        // arm.aimAdjustRight(true);
        // }
        // if (RobotConstants.JOYSTICK_C
        // .getRawButton(RobotConstants.RIGHT_DOWN_ANGLE_BUTTON)) {
        // arm.aimAdjustRight(false);
        // }
        
       if(FIRST_JOYSTICK
        .getRawButton(RobotConstants.ARM_MODE_TOGGLE_BUTTON)){
           setAngleMode = !setAngleMode;
       }
        //arm.armTeleopPeriodic(); don't use in setAngleMode
        
        if(setAngleMode){
        arm.goToAngle((FIRST_JOYSTICK.getThrottle()+1)*45);
        }
        else if(FIRST_JOYSTICK
        .getRawButton(RobotConstants.LEFT_UP_ANGLE_BUTTON)) {
        arm.aimAdjustLeft(true);
        }
        else if (FIRST_JOYSTICK
        .getRawButton(RobotConstants.LEFT_DOWN_ANGLE_BUTTON)) {
        arm.aimAdjustLeft(false);
        }
        else if (FIRST_JOYSTICK
        .getRawButton(RobotConstants.RIGHT_UP_ANGLE_BUTTON)) {
        arm.aimAdjustRight(true);
        }
        else if (FIRST_JOYSTICK
        .getRawButton(RobotConstants.RIGHT_DOWN_ANGLE_BUTTON)) {
        arm.aimAdjustRight(false);
        }
        
        if(!setAngleMode){
           arm.setPower(FIRST_JOYSTICK.getY());
        }
        
        
        if
        (FIRST_JOYSTICK.getRawButton(RobotConstants.ARM_RESET_BUTTON))
        {
        arm.resetEncoder();
        }
        if (FIRST_JOYSTICK
        .getRawButton(RobotConstants.PRINT_ANGLE_BUTTON)) {
        DriverStation.reportError("" + arm.getAngle() + "\n", false);
        }

    }

    @Override
    public void endModule() {
        arm = null;
    }

}
