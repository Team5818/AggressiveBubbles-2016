package team5818.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;
import team5818.robot.RobotCommon;

public class DriveVelocityCommand extends Command {

    private boolean hasStarted;
    private boolean hasRun;
    private double velocity = 0;

    public void setVelocity(double vel) {
        velocity = vel;
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        hasStarted = true;
        RobotCommon.runningRobot.driveTrain.getRightMotors()
                .setVelocity(velocity);
        hasRun = true;
    }

    @Override
    protected boolean isFinished() {
        return hasStarted && hasRun;
    }

    @Override
    protected void end() {
    }

    @Override
    protected void interrupted() {
    }

}
