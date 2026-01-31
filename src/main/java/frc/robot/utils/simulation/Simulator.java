package frc.robot.utils.simulation;

public interface Simulator {
    void stepSimulation();
    default void setTargetPosition(double rotations){
    }
}
