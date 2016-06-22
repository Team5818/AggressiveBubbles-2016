package team5818.robot.commands;

import org.usfirst.frc.team5818.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.modules.ComputerVision;
import team5818.robot.modules.Track;
import team5818.robot.modules.drivetrain.DriveTrain;
import team5818.robot.util.Vector2d;

public class ScanForTarget extends Command{

    private boolean clockwise;
    private Track targeting = Robot.runningRobot.targeting;
    private double power;
    private DriveTrain train = Robot.runningRobot.driveTrain;

    public ScanForTarget(boolean cw, double pow, double timeout){
        clockwise = cw;
        power = pow;
        setTimeout(timeout);
    }
    
    public ScanForTarget(boolean cw){
        this(cw, .3, 3);
    }
    
    @Override
    protected void initialize() {
        if(!clockwise){
            power *= -1;
        }
        Vector2d powers = new Vector2d(power, -power);
        train.setPower(powers);
        
    }

    @Override
    protected void execute(){
        targeting.GetData();
        
    }

    @Override
    protected boolean isFinished() {
        return ((targeting.blobCount >= 1) || isTimedOut());

    }

    @Override
    protected void end() {
        train.setPower(new Vector2d(0,0));
        
    }

    @Override
    protected void interrupted() {
        end();
        
    }
    

}
