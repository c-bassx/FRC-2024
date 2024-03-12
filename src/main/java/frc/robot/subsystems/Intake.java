package frc.robot.subsystems;

import com.revrobotics.CANSparkBase;
import com.revrobotics.SparkAbsoluteEncoder;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.SpinState;
import frc.robot.configs.REVConfigs;

public class Intake extends SubsystemBase {
    private CANSparkBase angleMotorController, intakeMotorController, feederMotorController;

    public Intake() {
        angleMotorController = REVConfigs.getMotorController("intakeAngle");
        intakeMotorController = REVConfigs.getMotorController("intakeMotor");
        feederMotorController = REVConfigs.getMotorController("intakeFeeder");

        intakeMotorController.setInverted(true);
    }
    
    /** @return radians */
    private double getPitch() {
        return Units.rotationsToRadians(angleMotorController.getAbsoluteEncoder(SparkAbsoluteEncoder.Type.kDutyCycle).getPosition());
    }
    
    /** @param goalPitch radians */
    private void setGoalPitch(double goalPitch) {
        angleMotorController.getPIDController().setReference(Units.radiansToRotations(goalPitch), CANSparkBase.ControlType.kSmartMotion);
    }

    /** @return radians / second */
    private double getSpin() {
        return Units.rotationsPerMinuteToRadiansPerSecond(intakeMotorController.getEncoder().getVelocity());
    }

    private void setSpin(SpinState ss) {
        intakeMotorController.set(ss.multiplier * Constants.Intake.intakeSpin);
        feederMotorController.set(ss.multiplier * Constants.Intake.feederSpin);
    }

    public class ChangeState extends Command {
        private final Constants.Intake.IntakeState desiredState;

        public ChangeState(Constants.Intake.IntakeState ds) {
            desiredState = ds;
            addRequirements(Intake.this);
        }

        @Override
        public void initialize() {
            setGoalPitch(desiredState.pitch);
            setSpin(desiredState.spin);
            super.initialize();
        }

        @Override
        public boolean isFinished() {
            return MathUtil.isNear(desiredState.pitch, getPitch(), Constants.Intake.pitchTolerance) &&
                MathUtil.isNear(desiredState.spin.multiplier * Constants.Intake.intakeSpin, getSpin(), Constants.Intake.spinTolerance);
        }

        @Override
        public void end(boolean interrupted) {
            if (interrupted) {
                setGoalPitch(Constants.Intake.IntakeState.STOW.pitch);
                setSpin(Constants.Intake.IntakeState.STOW.spin);
            }
            super.end(interrupted);
        }
    }
}