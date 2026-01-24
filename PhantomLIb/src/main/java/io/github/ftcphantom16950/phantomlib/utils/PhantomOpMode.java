package io.github.ftcphantom16950.phantomlib.utils;


import static io.github.ftcphantom16950.phantomlib.utils.Robot.INSTANCE;
import static io.github.ftcphantom16950.phantomlib.utils.Robot.data;
import static io.github.ftcphantom16950.phantomlib.utils.Robot.telemetryData;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import io.github.ftcphantom16950.phantomlib.mechanisms.VolatgeMechanism;
import io.github.ftcphantom16950.phantomlib.utils.action.Action;


/**
 * <p>Класс для работы с OpMode</p>
 * <p>Создан для работы с {@link Scheduler}</p>
 * Made by Hkial(Gleb)
 * Last Updated: 08.06.25 02:40
 */
public abstract class PhantomOpMode extends LinearOpMode {

    public static Set<Mechanism> mechanism = new CopyOnWriteArraySet<Mechanism>();
    private final Thread hardwareLoop = new Thread(() -> {
        while (!isStopRequested() && !Thread.currentThread().isInterrupted()) {
            try {
                // 1. Очищаем кэш хабов (для режима MANUAL - самый быстрый)
                INSTANCE.clearBulkCache();

                // 2. Читаем все механизмы
                for (Mechanism m : PhantomOpMode.mechanism) {
                    try {
                        m.read();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    });
    /// Действие запускаемое в начале OpMode
    public Action actions;
    /// Планировщик задач
    private Scheduler scheduler;
    private TelemetryPacket packet = new TelemetryPacket();
    private Thread telemetryExecutor = new Thread(() -> {
        while (!isStopRequested()) {
            INSTANCE.voltageCompenser = INSTANCE.voltage / 12;
            packet = new TelemetryPacket();
            packet.put("voltage", INSTANCE.voltage);
            packet.put("Pose heading x", INSTANCE.x / 25.4);
            packet.put("Pose heading y", INSTANCE.y / 25.4);
            packet.put("Pose heading", INSTANCE.rot);
            if (!isStopRequested()) {
                for (String s : telemetryData.keySet()) {
                    INSTANCE.multipleTelemetry.addData(s, telemetryData.get(s));
                }
                INSTANCE.multipleTelemetry.update();
                FtcDashboard.getInstance().sendTelemetryPacket(packet);
            }

        }
    });


    @Override
    public void runOpMode() {

        INSTANCE.allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule module : INSTANCE.allHubs) {
            module.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        }
        telemetry.setAutoClear(true);
        mechanism.clear();
        INSTANCE.customObjects.clear();
        telemetryData.clear();
        data.clear();
        try {
            //
            INSTANCE.voltageSensor = hardwareMap.voltageSensor.iterator().next();
            INSTANCE.myAppRef = new WeakReference<>(hardwareMap.appContext);
            INSTANCE.params.loopControl = 0;
            INSTANCE.params.waitForNonLoopingSoundsToFinish = true;
            INSTANCE.opMode = this;
            INSTANCE.hw = hardwareMap;
            INSTANCE.gamepadDriver = gamepad1;
            INSTANCE.gamepadOperator = gamepad2;
            mechanism.add(new VolatgeMechanism());
            // инициализация телеметрии
            initTelemetry();
            // инициализация настроек опмода
            customOpModeSettings();
            // инициализация Планировщик задач
            initScheduler();
            hardwareLoop.start();
            // ожидания нажатия на кнопку старт
            waitForStart();
            onStart();
            // запуск планировщика
            runScheduler();
            mechanism.clear();
            hardwareLoop.interrupt();
            telemetryExecutor.interrupt();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /// класс для указания имени, типа и группы OpMode
    public abstract void customOpModeSettings();

    private void initTelemetry() throws InterruptedException {
        INSTANCE.multipleTelemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());
        Robot.addData("Нижняя подсветка", true);
        try {
            telemetryExecutor.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initScheduler() {
        INSTANCE.voltageCompenser = INSTANCE.voltageSensor.getVoltage() / 12;
        scheduler = new Scheduler.Builder()
                .setAction(actions)
                .addMechanisms(mechanism)
                .build();

        scheduler.initMechanism();
        INSTANCE.rot = INSTANCE.imu.getRobotYawPitchRollAngles().getYaw();
    }

    private void runScheduler() throws InterruptedException {
        if (opModeIsActive()) {
            scheduler.run();
        }
        if (isStopRequested()) {
            data.clear();
            telemetryData.clear();
            mechanism = new HashSet<>();
        }
    }

    public void onStart() {
        data.clear();
        telemetryData.clear();
    }

}
