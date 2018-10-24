package com.ayu.ml.ann2;

import java.util.List;

import org.junit.Test;

public class TestDriver {

    @Test
    public void trainTest() throws Exception {

        List<double[]> dataSet = DatasetLoader.loadSet("data/ann2/seeds_dataset.txt");
        int numInputs = dataSet.get(0).length;
        int numHiddens = 3;
        int numHiddenLayers = 2;
        int numOutputs = 4;
        double error = 0.0d;
        NeuralNet neuralNet = new NeuralNet(numInputs, numHiddenLayers, numHiddens, numOutputs);
        
        DatasetLoader.normalizeMinMax(0, 1, 7, dataSet);
        neuralNet.trainNetwork(dataSet, 0.03, 460);

        for (double[] row : dataSet) {

        	NeuralNet neuralNet_trained = new NeuralNet(neuralNet); //Deep copy
        	double[] output = neuralNet_trained.forwardPropagate(row);
            double prediction = NeuralNet.predict(output);
            if (Math.abs(prediction-row[row.length-1])>0.01) {
                error += 1;
            }
        }
        error /= dataSet.size();
        System.out.printf("Error: %f%%\n", error*100);
        
        
        String line = "19.57,16.74,0.8779,6.384,3.772,1.472,6.273,2";
        String[] str_data = line.split(",");
        double[] real_data = new double[str_data.length];
        for (int i=0; i<real_data.length; i++) {
        	real_data[i] = Double.parseDouble(str_data[i]);    
        	System.out.print(real_data[i] + "  ");
        }
        System.out.println();
        NeuralNet neuralNet_trained = new NeuralNet(neuralNet);
    	double[] output = neuralNet_trained.forwardPropagate(real_data);
    	
        for (int i=0; i<output.length; i++) {
        	System.out.print(output[i] + "  ");
        }
        System.out.println();
    	
        double prediction = NeuralNet.predict(output);
        if (Math.abs(prediction-real_data[real_data.length-1])<=0.01) {
        	System.out.printf(prediction + " is correct = " +real_data[real_data.length-1]);
        } else {
        	System.out.printf(prediction + " is incorrect != " +real_data[real_data.length-1]);
        }
    }
}
