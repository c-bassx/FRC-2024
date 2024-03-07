package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.lib.math.Conversions;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Vision extends SubsystemBase {
    private static final NetworkTable limelightTable = NetworkTableInstance.getDefault().getTable("limelight");
    private static final NetworkTable rpiTable = NetworkTableInstance.getDefault().getTable("raspberrypi");

    private final SendableChooser<InstantCommand> pipelineSelector = new SendableChooser<>();
    private Kinesthetics kinesthetics;

    public Vision(Kinesthetics k) {
        this.kinesthetics = k;

        pipelineSelector.setDefaultOption("Limelight Pipeline 0", new InstantCommand(() -> Vision.switchToPipeline(0)));
        pipelineSelector.addOption("Limelight Pipeline 1", new InstantCommand(() -> Vision.switchToPipeline(1)));

        // Add limelight pipeline selector
        Shuffleboard.getTab("Vision")
            .add("Vision", pipelineSelector)
            .withWidget(BuiltInWidgets.kComboBoxChooser);
    }

    @Override
    public void periodic() {
        // Compare odometry and limelight pose
        SmartDashboard.putNumber("Pose distance difference",
            kinesthetics.getPose().getTranslation().getDistance(Vision.getBotPose().getTranslation().toTranslation2d())
        );
    }

    public static Pose3d getBotPose() {
        if (!limelightTable.getEntry("botpose").exists()) return null;
        double[] ntdata = limelightTable.getEntry("botpose").getDoubleArray(new double[6]);
        double xPos = ntdata[0] + Constants.Environment.fieldWidth/2;
        double yPos = ntdata[1] + Constants.Environment.fieldHeight/2;
        return new Pose3d(xPos, yPos, ntdata[2], new Rotation3d(ntdata[3], ntdata[4], ntdata[5]));
    } // limelight translation is y 12.947", z 8.03", pitch 64 deg

    public static double getLimelightPing() {
        return limelightTable.getEntry("cl").getDouble(-1);
    }

    public static Translation3d getNoteTranslation() {
        if (!limelightTable.getEntry("notetrans").exists()) return null;
        double[] ntdata = rpiTable.getEntry("notetrans").getDoubleArray(new double[3]);
        return Constants.Intake.luxonisTranslation.getTranslation().plus(new Translation3d(
            Conversions.millimetersToMeters(ntdata[0]),
            Conversions.millimetersToMeters(ntdata[1]),
            Conversions.millimetersToMeters(ntdata[2])
        ).rotateBy(Constants.Intake.luxonisTranslation.getRotation()));
    }

    public static double getRpiPing() {
        return rpiTable.getEntry("cl").getDouble(-1);
    }

    public static void switchToPipeline(int pipeline) {
        limelightTable.getEntry("pipeline").setNumber(pipeline);
    }

    public static double getPipeline() {
        return limelightTable.getEntry("pipeline").getDouble(-1);
    }
}