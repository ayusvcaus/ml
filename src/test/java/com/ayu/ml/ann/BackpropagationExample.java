package com.ayu.ml.ann;

import org.junit.Test;

import com.ayu.ml.ann.Backpropagation;
import com.ayu.ml.ann.Layer;
import com.ayu.ml.ann.NeuralNetworkError;

public class BackpropagationExample {

    private static Backpropagation createNetwork() {
        /* Create network with:
         * 2 input neurons,
         * 8 neurons on first hidden layer
         * 6 neurons on second hidden layer
         * 1 neuron on output layer
         * Fully link
         */
        Backpropagation net = new Backpropagation();
        net.addLayer(new Layer(2));
        net.addLayer(new Layer(8));
        net.addLayer(new Layer(6));
        net.addLayer(new Layer(1));
        net.linkAll();
        return net;
    }

    private static boolean setInputValues(Backpropagation net) {
        double[] input = {0.1, 0.8};
        try {
            // First layer is input layer
            net.first().setValues(input);
        }
        catch (NeuralNetworkError e) {
            System.err.println("Couldn't set input values to network!");
            return false;
        }
        return true;
    }

    private static void printOutputValues(Backpropagation net) {
        // Last layer == output, first == first neuron on layer
        System.out.println("Output neuron value: " + net.last().first().getValue());
    }

    @Test
    public void testAnn() throws Exception {
        Backpropagation net = createNetwork();
        if (!setInputValues(net)) return;
        net.forwardPass();
        printOutputValues(net);
    }
}
