package team5818.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.command.CommandGroup;

public class ActuateServo extends CommandGroup {
    
    public static final double ACT_TO_90 = 90;
    public static final double ACT_TO_0 = 0;
    private double frontOffset = 0;
    private double backOffset = 0;
    
    private double angle = 0;
    
    private static Servo frontServo = new Servo(2);
    private static Servo backServo = new Servo(3);
    
    
    public ActuateServo(double angle) {
        this.angle = angle;
    }
    
    @Override
    public void initialize() {
        if(angle == ACT_TO_90) {
            frontServo.setAngle(90);
            backServo.setAngle(100); 
        }
        else {
            frontServo.setAngle(200);
            backServo.setAngle(0); 
        }
    }
    
    public static Servo getFrontServo() {
        return frontServo;
    }
    
    public static Servo getBackServo() {
        return backServo;
    }
    
    

}
