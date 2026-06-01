package frc.robot.autochooser;

public enum AutoPath {
    DO_NOTHING("Do nothing"),
    SWIPE_DEPOT("Swipe depot"),
    SWIPE_OUTPOST("Swipe outpost"),
    SWIPE_DEPOT_DOT("Swipe Depot Dot"),
    SWIPE_OUTPOST_DOT("Swipe Outpost Dot"),
    MID_DEPOT("Mid Depot"),
    TEST("Test"),
    BIG_DOT_OUTPOST("Big Dot Outpost"),
    BIG_DOT_DEPOT("Big Dot Depot");

    private final String name;
    private AutoPath(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
}
