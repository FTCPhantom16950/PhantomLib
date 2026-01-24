package io.github.ftcphantom16950.phantomlib.utils.regulators;

public class FullRegulator {
    private double kV;
    private double kA;
    private double output;
    private double target;

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

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    public double getTarget() {
        return target;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public double getMotorVelocity() {
        return motorVelocity;
    }

    public void setMotorVelocity(double motorVelocity) {
        this.motorVelocity = motorVelocity;
    }

    public double getDerivativeFilter() {
        return derivativeFilter;
    }

    public void setDerivativeFilter(double derivativeFilter) {
        this.derivativeFilter = derivativeFilter;
    }

    public double getkP() {
        return kP;
    }

    public void setkP(double kP) {
        this.kP = kP;
    }

    public double getkD() {
        return kD;
    }

    public void setkD(double kD) {
        this.kD = kD;
    }

    public double getkI() {
        return kI;
    }

    public void setkI(double kI) {
        this.kI = kI;
    }

    private double motorVelocity;
    private double derivativeFilter;
    private double kP;
    private double kD;

    public FullRegulator(double kV, double kA, double kP, double kD, double kI, double derivativeFilter, double output, double target, double motorVelocity) {
        this.kV = kV;
        this.kA = kA;
        this.kP = kP;
        this.kD = kD;
        this.kI = kI;
        this.derivativeFilter = derivativeFilter;
        this.output = output;
        this.target = target;
        this.motorVelocity = motorVelocity;
        pidFilteredController = new PIDFilteredController(kI,kD,kP,derivativeFilter);
        feedForwardController = new FeedForwardController(kV,kA);
    }

    private double kI;

    private final PIDFilteredController pidFilteredController;
    private final FeedForwardController feedForwardController;
    public double calculate(){
        pidFilteredController.setTarget(target);
        feedForwardController.setTarget(target);
        pidFilteredController.setMotorVelocity(motorVelocity);
        feedForwardController.setkV(kV);
        feedForwardController.setkA(kA);
        pidFilteredController.setkP(kP);
        pidFilteredController.setkD(kD);
        pidFilteredController.setkI(kI);
        pidFilteredController.setDerivativeFilter(derivativeFilter);
        output = feedForwardController.calculate() + pidFilteredController.calculate();
        return output;
    }
}
