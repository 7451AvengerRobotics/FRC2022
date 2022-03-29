package frc.robot.commands.auton;

import com.pathplanner.lib.PathPlanner;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.commands.FridayRamseteCommand;
import frc.robot.commands.IntakeAndShootCommandGroup;
import frc.robot.commands.IntakeExtendCommand;
import frc.robot.commands.PickUpBallCommandGroup;
import frc.robot.commands.ShootBallCommandGroup;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Index;

import java.util.HashMap;
import java.util.Map;

public class ThreeBallRightAuton extends SequentialCommandGroup {
  public ThreeBallRightAuton(DriveTrain drive, Limelight limelight, Shooter shooter, Intake intake, Index index) {
    Trajectory path = PathPlanner.loadPath("3-Ball", 3, 5);
    // Reset odometry to the starting pose of the trajectory.
    drive.resetOdometry(path.getInitialPose());
//Changed path will have to redo timing
    Map<Double, Command> commands = new HashMap<Double,Command>();
    commands.put(1.536352434800916, new PickUpBallCommandGroup(intake, index));

    addCommands(
      new FridayRamseteCommand(path, drive, commands),
      deadline(new IntakeAndShootCommandGroup(shooter, index, limelight, intake), new RunCommand(() -> drive.tankDriveVolts(0, 0), drive)),
      new InstantCommand(() -> System.out.println("trajectory over")),
      new RunCommand(() -> drive.tankDriveVolts(0, 0), drive),
      )
    );
  }
}
