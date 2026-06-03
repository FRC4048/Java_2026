package frc.robot.autochooser;

public enum AutoPath {
    DO_NOTHING("Do nothing"),
    SINGLE_SWIPE_DEPOT("Single swipe depot"),
    SINGLE_SWIPE_OUTPOST("Single Swipe outpost"),
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
