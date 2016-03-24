package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;

public class DoNothing extends Command{
    
    double maxTime;
    
    public DoNothing(double timeout){
        maxTime = timeout;
    }
    
    @Override
    protected void initialize() {
        setTimeout(maxTime);
        
    }

    @Override
    protected void execute() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected boolean isFinished() {
        return isTimedOut();
    }

    @Override
    protected void end() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void interrupted() {
        // TODO Auto-generated method stub
        
    }

}
