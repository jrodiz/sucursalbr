package com.jrodiz.sucursalesbr.base;

public interface IRptContext<T> {

    boolean isAlive();

    T getViewContext();
}
