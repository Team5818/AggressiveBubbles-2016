
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
import team5818.robot.commands.ActuateServo;
import team5818.robot.commands.AutoChavalArc;
import team5818.robot.commands.AutoLowbarArc;
import team5818.robot.commands.AutoLowbarForward;
import team5818.robot.commands.AutoMoatArc;
import team5818.robot.commands.AutoPortcullisArc;
import team5818.robot.commands.AutoPortcullisUniversal;
import team5818.robot.commands.AutoRampartsArc;
import team5818.robot.commands.AutoRampartsUniversal;
import team5818.robot.commands.AutoRockwallArc;
import team5818.robot.commands.AutoRoughTerrainArc;
import team5818.robot.commands.AutoRoughTerrainUniversal;
import team5818.robot.commands.AutoTest;
import team5818.robot.commands.DoNothing;
import team5818.robot.commands.DoNothingAuto;
import team5818.robot.commands.SpybotAuto;
import team5818.robot.modules.Arm;
import team5818.robot.modules.ClimbArm;
import team5818.robot.modules.ClimbArms;
import team5818.robot.modules.ClimbWinch;
import team5818.robot.modules.ClimbWinchs;
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

    public final RobotDriver driver = addModule(new RobotDriver());
    public final RobotCoDriver coDriver = addModule(new RobotCoDriver());
    public final FlyWheel lowerFlywheel =
            new FlyWheel(new CANTalon(RobotConstants.TALON_FLYWHEEL_LOWER),
                    5.0 / 18.8, 140.0, true, true);
    public final FlyWheel upperFlywheel =
            new FlyWheel(new CANTalon(RobotConstants.TALON_FLYWHEEL_UPPER),
                    16.0 / 40.0, 240.0, false, false);
    public final VisionThread vision = addModule(new VisionThread());
    public final Track targeting = new Track();
    public final Arm arm = new Arm();
    public final Collector collector = new Collector(false);
    // public final ClimbArms climber = addModule(new
    // ClimbArms(RobotConstants.TALON_CLIMB_LEFT,
    // RobotConstants.TALON_CLIMB_RIGHT, false, true));
    public final ClimbWinchs winch =
            new ClimbWinchs(RobotConstants.TALON_WINCH_LEFT,
                    RobotConstants.TALON_WINCH_RIGHT, true, false);
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    private Command autoSelected;
    private SendableChooser chooserAuto;
    private SendableChooser chooserPos;

    enum AutoRoutine {
        DO_NOTHING, LOWBAR, PORTCULLIS, ROCKWALL, RAMPARTS, MOAT, ROUGH_TERRAIN,
        CHAVAL, SALLY_PORT, DRAW_BRIDGE, SPYBOT, AUTO_TEST, LOWBAR_ARC,
        PORTCULLIS_ARC, ROUGH_TERRAIN_ARC, ROCKWALL_ARC, RAMPARTS_ARC;
    }

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    @Override
    public void robotInit() {

        runningRobot = this;
        modules.forEach(Module::initModule);

        // Adding auto routines to SmartDashboard.
        chooserAuto = new SendableChooser();
        chooserAuto.addObject("Do Nothing Auto", AutoRoutine.DO_NOTHING);
        chooserAuto.addObject("Lowbar", AutoRoutine.LOWBAR);
        chooserAuto.addObject("Portcullis", AutoRoutine.PORTCULLIS);
        chooserAuto.addObject("Rough Terrain", AutoRoutine.ROUGH_TERRAIN);
        chooserAuto.addObject("Rock Wall", AutoRoutine.ROCKWALL);
        chooserAuto.addObject("Moat", AutoRoutine.MOAT);
        chooserAuto.addObject("Spybot", AutoRoutine.SPYBOT);
        chooserAuto.addObject("!!!AUTO TEST!!!", AutoRoutine.AUTO_TEST);
        chooserAuto.addObject("Arcing Lowbar", AutoRoutine.LOWBAR_ARC);
        chooserAuto.addObject("Arcing Portcullis", AutoRoutine.PORTCULLIS_ARC);
        chooserAuto.addObject("Arcing Rough Terrain", AutoRoutine.ROUGH_TERRAIN_ARC);
        chooserAuto.addObject("RockwallArc", AutoRoutine.ROCKWALL_ARC);
        chooserAuto.addObject("Arcing Ramparts", AutoRoutine.RAMPARTS_ARC);
        chooserAuto.addObject("Chaval de Grease", AutoRoutine.CHAVAL);

        // Adding auto position to SmartDashboard.
        chooserPos = new SendableChooser();
        chooserPos.addObject("2", 2);
        chooserPos.addObject("3", 3);
        chooserPos.addObject("4", 4);
        chooserPos.addObject("5", 5);

        /* NOT WORKING AUTO ROUTINES!! */
        // chooserAuto.addObject("F-Ramparts 2", new AutoRampartsUniversal(2));
        // chooserAuto.addObject("F-Ramparts 3", new AutoRampartsUniversal(2));
        // chooserAuto.addObject("F-Ramparts 4", new AutoRampartsUniversal(2));
        // chooserAuto.addObject("F-Ramparts 5", new AutoRampartsUniversal(2));
        // chooserAuto.addObject("Lowbar Backward", new Auto1EBackward());
        // chooserAuto.addObject("Portcullis Inside", new
        // AutoPortcullisInside());
        // chooserAuto.addObject("Portcullis Outside", new
        // AutoPortcullisOutside());
        // chooserAuto.addObject("Rough/Ramparts Inside", new
        // AutoRoughRampartsInside());
        // chooserAuto.addObject("Rough/Ramparts Outside", new
        // AutoRoughRampartsOutside());
        SmartDashboard.putData("Auto choices", chooserAuto);
        SmartDashboard.putData("Pos choices", chooserPos);

        panel = new PowerDistributionPanel();
    }

    /**
     * This autonomous (along with the chooserAuto code above) shows how to
     * select between different autonomous modes using the dashboard. The
     * sendable chooserAuto code works with the Java SmartDashboard. If you
     * prefer the LabVIEW Dashboard, remove all of the chooserAuto code and
     * uncomment the getString line to get the auto name from the text box below
     * the Gyro
     *
     * You can add additional auto modes by adding additional comparisons to the
     * switch structure below with additional strings. If using the
     * SendableChooser make sure to add them to the chooserAuto code above as
     * well.
     */
    @Override
    public void autonomousInit() {
        modules.forEach(Module::initAutonomous);
        Command auto;
        AutoRoutine autoSelected = (AutoRoutine) chooserAuto.getSelected();
        int pos = (int) chooserPos.getSelected();
        switch (autoSelected) {
            case DO_NOTHING:
                auto = new DoNothingAuto();
                break;
            case LOWBAR:
                auto = new AutoLowbarForward();
                break;
            case LOWBAR_ARC:
                auto = new AutoLowbarArc();
                break;
            case PORTCULLIS:
                auto = new AutoPortcullisUniversal(pos);
                break;
            case PORTCULLIS_ARC:
                auto = new AutoPortcullisArc(pos);
                break;
            case ROUGH_TERRAIN:
                auto = new AutoRoughTerrainUniversal(pos);
                break;
            case ROUGH_TERRAIN_ARC:
                auto = new AutoRoughTerrainArc(pos);
                break;     
            case ROCKWALL:
                auto = new AutoRoughTerrainUniversal(pos);
                break;
            case ROCKWALL_ARC:
                auto = new AutoRockwallArc(pos);
                break;
            case RAMPARTS_ARC:
                auto = new AutoRampartsArc(pos);
                break;
            case MOAT:
                auto = new AutoMoatArc(pos);
                break;
            case RAMPARTS:
                auto = new AutoRoughTerrainUniversal(pos);
                break;
            case CHAVAL:
                auto = new AutoChavalArc(pos);
                break;    
            case SALLY_PORT:
                auto = new DoNothingAuto();
                break;
            case DRAW_BRIDGE:
                auto = new DoNothingAuto();
                break;
            case SPYBOT:
                auto = new SpybotAuto(false);
                break;
            case AUTO_TEST:
                auto = new AutoTest();
                break;
            default:
                auto = new AutoLowbarForward();
                break;

        }
        auto.start();
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
        targeting.GetData();

    }

    private PowerDistributionPanel panel;

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        modules.forEach(Module::teleopPeriodicModule);
        targeting.GetData();
        /*
         * if(RobotCoDriver.firstJoystick.getRawButton(RobotCoDriver.
         * BUT_PERFORM_AUTO)) { Command autoSelected = (Command)
         * chooserAuto.getSelected(); autoSelected.start(); }
         */
        SmartDashboard.putNumber("Arm Angle", arm.getAngle());
        SmartDashboard.putNumber("Arm Angle Raw", arm.getRawPot());
        SmartDashboard.putNumber("Lower Flywheel RPS", lowerFlywheel.getRPS());

        
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