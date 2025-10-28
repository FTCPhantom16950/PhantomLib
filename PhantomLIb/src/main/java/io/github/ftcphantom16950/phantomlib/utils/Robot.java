package io.github.ftcphantom16950.phantomlib.utils;


import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Класс хранящий основные переменные OpMode, хранящиеся статично
 */
public class Robot {
    /// Используемый {@link PhantomOpMode }
    public static PhantomOpMode opMode;
    /// Используемый HardwareMap
    public static HardwareMap hw;
    /// Используемый Telemetry
    public static Telemetry telemetry;
    /// Используемый геймпад(gamepad1)
    public static Gamepad gamepadDriver,
    /// Используемый геймпад (gamepad2)
            gamepadOperator;
}
