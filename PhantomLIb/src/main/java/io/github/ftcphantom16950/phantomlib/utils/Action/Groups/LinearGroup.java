package io.github.ftcphantom16950.phantomlib.utils.Action.Groups;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.ftcphantom16950.phantomlib.utils.PhantomOpMode;

/// Класс для добавления последовательных действий
/// Made by Hkial(Gleb)
/// Last Updated: 08.06.25 02:40
public class LinearGroup extends Group {
    /// список добавляемых действий
    private final List<Group> actions = new ArrayList<Group>();

    /**
     * Класс для добавления последовательных групп
     *
     * @param actions действия которые будут выполняться последовательно
     */
    public LinearGroup(Group... actions) {

        this.actions.addAll(Arrays.asList(actions));
    }

    /// Метод выполнения действий последовательно
    @Override
    public void execute() throws InterruptedException {
        for (Group a : actions) {
            a.execute();
        }
    }
}
