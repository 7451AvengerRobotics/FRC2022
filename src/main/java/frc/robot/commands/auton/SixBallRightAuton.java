package frc.robot.commands.auton;

import com.pathplanner.lib.PathPlanner;

import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

import frc.robot.commands.FridayRamseteCommand;
import frc.robot.commands.IntakeAndShootCommandGroup;
import frc.robot.commands.PickUpBallCommandGroup;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;
//import frc.robot.commands.ShootBallCommandGroup;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Index;

import java.util.HashMap;
import java.util.Map;

public class SixBallRightAuton extends SequentialCommandGroup {
  public SixBallRightAuton(DriveTrain drive, Limelight limelight, Shooter shooter, Intake intake, Index index) {
    Trajectory path = PathPlanner.loadPath("6-Ball", 3, 5);
    // Reset odometry to the starting pose of the trajectory.
    drive.resetOdometry(path.getInitialPose());

    Map<Double, Command> commands = new HashMap<Double,Command>();
    commands.put(0.9555530630348358, new PickUpBallCommandGroup(intake, index));
    commands.put(2.0428413435675257, new IntakeAndShootCommandGroup(shooter, index, limelight, intake));
    commands.put(3.8162712857696506, new PickUpBallCommandGroup(intake, index));
    addCommands(
      new FridayRamseteCommand(path, drive, commands),
      deadline(new IntakeAndShootCommandGroup(shooter, index, limelight, intake), new RunCommand(() -> drive.tankDriveVolts(0, 0), drive)),
      new InstantCommand(() -> System.out.println("trajectory over")),
      new RunCommand(() -> drive.tankDriveVolts(0, 0), drive)
    );
  }
}
