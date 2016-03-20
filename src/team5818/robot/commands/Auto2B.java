package team5818.robot.commands;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * 
 * @author Petey
 * basic auto routine, uses velocity control to go over ramparts, 
 * might work on rough terrain.
 *
 */
public class Auto2B extends CommandGroup{

    double defenseDist = 180;
    double armAngle = Preferences.getInstance().getDouble("ArmAngleLow", 20.0);
    SetArmAngle lowerArm = new SetArmAngle(armAngle);
    DriveVelocityCommand driveVel = new DriveVelocityCommand(60,  defenseDist);
    
    public Auto2B(){
        this.addSequential(lowerArm);
        this.addSequential(driveVel);
    }
}
