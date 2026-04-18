package frc.robot.subsystems.swervedrive.align;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

import org.littletonrobotics.junction.Logger;
import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;

public class AutoAlignGenerator {
    public ArrayList<Node> nodeList = new ArrayList<>();
    private ArrayList<Pose2d> targetPath = new ArrayList<>();
    private final SwerveSubsystem drivebase;

    public AutoAlignGenerator(SwerveSubsystem drivebase) {
        this.drivebase = drivebase;
    }

    public AutoAlignGenerator addNode(Node node) {
        nodeList.add(node);
        Logger.recordOutput("Nodes/Node" + nodeList.size() + "/Pose", node.pose());
        Logger.recordOutput("Nodes/Node" + nodeList.size() + "/State", node.isActive());
        return this;
    }

    // robotPose.getTranslation().getDistance(nodeList.get(i).getTranslation())
    public void generatePath(Pose2d pose) {
        ArrayList<Double> xAcceptedDistanceList = new ArrayList<>();
        ArrayList<Pose2d> xAcceptedPoseList = new ArrayList<>();
        Pose2d robotPose = drivebase.getPose();
        for (int i = 0; i <= nodeList.size() - 1; i++) {
            Pose2d activePose = nodeList.get(i).pose();
            double x = activePose.getX();
            if (-(x - robotPose.getX()) * (x - pose.getX()) > 0 && nodeList.get(i).isActive().getAsBoolean()) {
                xAcceptedDistanceList.add(x);
                xAcceptedPoseList.add(activePose);
            }
        }
        ArrayList<Pose2d> path = new ArrayList<>();
        if (xAcceptedPoseList.size() > 0) {
            for (int i = 0; i <= nodeList.size()-1; i++) {
                Pose2d activePose = nodeList.get(i).pose();
                int q = 0;
                if (xAcceptedPoseList.contains(activePose)) {
                    if (path.size() > 0) {
                        while (path.size() > q && Math.abs(activePose.getX()-pose.getX()) < Math.abs(path.get(q).getX()-pose.getX())){
                            q++;
                        }
                    }
                    path.add(q, activePose);
                }
            }
        }
        path.add(pose);
        Pose2d[] alignPath = new Pose2d[path.size()];
        for (int i = 0; i <= path.size() - 1; i++) {
            alignPath[i] = path.get(i);
        }
        Logger.recordOutput("AlignPath", alignPath);
        targetPath = path;
    }

    public ArrayList<Pose2d> getTargetPath() {
        return targetPath;
    }
}
