package frc.robot.utils.logging.input;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public class GyroInputs implements LoggableInputs {

    private GyroValues values;

    public GyroValues getGyroValues() {
        return values;
    }

    @Override
    public void toLog(LogTable table) {
        table.put("anglesInDeg", values.getAnglesInDeg());
        table.put("angleOffset", values.getAngleOffset());
        table.put("worldLinearAccelX", values.getWorldLinearAccelX());
        table.put("worldLinearAccelY", values.getWorldLinearAccelY());
        table.put("worldLinearAccelZ", values.getWorldLinearAccelZ());
        table.put("velocityX", values.getVelocityX());
        table.put("velocityY", values.getVelocityY());
        table.put("velocityZ", values.getVelocityZ());
        table.put("displacementX", values.getDisplacementX());
        table.put("displacementY", values.getDisplacementY());
        table.put("displacementZ", values.getDisplacementZ());
        table.put("rawGyroX", values.getRawGyroX());
        table.put("rawGyroY", values.getRawGyroY());
        table.put("rawGyroZ", values.getRawGyroZ());
        table.put("rawAccelX", values.getRawAccelX());
        table.put("rawAccelY", values.getRawAccelY());
        table.put("rawAccelZ", values.getRawAccelZ());
        table.put("rawMagX", values.getRawMagX());
        table.put("rawMagY", values.getRawMagY());
        table.put("rawMagZ", values.getRawMagZ());
        table.put("yaw", values.getYaw());
        table.put("pitch", values.getPitch());
        table.put("roll", values.getRoll());
        table.put("fusedHeading", values.getFusedHeading());
        table.put("compassHeading", values.getCompassHeading());
        table.put("robotCentricVelocityX", values.getRobotCentricVelocityX());
        table.put("robotCentricVelocityY", values.getRobotCentricVelocityY());
        table.put("robotCentricVelocityZ", values.getRobotCentricVelocityZ());
    }

    @Override
    public void fromLog(LogTable table) {
        values.setAnglesInDeg(table.get("anglesInDeg", values.getAnglesInDeg()));
        values.setAngleOffset(table.get("angleOffset", values.getAngleOffset()));
        values.setWorldLinearAccelX(table.get("worldLinearAccelX", values.getWorldLinearAccelX()));
        values.setWorldLinearAccelY(table.get("worldLinearAccelY", values.getWorldLinearAccelY()));
        values.setWorldLinearAccelZ(table.get("worldLinearAccelZ", values.getWorldLinearAccelZ()));
        values.setVelocityX(table.get("velocityX", values.getVelocityX()));
        values.setVelocityY(table.get("velocityY", values.getVelocityY()));
        values.setVelocityZ(table.get("velocityZ", values.getVelocityZ()));
        values.setDisplacementX(table.get("displacementX", values.getDisplacementX()));
        values.setDisplacementY(table.get("displacementY", values.getDisplacementY()));
        values.setDisplacementZ(table.get("displacementZ", values.getDisplacementZ()));
        values.setRawGyroX(table.get("rawGyroX", values.getRawGyroX()));
        values.setRawGyroY(table.get("rawGyroY", values.getRawGyroY()));
        values.setRawGyroZ(table.get("rawGyroZ", values.getRawGyroZ()));
        values.setRawAccelX(table.get("rawAccelX", values.getRawAccelX()));
        values.setRawAccelY(table.get("rawAccelY", values.getRawAccelY()));
        values.setRawAccelZ(table.get("rawAccelZ", values.getRawAccelZ()));
        values.setRawMagX(table.get("rawMagX", values.getRawMagX()));
        values.setRawMagY(table.get("rawMagY", values.getRawMagY()));
        values.setRawMagZ(table.get("rawMagZ", values.getRawMagZ()));
        values.setYaw(table.get("yaw", values.getYaw()));
        values.setPitch(table.get("pitch", values.getPitch()));
        values.setRoll(table.get("roll", values.getRoll()));
        values.setCompassHeading(table.get("compassHeading", values.getCompassHeading()));
        values.setFusedHeading(table.get("fusedHeading", values.getFusedHeading()));
        values.setRobotCentricVelocityX(table.get("robotCentricVelocityX", values.getRobotCentricVelocityX()));
        values.setRobotCentricVelocityY(table.get("robotCentricVelocityY", values.getRobotCentricVelocityY()));
        values.setRobotCentricVelocityZ(table.get("robotCentricVelocityZ", values.getRobotCentricVelocityZ()));
    }

    public void fromHardware(GyroValues values) {
        this.values = values;
    }
}
