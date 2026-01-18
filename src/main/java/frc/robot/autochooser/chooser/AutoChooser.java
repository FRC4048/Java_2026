package frc.robot.autochooser.chooser;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.autochooser.event.AutoEvent;

/** interface for taking in a {@link AutoEvent} and returning the corresponding {@link Command} */
public interface AutoChooser {
  /**
   * @return Command that corresponds to the selected {@link AutoEvent} from the {@link
   *     frc.robot.autochooser.event.AutoEventProvider}
   */
  Command getAutoCommand();

  Pose2d getStartingPosition();
}
