package org.usfirst.frc.team5818.robot;

import org.usfirst.frc.team5818.robot.modules.Module;
import org.usfirst.frc.team5818.robot.modules.drivetrain.ArcadeDriveCalculator;
import org.usfirst.frc.team5818.robot.modules.drivetrain.DriveCalculator;
import org.usfirst.frc.team5818.robot.modules.drivetrain.TankDriveCalculator;
import org.usfirst.frc.team5818.robot.util.Vector2d;
import org.usfirst.frc.team5818.robot.util.Vectors;

import edu.wpi.first.wpilibj.Joystick;

/**
 * The primary robot driver. Responsible for driving the robot.
 */
public class RobotDriver implements Module {
    
    
    
    public static final int DRIVE_TYPE_TWOSTICK_ARCADE = 0;
    public static final int DRIVE_TYPE_TWOSTICK_TANK = 1;
    public static final int DRIVE_TYPE_ONESTICK_ARCADE = 2;
    
    public static final int BUT_TWOSTICK_ARCADE = 6;
    public static final int BUT_TWOSTICK_TANK = 7;
    public static final int BUT_ONESTICK_ARCADE = 11;
    public static final int BUT_INVERT = 9;
    public static final int BUT_NO_INVERT = 8;
    

    private static final Joystick FIRST_JOYSTICK =
            new Joystick(RobotConstants.DRIVER_FIRST_JOYSTICK_PORT);

    private static final Joystick SECOND_JOYSTICK =
            new Joystick(RobotConstants.DRIVER_SECOND_JOYSTICK_PORT);

    private static final DriveCalculator driveCalculator =
            ArcadeDriveCalculator.INSTANCE;
    private static final DriveCalculator tankDriveCalculator =
            TankDriveCalculator.INSTANCE;
    
    private int driveType;
    
    private boolean invertThrottle = false;

    @Override
    public void initModule() {
    }

    @Override
    public void teleopPeriodicModule() {
        
        if(FIRST_JOYSTICK.getRawButton(BUT_TWOSTICK_ARCADE))
            driveType = DRIVE_TYPE_TWOSTICK_ARCADE;
        else if(FIRST_JOYSTICK.getRawButton(BUT_TWOSTICK_TANK))
            driveType = DRIVE_TYPE_TWOSTICK_TANK;
        else if(FIRST_JOYSTICK.getRawButton(BUT_ONESTICK_ARCADE))
            driveType = DRIVE_TYPE_ONESTICK_ARCADE;
        
        if(FIRST_JOYSTICK.getRawButton(BUT_INVERT))
            invertThrottle = true;
        else if(FIRST_JOYSTICK.getRawButton(BUT_NO_INVERT))
            invertThrottle = false;
        
        Vector2d talonPowers =
                driveCalculator.compute(Vectors.fromJoystick(FIRST_JOYSTICK, invertThrottle));;
        
        switch(driveType)
        {
            case DRIVE_TYPE_TWOSTICK_ARCADE: 
                talonPowers =
                driveCalculator.compute(Vectors.fromJoystick(FIRST_JOYSTICK, SECOND_JOYSTICK, invertThrottle));
                break;
            case DRIVE_TYPE_TWOSTICK_TANK:
                talonPowers =
                tankDriveCalculator.compute(Vectors.fromJoystickTank(FIRST_JOYSTICK, SECOND_JOYSTICK, invertThrottle));
                break;
            case DRIVE_TYPE_ONESTICK_ARCADE:
                talonPowers =
                driveCalculator.compute(Vectors.fromJoystick(FIRST_JOYSTICK, invertThrottle));
                break;
        }
        
        Robot.runningRobot.driveTrain.setPower(talonPowers);

    }

    @Override
    public void endModule() {
    }

}
