package io.github.ftcphantom16950.phantomlib.utils;


import java.util.HashSet;
import java.util.Set;

import io.github.ftcphantom16950.phantomlib.utils.action.Action;

/**
 * Класс для подготовки и последовательного выполения действий
 * Class for initialization and running actions
 * Made by Hkial(Gleb)
 * Last Updated: 08.06.25 02:40
 */
public class Scheduler {
    /// сет с необходимыми механизмами
    private static Set<Mechanism> mechanisms = new HashSet<>();
    /// Выполняемое действие
    private final Action action;

    /**
     * Билдер для класса Scheduler, позволяет его настраивать
     */
    public static class Builder {
        /// сет с необходимыми механизмами
        private final Set<Mechanism> mechanisms = new HashSet<>();
        /// Выполняемое действие
        private Action action;

        /// Метод добавления механизмов в необходимые
        public Builder addMechanisms(Set<Mechanism> mechanisms) {
            if (mechanisms == null) throw new IllegalStateException("Mechanisms in scheduler mustn't be null");
            this.mechanisms.clear();
            this.mechanisms.addAll(mechanisms);
            return this;
        }

        /// Метод добавления механизма в необходимые
        public Builder addMechanism(Mechanism mechanism) {
            if (mechanism == null) throw new IllegalStateException("mechanism in scheduler mustn't be null");
            this.mechanisms.clear();
            this.mechanisms.add(mechanism);
            return this;
        }

        /// Метод добавления действия
        public Builder setAction(Action action) {
            if (action == null) throw new IllegalStateException("actions in scheduler mustn't be null");
            this.action = action;
            return this;
        }

        /// Метод для сборки класса Scheduler
        public Scheduler build() {
            if (action == null) {
                throw new IllegalStateException("actions is required");
            }
            return new Scheduler(this);
        }
    }

    /// Внутренний конструктор необходимый для Builder
    private Scheduler(Builder builder) {
        mechanisms = builder.mechanisms;
        action = builder.action;
    }

    /// Метод для инициализации механизмов
    public void initMechanism() {
        for (Mechanism mechanism : mechanisms) {
            try{
                Robot.addData(mechanism.getClass().getSimpleName(), true);
                mechanism.init();
                mechanism = null;
            } catch (Exception e) {
                Robot.addData(mechanism.getClass().getSimpleName(), false);
                Robot.INSTANCE.multipleTelemetry.update();
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isRunning = false;

    /// Запуск действий
    public void run() throws InterruptedException {
        if (isRunning) {
            throw new IllegalStateException("Scheduler already running");
        }
        isRunning = true;

        if (action == null) {
            throw new NullPointerException("actions is null");
        }
        try {
            action.execute();
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }
}
