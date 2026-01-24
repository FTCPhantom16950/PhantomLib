package io.github.ftcphantom16950.phantomlib.utils.action;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/// Класс для добавления последовательных действий
/// Made by Hkial(Gleb)
/// Last Updated: 08.06.25 02:40
public class LinearGroup implements Action {
    /// список добавляемых действий
    private final List<Action> actions = new ArrayList<Action>();

    /**
     * Класс для добавления последовательных групп
     *
     * @param actions действия которые будут выполняться последовательно
     */
    public LinearGroup(Action... actions) {
        this.actions.addAll(Arrays.asList(actions));
    }

    /// Метод выполнения действий последовательно
    @Override
    public void execute() throws InterruptedException {
        for (Action a : actions) {
            a.execute();
        }
    }
}
