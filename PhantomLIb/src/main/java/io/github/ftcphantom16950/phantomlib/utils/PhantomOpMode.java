package io.github.ftcphantom16950.phantomlib.utils;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;



import java.util.HashSet;
import java.util.Set;

import io.github.ftcphantom16950.phantomlib.utils.Action.Groups.Group;

/**
 * <p>Класс для работы с OpMode</p>
 * <p>Создан для работы с {@link Scheduler}</p>
 * <p>Для задачи действий необходимо приравнять {@link PhantomOpMode#actions}</p>
 * <p>Для задачи механизмов необходимо добавить их в {@link PhantomOpMode#mechanism}</p>
 * Made by Hkial(Gleb)
 */
public abstract class PhantomOpMode extends LinearOpMode {
    /// Действие запускаемое в начале OpMode {@link Group}
    public Group actions;
    /// {@link Set} для работы с механизмами
    public Set<Mechanism> mechanism = new HashSet<Mechanism>();
    /// Планировщик задач {@link Scheduler}
    private Scheduler scheduler;

    @Override
    public void runOpMode() {
        Robot.opMode = this;
        Robot.hw = this.hardwareMap;
        Robot.telemetry = this.telemetry;
        Robot.gamepadDriver = this.gamepad1;
        Robot.gamepadOperator = this.gamepad2;
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
    }

    /// Метод для задачи механизмов и действий
    public abstract void customOpModeSettings();

    private void initTelemetry() {
    }
    /// Инициализация планировщика
    private void initScheduler() {
        scheduler = new Scheduler.Builder()
                .setAction(actions)
                .addMechanisms(mechanism)
                .build();

        scheduler.initMechanism();
    }
    /// Запуск планировщика
    private void runScheduler() {
        if (opModeIsActive()) {
            scheduler.run();
        }

    }
    /// Выполняется в момент старта
    public void onStart(){

    }

}
