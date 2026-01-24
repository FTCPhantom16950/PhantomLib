package io.github.ftcphantom16950.phantomlib.utils.safemechanisms;

import com.qualcomm.robotcore.hardware.Servo;

public class SfServo {
    private final Servo servo;
    private final Object lock = new Object();
    private volatile double lastPosition = 0.0;
    public SfServo(Servo servo) {
        this.servo = servo;
    }

    public double getPosition() {
        return lastPosition;
    }

    public void setPosition(double position) {
        synchronized (lock) {
            servo.setPosition(position);
            lastPosition = position;
        }
    }


}
