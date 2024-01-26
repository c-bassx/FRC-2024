package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.DeflectorConstants.DeflectorState;

public class Deflector extends SubsystemBase{
    private CANSparkMax motor;
    private DeflectorState deflectorState;

    public Deflector(){
        motor = new CANSparkMax(Constants.DeflectorConstants.deflectorMotorID, MotorType.kBrushless);
        deflectorState = DeflectorState.OFF;
    }

    @Override
    public void periodic(){
        //uses percentage for now, but should be changed to encoder pos later.
        if(deflectorState != DeflectorState.OFF){
            motor.getPIDController().setReference(deflectorState.setPoint, CANSparkMax.ControlType.kPosition);
        }
        else{
            motor.stopMotor();
        }
    }

    public void setDeflectorState(DeflectorState state){
        deflectorState = state;
    }
}

