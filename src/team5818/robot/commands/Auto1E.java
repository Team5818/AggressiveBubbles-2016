package team5818.robot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import team5818.robot.Field;

/**
 * @author Petey
 * basic auto routine 
 *
 */
public class Auto1E extends CommandGroup {

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
            new DriveDistanceCommand(-lowbarDist, .3, 5);
    private DriveDistanceCommand backUp =
            new DriveDistanceCommand(lowbarDist, .3, 5);
    private DoNothing sitAround = new DoNothing(2);
    private DoNothing chill = new DoNothing(2);
    private SpinRobot aim = new SpinRobot(10.0);
    private SpinRobot unAim = new SpinRobot(-10.0);
    private Shoot dontMiss = new Shoot(shootAngle, flyUpVel, flyLoVel);

    /**
     * move arm down
     * pass under lowbar
     * spin clockwise 10 deg
     * shoot
     * spin counter clockwise
     * move back under
     */
    public Auto1E() {

        this.addSequential(putArmDown);
        this.addSequential(goUnderLowbar);
        this.addSequential(sitAround);
        this.addSequential(aim);
        this.addSequential(dontMiss);
        this.addSequential(chill);
        this.addSequential(unAim);
        this.addSequential(armDownAgain);
        this.addSequential(backUp);
    }

}
