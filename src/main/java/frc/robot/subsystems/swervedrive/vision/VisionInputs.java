package frc.robot.subsystems.swervedrive.vision;

import edu.wpi.first.math.geometry.Pose2d;
import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;
import frc.robot.subsystems.swervedrive.vision.truster.FilterResult;

public class VisionInputs implements LoggableInputs {
    public double[] timestamp = new double[0];
    public double[] serverTime = new double[0];
    public Pose2d[] position = new Pose2d[0];
    public double[] distanceFromTag = new double[0];
    public FilterResult[] filterResults = new FilterResult[0];
    public void toLog(LogTable table) {
        table.put("timestamp", timestamp);
        table.put("serverTime", serverTime);
        table.put("distanceFromTag", distanceFromTag);
        table.put("filterResults", filterResults);
        table.put("position", position);
    }
    public void fromLog(LogTable table) {
        this.timestamp = table.get("timestamp", timestamp);
        this.serverTime = table.get("serverTime", serverTime);
        this.position = table.get("position", position);
        this.distanceFromTag = table.get("distanceFromTag", distanceFromTag);
        this.filterResults = table.get("filterResults", filterResults);
    }
}