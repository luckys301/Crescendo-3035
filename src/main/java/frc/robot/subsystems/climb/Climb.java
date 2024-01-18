package frc.robot.subsystems.climb;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.DisabledCommand;
import frc.robot.utility.motor.SafeCanSparkMax;
import frc.robot.utility.motor.SafeMotor.IdleMode;
import frc.robot.utility.shuffleboard.ComplexWidgetBuilder;
import frc.robot.utility.shuffleboard.ShuffleboardValue;
@Deprecated
public class Climb extends SubsystemBase{
    public static class Constants {
        public static final double GEAR_RATIO = 1 / 1;
        public static final double GEAR_DIAMETER_INCHES = 1.88;
        public static final double COUNTS_PER_PULSE = 1; // 2048 bc rev through bore
        public static final double ROT_TO_INCHES = (COUNTS_PER_PULSE * GEAR_RATIO) / (GEAR_DIAMETER_INCHES * Math.PI);
        public static final double MIN_POSITION = 0;
        public static final double MAX_POSITION = 16.2;
    }
    private final PIDController controller = new PIDController(2.4, 0, 0);
    private final SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(0.1, 0.2, 0);
    private final ShuffleboardValue<Double> voltage = ShuffleboardValue.create(0.0, "Voltage", Climb.class.getSimpleName())
        .build();

    private final SafeCanSparkMax leftMotor, rightMotor;

    protected final ShuffleboardValue<Double> encoderPositionWriter = ShuffleboardValue.create(0.0, "Encoder Position", Climb.class.getSimpleName())
        .withSize(1, 3)
        .build();
    protected final ShuffleboardValue<Boolean> isMovingManually = ShuffleboardValue.create(false, "Moving manually", Climb.class.getSimpleName())
        .build();

    private final RelativeEncoder encoder;

    public Climb(Boolean isEnabledLeft, Boolean isEnabledRight) {
        leftMotor = new SafeCanSparkMax(
            16, 
            MotorType.kBrushless,
            ShuffleboardValue.create(isEnabledLeft, "Is Enabled Left", Climb.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
            voltage
        );

        rightMotor = new SafeCanSparkMax(
            15, 
            MotorType.kBrushless,
            ShuffleboardValue.create(isEnabledRight, "Is Enabled Right", Climb.class.getSimpleName())
                .withWidget(BuiltInWidgets.kToggleSwitch)
                .build(),
            voltage
        );
        leftMotor.setIdleMode(IdleMode.Brake);
        rightMotor.setIdleMode(IdleMode.Brake);
        leftMotor.setInverted(false);
        rightMotor.follow(leftMotor, true);
        
        controller.setTolerance(0.1);

        encoder = leftMotor.getEncoder();
        // encoder = rightMotor.getEncoder();
        encoder.setPositionConversionFactor(Constants.ROT_TO_INCHES);

        ComplexWidgetBuilder.create(controller, " PID Controller", Climb.class.getSimpleName())
            .withWidget(BuiltInWidgets.kPIDController)
            .withSize(2, 2);

        ComplexWidgetBuilder.create(DisabledCommand.create(runOnce(this::resetEncoder)), "Reset Encoder", Climb.class.getSimpleName());
    }

    
    // protected PIDController getController() {
    //     return controller;
    // }
  
    // protected SimpleMotorFeedforward getFeedforward() {
    //     return feedforward;
    // }

    
    protected void setVoltage(double voltage) {
        leftMotor.setVoltage(voltage);
        // rightMotor.setVoltage(voltage);
    }

    
    protected ShuffleboardValue<Boolean> getIsMovingManually() {
        return isMovingManually;
    }

    
    public double getMaxPosition() {
        return Constants.MAX_POSITION;
    }

    
    public double getMinPosition() {
        return Constants.MIN_POSITION;
    }

    
    public void resetEncoder() {
        encoder.setPosition(0);
    }

    
    public double getEncoderPosition() {
        double position = encoder.getPosition();
        encoderPositionWriter.write(position);
        return position;
    }

    public void setTargetPosition(double positionRadians) {
        if (positionRadians < getMinPosition()) return;
        if (positionRadians > getMaxPosition()) return;
        controller.setSetpoint(positionRadians);
    }

    public double getTargetPosition() {
        return controller.getSetpoint();
    }

    public void setMovingManually(boolean value) {
        getIsMovingManually().set(value);
    }

    public boolean isMovingManually() {
        return getIsMovingManually().get();
    }

    protected double calculateFeedforward(double targetVelocity) {
        return feedforward.calculate(targetVelocity);
    }

    protected double calculatePID(double targetVelocity) {
        return controller.calculate(getEncoderPosition(), targetVelocity);
    }
}
