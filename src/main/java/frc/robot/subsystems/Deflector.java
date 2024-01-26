package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Deflector extends SubsystemBase{
    private CANSparkMax motor;

    public Deflector(){
        motor = new CANSparkMax(Constants.DeflectorConstants.deflectorMotorID, MotorType.kBrushless);
    }

    public void setGoalPos(int setPoint){
        motor.getPIDController().setReference(setPoint, CANSparkMax.ControlType.kSmartMotion);
    }

    public void stopMotor(){
        motor.stopMotor();
    }
}

