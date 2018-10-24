package com.ayu.ml.cnn2;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;

import com.ayu.ml.cnn2.DataSet.Record;

/**
 * Created by shaoaq on 16-10-8.
 */
public interface Layer extends Serializable {

	public Size getSize();

    public int getOutCount();
    
    public void setOutput(Record record);

    public void forEachOutput(Record record, Function<Integer, Consumer<double[][]>> function);

    public void setNextLayer(Layer layer);
    
    public boolean setErrors(Record record);

    public double[][] getError(Record record, int outIndex);

    public double[][] getOut(int recordIndex, int outIndex);

    public void forward(Record record);

    public boolean backPropagation(Record record);

}
