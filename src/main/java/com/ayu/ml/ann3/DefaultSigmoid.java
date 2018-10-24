package com.ayu.ml.ann3;

import com.ayu.ml.ann3.Sigmoid;

public class DefaultSigmoid implements Sigmoid {
    public double transfer(double value) {
        return value;
    }
}