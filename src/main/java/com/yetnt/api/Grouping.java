package com.yetnt.api;

public enum Grouping {

    SINGULAR(1),
    DOUBLE(2),
    QUAD(4);

    private int n;

    Grouping(int n) {
        this.n = n;
    }

    public int getN() {
        return n;
    }
}
