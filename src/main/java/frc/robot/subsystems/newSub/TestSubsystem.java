package frc.robot.subsystems.newSub;


import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import frc.robot.DroidRageConstants.Control;
import frc.robot.utility.encoder.AbsoluteDutyEncoderRIO;
import frc.robot.utility.motor.better.CANMotorEx;
import frc.robot.utility.motor.better.SparkMax;
import frc.robot.utility.motor.better.TalonEx;
import frc.robot.utility.motor.better.CANMotorEx.ZeroPowerMode;
import frc.robot.utility.template.ArmAbsolute;

public class TestSubsystem extends ArmAbsolute {
    
    private static SparkMax test = SparkMax.create(3)
            .withDirection(CANMotorEx.Direction.Forward)
            .withIdleMode(ZeroPowerMode.Brake)
            .withPositionConversionFactor(1)
            .withSubsystemName(TestSubsystem.class.getName())
            .withIsEnabled(false);
    
    private static TalonEx talon = TalonEx.create(2)
        .withDirection(CANMotorEx.Direction.Forward)
        .withIdleMode(ZeroPowerMode.Brake)
        .withPositionConversionFactor(1)
        .withSubsystemName(TestSubsystem.class.getName())
        .withIsEnabled(false)
        .withSupplyCurrentLimit(0);
            
    public TestSubsystem(){
        super(new CANMotorEx[]{test, talon} , new PIDController(1,0,0), 
        new ArmFeedforward(0, 0, 0), 
        360, 90, 90, 
        Control.PID, TestSubsystem.class.getName(), 0, 
            AbsoluteDutyEncoderRIO.create(3)
            .withDirection(false)
            .withOffset(0)
            .withSubsystemBase(TestSubsystem.class.getName()));
        // test.get
    }

}
