
package org.usfirst.frc.team5818.robot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import team5818.robot.RobotCoDriver;
import team5818.robot.RobotConstants;
import team5818.robot.RobotDriver;
import team5818.robot.commands.ActuateServo;
import team5818.robot.commands.AutoAim;
import team5818.robot.commands.AutoChavalArc;
import team5818.robot.commands.AutoLowbarArc;
import team5818.robot.commands.AutoMoatArc;
import team5818.robot.commands.AutoPortcullisArc;
import team5818.robot.commands.AutoRampartsArc;
import team5818.robot.commands.AutoRockwallArc;
import team5818.robot.commands.AutoRoughTerrainArc;
import team5818.robot.commands.AutoTest;
import team5818.robot.commands.DoNothing;
import team5818.robot.modules.Arm;
import team5818.robot.modules.ClimbWinches;
import team5818.robot.modules.Collector;
import team5818.robot.modules.FlyWheel;
import team5818.robot.modules.Module;
import team5818.robot.modules.Track;
import team5818.robot.modules.VisionThread;
import team5818.robot.modules.drivetrain.DriveTrain;

/**
 * Run from {@link Robot}.
 */
public class Robot extends IterativeRobot {

    /**
     * The instance of this {@link Robot} that is currently running.
     */
    public static Robot runningRobot;
    
    /**
     * List of Modules running in robot 
     */
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
     * The UI for the Robot Driver
     */
    public final RobotDriver driver = addModule(new RobotDriver());
    
    /**
     * The UI for the Robot Co-Driver
     */
    public final RobotCoDriver coDriver = addModule(new RobotCoDriver());
    
    /**
     * The Lower FlyWheel on the shooter
     */
    public final FlyWheel lowerFlywheel =
            new FlyWheel(new CANTalon(RobotConstants.TALON_FLYWHEEL_LOWER),
                    5.0 / 18.8, 140.0, true, true);
    /**
     * The Upper FlyWheel on the shooter
     */
    public final FlyWheel upperFlywheel =
            new FlyWheel(new CANTalon(RobotConstants.TALON_FLYWHEEL_UPPER),
                    16.0 / 40.0, 240.0, false, false);
    /**
     * The Arm of the Robot
     */
    public final Arm arm = new Arm();
    
    /**
     * The Robot's ball intake
     */
    public final Collector collector = new Collector(false);
    
    /**
     * The winch for climbing during endgame
     */
    public final ClimbWinches winch =
            new ClimbWinches(RobotConstants.TALON_WINCH_LEFT,
                    RobotConstants.TALON_WINCH_RIGHT, false, true);
    /**
     * The thread for retrieving frames from the camera
     */
    //public final VisionThread vision = addModule(new VisionThread());
    
    /**
     * The module for processing images from the camera
     */
    //public final Track targeting = new Track();
    
    /**
     * The menu for choosing the defense to be crossed in Auto
     */
    private SendableChooser chooserAuto;
    
    /**
     * The menu for choosing the position of the auto routine
     */
    private SendableChooser chooserPos;
    
    /**
     *  Enum of all of the possible defenses to cross in auto
     */
    enum AutoRoutine {
        DO_NOTHING, LOWBAR, PORTCULLIS, ROCKWALL, RAMPARTS, MOAT, ROUGH_TERRAIN,
        CHEVAL, SALLY_PORT, DRAW_BRIDGE, SPYBOT, AUTO_TEST;
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
        chooserAuto.addObject("Ramparts", AutoRoutine.RAMPARTS);
        chooserAuto.addObject("Moat/Rough Terrain", AutoRoutine.MOAT);
        chooserAuto.addObject("Chaval de Grease", AutoRoutine.CHEVAL);
        chooserAuto.addObject("Spybot", AutoRoutine.SPYBOT);
        chooserAuto.addObject("!!!AUTO TEST!!!", AutoRoutine.AUTO_TEST);

        // Adding auto position to SmartDashboard.
        chooserPos = new SendableChooser();
        chooserPos.addObject("2", 2);
        chooserPos.addObject("3", 3);
        chooserPos.addObject("4", 4);
        chooserPos.addObject("5", 5);

        SmartDashboard.putData("Auto choices", chooserAuto);
        SmartDashboard.putData("Pos choices", chooserPos);

    }

    /**
     *
     *Takes data from autonomous choosers and selects appropriate auto routine.
     */
    @Override
    public void autonomousInit() {
        (new ActuateServo(ActuateServo.RETRACTED)).start();
        modules.forEach(Module::initAutonomous);
        Command auto;
        AutoRoutine autoSelected = (AutoRoutine) chooserAuto.getSelected();
        int pos = (int)chooserPos.getSelected();
        switch (autoSelected) {
            case DO_NOTHING:
                auto = new DoNothing(15);
                break;
            case LOWBAR:
                auto = new AutoLowbarArc();
                break;
            case PORTCULLIS:
                auto = new AutoPortcullisArc(pos);
                break;
            case ROUGH_TERRAIN:
                auto = new AutoRoughTerrainArc(pos);
                break;
            case ROCKWALL:
                auto = new AutoRockwallArc(pos);
                break;
            case RAMPARTS:
                auto = new AutoRampartsArc(pos);
                break;
            case MOAT:
                auto = new AutoMoatArc(pos);
                break;
            case CHEVAL:
                auto = new AutoChavalArc(pos);
                break;    
            case SALLY_PORT:
                auto = new DoNothing(15);
                break;
            case DRAW_BRIDGE:
                auto = new DoNothing(15);
                break;
            case AUTO_TEST:
                auto = new AutoTest();
                break;
            default:
                auto = new DoNothing(15);
                break;

        }
        auto.start();
        Scheduler.getInstance().enable();
    }

    /**
     * Runs the designated telepInit method of each module
     */
    @Override
    public void teleopInit() {
        Scheduler.getInstance().enable();
        modules.forEach(Module::initTeleop);
        (new ActuateServo(ActuateServo.EXTENDED)).start();
    }

    /**
     * Runs the designated autoPeriodic method of each module
     */
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
        modules.forEach(Module::autoPeriodicModule);
        //targeting.GetData();
        displayNumbers();

    }


    /**
     * Runs the designated teleopPeriodic method of each module
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        modules.forEach(Module::teleopPeriodicModule);
        //targeting.GetData();
        displayNumbers();
    }

    /**
     * Runs the designated testInit method of each module
     */
    @Override
    public void testInit() {
        modules.forEach(Module::initTest);
        LiveWindow.setEnabled(true);
        (new ActuateServo(ActuateServo.RETRACTED)).start();
    }

    /**
     * Runs the designated testPeriodic method of each module
     */
    @Override
    public void testPeriodic() {

        modules.forEach(Module::testPeriodic);
    }

    /**
     * Disable all motors and enter brake mode
     */
    @Override
    public void disabledInit() {
        driveTrain.getLeftMotors().getPIDController().disable();
        driveTrain.getRightMotors().getPIDController().disable();
        arm.setPower(0);
        lowerFlywheel.setPower(0);
        upperFlywheel.setPower(0);

    }
    
    @Override
    public void disabledPeriodic() {
        
    }
    
    /**
     * Print data from sensors to smart dashboard
     */
    private void displayNumbers() {
        SmartDashboard.putNumber("Arm Angle", arm.getAngle());
        SmartDashboard.putNumber("Arm Angle Raw", arm.getRawPot());
        SmartDashboard.putNumber("Upper Fly RPS", upperFlywheel.getRPS());
        SmartDashboard.putNumber("Lower Fly RPS", lowerFlywheel.getRPS());
        SmartDashboard.putNumber("Left Winch RPS", winch.getLeft().getRPS());
        SmartDashboard.putNumber("Right Winch RPS", winch.getRight().getRPS());
        SmartDashboard.putNumber("Left Winch Counts", winch.getLeft().getTalon().getEncPosition());
        SmartDashboard.putNumber("Right Winch Counts", winch.getRight().getTalon().getEncPosition());
        SmartDashboard.putNumber("AA - Err Y", AutoAim.calculateAngleY());
        SmartDashboard.putNumber("AA - Err X", AutoAim.calculateAngleX());
        //SmartDashboard.putNumber("locX", targeting.blobLocX);
        //SmartDashboard.putNumber("locY", targeting.blobLocY);
        SmartDashboard.putNumber("Left Winch Height", winch.getLeft().getTalon().getEncPosition() / 11339.0 * 100);
        SmartDashboard.putNumber("Right WInch Height", winch.getRight().getTalon().getEncPosition() / 10967.0 * 100);
    }
    

}