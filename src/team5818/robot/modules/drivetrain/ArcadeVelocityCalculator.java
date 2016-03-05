package team5818.robot.modules.drivetrain;

import team5818.robot.util.Vector2d;

/**
 * @author Petey
 *
 */
public enum ArcadeVelocityCalculator implements DriveCalculator{
    /**
     * the only instance of this calculator
     */
    INSTANCE;

    @Override
    public Vector2d compute(Vector2d leftAndRight) {
        ArcadeDriveCalculator arcadeCalc = ArcadeDriveCalculator.INSTANCE;
        Vector2d arcadePowers = arcadeCalc.compute(leftAndRight);
        Vector2d finalPowers = new Vector2d(arcadePowers.getX() * 50, arcadePowers.getY()*50);
        
        return finalPowers;
    }

}
