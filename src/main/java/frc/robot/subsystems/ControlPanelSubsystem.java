package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ColorSensor;
import frc.robot.Constants.ControlPanelSpinner;
import frc.robot.Constants.MotorIDs;

public class ControlPanelSubsystem extends SubsystemBase {

    private final WPI_TalonSRX spinner = new WPI_TalonSRX(MotorIDs.CONTROL_PANEL_SPINNER);
    
    private final ColorSensorV3 colorSensor = new ColorSensorV3(ColorSensor.PORT);
    private final ColorMatch colorMatcher = new ColorMatch();
    
    private PanelColor detectedColor;
    private PanelColor currentColor;
    private PanelColor pendingColor;
    
    private Timer verifyColorTimer = new Timer();

    public ControlPanelSubsystem() {
        spinner.configFactoryDefault();
        spinner.setNeutralMode(ControlPanelSpinner.BRAKE_MODE);
        
        toggle(false);
        
        for(PanelColor color : PanelColor.values()) {
            colorMatcher.addColorMatch(color.getColor());
        }
    }

    @Override
    public void periodic() {
        detectedColor = getDetectedColor();
        
        if(detectedColor == pendingColor) {
            if(verifyColorTimer.get() >= ColorSensor.VERIFY_COLOR_TIME) {
                verifyColorTimer.stop();
                
                currentColor = pendingColor;
                pendingColor = null;
            }
        } else {
            pendingColor = null;

            if(detectedColor != currentColor) {
                verifyColorTimer.reset();
                verifyColorTimer.start();

                pendingColor = detectedColor;
            }
        }
        
        if(currentColor != null)
            SmartDashboard.putString("Current color", currentColor.getName());
    }

    public void toggle(boolean on) {
        spinner.set(on ? ControlPanelSpinner.MOTOR_SPEED : 0);
    }
    
    private PanelColor getDetectedColor() {
        Color detectedColorRaw = colorSensor.getColor();
        SmartDashboard.putString("Detected color", (int)(detectedColorRaw.red*100) + ", " + (int)(detectedColorRaw.green*100) + ", " + (int)(detectedColorRaw.blue*100));
        ColorMatchResult colorMatchResult = colorMatcher.matchClosestColor(detectedColorRaw);
        for(PanelColor color : PanelColor.values()) {
            if(colorMatchResult.color == color.getColor()) {
                return color;
            }
        }
        return null;
    }
    public PanelColor getCurrentColor() {
        //return detectedColor;
        return currentColor;
    }

    public enum PanelColor {
        RED     (ColorSensor.RED),
        GREEN   (ColorSensor.GREEN),
        BLUE    (ColorSensor.BLUE),
        YELLOW  (ColorSensor.YELLOW);

        private Color color;
        private String name;

        private PanelColor(double[] rgb) {
            this.color = ColorMatch.makeColor(rgb[0], rgb[1], rgb[2]);
            this.name = this.toString();
        }

        public Color getColor() {
            return color;
        }
        public String getName() {
            return name;
        }

        public static PanelColor fromChar(char c) {
            switch(c) {
                case 'R':
                    return RED;
                case 'G':
                    return GREEN;
                case 'B':
                    return BLUE;
                case 'Y':
                    return YELLOW;
                default:
                    return null;
            }
        }
    }
}