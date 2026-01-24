package io.github.ftcphantom16950.phantomlib.mechanisms;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.IMU;

import io.github.ftcphantom16950.phantomlib.utils.Mechanism;
import io.github.ftcphantom16950.phantomlib.utils.Robot;


public class GyroScopeMechanism implements Mechanism {
    @Override
    public void init() throws InterruptedException {
        IMU imu = Robot.INSTANCE.hw.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(new RevHubOrientationOnRobot(RevHubOrientationOnRobot.LogoFacingDirection.UP, RevHubOrientationOnRobot.UsbFacingDirection.RIGHT)));
        Robot.INSTANCE.imu = imu;
    }

    @Override
    public void read() {
        Mechanism.super.read();
        Robot.INSTANCE.rot = Robot.INSTANCE.imu.getRobotYawPitchRollAngles().getYaw();
    }
}
