package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class DoNothingAuto extends CommandGroup{
    
    public DoNothingAuto(double time) {
        this.setTimeout(time);
    }
    
    public DoNothingAuto() {
        this.setTimeout(0.1);
    }
}
