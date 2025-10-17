package io.github.ftcphantom16950.phantomlib.utils;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;



import java.util.HashSet;
import java.util.Set;

import io.github.ftcphantom16950.phantomlib.utils.Action.Groups.Group;

/**
 * <p>Класс для работы с OpMode</p>
 * <p>Создан для работы с {@link Scheduler}</p>
 * Made by Hkial(Gleb)
 * Last Updated: 08.06.25 02:40
 */
public abstract class PhantomOpMode extends LinearOpMode {

    private PhantomOpMode opMode = this;
    Thread telemetryExecutor;
    /// Имя необходимое для указания в runOpMode, должно быть уникальным
    private String name = "Default";
    /// Тип необходимый для указания в runOpMode
    private OpModeMeta.Flavor flavor = OpModeMeta.Flavor.TELEOP;
    /// Группа необходимая для указания в runOpMode
    private String group = "default";
    /// Действие запускаемое в начале OpMode
    public Group actions;
    public Set<Mechanism> mechanism = new HashSet<Mechanism>();
    /// Планировщик задач
    private Scheduler scheduler;

    ///  Получить имя
    public String getName() {
        return name;
    }

    /// Получить тип
    public OpModeMeta.Flavor getFlavor() {
        return flavor;
    }

    /// Получить Группу
    public String getGroup() {
        return group;
    }


    @Override
    public void runOpMode() {
        gamepadControlInit();
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

        sleep(500);
    }

    /// класс для указания имени, типа и группы OpMode
    public abstract void customOpModeSettings();

    public void gamepadControlInit() {
        GamepadControl.Companion.setOpMode(this);
        GamepadControl.Companion.init();
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setFlavor(OpModeMeta.Flavor flavor) {
        this.flavor = flavor;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void initTelemetry() {
//        phantomLogger = new PhantomLogger(this);
//        phantomLogger.start();
    }

    private void initScheduler() {
//        PhantomLogger.addData("Inited", true);
        scheduler = new Scheduler.Builder()
                .setAction(actions)
                .addMechanisms(mechanism)
                .build();

        scheduler.initMechanism();
    }

    private void runScheduler() {
        if (opModeIsActive()) {
            scheduler.run();
        }

    }
    public void onStart(){

    }

}
