package frc.robot.subsystems.claw;

import com.revrobotics.CANSparkLowLevel.MotorType;
import edu.wpi.first.math.controller.ElevatorFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.DisabledCommand;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

public class ClawElevator extends SubsystemBase {
    public static class Constants {
        public static final double GEAR_RATIO = 1 / 1;
        public static final double GEAR_DIAMETER_INCHES = 1.88;
        public static final double COUNTS_PER_PULSE = 1; // 2048 bc rev through bore
        public static final double ROT_TO_INCHES = (COUNTS_PER_PULSE * GEAR_RATIO) / (GEAR_DIAMETER_INCHES * Math.PI);
        public static final double MIN_POSITION = 0;
        public static final double MAX_POSITION = 16.2;
    }
    private final SafeCanSparkMax leftMotor;
    private final SafeCanSparkMax rightMotor;
    
    private final PIDController controller = new PIDController(2.4, 0, 0);
    private final ElevatorFeedforward feedforward = 
        new ElevatorFeedforward(0.1, 0.2, 0, 0);
    
    private final ShuffleboardValue<Double> voltage = ShuffleboardValue
        .create(0.0, "Voltage", ClawElevator.class.getSimpleName())
        .build();
    protected final ShuffleboardValue<Double> encoderPositionWriter = ShuffleboardValue
        .create(0.0, "Encoder Position", ClawElevator.class.getSimpleName())
        .withSize(1, 3)
        .build();
    protected final ShuffleboardValue<Boolean> isMovingManually = ShuffleboardValue
        .create(false, "Moving manually", ClawElevator.class.getSimpleName())
        .build();
;

    public ClawElevator(Boolean isEnabledLeft, Boolean isEnabledRight) {
        leftMotor = new SafeCanSparkMax(
            16, 
            MotorType.kBrushless,
            false,
            IdleMode.Brake,
            Constants.ROT_TO_INCHES,
            1.0,
            ShuffleboardValue.create(isEnabledLeft, "Is Enabled Left", ClawElevator.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
            voltage
        );
        rightMotor = new SafeCanSparkMax(
            15, 
            MotorType.kBrushless,
            true,
            IdleMode.Brake,
            Constants.ROT_TO_INCHES,
            1.0,
            ShuffleboardValue.create(isEnabledRight, "Is Enabled Right", ClawElevator.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
            voltage
        );
        
        controller.setTolerance(0.1);

        ComplexWidgetBuilder.create(controller, " PID Controller", ClawElevator.class.getSimpleName())
            .withWidget(BuiltInWidgets.kPIDController)
            .withSize(2, 2);
        ComplexWidgetBuilder.create(
            DisabledCommand.create(runOnce(this::resetEncoder)),
            "Reset Encoder", ClawElevator.class.getSimpleName());
    }

    @Override
    public void periodic() {
        setVoltage(controller.calculate(getEncoderPosition(),getTargetPosition()) + 
            feedforward.calculate(getTargetPosition()));
    }
    @Override
    public void simulationPeriodic() {
        periodic();
    }

    
    public void setTargetPosition(double target) {
        if (target < Constants.MIN_POSITION) return;
        if (target > Constants.MAX_POSITION) return;
        controller.setSetpoint(target);
    }
    protected void setVoltage(double voltage) {
        leftMotor.setVoltage(voltage);
        // rightMotor.setVoltage(voltage);
    }
    public void setMovingManually(boolean value) {
        getIsMovingManually().set(value);
    }
    protected ShuffleboardValue<Boolean> getIsMovingManually() {
        return isMovingManually;
    }

    public void resetEncoder() {
        leftMotor.getEncoder().setPosition(0);
    }

    public double getEncoderPosition() {
        double position = leftMotor.getEncoder().getPosition();
        encoderPositionWriter.write(position);
        return position;
    }

    public double getTargetPosition(){
        return controller.getSetpoint();
    }

    public boolean isMovingManually() {
        return getIsMovingManually().get();
    }
}
