package com.homer.util.core;

/**
 * Created by arigolub on 3/28/16.
 */
public class Tuple<T> {
    private T left;
    private T right;

    public Tuple(T left, T right) {
        this.left = left;
        this.right = right;
    }

    public T getLeft() {
        return left;
    }

    public void setLeft(T left) {
        this.left = left;
    }

    public T getRight() {
        return right;
    }

    public void setRight(T right) {
        this.right = right;
    }
}
