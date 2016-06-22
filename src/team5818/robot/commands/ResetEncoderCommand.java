package team5818.robot.commands;

import org.usfirst.frc.team5818.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ResetEncoderCommand extends Command {

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        Robot.runningRobot.driveTrain.getLeftMotors().setDriveDistance(0);
        Robot.runningRobot.driveTrain.getRightMotors()
                .setDriveDistance(0);
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {
    }

    @Override
    protected void interrupted() {
    }

}
