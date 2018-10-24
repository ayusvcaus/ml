package com.ayu.ml.ann;

import com.ayu.ml.ann.Sigmoid;

public class DefaultSigmoid implements Sigmoid {
    public double transfer(double value) {
        return value;
    }
}