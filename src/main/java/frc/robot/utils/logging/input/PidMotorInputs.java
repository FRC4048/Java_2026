package frc.robot.utils.logging.subsystem.inputs;

import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.utils.logging.subsystem.builders.PidMotorInputBuilder;
import frc.robot.utils.logging.subsystem.providers.InputProvider;
import frc.robot.utils.logging.subsystem.providers.PidMotorInputProvider;
import org.littletonrobotics.junction.LogTable;

/** Contains Inputs that could be logged for a Motor with a Pid */
public class PidMotorInputs extends MotorInputs {

  private Double pidSetpoint;

  public PidMotorInputs(PidMotorInputBuilder<?> builder) {
    super(builder);
    this.pidSetpoint = builder.isLogPidSetpoint() ? 0.0 : null;
  }

  @Override
  public void toLog(LogTable table) {
    super.toLog(table);
    if (pidSetpoint != null) {
      table.put("pidSetpoint", pidSetpoint);
    }
  }

  @Override
  public void fromLog(LogTable table) {
    super.fromLog(table);
    if (pidSetpoint != null) {
      pidSetpoint = table.get("pidSetpoint", pidSetpoint);
    }
  }

  @Override
  public boolean process(InputProvider inputProvider) {
    boolean success = super.process(inputProvider);

    if (success && inputProvider instanceof PidMotorInputProvider pidMotorinputProvider) {
      if (pidSetpoint != null) {
        pidSetpoint = pidMotorinputProvider.getPidSetpoint();
      }
      return true;
    } else {
      DriverStation.reportError("inputProvider must be of type PidMotorinputProvider", true);
      return false;
    }
  }

  public Double getPidSetpoint() {
    return pidSetpoint;
  }
}
