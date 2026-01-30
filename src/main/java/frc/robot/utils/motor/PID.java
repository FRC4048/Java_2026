package frc.robot.utils.motor;

public class PID {
    private final double p;
    private final double i;
    private final double d;

    public PID(double p, double i, double d) {
        this.p = p;
        this.i = i;
        this.d = d;
    }

    public double getP() {
        return p;
    }

    public double getI() {
        return i;
    }

    public double getD() {
        return d;
    }

    public static PID of(double p, double i, double d) {
        return new PID(p, i, d);
    }
}