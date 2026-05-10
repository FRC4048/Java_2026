package frc.robot.autochooser;

public enum AutoPath {
    DO_NOTHING("Do nothing"),
    SWIPE("Swipe");

    private final String name;
    private AutoPath(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
}
