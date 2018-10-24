package com.ayu.ml.ann2;

import java.util.List;

public class NeuralNet {

    private int     numInputs;
    private int     numHiddens;
    private int     numHiddenLayers;
    private int     numOutputs;
    private Network network;

    public NeuralNet(int numInputs, int numHiddenLayers, int numHiddens, int numOutputs){
        this.numInputs = numInputs;
        this.numHiddens = numHiddens;
        this.numHiddenLayers = numHiddenLayers;
        this.numOutputs = numOutputs;
        network = new Network();
        initializeNetwork();
    }
    
    public NeuralNet(NeuralNet net) {
    	setNumInputs(net.getNumInputs());
    	setNumHiddens(net.getNumHiddens());
    	setNumHiddenLayers(net.getNumHiddenLayers());
    	setNumOutputs(net.getNumOutputs());
    	setNetwork(net.getNetwork());
    }
    
    public int getNumInputs() {
    	return numInputs;
    }
    
    public void setNumInputs(int numInputs) {
    	this.numInputs = numInputs;
    }
    
    public int getNumHiddens() {
    	return numHiddens;
    }
    
    public void setNumHiddens(int numHiddens) {
    	this.numHiddens = numHiddens;
    }
    
    public int getNumHiddenLayers() {
    	return numHiddenLayers;
    }
    
    public void setNumHiddenLayers(int numHiddenLayers) {
    	this.numHiddenLayers = numHiddenLayers;
    }
    
    public int getNumOutputs() {
    	return numOutputs;
    }
    
    public void setNumOutputs(int numOutputs) {
    	this.numOutputs = numOutputs;
    }
    
    public void setNetwork(Network network) {
    	//Deep copy
    	this.network = new Network(network.getLayers());
    }
    
    public Network getNetwork() {
    	return network;
    }

    /**
     * Instantiate/create layers.
     */
    private void initializeNetwork(){
        for (int i=0; i<this.numHiddenLayers; i++) {
        	Neuron[] hiddenLayer = new Neuron[numHiddens];
            for (int hidden=0; hidden<numHiddens; hidden++) {
                hiddenLayer[hidden] = new Neuron(numInputs+1);
            }
            this.network.append(hiddenLayer);
        }        
        Neuron[] outputLayer = new Neuron[numOutputs];
        for (int outputs=0; outputs<numOutputs; outputs++) {
            outputLayer[outputs] = new Neuron(numHiddens+1);
        }
        this.network.append(outputLayer);
    }

    /**
     * Propagates the last neuron activation to the new neuron as input.
     * @param inputs - Training inputs from the data set
     * @return - The outputs from neurons in the output layer
     */
    public double[] forwardPropagate(double [] inputs){

        double[] layerInputs = new double[inputs.length];
        System.arraycopy(inputs, 0, layerInputs, 0, inputs.length);

        for (int i=0; i<network.getLayers().size(); i++){
        	Neuron[] layer =  network.getLayers().get(i);
        	double[] layerOutputs = new double[layer.length];
            int n = 0;

            for (Neuron neuron : layer)  {// for each neuron in the layer
                layerOutputs[n++] = neuron.activate(layerInputs);
            }
            if (i==this.network.getLayers().size()-1) {
                layerInputs = new double[layerOutputs.length];
            } 
            System.arraycopy(layerOutputs, 0, layerInputs, 0, layerOutputs.length);
        }
        return layerInputs;
    }

    /**
     * Calculate error delta in each neuron using expected outputs and previous layer error delta values.
     * @param expected - Array[double] of expected outputs
     */
    public void backPropagate(double[] expected){

        List<Neuron[]> networkLayers = network.getLayers();

        for (int layerIdx=networkLayers.size()-1; layerIdx>=0; layerIdx--) {  // each layer in reverse

        	Neuron[] thisLayer = networkLayers.get(layerIdx);
            for (int neuronIdx=0; neuronIdx<thisLayer.length; neuronIdx++) { // each neuron in the layer

            	Neuron thisNeuron = thisLayer[neuronIdx]; // this neuron in the layer
            	double error = 0.0d;

                if (layerIdx == networkLayers.size()-1) { // if this is output layer
                    thisNeuron.setDeltaError((expected[neuronIdx] - thisNeuron.getOutput()) * thisNeuron.getTransferDerivative());
                } else { // else if this is a hidden layer or entry layer
                    for (Neuron plusLevelNeuron : networkLayers.get(layerIdx+1)) {
                        error += (plusLevelNeuron.getWeights()[neuronIdx] * plusLevelNeuron.getDeltaError());
                    }
                    thisNeuron.setDeltaError(error * thisNeuron.getTransferDerivative());
                }
            }
        }
    }


    /**
     * Update weights in the layers using previously calculated delta errors during back propagation.
     * @param record - The training record
     * @param learnRate - The learning rate
     */
    public void updateWeights(double[] record, double learnRate) {

        int numLayers = network.getLayers().size();

        List<Neuron[]> networkLayers = network.getLayers();
        
        double[] layerInputs = new double[record.length];

        System.arraycopy(record, 0, layerInputs, 0, record.length);

        for (int layerIdx=0; layerIdx<numLayers; layerIdx++) { // for each layer

        	Neuron[] thisLayer = networkLayers.get(layerIdx);

            for (int neuronIdx=0; neuronIdx<thisLayer.length; neuronIdx++) { // for each neuron in the layer

            	Neuron thisNeuron = thisLayer[neuronIdx];
                double[] thisNeuronWeights = thisNeuron.getWeights();

                // adjust the layer neuron weights using prior level inputs' deltaError
                for (int inputIdx=0; inputIdx<layerInputs.length; inputIdx++) {
                    thisNeuronWeights[inputIdx] += learnRate * thisNeuron.getDeltaError() * layerInputs[inputIdx];
                }

                thisNeuronWeights[thisNeuron.getWeights().length-1] += learnRate * thisNeuron.getDeltaError();
            }
            layerInputs = new double[thisLayer.length]; //

            for (int neuronIdx=0; neuronIdx<thisLayer.length; neuronIdx++) {
                layerInputs[neuronIdx] = thisLayer[neuronIdx].getOutput();
            }
        }
    }

    /**
     * Train the network using training set
     * @param trainingSet - The training set with known class values
     * @param learningRate - Learning rate
     * @param numEpochs - Number of epochs (training cycles)
     */
    public void trainNetwork(List<double[]> trainingSet, double learningRate, int numEpochs) {

       // Random rand = new Random(System.currentTimeMillis());

        for (int epoch=0; epoch<numEpochs; epoch++) {
            //double error = 0.0d;

            for (double[] record : trainingSet) {

                //double[] outputs = forwardPropagate(record);
            	forwardPropagate(record);

            	double[] expected = new double[numOutputs];

                int expectedVal = (int) record[record.length-1]; // the last record value is the expected value
                expected[expectedVal] = 1;

                //for (int idx = 0; idx < expected.length; idx++)
                //    error += Math.pow(expected[idx] - outputs[idx], 2);

                backPropagate(expected);
                updateWeights(record, learningRate);
            }
        }
    }

    /**
     * Forward propagate and return the prediction
     * @param record - The record to predict from
     * @return - The prediction
     */
    //public int predict(double[] record){
    public static int predict(double[]output){
        //double[] output = forwardPropagate(record);
        int maxIdx = 0;
        double maxVal = output[0];
        for (int i=1; i<output.length; i++){
            if (output[i]>maxVal){
                maxIdx = i;
                maxVal = output[i];
            }
        }
        return maxIdx;
    }
}
