package io.github.ftcphantom16950.phantomlib.utils.regulators;

public class FeedForwardController {
    private double kV, kA, output, target;

    public void setTarget(double target) {
        this.target = target;
    }

    public FeedForwardController(double kV, double kA) {
        this.kV = kV;
        this.kA = kA;
    }

    public double getkV() {
        return kV;
    }

    public void setkV(double kV) {
        this.kV = kV;
    }

    public double getkA() {
        return kA;
    }

    public void setkA(double kA) {
        this.kA = kA;
    }

    public double calculate(){
        output = kV * target + kA;
        return output;
    }
}
