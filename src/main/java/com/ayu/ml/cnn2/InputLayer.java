package com.ayu.ml.cnn2;


import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

import com.ayu.ml.cnn2.DataSet.Record;
import com.ayu.ml.cnn2.Layer;

/**
 * Created by shaoaq on 16-10-8.
 */
public class InputLayer implements Layer {
    private final Size size;
    /**
     * 姣忎釜杈撳叆涓轰竴涓煩褰�
     */
    private final double[][][] outs;
    private Layer nextLayer;

    public InputLayer(int batchSize, Size size) {
        this.size = size;
        this.outs = new double[batchSize][size.getWidth()][size.getHeight()];
    }

    //public void setOutput(int index, double[] values) {
    
    @Override
    public void setOutput(Record record) { 
    	
    	int index = record.getIndex(); 
    	double[] values = record.getData();
    	/*
        for (int x = 0; x < size.getWidth(); x++) {
            for (int y = 0; y < size.getHeight(); y++) {
                outs[index][x][y] = values[size.getWidth() * x + y];
            }
        }
        */
    	
        IntStream.range(0, size.getWidth()).parallel().forEach(x->{
        	IntStream.range(0, size.getHeight()).forEach(y->{
        		outs[index][x][y] = values[size.getWidth() * x + y];
        	});
        });
    }

    @Override
    public void forEachOutput(Record record, Function<Integer, Consumer<double[][]>> function) {
        function.apply(0).accept(outs[record.getIndex()]);
    }

    @Override
    public Size getSize() {
        return size;
    }

    @Override
    public int getOutCount() {
        return 1;
    }

    @Override
    public boolean setErrors(Record record) {
        //no need to set error to input layer
        return true;
    }

    @Override
    public void setNextLayer(Layer nextLayer) {
        this.nextLayer = nextLayer;
    }

    @Override
    public double[][] getError(Record record, int outIndex) {
        throw new RuntimeException("no error for input layer");
    }

    @Override
    public double[][] getOut(int recordIndex, int outIndex) {
        return outs[recordIndex];
    }

    @Override
    public void forward(DataSet.Record record) {
        setOutput(record);
    }

    @Override
    public boolean backPropagation(Record record) {
        return setErrors(record);
    }
}
