package com.ayu.ml.ann;

import java.util.List;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class Backpropagation extends Network {
	
    @Getter @Setter 
    private double learningRate = 0.01;

    public List<Double> getTotalErrorForOutputs(double[] expectedOutput) throws NeuralNetworkError {
        Layer output = last();
        ensureOutputSizeEqualsToExpectedOutputSize(output, expectedOutput);
        return calculateTotalErrorForOutputNeurons(output, expectedOutput);
    }

    public List<Double> calculateSquaredErrorForOutput(double[] expectedOutput) throws NeuralNetworkError {
        Layer output = last();
        ensureOutputSizeEqualsToExpectedOutputSize(output, expectedOutput);
        return calculateSquaredErrorForOutputNeurons(output, expectedOutput);
    }

    public double calculateSqaredErrorSumForOutput(double[] expectedOutput) throws NeuralNetworkError {
        Layer output = last();
        ensureOutputSizeEqualsToExpectedOutputSize(output, expectedOutput);
        return calculateSquaredErrorSumForOutputNeurons(output, expectedOutput);
    }

    public List<Double> calculateErrorForOutput(double[] expectedOutput) throws NeuralNetworkError {
        Layer output = last();
        ensureOutputSizeEqualsToExpectedOutputSize(output, expectedOutput);
        return calculateErrorForOutputNeurons(output, expectedOutput);
    }

    private void ensureOutputSizeEqualsToExpectedOutputSize(Layer output, double[] expectedOutput) throws NeuralNetworkError {
        if (output.size() != expectedOutput.length) {
            throw new NeuralNetworkError("Output size different from given expected outputs: " + output.size() + " != " + expectedOutput.length);
        }
    }

    private List<Double> calculateSquaredErrorForOutputNeurons(Layer output, double[] expectedOutput) {
    	List<Double> res = new ArrayList<>();
        for (int i = 0; i < output.size(); i++) {
            res.add(calculateSquaredErrorForOutputNeuron(output.get(i), expectedOutput[i]));
        }
        return res;
    }

    public List<Double> calculateErrorForOutputNeurons(Layer output, double[] expectedOutput) {
    	List<Double> res = new ArrayList<>();
        for (int i = 0; i < output.size(); i++) {
            res.add(calculateErrorForOutputNeuron(output.get(i), expectedOutput[i]));
        }
        return res;
    }

    private double calculateTotalErrorForOutputNeuron(Neuron neuron, double expected, double totalSquaredError) {
        double error = calculateErrorForOutputNeuron(neuron, expected);
        double derivate = getPartialLogisticDerivateOfValue(neuron.getValue());
        return error * derivate * totalSquaredError;
    }

    private List<Double> calculateTotalErrorForOutputNeurons(Layer output, double[] expectedOutput) {
    	List<Double> res = new ArrayList<>();

        double totalSquaredError = calculateSquaredErrorSumForOutputNeurons(output, expectedOutput);
        for (int i = 0; i < output.size(); i++) {
            res.add(calculateTotalErrorForOutputNeuron(output.get(i), expectedOutput[i], totalSquaredError));
        }
        return res;
    }

    private double calculateSquaredErrorSumForOutputNeurons(Layer output, double[] expectedOutput) {
        double res = 0.0;
        for (int i = 0; i < output.size(); i++) {
            res += calculateSquaredErrorForOutputNeuron(output.get(i), expectedOutput[i]);
        }
        return res;
    }

    public double calculateSquaredErrorForOutputNeuron(Neuron outputNeuron, double expected) {
        return 0.5 * Math.pow(expected - outputNeuron.getValue(), 2);
    }

    public double calculateErrorForOutputNeuron(Neuron outputNeuron, double expected) {
        return -(expected - outputNeuron.getValue());
    }

    public double getPartialLogisticDerivateOfValue(double value) {
        return value * (1 - value);
    }

    public List<Double> getPartialLogisticDerivateOfOutput() {
        Layer output = last();
        List<Double> res = new ArrayList<>();
        for (Double value : output.getValues()) {
            res.add(getPartialLogisticDerivateOfValue(value));
        }
        return res;
    }

    public void adjustLayerWeights(Layer layer, List<Double> weights) throws NeuralNetworkError {
        layer.adjustWeights(weights, learningRate);
    }

}
