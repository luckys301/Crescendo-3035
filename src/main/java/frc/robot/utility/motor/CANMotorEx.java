package frc.robot.utility.motor;

import frc.robot.utility.shuffleboard.ShuffleboardValue;

public abstract class CANMotorEx {
    // protected int deviceID; // specific and should not be in the abstract class
    // TODO: which fields should be final?
    protected Direction direction;
    protected ZeroPowerMode idleMode;
    protected double positionConversionFactor;
    protected double velocityConversionFactor;
    protected ShuffleboardValue<Boolean> isEnabledWriter;
    protected ShuffleboardValue<Double> outputWriter;
    
    public enum Direction {
        Forward,
        Reversed,
    }

    public enum ZeroPowerMode {
        Brake,
        Coast,
    }

    // public MotorEx(ShuffleboardValue<Boolean> isEnabled, 
    //     ShuffleboardValue<Double> outputWriter) {
    //     this.isEnabled = isEnabled;
    //     this.outputWriter = outputWriter;
    // }

    public class DirectionBuilder {
        public IdleModeBuilder withDirection(Direction direction) {
            setDirection(direction);
            return new IdleModeBuilder();
        }
    }
    public class IdleModeBuilder {
        public PositionConversionFactorBuilder withIdleMode(ZeroPowerMode idleMode) {
            setIdleMode(idleMode);
            return new PositionConversionFactorBuilder();
        }
    }
    public class PositionConversionFactorBuilder {
        public IsEnabledBuilder withPositionConversionFactor(double positionConversionFactor) {
            setPositionConversionFactor(positionConversionFactor);
            setVelocityConversionFactor(positionConversionFactor/60);
            return new IsEnabledBuilder();
        }
    }
    public class IsEnabledBuilder {
        @SuppressWarnings("unchecked")
        public <T extends CANMotorEx> T withIsEnabled(boolean isEnabled) {
            setIsEnabled(isEnabled);
            return (T) CANMotorEx.this;
        }
    }

    // public class VelocityConversionFactorBuilder {
    //     @SuppressWarnings("unchecked")
    //     public <T extends CANMotorEx> T withVelocityConversionFactor(double velocityConversionFactor) {
    //         setVelocityConversionFactor(positionConversionFactor);
    //         return (T) CANMotorEx.this;
    //     }
    // }
    
    @SuppressWarnings("unchecked")
    public <T extends CANMotorEx> T withSupplyCurrentLimit(double currentLimit) {
        setSupplyCurrentLimit(currentLimit);
        return (T) CANMotorEx.this;
    }

    
    protected abstract void setDirection(Direction direction);
    protected abstract void setIdleMode(ZeroPowerMode mode);
    protected void setPositionConversionFactor(double positionConversionFactor){
        this.positionConversionFactor=positionConversionFactor;
    }
    protected void setVelocityConversionFactor(double velocityConversionFactor){
        this.velocityConversionFactor=velocityConversionFactor;
    };
    protected abstract void setSupplyCurrentLimit(double currentLimit);
    protected void setIsEnabled(boolean isEnabled){
        this.isEnabledWriter.set(isEnabled);
    };
    
    public abstract void setPower(double power);
    public abstract void setVoltage(double outputVolts);
    public abstract double getVelocity();
    public abstract double getPosition();
    public abstract int getDeviceID();
    public abstract void resetEncoder(int num);

    public void stop() {
        setPower(0);
    }
}