package com.ayu.ml.ann2;

import java.util.Random;

public class Neuron {

    private double      output;
    private double      transferDerivative;
    private double      deltaError;
    private double[]    weights;

    public Neuron(int numInputs){
    	Random rand = new Random(System.nanoTime());
        weights = new double[numInputs];
        for (int idx=0; idx<weights.length; idx++) {
            weights[idx] = rand.nextDouble();
        }
    }
    
    public Neuron(Neuron n) {
    	output = n.getOutput();
    	transferDerivative = n.getTransferDerivative();
    	deltaError = n.getDeltaError();
    	double[] old_weights = n.getWeights();
    	this.weights = new double[old_weights.length];
    	for (int i=0; i<weights.length; i++) {
    		this.weights[i] = old_weights[i];
    	}
    }

    /**
     * Calculate the neuron output and transfer derivative
     * @param inputs - Layer above outputs
     * @return - Neuron output
     */
    public Double activate(double[] inputs){

        double activation = weights[weights.length-1]; // add bias

        for (int i=0; i<weights.length-1; i++) {
            activation += weights[i] * inputs[i];
        }

        output = 1.0 / (1.0 + Math.exp(-activation));
        transferDerivative = output * (1 - output);

        return output;
    }

    public void setDeltaError(double deltaError){
        this.deltaError = deltaError;
    }

    public double getDeltaError(){
        return deltaError;
    }

    public double getTransferDerivative(){
        return transferDerivative;
    }

    public double getOutput(){
        return output;
    }

    public double[] getWeights(){
        return weights;
    }

    @Override
    public String toString(){

       StringBuilder builder = new StringBuilder();

       builder.append("Delta error: [");
        builder.append(deltaError);
        builder.append("] Output: [");
        builder.append(output);
        builder.append("]");
        builder.append(" 'Weights' {");

        for(int idx = 0; idx < weights.length; idx++){
            builder.append(weights[idx]);
            if(idx < weights.length-1)
                builder.append(", ");
        }
        builder.append("}");
        return builder.toString();
    }

}
