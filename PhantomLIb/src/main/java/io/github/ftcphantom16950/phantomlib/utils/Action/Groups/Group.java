package io.github.ftcphantom16950.phantomlib.utils.Action.Groups;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import io.github.ftcphantom16950.phantomlib.utils.Robot;

public abstract class Group {
    LinearOpMode opMode;
    public Group() {
        this.opMode = Robot.opMode;
    }

    /// Метод для реализации выполнения действия
    public abstract void execute() throws InterruptedException;

}
