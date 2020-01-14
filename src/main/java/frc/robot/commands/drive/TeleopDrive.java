/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drive;

import frc.robot.subsystems.DriveTrainSubsystem;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import static frc.robot.Constants.Controller.*;
import static frc.robot.Constants.DriveTrain.*;

public class TeleopDrive extends CommandBase {

	// this command runs the entire teleop period

	private final XboxController xboxController;
	private final DriveTrainSubsystem driveTrainSubsystem;

	public TeleopDrive(XboxController xboxController, DriveTrainSubsystem driveTrainSubsystem) {
		addRequirements(driveTrainSubsystem);
		this.xboxController = xboxController;
		this.driveTrainSubsystem = driveTrainSubsystem;
	}
	
	@Override
	public void execute() {
		double speed = -xboxController.getY(SPEED_HAND);
		double rotation = xboxController.getX(ROTATION_HAND);
		driveTrainSubsystem.setGoalVelocity((speed + rotation) * MAX_VELOCITY, (speed - rotation) * MAX_VELOCITY);
	}

	@Override
	public void end(boolean interrupted) {
		driveTrainSubsystem.stop();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}