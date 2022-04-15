package frc.robot.commands.auton;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.math.util.Units;
import frc.robot.constants.DriveConstants;
import java.util.List;

/**
 * Store trajectories for autonomous. Edit points here.
 */
public class Trajectories {
  private static final DifferentialDriveVoltageConstraint autoVoltageConstraint =
      new DifferentialDriveVoltageConstraint(
          new SimpleMotorFeedforward(DriveConstants.KS_VOLTS,
              DriveConstants.KV_VOLT_SECONDS_PER_METER,
              DriveConstants.KA_VOLT_SECONDS_SQUARED_PER_METER),
          DriveConstants.K_DRIVE_KINEMATICS, DriveConstants.MAX_DRIVE_VOLTAGE);

  private static final TrajectoryConfig forwardTrajConfig =
      new TrajectoryConfig(DriveConstants.KP_DRIVE_VELOCITY,
          DriveConstants.K_MAX_ACCELERATION_METERS_PER_SECOND_SQUARED)
              .setKinematics(DriveConstants.K_DRIVE_KINEMATICS).addConstraint(autoVoltageConstraint)
              .setReversed(false);

  private static final TrajectoryConfig backwardsTrajConfig =
      new TrajectoryConfig(DriveConstants.K_MAX_SPEED_METER_PER_SECOND,
          DriveConstants.K_MAX_ACCELERATION_METERS_PER_SECOND_SQUARED)
              .setKinematics(DriveConstants.K_DRIVE_KINEMATICS).addConstraint(autoVoltageConstraint)
              .setReversed(true);

  public static Trajectory trajectorySimple = TrajectoryGenerator.generateTrajectory(
      new Pose2d(Units.inchesToMeters(0), Units.inchesToMeters(0), new Rotation2d(0)),
      List.of(new Translation2d(Units.inchesToMeters(30), Units.inchesToMeters(20)),
          new Translation2d(Units.inchesToMeters(60), Units.inchesToMeters(-20))),
      new Pose2d(Units.inchesToMeters(90), Units.inchesToMeters(0), new Rotation2d(0)),
      forwardTrajConfig);

  public static Trajectory trajectoryLessSimple =
      TrajectoryGenerator.generateTrajectory(new Pose2d(0, 0, new Rotation2d(0)),
          List.of(new Translation2d(Units.inchesToMeters(6), Units.inchesToMeters(-14))),
          new Pose2d(Units.inchesToMeters(50), Units.inchesToMeters(-15), new Rotation2d(0)),
          forwardTrajConfig);

  public static Trajectory driveBack30In =
      TrajectoryGenerator.generateTrajectory(new Pose2d(0, 0, new Rotation2d(0)), List.of(),
          new Pose2d(Units.inchesToMeters(-30), 0, new Rotation2d(0)), backwardsTrajConfig);
}
