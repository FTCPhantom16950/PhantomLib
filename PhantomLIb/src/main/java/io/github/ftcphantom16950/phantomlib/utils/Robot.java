package io.github.ftcphantom16950.phantomlib.utils;


import android.content.Context;

import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Класс хранящий основные переменные OpMode, хранящиеся статично
 */
public enum Robot {
    INSTANCE;
    public static final Map<String, Object> telemetryData = new ConcurrentHashMap<>();
    public static final Map<String, Object> data = new ConcurrentHashMap<>();
    public final ExecutorService threadPool = Executors.newCachedThreadPool();
    public final Map<String, Object> customObjects = new ConcurrentHashMap<>();
    public volatile double x = 0, y = 0, rot = 0;
    public IMU imu;
    public volatile VoltageSensor voltageSensor;
    public boolean soundPlaying = false, nearlyPlayed = false;
    /// Используемый Telemetry
    public volatile MultipleTelemetry multipleTelemetry;
    public SoundPlayer.PlaySoundParams params = new SoundPlayer.PlaySoundParams();
    public volatile WeakReference<Context> myAppRef;
    public volatile DcMotorEx lf, rb, lb, rf;
    /// Используемый {@link PhantomOpMode }
    public volatile PhantomOpMode opMode;
    /// Используемый HardwareMap
    public volatile HardwareMap hw;
    /// Используемый геймпад(gamepad1)
    public volatile Gamepad gamepadDriver,
    /// Используемый геймпад (gamepad2)
    gamepadOperator;
    public volatile double voltageCompenser;
    public volatile double voltage;
    List<LynxModule> allHubs;

    public static void addData(String s, Object data, boolean flag) {
        Robot.data.put(s, data);
        if (flag){
            Robot.telemetryData.put(s, data);
        }
    }
    public static void addData(String s, Object data) {
        Robot.data.put(s, data);
        Robot.telemetryData.put(s, data);
    }
    public void removeData(String s){
        Robot.data.remove(s);
    }
    public void removeTelemetryData(String s){
        Robot.telemetryData.remove(s);
    }
    public void getTelemetryData(String s){
        Robot.telemetryData.get(s);
    }

    public Object getData(String s){
        return Robot.data.get(s);
    }

    /**
     * Добавляет или обновляет кастомный объект в статичном хранилище
     *
     * @param key    ключ для доступа к объекту
     * @param object объект для хранения
     */
    public void addOrUpdate(Object object, String key) {
        customObjects.put(key, object);
    }

    public <T> T get(Object type, String name) {
        return (T) customObjects.get(name);
    }

    public Context getApp() {
        return myAppRef != null ? myAppRef.get() : null;
    }

    public void clearBulkCache() {
        for (LynxModule module : allHubs) {
            module.clearBulkCache();
        }
    }
}
