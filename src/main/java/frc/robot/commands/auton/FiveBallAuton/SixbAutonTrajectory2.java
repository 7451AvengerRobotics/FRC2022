package frc.robot.commands.auton.FiveBallAuton;

import com.pathplanner.lib.PathPlanner;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import frc.robot.constants.DriveConstants;
import frc.robot.subsystems.DriveTrain;

public class SixbAutonTrajectory2 extends CommandBase{
    public Command SixBallAutonBlue2(DriveTrain drive) {
        var autoVoltageConstraint =
          new DifferentialDriveVoltageConstraint(
            new SimpleMotorFeedforward(
              DriveConstants.KS_VOLTS,
              DriveConstants.KV_VOLT_SECONDS_PER_METER,
              DriveConstants.KA_VOLT_SECONDS_SQUARED_PER_METER),
          DriveConstants.K_DRIVE_KINEMATICS, 
          10);
        
        TrajectoryConfig config =
          new TrajectoryConfig(
              DriveConstants.K_MAX_SPEED_METER_PER_SECOND, 
              DriveConstants.K_MAX_ACCELERATION_METERS_PER_SECOND_SQUARED)
            .setKinematics(DriveConstants.K_DRIVE_KINEMATICS)
            .addConstraint(autoVoltageConstraint);

        Trajectory path2 = PathPlanner.loadPath("Trajectory2-5bAuto", 3, 3);
    
        RamseteCommand ramseteCommand2 =
        new RamseteCommand(
            path2,
            drive::getPose,
            new RamseteController(DriveConstants.K_RAMSETE, DriveConstants.K_RAMSETE_ZETA),
            new SimpleMotorFeedforward(
                DriveConstants.KS_VOLTS,
                DriveConstants.KV_VOLT_SECONDS_PER_METER,
                DriveConstants.KA_VOLT_SECONDS_SQUARED_PER_METER),
            DriveConstants.K_DRIVE_KINEMATICS,
            drive::getWheelSpeeds,
            new PIDController(DriveConstants.KP_DRIVE_VELOCITY, 0, 0),
            new PIDController(DriveConstants.KP_DRIVE_VELOCITY, 0, 0),
            // RamseteCommand passes volts to the callback
            drive::tankDriveVolts,
            drive);
        
        drive.resetOdometry(path2.getInitialPose());
    
        return ramseteCommand2.andThen(() -> drive.tankDriveVolts(0, 0));
}
}
