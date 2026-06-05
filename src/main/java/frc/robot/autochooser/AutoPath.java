package frc.robot.autochooser;

public enum AutoPath {
    DO_NOTHING("do nothing"),
    SINGLE_SWIPE_DEPOT("depot side, single swipe"),
    SINGLE_SWIPE_OUTPOST("outpost side, single swipe"),
    MID_DEPOT("mid, depot"),
    DIP_AND_DOT_DEPOT("depot side, dip and dot"),
    DIP_AND_DOT_OUTPOST("outpost side, dip and dot"),
    TEST("test");

    private final String name;
    private AutoPath(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
}
