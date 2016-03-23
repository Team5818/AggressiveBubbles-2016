package team5818.robot.commands;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.Field;
import team5818.robot.modules.FlyWheel;

/**
 * @author Petey
 * basic auto routine for portcullis
 *
 */

public class AutoPortcullisInside extends CommandGroup{
    public double lowAngle =
            Preferences.getInstance().getDouble("ArmShootLow", 40.0);
    public double flyUpVel =
            Preferences.getInstance().getDouble("UpperFlyVel", FlyWheel.SHOOT_VELOCITY_UPPER);
    public double flyLoVel =
            Preferences.getInstance().getDouble("LowerFlyVel", FlyWheel.SHOOT_VELOCITY_LOWER);
    public double lowbarDist = 60;

    private SetArmAngle lowerArm = new SetArmAngle(lowAngle);
    private LowerArmToGround armToGround = new LowerArmToGround();
    private DriveDistanceCommand driveToPortcullis =
            new DriveDistanceCommand(lowbarDist, .3, 5);
    private SetArmAngle findTarget = new SetArmAngle(40);
    private SetFlywheelVelocity setFlyVel = new SetFlywheelVelocity(flyUpVel, flyLoVel);
    private AutoAim aim = new AutoAim();
    private Shoot dontMiss = new Shoot();

    /**
     * move arm down
     * pass under lowbar
     * spin clockwise 10 deg
     * shoot
     * spin counter clockwise
     * move back under
     */
    public AutoPortcullisInside() {

        this.addSequential(lowerArm);
        this.addSequential(armToGround);
        this.addSequential(driveToPortcullis);
        this.addSequential(findTarget);
        this.addSequential(setFlyVel);
        this.addSequential(aim);
        this.addSequential(dontMiss);

    }


}
