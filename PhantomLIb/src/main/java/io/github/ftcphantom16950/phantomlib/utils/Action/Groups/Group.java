package io.github.ftcphantom16950.phantomlib.utils.Action.Groups;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class Group {
    LinearOpMode opMode;
    public Group(LinearOpMode opMode) {
        this.opMode = opMode;
    }

    /// Метод для реализации выполнения действия
    public abstract void execute();

}
