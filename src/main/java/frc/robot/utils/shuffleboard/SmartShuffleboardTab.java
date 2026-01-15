/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.utils.shuffleboard;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.wpilibj.shuffleboard.*;
import edu.wpi.first.wpilibj2.command.Command;

import java.util.*;

public class SmartShuffleboardTab {
  private final Map<String, SimpleWidget> widgetMap = new HashMap<>();
  private final Set<String> commandSet = new HashSet<>();
  private final ShuffleboardTab tab;

  SmartShuffleboardTab(String tabName) {
    tab = Shuffleboard.getTab(tabName);
  }

  /**
   * @param fieldName name of field shown on shuffleboard
   * @return the handle for the widget on shuffleboard
   */
  public SimpleWidget getWidget(String fieldName) {
    return widgetMap.get(fieldName);
  }

  /**
   * @param layoutName name of layout on shuffleboard
   * @return the handle of the layout
   */
  public ShuffleboardLayout getLayout(String layoutName) {
    try {
      return tab.getLayout(layoutName);
    } catch (Exception noSuchElementException) {
      return null;
    }
  }

  public SimpleWidget put(String fieldName, Object value) {
    SimpleWidget widget = widgetMap.get(fieldName);
    if (widget != null) {
      GenericEntry ntEntry = widget.getEntry();
      ntEntry.setValue(value);
    } else {
      widget = tab.add(fieldName, value).withWidget(BuiltInWidgets.kNumberSlider);
      widgetMap.put(fieldName, widget);
    }
    return widget;
  }

  public SimpleWidget put(String fieldName, String layoutName, Object value) {
    ShuffleboardLayout layout;
    try {
      layout = tab.getLayout(layoutName);
    } catch (NoSuchElementException ex) {
      layout = tab.getLayout(layoutName, BuiltInLayouts.kList);
      layout.withSize(2, 4).withProperties(Map.of("Label position", "LEFT"));
    }

    SimpleWidget widget = widgetMap.get(fieldName);
    if (widget != null) {
      GenericEntry ntEntry = widget.getEntry();
      ntEntry.setValue(value);
    } else {
      widget = layout.add(fieldName, value);
      widgetMap.put(fieldName, widget);
    }
    return widget;
  }

  public boolean getBoolean(String fieldName, boolean defaultValue) {
    SimpleWidget widget = widgetMap.get(fieldName);
    if (widget == null) {
      return defaultValue;
    } else {
      return widget.getEntry().getBoolean(defaultValue);
    }
  }

  public double getDouble(String fieldName, double defaultValue) {
    SimpleWidget widget = widgetMap.get(fieldName);
    if (widget == null) {
      return defaultValue;
    } else {
      return widget.getEntry().getDouble(defaultValue);
    }
  }

  public String getString(String fieldName, String defaultValue) {
    SimpleWidget widget = widgetMap.get(fieldName);
    if (widget == null) {
      return defaultValue;
    } else {
      return widget.getEntry().getString(defaultValue);
    }
  }

  public NetworkTableValue getValue(String fieldName) {
    SimpleWidget widget = widgetMap.get(fieldName);
    if (widget == null) {
      return null;
    } else {
      return widget.getEntry().get();
    }
  }

  /**
   * @param fieldName name of the field that represents the command on shuffleboard
   * @param cmd the command to execute
   */
  public void putCommand(String fieldName, Command cmd) {
    if (!commandSet.contains(fieldName)) {
      // getting the layout, or creating it if it doesn't exist
      ShuffleboardLayout layout;
      try {
        layout = tab.getLayout("Commands");
      } catch (NoSuchElementException ex) {
        layout = tab.getLayout("Commands", BuiltInLayouts.kList);
        layout.withSize(2, 4).withProperties(Map.of("Label position", "LEFT"));
      }

      // adding the command to the tab
      layout.add(fieldName, cmd).withWidget(BuiltInWidgets.kCommand);
      commandSet.add(fieldName);
    }
  }
}
