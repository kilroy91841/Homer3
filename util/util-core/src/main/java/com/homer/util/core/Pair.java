package com.homer.util.core;

/**
 * Created by arigolub on 7/27/16.
 */
public class Pair<T, K> {
    private T first;
    private K second;

    public Pair(T left, K right) {
        this.first = left;
        this.second = right;
    }

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public K getSecond() {
        return second;
    }

    public void setSecond(K second) {
        this.second = second;
    }
}
