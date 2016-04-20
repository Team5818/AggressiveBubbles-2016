package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

public class ShootIfReady extends Command {
    private AutoAim aim;
    private boolean finished = false;
    private boolean hasStartedShooting = false;
    private long time0;
    
    public ShootIfReady(AutoAim aim) {
        this.aim = aim;
    }
    
    public void execute() {
        if(aim.foundTarget()) {
            new Collect(1).start();
            hasStartedShooting = true;
            time0 = System.nanoTime();
        }
        
        if(Math.abs(System.nanoTime() - time0) > (long)(0.5*1000000000)) {
            finished = true;
        }
        
    }
    
    public boolean isFinished() {
        return isTimedOut() || finished;
    }

    @Override
    protected void initialize() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void end() {
    }

    @Override
    protected void interrupted() {
        end();
    }

}
