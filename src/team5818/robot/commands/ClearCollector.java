package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * Adds collect commands in sequance so that they can clear the collector if the ball is jammed.
 */
public class ClearCollector extends CommandGroup {

    private static final double maxSpitTime = .25;
    private CommandGroup commandGroup;
    
    /**
     * Creates the sequential commands.
     */
    public ClearCollector() {
        commandGroup = new CommandGroup();
        commandGroup.addSequential(new Collect(- Collect.COLLECT_POWER / 2, maxSpitTime));
        commandGroup.addSequential(new Collect(Collect.COLLECT_POWER / 2, maxSpitTime));
    }
    
}
