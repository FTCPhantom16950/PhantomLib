package io.github.ftcphantom16950.phantomlib.utils.regulators;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PIDFilteredController {
    double output = 0;
    private double I, integralSum, P, D, derivative;
    private double kP;
    private double kD;
    private double kI;
    private double motorVelocity;
    private double currentError;
    private double previousError;
    private double target;
    private ElapsedTime timer;
    private double derivativeFilter;

    public PIDFilteredController(double kI, double kD, double kP, double derivativeFilter) {
        this.kI = kI;
        this.kD = kD;
        this.kP = kP;
        this.derivativeFilter = derivativeFilter;
        timer = new ElapsedTime();
    }

    public double getDerivativeFilter() {
        return derivativeFilter;
    }

    public void setDerivativeFilter(double derivativeFilter) {
        this.derivativeFilter = derivativeFilter;
    }

    public double getCurrentError() {
        return currentError;
    }

    public double getMotorVelocity() {
        return motorVelocity;
    }

    public void setMotorVelocity(double motorVelocity) {
        this.motorVelocity = motorVelocity;
    }

    public double getTarget() {
        return target;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public void setkP(double kP) {
        this.kP = kP;
    }

    public void setkD(double kD) {
        this.kD = kD;
    }

    public void setkI(double kI) {
        this.kI = kI;
    }

    public double calculate() {
        timer.reset();
        currentError = (target - motorVelocity);
        P = kP * currentError;
        integralSum += currentError * timer.seconds();
        if (integralSum >= 1000) {
            integralSum = 1000;
        } else if (integralSum <= -1000) {
            integralSum = -1000;
        }
        I = integralSum * kI;
        derivative = derivativeFilter * previousError + (1 - derivativeFilter) * currentError;
        D = derivative * kD;
        output = P + I + D;

        previousError = currentError;
        return output;
    }
}
