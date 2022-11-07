package ru.akirakozov.sd.refactoring.util;

@FunctionalInterface
public interface ThrowingConsumer<T, E extends Throwable> {
    void accept(T t) throws E;
}
