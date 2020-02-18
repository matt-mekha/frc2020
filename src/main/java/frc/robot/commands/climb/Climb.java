package frc.robot.commands.climb;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj2.command.InstantCommand;

import static frc.robot.Robot.*;

public class Climb extends InstantCommand {

    private static boolean just_setup; // if true, only push the big pistons up to grab bar, don't actually climb
    private static boolean direction; // true = up
    public Climb(boolean up, boolean setup ) {
        just_setup = setup;
    }

    @Override
    public void initialize() {
        if (just_setup) {
            climbSubsystem.setPistons( UP );
            climbSubsystem.setMiniPistons( UP );
        } else {
          climbSubsystem.setPistons( DOWN );
          climbSubsystem.setMiniPistons( DOWN );
        }
    }

    public enum ClimbDirection {
        UP  (DoubleSolenoid.Value.kForward),
        DOWN(DoubleSolenoid.Value.kReverse);
    }
}
