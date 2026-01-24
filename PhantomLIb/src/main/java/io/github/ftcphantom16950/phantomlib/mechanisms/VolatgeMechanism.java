package io.github.ftcphantom16950.phantomlib.mechanisms;

import com.qualcomm.robotcore.hardware.VoltageSensor;

import io.github.ftcphantom16950.phantomlib.utils.Mechanism;
import io.github.ftcphantom16950.phantomlib.utils.Robot;


public class VolatgeMechanism implements Mechanism {
    @Override
    public void init() throws InterruptedException {
        VoltageSensor voltageSensor = Robot.INSTANCE.hw.voltageSensor.iterator().next();
        Robot.INSTANCE.addOrUpdate(voltageSensor, "vltg");
    }

    @Override
    public void read() {
        Mechanism.super.read();
        Robot.INSTANCE.voltage = Robot.INSTANCE.voltageSensor.getVoltage();
        Robot.addData("voltage", Robot.INSTANCE.voltage);
    }
}
