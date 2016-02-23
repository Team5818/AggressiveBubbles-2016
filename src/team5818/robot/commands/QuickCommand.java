package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

public abstract class QuickCommand extends Command {

    private boolean hasStarted;
    private boolean hasEnded;

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        hasStarted = true;
        hasEnded = false;
        try {
            subexecute();
        } finally {
            hasEnded = true;
        }
    }

    protected abstract void subexecute();

    @Override
    protected boolean isFinished() {
        return hasStarted && hasEnded;
    }

    @Override
    protected void end() {
        hasStarted = false;
        hasEnded = false;
    }

    @Override
    protected void interrupted() {
        end();
    }

}
