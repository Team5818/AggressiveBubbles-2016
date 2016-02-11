
package org.usfirst.frc.team5818.robot;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team5818.robot.modules.Arm;
import org.usfirst.frc.team5818.robot.modules.Eyes;
import org.usfirst.frc.team5818.robot.modules.Module;
import org.usfirst.frc.team5818.robot.modules.Shooter;
import org.usfirst.frc.team5818.robot.modules.drivetrain.ArcadeDriveCalculator;
import org.usfirst.frc.team5818.robot.modules.drivetrain.DriveCalculator;
import org.usfirst.frc.team5818.robot.modules.drivetrain.DriveTrain;
import org.usfirst.frc.team5818.robot.modules.drivetrain.DriveTrainController;

import edu.wpi.first.wpilibj.IterativeRobot;
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

    /**
     * The instance of this {@link Robot} that is currently running.
     */
    public static Robot runningRobot;

    private List<Module> modules = new ArrayList<>();

    private <M extends Module> M addModule(M module) {
        modules.add(module);
        return module;
    }

    /**
     * The drive train connected to the robot talons.
     */
    public final DriveTrain driveTrain = addModule(new DriveTrain());
    /**
     * A helper for the {@link #driveTrain}.
     */
    public final DriveTrainController driveTrainController =
            new DriveTrainController(ArcadeDriveCalculator.INSTANCE);
    private final RobotDriver driver = addModule(new RobotDriver());
    private final RobotCoDriver coDriver = addModule(new RobotCoDriver());
    private final Shooter shooter = addModule(new Shooter());
    private final Arm arm = addModule(new Arm());
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    String autoSelected;
    SendableChooser chooser;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        runningRobot = this;
        modules.forEach(Module::initModule);
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
        new Eyes().LookAtMe();
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
                break;
        }
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        modules.forEach(Module::teleopPeriodicModule);
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {

    }

}
