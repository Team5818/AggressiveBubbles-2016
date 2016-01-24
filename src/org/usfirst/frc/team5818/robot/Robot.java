
package org.usfirst.frc.team5818.robot;

import org.usfirst.frc.team5818.robot.powerpc.Calculator;
import org.usfirst.frc.team5818.robot.powerpc.TankDriveCalculator;
import org.usfirst.frc.team5818.robot.util.Vector2d;
import org.usfirst.frc.team5818.robot.util.Vectors;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick.ButtonType;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    String autoSelected;
    SendableChooser chooser;
    Calculator driveCalculator = new TankDriveCalculator();
    DriveSide leftSet = new DriveSide(RobotConstants.TALON_LEFT_BACK,
            RobotConstants.TALON_LEFT_FRONT);
    DriveSide rightSet = new DriveSide(RobotConstants.TALON_RIGHT_BACK,
            RobotConstants.TALON_RIGHT_FRONT);
    Arm arm = new Arm();

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable
     * chooser code works with the Java SmartDashboard. If you prefer the
     * LabVIEW Dashboard, remove all of the chooser code and uncomment the
     * getString line to get the auto name from the text box below the Gyro
     *
     * You can add additional auto modes by adding additional comparisons to the
     * switch structure below with additional strings. If using the
     * SendableChooser make sure to add them to the chooser code above as well.
     */
    public void autonomousInit() {
        autoSelected = (String) chooser.getSelected();
        // autoSelected = SmartDashboard.getString("Auto Selector",
        // defaultAuto);
        System.out.println("Auto selected: " + autoSelected);
    }

    @Override
    public void teleopInit() {
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        switch (autoSelected) {
            case customAuto:
                // Put custom auto code here
                break;
            case defaultAuto:
            default:
                // Put default auto code here
                Vector2d talonPowers =
                        driveCalculator.compute(new Vector2d(0.1, 0.1));
                leftSet.pidWrite(talonPowers.getX());
                rightSet.pidWrite(talonPowers.getY());
                break;
        }
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	//Driver teleop
        Vector2d talonPowers = driveCalculator
                .compute(Vectors.fromJoystick(RobotConstants.JOYSTICK_A));
        leftSet.pidWrite(talonPowers.getX());
        rightSet.pidWrite(talonPowers.getY());
        
        //Arm teleop
        arm.armTeleopPeriodic();
        
        if (RobotConstants.JOYSTICK_C.getRawButton(RobotConstants.ARM_RESET)){
        	RobotConstants.ARM_ENCODER.reset();
        }
        if (RobotConstants.JOYSTICK_C.getRawButton(RobotConstants.PRINT_ANGLE)){
        	DriverStation.reportError("" + arm.getAngle() + "\n", false);
        }
        if(RobotConstants.JOYSTICK_C.getRawButton(RobotConstants.LEFT_UP_ANGLE_BUTTON)){
        	arm.aimAdjustLeft(true);
        }
        if(RobotConstants.JOYSTICK_C.getRawButton(RobotConstants.LEFT_DOWN_ANGLE_BUTTON)){
        	arm.aimAdjustLeft(false);
        }
        if(RobotConstants.JOYSTICK_C.getRawButton(RobotConstants.RIGHT_UP_ANGLE_BUTTON)){
        	arm.aimAdjustRight(true);
        }
        if(RobotConstants.JOYSTICK_C.getRawButton(RobotConstants.RIGHT_DOWN_ANGLE_BUTTON)){
        	arm.aimAdjustRight(false);
        }  
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {

    }

}
