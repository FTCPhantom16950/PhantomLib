package io.github.ftcphantom16950.phantomlib.utils.Regulators;


import com.qualcomm.robotcore.util.ElapsedTime;

public class PIDFController {
    double output = 0;
    private double F, I, integralSum, P, D, derivative;
    private double kP;
    private double kD;
    private double kI;
    private double kF;
    private double motorVelocity;
    private double currentError;
    private double previousError;
    private double target;
    private ElapsedTime timer;
    private double derivativeFilter;
    private double targetIntegerSum;

    public double getTargetIntegerSum() {
        return targetIntegerSum;
    }

    public void setTargetIntegerSum(double targetIntegerSum) {
        this.targetIntegerSum = targetIntegerSum;
    }

    public double getDerivativeFilter() {
        return derivativeFilter;
    }

    public void setDerivativeFilter(double derivativeFilter) {
        this.derivativeFilter = derivativeFilter;
    }

    public PIDFController(double kF, double kI, double kD, double kP) {
        this.kF = kF;
        this.kI = kI;
        this.kD = kD;
        this.kP = kP;
        timer = new ElapsedTime();
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

    public void setkF(double kF) {
        this.kF = kF;
    }

    public double calculate() {
        timer.reset();
        currentError = (target - motorVelocity);
        F = target * kF;
        P = kP * currentError;
        integralSum += currentError * timer.seconds();
        if (integralSum >= targetIntegerSum) {
            integralSum = targetIntegerSum;
        } else if (integralSum <= -targetIntegerSum) {
            integralSum = -targetIntegerSum;
        }
        I = integralSum * kI;
        derivative = derivativeFilter * previousError + (1 - derivativeFilter) * currentError;
        D = derivative * kD;
        output = P + I + D + F;

        previousError = currentError;
        return output;
    }
}
