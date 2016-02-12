package org.usfirst.frc.team5818.robot.modules;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.usfirst.frc.team5818.robot.RobotConstants;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shooter implements Module {
    
    /**
     * The Talon motor controller class that controls
     * the upper fly wheel in the shooter mechanism.
     */
    private CANTalon talonFlyUpper = new CANTalon(RobotConstants.TALON_FLYWHEEL_UPPER);
    
    /**
     * The Talon motor controller class that controls
     * the upper fly wheel in the shooter mechanism.
     */
    private CANTalon talonFlyLower = new CANTalon(RobotConstants.TALON_FLYWHEEL_LOWER);
    
    NumberFormat formatter = new DecimalFormat("#0.00");
    
    private final FlyWheel flyWheel;
    
    public Shooter()
    {
        flyWheel = new FlyWheel(talonFlyUpper, talonFlyLower);
    }
    
    @Override
    public void initModule() {
        
        flyWheel.initModule();
    }
    
    @Override
    public void teleopPeriodicModule() {
        
        double flyLP = SmartDashboard.getNumber("DB/Slider 0");
        double flyUP = SmartDashboard.getNumber("DB/Slider 1");
        
        boolean onBut = SmartDashboard.getBoolean("DB/Button 0");
        
        flyWheel.setFlyUpperPower(flyUP);
        flyWheel.setFlyLowerPower(flyLP);
        
        SmartDashboard.putString("DB/String 0", "LowerRPS = " + formatter.format(flyWheel.getFlyLowerRPS()));
        SmartDashboard.putString("DB/String 5", "UpperRPS = " + formatter.format(flyWheel.getFlyUpperRPS()));
        SmartDashboard.putString("DB/String 1", "LowerP = " + formatter.format(flyWheel.getFlyLowerPower()));
        SmartDashboard.putString("DB/String 6", "UpperP = " + formatter.format(flyWheel.getFlyUpperPower()));
        
        flyWheel.teleopPeriodicModule();
    }
    @Override
    public void endModule() {
        
        flyWheel.endModule();
    }
    
}
