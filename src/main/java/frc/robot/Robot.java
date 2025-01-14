// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.commands.autos.AutoChooser;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.ampMech.AmpMech;
import frc.robot.subsystems.ampMech.AmpMechElevator;
import frc.robot.subsystems.ampMech.AmpMechIntake;
import frc.robot.subsystems.ampMech.ampMechArm.AmpMechArmAbsolute;
import frc.robot.subsystems.climb.ClimbAlternate;
import frc.robot.subsystems.drive.SwerveDrive;
import frc.robot.subsystems.intake.Intake;
import frc.robot.subsystems.intake.IntakeWheel;
import frc.robot.subsystems.intake.dropDown.IntakeDropDownAbsolute;
import frc.robot.subsystems.vision.Vision;
import frc.robot.utility.InfoTracker.CycleTracker;
import frc.robot.utility.shuffleboard.ShuffleboardValue;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the manifest file in the resource
 * directory.
 */
//CAN 15 is skipped
//Test Vision
//Climb positions
//current stuff
public class Robot extends TimedRobot {
    //15 missing^
    // private AbsoluteDutyEncoder encoder = new AbsoluteDutyEncoder(0,1,0.86);
    private final SwerveDrive drive = new SwerveDrive(true);//2-10
    private final Shooter shooter = new Shooter(false);//18,19  %  

    private final AmpMechElevator elevator = new AmpMechElevator(false);//22-DO NOT TURN THIS ON
    private final AmpMechIntake ampIntake = new AmpMechIntake(false);//24
    private final AmpMechArmAbsolute arm = new AmpMechArmAbsolute(false, ampIntake.getMotor());//23
    private final AmpMech ampMech = new AmpMech(elevator, arm, ampIntake);

    private final ClimbAlternate climb = new ClimbAlternate(false,false);//20,21
    private final IntakeWheel intakeWheel = new IntakeWheel(false);//16
    private final IntakeDropDownAbsolute dropDown = new IntakeDropDownAbsolute(false, climb.getMotorR());//17
    private final Intake intake = new Intake(dropDown, intakeWheel);
    
    // private AutoChooser autoChooser = new AutoChooser(
    //     drive, intake, shooter, ampMech//, claw, climb, vision, light
    // );
    private final CycleTracker cycleTracker = new CycleTracker();//Good to Use
// private final Climb climb = new Climb(false,false);//20,21

    private final Vision vision = new Vision();
    // private final Light light = new Light();
    // private final SysID sysID = new SysID(climb.getMotorL(), climb.getMotorR(), Measurement.ANGLE);
    // private final SysID sysID = new SysID(claw.getClawIntake().getMotor(), Measurement.DISTANCE);

    private RobotContainer robotContainer = new RobotContainer();
    // private TestButton testButton = new TestButton();
    private NewTeleopButtons teleopButtons = new NewTeleopButtons();    

    private ShuffleboardValue<Double> matchTime = ShuffleboardValue.create
		(0.0, "Match Time", "Misc")
		.withWidget(BuiltInWidgets.kTextView)
		.build();
    private Command autonomousCommand;
  
  /**
     * This function is run when the robot is first started up and should be used for any
     * initialization code.
     * Instantiate our RobotContainer.  This will perform all our button bindings, and put our
     * autonomous chooser on the dashboard.
     */
    @Override
    public void robotInit() {
        // RobotController.setBrownoutVoltage(kDefaultPeriod);
        // 6.3V for Roborio1- Roborio2 is 6.75V
    }
    
    /**
     * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
     * that you want ran during disabled, autonomous, teleoperated and test.
     *
     * <p>This runs after the mode specific periodic functions, but before LiveWindow and
     * SmartDashboard integrated updating.
     * Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
     * commands, running already-scheduled commands, removing finished or interrupted commands,
     * and running subsystem periodic() methods.  This must be called from the robot's periodic
     * block in order for anything in the Command-based framework to work.
     */
    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
        // if(DriverStation.isEStopped()){ //Robot Estopped
        //     light.flashingColors(light.red, light.white);
        // }
        // light.setAllColor(light.yellow);
    }

    @Override
    public void disabledInit() {
        // cycleTracker.printAllData();

    }
    
    @Override
    public void disabledPeriodic() {
        //In Here, Try using controller to pick the auto
        // CommandXboxController commandXboxController = new CommandXboxController(3);
        // commandXboxController.a().onTrue(
        //     autoChooser.set
        // )
        // if(RobotController.getBatteryVoltage()<11.5){
        //     light.setAllColor(light.batteryBlue);
        //     // drive.playMusic(2);
        // } else{
        //     light.flashingColors(light.yellow, light.blue);
        // }
        // light.setAllColor(light.blue);
    }

    @Override
    public void autonomousInit() {
        CommandScheduler.getInstance().cancelAll();
        autonomousCommand = AutoChooser.getAutonomousCommand();
        // ampMech.setAutoStartPos();
        // shooter.setTargetVelocity(ShooterSpeeds.AUTO_SPEAKER_SHOOT);
        autonomousCommand = new InstantCommand();

        if (autonomousCommand != null) {
            autonomousCommand.schedule();
        }
    }

    @Override
    public void autonomousPeriodic() {
        // if(DriverStation.isEStopped()){ //Robot Estopped
        //     light.flashingColors(light.red, light.white);
        // }
    }
    
    @Override
    public void teleopInit() {
        CommandScheduler.getInstance().cancelAll();
		DriverStation.silenceJoystickConnectionWarning(true);
        // ampMech.setTeleopStartPos();
        // drive.changeAllianceRotation();//Works
        // drive.runOnce(()->drive.setYawCommand(drive.getRotation2d().rotateBy(Rotation2d.fromDegrees(0)).getDegrees()));

		// drive.driveAutoReset();//TODO: Test
        robotContainer.configureTeleOpBindings(drive, intake, shooter, ampMech, climb, cycleTracker,vision
        );
        // teleopButtons.newTeleopButtons( climb, intake, shooter, ampMech , drive);
    }

    @Override
    public void teleopPeriodic() {
        // robotContainer.teleopPeriodic(intake,shooter);
        matchTime.set(DriverStation.getMatchTime());
    }
    
    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }
    @Override
    public void testPeriodic() {}

    @Override
    public void simulationInit() {}

    @Override
    public void simulationPeriodic() {}

    @Override
    public void teleopExit(){
        // cycleTracker.printAllData();
    }


        
}
