package frc.robot.commands.groups;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.Constants;
import frc.robot.Constants.DeflectorConstants.DeflectorState;
import frc.robot.commands.AutoSwerve;
import frc.robot.commands.ShooterManualAim;
import frc.robot.commands.ShooterManualSpin;
import frc.robot.subsystems.Deflector;
import frc.robot.subsystems.Kinesthetics;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Swerve;

public class AmpAuto extends ParallelCommandGroup {
    public AmpAuto(Kinesthetics k, Swerve sw, Shooter sh, Deflector d) {
        var desiredPose = Constants.Field.amps.get(k.getAlliance());
        addCommands(
            new AutoSwerve(k, sw, desiredPose),
            new ParallelCommandGroup(
                new ShooterManualAim(sh, () -> Constants.CommandConstants.ampShooterAngle),
                new WaitUntilCommand(() ->
                    k.getPose().minus(desiredPose).getTranslation().getNorm() < Constants.CommandConstants.ampAutoDistanceToStartSpinning
                )
            ).andThen(new InstantCommand(() -> k.setDeflectorState(DeflectorState.UP), d))
            .andThen(new ShooterManualSpin(sh, () -> Constants.CommandConstants.ampShooterSpin))
        );
    }
}
