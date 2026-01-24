package io.github.ftcphantom16950.phantomlib.utils;
/// Интерфейс для создания механизмов
/// Made by Hkial(Gleb)
/// Last Updated: 08.06.25 02:40
public interface Mechanism {
    void init() throws InterruptedException;
    default void read(){}
}
