// Copyright (c) 2021-2026 Littleton Robotics
// http://github.com/Mechanical-Advantage
//
// Use of this source code is governed by a BSD
// license that can be found in the LICENSE file
// at the root directory of this project.

package frc.robot.autochooser;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.autochooser.event.AutoEvent;
import frc.robot.utils.logging.commands.LoggableCommand;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.inputs.LoggableInputs;
import org.littletonrobotics.junction.networktables.LoggedNetworkInput;

/**
 * Manages a chooser value published to the "SmartDashboard" table of NT.
 *
 * @param <LoggableCommand> The value type associated with each string key.
 */
public class LoggedDashboardChooser extends LoggedNetworkInput {
  private final String key;
  private AutoEvent selectedValue = null;
  private AutoEvent previousValue = null;
  private SendableChooser<AutoEvent> sendableChooser = new SendableChooser<>();
  private Map<AutoEvent, LoggableCommand> options = new HashMap<>();
  private Consumer<LoggableCommand> listener = null;

  private final LoggableInputs inputs =
      new LoggableInputs() {
        public void toLog(LogTable table) {
          table.put(key, selectedValue.toString());
        }

        public void fromLog(LogTable table) {
          selectedValue = AutoEvent.fromString(table.get(key.toString(), selectedValue.toString()));
        }
      };

  /**
   * Creates a new LoggedDashboardChooser, for handling a chooser input sent via NetworkTables.
   *
   * @param key The key for the chooser, published to "/SmartDashboard/{key}" for NT or
   *     "/DashboardInputs/SmartDashboard/{key}" when logged.
   */
  public LoggedDashboardChooser(String key) {
    this.key = key;
    SmartDashboard.putData(key.toString(), sendableChooser);
    periodic();
    Logger.registerDashboardInput(this);
  }

  /**
   * Creates a new LoggedDashboardChooser, for handling a chooser input sent via NetworkTables. This
   * constructor copies the options from a SendableChooser. Note that updates to the original
   * SendableChooser will not affect this object.
   *
   * @param key The key for the chooser, published to "/SmartDashboard/{key}" for NT or
   *     "/DashboardInputs/{key}" when logged.
   * @param chooser The existing SendableChooser object.
   */
  @SuppressWarnings("unchecked")
  public LoggedDashboardChooser(String key, SendableChooser<LoggableCommand> chooser) {
    this(key);

    // Get options map
    Map<AutoEvent, LoggableCommand> options = new HashMap<>();
    try {
      Field mapField = SendableChooser.class.getDeclaredField("m_map");
      mapField.setAccessible(true);
      options = (Map<AutoEvent, LoggableCommand>) mapField.get(chooser);
    } catch (NoSuchFieldException
        | SecurityException
        | IllegalArgumentException
        | IllegalAccessException e) {
      e.printStackTrace();
    }

    // Get default option
    String defaultString = "";
    try {
      Field defaultField = SendableChooser.class.getDeclaredField("m_defaultChoice");
      defaultField.setAccessible(true);
      defaultString = (String) defaultField.get(chooser);
    } catch (NoSuchFieldException
        | SecurityException
        | IllegalArgumentException
        | IllegalAccessException e) {
      e.printStackTrace();
    }

    // Add options
    for (AutoEvent optionKey : options.keySet()) {
      if (optionKey.toString().equals(defaultString)) {
        addDefaultOption(optionKey, options.get(optionKey));
      } else {
        addOption(optionKey, options.get(optionKey));
      }
    }
  }

  /**
   * Adds a new option to the chooser.
   *
   * @param key The string key for the option.
   * @param value The value of the option.
   */
  public void addOption(AutoEvent key, LoggableCommand value) {
    sendableChooser.addOption(key.toString(), key);
    options.put(key, value);
  }

  /**
   * Adds a new option to the chooser and sets it to the default.
   *
   * @param key The string key for the option.
   * @param value The value of the option.
   */
  public void addDefaultOption(AutoEvent key, LoggableCommand value) {
    sendableChooser.setDefaultOption(key.toString(), key);
    options.put(key, value);
  }

  /**
   * Returns the selected option. If there is none selected, it will return the default. If there is
   * none selected and no default, then it will return {@code null}.
   *
   * @return The value for the selected option.
   */
  public LoggableCommand get() {
    return options.get(selectedValue);
  }

  /**
   * Binds the callback to run whenever the selected option changes. There can only be one listener,
   * and this method overrites it with each invokation.
   *
   * @param listener The function to call that accepts the new value.
   */
  public void onChange(Consumer<LoggableCommand> listener) {
    this.listener = listener;
  }

  /**
   * Returns the internal SendableChooser object, for use when setting up dashboard layouts. Do not
   * read data from the SendableChooser directly.
   *
   * @return The internal SendableChooser object.
   */
  public SendableChooser<AutoEvent> getSendableChooser() {
    return sendableChooser;
  }

  public void periodic() {
    if (!Logger.hasReplaySource()) {
      selectedValue = sendableChooser.getSelected();
    }
    Logger.processInputs(prefix + "/SmartDashboard", inputs);
    if (previousValue != selectedValue) {
      if (listener != null) listener.accept(get());
      previousValue = selectedValue;
    }
  }
}
