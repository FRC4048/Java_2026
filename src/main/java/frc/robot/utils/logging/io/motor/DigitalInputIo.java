package frc.robot.utils.logging.io.motor;

import frc.robot.utils.logging.io.BaseIo;

public interface DigitalInputIo extends BaseIo {
   
    boolean isPressed();
}