package team5818.robot.commands;


import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.Field;
import team5818.robot.modules.FlyWheel;

/**
 * @author Petey
 * basic auto routine, goes forward through lowbar and shoots 
 *
 */
public class Auto1EForward extends CommandGroup {

    public double collectAngle =
            Preferences.getInstance().getDouble("ArmCollectAngle", 3.0);
    public double shootAngle =
            Preferences.getInstance().getDouble("ArmShootHigh", 40.0);
    public double flyUpVel =
            Preferences.getInstance().getDouble("UpperFlyVel", FlyWheel.SHOOT_VELOCITY_UPPER);
    public double flyLoVel =
            Preferences.getInstance().getDouble("LowerFlyVel", FlyWheel.SHOOT_VELOCITY_LOWER);
    public double lowbarDist = 180;

    private SetArmAngle putArmDown = new SetArmAngle(collectAngle);
    private DriveDistanceCommand goUnderLowbar =
            new DriveDistanceCommand(lowbarDist, .3, 5);
    private SpinRobot spin = new SpinRobot(-170.0);
    private SetArmAngle findTarget = new SetArmAngle(40);
    private SetFlywheelVelocity setFlyVel = new SetFlywheelVelocity(flyUpVel, flyLoVel);
    private AutoAim autoAim = new AutoAim(-14);
    private Shoot dontMiss = new Shoot();

    /**
     * move arm down
     * pass under lowbar
     * spin clockwise 10 deg
     * shoot
     * spin counter clockwise
     * move back under
     */
    public Auto1EForward() {

        this.addSequential(putArmDown);
        this.addSequential(goUnderLowbar);
        this.addSequential(spin);
        this.addSequential(findTarget);
        this.addSequential(setFlyVel);
        this.addSequential(autoAim);
        this.addSequential(dontMiss);

    }

}