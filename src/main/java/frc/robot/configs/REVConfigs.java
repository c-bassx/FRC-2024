package frc.robot.configs;

import java.util.HashMap;
import java.util.Map;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkFlex;
import com.revrobotics.CANSparkLowLevel;
import com.revrobotics.CANSparkLowLevel.MotorType;

import com.revrobotics.CANSparkMax;

import frc.robot.Constants.PIDConstants;

import frc.robot.Constants;

public final class REVConfigs {
    public static Map<String, CANSparkBase> motorControllers;

    public REVConfigs() {
        /* Create a hashmap to store motor controller objects */
        motorControllers = new HashMap<>();

        /* Instantiate each motor controller */
        motorControllers.put("shooterAngle", new CANSparkFlex(Constants.Shooter.angleMotorID, CANSparkLowLevel.MotorType.kBrushless));
        motorControllers.put("shooterLeft", new CANSparkFlex(Constants.Shooter.shooterMLeftID, CANSparkLowLevel.MotorType.kBrushless));
        motorControllers.put("shooterRight", new CANSparkFlex(Constants.Shooter.shooterMRightID,CANSparkLowLevel.MotorType.kBrushless));
        motorControllers.put("shooterNeck", new CANSparkMax(Constants.Shooter.neckMotorID, CANSparkLowLevel.MotorType.kBrushless));
        motorControllers.put("intakeAngle", new CANSparkFlex(Constants.Intake.angleMotorID, CANSparkLowLevel.MotorType.kBrushless));
        motorControllers.put("intakeMotor", new CANSparkFlex(Constants.Intake.intakeMotorID, CANSparkLowLevel.MotorType.kBrushless));
        motorControllers.put("intakeFeeder", new CANSparkMax(Constants.Intake.feederMotorID, CANSparkLowLevel.MotorType.kBrushless));
        motorControllers.put("deflectorMotor", new CANSparkMax(Constants.Deflector.motorID, MotorType.kBrushless));

        /* Set PID Constants for each motor controller */
        setPIDConstants();
    }

    /*
     * Internal method to set PID constants for each motor controller that requires them
     */
    private void setPIDConstants() {
        /* Set PID constants for shooter angle motor controller */
        motorControllers.get("shooterAngle").getPIDController().setP(PIDConstants.Shooter.AngleMotor.kP);
        motorControllers.get("shooterAngle").getPIDController().setI(PIDConstants.Shooter.AngleMotor.kI);
        motorControllers.get("shooterAngle").getPIDController().setD(PIDConstants.Shooter.AngleMotor.kD);

        /* Set PID constants for left shooter motor controller */
        motorControllers.get("shooterLeft").getPIDController().setP(PIDConstants.Shooter.LeftMotor.kP);
        motorControllers.get("shooterLeft").getPIDController().setI(PIDConstants.Shooter.LeftMotor.kI);
        motorControllers.get("shooterLeft").getPIDController().setD(PIDConstants.Shooter.LeftMotor.kD);  
        
        /* Set PID constants for right shooter motor controller */
        motorControllers.get("shooterRight").getPIDController().setP(PIDConstants.Shooter.RightMotor.kP);
        motorControllers.get("shooterRight").getPIDController().setI(PIDConstants.Shooter.RightMotor.kI);
        motorControllers.get("shooterRight").getPIDController().setD(PIDConstants.Shooter.RightMotor.kD);  

        /* Set PID constants for intake angle motor controller */
        motorControllers.get("intakeAngle").getPIDController().setP(PIDConstants.Intake.AngleMotor.kP);
        motorControllers.get("intakeAngle").getPIDController().setI(PIDConstants.Intake.AngleMotor.kI);
        motorControllers.get("intakeAngle").getPIDController().setD(PIDConstants.Intake.AngleMotor.kD);

        /* Set PID constants for intake motor controller */
        motorControllers.get("intakeMotor").getPIDController().setP(PIDConstants.Intake.IntakeMotor.kP);
        motorControllers.get("intakeMotor").getPIDController().setI(PIDConstants.Intake.IntakeMotor.kI);
        motorControllers.get("intakeMotor").getPIDController().setD(PIDConstants.Intake.IntakeMotor.kD);

        /* Set PID constants for deflector motor controller */
        motorControllers.get("deflectorMotor").getPIDController().setP(PIDConstants.Deflector.DeflectorMotor.kP);
        motorControllers.get("deflectorMotor").getPIDController().setI(PIDConstants.Deflector.DeflectorMotor.kI);
        motorControllers.get("deflectorMotor").getPIDController().setD(PIDConstants.Deflector.DeflectorMotor.kD);
    }

    /**
     * Retrieves a motor controller from the map based on the specified key
     * 
     * @param key The key corresponding to the motor controller
     * @return The motor controller object, or null 
     *         if there's no mapping for the key
     */
    public static CANSparkBase getMotorController(String key) {
        return motorControllers.get(key);
    }
}