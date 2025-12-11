package io.github.ftcphantom16950.phantomlib.utils;


import android.content.Context;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.hardware.AnalogSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс хранящий основные переменные OpMode, хранящиеся статично
 */
public class Robot {
    public static IMU imu;
    public static volatile VoltageSensor voltageSensor;
    /// Используемый Telemetry
    public static volatile MultipleTelemetry multipleTelemetry;
    public static volatile Context myApp;
    /// Используемый {@link PhantomOpMode }
    public static volatile PhantomOpMode opMode;
    /// Используемый HardwareMap
    public static volatile HardwareMap hw;
    /// Используемый геймпад(gamepad1)
    public static volatile Gamepad gamepadDriver,
    /// Используемый геймпад (gamepad2)
    gamepadOperator;

    private static final Map<String, Object> customObjects = new ConcurrentHashMap<>();

    /**
     * Добавляет или обновляет кастомный объект в статичном хранилище
     *
     * @param key    ключ для доступа к объекту
     * @param object объект для хранения
     */
    public static void addOrUpdate(String key, Object object) {
        customObjects.put(key, object);
    }


    public static <T> T get(String name, Class<? extends T> type) {
        return type.cast(customObjects.get(name));
    }

}
