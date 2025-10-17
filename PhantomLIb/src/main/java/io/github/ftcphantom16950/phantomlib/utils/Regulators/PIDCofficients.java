package io.github.ftcphantom16950.phantomlib.utils.Regulators;

public class PIDCofficients {
    private double kP,kI, kD;

    public PIDCofficients(double kP, double kI, double kD) {
        this.kP = kP;
        this.kI = kI;
        this.kD = kD;
    }

    public double kP() {
        return kP;
    }

    public void setkP(double kP) {
        this.kP = kP;
    }

    public double kI() {
        return kI;
    }

    public void setkI(double kI) {
        this.kI = kI;
    }

    public double kD() {
        return kD;
    }

    public void setkD(double kD) {
        this.kD = kD;
    }
}
