/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.subsystems.DriveTrainSubsystem;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.Controller;

public class TeleopDrive extends CommandBase {

	/**
	 * 
	 * 		this command runs the entire teleop period
	 * 
	 * 		what it does:
	 * 		- takes in the controller stick inputs
	 * 		- sends input to DriveTrainSubsystem every tick
	 * 		- tells DriveTrainSubsystem to stop motors when done
	 * 
	*/

	private final XboxController xboxController;
	private final DriveTrainSubsystem driveTrainSubsystem;

	public TeleopDrive(XboxController xboxController, DriveTrainSubsystem driveTrainSubsystem) {
		this.xboxController = xboxController;
		this.driveTrainSubsystem = driveTrainSubsystem;
		addRequirements(driveTrainSubsystem);
	}

	@Override
	public void initialize() {
		
	}

	@Override
	public void execute() {
		double xSpeed = -xboxController.getY(Controller.SPEED_HAND); // get throttle
		double zRotation = xboxController.getX(Controller.ROTATION_HAND); // get rotation
		driveTrainSubsystem.arcadeDrive(xSpeed, zRotation); // give DriveTrainSubsystem throttle and rotation, it'll do the rest
	}

	@Override
	public void end(boolean interrupted) {
		driveTrainSubsystem.stop(); // stop the motors
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}