package io.github.ftcphantom16950.phantomlib.utils;


import com.qualcomm.robotcore.hardware.AnalogSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.HashMap;
import java.util.Map;

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

    private static final Map<String, Object> customObjects = new HashMap<>();

    /**
     * Добавляет или обновляет кастомный объект в статичном хранилище
     * @param key    ключ для доступа к объекту
     * @param object объект для хранения
     */
    public static void addOrUpdate(String key, Object object) {
        customObjects.put(key, object);
    }


    public static <T> T get(String name, Class<T> type) {
        return type.cast(customObjects.get(name));
    }

}
