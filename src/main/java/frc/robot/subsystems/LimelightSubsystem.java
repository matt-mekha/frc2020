package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.Limelight.*;

import javax.annotation.CheckForNull;

import frc.robot.util.Vector3;

public class LimelightSubsystem extends SubsystemBase {

    private final NetworkTable table;

    private Pipeline currentPipeline;

    public LimelightSubsystem() {
        table = NetworkTableInstance.getDefault().getTable("limelight");
        currentPipeline = Pipeline.POWER_PORT;

        SmartDashboard.setDefaultBoolean("Limelight LED", true);
    }
    @Override
    public void periodic() {
        Number ledMode = SmartDashboard.getBoolean("Limelight LED", true) ? 3 : 1;
        setNumber("ledMode", ledMode);
    }



    private double getDouble(String name) {
        return table.getEntry(name).getDouble(0);
    }
    private void setNumber(String name, Number number) {
        table.getEntry(name).setNumber(number);
    }


    @CheckForNull
    public Vector3 getTargetInfo() {
        boolean targetExists = getDouble("tv") == 1.0;
        if(!targetExists) {
            return null;
        }

        double tx = getDouble("tx");
        double ty = getDouble("ty");
        //double targetArea = getDouble("ta");
        //double targetSkew = getDouble("ts");

        double targetHeight = currentPipeline.getTargetHeight();

        return new Vector3(
            tx, // horizontal offset angle
            CAMERA_MOUNTING_ANGLE + ty, // vertical offset angle
            targetHeight // inches
        ).print("Limelight data");
    }


    public void setPipeline(Pipeline pipeline) {
        this.currentPipeline = pipeline;
        setNumber("pipeline", pipeline.getID());
    }

    public enum Pipeline {
        POWER_PORT(0, SHOOTER_VISION_HEIGHT_OFFSET),
        LOADING_STATION(1, LOADING_STATION_VISION_HEIGHT_OFFSET); // TODO no need to use vision for loading station

        private int num;
        private double height;

        private Pipeline(int num, double height) {
            this.num = num;
            this.height = height;
        }

        private int getID() {
            return num;
        }
        private double getTargetHeight() {
            return height;
        }
    }

}
