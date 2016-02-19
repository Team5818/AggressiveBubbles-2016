package org.usfirst.frc.team5818.robot;

import edu.wpi.first.wpilibj.RobotBase;
import team5818.robot.RobotCommon;

/**
 * Robot for 5818 mode.
 */
public class Robot extends RobotBase {

    private final RobotCommon robot = new RobotCommon();

    @Override
    public int hashCode() {
        return robot.hashCode();
    }

    @Override
    public void startCompetition() {
        robot.startCompetition();
    }

    @Override
    public void free() {
        robot.free();
    }

    @Override
    public boolean isDisabled() {
        return robot.isDisabled();
    }

    @Override
    public boolean equals(Object obj) {
        return robot.equals(obj);
    }

    @Override
    public boolean isEnabled() {
        return robot.isEnabled();
    }

    @Override
    public boolean isAutonomous() {
        return robot.isAutonomous();
    }

    @Override
    public boolean isTest() {
        return robot.isTest();
    }

    @Override
    public boolean isOperatorControl() {
        return robot.isOperatorControl();
    }

    @Override
    public boolean isNewDataAvailable() {
        return robot.isNewDataAvailable();
    }

    @Override
    public String toString() {
        return robot.toString();
    }

}
