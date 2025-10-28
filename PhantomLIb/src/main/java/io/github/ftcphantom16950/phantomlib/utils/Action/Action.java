package io.github.ftcphantom16950.phantomlib.utils.Action;


import io.github.ftcphantom16950.phantomlib.utils.Action.Groups.Group;
import io.github.ftcphantom16950.phantomlib.utils.PhantomOpMode;

/**
 * Класс создания действия
 * Made by Hkial(Gleb)
 * Last Updated: 08.06.25 02:40
 */
public abstract class Action extends Group {
    /// Метод для реализации выполнения действия
    public abstract void execute();
    public Action() {

    }

}
