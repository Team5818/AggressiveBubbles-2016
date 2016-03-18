
package team5818.robot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.usfirst.frc.team5818.robot.Robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.commands.Auto1E;
import team5818.robot.commands.DoNothingAuto;
import team5818.robot.commands.SpinTest;
import team5818.robot.commands.TestDriveVel;
import team5818.robot.modules.Arm;
import team5818.robot.modules.Collector;
import team5818.robot.modules.FlyWheel;
import team5818.robot.modules.Module;
import team5818.robot.modules.Track;
import team5818.robot.modules.VisionThread;
import team5818.robot.modules.drivetrain.ArcadeDriveCalculator;
import team5818.robot.modules.drivetrain.DriveTrain;

/**
 * Run from {@link Robot}.
 */
public class RobotCommon extends IterativeRobot {

    /**
     * The instance of this {@link RobotCommon} that is currently running.
     */
    public static RobotCommon runningRobot;
    private List<Module> modules = new ArrayList<Module>() {

        private static final long serialVersionUID = 7074129390191811566L;

        /**
         * 1337 hack to prevent iterating from breaking robot.
         */
        @Override
        public void forEach(Consumer<? super Module> action) {
            try {
                super.forEach(action);
            } catch (Exception e) {
                DriverStation.reportError(
                        "Error iterating modules: " + e.getMessage() + "\n",
                        false);
            }
        };
    };

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

    private final RobotDriver driver = addModule(new RobotDriver());
    private final RobotCoDriver coDriver = addModule(new RobotCoDriver());
    public final FlyWheel lowerFlywheel = addModule(
            new FlyWheel(new CANTalon(RobotConstants.TALON_FLYWHEEL_LOWER),
                    5.0 / 18.8, 140.0, true));
    public final FlyWheel upperFlywheel = addModule(
            new FlyWheel(new CANTalon(RobotConstants.TALON_FLYWHEEL_UPPER),
                    16.0 / 40.0, 240.0, true));
    public final VisionThread vision = addModule(new VisionThread());
    public final Track targeting = addModule(new Track());
    public final Arm arm = addModule(new Arm());
    public final Collector collector = addModule(new Collector(false));
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    Command autoSelected;
    SendableChooser chooser;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        runningRobot = this;
        modules.forEach(Module::initModule);
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("Lowbar Auto", new Auto1E());
        chooser.addObject("Do Nothing Auto", new DoNothingAuto());
        chooser.addObject("Drive Velocity", new TestDriveVel());
        SmartDashboard.putData("Auto choices", chooser);
        panel = new PowerDistributionPanel();
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
    @Override
    public void autonomousInit() {
        modules.forEach(Module::initAutonomous);
        autoSelected = (Command) chooser.getSelected();
        // autoSelected = SmartDashboard.getString("Auto Selector",
        // defaultAuto);
        // System.out.println("Auto selected: " + autoSelected);
        autoSelected.start();
        // driveTrainController.rotateDegrees(90, true);
        Scheduler.getInstance().enable();
    }

    @Override
    public void teleopInit() {
        Scheduler.getInstance().enable();
        modules.forEach(Module::initTeleop);
    }

    /**
     * This function is called periodically during autonomous
     */
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();

    }

    private PowerDistributionPanel panel;

    public void setDriveAutoAim() {
        driver.setAutoAim();
    }

    public void setDriveDefault() {
        driver.stopMovement();
    }
    
    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        modules.forEach(Module::teleopPeriodicModule);
    }

    @Override
    public void testInit() {
        modules.forEach(Module::initTest);
        LiveWindow.setEnabled(true);
    }

    /**
     * This function is called periodically during test mode
     */
    @Override
    public void testPeriodic() {

        modules.forEach(Module::testPeriodic);
    }

    @Override
    public void disabledInit() {
        driveTrain.getLeftMotors().getPIDController().disable();
        driveTrain.getRightMotors().getPIDController().disable();
    }

}