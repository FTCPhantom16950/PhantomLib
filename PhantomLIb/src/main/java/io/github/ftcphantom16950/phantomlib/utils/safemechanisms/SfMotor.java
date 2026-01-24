package io.github.ftcphantom16950.phantomlib.utils.safemechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class SfMotor {
    private final DcMotorEx dcMotorEx;
    private final Object lock = new Object();
    private volatile double lastPower = 0.0, lastPosition = 0.0;
    private final int encoderResolution;
    public SfMotor(DcMotorEx dcMotorEx, int encoderResolution) {
        this.dcMotorEx = dcMotorEx;
        this.encoderResolution = encoderResolution;
    }

    public double getPower() {
        return lastPower;
    }

    public void setPower(double power) {
        synchronized (lock) {
            dcMotorEx.setPower(power);
            lastPower = power;
        }
    }

    public void setZeroPowerBehaviour(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
        synchronized (lock) {
            dcMotorEx.setZeroPowerBehavior(zeroPowerBehavior);
        }
    }

    public void setMode(DcMotor.RunMode mode) {
        synchronized (lock) {
            dcMotorEx.setMode(mode);
        }
    }

    public void setDirection(DcMotor.Direction direction) {
        synchronized (lock) {
            dcMotorEx.setDirection(direction);
        }
    }

    public int getTargetPosition() {
        return dcMotorEx.getTargetPosition();
    }

    public void setTargetPosition(int targetPosition) {
        dcMotorEx.setTargetPosition(targetPosition);
    }

    public int getCurrentPosition() {
        return dcMotorEx.getCurrentPosition();
    }
    public double getVelocity(){
        return dcMotorEx.getVelocity() * 60 / encoderResolution;
    }
    public void setVelocity(double velocity){
        synchronized (lock){
            dcMotorEx.setVelocity(velocity);
        }
    }


}
