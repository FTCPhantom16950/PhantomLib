package io.github.ftcphantom16950.phantomlib.utils.action;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import io.github.ftcphantom16950.phantomlib.utils.Robot;

public class ParallelGroup implements Action {
    private final LinearOpMode opMode;
    private final List<Action> actions = new ArrayList<>();
    private final ExecutorService threadPool;

    public ParallelGroup(ExecutorService threadPool, LinearOpMode opMode, Action... actions) {
        this.actions.addAll(List.of(actions));
        this.opMode = opMode;
        this.threadPool = threadPool;
    }

    @Override
    public void execute() throws InterruptedException {
        // CountDownLatch можно использовать вместо join, но join проще для понимания
        List<Future<?>> futures = new ArrayList<>();

        for (Action a : actions) {
            futures.add(threadPool.submit(() -> {
                try {
                    a.execute();
                } catch (Exception e) {
                    Robot.INSTANCE.multipleTelemetry.addData("Parallel Error", e.getMessage());
                }
            }));
        }

        // Ждем завершения всех задач
        boolean allDone = false;
        while (!allDone && opMode.opModeIsActive() && !Thread.currentThread().isInterrupted()) {
            allDone = true;
            for (Future<?> future : futures) {
                if (!future.isDone()) {
                    allDone = false;
                    break; // Если хоть один не готов, выходим из for и ждем дальше
                }
            }
            // Спим чуть-чуть, чтобы не грузить процессор
            opMode.sleep(10);
        }

        // 3. Если OpMode остановлен (кнопка Stop), отменяем задачи, которые еще висят
        if (!opMode.opModeIsActive()) {
            for (Future<?> future : futures) {
                // true означает "прервать жестко", даже если поток работает
                future.cancel(true);
            }
        }
    }
}
