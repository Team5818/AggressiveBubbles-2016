package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Adds collect commands in sequance so that they can clear the collector if the ball is jammed.
 */
public class ClearCollector extends CommandGroup {

    private static final double SPIT_OUT_TIME = 0.25;
    private static final double SPIT_IN_TIME = 0.4;
    
    private CommandGroup commandGroup;
    
    /**
     * Creates the sequential commands.
     */
    public ClearCollector() {
        commandGroup = new CommandGroup();
        commandGroup.addSequential(new Collect(- Collect.COLLECT_POWER / 2, SPIT_OUT_TIME));
        commandGroup.addSequential(new Collect(Collect.COLLECT_POWER / 2, SPIT_IN_TIME));
    }
    
}
