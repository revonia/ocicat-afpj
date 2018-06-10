package com.ocicatteam.afpj.algo;

/**
 * Created by SiriusWangs on 2017/4/20.
 */
public class Counter {
    private int value = 0;

    public Counter() {}

    public Counter(int val) { this.value = val; }

    public int get() { return value; }

    public int set(int value) { return this.value = value; }

    public int add() { return ++this.value; }

    public int sub() { return --this.value; }
}