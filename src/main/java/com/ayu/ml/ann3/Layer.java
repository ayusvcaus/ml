package com.ayu.ml.ann3;

import java.util.List;
import java.util.ArrayList;

import com.ayu.ml.ann3.Neuron;
import com.ayu.ml.ann3.NeuralNetworkError;

public class Layer {
	
    private List<Neuron> neurons;

    public Layer() {
        neurons = new ArrayList<>();
    }

    public Layer(int size) {
        init(size, new DefaultSigmoid());
    }

    public Layer(int size, Sigmoid sigmoid) {
        init(size, sigmoid);
    }

    public void init(int size, Sigmoid sigmoid) {
        neurons = new ArrayList<>();
        generateNeurons(size, sigmoid);
    }

    public long size() {
        return neurons.size();
    }

    public Neuron get(int index) {
        return neurons.get(index);
    }

    public Neuron first() {
        return neurons.get(0);
    }

    public Neuron last() {
        return neurons.get(neurons.size()-1);
    }

    public Layer addNeuron(Neuron n) {
        neurons.add(n);
        return this;
    }

    public Layer generateNeurons(int num) {
        return generateNeurons(num, new DefaultSigmoid());
    }

    public Layer generateNeurons(int num, Sigmoid sigmoid) {
        for (int i=0; i<num; i++) {
            addNeuron(new Neuron(sigmoid));
        }
        return this;
    }

    private void ensureValuesSizeMatchLayerSize(double[] values) throws NeuralNetworkError {
        if (values.length != size()) {
            throw new NeuralNetworkError("Input size different from given values: " + size() + " != " + values.length);
        }
    }

    public void setValues(double[] values) throws NeuralNetworkError {
        ensureValuesSizeMatchLayerSize(values);
        for (int i=0; i<size(); i++) {
            get(i).setValue(values[i]);
        }
    }

    public List<Double> getValues() {
        List<Double> res = new ArrayList<>();
        for (Neuron neuron : neurons) {
            res.add(neuron.getValue());
        }
        return res;
    }

    public List<Double> getWeights() {
    	List<Double> res = new ArrayList<>();
        for (Neuron neuron : neurons) {
            for (Link link : neuron.getLinks()) {
                res.add(link.getWeight());
            }
        }
        return res;
    }

    private void ensureErrorsSizeMatchLayerSize(List<Double> errors) throws NeuralNetworkError {
        if (errors.size() != size()) {
            throw new NeuralNetworkError("Layer size different from given errors size: " + size() + " != " + errors.size());
        }
    }

    public void adjustNeuronWeights(Neuron neuron, double error, double learningRate) {
        for (Link link : neuron.getLinks()) {
            link.adjustWeight(error, learningRate);
        }
    }

    public void adjustLayerWeights(List<Double> errors, double learningRate) {
        for (int i = 0; i < neurons.size(); i++) {
            adjustNeuronWeights(neurons.get(i), errors.get(i), learningRate);
        }
    }

    public void adjustWeights(List<Double> errors, double learningRate) throws NeuralNetworkError {
        ensureErrorsSizeMatchLayerSize(errors);
        adjustLayerWeights(errors, learningRate);
    }

    public void linkNeuron(Neuron from) {
        for (Neuron neuron : neurons) {
            from.linkTo(neuron);
        }
    }

    public void linkToAnother(Layer to) {
        for (Neuron neuron : neurons) {
            to.linkNeuron(neuron);
        }
    }

    public void pass() {
        for (Neuron neuron : neurons) {
            neuron.pass();
        }
    }
}
