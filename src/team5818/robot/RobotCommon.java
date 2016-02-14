
package team5818.robot;

import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team5818.robot.Robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.hal.PDPJNI;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.modules.Arm;
import team5818.robot.modules.Eyes;
import team5818.robot.modules.Module;
import team5818.robot.modules.Shooter;
import team5818.robot.modules.drivetrain.ArcadeDriveCalculator;
import team5818.robot.modules.drivetrain.DriveTrain;
import team5818.robot.modules.drivetrain.DriveTrainController;

/**
 * Run from {@link Robot}.
 */
public class RobotCommon extends IterativeRobot {

    /**
     * The instance of this {@link RobotCommon} that is currently running.
     */
    public static RobotCommon runningRobot;

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
            new DriveTrainController(driveTrain,
                    ArcadeDriveCalculator.INSTANCE);
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
        panel = new PowerDistributionPanel();
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
        // driveTrainController.rotateDegrees(90, true);
    }

    @Override
    public void teleopInit() {
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        SmartDashboard.putString("DB/String 0",
                String.valueOf(driveTrain.getLeftMotors().getEncPosAbs()));
        SmartDashboard.putString("DB/String 1",
                String.valueOf(driveTrain.getLeftMotors().getEncPosRaw()));
        SmartDashboard.putString("DB/String 2",
                String.valueOf(driveTrain.getRightMotors().getEncPosAbs()));
        SmartDashboard.putString("DB/String 3",
                String.valueOf(driveTrain.getRightMotors().getEncPosRaw()));
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

    private PowerDistributionPanel panel;

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        modules.forEach(Module::teleopPeriodicModule);
        String data = SmartDashboard.getString("DB/String 0");
        try {
            int talon = Integer.parseInt(data);
            driveTrain.runTalon(talon);
        } catch (Exception e) {
        }
        SmartDashboard.putString("DB/String 1", "" + panel.getCurrent(2));
        SmartDashboard.putString("DB/String 2", "" + panel.getCurrent(1));
        SmartDashboard.putString("DB/String 3", "" + panel.getCurrent(0));
        SmartDashboard.putString("DB/String 4", "" + panel.getCurrent(15));
        SmartDashboard.putString("DB/String 5", "" + panel.getCurrent(13));
        SmartDashboard.putString("DB/String 6", "" + panel.getCurrent(12));
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {

    }

}
