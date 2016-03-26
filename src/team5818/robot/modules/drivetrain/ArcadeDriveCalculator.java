package team5818.robot.modules.drivetrain;

import team5818.robot.util.Vector2d;

/**
 * A {@link DriveCalculator} that computes values for arcade drive.
 */
public enum ArcadeDriveCalculator implements DriveCalculator {
    /**
     * The only instance of this calculator.
     */
    INSTANCE;
    
    
    public Vector2d computeDefault(Vector2d in) {
        double rotateValue = -in.getX();
        double moveValue = in.getY();
        double leftMotorSpeed;
        double rightMotorSpeed;
        if (moveValue > 0.0) {
            if (rotateValue > 0.0) {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = Math.max(moveValue, rotateValue);
            } else {
                leftMotorSpeed = Math.max(moveValue, -rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            }
        } else {
            if (rotateValue > 0.0) {
                leftMotorSpeed = -Math.max(-moveValue, rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            } else {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
            }
        }
        return new Vector2d(leftMotorSpeed, rightMotorSpeed);
    }
    
    
    public static final double TURNMULT = 0.5;
    public static final double TURNPOWER = 2;
    
    public static final double FORWARDMULT = 1;
    public static final double FORWARDPOWER = 1;
    
    public Vector2d computeTurnsDifferent(Vector2d in) {
        double rotateValue = Math.signum(-in.getX())*Math.pow(Math.abs(in.getX()),TURNPOWER)*TURNMULT; // Less sensitive turning 
        double moveValue = Math.signum(-in.getY())*Math.pow(Math.abs(in.getY()),FORWARDPOWER)*FORWARDMULT; // Less sensitive turning 
        
        double leftMotorSpeed;
        double rightMotorSpeed;
        if (moveValue > 0.0) {
            if (rotateValue > 0.0) {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = Math.max(moveValue, rotateValue);
            } else {
                leftMotorSpeed = Math.max(moveValue, -rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            }
        } else {
            if (rotateValue > 0.0) {
                leftMotorSpeed = -Math.max(-moveValue, rotateValue);
                rightMotorSpeed = moveValue + rotateValue;
            } else {
                leftMotorSpeed = moveValue - rotateValue;
                rightMotorSpeed = -Math.max(-moveValue, -rotateValue);
            }
        }
        return new Vector2d(leftMotorSpeed, rightMotorSpeed);
    }
    
    public static final int JOYSTICK_MODE = 0; // CHANGE THIS TO CHANGE DRIVE MODE

    @Override
    public Vector2d compute(Vector2d leftAndRight) {
        if(JOYSTICK_MODE != 1)
        {
            return this.computeDefault(leftAndRight);
        }else
        {
            return this.computeTurnsDifferent(leftAndRight);
        }
    }

}
