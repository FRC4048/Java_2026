package frc.robot.constants.enums;

public enum Trench {

        // These are not exact trench boundaries. These trench zones
        // include some space around the trench boundaries.
        RED_BOTTOM_LOWER(10,0),
        RED_BOTTOM_HIGHER(14,1.5),
        RED_TOP_LOWER(10,6.5),
        RED_TOP_HIGHER(14,8),
        BLUE_BOTTOM_LOWER(2.5,0),
        BLUE_BOTTOM_HIGHER(6.5,1.5),
        BLUE_TOP_LOWER(2.5,6.5),
        BLUE_TOP_HIGHER(6.5,8);

        private double x;
        private double y;

        private Trench(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
        return x;
        }

        public double getY() {
        return y;
        } 
    
}
