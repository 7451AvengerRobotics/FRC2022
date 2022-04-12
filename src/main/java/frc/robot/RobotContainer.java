// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.PathPlanner;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.RamseteController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PS4Controller;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.auton.FiveBallAuto;
import frc.robot.commands.auton.TwoBallTimeBased;
import frc.robot.commands.driveTypes.LucaDrive; 
import frc.robot.commands.ComplexCommands.AllIndexCommand;
import frc.robot.commands.ComplexCommands.DataTestingCommandGroup;
import frc.robot.commands.ComplexCommands.PickUpBallCommandGroup;
import frc.robot.commands.ComplexCommands.ShootBallCommandGroup;
import frc.robot.commands.SimpleCommands.IntakeCommand;
import frc.robot.commands.SimpleCommands.IntakeExtendCommand;
import frc.robot.commands.SimpleCommands.IntakeRetractCommand;
import frc.robot.commands.SimpleCommands.IntakeToIndexCommand;
import frc.robot.constants.ButtonConstants;
import frc.robot.constants.DriveConstants;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Index;
import frc.robot.subsystems.IndexToShooter;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.IntakeToIndex;
import frc.robot.subsystems.Lift;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.Shooter;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  private static final double intakePower = 0;
  // The robot's subsystems and commands are defined here...
  private final DriveTrain drive;
  private final IntakeToIndex intakeToIndex;
  private final Index index;
  private final IndexToShooter indexToShooter;
  private final Shooter shooter;
  private final PS4Controller controller;
  private final Limelight limelight;
  private final Intake intake;
  private final Joystick buttonPanel;
  private final Lift lift;

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    System.out.println("Hello, Driver");

    controller = new PS4Controller(ButtonConstants.CONTROLLER_PORT);
    buttonPanel = new Joystick(ButtonConstants.BUTTON_PANEL_PORT);

    drive = new DriveTrain();
    shooter = new Shooter();
    limelight = new Limelight();
    index = new Index();
    intake = new Intake();
    intakeToIndex = new IntakeToIndex();
    indexToShooter = new IndexToShooter();
    lift = new Lift();
    drive.gyroCalibrate();

    configureDriveTrain();
    configureButtonBindings();
  }

  private void configureDriveTrain() {
    // sets the command to drive the robot.
    // will run whenever the drivetrain is not being used.
    drive.setDefaultCommand(
        // pass in a reference to a method
        new LucaDrive( 
            drive,
            controller::getL2Axis,
            controller::getR2Axis,
            controller::getLeftX,
            controller::getCircleButton
        ));
        // new DefaultDrive(drive, controller::getLeftY, controller::getRightY, controller::getR1Button));
  }

  private void configureButtonBindings() {

    //LIFT 
    JoystickButton raiseLift = new JoystickButton(buttonPanel,ButtonConstants.LIFT_UP);
    raiseLift.whenHeld(new DataTestingCommandGroup(shooter, index, indexToShooter, limelight, 0.5, 0.5, 3000));
    JoystickButton lowerLift = new JoystickButton(buttonPanel,ButtonConstants.LIFT_DOWN);
    lowerLift.whenHeld(new DataTestingCommandGroup(shooter, index, indexToShooter, limelight, 0.5, 0.5, 3280));
    //lowerLift.whenHeld(new LiftCommand(lift, 1));
    //raiseLift.whenHeld(new LiftCommand(lift, -1));
    //LIFT 


    //ARMS
    JoystickButton liftForward = new JoystickButton(buttonPanel,ButtonConstants.LIFT_FORWARD);
    liftForward.whenHeld(new DataTestingCommandGroup(shooter, index, indexToShooter, limelight, 0.5, 0.5, 5280));
    JoystickButton liftBackward = new JoystickButton(buttonPanel,ButtonConstants.LIFT_BACK);
    liftBackward.whenHeld(new DataTestingCommandGroup(shooter, index, indexToShooter, limelight, 0.5, 0.5, 5565));
    //liftBackward.whenHeld(new LiftHorizontalCommand(lift, -1));
    //liftForward.whenHeld(new LiftHorizontalCommand(lift, 1));
    //ARMS

    
    //INTAKE EXTRACT/RETRACT 
    JoystickButton extendIntake = new JoystickButton(buttonPanel,ButtonConstants.INTAKE_EXTEND);
    extendIntake.whenHeld(new DataTestingCommandGroup(shooter, index, indexToShooter, limelight, 0.5, 0.5, 3570));
    JoystickButton retractIntake = new JoystickButton(buttonPanel,ButtonConstants.INTAKE_RETRACT);
    retractIntake.whenHeld(new DataTestingCommandGroup(shooter, index, indexToShooter, limelight, 0.5, 0.5, 3855));
    //retractIntake.whenPressed(intake::retract, intake);
    //extendIntake.whenPressed(intake::extend, intake); 
    //INTAKE EXTRACT/RETRACT 


    //INTAKE
    JoystickButton runIntakeIn = new JoystickButton(buttonPanel, ButtonConstants.INTAKE_IN);
    runIntakeIn.whenHeld(new DataTestingCommandGroup(shooter, index, indexToShooter, limelight, 0.5, 0.5, 4140));
    JoystickButton runIntakeOut = new JoystickButton(buttonPanel, ButtonConstants.INTAKE_OUT);
    runIntakeOut.whenHeld(new DataTestingCommandGroup(shooter, index, indexToShooter, limelight, 0.5, 0.5, 4425));
    //runIntakeOut.whenHeld(new IntakeToIndexCommandGroup(intake, intakeToIndex, index));
    //runIntakeIn.whenHeld(new IntakeToIndexCommandGroup(intake, intakeToIndex, index));
    //INTAKE


    //INDEX
    JoystickButton indexUp = new JoystickButton(buttonPanel, ButtonConstants.INDEX_UP);
    indexUp.whenHeld(new DataTestingCommandGroup(shooter, index, indexToShooter, limelight, 0.5, 0.5, 4710));
    JoystickButton indexOut = new JoystickButton(buttonPanel, ButtonConstants.INDEX_OUT);
    indexOut.whenHeld(new DataTestingCommandGroup(shooter, index, indexToShooter, limelight, 0.5, 0.5, 4995));
    //indexOut.whenHeld(new AllIndexCommand(intakeToIndex, index, -0.5, -0.5));
    //indexUp.whenHeld(new AllIndexCommand(intakeToIndex, index, 0.5, 0.5));
    //INDEX


    // Shoot button
    JoystickButton shootButton = new JoystickButton(buttonPanel, ButtonConstants.FLYWHEEL_ON);
    // null values subject to change
    shootButton.whenHeld(new DataTestingCommandGroup(shooter, index, indexToShooter, limelight, 0.5, 0.5, 5850));

    JoystickButton shootWrongBall = new JoystickButton(buttonPanel, ButtonConstants.SHOOT_WRONG_BALL);
    shootWrongBall.whenHeld(new DataTestingCommandGroup(shooter, index, indexToShooter, limelight, 0.5, .5, 6380));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return new FiveBallAuto(drive, limelight, shooter, intake, index, intakeToIndex, indexToShooter, 0, 0, 0, 0, 0);

}
}
