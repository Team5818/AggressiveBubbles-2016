package org.usfirst.frc.team5818.robot.commands;

import org.usfirst.frc.team5818.robot.Robot;
import org.usfirst.frc.team5818.robot.util.Vector2d;

import edu.wpi.first.wpilibj.command.Command;

public class DriveForwardSlowlyCommand extends Command {
    
    private boolean hasRun;

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        Robot.runningRobot.driveTrain.setPower(new Vector2d(0.25, 0.25));
        hasRun = true;
    }

    @Override
    protected boolean isFinished() {
        return hasRun;
    }

    @Override
    protected void end() {
        Robot.runningRobot.driveTrain.setPower(new Vector2d(0, 0));
    }

    @Override
    protected void interrupted() {
    }

}
