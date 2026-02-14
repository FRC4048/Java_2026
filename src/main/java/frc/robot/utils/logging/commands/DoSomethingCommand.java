package frc.robot.utils.logging.commands;

public class DoSomethingCommand extends DoNothingCommand {

    private String message;

    public DoSomethingCommand(String message) {
        this.message = message;
    }

    @Override
    public void execute() {
        System.out.println(message);
    }
    
}
