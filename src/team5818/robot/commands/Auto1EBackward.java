package team5818.robot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.Field;

/**
 * @author Petey
 * basic auto routine, goes backward through lowbar and shoots
 *
 */
public class Auto1EBackward extends CommandGroup {

    public double collectAngle =
            Preferences.getInstance().getDouble("ArmAngleCollect", 0.0);
    public double shootAngle =
            Preferences.getInstance().getDouble("ShootAngleLow", 40.0);
    public double flyUpVel =
            Preferences.getInstance().getDouble("UpperFlyVel", 192.0);
    public double flyLoVel =
            Preferences.getInstance().getDouble("LowerFlyVel", 112.0);
    public double lowbarDist = 180;

    private SetArmAngle putArmDown = new SetArmAngle(collectAngle);
    private SetArmAngle armDownAgain = new SetArmAngle(collectAngle);
    private SetArmAngle findTarget = new SetArmAngle(40);
    private DriveDistanceCommand goUnderLowbar =
            new DriveDistanceCommand(-lowbarDist, .5, 5);
    private DriveDistanceCommand backUp =
            new DriveDistanceCommand(lowbarDist, .5, 5);
    
    private AutoAim autoAim = new AutoAim(5);

    private SpinRobot spin = new SpinRobot(60.0);
    private SpinRobot unAim = new SpinRobot(-60.0);
    private Shoot dontMiss = new Shoot(flyUpVel, flyLoVel);

    /**
     * move arm down
     * pass under lowbar
     * spin clockwise 10 deg
     * shoot
     * spin counter clockwise
     * move back under
     */
    public Auto1EBackward() {

        this.addSequential(putArmDown);
        this.addSequential(goUnderLowbar);
        this.addSequential(spin);
        this.addSequential(findTarget);
        
        this.addSequential(dontMiss);
        this.addSequential(unAim);
        this.addSequential(armDownAgain);
        this.addSequential(backUp);
    }

}
