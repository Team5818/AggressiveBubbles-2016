package team5818.robot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class Auto2A extends CommandGroup{
    
    public double shootAngle =
            Preferences.getInstance().getDouble("ArmShootHigh", 40.0);
    public double flyUpVel =
            Preferences.getInstance().getDouble("UpperFlyVel", 100.0);
    public double flyLoVel =
            Preferences.getInstance().getDouble("LowerFlyVel", 60.0);
    public double lowbarDist = 60;

    private LowerArmToGround lowerChaval = new LowerArmToGround();
    private LowerArmToGround lowerAgain = new LowerArmToGround();
    private DriveDistanceCommand driveToChaval =
            new DriveDistanceCommand(lowbarDist, .3, 5);
    private DriveDistanceCommand crossChaval =
            new DriveDistanceCommand(20, .3, 5);
    private DriveDistanceCommand backUp =
            new DriveDistanceCommand(-lowbarDist, .3, 5);
    private DriveDistanceCommand reCross =
            new DriveDistanceCommand(-20, .3, 5);
    private DoNothing sitAround = new DoNothing(2);
    private DoNothing chill = new DoNothing(2);
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
    public Auto2A() {

        this.addSequential(driveToChaval);
        this.addSequential(lowerChaval);
        this.addSequential(crossChaval);
        this.addSequential(sitAround);
        this.addSequential(aim);
        this.addSequential(dontMiss);
        this.addSequential(chill);
        this.addSequential(unAim);
        this.addSequential(backUp);
        this.addSequential(lowerAgain);
        this.addSequential(reCross);
        

    }


}
