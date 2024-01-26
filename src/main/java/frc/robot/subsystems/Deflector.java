package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.SparkPIDController;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.DeflectorConstants.DeflectorState;

public class Deflector extends SubsystemBase{
    private CANSparkMax motor;
    private SparkPIDController pidController = motor.getPIDController();
    private DeflectorState deflectorState;
    private double kP, kI, kD, kIz, kFF, kMinOutput, kMaxOutput;

    public Deflector(){
        motor = new CANSparkMax(Constants.DeflectorConstants.deflectorMotorID, MotorType.kBrushless);
        deflectorState = DeflectorState.OFF;
    }

    @Override
    public void periodic(){
        //uses percentage for now, but should be changed to encoder pos later.
        if(deflectorState != DeflectorState.OFF){
            kP = pidController.getP();
            kI = pidController.getI();
            kD = pidController.getD();
            kIz = pidController.getIZone();
            kFF = pidController.getFF();
            kMaxOutput = 1;
            kMinOutput = -1;

            pidController.setP(kP);
            pidController.setI(kI);
            pidController.setD(kD);
            pidController.setIZone(kIz);
            pidController.setFF(kFF);
            pidController.setOutputRange(kMinOutput, kMaxOutput);

            pidController.setReference(deflectorState.setPoint, CANSparkMax.ControlType.kPosition);
        }
        else{
            motor.set(0);
        }
    }

    public void setDeflectorState(DeflectorState state){
        deflectorState = state;
    }
}

