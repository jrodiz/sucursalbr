package com.jrodiz.business;

public interface IRptContext<T> {

    boolean isAlive();

    T getViewContext();
}
