
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
import team5818.robot.commands.Auto1EBackward;
import team5818.robot.commands.Auto1EForward;
import team5818.robot.commands.AutoPortcullisInside;
import team5818.robot.commands.AutoPortcullisOutside;
import team5818.robot.commands.AutoPortcullisUniversal;
import team5818.robot.commands.AutoRampartsUniversal;
import team5818.robot.commands.AutoRoughRampartsInside;
import team5818.robot.commands.AutoRoughRampartsOutside;
import team5818.robot.commands.AutoRoughTerrainUniversal;
import team5818.robot.commands.AutoTest;
import team5818.robot.commands.DoNothing;
import team5818.robot.commands.DoNothingAuto;
import team5818.robot.commands.SpybotAuto;
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
                    5.0 / 18.8, 140.0, true, true));
    public final FlyWheel upperFlywheel = addModule(
            new FlyWheel(new CANTalon(RobotConstants.TALON_FLYWHEEL_UPPER),
                    16.0 / 40.0, 240.0, false, false));
    public final VisionThread vision = addModule(new VisionThread());
    public final Track targeting = addModule(new Track());
    public final Arm arm = addModule(new Arm());
    public final Collector collector = addModule(new Collector(false));
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    private Command autoSelected;
    private SendableChooser chooser;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {
        runningRobot = this;
        modules.forEach(Module::initModule);
        chooser = new SendableChooser();
        
        //Adding auto routines to SmartDashboard.
        chooser.addObject("Do Nothing Auto", new DoNothingAuto());
        chooser.addObject("Lowbar Forward", new Auto1EForward());
        chooser.addObject("F-Portcullis 2", new AutoPortcullisUniversal(2));
        chooser.addObject("F-Portcullis 3", new AutoPortcullisUniversal(3));
        chooser.addObject("F-Portcullis 4", new AutoPortcullisUniversal(4));
        chooser.addObject("F-Portcullis 5", new AutoPortcullisUniversal(5));
        chooser.addObject("B-Rough Terrain 2", new AutoRoughTerrainUniversal(2));
        chooser.addObject("B-Rough Terrain 3", new AutoRoughTerrainUniversal(3));
        chooser.addObject("B-Rough Terrain 4", new AutoRoughTerrainUniversal(4));
        chooser.addObject("B-Rough Terrain 5", new AutoRoughTerrainUniversal(5));
        chooser.addObject("Spybot W/O Lowbar", new SpybotAuto(SpybotAuto.WITH_OUT_LOWBAR));
        chooser.addObject("Spybot W Lowbar", new SpybotAuto(SpybotAuto.WITH_LOWBAR));
        //chooser.addObject("!!!AUTO TEST!!!", new AutoTest());

        /* NOT WORKING AUTO ROUTINES!! */
        //chooser.addObject("F-Ramparts 2", new AutoRampartsUniversal(2));
        //chooser.addObject("F-Ramparts 3", new AutoRampartsUniversal(2));
        //chooser.addObject("F-Ramparts 4", new AutoRampartsUniversal(2));
        //chooser.addObject("F-Ramparts 5", new AutoRampartsUniversal(2));
        //chooser.addObject("Lowbar Backward", new Auto1EBackward());
        //chooser.addObject("Portcullis Inside", new AutoPortcullisInside());
        //chooser.addObject("Portcullis Outside", new AutoPortcullisOutside());
        //chooser.addObject("Rough/Ramparts Inside", new AutoRoughRampartsInside());
        //chooser.addObject("Rough/Ramparts Outside", new AutoRoughRampartsOutside());
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
        modules.forEach(Module::autoPeriodicModule);

    }

    private PowerDistributionPanel panel;

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        modules.forEach(Module::teleopPeriodicModule);
        /*
        if(RobotCoDriver.firstJoystick.getRawButton(RobotCoDriver.BUT_PERFORM_AUTO)) {
            Command autoSelected = (Command) chooser.getSelected();
            autoSelected.start();
        }
        */
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
        arm.setPower(0);
        lowerFlywheel.setPower(0);
        upperFlywheel.setPower(0);
        arm.getFirstMotor().enableBrakeMode(true);
        arm.getSecondMotor().enableBrakeMode(true);
    }

}