package io.github.ftcphantom16950.phantomlib.Mechanisms;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import static io.github.ftcphantom16950.phantomlib.utils.Robot.*;

import io.github.ftcphantom16950.phantomlib.utils.Robot;

public class DriveMechanism {
    private static DcMotorEx lb, lf,rb,rf;
    private DriveMechanism() {
    }
    public static String[] motorNames = new String[]{"lf", "lb", "rf", "rb"};
    public static void setLeftBackMotor(String name) {
        lb = hw.get(DcMotorEx.class, name);
        Robot.addOrUpdate(name, lb);
        motorNames[0] = name;
    }

    public static void setLeftFrontMotor(String name) {
        lf = hw.get(DcMotorEx.class, name);
        Robot.addOrUpdate(name, lf);
        motorNames[1] = name;
    }

    public static void setRightBackMotor(String name) {
        rb = hw.get(DcMotorEx.class, name);
        Robot.addOrUpdate(name, rb);
        motorNames[2] = name;
    }

    public static void setRightFrontMotor(String name) {
        rf = hw.get(DcMotorEx.class, name);
        Robot.addOrUpdate(name, rf);
        motorNames[3] = name;
    }

}