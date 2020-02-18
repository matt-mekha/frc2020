package frc.robot.commands.main;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.commands.climb.Climb;
import frc.robot.commands.drive.DriveInches;
import frc.robot.commands.drive.DriveTrajectory;
import frc.robot.commands.drive.RotateDegrees;
import frc.robot.commands.intake.MoveIntake;
import frc.robot.commands.intake.ToggleIndexerTower;
import frc.robot.commands.intake.ToggleIntake;
import frc.robot.commands.intake.MoveIntake.IntakePosition;
import frc.robot.commands.music.PlayMusic;
import frc.robot.commands.shooter.AutoShoot;
import frc.robot.commands.shooter.ResetNavX;

public class Auton extends ParallelCommandGroup {

    public Auton(AutonType autonType) {
        addCommands(
            getAutonSequence(autonType)
        );
    }

    public enum AutonType {
        DO_NOTHING,
        SHOOT_TEST,
        CLIMB_TEST,
        SHUFFLE_PLAY,

        SQUARE,
        PATH_1;
    }

    private Command getAutonSequence(AutonType autonType) {
        switch(autonType) {

            case SHOOT_TEST: return
                parallel(
                    new ResetNavX(),
                    new ToggleIntake(true),
                    new AutoShoot()
                );

            case CLIMB_TEST: return
                sequence(
                    new Climb(true),
                    new DriveInches(6),
                    // new Climb(ClimbDirection.DOWN)
                    // So let me explain why this is commented out
                    // you will never, _never_ forcefully push your robot onto the field, or on the ground
                    // _EVEN FOR A TEST_
                );

            case SHUFFLE_PLAY: return
                new PlayMusic();

            case SQUARE: return
                sequence(
                    new ResetNavX(0, 0, 0),
                    new DriveInches(24),
                    new RotateDegrees(90),
                    new DriveInches(24),
                    new RotateDegrees(90),
                    new DriveInches(24),
                    new RotateDegrees(90),
                    new DriveInches(24),
                    new RotateDegrees(90)
                );

            case PATH_1: return
                sequence(
                    new AutoShoot(),
                    new MoveIntake(IntakePosition.DOWN),
                    new ToggleIntake(true),
                    new ToggleIndexerTower(true),
                    new DriveTrajectory("GoToCenter", true),
                    new WaitCommand(3),
                    new DriveTrajectory("GoToControlPanel")
                );

            default: return
                new ResetNavX();

        }
    }
}
