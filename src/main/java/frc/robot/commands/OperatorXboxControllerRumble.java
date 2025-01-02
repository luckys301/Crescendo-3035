package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

public class OperatorXboxControllerRumble extends Command {
  private Timer rumbleTimer = new Timer();
  private double rumbleTime;
  private CommandXboxController controller;

  public OperatorXboxControllerRumble(CommandXboxController controller,  double time) {
    this.controller = controller;
    rumbleTime = time;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  @Override
  public void execute() {
    rumbleTimer.start();
    controller.getHID().setRumble(RumbleType.kBothRumble, 1);
  }

  @Override
  public boolean isFinished() {
    return rumbleTimer.hasElapsed(rumbleTime);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    controller.getHID().setRumble(RumbleType.kBothRumble, 0);
    rumbleTimer.stop();
    rumbleTimer.reset();
  }
}