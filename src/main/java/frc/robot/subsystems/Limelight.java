package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Limelight extends SubsystemBase {
  public final NetworkTable table;
  // how many degrees back is your limelight rotated from perfectly vertical?
  private static final double LIMELIGHT_MOUNT_ANGLE_DEGREES = 20.00;
  // distance from the center of the Limelight lens to the floor
  private static final double LIMELIGHT_LENS_HEIGHT_METERS = 0.669925;
  // distance from the target to the floor
  private static final double GOAL_HEIGHT_METERS = 2.6416;

  private static final double KP = -0.1; // Proportional control constant
  private static final double MIN_COMMAND = 0.0; // Minimum amount to slightly move

  private double steering_adjust;

  /**
   * Creates a new limelight class with the provided name.
   */
  public Limelight() {
    table = NetworkTableInstance.getDefault().getTable("limelight-b");

    NetworkTableInstance.getDefault().getTable("limelight-b").getEntry("pipeline").setNumber(0);
  }

  /**
   * uses the targetXOffset value to calculate the rotationAdjust value.
   */
  public double getRotationAdjust() {
    double headingError = table.getEntry("tx").getDouble(0);

    if (headingError > 1.0) {
      steering_adjust = -KP * headingError - MIN_COMMAND;
    } else if (headingError < 1.0) {
      steering_adjust = -KP * headingError + MIN_COMMAND;
    }

    return steering_adjust;
  }

  /**
   * Calculates the distance to the hub using the targetYOffset.
   */
  public double getDistance() {
    double targetOffsetAngle_Vertical = table.getEntry("ty").getDouble(0);

    double angle = LIMELIGHT_MOUNT_ANGLE_DEGREES + targetOffsetAngle_Vertical;
    double height = GOAL_HEIGHT_METERS - LIMELIGHT_LENS_HEIGHT_METERS;

    // tan = opp / hypot; divide opp to get adj.
    double distance = height / Math.tan(Math.toRadians(angle));

    return distance;
  }

  public void disableLights() {
    // Disables LEDs
    NetworkTableInstance.getDefault().getTable("limelight-b").getEntry("ledMode").setNumber(0);
  }

  public void enableLights() {
    // Enables LEDs
    NetworkTableInstance.getDefault().getTable("limelight-b").getEntry("ledMode").setNumber(3);
  }
}