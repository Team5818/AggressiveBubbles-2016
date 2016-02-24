package team5818.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import team5818.robot.RobotCommon;
import team5818.robot.modules.ComputerVision;
import team5818.robot.RobotCommon;

public class LEDToggle extends Command {

    public ComputerVision CamLed;
    public boolean LEDState;

    public LEDToggle(boolean State) {
        CamLed = RobotCommon.runningRobot.vision.See;
        LEDState = State;
    }

    @Override
    protected void initialize() {
        CamLed.LEDToggle(LEDState);
    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {
        return true;
    }

    @Override
    protected void end() {
    }

    @Override
    protected void interrupted() {
    }

}