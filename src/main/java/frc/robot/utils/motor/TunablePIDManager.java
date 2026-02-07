// Until I see ohad's version cant really fix this (I think)

package frc.robot.utils.motor;

import frc.robot.utils.logging.LoggedTunableNumber;
import frc.robot.utils.logging.io.pidmotor.SparkMaxPidConfig;
import frc.robot.utils.logging.io.pidmotor.SparkMaxPidMotorIo;

public class TunablePIDManager {
    private final SparkMaxPidMotorIo io;
    private final SparkMaxPidConfig initConfig;
    private final String prefix;
    private final LoggedTunableNumber kPTunable;
    private final LoggedTunableNumber kITunable;
    private final LoggedTunableNumber kDTunable;
    private final LoggedTunableNumber kIZoneTunable;
    private final LoggedTunableNumber kFFTunable;
    private final LoggedTunableNumber kMaxVelTunable;
    private final LoggedTunableNumber kMaxAccTunable;
    private final LoggedTunableNumber kAllowedErrorTunable;

    public TunablePIDManager(String prefix, SparkMaxPidMotorIo io, SparkMaxPidConfig initConfig) {
        this.io = io;
        this.initConfig = initConfig;
        this.prefix = prefix;
        kPTunable = new LoggedTunableNumber(prefix + "/PID_P", initConfig.getP());
        kITunable = new LoggedTunableNumber(prefix + "/PID_I", initConfig.getI());
        kDTunable = new LoggedTunableNumber(prefix + "/PID_D", initConfig.getD());
        kIZoneTunable = new LoggedTunableNumber(prefix + "/PID_I_ZONE", initConfig.getIZone());
        kFFTunable = new LoggedTunableNumber(prefix + "/PID_FF", initConfig.getFF());
        kMaxVelTunable = new LoggedTunableNumber(prefix + "/PID_MAX_VEL", initConfig.getMaxVelocity());
        kMaxAccTunable = new LoggedTunableNumber(prefix + "/PID_MAX_ACCEL", initConfig.getMaxAccel());
        kAllowedErrorTunable =
                new LoggedTunableNumber(prefix + "/PID_ALLOWED_ERROR", initConfig.getAllowedError());
    }

    public void periodic() {
        LoggedTunableNumber.ifChanged(
                hashCode(),
                () ->
                        io.configurePID(
                                new SparkMaxPidConfig(initConfig.getUsesMaxMotion())
                                        .setP(kPTunable.get())
                                        .setI(kITunable.get())
                                        .setD(kDTunable.get())
                                        .setIZone(kIZoneTunable.get())
                                        .setFF(kFFTunable.get())
                                        .setMaxVelocity(kMaxVelTunable.get())
                                        .setMaxAccel(kMaxAccTunable.get())
                                        .setAllowedError(kAllowedErrorTunable.get())),
                kPTunable,
                kITunable,
                kDTunable,
                kIZoneTunable,
                kFFTunable,
                kMaxVelTunable,
                kMaxAccTunable,
                kAllowedErrorTunable);
    }
}