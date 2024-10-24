package frc.robot.utility.motor.better;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;

import frc.robot.DroidRageConstants;

public class TalonEx extends CANMotorEx {
    private final TalonFX motor;
    private TalonFXConfiguration configuration = new TalonFXConfiguration();
    
    private TalonEx(TalonFX motor) {
        this.motor = motor;
        // motor.getConfigurator().apply(configuration);
    }

    public static DirectionBuilder create(int deviceID) {
        CANMotorEx motor = new TalonEx(new TalonFX(deviceID));
        return motor.new DirectionBuilder();
    }

    @Override
    public void setPower(double power) {
        if(isEnabledWriter.get()){
            motor.set(power);
        }
        if(DroidRageConstants.removeWriterWriter.get()){//if(!DriverStation.isFMSAttached())
            outputWriter.set(power);
        }
    }

    @Override
    public void setVoltage(double outputVolts) {
        if(isEnabledWriter.get()){
            motor.setVoltage(outputVolts);
        }
        if(DroidRageConstants.removeWriterWriter.get()){//if(!DriverStation.isFMSAttached())
            outputWriter.set(outputVolts);
        }
    }

    @Override
    public void setDirection(Direction direction) {
        motor.setInverted(switch (direction) {
                case Forward -> false;
                case Reversed -> true;
            });
    }

    @Override
    public void setIdleMode(ZeroPowerMode mode) {
        motor.setNeutralMode(switch (mode) {
            case Brake -> NeutralModeValue.Brake;
            case Coast -> NeutralModeValue.Coast;
        });
    }

    //Already in rotations per sec so, just covert to
    @Override
    public double getVelocity() {
        return motor.getVelocity().getValueAsDouble()*positionConversionFactor;
    }

    @Override
    public double getPosition() {
        return motor.getPosition().getValueAsDouble()*positionConversionFactor;
    }

    @Override
    public int getDeviceID() {
        return motor.getDeviceID();
    }

    @Override
    public void setSupplyCurrentLimit(double currentLimit) {
        configuration.CurrentLimits.SupplyCurrentLimit = currentLimit;
        configuration.CurrentLimits.SupplyCurrentLimitEnable = true;
        motor.getConfigurator().apply(configuration);
    }

    public TalonEx withStatorCurrentLimit(double statorCurrent){
        setStatorCurrentLimit(statorCurrent);
        return this;
    }
    public void setStatorCurrentLimit(double statorCurrent){
        // CurrentLimitsConfigs configs = new CurrentLimitsConfigs();
        // configs.StatorCurrentLimit = 50;
        // motor.getConfigurator().apply(configs);
        configuration.CurrentLimits.StatorCurrentLimit = statorCurrent;
        configuration.CurrentLimits.StatorCurrentLimitEnable = true;
        motor.getConfigurator().apply(configuration);
    }

    @Override
    public void resetEncoder(int num) {
        motor.setPosition(num);
    }
}