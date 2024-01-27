package frc.robot.subsystems;

import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.hardware.Pigeon2;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.DeflectorConstants.DeflectorState;

public class Kinesthetics extends SubsystemBase {
    // Subsystem Information
    private Swerve s_Swerve;
    private Deflector s_Deflector;

    // Sensor Information
    private Pigeon2 gyro;
    private DigitalInput feederBeamBreak;
    private DigitalInput shooterBeamBreak;

    // Data Fields
    private DriverStation.Alliance alliance;
    private SwerveDrivePoseEstimator poseEstimator;

    public Kinesthetics(Swerve s, Deflector d) {
        s_Swerve = s;
        s_Swerve.setKinesthetics(this);
        s_Deflector = d;

        gyro = new Pigeon2(Constants.Swerve.pigeonID);
        gyro.getConfigurator().apply(new Pigeon2Configuration());
        gyro.setYaw(0);

        feederBeamBreak = new DigitalInput(Constants.Intake.beamBreakID);
        shooterBeamBreak = new DigitalInput(Constants.Shooter.beamBreakID);

        alliance = DriverStation.getAlliance().orElse(null);
        poseEstimator = new SwerveDrivePoseEstimator(Constants.Swerve.swerveKinematics, getGyroYaw(), s_Swerve.getModulePositions(), new Pose2d());
    }

    @Override
    public void periodic() {
        poseEstimator.update(getHeading(), s_Swerve.getModulePositions());
        poseEstimator.addVisionMeasurement(Vision.getVisionPose().toPose2d(), Vision.latestLatency());
        super.periodic();
    }

    private Rotation2d getGyroYaw() {
        return Rotation2d.fromDegrees(gyro.getYaw().getValue());
    }

    // Public Getters & Setters
    public DriverStation.Alliance getAlliance() {
        return alliance;
    }

    public boolean feederHasNote() {
        return feederBeamBreak.get();
    }

    public boolean shooterHasNote() {
        return shooterBeamBreak.get();
    }

    public Pose2d getPose() {
        return poseEstimator.getEstimatedPosition();
    }

    public void setPose(Pose2d pose) {
        poseEstimator.resetPosition(getGyroYaw(), s_Swerve.getModulePositions(), pose);
    }

    public Rotation2d getHeading(){
        return getPose().getRotation();
    }

    public void setHeading(Rotation2d heading){
        poseEstimator.resetPosition(getGyroYaw(), s_Swerve.getModulePositions(), new Pose2d(getPose().getTranslation(), heading));
    }

    public void zeroHeading(){
        poseEstimator.resetPosition(getGyroYaw(), s_Swerve.getModulePositions(), new Pose2d(getPose().getTranslation(), new Rotation2d()));
    }

    public void setDeflectorState(DeflectorState d){
        s_Deflector.setGoalPos(d.setPoint);
    }

    public void stopDeflectorMotor(){
        s_Deflector.stopMotor();
    }

    public double getDeflectorPos(){
        return s_Deflector.getPos();
    }
}
