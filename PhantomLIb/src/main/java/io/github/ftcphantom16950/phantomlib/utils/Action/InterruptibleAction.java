package io.github.ftcphantom16950.phantomlib.utils.Action;

import static io.github.ftcphantom16950.phantomlib.utils.Robot.opMode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import io.github.ftcphantom16950.phantomlib.utils.PhantomOpMode;


/**
 * Класс создания прерываемых действий
 * Made by Hkial(Gleb)
 * Last Updated: 08.06.25 04:00
 */
public abstract class InterruptibleAction extends Action {
    public boolean isInterrupted = false;

    public boolean isRunningOnce() {
        return isRunningOnce;
    }

    public void setRunningOnce(boolean runningOnce) {
        isRunningOnce = runningOnce;
    }

    private boolean isRunningOnce = false;

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
        if (isInterrupted && opMode.opModeIsActive()){
            handleInterrupt();
        }
    }
    public abstract void run();
    public abstract void handleInterrupt();
    public InterruptibleAction() {
    }
}
