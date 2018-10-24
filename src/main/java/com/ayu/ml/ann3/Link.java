package com.ayu.ml.ann3;

import java.util.concurrent.ThreadLocalRandom;

import lombok.Getter;
import lombok.Setter;

public class Link {

    @Getter private Neuron from;
    @Getter private Neuron to;
    @Getter @Setter private double weight;
    private static double rangeMin = -0.3;
    private static double rangeMax = 0.3;

    public Link(Neuron from, Neuron to) {
        this.from = from;
        this.to = to;

        this.weight = ThreadLocalRandom.current().nextDouble(rangeMin, rangeMax);
    }

    public static void setWeightRange(double min, double max) {
        rangeMin = min;
        rangeMax = max;
    }

    public double calculateWeightedValue() {
        return weight * from.getValue();
    }

    public double calculateWeightedError() {
        return weight * from.getError();
    }

    public void adjustWeight(double error, double learningRate) {
        weight -= learningRate * error;
    }
}
