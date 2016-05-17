package team5818.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class ActuateServo extends Command {
    
    public static final boolean EXTENDED = true;
    public static final boolean RETRACTED = false;
    private double frontOffset = 0;
    private double backOffset = 0;
    
    private boolean state = RETRACTED;
    
    private static Servo frontServo = new Servo(2);
    private static Servo backServo = new Servo(3);
    
    
    public ActuateServo(boolean state) {
        this.state = state;
    }
    
    @Override
    public void initialize() {
        if(state == EXTENDED) {
            frontServo.setAngle(90);
            backServo.setAngle(110); 
        }
        else {
            frontServo.setAngle(157);
            backServo.setAngle(40); 
        }
    }
    
    public static Servo getFrontServo() {
        return frontServo;
    }
    
    public static Servo getBackServo() {
        return backServo;
    }

    @Override
    protected void execute() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected boolean isFinished() {
        // TODO Auto-generated method stub
        return true;
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
