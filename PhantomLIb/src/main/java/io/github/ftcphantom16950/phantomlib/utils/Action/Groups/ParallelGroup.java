package io.github.ftcphantom16950.phantomlib.utils.Action.Groups;



import java.util.ArrayList;
import java.util.List;

import io.github.ftcphantom16950.phantomlib.utils.PhantomOpMode;

/// Класс для добавления параллельных действий
/// Made by Hkial(Gleb)
/// Last Updated: 08.06.25 02:40
public class ParallelGroup extends Group {
    /// список добавляемых действий
    private final List<Group> actions = new ArrayList<Group>();
    private final List<Thread> threads = new ArrayList<>();

    /**
     * Класс для добавления последовательных групп
     *
     * @param actions действия которые будут выполняться последовательно
     */
    public ParallelGroup(PhantomOpMode phantomOpMode, Group... actions) {
        super(phantomOpMode);
        this.actions.addAll(List.of(actions));
    }

    /// Метод выполнения действий последовательно
    @Override
    public void execute() {
        for (Group a :
                actions) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    a.execute();
                }
            };
            threads.add(thread);
            thread.start();
        }
        int i = 0;
        for (Thread t :
                threads) {
            i++;
            while (t.isAlive() && opMode.opModeIsActive()) {
            }
        }
    }
}
