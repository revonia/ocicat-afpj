package com.ocicatteam.afpj.algo;

import java.util.Stack;

/**
 * Created by SiriusWangs on 2017/4/20.
 */
public class TimeIdStack {
    // TODO 考虑换成一个省内存的方式
    Stack<TimeIdPair> s;
    TimeIdStack() {
        s = new Stack<>();
    }
    public TimeIdPair[] get() {
        return s.toArray(new TimeIdPair[s.size()]);
    }

}
