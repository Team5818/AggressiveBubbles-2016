package team5818.robot.commands;


import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.Field;

/**
 * @author Petey
 * basic auto routine, goes forward through lowbar and shoots 
 *
 */
public class Auto1EForward extends CommandGroup {

    public double collectAngle =
            Preferences.getInstance().getDouble("ArmCollectAngle", -6.0);
    public double shootAngle =
            Preferences.getInstance().getDouble("ArmShootHigh", 40.0);
    public double flyUpVel =
            Preferences.getInstance().getDouble("UpperFlyVel", 100.0);
    public double flyLoVel =
            Preferences.getInstance().getDouble("LowerFlyVel", 60.0);
    public double lowbarDist = 60;

    private SetArmAngle putArmDown = new SetArmAngle(collectAngle);
    private SetArmAngle armDownAgain = new SetArmAngle(collectAngle);
    private DriveDistanceCommand goUnderLowbar =
            new DriveDistanceCommand(lowbarDist, .3, 5);
    private DriveDistanceCommand backUp =
            new DriveDistanceCommand(-lowbarDist, .3, 5);

    private SpinRobot aim = new SpinRobot(-170.0);
    private SpinRobot unAim = new SpinRobot(170.0);
    private Shoot dontMiss = new Shoot(shootAngle, flyUpVel, flyLoVel);

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
        this.addSequential(aim);
        this.addSequential(dontMiss);
        this.addSequential(unAim);
        this.addSequential(armDownAgain);
        this.addSequential(backUp);
    }

}