package com.ayu.ml.ann3;

import com.ayu.ml.ann.Sigmoid;

public class LogSigmoid implements Sigmoid {
    private double p1;
    private double p2;
    public LogSigmoid(double param1, double param2) {
        p1 = param1;
        p2 = param2;
    }
    public double transfer(double value) {
        return (1 + p1) / (1 + Math.exp(p2 * -value));
    }
}