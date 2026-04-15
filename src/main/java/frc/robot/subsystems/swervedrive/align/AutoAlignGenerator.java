package frc.robot.subsystems.swervedrive.align;

import java.util.ArrayList;
import java.util.HashMap;

import org.littletonrobotics.junction.Logger;
import edu.wpi.first.math.geometry.Pose2d;
import frc.robot.subsystems.swervedrive.SwerveSubsystem;

public class AutoAlignGenerator {
    public ArrayList<Pose2d> nodeList = new ArrayList<>();
    private Pose2d targetPose = new Pose2d();
    private final SwerveSubsystem drivebase;
    private final AutoAlign alignCommand;

    public AutoAlignGenerator(SwerveSubsystem drivebase) {
        this.drivebase = drivebase;
        alignCommand = new AutoAlign(() -> {
            return targetPose;
        }, drivebase);
    }

    public AutoAlignGenerator addNode(Pose2d pose) {
        nodeList.add(pose);
        Logger.recordOutput("Nodes/Node" + nodeList.size(), pose);
        return this;
    }

    // robotPose.getTranslation().getDistance(nodeList.get(i).getTranslation())
    public void generatePath(Pose2d pose) {
        HashMap<Pose2d, Double> xAcceptedDistanceMap = new HashMap<>();
        HashMap<Double, Pose2d> xAcceptedDistanceMapInverse = new HashMap<>();
        Pose2d robotPose = drivebase.getPose();
        for (int i = 0; i <= nodeList.size() - 1; i++) {
            double x = nodeList.get(i).getX();
            if ((x - robotPose.getX()) * (x - pose.getX()) > 0) {
                if (!xAcceptedDistanceMap.containsValue(Math.round(nodeList.get(i).getX() * 10) / 10.0)) {
                    xAcceptedDistanceMap.put(nodeList.get(i), Math.round(nodeList.get(i).getX() * 10) / 10.0);
                    xAcceptedDistanceMapInverse.put(nodeList.get(i).getX(), nodeList.get(i));
                } else {
                    if (xAcceptedDistanceMapInverse.get(nodeList.get(i).getX()).getTranslation()
                            .getDistance(robotPose.getTranslation()) > nodeList.get(i).getTranslation()
                                    .getDistance(robotPose.getTranslation())) {
                        xAcceptedDistanceMap.remove(xAcceptedDistanceMapInverse.get(nodeList.get(i).getX()));
                        xAcceptedDistanceMap.put(nodeList.get(i), Math.round(nodeList.get(i).getX() * 10) / 10.0);
                        xAcceptedDistanceMapInverse.put(nodeList.get(i).getX(), nodeList.get(i));
                    }
                }
            }
        }
        ArrayList<Pose2d> path = new ArrayList<>();
        for (int i = 0; i <= xAcceptedDistanceMap.size() - 1; i++) {
            if (xAcceptedDistanceMap.containsKey(nodeList.get(i))) {
                int q =0;
                while (nodeList.get(i).getTranslation().getDistance(robotPose.getTranslation()) > path.get(q).getTranslation()
                        .getDistance(robotPose.getTranslation())) {
                    q++;
                }
                path.add(q,nodeList.get(i));
            }
        }
        path.add(path.size() - 1,pose);
        targetPose = path.get(0);
        alignCommand.schedule();
    }
}
