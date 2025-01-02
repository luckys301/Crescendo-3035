package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.drive.SwerveDrive;

public class ResetFieldCentric extends Command {

  private SwerveDrive drive;
  private double resetYaw;

  public ResetFieldCentric(SwerveDrive drive, double resetYaw) {
    this.drive = drive;
    this.resetYaw = resetYaw;
    addRequirements(drive);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    drive.setYawCommand(resetYaw);
    // String report = "Reset Field Centric";
    // DriverStation.reportWarning(report, false);
  }

  @Override
  public boolean runsWhenDisabled() {
    return true; // allow field orientation to be set before start of match
  }
}
