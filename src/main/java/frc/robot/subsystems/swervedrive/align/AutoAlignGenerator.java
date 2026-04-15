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
        ArrayList<Double> xAcceptedDistanceList = new ArrayList<>();
        ArrayList<Pose2d> xAcceptedPoseList  = new ArrayList<>();;
        Pose2d robotPose = drivebase.getPose();
        for (int i = 0; i <= nodeList.size() - 1; i++) {
            double x = nodeList.get(i).getX();
            Pose2d activePose = nodeList.get(i);
            if ((x - robotPose.getX()) * (x - pose.getX()) > 0) {
                if (!xAcceptedDistanceList.contains((double)Math.round(x*10)/10)) {
                    xAcceptedDistanceList.add(x);
                    xAcceptedPoseList.add(activePose);
                } else {
                    if(activePose.getTranslation().getDistance(robotPose.getTranslation()) > xAcceptedPoseList.get(xAcceptedDistanceList.indexOf((double)Math.round(x*10)/10)).getTranslation().getDistance(robotPose.getTranslation())){
                    int index = xAcceptedDistanceList.indexOf((double)Math.round(x*10)/10);
                    xAcceptedDistanceList.add(index,(double)Math.round(x*10)/10);
                    xAcceptedPoseList.add(index,activePose);
                    }
                }
            }
        }
        ArrayList<Pose2d> path = new ArrayList<>();
        for (int i = 0; i <= xAcceptedPoseList.size() - 1; i++) {
            Pose2d activePose = nodeList.get(i);
            if (xAcceptedPoseList.contains(activePose)) {
                int q =0;
                while (activePose.getTranslation().getDistance(robotPose.getTranslation()) > path.get(q).getTranslation()
                        .getDistance(robotPose.getTranslation())) {
                    q++;
                }
                path.add(q,activePose);
            }
        }
        path.add(pose);
        targetPose = path.get(0);
        alignCommand.schedule();
    }
}
