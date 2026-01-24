package io.github.ftcphantom16950.phantomlib.utils.action;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Класс создания прерываемых действий
 * Made by Hkial(Gleb)
 * Last Updated: 08.06.25 04:00
 */
public abstract class InterruptibleAction implements Action {
    private final LinearOpMode opMode;
    public boolean isInterrupted = false;
    private boolean isRunningOnce = false;

    public InterruptibleAction(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    public boolean isRunningOnce() {
        return isRunningOnce;
    }

    public void setRunningOnce(boolean runningOnce) {
        isRunningOnce = runningOnce;
    }

    public boolean isInterrupted() {
        return isInterrupted;
    }

    public void setInterrupted(boolean interrupted) {
        isInterrupted = interrupted;
    }

    @Override
    public void execute() {
        if (isRunningOnce && !isInterrupted && opMode.opModeIsActive()) {
            run();
        }
        while (!isRunningOnce && !isInterrupted && opMode.opModeIsActive()) {
            run();
        }
        if (isInterrupted && opMode.opModeIsActive()) {
            handleInterrupt();
        }
    }

    public abstract void run();

    public abstract void handleInterrupt();
}
