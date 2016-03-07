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
    public static final int MIN_VEL = 10;
    double velX;
    double velY;

    @Override
    public Vector2d compute(Vector2d leftAndRight) {
  
        ArcadeDriveCalculator arcadeCalc = ArcadeDriveCalculator.INSTANCE;
        Vector2d arcadePowers = arcadeCalc.compute(leftAndRight);
        velX = arcadePowers.getX() * 50;
        if(Math.abs(velX) <= MIN_VEL){
            velX = 0.0;
        }
        velY = arcadePowers.getY()*50;
        if(Math.abs(velY) <= MIN_VEL){
            velY = 0.0;
        }
        
        Vector2d finalPowers = new Vector2d(velX, velY);
        
        return finalPowers;
    }

}
