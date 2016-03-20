package team5818.robot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.RobotCommon;
import team5818.robot.modules.Collector;
import team5818.robot.modules.ComputerVision;

public class Shoot extends CommandGroup {

    private SetFlywheelPower flyToZero = new SetFlywheelPower(0);
    private Collect collectIn = new Collect(Collect.COLLECT_POWER, 2);
    private LEDToggle lightUp = new LEDToggle(true);
    private LEDToggle lightsOff = new LEDToggle(false);
    private SwitchFeed switchCam = new SwitchFeed(ComputerVision.CAMERA_SHOOTER);
    private CommandGroup prepareShot = new CommandGroup();

    /**
     * The maximum time the shooter can be on in nano seconds.
     */
    private double maxShootTime = 4;

    /**
     * @param angle
     *            to raise arm to
     * @param flyUpVel
     *            velocity to spin upper fly to
     * @param flyLoVel
     *            velocity to spin lower fly to
     */
    public Shoot(double angle, double flyUpVel, double flyLoVel) {
        
         SetFlywheelVelocity setFlyVelocity =
                new SetFlywheelVelocity(flyUpVel, flyLoVel);
        
        SetArmAngle setArmAngle = new SetArmAngle(angle);
        prepareShot.addParallel(setArmAngle);
        prepareShot.addParallel(setFlyVelocity);
        this.addSequential(prepareShot);
        this.addSequential(lightUp);
        this.addSequential(switchCam);
        this.addSequential(collectIn);
        this.addSequential(lightsOff);
        this.addSequential(flyToZero);
    }

}
