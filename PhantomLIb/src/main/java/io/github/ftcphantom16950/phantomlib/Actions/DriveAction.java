package io.github.ftcphantom16950.phantomlib.Actions;


import static io.github.ftcphantom16950.phantomlib.utils.PhantomMath.makeLinearToCubic;
import static io.github.ftcphantom16950.phantomlib.utils.Robot.gamepadDriver;



import com.qualcomm.robotcore.hardware.DcMotorEx;

import io.github.ftcphantom16950.phantomlib.Mechanisms.DriveMechanism;
import io.github.ftcphantom16950.phantomlib.utils.Action.Action;
import io.github.ftcphantom16950.phantomlib.utils.PhantomOpMode;
import io.github.ftcphantom16950.phantomlib.utils.Robot;

public class DriveAction extends Action {
    PhantomOpMode opMode;

    public DriveAction(PhantomOpMode OpMode) {
        this.opMode = OpMode;
    }

    double backRightPower, frontRightPower, backLeftPower, frontLeftPower, denominator;
    private static double x, y, rot;
    Thread thread = new Thread(() -> {
        while (opMode.opModeIsActive()) {
            x = 1.1 * gamepadDriver.left_stick_x + 1.1 * 0.6 * gamepadDriver.right_stick_x;
            y = -gamepadDriver.left_stick_y - 0.6 * gamepadDriver.right_stick_y;
            rot = gamepadDriver.left_trigger - gamepadDriver.right_trigger;
            x = makeLinearToCubic(x);
            y = makeLinearToCubic(y);
            rot = makeLinearToCubic(rot);
            if (-0.1 < x && x < 0.1) {
                x = 0;
            }
            if (-0.1 < y && y < 0.1) {
                x = 0;
            }
            if (-0.1 < rot && rot < 0.1) {
                x = 0;
            }
            if (gamepadDriver.right_bumper) {
                rot = -0.3;
            } else if (gamepadDriver.left_bumper) {
                rot = -0.3;
            }
        }
    });
    Thread motorPower = new Thread(() -> {
        while (opMode.opModeIsActive()) {
            Robot.get(DriveMechanism.motorNames[0], DcMotorEx.class).setPower(makeLinearToCubic(backLeftPower));
            Robot.get(DriveMechanism.motorNames[1], DcMotorEx.class).setPower(makeLinearToCubic(frontLeftPower));
            Robot.get(DriveMechanism.motorNames[3], DcMotorEx.class).setPower(makeLinearToCubic(frontRightPower));
            Robot.get(DriveMechanism.motorNames[2], DcMotorEx.class).setPower(makeLinearToCubic(backRightPower));
        }
    });

    @Override
    public void execute() {
        while (opMode.opModeIsActive()) {
            if (!thread.isAlive()) {
                thread.start();
            }
            denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rot), 1);
            frontLeftPower = (y + x + rot) / denominator;
            backLeftPower = (y - x + rot) / denominator;
            frontRightPower = (y - x - rot) / denominator;
            backRightPower = (y + x - rot) / denominator;
            if (!motorPower.isAlive()) {
                motorPower.start();
            }
        }
    }
}
