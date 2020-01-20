/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;

import static frc.robot.Constants.Controller.*;
import static frc.robot.Constants.DriveTrain.*;
import static frc.robot.Robot.*;

public class TeleopDrive extends CommandBase {

	// this command runs the entire teleop period

	public TeleopDrive() {
		addRequirements(driveTrainSubsystem);
		if(TUNING_MODE) {
			SmartDashboard.setDefaultNumber("Test Speed", 0);
		}
	}

	private double smooth(double x) {
		if(Math.abs(x) < DEADZONE) return 0;
		return Math.pow(x, 2) * Math.signum(x);
	}
	
	@Override
	public void execute() {
		if(TUNING_MODE) {
			double speed = SmartDashboard.getNumber("Test Speed", 0);
			driveTrainSubsystem.set(speed);
		} else {
			double speed = smooth(-xboxController1.getY(SPEED_HAND));
			double rotation = smooth(xboxController1.getX(ROTATION_HAND));
			double left = speed + rotation;
			double right = speed - rotation;
			driveTrainSubsystem.setMotorOutput(left * MAX_TELEOP_MOTOR_OUTPUT, right * MAX_TELEOP_MOTOR_OUTPUT);
			//driveTrainSubsystem.setGoalVelocity(left * MAX_VELOCITY, right * MAX_VELOCITY);

			SmartDashboard.putNumber("NavX Direction", navXSubsystem.getDirection());
			navXSubsystem.getVelocity().print("NavX Velocity");
			navXSubsystem.getDisplacement().print("NavX Displacement");
		}
	}

	@Override
	public void end(boolean interrupted) {
		driveTrainSubsystem.smoothStop();
	}

	@Override
	public boolean isFinished() {
		return false;
	}
}