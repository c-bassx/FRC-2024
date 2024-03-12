package frc.robot.subsystems;

import com.revrobotics.CANSparkBase;

// import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.configs.REVConfigs;

public class Deflector extends SubsystemBase{
    private CANSparkBase angleMotorController;

    public Deflector() {
        angleMotorController = REVConfigs.getMotorController("deflectorMotor");
    }

    // /** @return radians */
    // private double getPitch() {
    //     return Units.rotationsToRadians(angleMotorControllerL.getEncoder().getPosition());
    // }
    
    /** @param goalPitch radians */
    private void setGoalPitch(double goalPitch) {
        angleMotorController.getPIDController().setReference(Units.radiansToRotations(goalPitch), CANSparkBase.ControlType.kPosition);
    }

    public class Raise extends Command {
        public Raise() {
            addRequirements(Deflector.this);
        }

        @Override
        public void initialize(){
            setGoalPitch(Constants.Deflector.maximumPitch);
            super.initialize();
        }

        @Override
        public boolean isFinished() {
            return false;
            // return MathUtil.isNear(Constants.Deflector.maximumPitch, getPitch(), Constants.Deflector.pitchTolerance);
        }

        @Override
        public void end(boolean interrupted){
            setGoalPitch(0);
            super.end(interrupted);
        }
    }
}