package io.github.ftcphantom16950.phantomlib.utils;


import static io.github.ftcphantom16950.phantomlib.utils.Robot.multipleTelemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.github.ftcphantom16950.phantomlib.utils.Action.Groups.Group;

/**
 * <p>Класс для работы с OpMode</p>
 * <p>Создан для работы с {@link Scheduler}</p>
 * <p>Для задачи действий необходимо приравнять {@link PhantomOpMode#actions}</p>
 * <p>Для задачи механизмов необходимо добавить их в {@link PhantomOpMode#mechanism}</p>
 * Made by Hkial(Gleb)
 */
public abstract class PhantomOpMode extends LinearOpMode {
    private static final Map<String, Object> data = new ConcurrentHashMap<>();
    /// Действие запускаемое в начале OpMode
    public Group actions;
    public static volatile Set<Mechanism> mechanism = new HashSet<Mechanism>();
    /// Планировщик задач
    private Scheduler scheduler;
    Thread telemetryExecutor = new Thread(() -> {

        while (!isStopRequested()) {
            for (String s : data.keySet()) {
                multipleTelemetry.addData(s, data.get(s));
            }
            if (!isStopRequested()) {
                multipleTelemetry.update();
            }

        }
    });


    @Override
    public void runOpMode() {
        mechanism.clear();
        try {
            Robot.opMode = this;
            Robot.hw = hardwareMap;
            Robot.gamepadDriver = gamepad1;
            Robot.gamepadOperator = gamepad2;

            // инициализация телеметрии
            initTelemetry();
            // инициализация настроек опмода
            customOpModeSettings();
            // инициализация Планировщик задач
            initScheduler();
            // ожидания нажатия на кнопку старт
            waitForStart();
            onStart();
            // запуск планировщика
            runScheduler();

        } catch (Exception e) {
            data.clear();
            throw new RuntimeException(e);
        }
    }


    /// класс для указания имени, типа и группы OpMode
    public abstract void customOpModeSettings();


    private void initTelemetry() throws InterruptedException {
        multipleTelemetry = new MultipleTelemetry(this.telemetry, FtcDashboard.getInstance().getTelemetry());
        PhantomOpMode.addData("Нижняя подсветка", true);
        try {
            telemetryExecutor.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void initScheduler() {
        scheduler = new Scheduler.Builder().setAction(actions).addMechanisms(mechanism).build();
        scheduler.initMechanism();
    }

    private void runScheduler() throws InterruptedException {
        if (opModeIsActive()) {
            scheduler.run();
        }
        if (isStopRequested()) {
            SoundPlayer.getInstance().stopPlayingAll();
            data.clear();
            mechanism = new HashSet<>();
        }
    }

    public void onStart() {
        data.clear();
    }

    public static void addData(String s, Object data) {
        PhantomOpMode.data.put(s, data);
    }

}
