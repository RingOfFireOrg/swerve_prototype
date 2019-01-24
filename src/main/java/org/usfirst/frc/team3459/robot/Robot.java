package org.usfirst.frc.team3459.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Talon;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	Joystick commandStick = new Joystick(0);
	SendableChooser<String> chooser = new SendableChooser<>();
	JoystickButton frButton = new JoystickButton(commandStick, 6);
	JoystickButton flButton = new JoystickButton(commandStick, 5);
	JoystickButton brButton = new JoystickButton(commandStick, 4);
	JoystickButton blButton = new JoystickButton(commandStick, 3);
	JoystickButton trigger = new JoystickButton(commandStick, 1);
	JoystickButton stop = new JoystickButton(commandStick, 2);
	JoystickButton liftyBoy = new JoystickButton(commandStick, 7);
	
	private DoubleSolenoid pusher = new DoubleSolenoid(0,1);
	boolean buttonCycle = false;
	
	
	SwerveDrive swerveDrive = new SwerveDrive();
//	AHRS ahrs;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		chooser.addDefault("Default Auto", defaultAuto);
		chooser.addObject("My Auto", customAuto);

		SmartDashboard.putData("Auto choices", chooser);

		CameraServer.getInstance().startAutomaticCapture();

		try {
//			ahrs = new AHRS(SerialPort.Port.kUSB1);
		} catch (RuntimeException ex) {
			DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
		}

//		ahrs.reset();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional comparisons to the
	 * switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		autoSelected = chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		switch (autoSelected) {
		case customAuto:
			// Put custom auto code here
			break;
		case defaultAuto:
		default:
			// Put default auto code here
			break;
		}
	}

	/**
	 * This function is called periodically during operator control
	 */

	@Override
	public void teleopPeriodic() {
		double twist;
		double speed = Math.pow(commandStick.getMagnitude(), 2);
		double direction = commandStick.getDirectionDegrees() * -1;
		twist = commandStick.getTwist();
		if(twist < 0) {
			twist = -Math.pow(twist, 2);
		} else {
			twist = Math.pow(twist, 2);
		}
		SmartDashboard.putNumber("Joystick output", direction);
		SmartDashboard.putNumber("Joystick output speed", speed);
		SmartDashboard.putNumber("Joystick twist", twist);
	
		swerveDrive.syncroDrive(speed, direction, twist);
//		SmartDashboard.putNumber("Gyro output: ", ahrs.getAngle());
//				swerveDrive.syncroDrive(speed, direction, twist);	

	if(buttonCycle && trigger.get()) {
		if(pusher.get() == DoubleSolenoid.Value.kReverse) {
			pusher.set(DoubleSolenoid.Value.kForward);
		} else {
			pusher.set(DoubleSolenoid.Value.kReverse);
		}
		buttonCycle = false;
	}

	if (trigger.get() == false) {
		buttonCycle = true;
	}

	if(buttonCycle && upDown.get()) {
		//sim spins forward
	} else {
		//sim spins backwards
	}

	if(upDown.get() == false) {
		buttonCycle = true;

	}

	boolean upPressed = manipulatorStick.getRawButton(RobotMap.LIFT_UP_BUTTON);
	boolean downPressed = manipulatorStick.getRawButton(RobotMap.LIFT_DOWN_BUTTON);	

	if (upPressed) {
		lifter.up();
	} else if (downPressed) {
		lifter.down();
	} else {
		lifter.stop();
	}
}

	/**
	 * This function is called periodically during test mode
	 */

	@Override
	public void testPeriodic() {
		swerveDrive.individualModuleControl(frButton.get(), flButton.get(), brButton.get(), blButton.get());
	}
}